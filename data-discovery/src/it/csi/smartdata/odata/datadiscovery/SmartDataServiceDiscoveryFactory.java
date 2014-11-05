package it.csi.smartdata.odata.datadiscovery;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;

public class SmartDataServiceDiscoveryFactory extends ODataServiceFactory {
	static Logger log = Logger.getLogger(SmartDataServiceDiscoveryFactory.class);

	@Override
	public ODataService createService(ODataContext odc) throws ODataException {
		log.info("in SmartDataServiceDiscoveryFactory.createService odc " + odc);
		
		EdmProvider edmProvider = new SmartDataDiscoveryEdmProvider();

		ODataSingleProcessor singleProcessor = new SmartDataDiscoverySingleProcessor();

		return createODataSingleProcessorService(edmProvider, singleProcessor);
	}

}
