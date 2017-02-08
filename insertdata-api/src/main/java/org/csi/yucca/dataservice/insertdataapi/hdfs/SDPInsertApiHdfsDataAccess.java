package org.csi.yucca.dataservice.insertdataapi.hdfs;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
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
import org.csi.yucca.dataservice.insertdataapi.hdfs.model.FileStatus;
import org.csi.yucca.dataservice.insertdataapi.hdfs.model.POJOHdfs;
import org.csi.yucca.dataservice.insertdataapi.util.EmailDelegate;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class SDPInsertApiHdfsDataAccess {

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");


	public String deleteData(String datasetType, String datasetSubtype, String datasetDomain, String datasetSubdomain, String tenant, String datasetCode,
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
					System.out.println("CodSubDomain is null => " + datasetSubdomain);
					typeDirectory = "db_" + tenant;
				} else {
					System.out.println("CodSubDomain => " + datasetSubdomain);
					typeDirectory = "db_" + datasetSubdomain;
				}
				subTypeDirectory = datasetCode;

				System.out.println("typeDirectory => " + typeDirectory);
				System.out.println("subTypeDirectory => " + subTypeDirectory);
			} else if (datasetSubtype.equals("streamDataset") || datasetSubtype.equals("socialDataset")) {
				typeDirectory = "so_" + streamVirtualEntitySlug;
				subTypeDirectory = streamCode;
			}

			apiBaseUrl = SDPInsertApiConfig.getInstance().getKnoxSdnetUlr() + new String(tenant).toUpperCase() + "/rawdata/" + datasetDomain + "/" + typeDirectory + "/"
					+ subTypeDirectory;
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet httpget = new HttpGet(apiBaseUrl + "?op=LISTSTATUS");

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(SDPInsertApiConfig.getInstance().getKnoxSdnetUsername(), SDPInsertApiConfig.getInstance()
					.getKnoxSdnetPassword()));

			// Add AuthCache to the execution context
			final HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);

			HttpResponse response = client.execute(httpget, context);
			log.debug("[SDPInsertApiHdfsDataAccess::deleteData] call to " + apiBaseUrl + " - status " + response.getStatusLine().toString() + " - status Code "
					+ response.getStatusLine().getStatusCode());

			if (response.getStatusLine().getStatusCode() == 404) {
				String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject404();
				String body = SDPInsertApiConfig.getInstance().getDeleteMailBody404();
				mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl);
			} else if (response.getStatusLine().getStatusCode() == 500) {
				String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject500();
				String body = SDPInsertApiConfig.getInstance().getDeleteMailBody500();
				mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl);
				return "KO - Server Error "+ response.getStatusLine().getReasonPhrase();
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

				FileStatus[] hdfsPath = pojoHdfs.getFileStatuses().getFileStatus();
				for (int i = 0; i < hdfsPath.length; i++) {

					HttpDelete httpgetDel = null;
					if (null != datasetVersion) {
						if (hdfsPath[i].getPathSuffix().endsWith("-" + datasetVersion + ".csv")) {
							httpgetDel = new HttpDelete(apiBaseUrl + "/" + hdfsPath[i].getPathSuffix() + "?op=DELETE");
						}
					} else {
						httpgetDel = new HttpDelete(apiBaseUrl + "/" + hdfsPath[i].getPathSuffix() + "?op=DELETE");
					}

					HttpResponse responseDel = client.execute(httpgetDel, context);

					if (responseDel.getStatusLine().getStatusCode() == 404) {
						String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject404();
						String body = SDPInsertApiConfig.getInstance().getDeleteMailBody404();
						mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl + "/" + hdfsPath[i].getPathSuffix());
					} else if (responseDel.getStatusLine().getStatusCode() == 500) {
						String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject500();
						String body = SDPInsertApiConfig.getInstance().getDeleteMailBody500();
						mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl + "/" + hdfsPath[i].getPathSuffix());
						return "KO - Server Error "+ response.getStatusLine().getReasonPhrase();
					} else {
						// 200 HTTP STATUS CODE
						String subject = SDPInsertApiConfig.getInstance().getDeleteMailSubject200();
						String body = SDPInsertApiConfig.getInstance().getDeleteMailBody200();
						mailer.sendEmail(mailToAddress, mailFromAddress, subject, body + apiBaseUrl + "/" + hdfsPath[i].getPathSuffix());
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
			return "KO - Server Error "+ e.getMessage();
		}
		return "OK";
	}

}
