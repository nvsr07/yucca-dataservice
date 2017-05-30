package it.csi.smartdata.dataapi.multiapi.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.multiapi.constants.SDPDataMultiApiConstants;
import it.csi.smartdata.dataapi.odata.SDPExpressionVisitor;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.core.servlet.ODataServlet;

public class SDPOdataMultiApiServlet extends OlingoServletRewrite {
	
	static Logger log = Logger.getLogger(SDPOdataMultiApiServlet.class.getPackage().getName());
	
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
		log.info("[SDPOdataMultiApiServlet:service] ");
		if (req!=null)
		{
			log.info("[SDPOdataMultiApiServlet:service] method begin"+req.getRequestURI()+"|"+req.getRequestURL());
		}
		req.setAttribute(ODataServiceFactory.FACTORY_LABEL, "org.apache.olingo.odata2.service.factory");
		super.service(req, resp);
		log.info("[SDPOdataMultiApiServlet:service] method end");
	}
	
	
	 public String getInitParameter(String name) { 
		 String className= SDPDataMultiApiConstants.SDP_ODATA_DEFAULT_SERVICE_FACTORY;
		 if (name.equalsIgnoreCase(ODataServiceFactory.FACTORY_LABEL)) {
			 log.info("[SDPOdataMultiApiServlet::init] Servlet SDPOdataMultiApiServlet inizializzata");
			return className;
		 } else {
			 log.info("[SDPOdataMultiApiServlet::init] Servlet SDPOdataMultiApiServlet inizializzata");
			 return super.getInitParameter(name);
		 }
	 }

}
