package org.csi.yucca.adminapi.service.impl;

import static org.csi.yucca.adminapi.util.ServiceUtil.sendMessage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.csi.yucca.adminapi.delegate.StoreDelegate;
import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.ConflictException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.BundlesMapper;
import org.csi.yucca.adminapi.mapper.EcosystemMapper;
import org.csi.yucca.adminapi.mapper.FunctionMapper;
import org.csi.yucca.adminapi.mapper.MailTemplatesMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SequenceMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.mapper.TenantTypeMapper;
import org.csi.yucca.adminapi.mapper.UserMapper;
import org.csi.yucca.adminapi.messaging.MessageSender;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.model.Mailtemplates;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.TenantsType;
import org.csi.yucca.adminapi.model.User;
import org.csi.yucca.adminapi.model.join.DettaglioTenantBackoffice;
import org.csi.yucca.adminapi.model.join.TenantManagement;
import org.csi.yucca.adminapi.request.ActionRequest;
import org.csi.yucca.adminapi.request.ActionfeedbackOnTenantRequest;
import org.csi.yucca.adminapi.request.BundlesRequest;
import org.csi.yucca.adminapi.request.PostTenantRequest;
import org.csi.yucca.adminapi.request.PostTenantSocialRequest;
import org.csi.yucca.adminapi.request.TenantRequest;
import org.csi.yucca.adminapi.response.BackofficeDettaglioTenantResponse;
import org.csi.yucca.adminapi.response.EmailTenantResponse;
import org.csi.yucca.adminapi.response.FabricResponse;
import org.csi.yucca.adminapi.response.TenantManagementResponse;
import org.csi.yucca.adminapi.response.TenantResponse;
import org.csi.yucca.adminapi.response.TenantTypeResponse;
import org.csi.yucca.adminapi.service.ClassificationService;
import org.csi.yucca.adminapi.service.MailService;
import org.csi.yucca.adminapi.service.SmartObjectService;
import org.csi.yucca.adminapi.service.TenantService;
import org.csi.yucca.adminapi.service.TokenService;
import org.csi.yucca.adminapi.store.response.GeneralResponse;
import org.csi.yucca.adminapi.util.Constants;
import org.csi.yucca.adminapi.util.Ecosystem;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.ShareType;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.StreamAction;
import org.csi.yucca.adminapi.util.TenantType;
import org.csi.yucca.adminapi.util.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@PropertySources({ @PropertySource("classpath:ambiente_deployment.properties") })
public class TenantServiceImpl implements TenantService {

	@Value("${collprefix}")
	private String collprefix;

	@Autowired
	private TenantMapper tenantMapper;

	@Autowired
	private TenantTypeMapper tenantTypeMapper;

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
	private MailTemplatesMapper mailTemplatesMapper;

	@Autowired
	private ClassificationService classificationService;

	@Autowired
	private SmartObjectService smartObjectService;

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private MailService mailService;

	@Autowired
	private TokenService tokenService;
	
	@Override
	public ServiceResponse selectTenantToken(String tenantCode, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception {
		
		ServiceUtil.checkAuthTenant(authorizedUser, tenantCode);
		
		Tenant tenant = tenantMapper.selectTenantByTenantCode(tenantCode);
		
		ServiceUtil.checkIfFoundRecord(tenant);
		
		String clientKey = tenant.getClientkey();
		
		String clientSecret = tenant.getClientsecret();

		// per testare scommentare qua sotto e commentare il checkAuthTenant: 
		//		clientKey ="lj5De_Zev9snGEtZZBBFlZlgoO8a";
		//		clientSecret = "jli9DGdEHqVpw6W7IEUXrjyTDeMa";
		
		return tokenService.get(clientKey, clientSecret);
	}
	
	@Override
	public ServiceResponse selectMail(String tenantcode) throws BadRequestException, NotFoundException, Exception {
		
		DettaglioTenantBackoffice tenant = tenantMapper.selectDettaglioTenant(tenantcode);
		
		ServiceUtil.checkIfFoundRecord(tenant);
		
		Mailtemplates mailtemplates = mailTemplatesMapper.selectBundlesByIdTenantType(tenant.getIdTenantType());
		
		EmailTenantResponse response = EmailTenantResponse.build(tenantcode, tenant.getPassword())
				.destinatario(tenant.getUseremail())
				.soggetto(mailtemplates.getMailobject())
				.testo(mailtemplates.getMailbody());

		return ServiceResponse.build().object(response);
	}
	
	@Override
	public ServiceResponse insertTenantSocial(PostTenantSocialRequest request) throws BadRequestException, NotFoundException, Exception {

		if (TenantType.PERSONAL.id() != request.getIdTenantType() && TenantType.TRIAL.id() != request.getIdTenantType()) {
			throw new UnauthorizedException(Errors.UNAUTHORIZED, "Only tenants " + TenantType.PERSONAL.code() + "[ " + TenantType.PERSONAL.id() + " ] or "
					+ TenantType.TRIAL.code() + " [ " + TenantType.TRIAL.id() + " ] permitted.");
		}

		// inserimento bundles
		BundlesRequest bundlesRequest = new BundlesRequest();
		bundlesRequest.setMaxdatasetnum(Constants.INSTALLATION_TENANT_MAX_DATASET_NUM);
		bundlesRequest.setMaxstreamsnum(Constants.INSTALLATION_TENANT_MAX_STREAMS_NUM);
		
		ServiceResponse serviceResponse = insertTenant(request, bundlesRequest);
		
		mailService.sendTenantRequestInstallationEmail(request);
		
		return serviceResponse;
	}

	private ServiceResponse insertTenant(TenantRequest request, BundlesRequest bundlesRequest) throws BadRequestException, NotFoundException, Exception {

		validation(request);

		if (TenantType.PERSONAL.id() == request.getIdTenantType() || TenantType.TRIAL.id() == request.getIdTenantType()) {
			managePersonalOrTrial(request);
		}

		Tenant tenant = createTenant(request);

		// inserimento bundles
		Bundles bundles = insertBundles(bundlesRequest, request.getIdTenantType());

		// insert user
		User user = insertUser(tenant.getTenantcode(), request.getIdOrganization());

		// insert tenant
		tenantMapper.insertTenant(tenant);

		// inserimento tenant bundle
		tenantMapper.insertTenantBundles(tenant.getIdTenant(), bundles.getIdBundles());

		// inserimento r_tenant_users
		userMapper.insertTenantUser(tenant.getIdTenant(), user.getIdUser());

		// il tenant deve inoltre essere abilitato allo smartobject internal
		// dell'organizzazione (record nella r_tenant_smartobject con isOwner a
		// false
		Smartobject internalSmartObject = smartObjectService.selectSmartObjectByOrganizationAndSoType(request.getIdOrganization(), Type.INTERNAL.id());
		smartObjectService.insertNotManagerTenantSmartobject(tenant.getIdTenant(), internalSmartObject.getIdSmartObject(), new Timestamp(System.currentTimeMillis()));
		
		mailService.sendTenantCreationEmail(request);
		
		return ServiceResponse.build().object(new TenantResponse(tenant));
	}

	/**
	 * INSERT TENANT
	 */
	public ServiceResponse insertTenant(PostTenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception {
		return insertTenant(tenantRequest, tenantRequest.getBundles());
	}

	public ServiceResponse selectTenants(String sort) throws BadRequestException, NotFoundException, Exception {

		List<String> sortList = ServiceUtil.getSortList(sort, Tenant.class);

		List<TenantManagement> tenantList = tenantMapper.selectAllTenant(sortList);

		ServiceUtil.checkList(tenantList);

		List<TenantManagementResponse> responseList = new ArrayList<TenantManagementResponse>();

		for (TenantManagement tenantManagement : tenantList) {
			responseList.add(new TenantManagementResponse(tenantManagement));
		}

		return ServiceResponse.build().object(responseList);

	}

	/**
	 * SELECT DETTAGLIO TENANT BY TENANTCODE
	 * 
	 */
	public ServiceResponse selectTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception {

		DettaglioTenantBackoffice dettaglioTenant = tenantMapper.selectDettaglioTenant(tenantcode);

		ServiceUtil.checkIfFoundRecord(dettaglioTenant);

		return ServiceResponse.build().object(new BackofficeDettaglioTenantResponse(dettaglioTenant));

	}

	// -- actionOnTenant
	// management
	// patch
	// /management/tenants/{tenantCode}
	// in body: action: string
	//
	// VERIFICARE CHE LO STATUS DEL TENANT è SU RICHIESTA INSTALLAZIONE
	// ALTRIMENTI DARE ERRORE
	//
	// SE LA RICHIESTA VA A BUON FINE SETTARE LO STATO A : INSTALLAZIONE IN
	// PROGRESS
	public ServiceResponse actionOnTenant(ActionRequest actionOnTenantRequest, String tenantcode) throws BadRequestException, NotFoundException, Exception {

		ServiceUtil.checkMandatoryParameter(tenantcode, "tenantCode");
		ServiceUtil.checkMandatoryParameter(actionOnTenantRequest.getAction(), "action");
		ServiceUtil.checkMandatoryParameter(actionOnTenantRequest.getStartStep(), "StartStep");
		// ServiceUtil.checkCodeTenantStatus(actionOnTenantRequest.getCodeTenantStatus());
		ServiceUtil.checkValue("action", actionOnTenantRequest.getAction(), StreamAction.INSTALLATION.code(), StreamAction.MIGRATE.code(), StreamAction.DELETE.code());
		
		Tenant tenant = tenantMapper.selectTenantByTenantCode(tenantcode);
		ServiceUtil.checkIfFoundRecord(tenant);
			
		if (StreamAction.INSTALLATION.code().equals(actionOnTenantRequest.getAction()) && Status.REQUEST_INSTALLATION.id() != tenant.getIdTenantStatus()) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Current status: " + ServiceUtil.codeTenantStatus(tenant.getIdTenantStatus()));
		}
		
		if (StreamAction.DELETE.code().equals(actionOnTenantRequest.getAction()) && Status.REQUEST_UNINSTALLATION.id() != tenant.getIdTenantStatus()) {
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Current status: " + ServiceUtil.codeTenantStatus(tenant.getIdTenantStatus()));
		}

		// jms sender
		sendMessage(actionOnTenantRequest, "tenant", tenantcode, messageSender);
		
		// cambia lo stato del tenant:
		tenantMapper.updateTenantStatus(Status.INSTALLATION_IN_PROGRESS.id(), tenantcode);

		return ServiceResponse.build().NO_CONTENT();
	}

	// -- actionfeedbackOnTenant
	// management
	// patch
	// /management/tenants/{tenantCode}
	// in body: action: string
	//
	// VERIFICARE CHE LO STATUS DEL TENANT è SU RICHIESTA INSTALLAZIONE
	// ALTRIMENTI DARE ERRORE
	//
	// SE LA RICHIESTA VA A BUON FINE SETTARE LO STATO A : INSTALLAZIONE IN
	// PROGRESS
	public ServiceResponse actionfeedbackOnTenant(ActionfeedbackOnTenantRequest actionfeedbackOnTenantRequest, String tenantcode) throws BadRequestException, NotFoundException,
			Exception {

		ServiceUtil.checkMandatoryParameter(tenantcode, "tenantCode");
		ServiceUtil.checkMandatoryParameter(actionfeedbackOnTenantRequest.getStatus(), "status");
		ServiceUtil.checkValue("status", actionfeedbackOnTenantRequest.getStatus().toLowerCase(), "ok", "ko");

		Tenant tenant = tenantMapper.selectTenantByTenantCode(tenantcode);
		ServiceUtil.checkIfFoundRecord(tenant);

		if (Status.INSTALLATION_IN_PROGRESS.id().equals(tenant.getIdTenantStatus())) {
			if (actionfeedbackOnTenantRequest.getStatus().equalsIgnoreCase("ok")) {
				tenantMapper.updateTenantStatus(Status.INSTALLED.id(), tenantcode);
			} else {
				tenantMapper.updateTenantStatus(Status.INSTALLATION_FAIL.id(), tenantcode);
			}
		} else if (Status.UNINSTALLATION_IN_PROGRESS.id().equals(tenant.getIdTenantStatus())) {
			if (actionfeedbackOnTenantRequest.getStatus().equalsIgnoreCase("ok")) {
				tenantMapper.updateTenantStatus(Status.UNINSTALLATION.id(), tenantcode);
			} else {
				tenantMapper.updateTenantStatus(Status.INSTALLATION_FAIL.id(), tenantcode);
			}
		} else
			throw new BadRequestException(Errors.INCORRECT_VALUE, "Current status: " + ServiceUtil.codeTenantStatus(tenant.getIdTenantStatus()));

		return ServiceResponse.build().NO_CONTENT();
	}

	/**
	 * DELETE TENANT
	 */
	public ServiceResponse deleteTenant(String tenantcode) throws BadRequestException, NotFoundException, Exception {
		ServiceUtil.checkMandatoryParameter(tenantcode, "tenantcode");

		int count = 0;
		try {
			count = tenantMapper.deleteTenant(tenantcode);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new ConflictException(Errors.INTEGRITY_VIOLATION, "Not possible to delete, dependency problems.");
		}

		if (count == 0) {
			throw new BadRequestException(Errors.RECORD_NOT_FOUND);
		}

		return ServiceResponse.build().NO_CONTENT();

	}

	/********************
	 * SELECT TENANT TYPES
	 ********************/
	public ServiceResponse selectTenantTypes() throws BadRequestException, NotFoundException, Exception {

		List<TenantsType> tenantTypeList = tenantTypeMapper.selectTenantTypes();

		ServiceUtil.checkList(tenantTypeList);

		List<TenantTypeResponse> responseList = new ArrayList<TenantTypeResponse>();

		for (TenantsType tenantsType : tenantTypeList) {
			responseList.add(new TenantTypeResponse(tenantsType));
		}

		return ServiceResponse.build().object(responseList);

	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------
	// PRIVATE METHODS
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	private void validation(TenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception {

		ServiceUtil.checkMandatoryParameter(tenantRequest, "tenantRequest");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getIdTenantType(), "idTenantType");
		ServiceUtil.checkIdTenantType(tenantRequest.getIdTenantType());
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUsertypeauth(), "usertypeauth");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUsername(), "username");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUserfirstname(), "userfirstname");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUserlastname(), "userlastname");
		ServiceUtil.checkMandatoryParameter(tenantRequest.getUseremail(), "useremail");
		ServiceUtil.checkUserTypeAuth(tenantRequest.getUsertypeauth());
		ServiceUtil.checkTenantTypeAndUserTypeAuth(tenantRequest.getUsertypeauth(), tenantRequest.getIdTenantType());

		if (tenantRequest instanceof PostTenantRequest && TenantType.PERSONAL.id() != tenantRequest.getIdTenantType() && TenantType.TRIAL.id() != tenantRequest.getIdTenantType()) {

			ServiceUtil.checkMandatoryParameter(((PostTenantRequest) tenantRequest).getName(), "name");
			ServiceUtil.checkMandatoryParameter(((PostTenantRequest) tenantRequest).getDescription(), "description");
			ServiceUtil.checkMandatoryParameter(((PostTenantRequest) tenantRequest).getTenantcode(), "tenantcode");
			ServiceUtil.checkTenantCode(((PostTenantRequest) tenantRequest).getTenantcode(), "tenantcode");
			ServiceUtil.checkMandatoryParameter(((PostTenantRequest) tenantRequest).getIdOrganization(), "idOrganization");
			ServiceUtil.checkIfFoundRecord(organizationMapper.selectOrganizationById(((PostTenantRequest) tenantRequest).getIdOrganization()), "Organization [ id: "
					+ ((PostTenantRequest) tenantRequest).getIdOrganization() + " ]");
			ServiceUtil.checkMandatoryParameter(tenantRequest.getIdEcosystem(), "idEcosystem");
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
		// verificare che per quello username non esista un altro tenant
		// personal già attivo
		List<Tenant> tenantList = tenantMapper.selectActiveTenantByUserNameAndIdTenantType(tenantRequest.getUsername(), tenantRequest.getIdTenantType());

		if (tenantList != null && !tenantList.isEmpty()) {
			throw new BadRequestException(Errors.NOT_CONSISTENT_DATA, " Not possible more than one active tenant for user " + tenantRequest.getUsername() + " of type "
					+ tenantTypeDescription + "!");
		}
	}

	/**
	 * 
	 * @param idTenantType
	 * @return
	 */
	private String getProgressivo(Integer idTenantType) {

		String progressivo = "";
		
		if (TenantType.PERSONAL.id() == idTenantType) {
			progressivo = sequenceMapper.selectPersonalTenantsSequence()+"";
		}
		else{
			progressivo = sequenceMapper.selectTrialTenantsSequence()+""; 
		}

		while(progressivo.length() < 4 ){
			progressivo = "0" + progressivo; 
		}
		
		return progressivo;
	}

	
	/**
	 * 
	 * @param tenantRequest
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	private void managePersonalOrTrial(TenantRequest tenantRequest) throws BadRequestException, NotFoundException, Exception {

		// ecosystem SDNET (id = 1) di default
		tenantRequest.setIdEcosystem(Ecosystem.SDNET.id());

		String tenantTypeDescription = ServiceUtil.tenantTypeDescription(tenantRequest.getIdTenantType());

		checkActiveTrialOrPersonalTenant(tenantRequest, tenantTypeDescription);

		String code = tenantTypeDescription + getProgressivo(tenantRequest.getIdTenantType());
		String tenantName = tenantTypeDescription + " tenant " + code;
		String description = tenantName + " created for user " + tenantRequest.getUsername();

		// inserire un organizzazione con codice e descrizione uguali a quelle
		// del tenant
		Organization organization = new Organization();
		organization.setDatasolrcollectionname(collprefix + "_" + tenantTypeDescription + "_data");
		organization.setMeasuresolrcollectionname(collprefix + "_" + tenantTypeDescription + "_measures");
		organization.setSocialsolrcollectionname(collprefix + "_" + tenantTypeDescription + "_social");
		organization.setMediasolrcollectionname(collprefix + "_" + tenantTypeDescription + "_media");
		organization.setMediaphoenixschemaname(collprefix + "_" + tenantTypeDescription);
		organization.setMediaphoenixtablename("media");
		organization.setDataphoenixschemaname(collprefix + "_" + tenantTypeDescription);
		organization.setDataphoenixtablename("data");
		organization.setSocialphoenixschemaname(collprefix + "_" + tenantTypeDescription);
		organization.setSocialphoenixtablename("social");
		organization.setMeasuresphoenixschemaname(collprefix + "_" + tenantTypeDescription);
		organization.setMeasuresphoenixtablename("measures");
		organization.setOrganizationcode(code);
		organization.setDescription(description);
		classificationService.insertOrganization(organization, Ecosystem.SDNET.id());

		// personal<progressivo>
		tenantRequest.setTenantcode(code);

		// "personal tenant <codice>"
		tenantRequest.setName(tenantName);

		// descrizione --> "tral personal <codice> creato per user: <username>"
		tenantRequest.setDescription(description);

		tenantRequest.setIdOrganization(organization.getIdOrganization());

	}

	/**
	 * 
	 * @param tenantRequest
	 * @return
	 */
	private Tenant createTenant(TenantRequest tenantRequest) {
		Tenant tenant = new Tenant();
		BeanUtils.copyProperties(tenantRequest, tenant);

		// set creationdate
		tenant.setCreationdate(new Timestamp(System.currentTimeMillis()));

		// set tenant status
		tenant.setIdTenantStatus(Status.REQUEST_INSTALLATION.id());

		// set id share type
		if (TenantType.DEVELOP.id() == tenantRequest.getIdTenantType()) {
			tenant.setIdShareType(ShareType.NONE.id());
		} else {
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
	private User insertUser(String username, int idOrganization) {

		User user = userMapper.selectUserByUserName(username);

		if (user == null) {
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
	private Bundles insertBundles(BundlesRequest bundlesRequest, Integer idTenantType) {

		Bundles bundles = new Bundles();

		if (bundlesRequest != null) {

			if (bundlesRequest.getMaxOdataResultperpage() != null && TenantType.DEVELOP.id() != idTenantType && TenantType.TRIAL.id() != idTenantType
					&& TenantType.PERSONAL.id() != idTenantType) {
				bundles.setMaxOdataResultperpage(bundlesRequest.getMaxOdataResultperpage());
			}// else: prende i valori di default impostati nel costruttore
				// Bundle.

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

	@Override
	public ServiceResponse addAdminApplication(String tenantcode, String username, String password) {
		try {
			System.out.println("----addAdminApplication"+ tenantcode+ "," + username);
			ServiceUtil.checkMandatoryParameter(tenantcode, "tenantCode");
			ServiceUtil.checkMandatoryParameter(username, "username");
			ServiceUtil.checkMandatoryParameter(password, "password");

			CloseableHttpClient c = StoreDelegate.build().registerToStoreInit(username, password);
			StoreDelegate.build().addApplication(c, "userportal_" + tenantcode);
			StoreDelegate.build().logoutFromStore(c, username, password);
			return ServiceResponse.build().object(new FabricResponse(1, "Add Application completed successfully"));

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			return ServiceResponse.build().object(new FabricResponse(0, "Error: " + e.getMessage()));
		}
	}

	@Override
	public ServiceResponse subscribeAdminApiInStore(String tenantcode, String username, String password) {

		try {
			ServiceUtil.checkMandatoryParameter(tenantcode, "tenantCode");
			ServiceUtil.checkMandatoryParameter(username, "username");
			ServiceUtil.checkMandatoryParameter(password, "password");

			CloseableHttpClient c = StoreDelegate.build().registerToStoreInit(username, password);
			StoreDelegate.build().subscribeApi(c, "admin_api", "userportal_" + tenantcode);
			StoreDelegate.build().logoutFromStore(c, username, password);
			return ServiceResponse.build().object(new FabricResponse(1, "Subscribe Admin Api completed successfully"));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			return ServiceResponse.build().object(new FabricResponse(0, "Error: " + e.getMessage()));
		}
	}

	@Override
	public ServiceResponse generetateAdminKey(String tenantcode, String username, String password) {
		try {
			ServiceUtil.checkMandatoryParameter(tenantcode, "tenantCode");
			ServiceUtil.checkMandatoryParameter(username, "username");
			ServiceUtil.checkMandatoryParameter(password, "password");

			CloseableHttpClient c = StoreDelegate.build().registerToStoreInit(username, password);
			GeneralResponse generetateKeyResponse = StoreDelegate.build().generateKey(c, "userportal_" + tenantcode);
			StoreDelegate.build().logoutFromStore(c, username, password);
			String clientkey = generetateKeyResponse.getData().getKey().getConsumerKey();
			String clientsecret = generetateKeyResponse.getData().getKey().getConsumerSecret();
			// insert key
			tenantMapper.updateTenantClientCredential(clientkey, clientsecret, tenantcode);
			return ServiceResponse.build().object(new FabricResponse(1, "Generate Admin Key completed successfully"));

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			return ServiceResponse.build().object(new FabricResponse(0, "Error: " + e.getMessage()));
		}
	}

}
