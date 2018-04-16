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
			
//			BackofficeDettaglioApiResponse mresponse = getBackofficeDettaglioApi("ds_Stresstwt_11_241", "ds_TrFl_3");
			
//			for (int i = 0; i < 7; i++) {
//				
//				stop = "";
//				
//				BackofficeDettaglioStreamDatasetResponse response = getBackofficeDettaglioStreamDatasetByIdStream(124,
//						false, "ciccio");
//				
//				stop = "";
//				
//				if(i>3){
//					stop = "";
//					BackofficeDettaglioStreamDatasetResponse response3 = getBackofficeDettaglioStreamDatasetByIdStream(125,
//					false, "ciccio");
//					stop = "";
//				}
//				
//			}
			
//			stop = "";
//			BackofficeDettaglioStreamDatasetResponse response = getBackofficeDettaglioStreamDatasetByIdStream(124,
//					false, "ciccio");
//			stop = "";

//			BackofficeDettaglioStreamDatasetResponse response2 = getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(
//					"35fcb755-d518-41cf-8938-5f11f85db425", "ProvaDavideOData4", false, "ciccio");

//			stop = "";
//
//			BackofficeDettaglioStreamDatasetResponse response3 = getBackofficeDettaglioStreamDatasetByIdStream(124,
//					false, "ciccio");
//
//			stop = "";

//		} catch (Exception e) {
//			stop = "";
//		}
//
//	}

	/**
	 * 
	 * @param idMeasureUnit
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static MeasureUnitResponse getMeasureUnit(Integer idMeasureUnit, String logger) throws AdminApiClientException {
		
		try {
			return CacheUtilDB.getMeasureUnit(new KeyCacheDB().idMeasureUnit(idMeasureUnit));
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}

	
	/**
	 * 
	 * @param idDataset
	 * @param datasetVersion
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(
			Integer idDataset, Integer datasetVersion, String logger)
			throws AdminApiClientException {

		try {
			return CacheUtilDB.getDettaglioStreamDatasetByIdDatasetDatasetVersion(
					new KeyCacheDB().idDataset(idDataset).datasetVersion(datasetVersion));
		}
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}
	
	
	/**
	 * 
	 * @param datasetCode
	 * @param datasetVersion
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(
			String datasetCode, Integer datasetVersion, String logger) throws AdminApiClientException {
		
		try {
			return CacheUtilDB.getDettaglioStreamDatasetByDatasetCodeDatasetVersion(
					new KeyCacheDB().datasetCode(datasetCode).datasetVersion(datasetVersion));
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
	
	
	/**
	 * 
	 * @param idDataset
	 * @param onlyInstalled
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDataset(
			Integer idDataset, Boolean onlyInstalled, String logger)
			throws AdminApiClientException {
		
		try {
			return CacheUtilDB.getDettaglioStreamDatasetByIdDataset(
					new KeyCacheDB().idDataset(idDataset).onlyInstalled(onlyInstalled));
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error(" Exception", e);
			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param datasetCode
	 * @param onlyInstalled
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCode(
			String datasetCode, Boolean onlyInstalled, String logger)
			throws AdminApiClientException {
		try {
			
			return CacheUtilDB.getDettaglioStreamDatasetByDatasetCode(
					new KeyCacheDB().datasetCode(datasetCode).onlyInstalled(onlyInstalled));
			
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * OK
	 * @param idStream
	 * @param onlyInstalled
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdStream(
			Integer idStream, Boolean onlyInstalled, String logger) throws AdminApiClientException {
		try {
			return CacheUtilDB.getDettaglioStreamDataset(
					new KeyCacheDB().idStream(idStream).onlyInstalled(onlyInstalled));
		} catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param soCode
	 * @param streamCode
	 * @param onlyInstalled
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(
			String soCode, String streamCode, Boolean onlyInstalled, String logger) throws AdminApiClientException {
		try {

			return CacheUtilDB.getDettaglioStreamDatasetBySO(new KeyCacheDB()
					.smartObjectCode(soCode).streamCode(streamCode).onlyInstalled(onlyInstalled));
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param codApi
	 * @param logger
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioApiResponse getBackofficeDettaglioApi(
			String codApi, String logger) throws AdminApiClientException {
		
		try {
			
			return CacheUtilDB.getDettaglioApi(new KeyCacheDB().apiCode(codApi));
			
		} 
		catch (Exception e) {
			Logger log = Logger.getLogger(logger + ".AdminDBClientDelegate");
			log.error("Exception", e);
			throw new AdminApiClientException(e);
		}
	}

}
