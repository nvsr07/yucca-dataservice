package org.csi.yucca.dataservice.insertdataapi.hdfs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.csi.yucca.dataservice.insertdataapi.hdfs.model.FileStatus;
import org.csi.yucca.dataservice.insertdataapi.hdfs.model.POJOHdfs;
import org.csi.yucca.dataservice.insertdataapi.util.EmailDelegate;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class SDPInsertApiHdfsDataAccess {

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	public String deleteData(String datasetType, String datasetSubtype, String datasetDomain, String datasetSubdomain, String tenantOrganization, String datasetCode,
			String streamVirtualEntitySlug, String streamCode, Long idDataset, Long datasetVersion) {
		String apiBaseUrl = "";
		String typeDirectory = "";
		String subTypeDirectory = "";

		EmailDelegate mailer = new EmailDelegate();
		String mailToAddress = SDPInsertApiConfig.getInstance().getMailToAddress();
		String mailFromAddress = SDPInsertApiConfig.getInstance().getMailFromAddress();

		ObjectMapper mapper = new ObjectMapper();

		try {

			// Verifico il tipo di Dataset per creare il path corretto su HDFS
			if (datasetSubtype.equals("bulkDataset")) {
				if (datasetSubdomain == null) {
					log.info("CodSubDomain is null => " + datasetSubdomain);
					typeDirectory = "db_" + tenantOrganization;
				} else {
					log.info("CodSubDomain => " + datasetSubdomain);
					typeDirectory = "db_" + datasetSubdomain;
				}
				subTypeDirectory = datasetCode;

				log.info("typeDirectory => " + typeDirectory);
				log.info("subTypeDirectory => " + subTypeDirectory);
			} else if (datasetSubtype.equals("streamDataset") || datasetSubtype.equals("socialDataset")) {
				typeDirectory = "so_" + streamVirtualEntitySlug;
				subTypeDirectory = streamCode;
			}

			apiBaseUrl = SDPInsertApiConfig.getInstance().getKnoxSdnetUlr() + new String(tenantOrganization).toUpperCase() + "/rawdata/" + datasetDomain + "/" + typeDirectory
					+ "/" + subTypeDirectory;
			log.info("apiBaseUrl => " + apiBaseUrl + "?op=LISTSTATUS");

			HttpClient client = HttpClientBuilder.create().build();
			HttpGet httpget = new HttpGet(apiBaseUrl + "?op=LISTSTATUS");

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(SDPInsertApiConfig.getInstance().getKnoxSdnetUsername(), SDPInsertApiConfig.getInstance()
					.getKnoxSdnetPassword()));

			// Add AuthCache to the execution context
			final HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);

			HttpResponse response = client.execute(httpget, context);
			log.info("[SDPInsertApiHdfsDataAccess::deleteData] call to " + apiBaseUrl + " - status " + response.getStatusLine().toString() + " - status Code "
					+ response.getStatusLine().getStatusCode());

			if (response.getStatusLine().getStatusCode() == 404) {
				String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject404();
				String body = SDPInsertApiConfig.getInstance().getDeleteMailBody404();
				mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl);
			} else if (response.getStatusLine().getStatusCode() == 500) {
				String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject500();
				String body = SDPInsertApiConfig.getInstance().getDeleteMailBody500();
				mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl);
				return "KO - Server Error " + response.getStatusLine().getReasonPhrase();
			} else {
				// 200 HTTP STATUS CODE
				StringBuilder out = new StringBuilder();
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";

				while ((line = rd.readLine()) != null) {
					out.append(line);
				}

				String inputJson = out.toString();
				log.info("inputJson = " + inputJson);

				String json = inputJson.replaceAll("\\{\\n*\\t*.*@nil.*:.*\\n*\\t*\\}", "null");

				POJOHdfs pojoHdfs = mapper.readValue(json, POJOHdfs.class);

				List<FileStatus> hdfsPaths = pojoHdfs.getFileStatuses().getFileStatus();
				for (FileStatus hdfsPath : hdfsPaths) {
					
					log.info("hdfsPath.getPathSuffix() = " + hdfsPath.getPathSuffix());

					HttpDelete httpDel = null;
					if (null != datasetVersion) {
						if (hdfsPath.getPathSuffix().endsWith("-" + datasetVersion + ".csv")) {
							httpDel = new HttpDelete(apiBaseUrl + "/" + hdfsPath.getPathSuffix() + "?op=DELETE");
						}
					} else {
						httpDel = new HttpDelete(apiBaseUrl + "/" + hdfsPath.getPathSuffix() + "?op=DELETE");
					}

					log.info("httpDel = " + httpDel);

					HttpResponse responseDel = client.execute(httpDel, context);

					if (responseDel.getStatusLine().getStatusCode() == 404) {
						String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject404();
						String body = SDPInsertApiConfig.getInstance().getDeleteMailBody404();
						mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl + "/" + hdfsPath.getPathSuffix());
					} else if (responseDel.getStatusLine().getStatusCode() == 500) {
						String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject500();
						String body = SDPInsertApiConfig.getInstance().getDeleteMailBody500();
						mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl + "/" + hdfsPath.getPathSuffix());
						return "KO - Server Error " + response.getStatusLine().getReasonPhrase();
					} else {
						// 200 HTTP STATUS CODE
						String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject200();
						String body = SDPInsertApiConfig.getInstance().getDeleteMailBody200();
						mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl + "/" + hdfsPath.getPathSuffix());
					}
				}
			}
		} catch (Exception e) {
			log.error("[SDPInsertApiHdfsDataAccess::deleteData] - ERROR " + e.getMessage());
			String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject500();
			String body = SDPInsertApiConfig.getInstance().getDeleteMailBody500();
			mailer.sendEmail(mailToAddress, mailFromAddress, subject,
					body + apiBaseUrl + ". E' stata riscontrata la seguente eccezione: " + e.getMessage() + "\n" + e.getStackTrace());
			e.printStackTrace();
			return "KO - Server Error " + e.getMessage();
		}
		return "OK";
	}

	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();

		String json = "{\"FileStatuses\":{\"FileStatus\":[{\"accessTime\":1486640438707,\"blockSize\":134217728,\"childrenNum\":0,\"fileId\":36847018,\"group\":\"hdfs\",\"length\":158,\"modificationTime\":1485871427236,\"owner\":\"sdp\",\"pathSuffix\":\"589099251319c5f6e45a0ffa-ProvaCancell_697-1.csv\",\"permission\":\"640\",\"replication\":3,\"storagePolicy\":0,\"type\":\"FILE\"},{\"accessTime\":1485937962368,\"blockSize\":134217728,\"childrenNum\":0,\"fileId\":36968258,\"group\":\"hdfs\",\"length\":137,\"modificationTime\":1485937962604,\"owner\":\"sdp\",\"pathSuffix\":\"589197b7abe55b2aaaa99819-ProvaCancell_697-1.csv\",\"permission\":\"640\",\"replication\":3,\"storagePolicy\":0,\"type\":\"FILE\"},{\"accessTime\":1485959260011,\"blockSize\":134217728,\"childrenNum\":0,\"fileId\":36994105,\"group\":\"hdfs\",\"length\":116,\"modificationTime\":1485959260243,\"owner\":\"sdp\",\"pathSuffix\":\"5891eb90e3d02fd6c7259323-ProvaCancell_697-2.csv\",\"permission\":\"640\",\"replication\":3,\"storagePolicy\":0,\"type\":\"FILE\"},{\"accessTime\":1486456356915,\"blockSize\":134217728,\"childrenNum\":0,\"fileId\":37735252,\"group\":\"hdfs\",\"length\":305,\"modificationTime\":1486456357144,\"owner\":\"sdp\",\"pathSuffix\":\"589980d2753a098f8686fff3-ProvaCancell_697-2.csv\",\"permission\":\"640\",\"replication\":3,\"storagePolicy\":0,\"type\":\"FILE\"}]}}";
		try {
			POJOHdfs  pojoHdfs = mapper.readValue(json, POJOHdfs.class);
			System.out.println("ecco " + pojoHdfs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}


