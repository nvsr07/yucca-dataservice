package org.csi.yucca.dataservice.binaryapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.binaryapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.binaryapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.binaryapi.model.output.DatasetBulkInsertIOperationReport;
import org.csi.yucca.dataservice.binaryapi.model.output.DatasetBulkInsertOutput;
import org.csi.yucca.dataservice.binaryapi.util.AccountingLog;

import com.jayway.jsonpath.JsonPath;

public abstract class AbstractService {

	private static final Log log=LogFactory.getLog("org.csi.yucca.datainsert");
	private static final Log logAccounting=LogFactory.getLog("sdpaccounting");



	public AbstractService() {
	}

	
	
	public boolean validationJsonFormat(String body)
	{
		
		try {
			JsonPath.parse(body);
		} catch (Exception e) {
			return false;
		}
		return  true;
	}

	public DatasetBulkInsertOutput dataInsert(HttpServletRequest request,
			HttpServletResponse response, String jsonData, String codTenant, String uniqueid,
			String forwardfor, String authInfo) {
		System.out.println("codiceTenant"+codTenant);
	
		
		
		if (!validationJsonFormat(jsonData))
		{
			throw new InsertApiBaseException("E012");
		}
		
		DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();
		long starTtime=0;
		long deltaTime=-1;
		AccountingLog accLog=new AccountingLog();
		AccountingLog accLog1=new AccountingLog();

		try {
			starTtime=System.currentTimeMillis();
			accLog.setTenantcode(codTenant);
			accLog.setUniqueid(uniqueid+"");
			accLog.setForwardefor(forwardfor+"");
			accLog.setJwtData(authInfo+"");
			accLog.setPath("/dataset/input/");

			accLog1.setUniqueid(uniqueid);

			
			log.info( "[InsertApi::insertApiDataset] BEGIN ");

			//System.out.println(" TIMETIME insertApiDataset -- inizio --> "+System.currentTimeMillis());
			
			Long starTtimeX=System.currentTimeMillis();
			log.info( "[InsertApi::insertApiDataset] BEGIN Parsing and validation ..");
			HashMap<String, DatasetBulkInsert> mapAttributes = parseJsonInput(codTenant,jsonData);
			log.info( "[InsertApi::insertApiDataset] END Parsing and validation. Elapsed["+(System.currentTimeMillis()-starTtimeX)+"]");


			outData=inserimentoGeneralizzato(codTenant, mapAttributes);

			//System.out.println(" TIMETIME insertApiDataset -- fine --> "+System.currentTimeMillis());

			int inData=0;
			
			
			log.info( "[InsertApi::insertApiDataset] report inserimento: ");
			log.info( "[InsertApi::insertApiDataset]       globalRequestID --> " +outData.getGlobalRequestId());
			log.info( "[InsertApi::insertApiDataset]       error code      --> " +(outData.getInsertException()!=null ? outData.getInsertException().getErrorCode() : "NONE" ));
			log.info( "[InsertApi::insertApiDataset]       Numero Blocchi  --> " +(outData.getDataBLockreport()!=null ? outData.getDataBLockreport().size() : "WARNING: NONE" ));
			for (int i=0;outData.getDataBLockreport()!=null && i<outData.getDataBLockreport().size(); i++) {
				log.debug("[InsertApi::insertApiDataset]            blocco("+i+") status                  --> " +outData.getDataBLockreport().get(i).getStatus());
				log.debug( "[InsertApi::insertApiDataset]            blocco("+i+") getNumRowToInsFromJson  --> " +outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				log.debug( "[InsertApi::insertApiDataset]            blocco("+i+") getRequestId            --> " +outData.getDataBLockreport().get(i).getRequestId());
				accLog1.setDataIn(outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				accLog1.setDatasetcode(outData.getDataBLockreport().get(i).getIdDataset()+":"+outData.getDataBLockreport().get(i).getDatasetVersion());
				accLog1.setErrore(outData.getDataBLockreport().get(i).getStatus());
				inData+=outData.getDataBLockreport().get(i).getNumRowToInsFromJson();
				
			}
			accLog.setDataIn(inData);

			

		} catch (InsertApiBaseException insEx) {
			log.warn( "[InsertApi::insertApiDataset] InsertApiBaseException "+insEx.getErrorCode() + " - " + insEx.getErrorName());
			accLog.setErrore(insEx.getErrorCode() + " - " + insEx.getErrorName());
			throw insEx;
		} catch (Exception e) {
			log.fatal( "[InsertApi::insertApiDataset] GenericException "+e);
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			accLog.setErrore(newEx.getErrorCode() + " - " + newEx.getErrorName());
			throw newEx;
		} finally {
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
				accLog.setElapsed(deltaTime);
				
			} catch (Exception e) {}
			logAccounting.info(accLog.toString());	
			log.info( "[InsertApi::insertApiDataset] END --> elapsed: "+deltaTime);
		}

		response.setStatus(Status.ACCEPTED.getStatusCode());
		return outData;

	}

	protected abstract HashMap<String, DatasetBulkInsert> parseJsonInput(String codTenant,
			String jsonData) throws Exception ;

	
	protected DatasetBulkInsertOutput inserimentoGeneralizzato(String codTenant,HashMap<String, DatasetBulkInsert>datiDains) throws Exception{
		DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();
		AccountingLog accLog=new AccountingLog();

		try {

			log.info( "[InsertApi::inserimentoGeneralizzato] BEGIN ");
			accLog.setTenantcode(codTenant);

			InsertApiLogic insApiLogic=new InsertApiLogic();

			//System.out.println(" TIMETIME inserimentoGeneralizzato -- inizio --> "+System.currentTimeMillis());

			HashMap<String, DatasetBulkInsert> retHm = insApiLogic.insertManager(codTenant,datiDains);	

			//System.out.println(" TIMETIME inserimentoGeneralizzato -- dopo insert manager --> "+System.currentTimeMillis());

			ArrayList<DatasetBulkInsertIOperationReport> ret = new ArrayList<DatasetBulkInsertIOperationReport>();
			Iterator<String> it=retHm.keySet().iterator();
			DatasetBulkInsertIOperationReport retElement=null;
			String idRichieste=null;
			while (it.hasNext()) {
				String key = it.next();
				retElement=new DatasetBulkInsertIOperationReport();
				retElement.setDatasetVersion(retHm.get(key).getDatasetVersion());
				retElement.setIdDataset(retHm.get(key).getIdDataset());

				//TODO forse non ha senso, commentato
				//retElement.setNumRowInserted(retHm.get(key).getNumRowToInsFromJson());
				retElement.setNumRowToInsFromJson(retHm.get(key).getNumRowToInsFromJson());
				retElement.setRequestId(retHm.get(key).getRequestId());
				retElement.setSensor(retHm.get(key).getSensor());
				retElement.setStream(retHm.get(key).getStream());

				//TODO
				retElement.setStatus(retHm.get(key).getStatus());

				//TODO serve?
				retElement.setTimestamp(retHm.get(key).getTimestamp());
				idRichieste=retHm.get(key).getGlobalReqId();
				ret.add(retElement);
			}

			outData.setDataBLockreport(ret);
			outData.setGlobalRequestId(idRichieste);

			//System.out.println(" TIMETIME inserimentoGeneralizzato -- fine --> "+System.currentTimeMillis());


		} catch (InsertApiBaseException insEx) {
			log.warn( "[InsertApi::insertApi] InsertApiBaseException "+insEx.getErrorCode() + " - " + insEx.getErrorName());

			outData.setInsertException((InsertApiBaseException)insEx);
		} catch (Exception e) {
			log.fatal( "[InsertApi::insertApi] GenericException "+e);
			
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			outData.setInsertException(newEx);
		} finally {
		
			log.info( "[InsertApi::inserimentoGeneralizzato] END ");
			//logAccounting.info(accLog.toString());	
			
		}

		return outData;
	}



	public void authenticate(HttpServletRequest request, String codTenant) {
		// not used: authentication made on api manager
		
	}
}
