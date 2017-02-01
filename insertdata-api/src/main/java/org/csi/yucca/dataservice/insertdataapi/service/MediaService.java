package org.csi.yucca.dataservice.insertdataapi.service;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsert;
import org.csi.yucca.dataservice.insertdataapi.model.output.DatasetBulkInsertOutput;

@Path("/media")
public class MediaService extends AbstractService {

	@Context
	ServletContext context;

	//http://int-sdnet-intapi.sdp.csi.it:90/insertdataapi/media/input/${tenant.code}
	
	@POST
	@Path("/input/{codTenant}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response dataInsert(@Context HttpServletRequest request, String jsonData,
			@PathParam(value="codTenant") String codTenant, @HeaderParam(value="UNIQUE_ID")String uniqueid,
			 @HeaderParam(value="X-Forwarded-For")String forwardfor, @HeaderParam(value="Authorization")String authInfo,
			 @Context final HttpServletResponse response) throws InsertApiBaseException, InsertApiRuntimeException {
		
		DatasetBulkInsertOutput out = super.dataInsert(jsonData,codTenant,uniqueid,forwardfor,authInfo);
		return Response.status(Status.ACCEPTED).entity(out).build();

	}

	@Override
	protected HashMap<String, DatasetBulkInsert> parseJsonInput(
			String codTenant, String jsonData) throws Exception {
		return new InsertApiLogic().parseJsonInputMedia(codTenant, jsonData);
	}
	
	
	
}
