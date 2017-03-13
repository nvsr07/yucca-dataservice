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

@Path("/")
public class DetailService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(DetailService.class);

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

	@GET
	@Path("metadata/v02/{datasetCode}")
	@Produces("application/json; charset=UTF-8")
	public String getDatasetMetadata(@Context HttpServletRequest request, @PathParam("datasetCode") String datasetCode, @QueryParam("version") String version,
			@QueryParam("lang") String lang, @QueryParam("callback") String callback) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");
		log.info("[SearchService::search] START - userAuth: " + userAuth);

		String metadata = org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate.getInstance().loadDatasetMetadata(userAuth, datasetCode, version,null,  lang);

		return metadata;
	}

	@GET
	@Path("metadata/v02/{organizationCode}/{smartobjectCode}/{streamCode}/")
	@Produces("application/json; charset=UTF-8")
	public String getStreamMetadata(@Context HttpServletRequest request, @PathParam("organizationCode") String organizationCode,
			@PathParam("smartobjectCode") String smartobjectCode, @PathParam("streamCode") String streamCode, @QueryParam("version") String version,
			@QueryParam("lang") String lang, @QueryParam("callback") String callback) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");
		log.info("[SearchService::search] START - userAuth: " + userAuth);

		String metadata = org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate.getInstance().loadStreamMetadata(userAuth, organizationCode,
				smartobjectCode, streamCode, version,null,  lang);

		return metadata;
	}
}
