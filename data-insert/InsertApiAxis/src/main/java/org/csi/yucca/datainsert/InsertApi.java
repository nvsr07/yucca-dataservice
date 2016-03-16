package org.csi.yucca.datainsert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/*
import java.util.logging.Level;
import java.util.logging.Logger;
*/


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.csi.yucca.datainsert.business.InsertApiLogic;
import org.csi.yucca.datainsert.dto.DatasetBulkInsert;
import org.csi.yucca.datainsert.dto.DatasetBulkInsertIOperationReport;
import org.csi.yucca.datainsert.dto.DatasetBulkInsertOutput;
import org.csi.yucca.datainsert.exception.InsertApiBaseException;
import org.csi.yucca.datainsert.util.AccountingLog;

public class InsertApi {


	private static final Log log=LogFactory.getLog("org.csi.yucca.datainsert");
	private static final Log logAccounting=LogFactory.getLog("sdpaccounting");

	public DatasetBulkInsertOutput insertApiDataset(String codTenant, String jsonData,String uniqueid,String forwardfor,String authInfo,String path) throws Exception{
		DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();
		InsertApiLogic insApiLogic=new InsertApiLogic();
		long starTtime=0;
		long deltaTime=-1;
		AccountingLog accLog=new AccountingLog();
		AccountingLog accLog1=new AccountingLog();

		try {
			starTtime=System.currentTimeMillis();


			accLog.setTenantcode(codTenant);
			accLog.setUniqueid(uniqueid);
			accLog.setForwardefor(forwardfor);
			accLog.setJwtData(authInfo);
			accLog.setPath(path);

			accLog1.setUniqueid(uniqueid);

			
			log.info( "[InsertApi::insertApiDataset] BEGIN ");

			//System.out.println(" TIMETIME insertApiDataset -- inizio --> "+System.currentTimeMillis());

			HashMap<String, DatasetBulkInsert>aaaaa = insApiLogic.parseJsonInputDataset(codTenant,jsonData);
			//System.out.println(" TIMETIME insertApiDataset -- parsing --> "+System.currentTimeMillis());


			outData=inserimentoGeneralizzato(codTenant, aaaaa);

			//System.out.println(" TIMETIME insertApiDataset -- fine --> "+System.currentTimeMillis());

			int inData=0;
			
			
			log.info( "[InsertApi::insertApiDataset] report inserimento: ");
			log.info( "[InsertApi::insertApiDataset]       globalRequestID --> " +outData.getGlobalRequestId());
			log.info( "[InsertApi::insertApiDataset]       error code      --> " +(outData.getInsertException()!=null ? outData.getInsertException().getErrorCode() : "NONE" ));
			log.info( "[InsertApi::insertApiDataset]       Numero Blocchi  --> " +(outData.getDataBLockreport()!=null ? outData.getDataBLockreport().size() : "WARNING: NONE" ));
			for (int i=0;outData.getDataBLockreport()!=null && i<outData.getDataBLockreport().size(); i++) {
				log.info( "[InsertApi::insertApiDataset]            blocco("+i+") status                  --> " +outData.getDataBLockreport().get(i).getStatus());
				log.info( "[InsertApi::insertApiDataset]            blocco("+i+") getNumRowToInsFromJson  --> " +outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				log.info( "[InsertApi::insertApiDataset]            blocco("+i+") getRequestId            --> " +outData.getDataBLockreport().get(i).getRequestId());
				accLog1.setDataIn(outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				accLog1.setDatasetcode(outData.getDataBLockreport().get(i).getIdDataset()+":"+outData.getDataBLockreport().get(i).getDatasetVersion());
				accLog1.setErrore(outData.getDataBLockreport().get(i).getStatus());
				logAccounting.info(accLog1.toString());	
				inData+=outData.getDataBLockreport().get(i).getNumRowToInsFromJson();
				
			}
			accLog.setDataIn(inData);

			

		} catch (InsertApiBaseException insEx) {
			log.warn( "[InsertApi::insertApiDataset] InsertApiBaseException "+insEx.getErrorCode() + " - " + insEx.getErrorName());
			accLog.setErrore(insEx.getErrorCode() + " - " + insEx.getErrorName());
			outData.setInsertException((InsertApiBaseException)insEx);
		} catch (Exception e) {
			log.fatal( "[InsertApi::insertApiDataset] GenericException "+e);
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			accLog.setErrore(newEx.getErrorCode() + " - " + newEx.getErrorName());
			outData.setInsertException(newEx);
		} finally {
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
				accLog.setElapsed(deltaTime);
				
			} catch (Exception e) {}
			logAccounting.info(accLog.toString());	
			log.info( "[InsertApi::insertApiDataset] END --> elapsed: "+deltaTime);
		}

		return outData;

	}


	private DatasetBulkInsertOutput inserimentoGeneralizzato(String codTenant,HashMap<String, DatasetBulkInsert>datiDains) throws Exception{
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

	public DatasetBulkInsertOutput insertApi(String codTenant, String jsonData,String uniqueid,String forwardfor,String authInfo,String path) throws Exception{
		DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();
		long starTtime=0;
		long deltaTime=-1;
		AccountingLog accLog=new AccountingLog();
		AccountingLog accLog1=new AccountingLog();

		try {
			starTtime=System.currentTimeMillis();
			log.info( "[InsertApi::insertApi] BEGIN ");
			accLog.setTenantcode(codTenant);
			accLog.setUniqueid(uniqueid);
			accLog.setForwardefor(forwardfor);
			accLog.setJwtData(authInfo);
			accLog.setPath(path);

			accLog1.setUniqueid(uniqueid);
//			accLog1.setTenantcode(codTenant);
//			accLog1.setForwardefor(forwardfor);
//			accLog1.setJwtData(authInfo);
//			accLog1.setPath(path);

			InsertApiLogic insApiLogic=new InsertApiLogic();



			HashMap<String, DatasetBulkInsert>aaaaa = insApiLogic.parseJsonInputStream(codTenant,jsonData);

			outData=inserimentoGeneralizzato(codTenant, aaaaa);

			int inData=0;
			log.info( "[InsertApi::insertApi] report inserimento: ");
			log.info( "[InsertApi::insertApi]       globalRequestID --> " +outData.getGlobalRequestId());
			log.info( "[InsertApi::insertApi]       error code      --> " +(outData.getInsertException()!=null ? outData.getInsertException().getErrorCode() : "NONE" ));
			log.info( "[InsertApi::insertApi]       Numero Blocchi  --> " +(outData.getDataBLockreport()!=null ? outData.getDataBLockreport().size() : "WARNING: NONE" ));
			for (int i=0;outData.getDataBLockreport()!=null && i<outData.getDataBLockreport().size(); i++) {
				log.info( "[InsertApi::insertApi]            blocco("+i+") status                  --> " +outData.getDataBLockreport().get(i).getStatus());
				log.info( "[InsertApi::insertApi]            blocco("+i+") getNumRowToInsFromJson  --> " +outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				log.info( "[InsertApi::insertApi]            blocco("+i+") getRequestId            --> " +outData.getDataBLockreport().get(i).getRequestId());
				accLog1.setDataIn(outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				accLog1.setDatasetcode(outData.getDataBLockreport().get(i).getIdDataset()+":"+outData.getDataBLockreport().get(i).getDatasetVersion());
				accLog1.setErrore(outData.getDataBLockreport().get(i).getStatus());
				logAccounting.info(accLog1.toString());	
				inData+=outData.getDataBLockreport().get(i).getNumRowToInsFromJson();
				
				
			}
accLog.setDataIn(inData);

		} catch (InsertApiBaseException insEx) {
			log.warn( "[InsertApi::insertApi] InsertApiBaseException "+insEx.getErrorCode() + " - " + insEx.getErrorName());
			accLog.setErrore(insEx.getErrorCode() + " - " + insEx.getErrorName());

			outData.setInsertException((InsertApiBaseException)insEx);
		} catch (Exception e) {
			log.fatal( "[InsertApi::insertApi] GenericException "+e);
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			accLog.setErrore(newEx.getErrorCode() + " - " + newEx.getErrorName());
			outData.setInsertException(newEx);
		} finally {
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
				accLog.setElapsed(deltaTime);
			} catch (Exception e) {}
			
			log.info( "[InsertApi::insertApi] END --> elapsed: "+deltaTime);
			logAccounting.info(accLog.toString());	

		}

		return outData;
	}



	public String copyData (String codTenant, String globalIdRequest) throws Exception{
		long starTtime=0;
		long deltaTime=-1;
		try {
			starTtime=System.currentTimeMillis();
			log.info( "[InsertApi::copyData] BEGIN ");
			InsertApiLogic insApiLogic=new InsertApiLogic();
			insApiLogic.copyData(codTenant, globalIdRequest);
		} catch (InsertApiBaseException insEx) {
			log.warn( "[InsertApi::copyData] InsertApiBaseException "+insEx.getErrorCode() + " - " + insEx.getErrorName());

		} catch (Exception e) {
			
			log.fatal( "[InsertApi::copyData] GenericException "+e);
		} finally {
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
			} catch (Exception e) {}
			
			log.info( "[InsertApi::copyData] END --> elapsed: "+deltaTime);

		}

		return "FineElaborazioneCopia";

	}

}