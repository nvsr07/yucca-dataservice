package org.csi.yucca.dataservice.metadataapi.service;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.model.output.Metadata;
import org.csi.yucca.dataservice.metadataapi.service.response.ListResponse;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

@Path("/search")
public class SearchService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(SearchService.class);

	@GET
	@Path("/full")
	@Produces("application/json; charset=UTF-8")
	public String searchFull(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start,
			@QueryParam("end") Integer end, @QueryParam("tenant") String tenant, @QueryParam("domain") String domain, @QueryParam("opendata") Boolean opendata,
			@QueryParam("lang") String lang) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");

		List<Metadata> metadataList = search(userAuth, q, start, end, tenant, domain, opendata, lang);
		ListResponse response = new ListResponse();
		response.setCount(metadataList.size());
		response.setResult(metadataList);
		return response.toJson();
	}

//	@GET
//	@Path("/ckan")
//	@Produces("application/json; charset=UTF-8")
//	public String searchCkan(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start,
//			@QueryParam("end") Integer end, @QueryParam("tenant") String tenant, @QueryParam("domain") String domain, @QueryParam("opendata") Boolean opendata,
//			@QueryParam("lang") String lang) throws NumberFormatException, UnknownHostException {
//
//		String userAuth = (String) request.getSession().getAttribute("userAuth");
//		List<Metadata> metadataList = search(userAuth, q, start, end, tenant, domain, opendata, lang);
//
//		List<String> packageIds = new LinkedList<String>();
//		for (Metadata metadata : metadataList) {
//			if (metadata.getDataset() != null && metadata.getDataset().getDatasetId() != null)
//				packageIds.add(metadata.getCkanPackageId());
//		}
//		Gson gson = JSonHelper.getInstance();
//		return gson.toJson(packageIds);
//	}




}
