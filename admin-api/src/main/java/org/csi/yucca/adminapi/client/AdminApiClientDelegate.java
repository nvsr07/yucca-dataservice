package org.csi.yucca.adminapi.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * TODO  add cache
 */
public class AdminApiClientDelegate {

	public static <T> T getFromAdminApi(String url, final Class<T> cl, String loggerName, 
			Map<String, String> params) throws AdminApiClientException {

		CloseableHttpClient httpClient = Singleton.Client.get();
		Logger logger = Logger.getLogger(loggerName+".AdminApiClientDelegate");
		try {
			URIBuilder urib = new URIBuilder(url);
			if (params != null && !params.isEmpty()) {
				Iterator<Entry<String, String>> iter = params.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Entry<String, String> valore = iter.next();
					urib.addParameter(valore.getKey(), valore.getValue());
				}
			}

			HttpGet httpGet = new HttpGet(urib.build());

			ResponseHandler<T> responseHandler = new ResponseHandler<T>() {

				ObjectMapper mapper = new ObjectMapper();

				@Override
				public T handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {

					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? mapper.readValue(
								EntityUtils.toString(entity), cl) : null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}
			};
			return httpClient.execute(httpGet, responseHandler);
		} catch (URISyntaxException e) {
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
