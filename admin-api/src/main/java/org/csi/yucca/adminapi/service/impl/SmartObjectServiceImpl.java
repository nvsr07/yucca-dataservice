package org.csi.yucca.adminapi.service.impl;

import static org.csi.yucca.adminapi.util.ServiceUtil.checkCode;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkList;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkMandatoryParameter;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkWhitespace;
import static org.csi.yucca.adminapi.util.ServiceUtil.getResponseList;
import static org.csi.yucca.adminapi.util.ServiceUtil.getSortList;
import static org.csi.yucca.adminapi.util.ServiceUtil.isType;

import java.sql.Timestamp;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.ExposureTypeMapper;
import org.csi.yucca.adminapi.mapper.LocationTypeMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SmartobjectMapper;
import org.csi.yucca.adminapi.mapper.SoCategoryMapper;
import org.csi.yucca.adminapi.mapper.SoPositionMapper;
import org.csi.yucca.adminapi.mapper.SoTypeMapper;
import org.csi.yucca.adminapi.mapper.SupplyTypeMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.model.ExposureType;
import org.csi.yucca.adminapi.model.LocationType;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.SoCategory;
import org.csi.yucca.adminapi.model.SoPosition;
import org.csi.yucca.adminapi.model.SoType;
import org.csi.yucca.adminapi.model.SupplyType;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
import org.csi.yucca.adminapi.request.SoPositionRequest;
import org.csi.yucca.adminapi.response.ExposureTypeResponse;
import org.csi.yucca.adminapi.response.LocationTypeResponse;
import org.csi.yucca.adminapi.response.SmartobjectResponse;
import org.csi.yucca.adminapi.response.SoCategoryResponse;
import org.csi.yucca.adminapi.response.SoTypeResponse;
import org.csi.yucca.adminapi.response.SupplyTypeResponse;
import org.csi.yucca.adminapi.service.SmartObjectService;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.Type;
import org.springframework.beans.BeanUtils;
//import org.csi.yucca.adminapi.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SmartObjectServiceImpl implements SmartObjectService {

	@Autowired
	private ExposureTypeMapper exposureTypeMapper;

	@Autowired
	private LocationTypeMapper locationTypeMapper;

	@Autowired
	private SoCategoryMapper soCategoryMapper;

	@Autowired
	private SoTypeMapper soTypeMapper;

	@Autowired
	private SupplyTypeMapper supplyTypeMapper;

	@Autowired
	private TenantMapper tenantMapper;

	@Autowired
	private SmartobjectMapper smartobjectMapper;

	@Autowired
	private OrganizationMapper organizationMapper;

	@Autowired
	private SoPositionMapper soPositionMapper;

	public ServiceResponse updateSmartobject(SmartobjectRequest smartobjectRequest, Integer organizationCode,
			String soCode) throws BadRequestException, NotFoundException, Exception {

		return null;
	}

	/**
	 * 
	 * DELETE SMARTOBJECT: da sistemare!!!!!! da eccezione se viene passato un
	 * socode inseistente
	 * 
	 *
	 * 
	 */
	public ServiceResponse deleteSmartObject(String organizationCode, String socode)
			throws BadRequestException, NotFoundException, Exception {

		ServiceUtil.checkMandatoryParameter(organizationCode, "organizationCode");
		ServiceUtil.checkMandatoryParameter(socode, "soCode");

		Organization organization = getOrganization(organizationCode);

		Smartobject smartobject = smartobjectMapper.selectSmartobject(socode, organization.getIdOrganization());

		if (isType(Type.INTERNAL, smartobject)) {
			throw new BadRequestException(
					Errors.INCORRECT_VALUE.arg("idSoType di tipo: " + Type.INTERNAL.code() + " delete denied."));
		}

		smartobjectMapper.deleteTenantSmartobject(smartobject.getIdSmartObject());

		int count = 0;
		try {
			count = smartobjectMapper.deleteSmartobject(socode, organization.getIdOrganization());
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new ConflictException(Errors.INTEGRITY_VIOLATION.arg("Not possible to delete, dependency problems."));
		}

		if (count == 0) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}

		return ServiceResponse.build().NO_CONTENT();

	}

	private Organization getOrganization(String organizationCode) throws NotFoundException {
		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode);
		ServiceUtil.checkIfFoundRecord(organization, "organizationCode => " + organizationCode + " not found.");
		return organization;
	}

	
	/**
	 * INSERT SMART OBJECT
	 */
	public ServiceResponse insertSmartobject(SmartobjectRequest smartobjectRequest, String organizationCode)
			throws BadRequestException, NotFoundException, Exception {
		
		// recuera l'organizatione con il code passato:
		Organization organization = getOrganization(organizationCode);

		validation(smartobjectRequest, organization.getIdOrganization());

		Timestamp now = new Timestamp(System.currentTimeMillis());

		//		inserimento so
		Smartobject smartobject = insertSmartObject(smartobjectRequest, organization.getIdOrganization(), now);
		smartobjectMapper.insertTenantSmartobject(smartobjectRequest.getIdTenant(), smartobject.getIdSmartObject(), now);

		//		inserimento ventuali positions
		SoPositionRequest soPositionRequest = smartobjectRequest.getPosition();
		if(soPositionRequest != null){
			soPositionRequest.setIdSmartObject(smartobject.getIdSmartObject());
			insertSoPosition(soPositionRequest);
		}
		
		return ServiceResponse.build().object(new SmartobjectResponse(smartobject, smartobjectRequest.getPosition()));
	}

	
	
	private void checkOrganizationTenant(Integer soIdOrganization, Integer soIdTenant)
			throws NotFoundException, BadRequestException {
		Integer idOrganization = tenantMapper.selectIdOrganizationByIdTenant(soIdTenant);
		if (idOrganization == null) {
			throw new NotFoundException(Errors.RECORD_NOT_FOUND.arg("idTenant [" + soIdTenant + "]"));
		}
		if (idOrganization != soIdOrganization) {
			throw new BadRequestException(Errors.NOT_CONSISTENT_DATA.arg(
					"tenant with id " + soIdTenant + " does not belong to organozation with id " + soIdOrganization));
		}
	}

	/**
	 * 
	 * @param smartobjectRequest
	 * @throws BadRequestException
	 */
	private void checkTweet(SmartobjectRequest smartobjectRequest) throws BadRequestException {
		boolean justOneTweetInfo = smartobjectRequest.getTwtusername() != null
				|| smartobjectRequest.getTwtmaxsearchnumber() != null
				|| smartobjectRequest.getTwtmaxsearchinterval() != null || smartobjectRequest.getTwtusertoken() != null
				|| smartobjectRequest.getTwttokensecret() != null || smartobjectRequest.getTwtname() != null
				|| smartobjectRequest.getTwtuserid() != null || smartobjectRequest.getTwtmaxstreams() != null;

		if (!isType(Type.FEED_TWEET, smartobjectRequest) && justOneTweetInfo) {
			throw new BadRequestException(Errors.NO_TWEET_SO_TYPE);
		}

		if (isType(Type.FEED_TWEET, smartobjectRequest)) {
			checkMandatoryParameter(smartobjectRequest.getTwtmaxstreams(), "twtmaxstreams");
			checkMandatoryParameter(smartobjectRequest.getTwtusername(), "twtusername");
			checkMandatoryParameter(smartobjectRequest.getTwtusertoken(), "twtusertoken");
			checkMandatoryParameter(smartobjectRequest.getTwttokensecret(), "twttokensecret");			
			// da verificare se devono essere mandatari BEGIN
			checkMandatoryParameter(smartobjectRequest.getTwtmaxsearchnumber(), "twtmaxsearchnumber");
			checkMandatoryParameter(smartobjectRequest.getTwtname(), "twtname");
			checkMandatoryParameter(smartobjectRequest.getTwtmaxsearchinterval(), "twtmaxsearchinterval");
			checkMandatoryParameter(smartobjectRequest.getTwtuserid(), "twtuserid");
			// da verificare se devono essere mandatari END
		}

	}

	public ServiceResponse selectSupplyType(String sort) throws BadRequestException, NotFoundException, Exception {

		List<String> sortList = getSortList(sort, SupplyType.class);

		List<SupplyType> modelList = supplyTypeMapper.selectSupplyType(sortList);

		checkList(modelList);

		return ServiceResponse.build().object(getResponseList(modelList, SupplyTypeResponse.class));

	}

	public ServiceResponse selectSoType(String sort) throws BadRequestException, NotFoundException, Exception {

		List<String> sortList = getSortList(sort, SoType.class);

		List<SoType> modelList = soTypeMapper.selectSoType(sortList);

		checkList(modelList);

		return ServiceResponse.build().object(getResponseList(modelList, SoTypeResponse.class));
	}

	public ServiceResponse selectSoCategory(String sort) throws BadRequestException, NotFoundException, Exception {

		List<String> sortList = getSortList(sort, SoCategory.class);

		List<SoCategory> modelList = soCategoryMapper.selectSoCategory(sortList);

		checkList(modelList);

		return ServiceResponse.build().object(getResponseList(modelList, SoCategoryResponse.class));
	}

	public ServiceResponse selectExposureType(String sort) throws BadRequestException, NotFoundException, Exception {

		List<String> sortList = getSortList(sort, ExposureType.class);

		List<ExposureType> modelList = exposureTypeMapper.selectExposureType(sortList);

		checkList(modelList);

		return ServiceResponse.build().object(getResponseList(modelList, ExposureTypeResponse.class));

	}

	public ServiceResponse selectLocationType(String sort) throws BadRequestException, NotFoundException, Exception {

		List<String> sortList = getSortList(sort, LocationType.class);

		List<LocationType> modelList = locationTypeMapper.selectLocationType(sortList);

		checkList(modelList);

		return ServiceResponse.build().object(getResponseList(modelList, LocationTypeResponse.class));

	}

	private void checkSmartObject(Integer idOrganization, String soCode, String slug)
			throws NotFoundException, BadRequestException {
		Smartobject smartobject = smartobjectMapper.selectSmartobject(soCode, idOrganization);

		if (smartobject != null && smartobject.getIdSmartObject() != null) {
			throw new BadRequestException(
					Errors.DUPLICATE_KEY.arg("socode: " + soCode + ", idOrganization: " + idOrganization));
		}

		smartobject = smartobjectMapper.selectSmartobjectBySlugAndOrganization(slug, idOrganization);

		if (smartobject != null && smartobject.getIdSmartObject() != null) {
			throw new BadRequestException(
					Errors.DUPLICATE_KEY.arg("slug: " + slug + ", idOrganization: " + idOrganization));
		}

	}

	private void checkPosition(SoPositionRequest position) throws BadRequestException {
		if (position != null) {
			checkMandatoryParameter(position.getLon(), "lon");
			checkMandatoryParameter(position.getLat(), "lat");
		}
	}

	private void checkDevice(SmartobjectRequest smartobjectRequest) throws BadRequestException {
		if (isType(Type.DEVICE, smartobjectRequest)) {

			// se è device id codice deve avere il pattern uuid
			if (!ServiceUtil.isUUID(smartobjectRequest.getSocode())) {
				throw new BadRequestException(
						Errors.INCORRECT_VALUE.arg("For device type the socode must have UUID pattern [ "
								+ smartobjectRequest.getSocode() + " ] ."));
			}

			// se è device tipo esposizione obbligatorio (iterno/esterno)
			checkMandatoryParameter(smartobjectRequest.getIdExposureType(), "idExposureType");
		}
	}

	private void validation(SmartobjectRequest smartobjectRequest, Integer idOrganization)
			throws BadRequestException, NotFoundException, Exception {

		/******************************************************************************************************************************************
		 * controlla i campi obbligatori
		 ******************************************************************************************************************************************/
		checkMandatoryParameter(smartobjectRequest, "smartobjectRequest");
		checkMandatoryParameter(smartobjectRequest.getIdSoType(), "idSoType");
		checkMandatoryParameter(smartobjectRequest.getIdTenant(), "idTenant");
		checkMandatoryParameter(smartobjectRequest.getSocode(), "socode");
		checkMandatoryParameter(smartobjectRequest.getName(), "name");
		checkWhitespace(smartobjectRequest.getSocode(), "socode");

		/******************************************************************************************************************************************
		 * verifico i campi mandatory delle position:
		 ******************************************************************************************************************************************/
		checkPosition(smartobjectRequest.getPosition());
		
		/******************************************************************************************************************************************
		 * verifica che slug non sia null o stringa vuota verifica sintassi slug
		 * --> regex ^[a-zA-Z0-9]*$
		 ******************************************************************************************************************************************/
		checkCode(smartobjectRequest.getSlug(), "slug");

		/******************************************************************************************************************************************
		 * categoria obbligatoria (se non è di tipo tweet)
		 ******************************************************************************************************************************************/
		if (!isType(Type.FEED_TWEET, smartobjectRequest)) {
			checkMandatoryParameter(smartobjectRequest.getIdSoCategory(), "idSoCategory");
		}
		
		/******************************************************************************************************************************************
		 * se è device id codice deve avere il pattern uuid se è device tipo
		 * esposizione obbligatorio (iterno/esterno) se è device latitudine,
		 * longitudine, elevation, piano (floor) devono essere float
		 ******************************************************************************************************************************************/
		checkDevice(smartobjectRequest);

		/******************************************************************************************************************************************
		 * se non è device il codice deve passare questa regex /^(?!.*(?:[
		 * *./#<>àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅåÆæœ]))/;
		 ******************************************************************************************************************************************/
//		if (!isType(Type.DEVICE, smartobjectRequest)
//				&& !ServiceUtil.isCodeForNoteDeviceType(smartobjectRequest.getSocode())) {
//			throw new BadRequestException(Errors.INCORRECT_VALUE
//					.arg("Not correct pattern for not device type [ " + smartobjectRequest.getSocode() + " ] ."));
//		}
		
		/******************************************************************************************************************************************
		 * se è di tipo twitter deve avere questi campi:
		 *  - numero massimo di stream, 
		 *  - username, 
		 *  - usertoken, 
		 *  - tokensecret (gli ultimi 3 arrivano da twitter e non sono sull'interfaccia utente)		 
		 *******************************************************************************************************************************************/
		checkTweet(smartobjectRequest);

		/******************************************************************************************************************************************
		 * verifica che non esista un SO con lo stesso codice per
		 * quell'organizzazione (chiave codiceSO+codiceOrg) verifica univocita
		 * slug per l'organizzazione (chiave codiceSlug + codiceOrg)
		 ******************************************************************************************************************************************/
		checkSmartObject(idOrganization, smartobjectRequest.getSocode(), smartobjectRequest.getSlug());
		
		/******************************************************************************************************************************************
		 * verifica che l'oranzataion passata sia legata al tenant fornito nel json della request.
		 ******************************************************************************************************************************************/
		checkOrganizationTenant(idOrganization, smartobjectRequest.getIdTenant());


	}
	
	private SoPosition insertSoPosition(SoPositionRequest soPositionRequest) throws BadRequestException {

		SoPosition soPosition = null;

		try {
			soPosition = new SoPosition();

			BeanUtils.copyProperties(soPositionRequest, soPosition);

			soPositionMapper.insertSoPosition(soPosition);

		} 
		catch (DuplicateKeyException duplicateKeyException) {
			throw new BadRequestException(Errors.DUPLICATE_KEY.arg(duplicateKeyException.getRootCause().getMessage()));
		} 
		catch (DataIntegrityViolationException e) {
			throw new BadRequestException(Errors.INCORRECT_VALUE.arg(e.getRootCause().getMessage()));
		}

		return soPosition;
	}
	
	private Smartobject insertSmartObject(SmartobjectRequest smartobjectRequest, Integer organizationCode,
			Timestamp now) throws BadRequestException {

		Smartobject smartobject = null;

		try {
			smartobject = new Smartobject();

			BeanUtils.copyProperties(smartobjectRequest, smartobject);

			smartobject.setIdOrganization(organizationCode);
			smartobject.setCreationdate(now);

			smartobjectMapper.insertSmartObject(smartobject);

		} 
		catch (DuplicateKeyException duplicateKeyException) {
			throw new BadRequestException(Errors.DUPLICATE_KEY.arg(duplicateKeyException.getRootCause().getMessage()));
		} 
		catch (DataIntegrityViolationException e) {
			throw new BadRequestException(Errors.INCORRECT_VALUE.arg(e.getRootCause().getMessage()));
		}

		return smartobject;
	}

}
