package org.csi.yucca.adminapi.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.BundlesMapper;
import org.csi.yucca.adminapi.mapper.EcosystemMapper;
import org.csi.yucca.adminapi.mapper.FunctionMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SequenceMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.mapper.UserMapper;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.User;
import org.csi.yucca.adminapi.request.ActionOnTenantRequest;
import org.csi.yucca.adminapi.request.BundlesRequest;
import org.csi.yucca.adminapi.request.TenantRequest;
import org.csi.yucca.adminapi.response.TenantResponse;
import org.csi.yucca.adminapi.service.ClassificationService;
import org.csi.yucca.adminapi.service.SmartObjectService;
import org.csi.yucca.adminapi.service.TenantService;
import org.csi.yucca.adminapi.util.Ecosystem;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.ShareType;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.TenantType;
import org.csi.yucca.adminapi.util.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class TenantServiceImpl implements TenantService {

	@Autowired
	private TenantMapper tenantMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private EcosystemMapper ecosystemMapper;
	
	@Autowired
	private OrganizationMapper organizationMapper;

	@Autowired
	private BundlesMapper bundlesMapper;
	
	@Autowired
	private SequenceMapper sequenceMapper;

	@Autowired
	private FunctionMapper functionMapper;
	
	@Autowired
	private ClassificationService classificationService;

	@Autowired
	private SmartObjectService smartObjectService;
	
	
	//	-- actionOnTenant
	//	management
	//	patch
	//	/management/tenants/{tenantCode}
	//	in body: action:	string
	//
	//	VERIFICARE CHE LO STATUS DEL TENANT è SU RICHIESTA INSTALLAZIONE ALTRIMENTI DARE ERRORE
	//
	//	SE LA RICHIESTA VA A BUON FINE SETTARE LO STATO A : INSTALLAZIONE IN PROGRESS
	public ServiceResponse actionOnTenant(ActionOnTenantRequest actionOnTenantRequest, String tenantCode) throws BadRequestException, NotFoundException, Exception{
		
		ServiceUtil.checkMandatoryParameter(tenantCode, "tenantCode");
		ServiceUtil.checkMandatoryParameter(actionOnTenantRequest, "actionOnTenantRequest");
		ServiceUtil.checkMandatoryParameter(actionOnTenantRequest.getCodeTenantStatus(), "CodeTenantStatus");
		ServiceUtil.checkCodeTenantStatus(actionOnTenantRequest.getCodeTenantStatus());
		
		Tenant tenant = tenantMapper.selectTenantByTenantCode(tenantCode);
		ServiceUtil.checkIfFoundRecord(tenant);
		
		if(Status.REQUEST_INSTALLATION.id()!= tenant.getIdTenantStatus()){
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Current status: " + ServiceUtil.codeTenantStatus(tenant.getIdTenantStatus()));
		}
		
		// da implementare
		installationAction(tenantCode);
		
		// cambia lo stato del tenant:
		tenantMapper.updateTenantStatus(Status.INSTALLATION_IN_PROGRESS.id(), tenantCode);
		
		return ServiceResponse.build().NO_CONTENT();
	}
	
	private void installationAction(String tenantCode){
		
	}
	
	
	/**
	 * DELETE TENANT
	 */
	public ServiceResponse deleteTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception{
		ServiceUtil.checkMandatoryParameter(tenantcode, "tenantcode");
	
		int count = 0;
		try {
			count = tenantMapper.deleteTenant(tenantcode);
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
	 * INSERT TENANT
	 */
	public ServiceResponse insertTenant(TenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception {
		
		validation(tenantRequest);
		
		if(TenantType.PERSONAL.id() == tenantRequest.getIdTenantType() || TenantType.TRIAL.id() == tenantRequest.getIdTenantType()){
			managePersonalOrTrial(tenantRequest);
		}

		Tenant tenant = createTenant(tenantRequest);
		
		// inserimento bundles
		Bundles bundles = insertBundles(tenantRequest.getBundles(), tenantRequest.getIdTenantType());
		
		// insert user
		User user = insertUser(tenantRequest.getUsername(), tenantRequest.getIdOrganization());

		// insert tenant
		tenantMapper.insertTenant(tenant);
		userMapper.insertTenantUser(tenant.getIdTenant(), user.getIdUser());

		// inserimento tenant bundle
	    tenantMapper.insertTenantBundles(tenant.getIdTenant(), bundles.getIdBundles());
		
		// inserimento r_tenant_users
	    userMapper.insertTenantUser(tenant.getIdTenant(), user.getIdUser());
	    
		//	il tenant deve inoltre essere abilitato allo smartobject internal dell'organizzazione (record nella r_tenant_smartobject con isOwner a false		
		Smartobject internalSmartObject = smartObjectService.selectSmartObjectByOrganizationAndSoType(tenantRequest.getIdOrganization(), Type.INTERNAL.id()); 
		smartObjectService.insertNotManagerTenantSmartobject(tenant.getIdTenant(), internalSmartObject.getIdSmartObject(), new Timestamp(System.currentTimeMillis()));
		
		return ServiceResponse.build().object(new TenantResponse(tenant));
	
	}
	
// --------------------------------------------------------------------------------------------------------------------------------------------------
//											PRIVATE METHODS
// --------------------------------------------------------------------------------------------------------------------------------------------------	

	/**
	 * 
	 * @param tenantRequest
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void validation(TenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception {

		ServiceUtil.checkMandatoryParameter(tenantRequest.getIdTenantType(),   "idTenantType");
		ServiceUtil.checkIdTenantType(tenantRequest.getIdTenantType());
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUsertypeauth(),   "usertypeauth");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUsername(),       "username");
		ServiceUtil.checkMandatoryParameter(tenantRequest,                     "tenantRequest");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUserfirstname(),  "userfirstname");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUserlastname(),   "userlastname");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUseremail(),      "useremail");
		ServiceUtil.checkUserTypeAuth(tenantRequest.getUsertypeauth());
		ServiceUtil.checkTenantTypeAndUserTypeAuth(tenantRequest.getUsertypeauth(), tenantRequest.getIdTenantType());
		
		if(TenantType.PERSONAL.id() != tenantRequest.getIdTenantType() && TenantType.TRIAL.id() != tenantRequest.getIdTenantType()){
			ServiceUtil.checkMandatoryParameter(tenantRequest.getName(),           "name");
			ServiceUtil.checkMandatoryParameter(tenantRequest.getDescription(),    "description");
			ServiceUtil.checkMandatoryParameter(tenantRequest.getTenantcode(),     "tenantcode");
			ServiceUtil.checkCode(tenantRequest.getTenantcode(),                   "tenantcode");
			ServiceUtil.checkMandatoryParameter(tenantRequest.getIdOrganization(), "idOrganization");
			ServiceUtil.checkIfFoundRecord(organizationMapper.selectOrganizationById(tenantRequest.getIdOrganization()), "Organization [ id: " + tenantRequest.getIdOrganization() + " ]");
			ServiceUtil.checkMandatoryParameter(tenantRequest.getIdEcosystem(),    "idEcosystem");
			ServiceUtil.checkIfFoundRecord(ecosystemMapper.selectEcosystemById(tenantRequest.getIdEcosystem()), "Ecosystem [ id: " + tenantRequest.getIdEcosystem() + " ]");
		}
	}	
	
	/**
     * 
     * @param tenantRequest
     * @param tenantTypeDescription
     * @throws BadRequestException
     * @throws NotFoundException
     * @throws Exception
     */
	private void checkActiveTrialOrPersonalTenant(TenantRequest tenantRequest, String tenantTypeDescription) throws BadRequestException, NotFoundException, Exception {
		// verificare che per quello username non esista un altro tenant personal già attivo
		List<Tenant> tenantList = tenantMapper.selectActiveTenantByUserNameAndIdTenantType(tenantRequest.getUsername(), tenantRequest.getIdTenantType());
		
		if (tenantList != null && !tenantList.isEmpty()) {
			throw new BadRequestException(Errors.NOT_CONSISTENT_DATA, " Not possible more than one active tenant for user " + tenantRequest.getUsername()+ " of type " + tenantTypeDescription + "!");
		}
	}
	

	/**
	 * 
	 * @param idTenantType
	 * @return
	 */
	private int getProgressivo(Integer idTenantType){
		
		if(TenantType.PERSONAL.id() == idTenantType){
			return sequenceMapper.selectPersonalTenantsSequence();
		}
		
		return sequenceMapper.selectTrialTenantsSequence();
	}
	
	/**
	 * 
	 * @param tenantRequest
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void managePersonalOrTrial(TenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception {

		// ecosystem SDNET (id  = 1) di default
		tenantRequest.setIdEcosystem(Ecosystem.SDNET.id());

		String tenantTypeDescription = ServiceUtil.tenantTypeDescription(tenantRequest.getIdTenantType());
		
		checkActiveTrialOrPersonalTenant(tenantRequest, tenantTypeDescription);
		
		// personal<progressivo>
		tenantRequest.setTenantcode(tenantTypeDescription + getProgressivo(tenantRequest.getIdTenantType()));

		// "personal tenant <codice>"
		tenantRequest.setName(tenantTypeDescription + " tenant " + tenantRequest.getTenantcode());
		
		// descrizione --> "tral personal <codice> creato per user: <username>"
		tenantRequest.setDescription(tenantRequest.getName() + " created for user " + tenantRequest.getUsername());
		
		// inserire un organizzazione con codice e descrizione uguali a quelle del tenant
		Organization organization = new Organization();
		organization.setOrganizationcode(tenantRequest.getTenantcode());		
		organization.setDescription(tenantRequest.getDescription());
		classificationService.insertOrganization(organization, Ecosystem.SDNET.id());

		tenantRequest.setIdOrganization(organization.getIdOrganization());		
	}
	
	/**
	 * 
	 * @param tenantRequest
	 * @return
	 */
	private Tenant createTenant(TenantRequest tenantRequest){
		Tenant tenant = new Tenant();
		BeanUtils.copyProperties(tenantRequest, tenant);

		// set creationdate
		tenant.setCreationdate(new Timestamp(System.currentTimeMillis()));

		// set tenant status
		tenant.setIdTenantStatus(Status.REQUEST_INSTALLATION.id());
		
		
		// set id share type
		if(TenantType.DEVELOP.id() == tenantRequest.getIdTenantType()){
			tenant.setIdShareType(ShareType.NONE.id());
		}
		else{
			tenant.setIdShareType(ShareType.PUBLIC.id());
		}

		return tenant;
	}	
	
	/**
	 * 
	 * @param username
	 * @param idOrganization
	 * @return
	 */
	private User insertUser(String username, int idOrganization){
		
		User user = userMapper.selectUserByUserName(username);
		
		if(user == null){
			user = new User();
			user.setIdOrganization(idOrganization);
			user.setPassword(functionMapper.selectRandomPassword());  
			user.setUsername(username);
			userMapper.insertUser(user);
		}

		return user;
	}
	
	/**
	 * 
	 * @param bundlesRequest
	 * @param idTenantType
	 * @return
	 */
	private Bundles insertBundles(BundlesRequest bundlesRequest, Integer idTenantType){
		
		Bundles bundles = new Bundles();
		
		if(bundlesRequest != null){
			
			if (bundlesRequest.getMaxOdataResultperpage() != null && 
					TenantType.DEVELOP.id() != idTenantType &&
					TenantType.TRIAL.id() != idTenantType &&
					TenantType.PERSONAL.id() != idTenantType ) {
				bundles.setMaxOdataResultperpage(bundlesRequest.getMaxOdataResultperpage());
			}// else: prende i valori di default impostati nel costruttore Bundle.
			
			if (bundlesRequest.getHasstage() != null) {
				bundles.setHasstage(bundlesRequest.getHasstage());
			}
			
			if (bundlesRequest.getMaxdatasetnum() != null) {
				bundles.setMaxdatasetnum(bundlesRequest.getMaxdatasetnum());
			}
			
			if (bundlesRequest.getMaxstreamsnum() != null) {
				bundles.setMaxstreamsnum(bundlesRequest.getMaxstreamsnum());
			}
			
			if (bundlesRequest.getZeppelin() != null) {
				bundles.setZeppelin(bundlesRequest.getZeppelin());
			}
			
		}
		
		bundlesMapper.insertBundles(bundles);
		return bundles;
	}
	
}
