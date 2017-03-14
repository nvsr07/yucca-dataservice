package org.csi.yucca.dataservice.metadataapi.service;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.Constants;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

@Path("/ckan")
public class CkanService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(CkanService.class);

	@GET
	@Path("/2/package_list")
	@Produces("application/json; charset=UTF-8")
	public String searchCkan(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start, @QueryParam("end") Integer end,
			@QueryParam("sort") String sort, @QueryParam("tenant") String tenant, @QueryParam("organization") String organization, @QueryParam("domain") String domain,
			@QueryParam("subdomain") String subdomain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
			@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
			@QueryParam("lang") String lang) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");
		try {
			Result searchResult = MetadataDelegate.getInstance()
					.search(
					userAuth, q, start, end, sort, tenant, organization, domain, subdomain, opendata, geolocalized, minLat, minLon, maxLat, maxLon, lang,
					null,null,true,null);
	
			if (searchResult!= null && searchResult.getMetadata()!=null)
			{
				List<String> packageIds = new LinkedList<String>();
				for (Metadata metadata : searchResult.getMetadata()) {
					packageIds.add(metadata.getCkanPackageId());
				}
				Gson gson = JSonHelper.getInstance();
				return gson.toJson(packageIds);
				
			}
			else 
				return "[]";
		} catch (UnsupportedEncodingException e) {
			return new ErrorResponse("", "Invalid param").toJson();
		}		
		
	}

	@GET
	@Path("/2/package_list/{packageId}")
	@Produces("application/json; charset=UTF-8")
	public String detailCkan(@Context HttpServletRequest request, @PathParam("packageId") String packageId, @QueryParam("lang") String lang) {
		String userAuth = (String) request.getSession().getAttribute("userAuth");
		log.info("[SearchService::search] START - userAuth: " + userAuth);
		String result = "";
		try {
			String apiName = Metadata.getApiNameFromCkanPackageId(packageId);
			String metadata = MetadataDelegate.getInstance().loadDatasetMetadata(userAuth, apiName, null, Constants.OUTPUT_FORMAT_CKAN, lang);

			result = metadata;
		} catch (Exception e) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode("Error with packageId " + packageId);
			error.setMessage(e.getMessage());
			result = error.toJson();
		}
		return result;

	}

}
