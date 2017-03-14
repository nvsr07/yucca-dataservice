package org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.facet.FacetCount;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.facet.FacetField;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineMetadata;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineResult;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Constants;
import org.csi.yucca.dataservice.metadataapi.util.FacetParams;
import org.csi.yucca.dataservice.metadataapi.util.HttpUtil;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class MetadataDelegate {

	static Logger log = Logger.getLogger(MetadataDelegate.class);

	private static MetadataDelegate instance;

	protected String SEARCH_ENGINE_BASE_URL = Config.getInstance().getSearchEngineBaseUrl();

	private MetadataDelegate() {
		super();
	}

	public static MetadataDelegate getInstance() {
		if (instance == null)
			instance = new MetadataDelegate();
		return instance;
	}

	public Result search(String userAuth, String q, Integer start, Integer rows, String sort, String tenant, String organization, String domain, String subdomain,
			Boolean opendata, Boolean geolocalizated, Double minLat, Double minLon, Double maxLat, Double maxLon, String lang, Boolean dCatReady, FacetParams facet)
			throws NumberFormatException, UnknownHostException, UnsupportedEncodingException {

		log.info("[MetadataDelegate::search] START - userAuth: " + userAuth);

		Map<String, String> params = new HashMap<String, String>();
		StringBuffer searchUrl = new StringBuffer(SEARCH_ENGINE_BASE_URL + "select?wt=json");

		// http://sdnet-master4.sdp.csi.it:8983/solr/sdp_int_metasearch_shard3_replica2/select?q=search_lemma:parametroQfq=domainCode:%22AGRICOLTURE%22&fq=sudomain:"sss"

		if (q == null || q.equals(""))
			q = "*";

		params.put("q", q);

		searchUrl.append("&q=search_lemma:" + URLEncoder.encode(q, "UTF-8"));

		if (domain != null) {
			searchUrl.append("&fq=domainCode:" + URLEncoder.encode(domain, "UTF-8"));
			params.put("domain", domain);
		}
		if (subdomain != null) {
			searchUrl.append("&fq=subdomainCode:" + URLEncoder.encode(subdomain, "UTF-8"));
			params.put("subdomain", subdomain);
		}
		if (tenant != null) {
			searchUrl.append("&fq=tenantCode:" + URLEncoder.encode(tenant, "UTF-8"));
			params.put("tenant", tenant);
		}
		if (organization != null) {
			searchUrl.append("&fq=organizationCode:" + URLEncoder.encode(organization, "UTF-8"));
			params.put("organization", organization);
		}

		if (dCatReady != null) {
			searchUrl.append("&fq=dCatReady:"+(dCatReady?"1":"0"));
			params.put("dCatReady", "" + dCatReady);
		}
		

		if (opendata != null) {
			// FIXME opendata
			params.put("opendata", ""+opendata);
		}

		if (facet != null) {
			searchUrl.append("&" + facet.toSorlParams());
			for (String facetKey : facet.getParamsMap().keySet()) {
				params.put(facetKey, facet.getParamsMap().get(facetKey));
			}
		}

		if (start == null)
			start = 0;
		searchUrl.append("&start=" + start);

		if (rows == null)
			rows = 12;

		searchUrl.append("&rows=" + rows);
		if (sort != null)
			searchUrl.append("&sort=" + sort);

		log.info("[AbstractService::dopost] searchUrl: " + searchUrl);

		String resultString = HttpUtil.getInstance().doGet(searchUrl.toString(), "application/json", null, null);

		SearchEngineResult searchEngineResult = SearchEngineResult.fromJson(resultString);

		Result result = new Result();
		result.setStart(searchEngineResult.getResponse().getStart());
		result.setTotalCount(searchEngineResult.getResponse().getNumFound());
		Integer pageCount = new Double(Math.ceil(searchEngineResult.getResponse().getNumFound().doubleValue() / rows)).intValue();
		result.setTotalPages(pageCount);

		result.setParams(params);

		if (searchEngineResult.getResponse() != null && searchEngineResult.getResponse().getDocs() != null) {
			result.setCount(searchEngineResult.getResponse().getDocs().size());
			for (SearchEngineMetadata searchEngineItem : searchEngineResult.getResponse().getDocs()) {
				result.addMetadata(Metadata.createFromSearchEngineItem(searchEngineItem, lang));
			}
		}

		if (searchEngineResult.getFacet_counts() != null) {
			FacetCount facetCount = new FacetCount();
			for (String facetKey : searchEngineResult.getFacet_counts().getFacet_fields().keySet()) {

				List<Object> facetFieldValues = searchEngineResult.getFacet_counts().getFacet_fields().get(facetKey);
				FacetField facetField = new FacetField(facetFieldValues);
				facetCount.addFacetField(facetKey, facetField);
			}
			result.setFacetCount(facetCount);
		}

		Gson gson = JSonHelper.getInstance();
		String json = gson.toJson(result);
		log.info("[AbstractService::dopost] json: " + json);

		return result;

	}

	public Result searchOld(String userAuth, String q, Integer start, Integer rows, String sort, String tenant, String domain, Boolean opendata, Boolean geolocalizated,
			Double minLat, Double minLon, Double maxLat, Double maxLon, String lang, Boolean dCatReady, FacetParams facet) throws NumberFormatException, UnknownHostException {

		log.info("[MetadataDelegate::search] START - userAuth: " + userAuth);

		// http://sdnet-slave8.sdp.csi.it:8983/solr/sdp_metasearch2_shard1_replica1/select?q=*%3A*&wt=json&indent=true&facet=true&facet.field=tenant_risorse&facet.field=Organizzazione&facet.prefix=C
		StringBuffer searchUrl = new StringBuffer(SEARCH_ENGINE_BASE_URL + "select?wt=json&");

		// if (userAuth != null)
		// parameters.put("username", userAuth);

		String query = "";
		if (q != null)
			query += "(" + q + ")";
		if (domain != null) {
			if (!query.equals(""))
				query += " AND ";
			query += "(domainCode=" + domain + ")";
		}

		if (dCatReady != null && dCatReady) {
			if (!query.equals(""))
				query += " AND ";
			query += " (dcatReady) ";
		}

		if (tenant != null && !tenant.trim().equals("")) {
			if (!query.equals(""))
				query += " AND ";
			query += "(%22%5C%22tenantCode%5C%22+%3A+%5C%22" + tenant + "%5C%22%22) ";
		}

		if (opendata != null && opendata) {
			if (!query.equals(""))
				query += " AND ";
			query += " (%22%5C%22isOpendata%5C%22%20%3A%20true%22) ";
		}

		if (query.equals(""))
			query = "*:*";
		searchUrl.append("q=" + query + "&");
		if (facet != null) {
			searchUrl.append(facet.toSorlParams() + "&");
		}

		if (start == null)
			start = 0;
		searchUrl.append("start=" + start + "&");

		if (rows == null)
			rows = 12;

		searchUrl.append("end=" + rows + "&");
		if (sort != null)
			searchUrl.append("sort=" + sort + "&");

		log.info("[AbstractService::dopost] searchUrl: " + searchUrl);

		String resultString = HttpUtil.getInstance().doGet(searchUrl.toString(), "application/json", null, null);

		SearchEngineResult searchEngineResult = SearchEngineResult.fromJson(resultString);

		Result result = new Result();
		result.setStart(searchEngineResult.getResponse().getStart());
		result.setTotalCount(searchEngineResult.getResponse().getNumFound());
		Integer pageCount = new Double(Math.ceil(searchEngineResult.getResponse().getNumFound().doubleValue() / rows)).intValue();
		result.setTotalPages(pageCount);
		if (searchEngineResult.getResponse() != null && searchEngineResult.getResponse().getDocs() != null) {
			result.setCount(searchEngineResult.getResponse().getDocs().size());
			for (SearchEngineMetadata searchEngineItem : searchEngineResult.getResponse().getDocs()) {
				result.addMetadata(Metadata.createFromSearchEngineItem(searchEngineItem, lang));
			}
		}

		if (searchEngineResult.getFacet_counts() != null) {
			FacetCount facetCount = new FacetCount();
			for (String facetKey : searchEngineResult.getFacet_counts().getFacet_fields().keySet()) {

				List<Object> facetFieldValues = searchEngineResult.getFacet_counts().getFacet_fields().get(facetKey);
				FacetField facetField = new FacetField(facetFieldValues);
				facetCount.addFacetField(facetKey, facetField);
			}
			result.setFacetCount(facetCount);
		}

		Gson gson = JSonHelper.getInstance();
		String json = gson.toJson(result);
		log.info("[AbstractService::dopost] json: " + json);

		return result;

	}

	public String loadDatasetMetadata(String userAuth, String datasetCode, String version, String format, String lang) {
		String query = "datasetCode:" + datasetCode;
		return loadMetadata(query, format, lang);

	}

	public String loadStreamMetadata(String userAuth, String tenantCode, String smartobjectCode, String streamCode, String version, String format, String lang) {
		String query = "streamCode:" + streamCode;
		return loadMetadata(query, format, lang);  // FIXME stream code in detail

	}

	private String loadMetadata(String query, String format, String lang) {

		String result = null;
		StringBuffer searchUrl = new StringBuffer(SEARCH_ENGINE_BASE_URL + "select?wt=json&");

		searchUrl.append("q=" + query + "&start=0&end=1");

		log.info("[AbstractService::dopost] searchUrl: " + searchUrl);

		String resultString = HttpUtil.getInstance().doGet(searchUrl.toString(), "application/json", null, null);

		SearchEngineResult searchEngineResult = SearchEngineResult.fromJson(resultString);

		if (searchEngineResult.getResponse().getDocs() == null || searchEngineResult.getResponse().getDocs().size() == 0) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode("NOT FOUND");
			error.setMessage("Resource not found");
			result = error.toJson();
		} else {
			SearchEngineMetadata searchEngineMetadata = searchEngineResult.getResponse().getDocs().get(0);
			Metadata metadata = Metadata.createFromSearchEngineItem(searchEngineMetadata, lang);

			if (format != null && format.equals(Constants.OUTPUT_FORMAT_CKAN))
				result = metadata.toCkan();
			else
				result = metadata.toJson();

		}
		return result;
	}

}
