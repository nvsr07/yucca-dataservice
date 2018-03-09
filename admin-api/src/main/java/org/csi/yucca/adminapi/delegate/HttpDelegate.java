package org.csi.yucca.adminapi.delegate;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpDelegate {
	
	private static final Logger logger = Logger.getLogger(HttpDelegate.class);

	public static String makeHttpDelete(String url, String basicAuthUsername, String basicAuthPassword) throws HttpException, IOException {
		return makeHttpDelete(null, url, basicAuthUsername, basicAuthPassword);
	}
	
	public static String makeHttpDelete(
			CloseableHttpClient httpclient, 
			String url,  
			String basicAuthUsername, 
			String basicAuthPassword) throws HttpException, IOException {
		
		logger.debug("[HttpDelegate::makeHttpDelete] url " + url );

		HttpDelete httpDelete =  new HttpDelete(url);
		
		if (basicAuthUsername != null && basicAuthPassword != null) {
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(basicAuthUsername, basicAuthPassword);
			httpDelete.addHeader(new BasicScheme().authenticate(creds, httpDelete, null));
		}
		
		if(httpclient==null){
			httpclient = HttpClients.createDefault();
		}

		CloseableHttpResponse response = httpclient.execute(httpDelete);
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
			logger.error("[HttpDelegate::makeHttpDelete] ERROR Status code " + statusCode);
			throw new HttpException("ERROR: Status code " + statusCode);
		}
	}
	
	public static String makeHttpPost(CloseableHttpClient httpclient, String url, List<NameValuePair> params) throws HttpException, IOException {
		return makeHttpPost(httpclient, url, params, null, null, null, null);
	}

	public static String makeHttpPost(
			CloseableHttpClient httpclient, 
			String url, 
			List<NameValuePair> params, 
			String basicAuthUsername, 
			String basicAuthPassword, 
			String stringData) throws HttpException, IOException {
		return makeHttpPost(httpclient, url, params, basicAuthUsername, basicAuthPassword, stringData, null);
	}
	
	public static String makeHttpPost(
			
			CloseableHttpClient httpclient, 
			String url, 
			List<NameValuePair> params, 
			String basicAuthUsername, 
			String basicAuthPassword, 
			String stringData, ContentType contentType) throws HttpException, IOException {
		
		logger.debug("[HttpDelegate::makeHttpPost] url " + url + " params " + explainParams(params));

		HttpPost postMethod = new HttpPost(url);
		if(params!=null)
			postMethod.setEntity(new UrlEncodedFormEntity(params));
		
		if (basicAuthUsername != null && basicAuthPassword != null) {
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(basicAuthUsername, basicAuthPassword);
			postMethod.addHeader(new BasicScheme().authenticate(creds, postMethod, null));
		}
		if(stringData!=null){
			StringEntity  requestEntity = new StringEntity(stringData, contentType == null ? ContentType.APPLICATION_JSON : contentType);
			postMethod.setEntity(requestEntity);
		}
		
		if(httpclient==null){
			httpclient = HttpClients.createDefault();
		}

		CloseableHttpResponse response = httpclient.execute(postMethod);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_ACCEPTED) {
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
