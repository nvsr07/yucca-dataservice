package org.csi.yucca.dataservice.metadataapi.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.model.output.Metadata;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreDocResponse;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreListResponse;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreMetadataItem;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Constants;
import org.csi.yucca.dataservice.metadataapi.util.HttpUtil;

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
	
	protected List<Metadata> search(String userAuth, String q, Integer start, Integer end, String tenant, String domain, Boolean opendata, String lang)
			throws NumberFormatException, UnknownHostException {

		log.info("[SearchService::search] START - userAuth: " + userAuth);

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("action", "searchAPIs");

		if (userAuth != null)
			parameters.put("username", userAuth);

		String query = "(" + q + ")";
		if (domain != null) {
			query += " && (domainStream=" + domain + " dataDomain=" + domain + ")";
		}

		if (tenant != null && !tenant.trim().equals("")) {
			query += " && (configData.tenantCode=" + tenant + " dataDomain=" + domain + ")";
		}

		if (opendata != null && opendata)
			query += " && (opendata.isOpendata = true)";

		parameters.put("query", query);
		if (start == null)
			start = 0;

		parameters.put("start", "" + start);
		if (end == null)
			end = 12;

		parameters.put("end", "" + end);

		String searchUrl = STORE_BASE_URL + "site/blocks/secure/search.jag?";

		String resultString = doPost(searchUrl, "application/json", null, parameters);

		StoreListResponse storeResponse = StoreListResponse.fromJson(resultString);

		List<Metadata> metadataList = new LinkedList<Metadata>();

		if (storeResponse.getResult() != null) {
			for (StoreMetadataItem storeItem : storeResponse.getResult()) {
				metadataList.add(Metadata.createFromStoreSearchItem(storeItem, lang));
			}
		}

		return metadataList;

	}
	
	protected String loadMetadata(String userAuth, String apiName, String version, String format,  String lang) {

		String docName = apiName + "_internal_content";
		version = version == null ? "1.0" : version;
		
		//https://int-userportal.smartdatanet.it/store/site/blocks/secure/detail.jag?action=getInlineContent&provider=admin&apiName=AllegatiCond_408_odata&version=1.0&docName=AllegatiCond_408_odata_internal_content&username=PRDCLD75D29A052V#

		String searchUrl = STORE_BASE_URL + "site/blocks/secure/detail.jag?action=getInlineContent&provider=admin&apiName=" + apiName + "&version="
				+ version + "&docName=" + docName;

		if (userAuth != null)
			searchUrl += "&username=" + userAuth;

		String resultString = doPost(searchUrl, "application/json", null, null);

		StoreDocResponse storeDocResponse = StoreDocResponse.fromJson(resultString);

		String result = null;
		if (storeDocResponse == null || storeDocResponse.getError()) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode("NOT FOUND");
			error.setMessage("Resource not found");
			result = error.toJson();
		} else {
			Metadata metadata = null;
			if (apiName.startsWith("ds_") || apiName.endsWith("_stream"))
				metadata = Metadata.createFromStoreDocStream(storeDocResponse.getDoc(), lang);
			else
				metadata = Metadata.createFromStoreDocDataset(storeDocResponse.getDoc(), lang);

			if (metadata.getDataset() != null && metadata.getDataset().getCode() == null) {
				// http://localhost:8080/datamanagementapi/api/stream/datasetCode/sandbox/smartobject_twitter_saverino/stressTwt_06
				if (metadata.getStream() != null && metadata.getStream().getCode() != null && metadata.getStream().getSmartobject() != null
						&& metadata.getStream().getSmartobject().getCode() != null) {
					String tenantCode = metadata.getTenantCode();
					String smartObjectCode = metadata.getStream().getSmartobject().getCode();
					String streamCode = metadata.getStream().getCode();

					String datasetCodeUrl = MANAGEMENT_BASE_URL + "stream/datasetCode/" + tenantCode + "/" + smartObjectCode + "/" + streamCode;

					String datasetCode = HttpUtil.getInstance().doGet(datasetCodeUrl, "plain/text", null, null);
					metadata.getDataset().setCode(datasetCode);
				}

			}
			if(format!=null && format.equals(Constants.OUTPUT_FORMAT_CKAN))
				result = metadata.toCkan();
			else
				result = metadata.toJson();

		}

		return result;
	}


}