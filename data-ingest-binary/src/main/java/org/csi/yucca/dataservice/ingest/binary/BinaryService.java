package org.csi.yucca.dataservice.ingest.binary;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ParserDecorator;
import org.apache.tika.sax.BodyContentHandler;
import org.csi.yucca.dataservice.ingest.binary.hdfs.HdfsFSUtils;
import org.csi.yucca.dataservice.ingest.model.api.Dataset;
import org.csi.yucca.dataservice.ingest.model.api.MyApi;
import org.csi.yucca.dataservice.ingest.model.metadata.BinaryData;
import org.csi.yucca.dataservice.ingest.model.metadata.Metadata;
import org.csi.yucca.dataservice.ingest.mongo.singleton.MongoSingleton;
import org.csi.yucca.dataservice.ingest.dao.MongoDBBinaryDAO;
import org.csi.yucca.dataservice.ingest.dao.MongoDBMetadataDAO;
import org.csi.yucca.dataservice.ingest.mongo.singleton.Config;
import org.xml.sax.SAXException;

import com.mongodb.MongoClient;

@Path("/")
public class BinaryService {

	private final String MEDIA = "media";
	//private final String METADATA = "metadata";
	private final String PATH_INTERNAL_HDFS = "/rawdata/files/";

	static Logger log = Logger.getLogger(BinaryService.class);

	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/binary/{apiCode}/attachment/{idDataSet}/{datasetVersion}/{idBinary}")
	public InputStream downloadFile(@PathParam("apiCode") String apiCode, @PathParam("idDataSet") Long idDataSet, @PathParam("datasetVersion") Integer datasetVersion,
			@PathParam("idBinary") String idBinary) throws WebApplicationException, NumberFormatException, UnknownHostException {

		MongoClient mongo = MongoSingleton.getMongoClient();
		String supportDb = Config.getInstance().getDbSupport();
		String supportDatasetCollection = Config.getInstance().getCollectionSupportDataset();
		MongoDBMetadataDAO metadataDAO = new MongoDBMetadataDAO(mongo, supportDb, supportDatasetCollection);
		String supportApiCollection = Config.getInstance().getCollectionSupportApi();
		MongoDBBinaryDAO binaryDAO = null;
		BinaryData binaryData = null;
		
		org.csi.yucca.dataservice.ingest.dao.MongoDBApiDAO apiDAO = new org.csi.yucca.dataservice.ingest.dao.MongoDBApiDAO(mongo, supportDb, supportApiCollection);

		MyApi api = null;
		try {
			api = apiDAO.readApiByCode(apiCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (api != null){
			String tenantCode = api.getConfigData().getTenantCode();
			List<Dataset> myListDS = api.getDataset();
			
			binaryDAO = new MongoDBBinaryDAO(mongo, "DB_" + tenantCode, MEDIA);
			binaryData = binaryDAO.readCurrentBinaryDataByIdBinary(idBinary);

			Metadata mdBinaryDataSet = null;
			try { 
				mdBinaryDataSet = metadataDAO.readCurrentMetadataByTntAndIDDS(idDataSet, datasetVersion, tenantCode);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			if (mdBinaryDataSet != null){
				Metadata mdFromMongo = null;
				
				for (Iterator<Dataset> itDS = myListDS.iterator(); itDS.hasNext();) {
					Dataset itemDS = itDS.next();
					mdFromMongo = metadataDAO.readCurrentMetadataByTntAndIDDS(itemDS.getIdDataset(), itemDS.getDatasetVersion(), itemDS.getTenantCode());
					
					if (mdFromMongo.getConfigData().getSubtype().equals("bulkDataset")
							&& (mdFromMongo.getConfigData().getTenantCode().equals(api.getConfigData().getTenantCode()) && 
							   (mdFromMongo.getInfo().getBinaryIdDataset().equals(idDataSet)) && 
							   (mdFromMongo.getInfo().getBinaryDatasetVersion().equals(datasetVersion)))) {
						
						String pathForUri = binaryData.getPathHdfsBinary();

						InputStream is = HdfsFSUtils.readFile(Config.getHdfsUsername() + tenantCode, pathForUri);
						if (is != null)
							return is;
					} 
				}
			}
		}
		throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
				.entity("{'error_name':'Dataset not attachment', 'error_code':'E112', 'output':'NONE', 'message':'this dataset does not accept attachments'}").build());
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/binary/input/{tenant}/")
	public Response uploadFile(@Multipart("upfile") Attachment attachment, @PathParam("tenant") String tenantCode, @Multipart("datasetCode") String datasetCode,
			@Multipart("datasetVersion") Integer datasetVersion, @Multipart("alias") String aliasFile, @Multipart("idBinary") String idBinary) throws NumberFormatException,
			UnknownHostException {

		String pathFile = attachment.getContentDisposition().getParameter("filename");
		BinaryData binaryData = new BinaryData();
		MongoClient mongo = MongoSingleton.getMongoClient();
		String supportDb = Config.getInstance().getDbSupport();
		String supportDatasetCollection = Config.getInstance().getCollectionSupportDataset();
		MongoDBBinaryDAO binaryDAO = new MongoDBBinaryDAO(mongo, "DB_" + tenantCode, MEDIA);
		MongoDBMetadataDAO metadataDAO = new MongoDBMetadataDAO(mongo, supportDb, supportDatasetCollection);

		Metadata mdFromMongo = null;
		try {
			mdFromMongo = metadataDAO.readCurrentMetadataByTntAndDSCode(datasetCode, datasetVersion, tenantCode);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		if (mdFromMongo == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{'error_name':'Dataset unknown', 'error_code':'E111', 'output':'NONE', 'message':'You could not find the specified dataset'}").build();
		}
		binaryData.setIdDataset(mdFromMongo.getInfo().getBinaryIdDataset());
		binaryData.setIdBinary(idBinary);
		binaryData.setAliasNameBinary(aliasFile);
		binaryData.setTenantBinary(tenantCode);
		binaryData.setDatasetCode(datasetCode);
		binaryData.setFilenameBinary(pathFile);
		binaryData.setContentTypeBinary(attachment.getContentType().toString());

		Metadata mdBinaryDataSet = metadataDAO.getCurrentMetadaByBinaryID(mdFromMongo.getInfo().getBinaryIdDataset());

		if (mdFromMongo.getConfigData().getSubtype().equals("bulkDataset") && (mdBinaryDataSet != null)) {

			String pathForUri = "/" + Config.getHdfsRootDir() + "/tnt-" + tenantCode + PATH_INTERNAL_HDFS + mdBinaryDataSet.getDatasetCode() + "/" + idBinary;
			binaryData.setPathHdfsBinary(pathForUri);
			String uri = null;
			try {
				uri = HdfsFSUtils.writeFile(Config.getHdfsUsername() + tenantCode, pathForUri, attachment.getObject(InputStream.class));
			} catch (Exception ex) {
				ex.printStackTrace();
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("{'error_name':'Dataset attachment wrong', 'error_code':'E113', 'output':'NONE', 'message':'" + ex.getMessage() + "'}").build();
			}
			
			System.out.println("HDFS URI = " + uri);
			
			binaryData.setDatasetVersion(mdBinaryDataSet.getDatasetVersion());
			binaryData.setMetadataBinary("");
			binaryData.setSizeBinary(0L);
			
			binaryDAO.createBinary(binaryData);

			return Response.ok().build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{'error_name':'Dataset not attachment', 'error_code':'E112', 'output':'NONE', 'message':'this dataset does not accept attachments'}").build();
		}
	}

	@PUT
	@Path("/binary/{tenantCode}/metadata/{idDataSet}/{datasetVersion}/{idBinary}")
	public void updateMongo(@PathParam("tenantCode") String tenantCode, @PathParam("idDataSet") Long idDataSet, @PathParam("datasetVersion") Integer datasetVersion,
			@PathParam("idBinary") String idBinary) throws WebApplicationException, NumberFormatException, UnknownHostException {

		MongoClient mongo = MongoSingleton.getMongoClient();
		MongoDBBinaryDAO binaryDAO = null;
		BinaryData binaryData = null;
		
		binaryDAO = new MongoDBBinaryDAO(mongo, "DB_" + tenantCode, MEDIA);
		binaryData = binaryDAO.readCurrentBinaryDataByIdBinary(idBinary);
		
		System.out.println("binaryData = " + binaryData.toString());
		
		String pathForUri = binaryData.getPathHdfsBinary();
		System.out.println("pathForUri = " + pathForUri);

		InputStream fileToParser = HdfsFSUtils.readFile(Config.getHdfsUsername() + tenantCode, pathForUri);
		if (fileToParser != null){

			Map<String,String> mapHS = null;
			try {
				System.out.println("START EXTRACT METADATA");
				mapHS = extractMetadata(fileToParser);
				System.out.println("mapHS = " + mapHS.toString());
				binaryData.setMetadataBinary(mapHS.toString());
				System.out.println(" METADATA settati!!!");
				Long sizeFileLenght = Long.parseLong(mapHS.get("sizeFileLenght"));
				System.out.println("sizeFileLenght = " + sizeFileLenght);
				
				binaryData.setSizeBinary(sizeFileLenght);
				System.out.println(" SIZE settati!!!");
				binaryDAO.updateBinaryData(binaryData);
				System.out.println("--------> mapHS = " + mapHS.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fileToParser.close();
					throw new WebApplicationException(Response.ok("{'update':'success'}").build());
				} catch (IOException ex) {
					
					ex.printStackTrace();
				}
			}
		} else {
			//System.out.println("File " + pathForUri + "not found in HDFS!");
		}
		throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
				.entity("{'error_name':'Dataset not attachment', 'error_code':'E112', 'output':'NONE', 'message':'this dataset does not accept attachments'}").build());
	}
	
	public static Map<String, String> extractMetadata(InputStream is) {

	    Map<String,String> map = new HashMap<String,String>();
	    BodyContentHandler contentHandler = new BodyContentHandler(10000);
	    org.apache.tika.metadata.Metadata metadata = new org.apache.tika.metadata.Metadata();

        Parser parser = new AutoDetectParser();
        ParseContext parseContext = new ParseContext();
        parseContext.set(Parser.class, new ParserDecorator(parser));
        
        TikaInputStream tikaInputStream = null;
        //File tikaFile = null;
        Integer sizeFileLenght = 0;
	    try {
	    	//System.out.println("Size.......");
		    sizeFileLenght = is.available();
		    //System.out.println("Parse....");
	        parser.parse(is, contentHandler, metadata, parseContext);
	        //System.out.println("FIll into Map....");
		    for (String name : metadata.names()) {
	            map.put(name, metadata.get(name));
	        }
	    } catch (IOException e) {
	    	System.out.println("(IOException), Error while retriving Metadata, " + e.getMessage());
	        for (String name : metadata.names()) {
	            map.put(name, metadata.get(name));
	        }
	    } catch (SAXException e) {
	    	System.out.println("(SAXException), Error while retriving Metadata, " + e.getMessage());
	        for (String name : metadata.names()) {
	            map.put(name, metadata.get(name));
	        }
	    } catch (TikaException e) {
	    	System.out.println("(TikaException), Error while retriving Metadata, " + e.getMessage());
	        for (String name : metadata.names()) {
	            map.put(name, metadata.get(name));
	        }
	    } catch (Exception e) {
	    	System.out.println("(Exception), Error while retriving Metadata, " + e.getMessage());
	        for (String name : metadata.names()) {
	            map.put(name, metadata.get(name));
	        }
	    } finally {
		    map.put("sizeFileLenght", sizeFileLenght.toString());
	    	IOUtils.closeQuietly(tikaInputStream);
	    }
	    //System.out.println("Return...");

	    return map;
	}
}