package org.csi.yucca.dataservice.binaryapi.dao;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.binaryapi.model.api.MediaObject;
import org.csi.yucca.dataservice.binaryapi.model.metadata.BinaryData;

public class InsertAPIBinaryDAO {
	static Logger log = Logger.getLogger(InsertAPIBinaryDAO.class);

	public InsertAPIBinaryDAO() {
	}

	public void createBinary(BinaryData binary) {
		try {
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
			newObj.setIdDataset(binary.getIdDataset());
			newObj.setDatasetVersion(binary.getDatasetVersion());
			
			

		} catch (Exception e) {
			log.error("[] - ERROR in insert. Message: " + e.getMessage());
		}
	}
}
