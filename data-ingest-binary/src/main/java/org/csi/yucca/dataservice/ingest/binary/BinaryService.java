package org.csi.yucca.dataservice.ingest.binary;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

//import org.csi.yucca.dataservice.ingest.binary.webhdfs.HdfsFSUtils;

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
	private final String PATH_INTERNAL_HDFS = "/rawdata/files/";
	private final Integer MAX_SIZE_FILE_ATTACHMENT = 154857601;
	private String datasetCode;

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
		
		//Get tenantCode from ApiCode
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
			binaryData = binaryDAO.readCurrentBinaryDataByIdBinary(idBinary, idDataSet);

			Metadata mdBinaryDataSet = null;
			try { 
				mdBinaryDataSet = metadataDAO.readCurrentMetadataByTntAndIDDS(idDataSet, datasetVersion, tenantCode);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			if (mdBinaryDataSet != null){
				Metadata mdFromMongo = null;
				
				//Verify if have a permission to download selected file (idBinary)
				for (Iterator<Dataset> itDS = myListDS.iterator(); itDS.hasNext();) {
					Dataset itemDS = itDS.next();
					mdFromMongo = metadataDAO.readCurrentMetadataByTntAndIDDS(itemDS.getIdDataset(), itemDS.getDatasetVersion(), itemDS.getTenantCode());
					
					if (mdFromMongo.getConfigData().getSubtype().equals("bulkDataset")
							&& (mdFromMongo.getConfigData().getTenantCode().equals(api.getConfigData().getTenantCode()) && 
							   (mdFromMongo.getInfo().getBinaryIdDataset().equals(idDataSet)) && 
							   (mdFromMongo.getInfo().getBinaryDatasetVersion().equals(datasetVersion)))) {
						if (binaryData == null){ 
							throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
									.entity("{\"error_name\":\"Binary not found\", \"error_code\":\"E117a\", \"output\":\"NONE\", \"message\":\"this binary does not exist\"}").build());
						} else {
							String pathForUri = binaryData.getPathHdfsBinary();

							InputStream is = null;
							if (Config.getHdfsLibrary().equals("webhdfs")){
								is = org.csi.yucca.dataservice.ingest.binary.webhdfs.HdfsFSUtils.readFile(Config.getKnoxUser(), Config.getKnoxPwd(), pathForUri, Config.getKnoxUrl(), idBinary);
							} else if (Config.getHdfsLibrary().equals("hdfs")){
								is = org.csi.yucca.dataservice.ingest.binary.hdfs.HdfsFSUtils.readFile(Config.getHdfsUsername() + tenantCode, pathForUri);
							} else {
								is = org.csi.yucca.dataservice.ingest.binary.localfs.LocalFSUtils.readFile(Config.getHdfsUsername() + tenantCode, pathForUri);
							}
							if (is != null)
								return is;
							else 
								throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
										.entity("{\"error_name\":\"Binary not found\", \"error_code\":\"E117b\", \"output\":\"NONE\", \"message\":\"this binary does not exist\"}").build());
						}
					} else {
						throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
								.entity("{\"error_name\":\"Dataset not attachment\", \"error_code\":\"E112\", \"output\":\"NONE\", \"message\":\"this dataset does not accept attachments\"}").build());
					}
				}
			} else {
				throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
						.entity("{\"error_name\":\"Dataset not found\", \"error_code\":\"E116\", \"output\":\"NONE\", \"message\":\"this dataset does not exist\"}").build());
			}
		} else {
			throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
					.entity("{\"error_name\":\"API Code Wrong\", \"error_code\":\"E115\", \"output\":\"NONE\", \"message\":\"this api does not exist\"}").build());
		}
		return null;
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/binary/input/{tenant}/")
	public Response uploadFile(@Multipart("upfile") Attachment attachment, @PathParam("tenant") String tenantCode, @Multipart("datasetCode") String datasetCode,
			@Multipart("datasetVersion") Integer datasetVersion, @Multipart("alias") String aliasFile, @Multipart("idBinary") String idBinary) throws NumberFormatException,
			UnknownHostException {

		//Get size for verify max size file upload (dirty) 
		Integer sizeFileAttachment = null;
		try {
			sizeFileAttachment = attachment.getObject(InputStream.class).available();
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}
		
		if (sizeFileAttachment > MAX_SIZE_FILE_ATTACHMENT){
			return Response.status(413)
					.entity("{\"error_name\":\"File too Big\", \"error_code\":\"E114\", \"output\":\"NONE\", \"message\":\"THE SIZE IS TOO BIG\"}").build();
		}
		
		SimpleDateFormat sdfStartMongo = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		Date dateS = new Date();
		System.out.println("start Mongo => " + sdfStartMongo.format(dateS));
		
		String pathFile = attachment.getContentDisposition().getParameter("filename");
		BinaryData binaryData = new BinaryData();
		MongoClient mongo = MongoSingleton.getMongoClient();
		String supportDb = Config.getInstance().getDbSupport();
		String supportDatasetCollection = Config.getInstance().getCollectionSupportDataset();
		MongoDBBinaryDAO binaryDAO = new MongoDBBinaryDAO(mongo, "DB_" + tenantCode, MEDIA);
		MongoDBMetadataDAO metadataDAO = new MongoDBMetadataDAO(mongo, supportDb, supportDatasetCollection);

		//Get idDataset from datasetCode, datasetVersion and tenantCode
		Metadata mdFromMongo = null;
		try {
			mdFromMongo = metadataDAO.readCurrentMetadataByTntAndDSCode(datasetCode, datasetVersion, tenantCode);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		if (mdFromMongo == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error_name\":\"Dataset unknown\", \"error_code\":\"E111\", \"output\":\"NONE\", \"message\":\"You could not find the specified dataset\"}").build();
		}
		binaryData.setIdDataset(mdFromMongo.getInfo().getBinaryIdDataset());
		binaryData.setIdBinary(idBinary);
		binaryData.setAliasNameBinary(aliasFile);
		binaryData.setTenantBinary(tenantCode);
		binaryData.setDatasetCode(datasetCode);
		binaryData.setFilenameBinary(pathFile);
		binaryData.setContentTypeBinary(attachment.getContentType().toString());

		Metadata mdBinaryDataSet = metadataDAO.getCurrentMetadaByBinaryID(mdFromMongo.getInfo().getBinaryIdDataset());
		
		SimpleDateFormat sdfEndMongo = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		Date dateE = new Date();
		System.out.println("end Mongo => " + sdfEndMongo.format(dateE));
		
		System.out.println("Subtype = " + mdFromMongo.getConfigData().getSubtype());
		if (mdFromMongo.getConfigData().getSubtype().equals("bulkDataset") && (mdBinaryDataSet != null)) {


			SimpleDateFormat sdfStartWebHDFS = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date dateSWebHDFS = new Date();
			System.out.println("start WebHDFS => " + sdfStartWebHDFS.format(dateSWebHDFS));
			
			
			String pathForUri = "/" + Config.getHdfsRootDir() + "/tnt-" + tenantCode + PATH_INTERNAL_HDFS + mdBinaryDataSet.getDatasetCode() + "/" + idBinary;
			pathForUri = "/tenant/tnt-sandbox" + PATH_INTERNAL_HDFS + mdBinaryDataSet.getDatasetCode() + "/" + idBinary;
			binaryData.setPathHdfsBinary(pathForUri);
			String uri = null;
			try {
				if (Config.getHdfsLibrary().equals("webhdfs")){
					System.out.println("webhdfs = " + Config.getHdfsLibrary());
					uri = org.csi.yucca.dataservice.ingest.binary.webhdfs.HdfsFSUtils.writeFile(Config.getKnoxUser(), Config.getKnoxPwd(), pathForUri, Config.getKnoxUrl(), Config.getKnoxGroup(), attachment.getObject(InputStream.class), idBinary);
				} else if (Config.getHdfsLibrary().equals("hdfs")){
					System.out.println("hdfs = " + Config.getHdfsLibrary());
					uri = org.csi.yucca.dataservice.ingest.binary.hdfs.HdfsFSUtils.writeFile(Config.getHdfsUsername() + tenantCode, pathForUri, attachment.getObject(InputStream.class));
				} else {
					System.out.println("LocalFSUtils = " + Config.getHdfsLibrary());
					uri = org.csi.yucca.dataservice.ingest.binary.localfs.LocalFSUtils.writeFile(Config.getHdfsUsername() + tenantCode, pathForUri, attachment.getObject(InputStream.class));
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("{\"error_name\":\"Dataset attachment wrong\", \"error_code\":\"E113\", \"output\":\"NONE\", \"message\":\"" + ex.getMessage() + "\"}").build();
			}
			

			SimpleDateFormat sdfEndWebHDFS = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date dateEWebHDFS = new Date();
			System.out.println("end WebHDFS => " + sdfEndWebHDFS.format(dateEWebHDFS));
			
			System.out.println("HDFS URI = " + uri);
			
			binaryData.setDatasetVersion(mdBinaryDataSet.getDatasetVersion());
			binaryData.setMetadataBinary("");
			binaryData.setSizeBinary(0L);
			binaryDAO.createBinary(binaryData);

			updateMongo(binaryData.getTenantBinary(), binaryData.getDatasetCode(), binaryData.getDatasetVersion(), binaryData.getIdBinary());

			return Response.ok().build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error_name\":\"Dataset not attachment\", \"error_code\":\"E112\", \"output\":\"NONE\", \"message\":\"this dataset does not accept attachments\"}").build();
		}
	}

	@PUT
	@Path("/binary/{tenantCode}/metadata/{datasetCode}/{datasetVersion}/{idBinary}")
	public void updateMongo(@PathParam("tenantCode") String tenantCode, @PathParam("datasetCode") String datasetCode, @PathParam("datasetVersion") Integer datasetVersion,
			@PathParam("idBinary") String idBinary) throws WebApplicationException, NumberFormatException, UnknownHostException {

		MongoClient mongo = MongoSingleton.getMongoClient();
		MongoDBBinaryDAO binaryDAO = null;
		BinaryData binaryData = null;
		
		binaryDAO = new MongoDBBinaryDAO(mongo, "DB_" + tenantCode, MEDIA);
		
		System.out.println("updateMongo - tenantCode = " + tenantCode + ", datasetCode = " + datasetCode + ", datasetVersion = " + datasetVersion + ", idBinary=" + idBinary);

		//Get idDataset from datasetCode 
		String supportDb = Config.getInstance().getDbSupport();
		String supportDatasetCollection = Config.getInstance().getCollectionSupportDataset();
		MongoDBMetadataDAO metadataDAO = new MongoDBMetadataDAO(mongo, supportDb, supportDatasetCollection);
		Metadata mdFromMongo = null;
		try {
			mdFromMongo = metadataDAO.readCurrentMetadataByTntAndDSCode(datasetCode, datasetVersion, tenantCode);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		if (mdFromMongo == null) {
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error_name\":\"Dataset unknown\", \"error_code\":\"E111\", \"output\":\"NONE\", \"message\":\"You could not find the specified dataset\"}").build());
		}
		Long idDataset = mdFromMongo.getInfo().getBinaryIdDataset();
		
		//Get binaryObject
		binaryData = binaryDAO.readCurrentBinaryDataByIdBinary(idBinary, idDataset);
		String pathForUri = binaryData.getPathHdfsBinary();

		//Get METADATA of selected file
		//InputStream fileToParser = HdfsFSUtils.readFile(Config.getHdfsUsername() + tenantCode, Config.getKnoxPwd() , pathForUri, Config.getKnoxUrl());
		InputStream fileToParser = null;
		if (Config.getHdfsLibrary().equals("webhdfs")){
			fileToParser = org.csi.yucca.dataservice.ingest.binary.webhdfs.HdfsFSUtils.readFile(Config.getKnoxUser(), Config.getKnoxPwd(), pathForUri, Config.getKnoxUrl(), idBinary);
		} else if (Config.getHdfsLibrary().equals("hdfs")){
			fileToParser = org.csi.yucca.dataservice.ingest.binary.hdfs.HdfsFSUtils.readFile(Config.getHdfsUsername() + tenantCode, pathForUri);
		} else {
			fileToParser = org.csi.yucca.dataservice.ingest.binary.localfs.LocalFSUtils.readFile(Config.getHdfsUsername() + tenantCode, pathForUri); 
		}
		
		System.out.println("fileToParser = " + fileToParser.toString());
		
		if (fileToParser != null){

			Map<String,String> mapHS = null;
			try {
				mapHS = extractMetadata(fileToParser);
				binaryData.setMetadataBinary(mapHS.toString());
				
				Long sizeFileLenght = 0L;
				
				if (Config.getHdfsLibrary().equals("webhdfs")){
					sizeFileLenght = org.csi.yucca.dataservice.ingest.binary.webhdfs.HdfsFSUtils.sizeFile(Config.getKnoxUser(), Config.getKnoxPwd(), pathForUri, Config.getKnoxUrl(), idBinary);
				} else {
					sizeFileLenght = Long.parseLong(mapHS.get("sizeFileLenght"));
				}
				
				System.out.println("sizeFileLenght = " + sizeFileLenght.toString());
				
				binaryData.setSizeBinary(sizeFileLenght);
				binaryDAO.updateBinaryData(binaryData);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fileToParser.close();
					throw new WebApplicationException(Response.ok().build());
				} catch (IOException ex) {
					
					ex.printStackTrace();
				}
			}
		}
		throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
				.entity("{\"error_name\":\"Dataset not attachment\", \"error_code\":\"E112\", \"output\":\"NONE\", \"message\":\"this dataset does not accept attachments\"}").build());
	}

	@DELETE
	@Path("/binary/{tenantCode}/clearMetadata/{datasetCode}")
	public void clearMetadata(@PathParam("tenantCode") String tenantCode, @PathParam("datasetCode") String datasetCode) throws WebApplicationException, NumberFormatException, UnknownHostException {

		MongoClient mongo = MongoSingleton.getMongoClient();

		//Get idDataset from datasetCode 
		String supportDb = Config.getInstance().getDbSupport();
		String supportDatasetCollection = Config.getInstance().getCollectionSupportDataset();
		MongoDBMetadataDAO metadataDAO = new MongoDBMetadataDAO(mongo, supportDb, supportDatasetCollection);
		Metadata mdFromMongo = null;
		try {
			mdFromMongo = metadataDAO.readCurrentMetadataByTntAndDSCode(datasetCode, tenantCode);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}
		if (mdFromMongo == null) {
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error_name\":\"Dataset unknown\", \"error_code\":\"E111\", \"output\":\"NONE\", \"message\":\"You could not find the specified dataset\"}").build());
		}
		
		//Get binaryObject
		String pathForUri = "/pre-tenant/tnt-"+tenantCode+"/rawdata/files/"+datasetCode+"/";
		pathForUri = "/tenant/tnt-sandbox/rawdata/files/"+datasetCode+"/";

		//Get METADATA of selected file
		Boolean resultDel = false;
		try {
			//resultDel = HdfsFSUtils.deleteDir(Config.getHdfsUsername() + tenantCode, Config.getKnoxPwd(), pathForUri, Config.getKnoxUrl());
			if (Config.getHdfsLibrary().equals("webhdfs")){
				resultDel = org.csi.yucca.dataservice.ingest.binary.webhdfs.HdfsFSUtils.deleteDir(Config.getKnoxUser(), Config.getKnoxPwd(), pathForUri, Config.getKnoxUrl());
			} else if (Config.getHdfsLibrary().equals("hdfs")){
				resultDel = org.csi.yucca.dataservice.ingest.binary.hdfs.HdfsFSUtils.deleteDir(Config.getHdfsUsername() + tenantCode, pathForUri);
			} else {
				resultDel = org.csi.yucca.dataservice.ingest.binary.localfs.LocalFSUtils.deleteDir(Config.getHdfsUsername() + tenantCode, pathForUri);
			}
			
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
	
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error_name\":\"Dataset not attachment (METADATA not found) " + ex.getMessage() + "\", \"error_code\":\"E112\", \"output\":\"NONE\", \"message\":\"this dataset does not accept attachments " + ex.getCause() + "\"}").build());
		}
		if (resultDel){
			MongoDBBinaryDAO binaryDAO = null;
			binaryDAO = new MongoDBBinaryDAO(mongo, "DB_" + tenantCode, MEDIA);
			
			Long idDataset = mdFromMongo.getIdDataset();
			
			//Get binaryObject
			List<BinaryData> binaryData = binaryDAO.readCurrentBinaryDataByIdDataset(idDataset);
			
			for (Iterator<BinaryData> binDS = binaryData.iterator(); binDS.hasNext();) {
				BinaryData binary = binDS.next();
				binaryDAO.deleteBinaryData(binary);
			}
			
			
			throw new WebApplicationException(Response.ok().build());
		} else 
			throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
					.entity("{\"error_name\":\"Dataset not found\", \"error_code\":\"E112\", \"output\":\"NONE\", \"message\":\"this dataset does not accept attachments\"}").build());
	}
	
	public static Map<String, String> extractMetadata(InputStream is) {

		//System.out.println("sizeFileLenght = " + sizeFileLenght.toString());
		
	    Map<String,String> map = new HashMap<String,String>();
	    BodyContentHandler contentHandler = new BodyContentHandler(0);
	    org.apache.tika.metadata.Metadata metadata = new org.apache.tika.metadata.Metadata();

        Parser parser = new AutoDetectParser();
        ParseContext parseContext = new ParseContext();
        parseContext.set(Parser.class, new ParserDecorator(parser));
        
        TikaInputStream tikaInputStream = null;
        Integer sizeFileLenght = 0;
	    try {
		    sizeFileLenght = is.available();
	        parser.parse(is, contentHandler, metadata, parseContext);
		    for (String name : metadata.names()) {
		    	if (!name.equals("X-Parsed-By"))
		    		map.put(name, metadata.get(name));
	        }
	    } catch (IOException e) {
	        for (String name : metadata.names()) {
	        	if (!name.equals("X-Parsed-By"))
		    		map.put(name, metadata.get(name));
	        }
	    } catch (SAXException e) {
	        for (String name : metadata.names()) {
	        	if (!name.equals("X-Parsed-By"))
		    		map.put(name, metadata.get(name));
	        }
	    } catch (TikaException e) {
	        for (String name : metadata.names()) {
	        	if (!name.equals("X-Parsed-By"))
		    		map.put(name, metadata.get(name));
	        }
	    } catch (Exception e) {
	        for (String name : metadata.names()) {
	        	if (!name.equals("X-Parsed-By"))
		    		map.put(name, metadata.get(name));
	        }
	    } finally {
		    map.put("sizeFileLenght", sizeFileLenght.toString());
	    	IOUtils.closeQuietly(tikaInputStream);
	    }

	    return map;
	}

	public String getDatasetCode() {
		return datasetCode;
	}

	public void setDatasetCode(String datasetCode) {
		this.datasetCode = datasetCode;
	}
}