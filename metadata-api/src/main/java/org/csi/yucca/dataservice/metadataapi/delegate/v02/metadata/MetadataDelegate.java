package org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.security.SecurityDelegate;
import org.csi.yucca.dataservice.metadataapi.exception.UserWebServiceException;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.facet.FacetCount;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.facet.FacetField;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineMetadata;
import org.csi.yucca.dataservice.metadataapi.model.searchengine.v02.SearchEngineResult;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.FacetParams;
import org.csi.yucca.dataservice.metadataapi.util.HttpUtil;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class MetadataDelegate {

	static Logger log = Logger.getLogger(MetadataDelegate.class);

	private static MetadataDelegate instance;

	protected String SEARCH_ENGINE_BASE_URL = Config.getInstance().getSearchEngineBaseUrl();

	private MetadataDelegate() {
		//super();
		
	}

	public static MetadataDelegate getInstance() {
		if (instance == null)
			instance = new MetadataDelegate();
		return instance;
	}

	public Result search(HttpServletRequest request, String q, Integer start, Integer rows, String sort, String tenant, String organization, String domain, String subdomain,
			Boolean opendata, Boolean geolocalizated, Double minLat, Double minLon, Double maxLat, Double maxLon, 
			String lang, Boolean dCatReady, FacetParams facet, Boolean hasDataset, Boolean hasStream, 
			String tags, String visibility, Boolean isSearchExact, Boolean includeSandbox, String externalReference)
			throws NumberFormatException, UnknownHostException, UnsupportedEncodingException,UserWebServiceException {

		log.info("[MetadataDelegate::search] START ");

		List<String> tenantAuthorized = SecurityDelegate.getInstance().getTenantAuthorized(request);
		
		
		Map<String, String> params = new HashMap<String, String>();
		StringBuffer searchUrl = new StringBuffer(SEARCH_ENGINE_BASE_URL + "select?wt=json");

		// http://sdnet-master4.sdp.csi.it:8983/solr/sdp_int_metasearch_shard3_replica2/select?q=search_lemma:parametroQfq=domainCode:%22AGRICOLTURE%22&fq=sudomain:"sss"

		if (q == null || q.equals(""))
			q = "*";

		params.put("q", q);

		if (BooleanUtils.isTrue(isSearchExact))
		{
			searchUrl.append("&q=search_text:" + URLEncoder.encode(q, "UTF-8"));
			params.put("isSearchExact", "true");
		}
		else {
			searchUrl.append("&q=search_lemma:" + URLEncoder.encode(q, "UTF-8"));
		}
		
		if (tenantAuthorized==null || tenantAuthorized.size()==0)
		{
			searchUrl.append("&fq=visibility:public");
		}
		else {
			StringBuffer searchTenants = new StringBuffer("(visibility:public OR tenantsCode:(");
			
			Iterator<String> iter = tenantAuthorized.iterator();
			while(iter.hasNext())
			{
				String tenantAuth = iter.next();
				searchTenants .append(tenantAuth);
				if (iter.hasNext())
					searchTenants.append(" OR ");
			}
			searchTenants.append("))");
			
			
			searchUrl.append("&fq="+URLEncoder.encode(searchTenants.toString(), "UTF-8"));
		}

		if (geolocalizated != null) {
			// FIXME
		}
		
		if (minLat!=null || minLon!=null || maxLon!=null || maxLat!=null)
		{
			double mxlon = NumberUtils.max((maxLon==null?180D:maxLon),180D);
			double mxlat = NumberUtils.max((maxLat==null?90D:maxLat),90D);
			double mnlon = NumberUtils.max((minLon==null?-180D:minLon),-180D);
			double mnlat = NumberUtils.max((minLat==null?-90D:minLat),-90D);
			
			String geoStr  = "["+mnlat+","+mnlon+" TO "+mxlat+","+mxlon+"]";
//			if (BooleanUtils.isTrue(geolocalizated))
//			{
//				
//			}
//			else {
//				geoStr = "["+mnlat+","+mnlon+" TO "+mxlat+","+mxlon+"]";
//			}
			// TO_UNDERSTAND
			
			
			searchUrl.append("&fq=geogeo:" + URLEncoder.encode(geoStr, "UTF-8"));
			params.put("minLat", ""+mnlat);
			params.put("minLon", ""+mnlon);
			params.put("maxLat", ""+mxlat);
			params.put("maxLon", ""+mxlon);
			
			
			
			
		}
		
		
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
		if (externalReference != null) {
			searchUrl.append("&fq=externalReference:" + URLEncoder.encode(externalReference, "UTF-8"));
			params.put("externalReference", externalReference);
		}

		if (dCatReady != null) {
			searchUrl.append("&fq=dcatReady:" + (dCatReady ? "true" : "false"));
			params.put("dcatReady", "" + dCatReady);
		}

		if (opendata != null) {
			searchUrl.append("&fq=isOpendata:" + (opendata ? "true" : "false"));
			params.put("opendata", ""+opendata);
		}

		if (hasDataset != null)
		{
			if (BooleanUtils.isTrue(hasDataset)) {
				searchUrl.append("&fq=entityType:dataset");
				params.put("hasDataset", "true");
			} else {
				searchUrl.append("&fq=-entityType:dataset");
				params.put("hasDataset", "false");
			}
		}

		if (hasStream != null)
		{
			if (BooleanUtils.isTrue(hasStream)) {
				searchUrl.append("&fq=entityType:stream");
				params.put("hasStream", "true");
			} else {
				searchUrl.append("&fq=-entityType:stream");
				params.put("hasStream", "false");
			}
			
			
		}

		if (tags!=null)
		{
			String[] tagsArr = StringUtils.split(tags,',');
			for (int i = 0; i < tagsArr.length; i++) {
				String tag = tagsArr[i];
				searchUrl.append("&fq=tagCode:" +URLEncoder.encode(tag,"UTF-8"));		
			} 
			params.put("tags", ""+tags);
		} 

		if (BooleanUtils.isNotTrue(includeSandbox))
		{
			searchUrl.append("&fq=-tenantCode:sandbox");
		} else {
			params.put("includeSandbox", "true");
		}
		
		if (facet != null) {
			searchUrl.append("&" + facet.toSorlParams());
			for (String facetKey : facet.getParamsMap().keySet()) {
				params.put(facetKey, facet.getParamsMap().get(facetKey));
			}
		}

		if (visibility != null) {
			searchUrl.append("&fq=visibility:" +URLEncoder.encode(visibility,"UTF-8"));
			params.put("visibility", ""+visibility);
		}
		
		if (start == null)
			start = 0;
		searchUrl.append("&start=" + start);

		if (rows == null)
			rows = 12;

		searchUrl.append("&rows=" + rows);
		if (sort != null)
			searchUrl.append("&sort=" + sort);

		log.info("[MetadataDelegate::search] searchUrl: " + searchUrl);

		String resultString = HttpUtil.getInstance().doGet(searchUrl.toString(), "application/json", null, null);

		SearchEngineResult searchEngineResult = SearchEngineResult.fromJson(resultString);

		Result result = new Result();
		int discardedCount = 0;
		if (searchEngineResult.getResponse() != null && searchEngineResult.getResponse().getDocs() != null) {
			for (SearchEngineMetadata searchEngineItem : searchEngineResult.getResponse().getDocs()) {
				try {
					result.addMetadata(Metadata.createFromSearchEngineItem(searchEngineItem, lang));
				} catch (Exception e) {
					log.error("[MetadataDelegate::search] ERROR on metadata conversion - datasetCode:" + searchEngineItem.getDatasetCode() + "- streamCode:"
							+ searchEngineItem.getStreamCode() + " - smartobjectCode:" + searchEngineItem.getSoCode() + " - ERROR:  " + e.getMessage());
					discardedCount ++;
				}
			}
			result.setCount(searchEngineResult.getResponse().getDocs().size()- discardedCount);

		}
		result.setStart(searchEngineResult.getResponse().getStart());
		Integer totalCount = searchEngineResult.getResponse().getNumFound() - discardedCount;
		result.setTotalCount(totalCount);
		Integer pageCount = new Double(Math.ceil(totalCount.doubleValue() / rows)).intValue();
		result.setTotalPages(pageCount);

		result.setParams(params);

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


	public Metadata loadDatasetMetadata(HttpServletRequest request, String datasetCode, String version, String lang) throws UserWebServiceException, UnsupportedEncodingException {
		String query = "datasetCode:" + datasetCode;
		return loadMetadata(request,query, lang);

	}

	public Metadata loadStreamMetadata(HttpServletRequest request, String tenantCode, String smartobjectCode, String streamCode, String version, String lang) throws UserWebServiceException, UnsupportedEncodingException {
		String query = "(tenantCode:"+tenantCode+" AND streamCode:"+streamCode+" AND soCode:"+smartobjectCode+")";
		return loadMetadata(request,query, lang); 

	}

	private Metadata loadMetadata(HttpServletRequest request,String query, String lang) throws UserWebServiceException, UnsupportedEncodingException {

		List<String> tenantAuthorized = SecurityDelegate.getInstance().getTenantAuthorized(request);

		StringBuffer searchUrl = new StringBuffer(SEARCH_ENGINE_BASE_URL + "select?wt=json&");

		searchUrl.append("q=*:*&fq=" + URLEncoder.encode(query,"UTF-8") + "&start=0&end=1");

		if (tenantAuthorized==null || tenantAuthorized.size()==0)
		{
			searchUrl.append("&fq="+URLEncoder.encode("visibility:public","UTF-8"));
		}
		else {
			StringBuffer searchTenants = new StringBuffer("(visibility:public OR tenantsCode:(");
			
			Iterator<String> iter = tenantAuthorized.iterator();
			while(iter.hasNext())
			{
				String tenantAuth = iter.next();
				searchTenants .append(tenantAuth);
				if (iter.hasNext())
					searchTenants.append(" OR ");
			}
			searchTenants.append("))");
			
			
			searchUrl.append("&fq="+URLEncoder.encode(searchTenants.toString(), "UTF-8"));
		}
		
		log.info("[AbstractService::dopost] searchUrl: " + searchUrl);

		String resultString = HttpUtil.getInstance().doGet(searchUrl.toString(), "application/json", null, null);

		SearchEngineResult searchEngineResult = SearchEngineResult.fromJson(resultString);

		if (searchEngineResult.getResponse().getDocs() == null || searchEngineResult.getResponse().getDocs().size() == 0) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode("NOT FOUND");
			error.setMessage("Resource not found");
			throw new UserWebServiceException(Response.ok(error.toJson()).build());
		} else {
			SearchEngineMetadata searchEngineMetadata = searchEngineResult.getResponse().getDocs().get(0);
			Metadata metadata = Metadata.createFromSearchEngineItem(searchEngineMetadata, lang);
			return metadata;
//			if (format != null && format.equals(Constants.OUTPUT_FORMAT_CKAN))
//				result = metadata.toCkan();
//			else  if (format != null && format.equals(Constants.OUTPUT_FORMAT_V01_STREAM))
//				result = metadata.toV01(Constants.OUTPUT_FORMAT_V01_STREAM);
//			else  if (format != null && format.equals(Constants.OUTPUT_FORMAT_V01_DATASET))
//				result = metadata.toV01(Constants.OUTPUT_FORMAT_V01_DATASET);
//			else  if (format != null && format.equals(Constants.OUTPUT_FORMAT_V01_LIST))
//				result = metadata.toV01(Constants.OUTPUT_FORMAT_V01_LIST);
//			else  if (format != null && format.equals(Constants.OUTPUT_FORMAT_JSON))
//				result = metadata.toV01(Constants.OUTPUT_FORMAT_JSON);
//			else
//				result = metadata.toJson();

			
		}
	}
	
	
	

}
