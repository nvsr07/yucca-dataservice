package org.csi.yucca.adminapi.client.db;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.MeasureUnitResponse;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

public class BackofficeDettaglioClientDB {

//	public static void main(String[] args) {
//		String stop = "";
//		try {
//			
////			BackofficeDettaglioApiResponse mresponse = getBackofficeDettaglioApi("ds_Stresstwt_11_241", "ds_TrFl_3");
//			
//			stop = "";
//			BackofficeDettaglioStreamDatasetResponse response = getBackofficeDettaglioStreamDatasetByIdStream(124,
//					false, "ciccio");
//			stop = "";
//
//			BackofficeDettaglioStreamDatasetResponse response2 = getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(
//					"35fcb755-d518-41cf-8938-5f11f85db425", "ProvaDavideOData4", false, "ciccio");
//
//			stop = "";
//
//			BackofficeDettaglioStreamDatasetResponse response3 = getBackofficeDettaglioStreamDatasetByIdStream(124,
//					false, "ciccio");
//
//			stop = "";
//
//		} catch (Exception e) {
//			stop = "";
//		}
//
//	}

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(
			String soCode, String streamCode, Boolean onlyInstalled, String logger) throws AdminApiClientException {
		try {
			return (BackofficeDettaglioStreamDatasetResponse) AdminDBClientDelegate.getInstance().getStreamService()
					.selectStreamBySoCodeStreamCode(soCode, streamCode, onlyInstalled).getObject();
		} catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdStream(
			Integer idStream, Boolean onlyInstalled, String logger) throws AdminApiClientException {

		try {
			return (BackofficeDettaglioStreamDatasetResponse) AdminDBClientDelegate.getInstance().getStreamService()
					.selectStreamByIdStream(idStream, onlyInstalled).getObject();
		} catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCode(
			String datasetCode, Boolean onlyInstalled, String logger)
			throws AdminApiClientException {
		try {
			return (BackofficeDettaglioStreamDatasetResponse)
					AdminDBClientDelegate.getInstance().getDatasetService().selectDatasetByDatasetCode(datasetCode, onlyInstalled).getObject();
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDataset(
			Integer IdDataset, Boolean onlyInstalled, String logger)
			throws AdminApiClientException {
		
		try {
			
			return (BackofficeDettaglioStreamDatasetResponse)
					AdminDBClientDelegate.getInstance().getDatasetService().selectDatasetByIdDataset(IdDataset, onlyInstalled).getObject();
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error(" Exception", e);
			throw new AdminApiClientException(e);
		}
		
	}
	
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(
			String datasetCode, Integer datasetVersion, String logger) throws AdminApiClientException {
		
		try {
			return (BackofficeDettaglioStreamDatasetResponse)
					AdminDBClientDelegate.getInstance().getDatasetService().selectDatasetByDatasetCodeDatasetVersion(datasetCode, datasetVersion).getObject();
		} 
		catch (InvalidCacheLoadException exception) {
			return null;
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}
	
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(
			Integer idDataset, Integer datasetVersion, String logger)
			throws AdminApiClientException {

		try {
			return (BackofficeDettaglioStreamDatasetResponse)
					AdminDBClientDelegate.getInstance().getDatasetService()
					.selectDatasetByIdDatasetDatasetVersion(idDataset, datasetVersion).getObject();
		}
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}
	
	public static MeasureUnitResponse getMeasureUnit(Integer idMeasureUnit, String logger) throws AdminApiClientException {
		
		try {
			return (MeasureUnitResponse)
					AdminDBClientDelegate.getInstance().getComponentService().selectMeasureUnit(idMeasureUnit).getObject();
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}
	
	public static BackofficeDettaglioApiResponse getBackofficeDettaglioApi(
			String codApi, String logger) throws AdminApiClientException {
		
		try {
			return (BackofficeDettaglioApiResponse)
					AdminDBClientDelegate.getInstance().getApiService().selectBackofficeLastInstalledDettaglioApi(codApi).getObject();
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}

}
