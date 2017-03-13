package org.csi.yucca.dataservice.metadataapi.delegate.v01.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
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

		GetMethod getMethod = new GetMethod(completeUrl);
		HttpClient httpclient = new HttpClient();
		int result = httpclient.executeMethod(getMethod);
		log.debug("[ResourcesDelegate::loadDatasetIcon] result: " + result);

		return getMethod.getResponseBody();

	}

}
