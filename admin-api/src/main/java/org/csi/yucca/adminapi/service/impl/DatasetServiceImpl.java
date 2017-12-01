package org.csi.yucca.adminapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.ApiMapper;
import org.csi.yucca.adminapi.mapper.ComponentMapper;
import org.csi.yucca.adminapi.mapper.DataSourceMapper;
import org.csi.yucca.adminapi.mapper.DatasetMapper;
import org.csi.yucca.adminapi.mapper.DcatMapper;
import org.csi.yucca.adminapi.mapper.LicenseMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SequenceMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.model.Api;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.PostDatasetRequest;
import org.csi.yucca.adminapi.response.DatasetResponse;
import org.csi.yucca.adminapi.response.DettaglioDatasetResponse;
import org.csi.yucca.adminapi.response.PostDatasetResponse;
import org.csi.yucca.adminapi.service.DatasetService;
import org.csi.yucca.adminapi.util.DataOption;
import org.csi.yucca.adminapi.util.DataType;
import org.csi.yucca.adminapi.util.DatasetSubtype;
import org.csi.yucca.adminapi.util.ManageOption;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class DatasetServiceImpl implements DatasetService {

	@Autowired
	private DatasetMapper datasetMapper;

	@Autowired
	private TenantMapper tenantMapper;

	@Autowired
	private OrganizationMapper organizationMapper;
	
	@Autowired
	private DataSourceMapper dataSourceMapper;

	@Autowired
	private LicenseMapper licenseMapper;
	
	@Autowired
	private DcatMapper dcatMapper;
	
	@Autowired
	private SequenceMapper sequenceMapper;
	
	@Autowired
	private ComponentMapper componentMapper;
	
	@Autowired
	private ApiMapper apiMapper;

	/**
	 * 
	 */
	@Override
	public ServiceResponse insertDataset(String organizationCode, PostDatasetRequest postDatasetRequest, JwtUser authorizedUser)
				throws BadRequestException, NotFoundException, Exception {

		Organization organization = organizationMapper.selectOrganizationByCode(organizationCode); 

		insertDatasetValidation(postDatasetRequest, authorizedUser, organizationCode, organization);
		
		Dataset dataset = insertDatasetTransaction(postDatasetRequest, authorizedUser, organization);
		
		return ServiceResponse.build().object(PostDatasetResponse.build(dataset.getIddataset())
				.datasetcode(dataset.getDatasetcode()).datasetname(dataset.getDatasetname()));
	}
	
	/**
	 * 
	 */
	@Override
	public ServiceResponse selectDataset(String organizationCode, Integer idDataset, String tenantCodeManager, JwtUser authorizedUser) 
			throws BadRequestException, NotFoundException, Exception {
		
		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDataset(tenantCodeManager, idDataset, organizationCode, ServiceUtil.getTenantCodeListFromUser(authorizedUser));

		ServiceUtil.checkIfFoundRecord(dettaglioDataset);

		return ServiceUtil.buildResponse(new DettaglioDatasetResponse(dettaglioDataset));
	}

	/**
	 * 
	 */
	@Override
	public ServiceResponse selectDatasets(String organizationCode, String tenantCodeManager, String sort, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, UnauthorizedException, Exception {
		
		ServiceUtil.checkAuthTenant(authorizedUser, tenantCodeManager);
		
		List<String> sortList = ServiceUtil.getSortList(sort, Dataset.class);

		List<Dataset> dataSetList = datasetMapper.selectDataSets(tenantCodeManager, organizationCode, sortList, ServiceUtil.getTenantCodeListFromUser(authorizedUser));
		
		ServiceUtil.checkList(dataSetList);
		
		List<DatasetResponse> listResponse = new ArrayList<DatasetResponse>();
		for (Dataset dataset : dataSetList) {
			listResponse.add(new DatasetResponse(dataset));
		}
		
		return ServiceUtil.buildResponse(listResponse);

	}

	/**
	 * 
	 * @param components
	 * @return
	 */
	private boolean isBinaryDataset(List<ComponentRequest> components){
		for (ComponentRequest component : components) {
			if(DataType.BINARY.id() == component.getIdDataType()){
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param postDatasetRequest
	 * @param authorizedUser
	 * @param organizationCode
	 * @param organization
	 * @throws Exception
	 */
	private void insertDatasetValidation(PostDatasetRequest postDatasetRequest, JwtUser authorizedUser, String organizationCode, Organization organization) throws Exception{
		ServiceUtil.checkIfFoundRecord(organization, "Not found organization code: " + organizationCode );
		ServiceUtil.checkList(postDatasetRequest.getTags(), "tags");
		ServiceUtil.checkTenant(postDatasetRequest.getIdTenant(), organizationCode, tenantMapper);
		ServiceUtil.checkAuthTenant(authorizedUser, postDatasetRequest.getIdTenant(), tenantMapper);
		ServiceUtil.checkMandatoryParameter(postDatasetRequest.getDatasetname(), "datasetname");
		ServiceUtil.checkLicense(postDatasetRequest.getLicense());
		ServiceUtil.checkVisibility(postDatasetRequest, tenantMapper);
	}
	
	/**
	 * 
	 * @param postDatasetRequest
	 * @param organization
	 * @return
	 * @throws Exception
	 */
	private Integer insertBinary(PostDatasetRequest postDatasetRequest, Organization organization)throws Exception{
		Integer idBinaryDataSource = null;
		if(isBinaryDataset(postDatasetRequest.getComponents())){

			// BINARY DATASOURCE
			idBinaryDataSource = ServiceUtil.insertDataSource(
					new PostDatasetRequest().datasetname(postDatasetRequest.getDatasetname()).idSubdomain(postDatasetRequest.getIdSubdomain()), 
					organization.getIdOrganization(), 
					Status.INSTALLED.id(), dataSourceMapper);
			
			// INSERT DATASET
			ServiceUtil.insertDataset(idBinaryDataSource, ServiceUtil.DATASOURCE_VERSION, postDatasetRequest.getDatasetname(), 
					DatasetSubtype.BINARY.id(), datasetMapper, sequenceMapper);
			
			// BINARY COMPONENT
			ServiceUtil.insertBinaryComponents(idBinaryDataSource, componentMapper);
		}
		return idBinaryDataSource;
	}
	
	/**
	 * 
	 * @param postDatasetRequest
	 * @param authorizedUser
	 * @param organization
	 * @return
	 * @throws Exception
	 */
	private Dataset insertDatasetTransaction(PostDatasetRequest postDatasetRequest, JwtUser authorizedUser, Organization organization)throws Exception{
		
		// BINARY
		Integer idBinaryDataSource = insertBinary(postDatasetRequest, organization);
		
		// INSERT LICENSE:
		Integer idLicense = ServiceUtil.insertLicense(postDatasetRequest.getLicense(), licenseMapper);
		
		// INSERT DCAT:
		Long idDcat = ServiceUtil.insertDcat(postDatasetRequest.getDcat(), dcatMapper);
		
		// INSERT DATA SOURCE:
		Integer idDataSource = ServiceUtil.insertDataSource(postDatasetRequest, organization.getIdOrganization(), 
				idDcat, idLicense, Status.INSTALLED.id(), dataSourceMapper);

		// INSERT DATASET
		Dataset dataset = ServiceUtil.insertDataset(
				idDataSource, ServiceUtil.DATASOURCE_VERSION, postDatasetRequest.getDatasetname(), 
				DatasetSubtype.BULK.id(), postDatasetRequest.getImportfiletype(), ServiceUtil.DATASOURCE_VERSION, 
				idBinaryDataSource, postDatasetRequest.getJdbcdburl(),postDatasetRequest.getJdbcdbname(),
				postDatasetRequest.getJdbcdbtype(), postDatasetRequest.getJdbctablename(), datasetMapper, sequenceMapper);
		
		// TAGS
		for (Integer idTag : postDatasetRequest.getTags()) {
			dataSourceMapper.insertTagDataSource(idDataSource, ServiceUtil.DATASOURCE_VERSION, idTag);
		}
		
		// COMPONENT
		ServiceUtil.insertComponents(postDatasetRequest.getComponents(), idDataSource, ServiceUtil.DATASOURCE_VERSION, Util.booleanToInt(true), componentMapper);
		
		// TENANT-DATASOURCE
		ServiceUtil.insertTenantDataSource(postDatasetRequest.getIdTenant(), idDataSource, Util.getNow(), tenantMapper);		
		
		// SHARING TENANT
	    ServiceUtil.insertSharingTenants(postDatasetRequest.getSharingTenants(), idDataSource, Util.getNow(), DataOption.READ_AND_USE.id(), ManageOption.NO_RIGHT.id(), tenantMapper);
		
	    // API
		apiMapper.insertApi(Api.buildOutput(ServiceUtil.DATASOURCE_VERSION).apicode(dataset.getDatasetcode())
				.apiname(dataset.getDatasetname()).apisubtype(ServiceUtil.API_SUBTYPE_ODATA).idDataSource(idDataSource));
		
		return dataset;
	}
	
}
