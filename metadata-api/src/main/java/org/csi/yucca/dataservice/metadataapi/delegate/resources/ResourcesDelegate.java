package org.csi.yucca.dataservice.metadataapi.delegate.resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.HttpUtil;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

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
		String apiBaseUrl = Config.getInstance().getServiceBaseUrl();

		// https://int-userportal.smartdatanet.it/userportal/api/proxy/services/streams/sandbox/internal/33/?visibleFrom=sandbox&callback=angular.callbacks._f

		String completeUrl = apiBaseUrl + "streams/" + tenant + "/" + smartobjectCode + "/" + streamCode + "/";

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("visibleFrom", tenant);

		String responseString = HttpUtil.getInstance().doGet(completeUrl, null, null, parameters);
		return extractImageFromStream(responseString);

	}

	private byte[] extractImageFromStream(String streamJson) throws IOException {
		Gson gson = JSonHelper.getInstance();
		StreamsForIcon streamsForIcon = gson.fromJson(streamJson, StreamsForIcon.class);
		// InputStream streamIconIS = null;
		BufferedImage imag = null;
		if (streamsForIcon != null && streamsForIcon.getStreams() != null && streamsForIcon.getStreams().getStream() != null
				&& streamsForIcon.getStreams().getStream().getStreamIcon() != null) {
			String streamIcon = streamsForIcon.getStreams().getStream().getStreamIcon();

			imag = readStreamImageBuffer(streamIcon);
		} else
			imag = ImageIO.read(ResourcesDelegate.class.getClassLoader().getResourceAsStream("stream-icon-default.png"));

		byte[] iconBytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(imag, "png", baos);
		baos.flush();
		iconBytes = baos.toByteArray();
		baos.close();

		return iconBytes;
	}

	private BufferedImage readStreamImageBuffer(String imageBase64) throws IOException {
		BufferedImage imag = null;

		if (imageBase64 != null) {
			String[] imageBase64Array = imageBase64.split(",");

			String imageBase64Clean;
			if (imageBase64Array.length > 1) {
				imageBase64Clean = imageBase64Array[1];
			} else {
				imageBase64Clean = imageBase64Array[0];
			}

			byte[] bytearray = Base64.decodeBase64(imageBase64Clean.getBytes());
			imag = ImageIO.read(new ByteArrayInputStream(bytearray));
		}
		if (imageBase64 == null || imag == null) {
			imag = ImageIO.read(ResourcesDelegate.class.getClassLoader().getResourceAsStream("entity-icon-default.png"));

		}
		return imag;
	}

	public byte[] loadDatasetIcon(String tenant, String datasetCode) throws IOException {
		log.debug("[ResourcesDelegate::loadDatasetIcon] START - tenant: " + tenant + " | dataset: " + datasetCode);
		String apiBaseUrl = Config.getInstance().getManagementBaseUrl();

		// http://localhost:8080/datamanagementapi/api/dataset/icon/smartlab/Provalimiti0_401
		String completeUrl = apiBaseUrl + "dataset/icon/" + tenant + "/" + datasetCode + "/";

		GetMethod getMethod = new GetMethod(completeUrl);
		HttpClient httpclient = new HttpClient();
		int result = httpclient.executeMethod(getMethod);
		log.debug("[ResourcesDelegate::loadDatasetIcon] result: " + result);

		return getMethod.getResponseBody();

	}

}
