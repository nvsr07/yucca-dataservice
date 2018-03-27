package org.csi.yucca.dataservice.metadataapi.delegate.resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.util.Config;

public class ResourcesDelegate {

	static Logger log = Logger.getLogger(ResourcesDelegate.class);

	public static ResourcesDelegate instance;

	public ResourcesDelegate() {

	}

	public static ResourcesDelegate getInstance() {
		if (instance == null)
			instance = new ResourcesDelegate();
		return instance;
	}

	public byte[] loadStreamIcon(String tenant, String smartobjectCode, String streamCode) throws IOException {
		log.debug("[ResourcesDelegate::loadStreamIcon] START - tenant: " + tenant + " | smartobject: " + smartobjectCode + " | stream: " + streamCode);
		String apiBaseUrl = Config.getInstance().getApiAdminUrl();
		String completeUrl = apiBaseUrl + "/1/backoffice/smartobjects/" + smartobjectCode + "/streams/" + streamCode + "/icon";
		return loadDatasourceIcon(completeUrl);

	}

	public byte[] loadDatasetIcon(String tenant, String datasetCode) throws IOException {
		log.debug("[ResourcesDelegate::loadDatasetIcon] START - tenant: " + tenant + " | dataset: " + datasetCode);
		String apiBaseUrl = Config.getInstance().getApiAdminUrl();
		// /1/management/organizations/{organizationCode}/streams/"+idstream+"/icon";
		String completeUrl = apiBaseUrl + "/1/backoffice/datasets/" + datasetCode + "/icon";

		return loadDatasourceIcon(completeUrl);
	}

	private byte[] loadDatasourceIcon(String completeUrl) throws IOException {
		log.info("[ResourcesDelegate::loadDatasourceIcon] START - completeUrl: " + completeUrl);

		try {
			HttpGet getMethod = new HttpGet(completeUrl);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse response = httpClient.execute(getMethod);
			HttpEntity entity = response.getEntity();

			// log.debug("[ResourcesDelegate::loadDatasetIcon] result: " +
			// result);
			log.info("[ResourcesDelegate::loadDatasourceIcon] load icon ok");

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			InputStream inputStream = entity.getContent();
			byte[] tmp = new byte[1024];
			int chunk;
			while ((chunk = inputStream.read(tmp)) != -1) {
				buffer.write(tmp, 0, chunk);
			}
			// return getMethod.getResponseBody();
			return buffer.toByteArray();
		} catch (Exception e) {
			log.info("[ResourcesDelegate::loadDatasourceIcon] load error load default ok");

			BufferedImage defaultIcon = ImageIO.read(ResourcesDelegate.class.getClassLoader().getResourceAsStream("stream-icon-default.png"));
			byte[] iconBytes = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(defaultIcon, "png", baos);
			baos.flush();
			iconBytes = baos.toByteArray();
			baos.close();

			return iconBytes;
		}
	}

}
