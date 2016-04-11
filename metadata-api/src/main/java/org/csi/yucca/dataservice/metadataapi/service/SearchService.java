package org.csi.yucca.dataservice.metadataapi.service;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.model.output.Metadata;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreListResponse;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreMetadataItem;
import org.csi.yucca.dataservice.metadataapi.service.response.ListResponse;


@Path("/search")
public class SearchService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(SearchService.class);

	@GET
	@Path("/full")
	@Produces("application/json; charset=UTF-8")
	public String search(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start, @QueryParam("end") Integer end,
			@QueryParam("domain") Integer domain, @QueryParam("lang") String lang) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");
		log.info("[SearchService::search] START - userAuth: " + userAuth);


		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("action", "searchAPIs");

		String query = "(" + q + ")";
		if (domain != null) {
			query += " && (domainStream=" + domain + " dataDomain=" + domain + ")";
		}
		parameters.put("query", query);
		if (start == null)
			start = 0;

		parameters.put("start", "" + start);
		if (end == null)
			end = 12;

		parameters.put("end", "" + end);

		String searchUrl = STORE_BASE_URL + "site/blocks/search/api-search/ajax/search.jag?";
		// String params = "?start=0&query=rumore&action=searchAPIs&end=12";
		// searchUrl += params;

		String resultString = doPost(searchUrl, "application/json", null, parameters);

		StoreListResponse storeResponse = StoreListResponse.fromJson(resultString);

		List<Metadata> metadataList = new LinkedList<Metadata>();
		if (storeResponse.getResult() != null) {
			for (StoreMetadataItem storeItem : storeResponse.getResult()) {
				metadataList.add(Metadata.createFromStoreSearchItem(storeItem, lang));
			}
		}

		ListResponse response = new ListResponse();
		response.setCount(metadataList.size());
		response.setResult(metadataList);
		return response.toJson();
	}

}
