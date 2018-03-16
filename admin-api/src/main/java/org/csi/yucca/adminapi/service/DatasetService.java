package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.exception.BadRequestException;
import org.csi.yucca.adminapi.exception.NotFoundException;
import org.csi.yucca.adminapi.jwt.JwtUser;
import org.csi.yucca.adminapi.request.AllineamentoScaricoDatasetRequest;
import org.csi.yucca.adminapi.request.DatasetRequest;
import org.csi.yucca.adminapi.request.ImportMetadataDatasetRequest;
import org.csi.yucca.adminapi.util.ServiceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DatasetService {

	ServiceResponse selectAllineamentoScaricoDataset(Integer idOrganization) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse insertLastMongoObjectId(AllineamentoScaricoDatasetRequest request, Integer idOrganization)throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectAllineamentoScaricoDataset(Integer idOrganization, Integer idDataset, Integer datasetVersion) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse deleteDatasetData(String organizationCode, Integer idDataset, String tenantCodeManager, Integer version, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse uninstallingDatasets(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectDatasetByOrganizationCode(String organizationCode) throws BadRequestException, NotFoundException, Exception;
	
	ServiceResponse selectDatasets(String organizationCode, String tenantCodeManager, String sort,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDataset(String organizationCode, Integer idDataset, String tenantCodeManager,
			JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByIdDataset(Integer idDataset, boolean onlyInstalled) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByIdDatasetDatasetVersion(Integer idDataset,Integer datasetVersion) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByDatasetCodeDatasetVersion(String datasetCode, Integer datasetVersion)throws BadRequestException, NotFoundException, Exception;

	ServiceResponse selectDatasetByDatasetCode(String datasetCode, boolean onlyInstalled)throws BadRequestException, NotFoundException, Exception;

	
	ServiceResponse insertDataset(String organizationCode, DatasetRequest postDatasetRequest, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception;

	ServiceResponse updateDataset(String organizationCode, Integer idDataset, DatasetRequest datasetRequest,
			String tenantCodeManager, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;

	ServiceResponse insertCSVData(MultipartFile file, Boolean skipFirstRow, String encoding,
			String csvSeparator, String componentInfoRequests, String organizationCode, Integer idDataset, String tenantCodeManager, JwtUser authorizedUser)
			throws BadRequestException, NotFoundException, Exception;
	ServiceResponse importMetadata(String organizationCode, ImportMetadataDatasetRequest importMetadataRequest, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;
	
	byte[] selectDatasetIcon(String organizationCode, Integer idDataset, JwtUser authorizedUser) throws BadRequestException, NotFoundException, Exception;




}
