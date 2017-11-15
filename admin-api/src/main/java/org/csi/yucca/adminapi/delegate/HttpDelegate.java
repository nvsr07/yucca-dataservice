package org.csi.yucca.adminapi.delegate;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpDelegate {
	private static final Logger logger = Logger.getLogger(HttpDelegate.class);

	public static String makeHttpPost(CloseableHttpClient httpclient, String url, List<NameValuePair> params) throws HttpException, IOException {
		logger.debug("[HttpDelegate::makeHttpPost] url " + url + " params " + explainParams(params));

		HttpPost postMethod = new HttpPost(url);
		postMethod.setEntity(new UrlEncodedFormEntity(params));
		CloseableHttpResponse response = httpclient.execute(postMethod);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			try {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity);
			} finally {
				response.close();
			}
		} else {
			logger.error("[HttpDelegate::makeHttpPost] ERROR Status code " + statusCode);
			throw new HttpException("ERROR: Status code " + statusCode);
		}
	}
	
	
	public static String explainParams(List<NameValuePair> params) {
		String result = "";
		if (params != null)
			for (NameValuePair nameValuePair : params) {
				result += nameValuePair.getName() + "=" + nameValuePair.getValue() + "&";
			}
		return result;
	}
}
