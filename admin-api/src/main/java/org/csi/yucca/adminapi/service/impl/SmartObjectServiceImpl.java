package org.csi.yucca.adminapi.service.impl;

import static org.csi.yucca.adminapi.util.ServiceUtil.checkAphanumeric;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkList;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkMandatoryParameter;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkWhitespace;
import static org.csi.yucca.adminapi.util.ServiceUtil.getResponseList;
import static org.csi.yucca.adminapi.util.ServiceUtil.getSortList;

import java.sql.Timestamp;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.ExposureTypeMapper;
import org.csi.yucca.adminapi.mapper.LocationTypeMapper;
import org.csi.yucca.adminapi.mapper.SmartobjectMapper;
import org.csi.yucca.adminapi.mapper.SoCategoryMapper;
import org.csi.yucca.adminapi.mapper.SoTypeMapper;
import org.csi.yucca.adminapi.mapper.SupplyTypeMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.model.ExposureType;
import org.csi.yucca.adminapi.model.LocationType;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.SoCategory;
import org.csi.yucca.adminapi.model.SoType;
import org.csi.yucca.adminapi.model.SupplyType;
import org.csi.yucca.adminapi.request.SmartobjectRequest;
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
	
	/**
	 * DELETE SMARTOBJECT
	 */
	public ServiceResponse deleteSmartObject(Integer organizationCode, String socode) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(organizationCode, "organizationCode");
		ServiceUtil.checkMandatoryParameter(socode, "soCode");
		
		Smartobject smartobject = smartobjectMapper.selectSmartobject(socode, organizationCode);

		if(org.csi.yucca.adminapi.util.SoType.INTERNAL.id() == smartobject.getIdSoType()){
			throw new BadRequestException(Errors.INCORRECT_VALUE.arg("idSoType di tipo: " + org.csi.yucca.adminapi.util.SoType.INTERNAL.code() + " delete denied."));
		}
		
		smartobjectMapper.deleteTenantSmartobject(smartobject.getIdSmartObject());
	
		int count = 0;
		try {
			count = smartobjectMapper.deleteSmartobject(socode, organizationCode);
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
	 * INSERT SMART OBJECT
	 */
	public ServiceResponse insertSmartobject(SmartobjectRequest smartobjectRequest, Integer organizationCode) throws BadRequestException, NotFoundException, Exception{

		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		validation(smartobjectRequest, organizationCode);		
		
		Smartobject smartobject = insertSmartObject(smartobjectRequest, organizationCode, now);
		
		smartobjectMapper.insertTenantSmartobject(smartobjectRequest.getIdTenant(), smartobject.getIdSmartObject(), now);
		
		return ServiceResponse.build().object(new SmartobjectResponse(smartobject));
	}

	
	private void checkOrganizationTenant(Integer soIdOrganization, Integer soIdTenant) throws NotFoundException, BadRequestException{
		Integer idOrganization = tenantMapper.selectIdOrganizationByIdTenant(soIdTenant);
		if(idOrganization == null){
			throw new NotFoundException(Errors.RECORD_NOT_FOUND.arg("idTenant [" + soIdTenant + "]"));
		}
		if(idOrganization != soIdOrganization){
			throw new BadRequestException(Errors.NOT_CONSISTENT_DATA.arg(
					"tenant with id " + soIdTenant + " does not belong to organozation with id " + soIdOrganization));
		}
	}
	
	private void checkTweet(SmartobjectRequest smartobjectRequest) throws BadRequestException{
		boolean justOneTweetInfo = smartobjectRequest.getTwtusername() != null
				|| smartobjectRequest.getTwtmaxsearchnumber() != null
				|| smartobjectRequest.getTwtmaxsearchinterval() != null || smartobjectRequest.getTwtusertoken() != null
				|| smartobjectRequest.getTwttokensecret() != null || smartobjectRequest.getTwtname() != null
				|| smartobjectRequest.getTwtuserid() != null || smartobjectRequest.getTwtmaxstreams() != null;

		boolean isTweetSoType = org.csi.yucca.adminapi.util.SoType.FEED_TWEET.id() == smartobjectRequest.getIdSoType();

		if (!isTweetSoType && justOneTweetInfo) {
			throw new BadRequestException(Errors.NO_TWEET_SO_TYPE);
		}

		if (isTweetSoType) {
			checkMandatoryParameter(smartobjectRequest.getTwtusername(), "idSoType");
			checkMandatoryParameter(smartobjectRequest.getTwtmaxsearchnumber(), "twtmaxsearchnumber");
			checkMandatoryParameter(smartobjectRequest.getTwtmaxsearchinterval(), "twtmaxsearchinterval");
			checkMandatoryParameter(smartobjectRequest.getTwtusertoken(), "twtusertoken");
			checkMandatoryParameter(smartobjectRequest.getTwttokensecret(), "twttokensecret");
			checkMandatoryParameter(smartobjectRequest.getTwtname(), "twtname");
			checkMandatoryParameter(smartobjectRequest.getTwtuserid(), "twtuserid");
			checkMandatoryParameter(smartobjectRequest.getTwtmaxstreams(), "twtmaxstreams");
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
	
	private void validation(SmartobjectRequest smartobjectRequest, Integer organizationCode)throws BadRequestException, NotFoundException, Exception{
		checkMandatoryParameter(smartobjectRequest, "smartobjectRequest");
		checkMandatoryParameter(smartobjectRequest.getName(), "name");
		checkMandatoryParameter(smartobjectRequest.getSlug(), "slug");
		checkMandatoryParameter(smartobjectRequest.getIdSoCategory(), "idSoCategory");
		checkMandatoryParameter(smartobjectRequest.getSocode(), "socode");
		checkMandatoryParameter(smartobjectRequest.getIdSoType(), "idSoType");
		checkMandatoryParameter(smartobjectRequest.getIdTenant(), "idTenant");
		checkWhitespace(smartobjectRequest.getSocode(), "socode");
		checkWhitespace(smartobjectRequest.getSlug(), "slug");
		checkTweet(smartobjectRequest);		
		checkOrganizationTenant(organizationCode, smartobjectRequest.getIdTenant());		
		checkAphanumeric(smartobjectRequest.getSlug(), "slug");
	}

	private Smartobject insertSmartObject(SmartobjectRequest smartobjectRequest, Integer organizationCode, Timestamp now)throws BadRequestException{
		
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
