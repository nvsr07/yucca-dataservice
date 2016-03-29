package org.csi.yucca.dataservice.metadataapi.service;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
	public String getDataset(@PathParam("tenant") String tenant, @PathParam("datasetCode") String datasetCode) throws NumberFormatException,
			UnknownHostException {
		String result = "dataset" + tenant + "- " + datasetCode;
		return result;
	}

	@GET
	@Path("/{tenant}/{{smartobjectCode}}/{streamCode}/")
	@Produces("application/json; charset=UTF-8")
	public String getStream(@PathParam("tenant") String tenant, @PathParam("smartobjectCode") String smartobjectCode, @PathParam("streamCode") String streamCode)
			throws NumberFormatException, UnknownHostException {
		String result = "dataset" + tenant + "- " + streamCode;
		return result;
	}
}
