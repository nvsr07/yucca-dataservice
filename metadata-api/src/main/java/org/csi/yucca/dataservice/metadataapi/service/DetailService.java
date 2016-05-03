package org.csi.yucca.dataservice.metadataapi.service;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

@Path("/detail")
public class DetailService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(DetailService.class);

	@GET
	@Path("/{tenant}/{datasetCode}")
	@Produces("application/json; charset=UTF-8")
	public String getDataset(@Context HttpServletRequest request, @PathParam("tenant") String tenant, @PathParam("datasetCode") String datasetCode,
			@QueryParam("version") String version, @QueryParam("lang") String lang, @QueryParam("callback") String callback) throws NumberFormatException,
			UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");
		log.info("[SearchService::search] START - userAuth: " + userAuth);

		String apiName = datasetCode + "_odata";
		String metadata = loadMetadata(userAuth, apiName, version, null, lang);

		return metadata;
	}

	@GET
	@Path("/{tenant}/{smartobjectCode}/{streamCode}/")
	@Produces("application/json; charset=UTF-8")
	public String getStream(@Context HttpServletRequest request, @PathParam("tenant") String tenant, @PathParam("smartobjectCode") String smartobjectCode,
			@PathParam("streamCode") String streamCode, @QueryParam("version") String version, @QueryParam("lang") String lang,
			@QueryParam("callback") String callback) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");

		String apiName = tenant + "." + smartobjectCode + "_" + streamCode + "_stream";

		String metadata = loadMetadata(userAuth, apiName, version, null, lang);

		return metadata;
	}

}
