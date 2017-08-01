package org.csi.yucca.dataservice.metadataapi.delegate.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.HttpUtil;
import org.csi.yucca.dataservice.metadataapi.util.Util;

public class ResourcesDelegate {

	static Logger log = Logger.getLogger(ResourcesDelegate.class);

	public static ResourcesDelegate instance;

	public ResourcesDelegate() {

	}

	public static ResourcesDelegate getInstance() {
		if (instance == null)
			instance = new ResourcesDelegate();
		return instance;
	}

	public byte[] loadStreamIcon(String tenant, String smartobjectCode, String streamCode) throws IOException {
		log.debug("[ResourcesDelegate::loadStreamIcon] START - tenant: " + tenant + " | smartobject: " + smartobjectCode + " | stream: " + streamCode);
		String apiBaseUrl = Config.getInstance().getServiceBaseUrl();

		// https://int-userportal.smartdatanet.it/userportal/api/proxy/services/streams/sandbox/internal/33/?visibleFrom=sandbox&callback=angular.callbacks._f

		String completeUrl = apiBaseUrl + "streams/" + tenant + "/" + smartobjectCode + "/" + streamCode + "/";

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("visibleFrom", tenant);

		String responseString = HttpUtil.getInstance().doGet(completeUrl, null, null, parameters);
		return Util.extractImageFromStream(responseString);

	}

	public byte[] loadDatasetIcon(String tenant, String datasetCode) throws IOException {
		log.debug("[ResourcesDelegate::loadDatasetIcon] START - tenant: " + tenant + " | dataset: " + datasetCode);
		String apiBaseUrl = Config.getInstance().getManagementBaseUrl();

		// http://localhost:8080/datamanagementapi/api/dataset/icon/smartlab/Provalimiti0_401
		String completeUrl = apiBaseUrl + "dataset/icon/" + tenant + "/" + datasetCode + "/";

		// GetMethod getMethod = new GetMethod(completeUrl);
		// HttpClient httpclient = new HttpClient();
		// int result = httpClient.execute(getMethod);

		HttpGet getMethod = new HttpGet(completeUrl);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse response = httpClient.execute(getMethod);
		HttpEntity entity = response.getEntity();

		// log.debug("[ResourcesDelegate::loadDatasetIcon] result: " + result);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		InputStream inputStream = entity.getContent();
		byte[] tmp = new byte[1024];
		int chunk;
		while ((chunk = inputStream.read(tmp)) != -1) {
			buffer.write(tmp, 0, chunk);
		}
		// return getMethod.getResponseBody();
		return buffer.toByteArray();
	}

}
