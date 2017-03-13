package org.csi.yucca.dataservice.metadataapi.service;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.v01.resources.ResourcesDelegate;

@Path("/resource")
public class ResourceService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(ResourceService.class);

	@GET
	@Path("/icon/{tenant}/{datasetCode}")
	@Produces("image/png")
	public Response getDataset(@PathParam("tenant") String tenant, @PathParam("datasetCode") String datasetCode) throws NumberFormatException,
			IOException {

		byte[] iconBytes = ResourcesDelegate.getInstance().loadDatasetIcon(tenant, datasetCode);
		
		return Response.ok().entity(iconBytes).type("image/png").build();
	}

	@GET
	@Path("/icon/{tenant}/{smartobjectCode}/{streamCode}/")
	@Produces("image/png")
	public Response getStream(@PathParam("tenant") String tenant, @PathParam("smartobjectCode") String smartobjectCode,
			@PathParam("streamCode") String streamCode) throws NumberFormatException, IOException {

		byte[] iconBytes = ResourcesDelegate.getInstance().loadStreamIcon(tenant, smartobjectCode, streamCode);
		return Response.ok().entity(iconBytes).type("image/png").build();
	}

}
