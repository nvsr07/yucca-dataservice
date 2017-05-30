package it.csi.smartdata.dataapi.multiapi.odata;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.odata.SDPEdmProvider;
import it.csi.smartdata.dataapi.odata.SDPSingleProcessor;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;

public class SDPMultiApiServiceFactory extends ODataServiceFactory {
	static Logger log = Logger.getLogger(SDPMultiApiServiceFactory.class.getPackage().getName());

	@Override
	public ODataService createService(ODataContext odc) throws ODataException {
		log.debug("[SDPMultiApiServiceFactory::createService] BEGIN");

		try {

			log.debug("[SDPMultiApiServiceFactory::createService] odc="+odc);
			String uri=odc.getPathInfo().getRequestUri().toString();
			String root=odc.getPathInfo().getServiceRoot().toString();
			String codiceApi=null;
			String apacheUniqueId=null;
			String queryUrl=odc.getPathInfo().getRequestUri().getQuery();
			StringTokenizer st= new StringTokenizer(queryUrl, "&",false);
			while (st.hasMoreTokens()) {
				String curr=st.nextToken();
				if (curr.indexOf("codiceApi")!=-1) codiceApi=curr.substring(curr.indexOf("=")+1);
				if (curr.indexOf("apacheUniqueId")!=-1) apacheUniqueId=curr.substring(curr.indexOf("=")+1);
				
				
			}



			String resto=uri.substring(root.length());


			log.debug("[SDPMultiApiServiceFactory::createService] uri " + uri);
			log.debug("[SDPMultiApiServiceFactory::createService] root " + root);
			log.debug("[SDPMultiApiServiceFactory::createService] uri " + resto);


			SDPEdmProvider edmProvider = new SDPEdmProvider();
			edmProvider.setCodiceApi(codiceApi);
			SDPSingleProcessor singleProcessor = new SDPSingleProcessor();

			String webBaseUrl=null;
			try {
				webBaseUrl=SDPDataApiConfig.getInstance().getWebBaseUrl();
				if (!webBaseUrl.toLowerCase().startsWith("http") ){
					if (!webBaseUrl.startsWith("/")) webBaseUrl="/"+webBaseUrl; 
					
//					webBaseUrl=odc.getPathInfo().getServiceRoot().getScheme()+"://"+
//					odc.getPathInfo().getServiceRoot().getHost()+":"+
//					odc.getPathInfo().getServiceRoot().getPort()+webBaseUrl;
					webBaseUrl="https://"+
					odc.getPathInfo().getServiceRoot().getHost()+":"+
					odc.getPathInfo().getServiceRoot().getPort()+webBaseUrl;
				}
				
				
				
				webBaseUrl="https://"+SDPDataApiConfig.getInstance().getPubUri();
				
				
			} catch (Exception e ) {
				log.warn("[SDPMultiApiServiceFactory::createService] error (ignored) handling webBaseUrl   "+e);
			}
			if (null!=webBaseUrl) singleProcessor.setBaseUrl(webBaseUrl+codiceApi+"/");
			singleProcessor.setCodiceApi(codiceApi);
			singleProcessor.setApacheUniqueId(apacheUniqueId);

			return createODataSingleProcessorService((EdmProvider )edmProvider, singleProcessor);

		} catch (Exception e ) {
			log.error("[SDPMultiApiServiceFactory::createService] "+e);
			if (e instanceof ODataException) throw (ODataException)e;
			throw new ODataException(e);
		} finally {
			log.debug("[SDPMultiApiServiceFactory::createService] END");
		}

	}

}
