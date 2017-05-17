package it.csi.smartdata.dataapi.servlet;

import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.commons.HttpHeaders;
import org.apache.olingo.odata2.api.commons.ODataHttpMethod;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotAcceptableException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.core.ODataContextImpl;
import org.apache.olingo.odata2.core.ODataRequestHandler;
import org.apache.olingo.odata2.core.servlet.ODataExceptionWrapper;
import org.apache.olingo.odata2.core.servlet.ODataServlet;
import org.apache.olingo.odata2.core.servlet.RestUtil;
public class SDPOdataServlet extends ODataServlet {
	
	// TODO Logger ... .verificare se questa classe serve o se si puo usare la servlet di defaul
	
	
	
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
//		String newUri=req.getRequestURI().replace("/CSP/", "/");
//		req.getRequestDispatcher(newUri);
//		
		req.setAttribute(ODataServiceFactory.FACTORY_LABEL, "org.apache.olingo.odata2.service.factory");
		super.service(req, resp);
	}
	
		

	
	
	 public String getInitParameter(String name) {
		 String className=SDPDataApiConstants.SDP_ODATA_DEFAULT_SERVICE_FACTORY;
		 if (name.equalsIgnoreCase(ODataServiceFactory.FACTORY_LABEL)) {
			return className;
		 } else {
			 return super.getInitParameter(name);
		 }
		 
	 }

}
