package org.csi.yucca.dataservice.binaryapi.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.binaryapi.delegate.HttpDelegate;
import org.csi.yucca.dataservice.binaryapi.model.api.InsertObject;
import org.csi.yucca.dataservice.binaryapi.model.api.MediaObject;
import org.csi.yucca.dataservice.binaryapi.model.metadata.BinaryData;
import org.csi.yucca.dataservice.binaryapi.model.tenantin.TenantIn;
import org.csi.yucca.dataservice.binaryapi.mongo.singleton.Config;
import org.csi.yucca.dataservice.binaryapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class InsertAPIBinaryDAO {
	static Logger log = Logger.getLogger(InsertAPIBinaryDAO.class);

	public InsertAPIBinaryDAO() {
	}

	public void createBinary(BinaryData binary) {
		try {
			Gson gson = JSonHelper.getInstance();
			MediaObject newObj = new MediaObject();

			newObj.setTenantBinary(binary.getTenantBinary());
			newObj.setFilenameBinary(binary.getFilenameBinary());
			newObj.setIdBinary(binary.getIdBinary());
			newObj.setSizeBinary(binary.getSizeBinary());
			newObj.setContentTypeBinary(binary.getContentTypeBinary());
			newObj.setAliasNameBinary(binary.getAliasNameBinary());
			newObj.setPathHdfsBinary(binary.getPathHdfsBinary());
			newObj.setInsertDateBinary(new java.util.Date().toString());
			newObj.setLastUpdateDateBinary(new java.util.Date().toString());
			
			log.info("[InsertAPIBinaryDAO:createBinary] - newObj = " + gson.toJson(newObj));
			
			InsertObject data = new InsertObject();
			data.setDatasetCode(binary.getDatasetCode());
			data.addMediaObject(newObj);
			List<InsertObject> dataInsert = new ArrayList<InsertObject>();
			dataInsert.add(data);
			
			log.info("[InsertAPIBinaryDAO:createBinary] - dataInsert = " + gson.toJson(dataInsert));
			
			String tenantDetailUrl = Config.getInstance().getApiAdminServicesUrl() + "/tenants/" + binary.getTenantBinary();
			String tenantDetailString = HttpDelegate.executeGet(tenantDetailUrl, null, null, null);
			log.info("[InsertAPIBinaryDAO:createBinary] - tenantDetailString (executeGet) = " + tenantDetailString);
			TenantIn tenantin = gson.fromJson(tenantDetailString, TenantIn.class);
			String tenantPassword = tenantin.getTenants().getTenant().getTenantPassword();

			String insertApiUrl = Config.getInstance().getDataInsertBaseUrl() + binary.getTenantBinary();

			log.info("[InsertAPIBinaryDAO:createBinary] - insertApiUrl = " + insertApiUrl);
			log.info("[InsertAPIBinaryDAO:createBinary] - binary.getTenantBinary() = " + binary.getTenantBinary());
			log.info("[InsertAPIBinaryDAO:createBinary] - tenantPassword = " + tenantPassword);

			String executePost = HttpDelegate.executePost(insertApiUrl, binary.getTenantBinary(), tenantPassword, null, null, null, gson.toJson(dataInsert));

			log.info("[InsertAPIBinaryDAO:createBinary] - executePost = " + executePost);
			
			if (executePost == null){
				throw new Exception("impossibile memorizzare i dati!");
			}

		} catch (Exception e) {
			log.error("[] - ERROR in insert. Message: " + e.getMessage());
		}
	}
}
