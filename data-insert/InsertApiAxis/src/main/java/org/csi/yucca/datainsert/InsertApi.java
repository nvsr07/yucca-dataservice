package org.csi.yucca.datainsert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.csi.yucca.datainsert.business.InsertApiLogic;
import org.csi.yucca.datainsert.dto.DatasetBulkInsert;
import org.csi.yucca.datainsert.dto.DatasetBulkInsertIOperationReport;
import org.csi.yucca.datainsert.dto.DatasetBulkInsertOutput;
import org.csi.yucca.datainsert.exception.InsertApiBaseException;

public class InsertApi {


	private static final Logger log=Logger.getLogger("org.csi.yucca.datainsert");


	public DatasetBulkInsertOutput insertApiDataset(String codTenant, String jsonData) throws Exception{
		DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();
		InsertApiLogic insApiLogic=new InsertApiLogic();
		try {


			log.log(Level.INFO, "[InsertApi::insertApiDataset] BEGIN ");

			//System.out.println(" TIMETIME insertApiDataset -- inizio --> "+System.currentTimeMillis());

			HashMap<String, DatasetBulkInsert>aaaaa = insApiLogic.parseJsonInputDataset(codTenant,jsonData);
			//System.out.println(" TIMETIME insertApiDataset -- parsing --> "+System.currentTimeMillis());


			outData=inserimentoGeneralizzato(codTenant, aaaaa);

			//System.out.println(" TIMETIME insertApiDataset -- fine --> "+System.currentTimeMillis());

			log.log(Level.INFO, "[InsertApi::insertApiDataset] report inserimento: ");
			log.log(Level.INFO, "[InsertApi::insertApiDataset]       globalRequestID --> " +outData.getGlobalRequestId());
			log.log(Level.INFO, "[InsertApi::insertApiDataset]       error code      --> " +(outData.getInsertException()!=null ? outData.getInsertException().getErrorCode() : "NONE" ));
			log.log(Level.INFO, "[InsertApi::insertApiDataset]       Numero Blocchi  --> " +(outData.getDataBLockreport()!=null ? outData.getDataBLockreport().size() : "WARNING: NONE" ));
			for (int i=0;outData.getDataBLockreport()!=null && i<outData.getDataBLockreport().size(); i++) {
				log.log(Level.INFO, "[InsertApi::insertApiDataset]            blocco("+i+") status                  --> " +outData.getDataBLockreport().get(i).getStatus());
				log.log(Level.INFO, "[InsertApi::insertApiDataset]            blocco("+i+") getNumRowToInsFromJson  --> " +outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				log.log(Level.INFO, "[InsertApi::insertApiDataset]            blocco("+i+") getRequestId            --> " +outData.getDataBLockreport().get(i).getRequestId());
			}
			
			

		} catch (InsertApiBaseException insEx) {
			log.log(Level.WARNING, "[InsertApi::insertApiDataset] InsertApiBaseException "+insEx.getErrorCode() + " - " + insEx.getErrorName());
			outData.setInsertException((InsertApiBaseException)insEx);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[InsertApi::insertApiDataset] GenericException "+e);
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			outData.setInsertException(newEx);
		} finally {
			log.log(Level.INFO, "[InsertApi::insertApiDataset] BEGIN ");
		}

		return outData;

	}


	private DatasetBulkInsertOutput inserimentoGeneralizzato(String codTenant,HashMap<String, DatasetBulkInsert>datiDains) throws Exception{
		DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();

		try {

			log.log(Level.FINE, "[InsertApi::inserimentoGeneralizzato] BEGIN ");

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
			log.log(Level.WARNING, "[InsertApi::insertApi] InsertApiBaseException "+insEx.getErrorCode() + " - " + insEx.getErrorName());

			outData.setInsertException((InsertApiBaseException)insEx);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[InsertApi::insertApi] GenericException "+e);
			
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			outData.setInsertException(newEx);
		} finally {
			log.log(Level.FINE, "[InsertApi::inserimentoGeneralizzato] END ");
			
		}

		return outData;
	}

	public DatasetBulkInsertOutput insertApi(String codTenant, String jsonData) throws Exception{
		DatasetBulkInsertOutput outData=new DatasetBulkInsertOutput();

		try {
			log.log(Level.INFO, "[InsertApi::insertApi] BEGIN ");


			InsertApiLogic insApiLogic=new InsertApiLogic();



			HashMap<String, DatasetBulkInsert>aaaaa = insApiLogic.parseJsonInputStream(codTenant,jsonData);

			outData=inserimentoGeneralizzato(codTenant, aaaaa);

			log.log(Level.INFO, "[InsertApi::insertApi] report inserimento: ");
			log.log(Level.INFO, "[InsertApi::insertApi]       globalRequestID --> " +outData.getGlobalRequestId());
			log.log(Level.INFO, "[InsertApi::insertApi]       error code      --> " +(outData.getInsertException()!=null ? outData.getInsertException().getErrorCode() : "NONE" ));
			log.log(Level.INFO, "[InsertApi::insertApi]       Numero Blocchi  --> " +(outData.getDataBLockreport()!=null ? outData.getDataBLockreport().size() : "WARNING: NONE" ));
			for (int i=0;outData.getDataBLockreport()!=null && i<outData.getDataBLockreport().size(); i++) {
				log.log(Level.INFO, "[InsertApi::insertApi]            blocco("+i+") status                  --> " +outData.getDataBLockreport().get(i).getStatus());
				log.log(Level.INFO, "[InsertApi::insertApi]            blocco("+i+") getNumRowToInsFromJson  --> " +outData.getDataBLockreport().get(i).getNumRowToInsFromJson());
				log.log(Level.INFO, "[InsertApi::insertApi]            blocco("+i+") getRequestId            --> " +outData.getDataBLockreport().get(i).getRequestId());
			}


		} catch (InsertApiBaseException insEx) {
			log.log(Level.WARNING, "[InsertApi::insertApi] InsertApiBaseException "+insEx.getErrorCode() + " - " + insEx.getErrorName());

			outData.setInsertException((InsertApiBaseException)insEx);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[InsertApi::insertApi] GenericException "+e);
			InsertApiBaseException newEx=new InsertApiBaseException("UNKNOWN");
			outData.setInsertException(newEx);
		} finally {
			log.log(Level.INFO, "[InsertApi::insertApi] END ");

		}

		return outData;
	}



	public void copyData (String codTenant, String globalIdRequest) throws Exception{
		try {
			log.log(Level.INFO, "[InsertApi::copyData] BEGIN ");
			InsertApiLogic insApiLogic=new InsertApiLogic();
			insApiLogic.copyData(codTenant, globalIdRequest);
		} catch (InsertApiBaseException insEx) {
			log.log(Level.WARNING, "[InsertApi::copyData] InsertApiBaseException "+insEx.getErrorCode() + " - " + insEx.getErrorName());

		} catch (Exception e) {
			log.log(Level.SEVERE, "[InsertApi::copyData] GenericException "+e);
		} finally {
			log.log(Level.INFO, "[InsertApi::copyData] END ");

		}


	}

}