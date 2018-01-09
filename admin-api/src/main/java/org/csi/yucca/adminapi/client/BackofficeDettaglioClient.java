package org.csi.yucca.adminapi.client;

import java.util.concurrent.ExecutionException;

import org.csi.yucca.adminapi.client.cache.CacheUtil;
import org.csi.yucca.adminapi.client.cache.KeyCache;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;

public class BackofficeDettaglioClient {
	
	public static  BackofficeDettaglioApiResponse getBackofficeDettaglioApi(String adminApiBaseUrl, String codapi, String logger) throws AdminApiClientException
	{
		return AdminApiClientDelegate.getFromAdminApi(adminApiBaseUrl+"/1/backoffice/api/"+codapi,BackofficeDettaglioApiResponse.class,logger, null);
	}
//	public static  BackofficeDettaglioApiResponse getBackofficeDettaglioApi(String adminApiBaseUrl, String codapi, String logger) throws AdminApiClientException
//	{
//		try {
//			return CacheUtil.getDettaglioApiCache().get(new KeyCache().adminApiBaseUrl(adminApiBaseUrl).code(codapi).logger(logger));	
//		} 
//		catch (ExecutionException e) {
//			throw new AdminApiClientException(e);
//		}
//	}


	public static  BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdStream(String adminApiBaseUrl, Integer IdStream, String logger) throws AdminApiClientException
	{
		return AdminApiClientDelegate.getFromAdminApi(adminApiBaseUrl+"/1/backoffice/streams/"+Integer.toString(IdStream),BackofficeDettaglioStreamDatasetResponse.class,logger, null);
	}
//	public static  BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdStream(String adminApiBaseUrl, Integer IdStream, String logger) throws AdminApiClientException
//	{
//		try {
//			return CacheUtil.getDettaglioStreamDatasetCache().get(new KeyCache().adminApiBaseUrl(adminApiBaseUrl).id(IdStream).logger(logger));	
//		} 
//		catch (ExecutionException e) {
//			throw new AdminApiClientException(e);
//		}
//	}

	public static  BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(String adminApiBaseUrl, String soCode, String streamCode, String logger) throws AdminApiClientException
	{
		return AdminApiClientDelegate.getFromAdminApi(adminApiBaseUrl+"/1/backoffice/streams/"+soCode+"/"+streamCode,BackofficeDettaglioStreamDatasetResponse.class,logger, null);
	}

	public static  BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDataset(String adminApiBaseUrl, Integer IdDataset, String logger) throws AdminApiClientException
	{
		return AdminApiClientDelegate.getFromAdminApi(adminApiBaseUrl+"/1/backoffice/datasets/"+Integer.toString(IdDataset),BackofficeDettaglioStreamDatasetResponse.class,logger, null);
	}


	public static  BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(String adminApiBaseUrl, Integer IdDataset, Integer datasetVersion, String logger) throws AdminApiClientException
	{
		return AdminApiClientDelegate.getFromAdminApi(adminApiBaseUrl+"/1/backoffice/datasets/"+Integer.toString(IdDataset)+"/"+Integer.toString(datasetVersion),BackofficeDettaglioStreamDatasetResponse.class,logger, null);
	}

	public static  BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(String adminApiBaseUrl, String datasetCode, Integer datasetVersion, String logger) throws AdminApiClientException
	{
		return AdminApiClientDelegate.getFromAdminApi(adminApiBaseUrl+"/1/backoffice/datasets/datasetCode="+datasetCode+"/"+Integer.toString(datasetVersion),BackofficeDettaglioStreamDatasetResponse.class,logger, null);
	}

	public static  BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCode(String adminApiBaseUrl, String datasetCode, String logger) throws AdminApiClientException
	{
		return AdminApiClientDelegate.getFromAdminApi(adminApiBaseUrl+"/1/backoffice/datasets/datasetCode="+datasetCode,BackofficeDettaglioStreamDatasetResponse.class,logger, null);
	}
}
