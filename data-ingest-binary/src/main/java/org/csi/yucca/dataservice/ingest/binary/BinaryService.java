package org.csi.yucca.dataservice.ingest.binary;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

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
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.xml.XMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.csi.yucca.dataservice.ingest.binary.hdfs.HdfsFSUtils;
import org.csi.yucca.dataservice.ingest.model.api.Dataset;
import org.csi.yucca.dataservice.ingest.model.api.MyApi;
import org.csi.yucca.dataservice.ingest.model.metadata.Metadata;
import org.csi.yucca.dataservice.ingest.mongo.singleton.MongoSingleton;
import org.csi.yucca.dataservice.ingest.dao.MongoDBMetadataDAO;
import org.csi.yucca.dataservice.ingest.mongo.singleton.Config;
import org.joda.time.DateTime;
import org.xml.sax.SAXException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

@Path("/")
public class BinaryService {

	private final String MEDIA = "media";
	private final String METADATA = "metadata";
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
		org.csi.yucca.dataservice.ingest.dao.MongoDBApiDAO apiDAO = new org.csi.yucca.dataservice.ingest.dao.MongoDBApiDAO(mongo, supportDb, supportApiCollection);
		MyApi api = null;
		try {
			api = apiDAO.readApiByCode(apiCode);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		if (api != null){
			String tenantCode = api.getConfigData().getTenantCode();
			List<Dataset> myListDS = api.getDataset();

			Metadata mdBinaryDataSet = null;
			try {
				mdBinaryDataSet = metadataDAO.readCurrentMetadataByTntAndIDDS(idDataSet, datasetVersion, api.getConfigData().getTenantCode());
			} catch (Exception ex) {
				// TODO Auto-generated catch block
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
						
						String pathForUri = "/" + Config.getHdfsRootDir() + "/tnt-" + tenantCode + PATH_INTERNAL_HDFS + mdBinaryDataSet.getDatasetCode() + "/" + idBinary;

						InputStream is = HdfsFSUtils.readFile(Config.getHdfsUsername() + tenantCode, pathForUri);
						if (is != null)
							return is;
					} 
				}
			}
		}
		throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
				.entity("{error_name:'Dataset not attachment', error_code:'E112', output:'NONE', message:'this dataset does not accept attachments'}").build());
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/binary/{tenant}/")
	public Response uploadFile(@Multipart("upfile") Attachment attachment, @PathParam("tenant") String tenantCode, @Multipart("dataSetCode") String dataSetCode,
			@Multipart("dataSetVersion") Integer dataSetVersion, @Multipart("alias") String aliasFile, @Multipart("idBinary") String idBinary) throws NumberFormatException,
			UnknownHostException {

		String pathFile = attachment.getContentDisposition().getParameter("filename");

		MongoClient mongo = MongoSingleton.getMongoClient();
		String supportDb = Config.getInstance().getDbSupport();
		String supportDatasetCollection = Config.getInstance().getCollectionSupportDataset();
		MongoDBMetadataDAO metadataDAO = new MongoDBMetadataDAO(mongo, supportDb, supportDatasetCollection);

		Metadata mdFromMongo = metadataDAO.readCurrentMetadataByTntAndDSCode(dataSetCode, dataSetVersion, tenantCode);
		if (mdFromMongo == null) {
			// ToDo 500 Dataset unknown
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{error_name:'Dataset unknown', error_code:'E111', output:'NONE', message:'You could not find the specified dataset'}").build();
		}

		Metadata mdBinaryDataSet = metadataDAO.getCurrentMetadaByBinaryID(mdFromMongo.getInfo().getBinaryIdDataset());

		if (mdFromMongo.getConfigData().getSubtype().equals("bulkDataset") && (mdBinaryDataSet != null)) {

			String pathForUri = "/" + Config.getHdfsRootDir() + "/tnt-" + tenantCode + PATH_INTERNAL_HDFS + mdBinaryDataSet.getDatasetCode() + "/" + idBinary;

			String uri;
			try {
				uri = HdfsFSUtils.writeFile(Config.getHdfsUsername() + tenantCode, pathForUri, attachment.getObject(InputStream.class));
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
				// ToDo 500 Dataset attachment wrong
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("{error_name:'Dataset attachment wrong', error_code:'E113', output:'NONE', message:'" + ex.getMessage() + "'}").build();
			}

			/* TIKA WORKS */
			System.out.println("********TIKA WORKS********");
			Tika tika = new Tika();
			Parser parser = new XMLParser();
			ParseContext context = new ParseContext();
			BodyContentHandler handler = new BodyContentHandler(10000000);
			org.apache.tika.metadata.Metadata metadataStream = new org.apache.tika.metadata.Metadata();
			String resultMD = null;
			InputStream fileToParser = attachment.getObject(InputStream.class);
			try {
				System.out.println("  parser   ");
				detectContentFromFile(fileToParser);
				extractContentFromFile(fileToParser);
				String mimeType = tika.detect(fileToParser);
				System.out.println("  mimeType = " + mimeType);
				metadataStream.set(org.apache.tika.metadata.Metadata.CONTENT_TYPE, mimeType);
				System.out.println("  MD = " + metadataStream.toString());
				
				//attachment.
				parser.parse(fileToParser, handler, metadataStream, context);
				System.out.println("  result   ");
				resultMD = metadataStream.toString();
				System.out.println("resultMD: " + resultMD);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("********TIKA WORKS********");
			/* TIKA WORKS */

			String DB_tenant = "DB_" + tenantCode;
			DB db = mongo.getDB(DB_tenant);

			DBCollection colMedia = db.getCollection(MEDIA);
			BasicDBObject newObj = new BasicDBObject();

			newObj.put("tenantBinary", tenantCode);
			newObj.put("filenameBinary", pathFile);
			newObj.put("idBinary", idBinary);
			newObj.put("sizeBinary", null);
			newObj.put("contentTypeBinary", attachment.getContentType().toString());
			//attachment.get
			newObj.put("aliasNameBinary", aliasFile);
			newObj.put("pathHdfsBinary", pathForUri);
			newObj.put("insertDateBinary", new DateTime().toString());
			newObj.put("lastUpdateDateBinary", new DateTime().toString());
			newObj.put("idDataset", mdFromMongo.getInfo().getBinaryIdDataset());
			//newObj.put("dataSetCode", mdBinaryDataSet.getDatasetCode());
			newObj.put("datasetVersion", mdBinaryDataSet.getDatasetVersion());

			/*
			 * Da inserire la parte dei METADATI del file da Apache TIKA
			 */
			// newObj.put("contentTypeBinary", metadataStream.get(property))

			colMedia.insert(newObj);

			return Response.ok(uri).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{error_name:'Dataset not attachment', error_code:'E112', output:'NONE', message:'this dataset does not accept attachments'}").build();
		}
	}
	
	private static void detectContentFromFile(final InputStream file) throws IOException {
      Detector detector = new DefaultDetector();
      org.apache.tika.mime.MediaType type = detector.detect(file, new org.apache.tika.metadata.Metadata());
      System.out.println(String.format("detected media type for given file %s", type.toString()));
	}
	
	private static void extractContentFromFile(final InputStream file) throws IOException, SAXException, TikaException {
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler(10000000);
		org.apache.tika.metadata.Metadata metadata = new org.apache.tika.metadata.Metadata();
		parser.parse(file, handler, metadata, new ParseContext());
		System.out.println("handler = " + handler.toString());
		System.out.println("metadata = " + metadata.toString());
	}
}