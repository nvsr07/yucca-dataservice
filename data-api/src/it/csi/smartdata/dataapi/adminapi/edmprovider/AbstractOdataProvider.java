package it.csi.smartdata.dataapi.adminapi.edmprovider;

import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;

public abstract class AbstractOdataProvider implements SchemaOdataProvider {
	public static Logger log = Logger.getLogger(StreamOdataProvider.class.getPackage()
			.getName());

	public AbstractOdataProvider() {
		super();
	}

	@Override
		public EntityContainerInfo getEntityContainerInfo(final String name, BackofficeDettaglioApiResponse dettaglio) throws ODataException {
		try {
			log.debug("[AbstractOdataProvider::getEntityContainerInfo] BEGIN");
			log.debug("[AbstractOdataProvider::getEntityContainerInfo] name="+name);

			EntityContainerInfo eci = null;
			String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+dettaglio.getEntitynamespace().replace('.', '_');
			//entContainerDB=SMART_ENTITY_CONTAINER;

			if (name == null || entContainerDB.equals(name)) {
				eci = new EntityContainerInfo().setName(entContainerDB).setDefaultEntityContainer(true);
			}
			
			return eci;

		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getEntityContainerInfo] " + e);
			if (e instanceof ODataException) throw (ODataException)e;
			throw new ODataException(e);
		} finally {
			log.debug("[SDPMongoOdataCast::getEntityContainerInfo] END");
		}		
	}

}