package org.csi.yucca.dataservice.ingest.binary;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.csi.yucca.dataservice.ingest.binary.hdfs.HdfsFSUtils;
import org.csi.yucca.dataservice.ingest.binary.localfs.LocalFSUtils;




@Path("/")
public class BinaryService {

	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/binary/{tenant}/{pathFile}")
	public InputStream downloadFile(@PathParam("tenant") String tenantCode,@PathParam("pathFile") String pathFile) throws WebApplicationException
	{	
		System.out.println("The filePath for download: "+pathFile);
		InputStream is = HdfsFSUtils.readFile(tenantCode,"/svil-tenant/tnt-csi/temp/"+pathFile);
		if (is!=null)
			return is;
		else
			throw new WebApplicationException(Response.serverError().build());
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/binary/{tenant}/{pathFile}")
	public Response uploadFile(@Multipart("upfile") Attachment attachment,@PathParam("tenant") String tenantCode,@PathParam("pathFile") String pathFile)
	{	
		System.out.println("The filePath for upload: "+pathFile);
		String uri  = HdfsFSUtils.writeFile(tenantCode,"/svil-tenant/tnt-csi/temp/"+pathFile,attachment.getObject(InputStream.class));
		System.out.println("uri: "+uri);
		if (uri!=null)
			return Response.ok(uri).build();
		else
			return Response.serverError().build();
	}
	

	
//	public static void main(String[] args) throws IOException {
//
//		File output = new File("pippo.zip");
//		output.createNewFile();
//		InputStream inputStream = null;
//		OutputStream outputStream = null;
//		try {
////			inputStream = BinaryService.getFileFromHdfs("svil-csi", "");
//			outputStream = new BufferedOutputStream(new FileOutputStream(output));
//			IOUtils.copyBytes(inputStream, outputStream, new Configuration());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			IOUtils.closeStream(inputStream);
//			IOUtils.closeStream(outputStream);
//		}
//	}
}