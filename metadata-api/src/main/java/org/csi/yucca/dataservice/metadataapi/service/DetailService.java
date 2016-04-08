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
import org.csi.yucca.dataservice.metadataapi.model.output.Metadata;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreDocResponse;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;

@Path("/detail")
public class DetailService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(DetailService.class);

	@GET
	@Path("/{tenant}/{datasetCode}")
	@Produces("application/json; charset=UTF-8")
	public String getDataset(@Context HttpServletRequest request, @PathParam("tenant") String tenant, @PathParam("datasetCode") String datasetCode,
			@QueryParam("version") String version, @QueryParam("lang") String lang) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");
		log.info("[SearchService::search] START - userAuth: " + userAuth);

		String apiName = datasetCode + "_odata";
		return loadMetadata(userAuth, apiName, version, lang);
	}

	@GET
	@Path("/{tenant}/{smartobjectCode}/{streamCode}/")
	@Produces("application/json; charset=UTF-8")
	public String getStream(@Context HttpServletRequest request, @PathParam("tenant") String tenant, @PathParam("smartobjectCode") String smartobjectCode,
			@PathParam("streamCode") String streamCode, @QueryParam("version") String version, @QueryParam("lang") String lang) throws NumberFormatException,
			UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");

		String apiName = tenant + "." + smartobjectCode + "_" + streamCode + "_stream";

		return loadMetadata(userAuth, apiName, version, lang);
	}

	private String loadMetadata(String userAuth, String apiName, String version, String lang) {

		String docName = apiName + "_internal_content";
		version = version == null ? "1.0" : version;

		String searchUrl = BASE_STORE_URL + "site/blocks/api/documentation/docs.jag?action=getInlineContent&provider=admin&apiName=" + apiName + "&version="
				+ version + "&docName=" + docName;

		String resultString = doPost(searchUrl, "application/json", null, null);

		StoreDocResponse storeDocResponse = StoreDocResponse.fromJson(resultString);

		String result = null;
		if (storeDocResponse == null || storeDocResponse.getError()) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorCode("NOT FOUND");
			error.setMessage("Resource not found");
			result = error.toJson();
		} else {
			Metadata metadata = null;
			if(apiName.startsWith("ds_") || apiName.endsWith("_stream"))
				metadata = Metadata.createFromStoreDocStream(storeDocResponse.getDoc(), lang);
			else 
				metadata = Metadata.createFromStoreDocDataset(storeDocResponse.getDoc(), lang);
			
			result = metadata.toJson();

		}

		return result;
	}
}
