package org.csi.yucca.adminapi.service.impl;

import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.BundlesMapper;
import org.csi.yucca.adminapi.mapper.SmartobjectMapper;
import org.csi.yucca.adminapi.mapper.StreamMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.Stream;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.InternalStreamRequest;
import org.csi.yucca.adminapi.request.PostStreamRequest;
import org.csi.yucca.adminapi.request.SharingTenantRequest;
import org.csi.yucca.adminapi.service.StreamService;
import org.csi.yucca.adminapi.util.DataOption;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ManageOption;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.StreamVisibility;
import org.csi.yucca.adminapi.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class StreamServiceImpl implements StreamService {

	@Autowired
	private SmartobjectMapper smartobjectMapper;
	
	@Autowired
	private TenantMapper tenantMapper;

	@Autowired
	private BundlesMapper bundlesMapper;
	
	@Autowired
	private StreamMapper streamMapper;

	@Override
	public ServiceResponse createStreamDataset(PostStreamRequest request, String organizationCode, String soCode, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, UnauthorizedException, Exception {
		
		validation(request, organizationCode, soCode, authorizedUser);
		
		insertStream();
		
		return ServiceResponse.build().object("oook");
	}
	
	private void insertStream(){
		
	}
	
	/**
	 * 
	 * @param request
	 * @param smartobject
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void validation(PostStreamRequest request, String organizationCode, String soCode, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception {

		checkMandatories(request);
		
		Smartobject smartobject = checkSmartobject(organizationCode, soCode);
		
		Tenant tenant = checkTenant(request.getIdTenant(), organizationCode);
		
		checkAuthTenant(authorizedUser, tenant.getTenantcode());		
		
		checkMaxNumStream(request.getIdTenant());
		
		checkStreamCode(request.getStreamcode(), smartobject.getIdSmartObject());		
		// da chiarire:
		//		streamcode deve essere univoca la coppia streamcode + id_smart_object e controllo caratteri (a-zA-Z0-9) 
		//		streamCode + smartobject +tenant univoco (controllo temporaneo)
		
		checkInternalSmartObject(request, smartobject.getIdSoType());

		checkFeedTweetSmartobject(request, smartobject.getIdSoType());
		
		checkComponents(request, smartobject.getIdSoType());
		
		checkVisibility(request);
	}

	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkInternalSmartObject(PostStreamRequest request, Integer idSoType) throws BadRequestException, NotFoundException{
		//   INTERNAL SO TYPE
		if(Type.INTERNAL.id() == idSoType){
			ServiceUtil.checkMandatoryParameter(request.getInternalquery(), "internalquery mandatory (only for internal smartobject)");
			ServiceUtil.checkList(request.getInternalStreams(), "InternalStreams mandatory (only for internal smartobject)");
			for (InternalStreamRequest internalStream : request.getInternalStreams()) {
				ServiceUtil.checkMandatoryParameter(internalStream.getIdStream(), "internalStream => idStream");
				ServiceUtil.checkMandatoryParameter(internalStream.getStreamAlias(), "internalStream => streamAlias");
			}
		}

		//   NOT INTERNAL SO TYPE
		if(Type.INTERNAL.id() != idSoType && request.getInternalquery() != null){
			throw new BadRequestException(Errors.INCORRECT_VALUE, "internalquery: is not internal smartobject.");
		}
	}
	
	/**
	 * 
	 * @param request
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkMandatories(PostStreamRequest request) throws BadRequestException, NotFoundException{
		ServiceUtil.checkMandatoryParameter(request.getStreamname(), "streamname");
		ServiceUtil.checkMandatoryParameter(request.getName(), "name");
		ServiceUtil.checkMandatoryParameter(request.getSavedata(), "savedata");
		ServiceUtil.checkMandatoryParameter(request.getVisibility(), "visibility");
		ServiceUtil.checkMandatoryParameter(request.getRequestername(), "requestername");
		ServiceUtil.checkMandatoryParameter(request.getRequestersurname(), "requestersurname");
		ServiceUtil.checkMandatoryParameter(request.getRequestermail(), "requestermail");
		ServiceUtil.checkMandatoryParameter(request.getIdSubdomain(), "idSubdomain");
		ServiceUtil.checkList(request.getTags(), "tags");
	}
	
	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkFeedTweetSmartobject(PostStreamRequest request, Integer idSoType) throws BadRequestException, NotFoundException{
		
		if(Type.FEED_TWEET.id() == idSoType){
			ServiceUtil.checkMandatoryParameter(request.getTwitterInfoRequest(), "twitterInfo mandatory (only for Feed Tweet smartobject)");
			ServiceUtil.checkMandatoryParameter(request.getTwitterInfoRequest().getTwtquery(), "twitterInfo => Twtquery");
			if(request.getComponents() != null){
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Component not allowed for Feed Tweet smartobject!");
			}
		}

		if(Type.FEED_TWEET.id() != idSoType && request.getTwitterInfoRequest() != null){
			throw new BadRequestException(Errors.INCORRECT_VALUE, "TwitterInfo: is not feed tweet smartobject.");
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param idSoType
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	private void checkComponents(PostStreamRequest request, Integer idSoType) throws NotFoundException, BadRequestException{
		if(Type.FEED_TWEET.id() != idSoType){
			ServiceUtil.checkList(request.getComponents());
			for (ComponentRequest component : request.getComponents()) {
				ServiceUtil.checkMandatoryParameter(component.getName(), "name");
				ServiceUtil.checkAphanumeric(component.getName(), "component name");
				ServiceUtil.checkMandatoryParameter(component.getAlias(), "alias");
				ServiceUtil.checkMandatoryParameter(component.getInorder(), "inorder");
				ServiceUtil.checkMandatoryParameter(component.getTolerance(), "tolerance");
				ServiceUtil.checkMandatoryParameter(component.getIdPhenomenon(), "idPhenomenon");
				ServiceUtil.checkMandatoryParameter(component.getIdDataType(), "idDataType");
				ServiceUtil.checkMandatoryParameter(component.getIdMeasureUnit(), "idMeasureUnit");
				ServiceUtil.checkMandatoryParameter(component.getRequired(), "required");
			}
		}
	}
	
	/**
	 * 
	 * @param request
	 * @throws BadRequestException
	 * @throws NotFoundException
	 */
	private void checkVisibility(PostStreamRequest request) throws BadRequestException, NotFoundException{

		ServiceUtil.checkValue("visibility", request.getVisibility(), StreamVisibility.PRIVATE.code(), StreamVisibility.PUBLIC.code());
		
		// PRIVATE
		if(StreamVisibility.PRIVATE.code().equals(request.getVisibility())){
			if(request.getLicense() != null){
				throw new BadRequestException(Errors.INCORRECT_VALUE, "License only for public visibility, provided: " + request.getVisibility());
			}
			if(request.getOpenData() != null){
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Opendata only for public visibility, provided: " + request.getVisibility());
			}
			if(request.getSharingTenants() != null){
				for (SharingTenantRequest sharingTenant : request.getSharingTenants() ) {
					ServiceUtil.checkMandatoryParameter(sharingTenant.getIdTenant(), "sharingTenant => idTenant");
					ServiceUtil.checkValue("dataOptions", sharingTenant.getDataOptions(), DataOption.READ.id(), DataOption.READ_AND_SUBSCRIBE.id(), DataOption.READ_AND_USE.id(), DataOption.WRITE.id());
					ServiceUtil.checkValue("manageOptions", sharingTenant.getManageOptions(), ManageOption.EDIT_METADATA.id(), ManageOption.LIFE_CYCLE_HANDLING.id(), ManageOption.NO_RIGHT.id());
					Tenant selectedTenant = tenantMapper.selectTenantByidTenant(sharingTenant.getIdTenant());
					ServiceUtil.checkIfFoundRecord(selectedTenant, "Sharing Tenant with [ " + sharingTenant.getIdTenant() + " ] not found!");
				}
			}
		}

		// PUBLIC
		if(StreamVisibility.PUBLIC.code().equals(request.getVisibility())){
			
			if (request.getSharingTenants() != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Sharing Tenants permitted only for private visibility!");
			}
			if (request.getCopyright() != null) {
				throw new BadRequestException(Errors.INCORRECT_VALUE, "Copyright permitted only for private visibility!");
			}
		}	
	}

	/**
	 * 
	 * @param authorizedUser
	 * @param tenantCode
	 * @throws UnauthorizedException
	 */
	private void checkAuthTenant(JwtUser authorizedUser, String tenantCode) throws UnauthorizedException{
		List<String> userAuthorizedTenantCodeList = ServiceUtil.getTenantCodeListFromUser(authorizedUser);
		for (String authTenant : userAuthorizedTenantCodeList) {
			if(authTenant.equals(tenantCode))return;
		}
		throw new UnauthorizedException(Errors.UNAUTHORIZED, "not authorized tenantCode [" + tenantCode + "]");
	}
	
	/**
	 * 
	 * @param organizationCode
	 * @param soCode
	 * @return
	 * @throws NotFoundException
	 */
	private Smartobject checkSmartobject(String organizationCode, String soCode) throws NotFoundException{
		Smartobject smartobject = smartobjectMapper.selectSmartobjectBySocodeAndOrgcode(soCode, organizationCode);
		ServiceUtil.checkIfFoundRecord(smartobject, "smartobject not found socode [" + soCode + "], organizationcode [" + organizationCode + "] ");
		return smartobject;
	}

	/**
	 * 
	 * @param idTenant
	 * @param organizationCode
	 * @return
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	private Tenant checkTenant(Integer idTenant, String organizationCode) throws NotFoundException, BadRequestException{
		ServiceUtil.checkMandatoryParameter(idTenant, "idTenant");
		Tenant tenant = tenantMapper.selectTenantByIdAndOrgCodeCode(idTenant, organizationCode);
		ServiceUtil.checkIfFoundRecord(tenant, "tenant not found idTenant [" + idTenant + "], organizationcode [" + organizationCode + "] ");
		return tenant;
	}

	/**
	 * 
	 * @param idTenant
	 * @throws UnauthorizedException
	 */
	private void checkMaxNumStream(Integer idTenant) throws UnauthorizedException{
		Bundles bundles = bundlesMapper.selectBundlesByTenant(idTenant);
		if(bundles.getMaxstreamsnum() != -1){
			Integer countOfTenantStream = streamMapper.selectCountOfTenantStream(idTenant);
			if(countOfTenantStream+1 > bundles.getMaxstreamsnum()){
				throw new UnauthorizedException(Errors.UNAUTHORIZED, "max num stream reached.");
			}
		}
	}
	
	/**
	 * 
	 * @param streamcode
	 * @param idSmartobject
	 * @throws BadRequestException
	 */
	private void checkStreamCode(String streamcode, Integer idSmartobject) throws BadRequestException{
		ServiceUtil.checkCode(streamcode, "streamcode");
		Stream stream = streamMapper.selectStreamByStreamcodeAndIdSmartObject(streamcode, idSmartobject);
		if(stream != null){
			throw new BadRequestException(Errors.INTEGRITY_VIOLATION, "There is another stream with streamcode [ " + streamcode + " ] and idSmartObject [ " + idSmartobject + " ]");
		}
	}

}
