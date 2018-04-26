package org.csi.yucca.dataservice.metadataapi.service;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate;
import org.csi.yucca.dataservice.metadataapi.exception.UserWebServiceException;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.metadata.Metadata;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;
import com.google.gson.internal.bind.MapTypeAdapterFactory;

@Path("/ckan")
public class CkanService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(CkanService.class);

	@GET
	@Path("/2/package_list")
	@Produces("application/json; charset=UTF-8")
	public Response searchCkan(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start, @QueryParam("end") Integer end,
			@QueryParam("sort") String sort, @QueryParam("tenant") String tenant, @QueryParam("organization") String organization, @QueryParam("domain") String domain,
			@QueryParam("subdomain") String subdomain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
			@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
			@QueryParam("lang") String lang,
			@QueryParam("tags") String tags, @QueryParam("visibility") String visibility,@QueryParam("isSearchExact") Boolean isSearchExact,
			@QueryParam("includeSandbox") Boolean includeSandbox) throws NumberFormatException, UnknownHostException {
		
		String result;
		
		try {
			Result searchResult = MetadataDelegate.getInstance()
					.search(
					request, q, start, end, sort, tenant, organization, domain, subdomain, opendata, geolocalized, minLat, minLon, maxLat, maxLon, lang,
					null,null,true,null, tags, visibility, isSearchExact, includeSandbox, null);
	
			if (searchResult!= null && searchResult.getMetadata()!=null)
			{
				List<String> packageIds = new LinkedList<String>();
				for (Metadata metadata : searchResult.getMetadata()) {
					
					String prova = metadata.isBinary() ? " ==>> isBinary" : "";
					
					packageIds.add(metadata.getCkanPackageId() + prova);
				}
				Gson gson = JSonHelper.getInstance();
				result = gson.toJson(packageIds);
			}
			else 
				result = "[]";
		} catch (UnsupportedEncodingException e) {
			result = new ErrorResponse("", "Invalid param").toJson();
		} catch (UserWebServiceException e) {
			return e.getResponse();
		}		
		
		return Response.ok(result).build();
	}

	@GET
	@Path("/2/package_list/{packageId}")
	@Produces("application/json; charset=UTF-8")
	public Response detailCkan(@Context HttpServletRequest request, @PathParam("packageId") String packageId, @QueryParam("lang") String lang) {
		log.info("[SearchService::search] START");
		String result = "";
		try {
			String apiName = Metadata.getApiNameFromCkanPackageId(packageId);
			Metadata metadata = MetadataDelegate.getInstance().loadDatasetMetadata(request, apiName, null, lang);

			result = metadata.toCkan();
		} catch (UserWebServiceException e) {
			return e.getResponse();
		} catch (Exception e) {
			log.error("[MetadataService::getStream]"+e.getMessage(),e);
			return Response.serverError().build();
		}
		return Response.ok(result).build();

	}

}
