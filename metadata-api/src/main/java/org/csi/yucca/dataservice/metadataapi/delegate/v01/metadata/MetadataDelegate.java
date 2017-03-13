package org.csi.yucca.dataservice.metadataapi.delegate.v01.metadata;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreDocResponse;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreListResponse;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreMetadataItem;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Constants;
import org.csi.yucca.dataservice.metadataapi.util.HttpUtil;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class MetadataDelegate {

	static Logger log = Logger.getLogger(MetadataDelegate.class);
	
	private static MetadataDelegate instance;
	
	protected String STORE_BASE_URL = Config.getInstance().getStoreBaseUrl();
	protected String MANAGEMENT_BASE_URL = Config.getInstance().getManagementBaseUrl();

	private MetadataDelegate() {
		super();
	}
	
	public static MetadataDelegate getInstance() {
		if (instance == null)
			instance = new MetadataDelegate();
		return instance;
	}

	public List<Metadata> search(String userAuth, String q, Integer start, Integer end, String tenant, String domain, Boolean opendata, Boolean geolocalizated, Double minLat,
			Double minLon, Double maxLat, Double maxLon, String lang, Boolean dCatReady) throws NumberFormatException, UnknownHostException {

		log.info("[MetadataDelegate::search] START - userAuth: " + userAuth);

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("action", "searchAPIs");

		if (userAuth != null)
			parameters.put("username", userAuth);

		String query = "";
		if (q != null)
			query += "(" + q + ")";
		if (domain != null) {
			if (!query.equals(""))
				query += " AND ";
			query += "(domainStream=" + domain + " dataDomain=" + domain + ")";
		}

		if (dCatReady != null && dCatReady) {
			if (!query.equals(""))
				query += " AND ";
			query += " (dcatReady) ";
			// ("\"dcatReady\" : 0") AND ("\"tenantCode\" : \"tst_csp\"" OR
			// "\"codTenant\" : \"tst_csp\"")
		}

		if (tenant != null && !tenant.trim().equals("")) {
			if (!query.equals(""))
				query += " AND ";
			query += "(%22%5C%22tenantCode%5C%22+%3A+%5C%22" + tenant + "%5C%22%22 OR %22%5C%22codiceTenant%5C%22+%3A+%5C%22" + tenant + "%5C%22%22) ";
			// query += " (tenantCode eq " + tenant + " codiceTenant eq " +
			// tenant + ") ";
		}

		if (opendata != null && opendata) {
			if (!query.equals(""))
				query += " AND ";
			query += " (%22%5C%22isOpendata%5C%22%20%3A%20true%22) ";
		}

		String extraLatitudeField = "streams.stream.virtualEntityPositions.position[0].lat";
		String extraLongitudeField = "streams.stream.virtualEntityPositions.position[0].lon";

		if (geolocalizated != null && geolocalizated) {
			if (minLat == null)
				minLat = -90.;
			if (minLon == null)
				minLon = -180.;
			if (maxLat == null)
				maxLat = 90.;
			if (maxLon == null)
				maxLon = 180.;
		}

		if (minLat != null && minLon != null && maxLat != null && maxLon != null) {
			if (!query.equals(""))
				query += " AND ";
			query += "(" + extraLatitudeField + ">=" + minLat + " AND " + extraLatitudeField + "<=" + maxLat + " AND " + extraLongitudeField + ">=" + minLon + " AND "
					+ extraLongitudeField + "<=" + maxLon + ")";
		}

		if (!query.equals(""))
			parameters.put("query", "(" + query + ")");

		if (start == null)
			start = 0;

		parameters.put("start", "" + start);
		if (end == null)
			end = 12;

		parameters.put("end", "" + end);

		String searchUrl = STORE_BASE_URL + "site/blocks/secure/search.jag?";

		log.info("[AbstractService::dopost] searchUrl: " + searchUrl + ", parameters: " + parameters);

		String resultString = HttpUtil.getInstance().doPost(searchUrl, "application/json", null, parameters);

		StoreListResponse storeResponse = StoreListResponse.fromJson(resultString);

		List<Metadata> metadataList = new LinkedList<Metadata>();

		if (storeResponse.getResult() != null) {
			for (StoreMetadataItem storeItem : storeResponse.getResult()) {
				metadataList.add(Metadata.createFromStoreSearchItem(storeItem, lang));
			}
		}

		Gson gson = JSonHelper.getInstance();
		String json = gson.toJson(metadataList);
		log.info("[AbstractService::dopost] json: " + json);

		return metadataList;

	}

	public String loadMetadata(String userAuth, String apiName, String version, String format, String lang) {

		String docName = apiName + "_internal_content";
		version = version == null ? "1.0" : version;

		// https://int-userportal.smartdatanet.it/store/site/blocks/secure/detail.jag?action=getInlineContent&provider=admin&apiName=AllegatiCond_408_odata&version=1.0&docName=AllegatiCond_408_odata_internal_content&username=PRDCLD75D29A052V#

		String searchUrl = STORE_BASE_URL + "site/blocks/secure/detail.jag?action=getInlineContent&provider=admin&apiName=" + apiName + "&version=" + version + "&docName="
				+ docName;

		if (userAuth != null)
			searchUrl += "&username=" + userAuth;

		String resultString = HttpUtil.getInstance().doPost(searchUrl, "application/json", null, null);

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
			if (format != null && format.equals(Constants.OUTPUT_FORMAT_CKAN))
				result = metadata.toCkan();
			else
				result = metadata.toJson();

		}

		return result;
	}

}
