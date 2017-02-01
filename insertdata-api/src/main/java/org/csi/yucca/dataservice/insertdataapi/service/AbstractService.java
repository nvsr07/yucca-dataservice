package org.csi.yucca.dataservice.insertdataapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsertIOperationReport;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsertOutput;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetDeleteOutput;
import org.csi.yucca.dataservice.insertdataapi.util.AccountingLog;

import com.jayway.jsonpath.JsonPath;

public abstract class AbstractService {

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");
	private static final Log logAccounting = LogFactory.getLog("sdpaccounting");

	public AbstractService() {
	}

	public boolean validationJsonFormat(String body) {

		try {
			JsonPath.parse(body);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public DatasetBulkInsertOutput dataInsert(String jsonData, String codTenant, String uniqueid, String forwardfor, String authInfo) throws InsertApiBaseException {
		if (!validationJsonFormat(jsonData)) {
			throw new InsertApiBaseException("E012");
		}

		DatasetBulkInsertOutput outData = new DatasetBulkInsertOutput();
		long starTtime = 0;
		long deltaTime = -1;
		AccountingLog accLog = new AccountingLog();
		AccountingLog accLog1 = new AccountingLog();

		try {
			starTtime = System.currentTimeMillis();
			accLog.setTenantcode(codTenant);
			accLog.setUniqueid(uniqueid + "");
			accLog.setForwardefor(forwardfor + "");
			accLog.setJwtData(authInfo + "");
			accLog.setPath("/dataset/input/");

			accLog1.setUniqueid(uniqueid);

			log.debug("[AbstractService::dataInsert] BEGIN ");

			// System.out.println(" TIMETIME insertApiDataset -- inizio --> "+System.currentTimeMillis());

			log.debug("[AbstractService::dataInsert] BEGIN Parsing and validation ..");
			HashMap<String, DatasetBulkInsert> mapAttributes = parseJsonInput(codTenant, jsonData);
			log.debug("[InsertApi::dataInsert] END Parsing and validation. Elapsed[" + (System.currentTimeMillis() - starTtime) + "]");

			outData = inserimentoGeneralizzato(codTenant, mapAttributes);
			log.debug("[InsertApi::dataInsert] END inserimentoGeneralizzato. Elapsed[" + (System.currentTimeMillis() - starTtime) + "]");

			// System.out.println(" TIMETIME insertApiDataset -- fine --> "+System.currentTimeMillis());

			int inDataCount = 0;
			int outDataCount = 0;

			log.debug("[AbstractService::dataInsert] report inserimento: ");
			log.debug("[AbstractService::dataInsert] globalRequestID --> " + outData.getGlobalRequestId() + "|error code --> "
					+ (outData.getInsertException() != null ? outData.getInsertException().getErrorCode() : "NONE") + "| Numero Blocchi  --> "
					+ (outData.getDataBLockreport() != null ? outData.getDataBLockreport().size() : "WARNING: NONE"));
			for (int i = 0; outData.getDataBLockreport() != null && i < outData.getDataBLockreport().size(); i++) {
				log.debug("[AbstractService::dataInsert]            blocco(" + i + ") status                  --> " + outData.getDataBLockreport().get(i).getStatus());
				log.debug("[AbstractService::dataInsert]            blocco(" + i + ") getNumRowToInsFromJson  --> " + outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				log.debug("[AbstractService::dataInsert]            blocco(" + i + ") getRequestId            --> " + outData.getDataBLockreport().get(i).getRequestId());
				log.debug("[AbstractService::dataInsert]            blocco(" + i + ") getDatasetCode            --> " + outData.getDataBLockreport().get(i).getDatasetCode());
				log.debug("[AbstractService::dataInsert]            blocco(" + i + ") getNumRowInserted            --> " + outData.getDataBLockreport().get(i).getNumRowInserted());
				accLog1.setDataIn(outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				accLog1.setDatasetcode(outData.getDataBLockreport().get(i).getIdDataset() + ":" + outData.getDataBLockreport().get(i).getDatasetVersion());
				accLog1.setErrore(outData.getDataBLockreport().get(i).getStatus());
				inDataCount += outData.getDataBLockreport().get(i).getNumRowToInsFromJson();
				outDataCount +=   outData.getDataBLockreport().get(i).getNumRowInserted();
				
				if(outData.getDataBLockreport().get(i).getDatasetCode()!=null)
					accLog.setDatasetcode(outData.getDataBLockreport().get(i).getDatasetCode());

			}
			log.debug("[AbstractService::dataInsert] inDataCount --> " + inDataCount);

			accLog.setDataIn(inDataCount);
			accLog.setDataOut(outDataCount);
			log.debug("[AbstractService::dataInsert] getDataIn --> " + accLog.getDataIn());
			log.debug("[AbstractService::dataInsert] getDataIn --> " + accLog.toString());

		} catch (InsertApiBaseException insEx) {
			log.warn("[InsertApi::insertApiDataset] InsertApiBaseException " + insEx.getErrorCode() + " - " + insEx.getErrorName());
			accLog.setErrore(insEx.getErrorCode() + " - " + insEx.getErrorName());
			throw insEx;
		} catch (Exception e) {
			log.fatal("[InsertApi::insertApiDataset] GenericException " + e);
			throw new InsertApiRuntimeException(e);
		} finally {
			try {
				deltaTime = System.currentTimeMillis() - starTtime;
				accLog.setElapsed(deltaTime);

			} catch (Exception e) {
			}
			logAccounting.info(accLog.toString());
			log.info("[InsertApi::insertApiDataset] END --> elapsed: " + deltaTime);
		}

		// response.setStatus(Status.ACCEPTED.getStatusCode());
		return outData;

	}

	protected abstract HashMap<String, DatasetBulkInsert> parseJsonInput(String codTenant, String jsonData) throws Exception;

	protected DatasetBulkInsertOutput inserimentoGeneralizzato(String codTenant, HashMap<String, DatasetBulkInsert> datiDains) throws InsertApiBaseException {
		DatasetBulkInsertOutput outData = new DatasetBulkInsertOutput();
		AccountingLog accLog = new AccountingLog();

		try {
			long starTtime = System.currentTimeMillis();

			log.debug("[InsertApi::inserimentoGeneralizzato] BEGIN ");
			accLog.setTenantcode(codTenant);

			InsertApiLogic insApiLogic = new InsertApiLogic();

			// System.out.println(" TIMETIME inserimentoGeneralizzato -- inizio --> "+System.currentTimeMillis());

			HashMap<String, DatasetBulkInsert> retHm = insApiLogic.insertManager(codTenant, datiDains);

			log.debug("[InsertApi::dataInsert] END insertManager. Elapsed[" + (System.currentTimeMillis() - starTtime) + "]");

			// System.out.println(" TIMETIME inserimentoGeneralizzato -- dopo insert manager --> "+System.currentTimeMillis());

			ArrayList<DatasetBulkInsertIOperationReport> ret = new ArrayList<DatasetBulkInsertIOperationReport>();
			Iterator<String> it = retHm.keySet().iterator();
			DatasetBulkInsertIOperationReport retElement = null;
			String idRichieste = null;
			while (it.hasNext()) {
				String key = it.next();
				retElement = new DatasetBulkInsertIOperationReport();
				retElement.setDatasetVersion(retHm.get(key).getDatasetVersion());
				retElement.setIdDataset(retHm.get(key).getIdDataset());
				retElement.setDatasetCode(retHm.get(key).getDatasetCode());

				// TODO forse non ha senso, commentato
				// retElement.setNumRowInserted(retHm.get(key).getNumRowToInsFromJson());
				retElement.setNumRowToInsFromJson(retHm.get(key).getNumRowToInsFromJson());
				retElement.setRequestId(retHm.get(key).getRequestId());
				retElement.setSensor(retHm.get(key).getSensor());
				retElement.setStream(retHm.get(key).getStream());

				// TODO
				retElement.setStatus(retHm.get(key).getStatus());

				// TODO serve?
				retElement.setTimestamp(retHm.get(key).getTimestamp());
				idRichieste = retHm.get(key).getGlobalReqId();
				ret.add(retElement);
			}

			log.debug("[InsertApi::dataInsert] END Request creation. Elapsed[" + (System.currentTimeMillis() - starTtime) + "]");

			outData.setDataBLockreport(ret);
			outData.setGlobalRequestId(idRichieste);

			// System.out.println(" TIMETIME inserimentoGeneralizzato -- fine --> "+System.currentTimeMillis());

		} catch (InsertApiBaseException insEx) {
			log.warn("[InsertApi::insertApi] InsertApiBaseException " + insEx.getErrorCode() + " - " + insEx.getErrorName());

			outData.setInsertException((InsertApiBaseException) insEx);
		} catch (Exception e) {
			log.fatal("[InsertApi::insertApi] GenericException " + e);
			if (e instanceof InsertApiRuntimeException)
				throw (InsertApiRuntimeException) e;
			else
				throw new InsertApiRuntimeException(e);
		} finally {

			log.debug("[InsertApi::inserimentoGeneralizzato] END ");
			// logAccounting.info(accLog.toString());

		}

		return outData;
	}

	public DatasetDeleteOutput dataDelete(String codTenant, String idDataset, String datasetVersion, String uniqueid, String forwardfor, String authInfo) {
		log.info("[InsertApi::dataDelete] START ");
		log.info("[InsertApi::dataDelete] codTenant " + codTenant);
		log.info("[InsertApi::dataDelete] idDataset " + idDataset);
		log.info("[InsertApi::dataDelete] datasetVersion " + datasetVersion);
		log.info("[InsertApi::dataDelete] uniqueid " + uniqueid);
		log.info("[InsertApi::dataDelete] forwardfor " + forwardfor);
		log.info("[InsertApi::dataDelete] authInfo " + authInfo);


		DatasetDeleteOutput outData = new DatasetDeleteOutput();
		
		
		long starTtime = 0;
		long deltaTime = -1;
		AccountingLog accLog = new AccountingLog();
		AccountingLog accLog1 = new AccountingLog();
		Long idDatasetLong = null;
		Long datasetVersionLong = null;
		if(idDataset == null){
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID);
		}
		try {
			idDatasetLong = new Long(idDataset);
		} catch (Exception e) {
			throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID);
		}
		
		if (datasetVersion !=null) {
			try {
				datasetVersionLong = new Long(datasetVersion);
			} catch (Exception e) {
				throw new InsertApiBaseException(InsertApiBaseException.ERROR_CODE_DATASET_DATASETVERSION_INVALID);
			}
		}

		try {
			starTtime = System.currentTimeMillis();
			accLog.setTenantcode(codTenant);
			accLog.setUniqueid(uniqueid + "");
			accLog.setForwardefor(forwardfor + "");
			accLog.setJwtData(authInfo + "");
			accLog.setPath("/dataset/delete/");

			accLog1.setUniqueid(uniqueid);

			log.info("[AbstractService::dataDelete] BEGIN ");
			InsertApiLogic insertApiLogic = new InsertApiLogic();
			outData = insertApiLogic.deleteManager(codTenant, idDatasetLong, datasetVersionLong);
			log.info("[AbstractService::dataDelete] phoenixResult " + outData.getDataDeletedMessage());
			
		} catch (InsertApiBaseException insEx) {
			log.warn("[InsertApi::dataDelete] InsertApiBaseException " + insEx.getErrorCode() + " - " + insEx.getErrorName());
			accLog.setErrore(insEx.getErrorCode() + " - " + insEx.getErrorName());
			throw insEx;
		} catch (Exception e) {
			log.fatal("[InsertApi::dataDelete] GenericException " + e);
			throw new InsertApiRuntimeException(e);
		} finally {
			try {
				deltaTime = System.currentTimeMillis() - starTtime;
				accLog.setElapsed(deltaTime);

			} catch (Exception e) {
			}
			logAccounting.info(accLog.toString());
			log.info("[InsertApi::dataDelete] END --> elapsed: " + deltaTime);
		}

		// response.setStatus(Status.ACCEPTED.getStatusCode());
		return outData;
	}

}
