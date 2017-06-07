package it.csi.smartdata.dataapi.multiapi.servlet;


import it.csi.smartdata.dataapi.multiapi.constants.SDPDataMultiApiConstants;
import it.csi.smartdata.dataapi.multiapi.proxy.OdataSingleton;
import it.csi.smartdata.dataapi.util.AccountingLog;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class SDPOdataMultiApiFilter implements Filter{

	static Logger log = Logger.getLogger(SDPOdataMultiApiFilter.class.getPackage().getName());

	static Logger logAccounting= Logger.getLogger("accounting");

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
		log.info("[SDPOdataMultiApiFilter::doFilter] BEGIN");
		AccountingLog accLog=new AccountingLog(); 
		long starTtime=0;
		long deltaTime=-1;
		
		
		try { 
			starTtime=System.currentTimeMillis();
			HttpServletRequest request = (HttpServletRequest) req;
			/* LOG HEADER TMP */
			String jwt="-";
			String forwardefor="-";
			String uniqueid="-";

			Enumeration<String> headerNames = request.getHeaderNames();
			String headerName ="";
			String headerValue ="";
			
			String logAccountingMessage="";
			while (headerNames.hasMoreElements()) {
				headerName = headerNames.nextElement();
				headerValue = request.getHeader(headerName);
				
				if ("UNIQUE_ID".equals(headerName)) uniqueid=headerValue;
				else if ("X-Forwarded-For".equals(headerName)) forwardefor=headerValue;
				else if ("X-JWT-Assertion".equals(headerName)) jwt=headerValue;
				
			}
			
			accLog.setUniqueid(uniqueid);
			accLog.setForwardefor(forwardefor);
			accLog.setJwtData(jwt);
			
			String requestURI = request.getRequestURI();
			log.info("[SDPOdataMultiApiFilter::doFilter] requestURI="+requestURI);

			
			String webFilterPattern=SDPDataMultiApiConstants.SDP_WEB_FILTER_PATTERN;
			String webServletUrl=SDPDataMultiApiConstants.SDP_WEB_SERVLET_URL;

		
			log.debug("[SDPOdataMultiApiFilter::doFilter] webFilterPattern="+webFilterPattern);
			log.debug("[SDPOdataMultiApiFilter::doFilter] webServletUrl="+webServletUrl);

			if (requestURI.indexOf(webFilterPattern)!=-1) {
				log.debug("[SDPOdataMultiApiFilter::doFilter] FILTERING");

				String prima=requestURI.substring(0,requestURI.indexOf(webFilterPattern));
				String dopo="";

				String codiceApi=requestURI.substring(prima.length()+webFilterPattern.length());

				

				
				
				int indicea=requestURI.indexOf("/",requestURI.indexOf(webFilterPattern)+webFilterPattern.length()+1);
				int indiceb=requestURI.indexOf("$",requestURI.indexOf(webFilterPattern)+webFilterPattern.length()+1);

				if (indicea>0 || indiceb>0 ) {
					indicea=indicea>0 ? indicea : 1000000;
					indiceb=indiceb>0 ? indiceb : 1000000;
					int indice= indicea<indiceb ?indicea : indiceb;
					if (indicea<indiceb) {
						dopo=requestURI.substring(indice+1);
						if (dopo.length()>0) codiceApi=codiceApi.substring(0, codiceApi.indexOf(dopo)-1);
						else codiceApi=codiceApi.substring(0, codiceApi.length()-1);
					} else {
						dopo=requestURI.substring(indice);
						if (dopo.length()>0) codiceApi=codiceApi.substring(0, codiceApi.indexOf(dopo));

					}
				} else {
					//codiceApi=codiceApi.substring(0, codiceApi.length()-1);
				}

				String dopoCodiceApi = StringUtils.substringAfter(requestURI, codiceApi+"/");

				String maybeDataset = StringUtils.substringBefore(dopoCodiceApi, "/");
				
				if (maybeDataset!=null && maybeDataset.contains("__"))
				{
					log.info("[SDPOdataMultiApiFilter::doFilter] Url da proxare, dopoCodiceApi:"+dopoCodiceApi+", maybeDataset:"+maybeDataset);
					
					String dataset = StringUtils.substringBeforeLast(maybeDataset, "__");
					String entity =StringUtils.substringAfterLast(maybeDataset, "__");
					
					String pathOther = StringUtils.substringAfter(dopoCodiceApi, maybeDataset+"/");
					
// TODO: manage invalid access to api from here... (i.e. $metadata is not valid for single api)
//					if (pathOther!=null && StringUtils.contains(pathOther, "$metadata"))
//						OdataSingleton.INSTANCE.getOdataResponse(dataset+"/"+pathOther,req.getParameterMap(), res);
//					else
						OdataSingleton.INSTANCE.getOdataResponse(dataset+"/"+entity+"/"+pathOther,req.getParameterMap(), res);
					log.info("[SDPOdataMultiApiFilter::doFilter] END");
					return;
					
				}
				else {
					log.info("[SDPOdataMultiApiFilter::doFilter] Url da NOproxare, dopoCodiceApi:"+dopoCodiceApi);
				}

				
				
				
				String newURI=webServletUrl+dopo+"?codiceApi="+codiceApi+"&apacheUniqueId="+uniqueid;
				
				accLog.setPath(dopo);
				accLog.setApicode(codiceApi);
				

				

				log.info("[SDPOdataMultiApiFilter::doFilter] codiceApi="+codiceApi);
				log.info("[SDPOdataMultiApiFilter::doFilter] newURI="+newURI);
				req.getRequestDispatcher(newURI).forward(req, res);
				log.info("[SDPOdataMultiApiFilter::doFilter] END");
			} else {
				log.debug("[SDPOdataMultiApiFilter::doFilter] NO FILTER");

				chain.doFilter(req, res);
			}
			
			
		} catch (Exception e) {
			log.error("[SDPOdataMultiApiFilter::doFilter] " ,e);

			accLog.setErrore(e.getMessage());
			
			if (e instanceof ServletException) throw (ServletException)e;
			if (e instanceof IOException) throw (IOException)e;
			throw new ServletException(e);

		} finally {
			try {
				deltaTime=System.currentTimeMillis()-starTtime;
				accLog.setElapsed(deltaTime);
			} catch (Exception e) {}
			logAccounting.info(accLog.toString());				
			log.debug("[SDPOdataMultiApiFilter::doFilter] END");

		}
	}

	@Override
	public void destroy() {
		//
	}
	@Override
	public void init(FilterConfig config) throws ServletException {
		//
	}

	
}