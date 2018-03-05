package org.csi.yucca.adminapi.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * TODO  add cache
 */
public class AdminApiClientDelegate {

	/**
	 * 
	 * @param url
	 * @param clazz
	 * @param loggerName
	 * @param json
	 * @return
	 * @throws AdminApiClientException
	 */
	public static <T> T postFromAdminApi(final Class<T> cl, String url, String loggerName, String json) throws AdminApiClientException {
		return postFromAdminApi(cl, url, loggerName, null, json);
	}

	/**
	 * 
	 * @param url
	 * @param cl
	 * @param loggerName
	 * @param params
	 * @param json
	 * @return
	 * @throws AdminApiClientException
	 */
	public static <T> T postFromAdminApi(final Class<T> cl, String url, String loggerName, Map<String, String> params, String json) throws AdminApiClientException {

		CloseableHttpClient httpClient = Singleton.Client.get();
		
		Logger logger = Logger.getLogger(loggerName+".AdminApiClientDelegate");
		
		try {
			HttpPost httpPost = getHttpPost(url, params);
		    StringEntity entity = new StringEntity(json);
		    httpPost.setEntity(entity);
		    httpPost.setHeader("Accept", "application/json");
		    httpPost.setHeader("Content-type", "application/json");
			ResponseHandler<T> responseHandler = getResponseHandler(cl);
			
			return httpClient.execute(httpPost, responseHandler);
		} 
		catch (URISyntaxException e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		}
		catch (ClientProtocolException e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		} catch (IOException e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		} catch (Exception e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		}
		
		
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	private static HttpPost getHttpPost(String url, Map<String, String> params) throws URISyntaxException {
		
		URIBuilder urib = new URIBuilder(url);
		
		if (params != null && !params.isEmpty()) {
			Iterator<Entry<String, String>> iter = params.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> valore = iter.next();
				urib.addParameter(valore.getKey(), valore.getValue());
			}
		}

		HttpPost httpPost = new HttpPost(urib.build());

		return httpPost;
	}

	/**
	 * 
	 * @param cl
	 * @return
	 */
	private static <T> ResponseHandler<T> getResponseHandler(final Class<T> cl){
		
		ResponseHandler<T> responseHandler = new ResponseHandler<T>() {

			ObjectMapper mapper = new ObjectMapper();

			@Override
			public T handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {

				int status = response.getStatusLine().getStatusCode();
				
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? mapper.readValue(EntityUtils.toString(entity), cl) : null;
				} 
				else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
			
		};

		return responseHandler;
	}

	/**
	 * 
	 * @param cl
	 * @return
	 */
	private static <T> ResponseHandler<List<T>> getListResponseHandler(final Class<T> cl){

		ResponseHandler<List<T>> responseHandler = new ResponseHandler<List<T>>() {

			ObjectMapper mapper = new ObjectMapper();

			@Override
			public List<T> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {

				int status = response.getStatusLine().getStatusCode();
				
				if (status >= 200 && status < 300) {

					HttpEntity entity = response.getEntity();
					
					List<T> myObjects = mapper.readValue(EntityUtils.toString(entity), mapper.getTypeFactory().constructCollectionType(List.class, cl));

					return myObjects != null ? myObjects : null;
					
				} 
				else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
			
		};
		
		return responseHandler;
	}
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	private static HttpGet getHttpGet(String url, Map<String, String> params) throws URISyntaxException {
		
		URIBuilder urib = new URIBuilder(url);
		
		if (params != null && !params.isEmpty()) {
			Iterator<Entry<String, String>> iter = params.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> valore = iter.next();
				urib.addParameter(valore.getKey(), valore.getValue());
			}
		}

		HttpGet httpGet = new HttpGet(urib.build());

		return httpGet;
	}

	/**
	 * 
	 * @param url
	 * @param cl
	 * @param loggerName
	 * @param params
	 * @return
	 * @throws AdminApiClientException
	 */
	public static <T> List<T> getListFromAdminApi(String url, final Class<T> cl, String loggerName, Map<String, String> params) throws AdminApiClientException {

		CloseableHttpClient httpClient = Singleton.Client.get();
		
		Logger logger = Logger.getLogger(loggerName+".AdminApiClientDelegate");
		
		try {

			HttpGet httpGet = getHttpGet(url, params);
			
			ResponseHandler<List<T>> responseHandler = getListResponseHandler(cl);
			
			return httpClient.execute(httpGet, responseHandler);

		} 
		catch (URISyntaxException e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		}
		catch (ClientProtocolException e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		} catch (IOException e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param url
	 * @param cl
	 * @param loggerName
	 * @param params
	 * @return
	 * @throws AdminApiClientException
	 */
	public static <T> T getFromAdminApi(String url, final Class<T> cl, String loggerName, 
			Map<String, String> params) throws AdminApiClientException {

		CloseableHttpClient httpClient = Singleton.Client.get();
		
		Logger logger = Logger.getLogger(loggerName+".AdminApiClientDelegate");
		
		try {

			HttpGet httpGet = getHttpGet(url, params);
			
			ResponseHandler<T> responseHandler = getResponseHandler(cl);
			
			return httpClient.execute(httpGet, responseHandler);

		} 
		catch (URISyntaxException e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		}
		catch (ClientProtocolException e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		} catch (IOException e) {
			logger.error("Error during calls", e);
			throw new AdminApiClientException(e);
		}
	}

	/**
	 * 
	 * @author gianfranco.stolfa
	 *
	 */
	private static enum Singleton {
		// Just one of me so constructor will be called once.
		Client;
		// The pool
		private PoolingHttpClientConnectionManager cm;

		// The constructor creates it - thus late
		private Singleton() {
			cm = new PoolingHttpClientConnectionManager();
			// Increase max total connection to 200
			cm.setMaxTotal(200);
			// Increase default max connection per route to 20
			cm.setDefaultMaxPerRoute(20);

		}

		public CloseableHttpClient get() {
			CloseableHttpClient threadSafeClient = HttpClients.custom()
					.setConnectionManager(cm).build();
			return threadSafeClient;
		}

	}
}
