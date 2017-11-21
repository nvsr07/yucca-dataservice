package org.csi.yucca.adminapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.exception.UnauthorizedException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.mapper.DatasetMapper;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.response.DatasetResponse;
import org.csi.yucca.adminapi.response.DettaglioDatasetResponse;
import org.csi.yucca.adminapi.service.DatasetService;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class DatasetServiceImpl implements DatasetService {

	@Autowired
	private DatasetMapper datasetMapper;
	
	@Override
	public ServiceResponse selectDataset(String organizationCode, Integer idDataset, String tenantCodeManager, JwtUser authorizedUser) 
			throws BadRequestException, NotFoundException, Exception {
		
		DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDataset(tenantCodeManager, idDataset, organizationCode, ServiceUtil.getTenantCodeListFromUser(authorizedUser));

		ServiceUtil.checkIfFoundRecord(dettaglioDataset);

		return ServiceUtil.buildResponse(new DettaglioDatasetResponse(dettaglioDataset));
	}
	
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

}
