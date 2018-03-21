package org.csi.yucca.adminapi.service.impl;

import static org.csi.yucca.adminapi.util.ServiceUtil.buildResponse;
import static org.csi.yucca.adminapi.util.ServiceUtil.checkIfFoundRecord;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.mapper.ApiMapper;
import org.csi.yucca.adminapi.mapper.DatasetMapper;
import org.csi.yucca.adminapi.mapper.SmartobjectMapper;
import org.csi.yucca.adminapi.mapper.StreamMapper;
import org.csi.yucca.adminapi.model.Api;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.service.ApiService;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.csi.yucca.adminapi.util.ServiceUtil;
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
	private StreamMapper streamMapper;

	
	/**
	 * select stream
	 */
	@Override
	public ServiceResponse selectBackofficeLastInstalledDettaglioApi(String apiCode) 
			throws BadRequestException, NotFoundException, Exception {
		
		Api api =  apiMapper.selectLastApiInstalled(apiCode);
		
		BackofficeDettaglioStreamDatasetResponse dettaglio = null;
		
		checkIfFoundRecord(api);
		
		if (api.getApisubtype().equals(org.csi.yucca.adminapi.util.ServiceUtil.API_SUBTYPE_ODATA)){
			
			DettaglioDataset dettaglioDataset = datasetMapper.selectDettaglioDatasetByDatasource(api.getIdDataSource(),api.getDatasourceversion());

			dettaglio = ServiceUtil.getDettaglioStreamDataset(dettaglioDataset, streamMapper, smartobjectMapper, datasetMapper);
		}

		return buildResponse(new BackofficeDettaglioApiResponse(api, dettaglio));
		
	}
	


}
