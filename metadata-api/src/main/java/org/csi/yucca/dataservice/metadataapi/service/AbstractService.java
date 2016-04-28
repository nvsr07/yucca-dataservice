package org.csi.yucca.dataservice.metadataapi.service;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Config;

public abstract class AbstractService {

	protected String STORE_BASE_URL = Config.getInstance().getStoreBaseUrl();
	protected String MANAGEMENT_BASE_URL = Config.getInstance().getManagementBaseUrl();

	static Logger log = Logger.getLogger(AbstractService.class);

	public AbstractService() {
	}

	protected String doPost(String targetUrl, String contentType, String characterEncoding, Map<String, String> parameters) {
		log.debug("[AbstractService::doPost] START");
		String result = "";
		int resultCode = -1;
		try {


			if (contentType == null)
				contentType = "application/json";
			if (characterEncoding == null)
				characterEncoding = "UTF-8";

			log.debug("[AbstractService::doPost] - targetUrl: " + targetUrl);
			
			if (parameters != null) {
				for (String key : parameters.keySet()) {
					//post.addParameter(key, parameters.get(key));
					targetUrl += key+"="+parameters.get(key)+"&";
				}
			}

			PostMethod post = new PostMethod(targetUrl);
			post.setRequestHeader("Content-Type", contentType);

			HttpClient httpclient = new HttpClient();
			try {
				resultCode = httpclient.executeMethod(post);
				log.debug("[AbstractService::doPost] - post result: " + resultCode);
				result = post.getResponseBodyAsString();
			} finally {
				post.releaseConnection();
			}

		} catch (IOException e) {
			log.error("[AbstractService::doPost] ERROR IOException: " + e.getMessage());
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode("" + resultCode);
			error.setMessage(e.getMessage());
			result = error.toJson();
		} finally {
			log.debug("[AbstractService::doPost] END");
		}
		return result;
	}
	


}
