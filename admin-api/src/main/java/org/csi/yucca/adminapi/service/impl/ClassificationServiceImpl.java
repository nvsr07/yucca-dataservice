package org.csi.yucca.adminapi.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.BundlesMapper;
import org.csi.yucca.adminapi.mapper.DomainMapper;
import org.csi.yucca.adminapi.mapper.EcosystemMapper;
import org.csi.yucca.adminapi.mapper.LicenseMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SubdomainMapper;
import org.csi.yucca.adminapi.mapper.TagMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.mapper.UserMapper;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.model.Domain;
import org.csi.yucca.adminapi.model.Ecosystem;
import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.model.Tag;
import org.csi.yucca.adminapi.request.DomainRequest;
import org.csi.yucca.adminapi.request.EcosystemRequest;
import org.csi.yucca.adminapi.request.LicenseRequest;
import org.csi.yucca.adminapi.request.OrganizationRequest;
import org.csi.yucca.adminapi.request.SubdomainRequest;
import org.csi.yucca.adminapi.request.TagRequest;
import org.csi.yucca.adminapi.response.BackOfficeOrganizationResponse;
import org.csi.yucca.adminapi.response.BackofficeLoadDomainResponse;
import org.csi.yucca.adminapi.response.DomainResponse;
import org.csi.yucca.adminapi.response.EcosystemResponse;
import org.csi.yucca.adminapi.response.LicenseResponse;
import org.csi.yucca.adminapi.response.OrganizationResponse;
import org.csi.yucca.adminapi.response.PublicOrganizationResponse;
import org.csi.yucca.adminapi.response.SubdomainResponse;
import org.csi.yucca.adminapi.response.TagResponse;
import org.csi.yucca.adminapi.response.builder.OrganizationResponseBuilder;
import org.csi.yucca.adminapi.service.ClassificationService;
import org.csi.yucca.adminapi.service.SmartObjectService;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.Languages;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ClassificationServiceImpl implements ClassificationService{
	
	private static final Logger LOG = Logger.getLogger(ClassificationServiceImpl.class);
	
	@Autowired
	private DomainMapper domainMapper;
	
	@Autowired
	private EcosystemMapper ecosystemMapper;

	@Autowired
	private LicenseMapper licenseMapper;

	@Autowired
	private OrganizationMapper organizationMapper;
	
	@Autowired
	private TenantMapper tenantMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BundlesMapper bundlesMapper;
	
	@Autowired
	private SubdomainMapper subdomainMapper;

	@Autowired
	private TagMapper tagMapper;

	@Autowired
	private SmartObjectService smartObjectService;
	
	/**
	 * SELECT SUBDOMAIN
	 */
	public ServiceResponse selectSubdomain(Integer idSubdomain) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idSubdomain, "idSubdomain");

		Subdomain subdomain = subdomainMapper.selectSubdomainByIdSubdomain(idSubdomain);
		ServiceUtil.checkIfFoundRecord(subdomain);
		
		return ServiceResponse.build().object(new SubdomainResponse(subdomain));
	}

	
	/**
	 * SELECT ORGANIZATION
	 * 
	 * chiamato da backoffice
	 */
	@Override
	public ServiceResponse selectOrganization(Integer idOrganization) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idOrganization, "idOrganization");

		Organization organization = organizationMapper.selectOrganizationById(idOrganization);
		
		ServiceUtil.checkIfFoundRecord(organization);
		
		return ServiceResponse.build().object(new BackOfficeOrganizationResponse(organization));
	}

	@Override
	public ServiceResponse selectOrganization() throws BadRequestException, NotFoundException, Exception{

		List<Organization> listModel = organizationMapper.selectAllOrganization(null);

		List<OrganizationResponse> listResponse = new ArrayList<>();
		
		for (Organization model : listModel) {
			listResponse.add(new OrganizationResponseBuilder(model).build());
		}
		
		return ServiceUtil.buildResponse(listResponse); 
	}

	
	/**
	 * SELECT LICENSE
	 */
	public ServiceResponse selectLicense(Integer idLicense) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idLicense, "idLicense");

		License license = licenseMapper.selectLicenseById(idLicense);
		
		ServiceUtil.checkIfFoundRecord(license);
		
		return ServiceResponse.build().object(new LicenseResponse(license));
	}
	
	/**
	 * SELECT ECOSYSTEM
	 */
	public ServiceResponse selectEcosystem(Integer idEcosystem) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idEcosystem, "idEcosystem");

		Ecosystem ecosystem = ecosystemMapper.selectEcosystemById(idEcosystem);
		
		ServiceUtil.checkIfFoundRecord(ecosystem);
		
		return ServiceResponse.build().object(new EcosystemResponse(ecosystem));
	}
	
	/**
	 * SELECT TAG
	 */
	public ServiceResponse selectTag(Integer idTag) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idTag, "idTag");

		Tag tag = tagMapper.selectTagById(idTag);
		ServiceUtil.checkIfFoundRecord(tag);
		
		return ServiceResponse.build().object(new TagResponse(tag));
		
	}

	
	/**
	 * DELETE SUBDOMAIN
	 */
	public ServiceResponse deleteSubdomain(Integer idSubdomain) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(idSubdomain, "idSubdomain");
	
		int count = 0;
		try {
			count = subdomainMapper.deleteSubdomain(idSubdomain);
		} 		
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new ConflictException(Errors.INTEGRITY_VIOLATION, "Not possible to delete, dependency problems.");
		}
		
		if (count == 0 ) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}
		
		return ServiceResponse.build().NO_CONTENT();
		
	}
	
	/**
	 * UPDATE SUBDOMAIN
	 */
	public ServiceResponse updateSubdomain(SubdomainRequest subdomainRequest, Integer idSubdomain) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idSubdomain, "idSubdomain");
		ServiceUtil.checkMandatoryParameter(subdomainRequest,                    "subdomainRequest");
		ServiceUtil.checkMandatoryParameter(subdomainRequest.getLangEn(),        "langEn");
		ServiceUtil.checkMandatoryParameter(subdomainRequest.getLangIt(),        "langIt");
//		ServiceUtil.checkMandatoryParameter(subdomainRequest.getSubdomaincode(), "subdomaincode");
		ServiceUtil.checkCode(subdomainRequest.getSubdomaincode(), "subdomaincode");
		ServiceUtil.checkMandatoryParameter(subdomainRequest.getIdDomain(),      "idDomain");
		
		Subdomain subdomain = new Subdomain();
		BeanUtils.copyProperties(subdomainRequest, subdomain);
		subdomain.setIdSubdomain(idSubdomain);
		subdomain.setSubdomaincode(subdomainRequest.getSubdomaincode());
		subdomain.setLangIt(subdomainRequest.getLangIt());
		subdomain.setLangEn(subdomainRequest.getLangEn());
		subdomain.setDeprecated(Util.booleanToInt(subdomainRequest.getDeprecated()));
		subdomain.setIdDomain(subdomainRequest.getIdDomain());
		
		int count = subdomainMapper.updateSubdomain(subdomain);
		ServiceUtil.checkCount(count);
		
		return ServiceResponse.build().object(new SubdomainResponse(subdomain));
	}

	
	/**
	 * INSERT SUBDOMAIN
	 * 
	 * @param subdomainRequest
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse insertSubdomain(SubdomainRequest subdomainRequest) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(subdomainRequest, "subdomainRequest");
		ServiceUtil.checkMandatoryParameter(subdomainRequest.getLangEn(), "langEn"); 
		ServiceUtil.checkMandatoryParameter(subdomainRequest.getLangIt(), "langIt"); 
		ServiceUtil.checkCode(subdomainRequest.getSubdomaincode(), "subdomaincode"); 
//		ServiceUtil.checkMandatoryParameter(subdomainRequest.getSubdomaincode(), "subdomaincode"); 
		ServiceUtil.checkMandatoryParameter(subdomainRequest.getIdDomain(), "idDomain"); 
		
		Subdomain subdomain = new Subdomain();
		subdomain.setSubdomaincode(subdomainRequest.getSubdomaincode());
		subdomain.setLangIt(subdomainRequest.getLangIt());
		subdomain.setLangEn(subdomainRequest.getLangEn());
		subdomain.setDeprecated(Util.booleanToInt(subdomainRequest.getDeprecated()));
		subdomain.setIdDomain(subdomainRequest.getIdDomain());
		
		insertSubdomain(subdomain);
		
		return ServiceResponse.build().object(new SubdomainResponse(subdomain));
	}
	
	
	/**
	 * DELETE TAG
	 */
	public ServiceResponse deleteTag(Integer idTag) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(idTag, "idTag");
	
		int count = 0;
		try {
			count = tagMapper.deleteTag(idTag);
		} 		
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new ConflictException(Errors.INTEGRITY_VIOLATION, "Not possible to delete, dependency problems.");
		}
		
		if (count == 0 ) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}
		
		return ServiceResponse.build().NO_CONTENT();
		
	}
	
	/**
	 * UPDATE TAG
	 * 
	 * @param tagRequest
	 * @param idTag
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse updateTag(TagRequest tagRequest, Integer idTag) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(tagRequest, 			 "tagRequest");
//		ServiceUtil.checkMandatoryParameter(tagRequest.getTagcode(), "tagcode");
		ServiceUtil.checkCode(tagRequest.getTagcode(), "tagcode");
		ServiceUtil.checkMandatoryParameter(tagRequest.getLangen(),  "langen");
		ServiceUtil.checkMandatoryParameter(tagRequest.getLangit(),  "langit");
		ServiceUtil.checkMandatoryParameter(idTag,                   "idTag");
		
		Tag tag = new Tag(idTag, tagRequest.getTagcode(), tagRequest.getLangit(), tagRequest.getLangen(), tagRequest.getIdEcosystem() );
		int count = tagMapper.updateTag(tag);
		
		ServiceUtil.checkCount(count);
		
		if(tagRequest.getIdEcosystem() == null){
			tag = tagMapper.selectTagById(idTag);
		}
		
		return ServiceResponse.build().object(new TagResponse(tag));
	}

	
	/**
	 * INSERT TAG
	 */
	public ServiceResponse insertTag(TagRequest tagRequest) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(tagRequest, "tagRequest");
		
		ServiceUtil.checkMandatoryParameter(tagRequest.getLangen(), "langen"); 
		ServiceUtil.checkMandatoryParameter(tagRequest.getLangit(), "langit"); 
		ServiceUtil.checkCode(tagRequest.getTagcode(), "tagcode"); 
//		ServiceUtil.checkMandatoryParameter(tagRequest.getTagcode(), "tagcode"); 
		
		Tag tag = new Tag();
		BeanUtils.copyProperties(tagRequest, tag);

		insertTag(tag);
		
		return ServiceResponse.build().object(new TagResponse(tag));
	}

	
	
	/**
	 * DELETE ECOSYSTEM
	 */
	public ServiceResponse deleteLicense(Integer idLicense) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(idLicense, "idLicense");
	
		int count = 0;
		try {
			count = licenseMapper.deleteLicense(idLicense);
		} 		
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new ConflictException(Errors.INTEGRITY_VIOLATION, "Not possible to delete, dependency problems.");
		}
		
		if (count == 0 ) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}
		
		return ServiceResponse.build().NO_CONTENT();
		
	}
	
	/**
	 * UPDATE LICENSE
	 * 
	 * @param licenseRequest
	 * @param idLicense
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse updateLicense(LicenseRequest licenseRequest, Integer idLicense) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(licenseRequest, "licenseRequest");
		ServiceUtil.checkMandatoryParameter(licenseRequest.getDescription(), "description");
//		ServiceUtil.checkMandatoryParameter(licenseRequest.getLicensecode(), "licensecode");
		ServiceUtil.checkCode(licenseRequest.getLicensecode(), "licensecode");
		ServiceUtil.checkMandatoryParameter(idLicense, "idLicense");

		License license = new License(idLicense, licenseRequest.getLicensecode(), licenseRequest.getDescription());
		int count = licenseMapper.updateLicense(license);
		
		ServiceUtil.checkCount(count);
		
		return ServiceResponse.build().object(new LicenseResponse(license));
	}
	
	/**
	 * INSERT LICENSE
	 */
	public ServiceResponse insertLicense(LicenseRequest licenseRequest) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(licenseRequest, "licenseRequest");
		
		ServiceUtil.checkMandatoryParameter(licenseRequest.getDescription(), "description"); 
//		ServiceUtil.checkMandatoryParameter(licenseRequest.getLicensecode(), "licensecode"); 
		ServiceUtil.checkCode(licenseRequest.getLicensecode(), "licensecode"); 
		
		License license = new License();
		BeanUtils.copyProperties(licenseRequest, license);

		insertLicense(license);
		
		return ServiceResponse.build().object(new LicenseResponse(license));
	}
	
	/**
	 * UPDATE ORGANIZATION
	 */
	public ServiceResponse updateOrganization(OrganizationRequest organizationRequest, Integer idOrganization) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(organizationRequest, "organizationRequest");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getDescription(), "description");
//		ServiceUtil.checkMandatoryParameter(organizationRequest.getOrganizationcode(), "organizationcode");
		ServiceUtil.checkCode(organizationRequest.getOrganizationcode(), "organizationcode");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getEcosystemCodeList(), "ecosystemCodeList");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getEcosystemCodeList().isEmpty(), "ecosystemCodeList");
		ServiceUtil.checkMandatoryParameter(idOrganization, "idOrganization");

		organizationMapper.deleteEcosystemOrganization(idOrganization);
		
		insertEcosystemOrganization(organizationRequest.getEcosystemCodeList(), idOrganization);
		
		Organization organization = new Organization(idOrganization, organizationRequest.getOrganizationcode(), organizationRequest.getDescription());
		organizationMapper.updateOrganization(organization);
		
		return ServiceResponse.build().object(new BackOfficeOrganizationResponse(organization));
	}	
	
	/**
	 * DELETE ORGANOZATION
	 */
	public ServiceResponse deleteOrganization(Integer idOrganization) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idOrganization, "idOrganization");
		
		organizationMapper.deleteEcosystemOrganization(idOrganization);
		
		int count = 0;
		
		try {
			
			deleteBundlesByOrganization(idOrganization);			
			deleteUserByIdOrganization(idOrganization);			

			smartObjectService.deleteInternalSmartObject(idOrganization);			
			tenantMapper.deleteTenantByIdOrganization(idOrganization);
		
			count = organizationMapper.deleteOrganization(idOrganization);
		} 		
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new ConflictException(Errors.INTEGRITY_VIOLATION, "Not possible to delete, dependency problems." + dataIntegrityViolationException.getRootCause());
		}
		
		if (count == 0 ) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}
		
		return ServiceResponse.build().NO_CONTENT();
	}
	
	/**
	 * DELETE ECOSYSTEM
	 */
	public ServiceResponse deleteEcosystem(Integer idEcosystem) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(idEcosystem, "idEcosystem");
	
		int count = 0;
		try {
			count = ecosystemMapper.deleteEcosystem(idEcosystem);
		} 		
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new ConflictException(Errors.INTEGRITY_VIOLATION, "Not possible to delete, dependency problems.");
		}
		
		if (count == 0 ) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}
		
		return ServiceResponse.build().NO_CONTENT();
		
	}
	
	/**
	 * 
	 * @param ecosystemRequest
	 * @param idEcosystem
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse updateEcosystem(EcosystemRequest ecosystemRequest, Integer idEcosystem) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(ecosystemRequest, "ecosystemRequest");
		ServiceUtil.checkMandatoryParameter(ecosystemRequest.getDescription(), "description");
//		ServiceUtil.checkMandatoryParameter(ecosystemRequest.getEcosystemcode(), "ecosystemcode");
		ServiceUtil.checkCode(ecosystemRequest.getEcosystemcode(), "ecosystemcode");
		ServiceUtil.checkMandatoryParameter(idEcosystem, "idEcosystem");

		Ecosystem ecosystem  = new Ecosystem(idEcosystem,ecosystemRequest.getEcosystemcode(), ecosystemRequest.getDescription() );
		int count = ecosystemMapper.updateEcosystem(ecosystem);
		
		ServiceUtil.checkCount(count);
		
		return ServiceResponse.build().object(new EcosystemResponse(ecosystem));
	}
	
	/**
	 * 
	 * @param ecosystemRequest
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse insertEcosystem(EcosystemRequest ecosystemRequest) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(ecosystemRequest, "ecosystemRequest");
		
		ServiceUtil.checkMandatoryParameter(ecosystemRequest.getDescription(), "description"); 
//		ServiceUtil.checkMandatoryParameter(ecosystemRequest.getEcosystemcode(), "ecosystemcode"); 
		ServiceUtil.checkCode(ecosystemRequest.getEcosystemcode(), "ecosystemcode"); 
		
		Ecosystem  ecosystem = new Ecosystem();
		BeanUtils.copyProperties(ecosystemRequest, ecosystem);
		
		insertEcosystem(ecosystem);
		
		return ServiceResponse.build().object(new EcosystemResponse(ecosystem));
	}
	
	/**
	 * SELECT TAG
	 * 
	 * @param lang
	 * @param sort
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectTag(String lang, String sort, String ecosystemCode) throws BadRequestException, NotFoundException, Exception{

		ServiceUtil.checkMandatoryParameter(ecosystemCode, "ecosystemCode");
		
		List<Tag> modelList = null;
		
		List<String> sortList = ServiceUtil.getSortList(sort, Tag.class);
		
		if(lang==null || lang.isEmpty()){
			modelList =  tagMapper.selectTagAllLanguage(sortList, ecosystemCode);
		}
		else if(Languages.IT.value().equals(lang)){
			modelList =  tagMapper.selectTagITLanguage(sortList, ecosystemCode);
		}
		else if(Languages.EN.value().equals(lang)){
			modelList =  tagMapper.selectTagENLanguage(sortList, ecosystemCode);
		}
		else{
			throw new BadRequestException(Errors.LANGUAGE_NOT_SUPPORTED, lang);
		}

		ServiceUtil.checkList(modelList);
		
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, TagResponse.class));
		
	}	
	
    /**
     * 	SELECT SUBDOMAIN
     */
	public ServiceResponse selectSubdomain(String domainCode, String lang, String sort) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(domainCode, "domainCode");
		
		List<Subdomain> subdomainList = null;
		
		List<String> sortList = ServiceUtil.getSortList(sort, Subdomain.class);
		
		if(lang==null || lang.isEmpty()){
			subdomainList =  subdomainMapper.selectSubdomainAllLanguage(domainCode, sortList);
		}
		else if(Languages.IT.value().equals(lang)){
			subdomainList =  subdomainMapper.selectSubdomainITLanguage(domainCode, sortList);
		}
		else if(Languages.EN.value().equals(lang)){
			subdomainList =  subdomainMapper.selectSubdomainENLanguage(domainCode, sortList);
		}
		else{
			throw new BadRequestException(Errors.LANGUAGE_NOT_SUPPORTED, lang);
		}

		ServiceUtil.checkList(subdomainList);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(subdomainList, SubdomainResponse.class));
		
	}
	
	
	/**
	 * SELECT ORGANIZATIONS
	 * 
	 * chiamato da public.
	 * 
	 * @param ecosystemCode
	 * @param sort
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectOrganization(String ecosystemCode, String sort) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(ecosystemCode, "ecosystemCode");
		
		List<String> sortList = ServiceUtil.getSortList(sort, Organization.class);
		
		List<Organization> organizationList = organizationMapper.selectOrganization(ecosystemCode, sortList);
		
		ServiceUtil.checkList(organizationList);
		
		return ServiceResponse.build().object(ServiceUtil.getResponseList(organizationList, PublicOrganizationResponse.class));
		
	}		

	
	/**
	 * SELECT LICENSE
	 */
	@Override
	public ServiceResponse selectLicense(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, License.class);
		
		List<License> licenseList = licenseMapper.selectLicense(sortList);
		
		ServiceUtil.checkList(licenseList);
		
		return ServiceResponse.build().object(ServiceUtil.getResponseList(licenseList, LicenseResponse.class));
	}	
	
	/**
	 * SELECT ECOSYSTEM
	 */
	public ServiceResponse selectEcosystem(String organizationCode, String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, Ecosystem.class);
		
		List<Ecosystem> ecosystemList = null;

		if (organizationCode == null) {
			ecosystemList = selectAllEcosystem(sortList);
		}
		else{
			ecosystemList = selectEcosystemByOrganizationCode(organizationCode, sortList);
		}
		
		ServiceUtil.checkList(ecosystemList);
		
		return ServiceResponse.build().object(ServiceUtil.getResponseList(ecosystemList, EcosystemResponse.class));
		
	}	
	
	/**
	 * UPDATE DOMAIN
	 */
	public ServiceResponse updateDomain(DomainRequest domainRequest, Integer idDomain) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(domainRequest, "domainRequest");
//		ServiceUtil.checkMandatoryParameter(domainRequest.getDomaincode(), "domaincode");
		ServiceUtil.checkCode(domainRequest.getDomaincode(), "domaincode");
		
		ServiceUtil.checkMandatoryParameter(domainRequest.getEcosystemCodeList(), "ecosystemCodeList");
		ServiceUtil.checkMandatoryParameter(idDomain, "idDomain");
		ServiceUtil.checkMandatoryParameter(domainRequest.getLangen(), "langen");
		ServiceUtil.checkMandatoryParameter(domainRequest.getLangit(), "langit");
		ServiceUtil.checkNullInteger(domainRequest, "deprecated");
		
		domainMapper.deleteEcosystemDomain(idDomain);
		
		insertEcosystemDomain(domainRequest.getEcosystemCodeList(), idDomain);
		
		domainMapper.updateDomain(idDomain, domainRequest.getDomaincode(), domainRequest.getLangit(), 
				domainRequest.getLangen(), Util.booleanToInt(domainRequest.getDeprecated()));
		
		return ServiceResponse.build().object(domainRequest);
	}
	
	
	/**
	 * DELETE DOMAIN
	 */
	public ServiceResponse deleteDomain(Integer idDomain) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(idDomain, "idDomain");
		
		domainMapper.deleteEcosystemDomain(idDomain);
	
		int count = 0;
		try {
			count = domainMapper.deleteDomain(idDomain);
		} 		
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new ConflictException(Errors.INTEGRITY_VIOLATION, "Not possible to delete, dependency problems.");
		}
		
		if (count == 0 ) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}
		
		return ServiceResponse.build().NO_CONTENT();
		
	}
	
	/**
	 *	INSERT ORGANIZATION 
	 */
	@Override
	public ServiceResponse insertOrganization(OrganizationRequest organizationRequest) throws BadRequestException, NotFoundException, Exception{
		
		LOG.info("insertOrganization ==> BEGIN");
		
		LOG.debug("organizationRequest.getIdOrganization(): " + organizationRequest.getIdOrganization());
		
		ServiceUtil.checkMandatoryParameter(organizationRequest, "organizationRequest");

		ServiceUtil.checkMandatoryParameter(organizationRequest.getDatasolrcollectionname(), "Datasolrcollectionname");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getMeasuresolrcollectionname(), "Measuresolrcollectionname");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getMediasolrcollectionname(), "Mediasolrcollectionname");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getSocialsolrcollectionname(), "Socialsolrcollectionname");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getDataphoenixtablename(), "Dataphoenixtablename");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getDataphoenixschemaname(), "Dataphoenixschemaname");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getMeasuresphoenixtablename(), "Measuresphoenixtablename");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getMeasuresphoenixschemaname(), "Measuresphoenixschemaname");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getMediaphoenixtablename(), "Mediaphoenixtablename");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getMediaphoenixschemaname(), "Mediaphoenixschemaname");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getSocialphoenixtablename(), "Socialphoenixtablename");
		ServiceUtil.checkMandatoryParameter(organizationRequest.getSocialphoenixschemaname(), "Socialphoenixschemaname");				
		
		ServiceUtil.checkMandatoryParameter(organizationRequest.getDescription(), "description"); 
		ServiceUtil.checkCode(organizationRequest.getOrganizationcode(), "organizationcode"); 
		ServiceUtil.checkMandatoryParameter(organizationRequest.getEcosystemCodeList(), "ecosystemCodeList"); 
		ServiceUtil.checkMandatoryParameter(organizationRequest.getEcosystemCodeList().isEmpty(), "ecosystemCodeList"); 
		
		LOG.debug("VALIDATION OK!");
		
		Organization organization = new Organization();
		BeanUtils.copyProperties(organizationRequest, organization);

		insertOrganization(organization, organizationRequest.getEcosystemCodeList());
		
		LOG.debug("INSERT OK!");
		
		LOG.info("insertOrganization ==> END");
		
		return ServiceResponse.build().object(new BackOfficeOrganizationResponse(organization));
	}

	public void insertOrganization(Organization organization, Integer idEcosystemCode)throws BadRequestException{
		insertOrganization(organization, Arrays.asList(idEcosystemCode));
	}
	
	public void insertOrganization(Organization organization, List<Integer> ecosystemCodeList)throws BadRequestException{
		insertOrganization(organization);
		insertEcosystemOrganization(ecosystemCodeList, organization.getIdOrganization());
		smartObjectService.insertInternalSmartObject(organization);
	}
	
	/**
	 *	INSERT DOMAIN 
	 */
	public ServiceResponse insertDomain(DomainRequest domainRequest) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(domainRequest, "domainRequest");
		
		ServiceUtil.checkMandatoryParameter(domainRequest.getDeprecated(), "deprecated"); 
//		ServiceUtil.checkMandatoryParameter(domainRequest.getDomaincode(), "domaincode"); 
		ServiceUtil.checkCode(domainRequest.getDomaincode(), "domaincode"); 
		ServiceUtil.checkMandatoryParameter(domainRequest.getEcosystemCodeList(), "ecosystemCodeList"); 
		ServiceUtil.checkMandatoryParameter(domainRequest.getEcosystemCodeList().isEmpty(), "ecosystemCodeList"); 
		ServiceUtil.checkMandatoryParameter(domainRequest.getLangen(), "langen"); 
		ServiceUtil.checkMandatoryParameter(domainRequest.getLangit(), "langit"); 
		
		Domain domain = new Domain();
		BeanUtils.copyProperties(domainRequest, domain);
		
		insertDomain(domain);
		insertEcosystemDomain(domainRequest.getEcosystemCodeList(), domain.getIdDomain());
		
		return ServiceResponse.build().object(new DomainResponse(domain));
	}

	/**
	 * 
	 * @param idDomain
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectDomain(Integer idDomain) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(idDomain, "idDomain");

		Domain domain = domainMapper.selectDomain(idDomain);
		
		ServiceUtil.checkIfFoundRecord(domain);
		
		List<Subdomain> subdomains = subdomainMapper.selectSubdomain(idDomain);
		
		return ServiceResponse.build().object(new BackofficeLoadDomainResponse(domain, subdomains));
		
	}

	
	/**
	 * 
	 */
	public ServiceResponse selectDomain(String ecosystemCode, String lang, String sort) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(ecosystemCode, "ecosystemCode");
		
		List<Domain> domainList = null;
		
		List<String> sortList = ServiceUtil.getSortList(sort, Domain.class);
		
		if(lang==null || lang.isEmpty()){
			domainList =  domainMapper.selectDomainAllLanguage(ecosystemCode, sortList);
		}
		else if(Languages.IT.value().equals(lang)){
			domainList =  domainMapper.selectDomainITLanguage(ecosystemCode, sortList);
		}
		else if(Languages.EN.value().equals(lang)){
			domainList =  domainMapper.selectDomainENLanguage(ecosystemCode, sortList);
		}
		else{
			throw new BadRequestException(Errors.LANGUAGE_NOT_SUPPORTED, lang);
		}

		ServiceUtil.checkList(domainList);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(domainList, DomainResponse.class));
		
	}
	
	
	/***************************************************************
	 * 
	 * 						private methods
	 * 
	 ***************************************************************/
	
	private void insertEcosystem(Ecosystem ecosystem)throws BadRequestException{
		
		try {
			ecosystemMapper.insertEcosystem(ecosystem);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			// se passo un ecosystemcode gia inserito
			throw new BadRequestException(Errors.DUPLICATE_KEY, "ecosystemcode");
		}
	}

	private void insertSubdomain(Subdomain subdomain)throws BadRequestException{
		
		try {
			subdomainMapper.insertSubdomain(subdomain);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			// se passo un subdomaincode gia inserito
			throw new BadRequestException(Errors.DUPLICATE_KEY, "subdomaincode");
		}
	}

	private void insertLicense(License license)throws BadRequestException{
		
		try {
			licenseMapper.insertLicense(license);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			// se passo un licensecode gia inserito
			throw new BadRequestException(Errors.DUPLICATE_KEY, "licensecode");
		}
	}
	
	private void insertTag(Tag tag)throws BadRequestException{
		
		try {
			tagMapper.insertTag(tag);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			// se passo un licensecode gia inserito
			throw new BadRequestException(Errors.DUPLICATE_KEY);
		}
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			// se passo un ecosystem inesistente
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION, "idEcosystem not present.");
		}
	}
	
	
	private List<Ecosystem> selectAllEcosystem(List<String> sortList){
		return ecosystemMapper.selectAllEcosystem(0,sortList);
	}

	private List<Ecosystem> selectEcosystemByOrganizationCode(String organizationCode, List<String> sortList){
		return ecosystemMapper.selectEcosystem(organizationCode, sortList);
	}

	private void insertDomain(Domain domain)throws BadRequestException{
		
		try {
			domainMapper.insertDomain(domain);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			
			// se passo un domaincode gia inserito .gjhgjhg
			throw new BadRequestException(Errors.DUPLICATE_KEY, "domaincode: " + duplicateKeyException.getRootCause().getMessage());
		}
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			// se passo un ecosystem inesistente
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION, "idEcosystem not present.");
		}
		
	}

	private void insertOrganization(Organization organization)throws BadRequestException{
		
		try {
			organizationMapper.insertOrganization(organization);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			// se passo un domaincode gia inserito
			throw new BadRequestException(Errors.DUPLICATE_KEY, "organizationcode");
		}
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			// se passo un ecosystem inesistente
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION, "idEcosystem not present.");
		}
		
	}
	
	private void insertEcosystemDomain(List<Integer> ecosystemCodeList, Integer idDomain)throws BadRequestException{
		
		try {
			for (Integer idEcosystem : ecosystemCodeList) {
				domainMapper.insertEcosystemDomain(idEcosystem, idDomain);
			}
		} 
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			// se passo un ecosystem on un idDomain inesistente
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION, dataIntegrityViolationException.getRootCause().getMessage());
		}
		
	}
	
	
	private void insertEcosystemOrganization(List<Integer> ecosystemCodeList, Integer idOrganization)throws BadRequestException{
		
		try {
			for (Integer idEcosystem : ecosystemCodeList) {
				organizationMapper.insertEcosystemOrganization(idEcosystem, idOrganization);
			}
		} 
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			// se passo un ecosystem inesistente
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION, dataIntegrityViolationException.getRootCause().getMessage());
		}
		
	}

	private void deleteBundlesByOrganization(Integer idOrganization){
		List<Bundles> bundles =  bundlesMapper.selectBundlesByOrganization(idOrganization);
		for (Bundles bundle : bundles) {
			tenantMapper.deleteTenantBundlesByBundles(bundle.getIdBundles());
			bundlesMapper.deleteBundles(bundle.getIdBundles());
		}
	}
	
	private void deleteUserByIdOrganization(Integer idOrganization){
		userMapper.deleteTenantUserByIdOrganization(idOrganization);
		userMapper.deleteUserByIdOrganization(idOrganization);
	}	
	
	

	
}
