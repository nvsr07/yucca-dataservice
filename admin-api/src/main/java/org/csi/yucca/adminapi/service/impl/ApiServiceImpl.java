package org.csi.yucca.adminapi.service.impl;

import static org.csi.yucca.adminapi.util.ServiceUtil.API_CODE_PREFIX_MQTT;
import static org.csi.yucca.adminapi.util.ServiceUtil.API_CODE_PREFIX_WEBSOCKET;
import static org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_MQTT;
import static org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_ODATA;
import static org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_WEBSOCKET;
import static org.csi.yucca.adminapi.util.ServiceUtil.DATASOURCE_VERSION;
import static org.csi.yucca.adminapi.util.ServiceUtil.SINCE_VERSION;
import static org.csi.yucca.adminapi.util.ServiceUtil.buildResponse;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkAuthTenant;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkCode;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkComponents;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkIfFoundRecord;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkList;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkMandatoryParameter;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkTenant;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkVisibility;
import static org.csi.yucca.adminapi.util.ServiceUtil.getSortList;
import static org.csi.yucca.adminapi.util.ServiceUtil.getTenantCodeListFromUser;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDataset;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertDcat;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertLicense;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertTags;
import static org.csi.yucca.adminapi.util.ServiceUtil.insertTenantDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.updateDataSource;
import static org.csi.yucca.adminapi.util.ServiceUtil.updateTagDataSource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotAcceptableException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.ApiMapper;
import org.csi.yucca.adminapi.mapper.BundlesMapper;
import org.csi.yucca.adminapi.mapper.ComponentMapper;
import org.csi.yucca.adminapi.mapper.DataSourceMapper;
import org.csi.yucca.adminapi.mapper.DatasetMapper;
import org.csi.yucca.adminapi.mapper.DcatMapper;
import org.csi.yucca.adminapi.mapper.LicenseMapper;
import org.csi.yucca.adminapi.mapper.OrganizationMapper;
import org.csi.yucca.adminapi.mapper.SequenceMapper;
import org.csi.yucca.adminapi.mapper.SmartobjectMapper;
import org.csi.yucca.adminapi.mapper.StreamMapper;
import org.csi.yucca.adminapi.mapper.SubdomainMapper;
import org.csi.yucca.adminapi.mapper.TenantMapper;
import org.csi.yucca.adminapi.model.Api;
import org.csi.yucca.adminapi.model.Bundles;
import org.csi.yucca.adminapi.model.Component;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.model.Organization;
import org.csi.yucca.adminapi.model.Smartobject;
import org.csi.yucca.adminapi.model.Stream;
import org.csi.yucca.adminapi.model.StreamInternal;
import org.csi.yucca.adminapi.model.StreamToUpdate;
import org.csi.yucca.adminapi.model.Subdomain;
import org.csi.yucca.adminapi.model.Tenant;
import org.csi.yucca.adminapi.model.TenantDataSource;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.request.ComponentRequest;
import org.csi.yucca.adminapi.request.InternalStreamRequest;
import org.csi.yucca.adminapi.request.PostStreamRequest;
import org.csi.yucca.adminapi.request.SharingTenantRequest;
import org.csi.yucca.adminapi.request.StreamRequest;
import org.csi.yucca.adminapi.request.TwitterInfoRequest;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.DettaglioSmartobjectResponse;
import org.csi.yucca.adminapi.response.DettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.ListStreamResponse;
import org.csi.yucca.adminapi.response.PostStreamResponse;
import org.csi.yucca.adminapi.service.ApiService;
import org.csi.yucca.adminapi.service.StreamService;
import org.csi.yucca.adminapi.util.Constants;
import org.csi.yucca.adminapi.util.DataOption;
import org.csi.yucca.adminapi.util.DatasetSubtype;
import org.csi.yucca.adminapi.util.Errors;
import org.csi.yucca.adminapi.util.ManageOption;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.csi.yucca.adminapi.util.Status;
import org.csi.yucca.adminapi.util.Type;
import org.csi.yucca.adminapi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ApiServiceImpl implements ApiService {


	@Autowired
	private ApiMapper apiMapper;

	@Autowired
	private DatasetMapper datasetMapper;

	@Autowired
	private SmartobjectMapper smartobjectMapper;

	@Autowired
	private TenantMapper tenantMapper;

	@Autowired
	private BundlesMapper bundlesMapper;

	@Autowired
	private StreamMapper streamMapper;

	@Autowired
	private DcatMapper dcatMapper;

	@Autowired
	private LicenseMapper licenseMapper;

	@Autowired
	private OrganizationMapper organizationMapper;

	@Autowired
	private DataSourceMapper dataSourceMapper;

	@Autowired
	private ComponentMapper componentMapper;
	
 
	
	/**
	 * select stream
	 */
	@Override
	public ServiceResponse selectBackofficeLastInstalledDettaglioApi(String apiCode) 
			throws BadRequestException, NotFoundException, Exception {

		Api api =  apiMapper.selectLastApiInstalled(apiCode);
		
		BackofficeDettaglioStreamDatasetResponse dettaglio = null;
		
		checkIfFoundRecord(api);
		
		if (api.getApisubtype().equals(org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_ODATA))
		{
			DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasource(api.getIdDataSource(),api.getDatasourceversion());

			checkIfFoundRecord(dettaglioDataset);

			
			if (DatasetSubtype.STREAM.id().equals(dettaglioDataset.getIdDatasetSubtype()) || 
					DatasetSubtype.SOCIAL.id().equals(dettaglioDataset.getIdDatasetSubtype()) ) {

				DettaglioStream dettaglioStream = streamMapper.selectStreamByDatasource(dettaglioDataset.getIdDataSource(), dettaglioDataset.getDatasourceversion());
				if (dettaglioStream != null) {

					DettaglioSmartobject dettaglioSmartobject = smartobjectMapper.selectSmartobjectById(dettaglioStream.getIdSmartObject());
					
					List<InternalDettaglioStream> listInternalStream = streamMapper.selectInternalStream( dettaglioStream.getIdDataSource(), dettaglioStream.getDatasourceversion() );
					
					dettaglio = new BackofficeDettaglioStreamDatasetResponse(dettaglioStream, dettaglioDataset, dettaglioSmartobject, listInternalStream);
				}				
			}
			
			DettaglioDataset dettaglioBinary = null;
			
			if (dettaglioDataset.getIdDataSourceBinary()!=null)
			{
				dettaglioBinary = datasetMapper.selectDettaglioDatasetByDatasource(
						dettaglioDataset.getIdDataSourceBinary(), 	
						dettaglioDataset.getDatasourceversionBinary());
			}
				
			dettaglio = new BackofficeDettaglioStreamDatasetResponse(dettaglioDataset, dettaglioBinary);
		}
		

		return buildResponse(new BackofficeDettaglioApiResponse(api, dettaglio));
		
	}
	

	


}
