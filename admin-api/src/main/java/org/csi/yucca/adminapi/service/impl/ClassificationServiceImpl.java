package org.csi.yucca.adminapi.service.impl;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.DomainMapper;
import org.csi.yucca.adminapi.mapper.EcosystemMapper;
import org.csi.yucca.adminapi.mapper.LicenseMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SubdomainMapper;
import org.csi.yucca.adminapi.mapper.TagMapper;
import org.csi.yucca.adminapi.model.Domain;
import org.csi.yucca.adminapi.model.Ecosystem;
import org.csi.yucca.adminapi.model.License;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.model.Tag;
import org.csi.yucca.adminapi.request.DomainRequest;
import org.csi.yucca.adminapi.response.DomainResponse;
import org.csi.yucca.adminapi.response.EcosystemResponse;
import org.csi.yucca.adminapi.response.LicenseResponse;
import org.csi.yucca.adminapi.response.OrganizationResponse;
import org.csi.yucca.adminapi.response.SubdomainResponse;
import org.csi.yucca.adminapi.response.TagResponse;
import org.csi.yucca.adminapi.service.ClassificationService;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.Languages;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
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
	
	@Autowired
	private DomainMapper domainMapper;
	
	@Autowired
	private EcosystemMapper ecosystemMapper;

	@Autowired
	private LicenseMapper licenseMapper;

	@Autowired
	private OrganizationMapper organizationMapper;
	
	@Autowired
	private SubdomainMapper subdomainMapper;

	@Autowired
	private TagMapper tagMapper;

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
	public ServiceResponse selectTag(String lang, String sort, Integer ecosystemCode) throws BadRequestException, NotFoundException, Exception{

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
			throw new BadRequestException(Errors.LANGUAGE_NOT_SUPPORTED.arg(lang));
		}

		ServiceUtil.checkList(modelList);
		
		return ServiceResponse.build().object(ServiceUtil.getResponseList(modelList, TagResponse.class));
		
	}	
	
    /**
     * 	SELECT SUBDOMAIN
     */
	public ServiceResponse selectSubdomain(Integer domainCode, String lang, String sort) throws BadRequestException, NotFoundException, Exception{
		
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
			throw new BadRequestException(Errors.LANGUAGE_NOT_SUPPORTED.arg(lang));
		}

		ServiceUtil.checkList(subdomainList);
//		return ServiceUtil.getResponseList(subdomainList, SubdomainResponse.class);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(subdomainList, SubdomainResponse.class));
		
	}
	
	
	/**
	 * SELECT ORGANIZATIONS
	 * 
	 * @param ecosystemCode
	 * @param sort
	 * @return
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public ServiceResponse selectOrganization(Integer ecosystemCode, String sort) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(ecosystemCode, "ecosystemCode");
		
		List<String> sortList = ServiceUtil.getSortList(sort, Organization.class);
		
		List<Organization> organizationList = organizationMapper.selectOrganization(ecosystemCode, sortList);
		
		ServiceUtil.checkList(organizationList);
		
		return ServiceResponse.build().object(ServiceUtil.getResponseList(organizationList, OrganizationResponse.class));
		
	}		

	
	/**
	 * SELECT LICENSE
	 */
	public ServiceResponse selectLicense(String sort) throws BadRequestException, NotFoundException, Exception{
		
		List<String> sortList = ServiceUtil.getSortList(sort, License.class);
		
		List<License> licenseList = licenseMapper.selectLicense(sortList);
		
		ServiceUtil.checkList(licenseList);
		
		return ServiceResponse.build().object(ServiceUtil.getResponseList(licenseList, LicenseResponse.class));
	}	
	
	/**
	 * SELECT ECOSYSTEM
	 */
	public ServiceResponse selectEcosystem(Integer organizationCode, String sort) throws BadRequestException, NotFoundException, Exception{
		
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
		ServiceUtil.checkMandatoryParameter(domainRequest.getDomaincode(), "domaincode");
		ServiceUtil.checkMandatoryParameter(domainRequest.getEcosystemCodeList(), "ecosystemCodeList");
		ServiceUtil.checkMandatoryParameter(idDomain, "idDomain");
		ServiceUtil.checkMandatoryParameter(domainRequest.getLangen(), "langen");
		ServiceUtil.checkMandatoryParameter(domainRequest.getLangit(), "langit");
		ServiceUtil.checkNullInteger(domainRequest, "deprecated");
		
		domainMapper.deleteEcosystemDomain(idDomain);
		
		insertEcosystemDomain(domainRequest.getEcosystemCodeList(), idDomain);

		domainMapper.updateDomain(idDomain, domainRequest.getDomaincode(), domainRequest.getLangit(), 
				domainRequest.getLangen(), domainRequest.getDeprecated());
		
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
			throw new ConflictException(Errors.INTEGRITY_VIOLATION.arg("Not possible to delete, dependency problems."));
		}
		
		if (count == 0 ) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}
		
		return ServiceResponse.build().NO_CONTENT();
		
	}
	
	/**
	 * 
	 */
	public ServiceResponse insertDomain(DomainRequest domainRequest) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(domainRequest, "domainRequest");
		
		ServiceUtil.checkMandatoryParameter(domainRequest.getDeprecated(), "deprecated"); 
		ServiceUtil.checkMandatoryParameter(domainRequest.getDomaincode(), "domaincode"); 
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
	 */
	public ServiceResponse selectDomain(Integer ecosystemCode, String lang, String sort) throws BadRequestException, NotFoundException, Exception{
		
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
			throw new BadRequestException(Errors.LANGUAGE_NOT_SUPPORTED.arg(lang));
		}

		ServiceUtil.checkList(domainList);
		return ServiceResponse.build().object(ServiceUtil.getResponseList(domainList, DomainResponse.class));
		
	}
	
	
	/***************************************************************
	 * 
	 * 						private methods
	 * 
	 ***************************************************************/
	
	
	private List<Ecosystem> selectAllEcosystem(List<String> sortList){
		return ecosystemMapper.selectAllEcosystem(0,sortList);
	}

	private List<Ecosystem> selectEcosystemByOrganizationCode(Integer organizationCode, List<String> sortList){
		return ecosystemMapper.selectEcosystem(organizationCode, sortList);
	}

	private void insertDomain(Domain domain)throws BadRequestException{
		
		try {
			domainMapper.insertDomain(domain);
		} 
		catch (DuplicateKeyException duplicateKeyException) {
			// se passo un domaincode gia inserito
			throw new BadRequestException(Errors.DUPLICATE_KEY.arg("domaincode"));
		}
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			// se passo un ecosystem inesistente
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION.arg("idEcosystem not present."));
		}
		
	}
	
	private void insertEcosystemDomain(List<Integer> ecosystemCodeList, Integer idDomain)throws BadRequestException{
		
		try {
			for (Integer idEcosystem : ecosystemCodeList) {
				domainMapper.insertEcosystemDomain(idEcosystem, idDomain);
			}
		} 
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			// se passo un ecosystem inesistente
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION.arg("idEcosystem not present."));
		}
		
		
	}
	
}
