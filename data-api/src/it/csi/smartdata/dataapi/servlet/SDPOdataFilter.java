package it.csi.smartdata.dataapi.servlet;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


public class SDPOdataFilter implements Filter{

	static Logger log = Logger.getLogger(SDPOdataFilter.class.getPackage().getName());


	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
		log.debug("[SDPOdataFilter::doFilter] BEGIN");
		try { 
			HttpServletRequest request = (HttpServletRequest) req;
			String requestURI = request.getRequestURI();
			log.debug("[SDPOdataFilter::doFilter] requestURI="+requestURI);

			String webFilterPattern=SDPDataApiConstants.SDP_WEB_FILTER_PATTERN;
			String webServletUrl=SDPDataApiConstants.SDP_WEB_SERVLET_URL;

			try { 
				webFilterPattern=SDPDataApiConfig.getInstance().getWebFilterPattern();
			} catch (Exception e) {

			}
			try { 
				webServletUrl=SDPDataApiConfig.getInstance().getWebServletUrl();
			} catch (Exception e) {

			}
			log.debug("[SDPOdataFilter::doFilter] webFilterPattern="+webFilterPattern);
			log.debug("[SDPOdataFilter::doFilter] webServletUrl="+webServletUrl);

			if (requestURI.indexOf(webFilterPattern)!=-1) {
				log.debug("[SDPOdataFilter::doFilter] FILTERING");


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
				
//				if (indice>0 && indice>requestURI.indexOf("$",requestURI.indexOf(webFilterPattern)+webFilterPattern.length()+1)) 
//					indice=requestURI.indexOf("$",requestURI.indexOf(webFilterPattern)+webFilterPattern.length()+1);
//				if (indice>0 && indice>requestURI.indexOf("$",requestURI.indexOf(webFilterPattern)+webFilterPattern.length()+1)) 
//					indice=requestURI.indexOf("$",requestURI.indexOf(webFilterPattern)+webFilterPattern.length()+1);

				
				
				
//				if (requestURI.indexOf("/",requestURI.indexOf(webFilterPattern)+webFilterPattern.length()+1)>0) {
//					dopo=requestURI.substring(requestURI.indexOf("/",requestURI.indexOf(webFilterPattern)+webFilterPattern.length()+1)+1);
//				}
//						
//						requestURI.substring(requestURI.indexOf("/",requestURI.indexOf(webFilterPattern)+webFilterPattern.length()+1)+1);




				//if (dopo.length()>0) codiceApi=codiceApi.substring(0, codiceApi.indexOf(dopo)-1);
				//else codiceApi=codiceApi.substring(0, codiceApi.length()-1);
				String newURI=webServletUrl+dopo+"?codiceApi="+codiceApi;
				
				//YUCCA-345
				if ("csv".equals(request.getParameter("$format"))) {
					newURI=SDPDataApiConstants.SDP_WEB_CSVSERVLET_URL+dopo+"?codiceApi="+codiceApi+"&restoUri="+dopo;
				}


				if (request.getQueryString()!=null && request.getQueryString().length()>0) newURI=newURI+"&"+request.getQueryString();

				//YUCCA-345
				if ("csv".equals(request.getParameter("$format"))) {

					CharSequence toFind="$format=csv";
					CharSequence toreplace="$format=json";
					
					newURI=newURI.replace(toFind, toreplace);
				}
				
				
				log.info("[SDPOdataFilter::doFilter] codiceApi="+codiceApi);
				log.info("[SDPOdataFilter::doFilter] newURI="+newURI);
				req.getRequestDispatcher(newURI).forward(req, res);
			} else {
				log.debug("[SDPOdataFilter::doFilter] NO FILTER");
				
				chain.doFilter(req, res);
			}
		} catch (Exception e) {
			log.error("[SDPOdataFilter::doFilter] " +e);
			
			if (e instanceof ServletException) throw (ServletException)e;
			if (e instanceof IOException) throw (IOException)e;
			throw new ServletException(e.getMessage());
			
		} finally {
			log.debug("[SDPOdataFilter::doFilter] END");

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
