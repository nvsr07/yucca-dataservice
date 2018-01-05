package org.csi.yucca.dataservice.insertdataapi.metadata;

import org.csi.yucca.dataservice.insertdataapi.adminapi.SDPAdminApiAccess;
import org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertApiMongoDataAccess;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class SDPInsertMedataFactory {

	
	public static SDPInsertMetadataApiAccess getSDPInsertMetadataApiAccess()
	{
		if (SDPInsertApiConfig.instance.isSourceAdminApi())
		{
			return new SDPAdminApiAccess();
		}
		else {
			return new SDPInsertApiMongoDataAccess();
		}
			
	}
	
	
}
