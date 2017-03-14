package org.csi.yucca.dataservice.metadataapi.service.v01;

import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata;
import org.csi.yucca.dataservice.metadataapi.service.AbstractService;
import org.csi.yucca.dataservice.metadataapi.service.response.ListResponse;


@Path("/")
public class MetadataService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(MetadataService.class);

	@GET
	@Path("search/full")
	// @Consumes({ "*/*", "application/vnd.sdp-metadata.v1+json" })
	@Produces({ "application/json; charset=UTF-8", "application/vnd.sdp-metadata.v1+json" })
	public String searchFull(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start, @QueryParam("end") Integer end,
			@QueryParam("tenant") String tenant, @QueryParam("domain") String domain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
			@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
			@QueryParam("lang") String lang) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");

		List<Metadata> metadataList = org.csi.yucca.dataservice.metadataapi.delegate.v01.metadata.MetadataDelegate.getInstance().search(userAuth, q, start, end, tenant, domain,
				opendata, geolocalized, minLat, minLon, maxLat, maxLon, lang, null);
		ListResponse response = new ListResponse();
		response.setCount(metadataList.size());
		response.setResult(metadataList);
		return response.toJson();
	}
	
	@GET
	@Path("detail/{tenant}/{datasetCode}")
	@Produces("application/json; charset=UTF-8")
	public String getDataset(@Context HttpServletRequest request, @PathParam("tenant") String tenant, @PathParam("datasetCode") String datasetCode,
			@QueryParam("version") String version, @QueryParam("lang") String lang, @QueryParam("callback") String callback) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");
		log.info("[SearchService::search] START - userAuth: " + userAuth);

		String apiName = datasetCode + "_odata";
		String metadata = org.csi.yucca.dataservice.metadataapi.delegate.v01.metadata.MetadataDelegate.getInstance().loadMetadata(userAuth, apiName, version, null, lang);

		return metadata;
	}

	@GET
	@Path("detail/{tenant}/{smartobjectCode}/{streamCode}/")
	@Produces("application/json; charset=UTF-8")
	public String getStream(@Context HttpServletRequest request, @PathParam("tenant") String tenant, @PathParam("smartobjectCode") String smartobjectCode,
			@PathParam("streamCode") String streamCode, @QueryParam("version") String version, @QueryParam("lang") String lang, @QueryParam("callback") String callback)
			throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");

		String apiName = tenant + "." + smartobjectCode + "_" + streamCode + "_stream";

		String metadata = org.csi.yucca.dataservice.metadataapi.delegate.v01.metadata.MetadataDelegate.getInstance().loadMetadata(userAuth, apiName, version, null, lang);

		return metadata;
	}


}
