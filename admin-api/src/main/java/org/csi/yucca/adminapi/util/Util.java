package org.csi.yucca.adminapi.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.SharingTenantsJson;
import org.csi.yucca.adminapi.model.TagJson;
import org.csi.yucca.adminapi.request.ComponentInfoRequest;
import org.csi.yucca.adminapi.response.ComponentResponse;
import org.csi.yucca.adminapi.response.TenantResponse;
import org.fusesource.hawtbuf.ByteArrayInputStream;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

public class Util {
	
	public static String[] getCsvRows(MultipartFile file, boolean skipFirstRow)throws IOException{
		
		byte[] bytes = file.getBytes();
		
		String completeData = new String(bytes);
		
		String[] rows = completeData.split("\r\n");

		if (skipFirstRow) {
			return Arrays.copyOfRange(rows, 1, rows.length);
		}
		
		return rows;
	}

	
	public static boolean isThisDateValid(String dateToValidate, String dateFromat){

		if(dateToValidate == null){
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {

			//if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println(date);

		} catch (ParseException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static <E> boolean notEqual(E value1, E value2){

		if (value1==null && value2==null || value1 != null && value1.equals(value2)) {
			return false;
		}
		
		return true;
	}

	
	public static void addSharingTenants(String sharingTenants, List<TenantResponse> listTenantResponses) throws Exception {
		if (sharingTenants != null) {
			
			ObjectMapper mapper = new ObjectMapper();
			List<SharingTenantsJson> list = mapper.readValue(sharingTenants, new TypeReference<List<SharingTenantsJson>>() {});

			for (SharingTenantsJson json : list) {
				listTenantResponses.add(new TenantResponse(json));
			}
		}
	}
	
	public static List<ComponentInfoRequest> getComponentInfoRequests(String json) throws Exception {
		if (json != null) {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, new TypeReference<List<ComponentInfoRequest>>() {});
		}
		return null;
	}
	
	public static void addComponents(String sComponents, List<ComponentResponse> components) throws Exception {
		if (sComponents != null) {
			
			ObjectMapper mapper = new ObjectMapper();
			List<ComponentJson> listComponentJson = mapper.readValue(sComponents, new TypeReference<List<ComponentJson>>() {});

			for (ComponentJson componentJson : listComponentJson) {
				components.add(new ComponentResponse(componentJson));
			}
		}
	}

	public static List<ComponentResponse> getComponents(String sComponents) throws Exception {

		List<ComponentResponse> components = new ArrayList<ComponentResponse>();
 		
		addComponents(sComponents, components);
		
		return components;
	}
	
//	public static <E> List<E> getListFromJsonString(String jsonString, Class<E> type)throws Exception{
//
//		if(jsonString == null) return null;
//
//		ObjectMapper mapper = new ObjectMapper();
//		
//		return mapper.readValue(jsonString, new TypeReference<List<E>>(){});
//	}
//	public static <E> List<E> getListFromJsonString(String jsonString)throws Exception{
//
//		if(jsonString == null) return null;
//
//		ObjectMapper mapper = new ObjectMapper();
//		
//		return mapper.readValue(jsonString, new TypeReference<List<E>>(){});
//	}
	
	public static <T> T getFromJsonString(String jsonString, Class<T> type) throws Exception {
		if (jsonString == null) return null;

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonString, type);
	}
	
	public static String cleanStringCamelCase(String in) {
		String out = "";
		if (in != null) {
			in = in.replaceAll("[-]", " ").replaceAll("[.]", " ").replaceAll("[/]", " ");
			String[] words = in.split(" ");
			for (String word : words) {
				out += toProperCase(cleanString(word));
			}
		}

		return out;
	}

	static String toProperCase(String in) {
		if (in != null && in.length() > 1)
			return in.substring(0, 1).toUpperCase() + in.substring(1).toLowerCase();
		else if (in != null)
			return in.toUpperCase();
		return "";

	}

	public static String cleanString(String in) {
		if (in != null)
			return in.replaceAll(" ", "").replaceAll("[^\\w\\s]", "");

		return "";
	}

	public static String cleanStringCamelCase(String in, int length) {
		return safeSubstring(cleanStringCamelCase(in), length);
	}

	public static String safeSubstring(String in, int length) {
		String out = in;
		if (in != null && in.length() > length)
			out = in.substring(0, length);

		return out == null ? "" : out;
	}

	public static String dateString(Timestamp timestamp) {
		if (timestamp == null)
			return "";
		return new SimpleDateFormat(Constants.CLIENT_FORMAT_DATE).format(timestamp);
	}

	public static Integer booleanToInt(Boolean booleanValue) {
		
		if (booleanValue == null) return null;
		
		if(booleanValue==false) return 0;
		
		return 1;
	}
	
	public static Boolean intToBoolean(Integer intValue) {
		if(intValue ==null || intValue==0) return false;
		return true;
		
	}

	public static Timestamp getNow() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Timestamp dateStringToTimestamp(String dateString) {
		if (dateString == null)
			return null;

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.CLIENT_FORMAT_DATE);
			Date parsedDate = dateFormat.parse(dateString);
			Timestamp timestamp = new Timestamp(parsedDate.getTime());
			return timestamp;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isValidDateFormat(String value) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.CLIENT_FORMAT_DATE);
			date = sdf.parse(value);
			if (!value.equals(sdf.format(date))) {
				date = null;
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return date != null;
	}

	
	public static byte[] convertIconFromDBToByte(String imageBase64) {
		BufferedImage imag = null;
		try {

			if (imageBase64 != null) {
				String[] imageBase64Array = imageBase64.split(",");

				String imageBase64Clean;
				if (imageBase64Array.length > 1) {
					imageBase64Clean = imageBase64Array[1];
				} else {
					imageBase64Clean = imageBase64Array[0];
				}

				byte[] bytearray = Base64Utils.decodeFromString(imageBase64Clean);
				imag = ImageIO.read(new ByteArrayInputStream(bytearray));
			}
			if (imageBase64 == null || imag == null) {
				return null;
				// imag =
				// ImageIO.read(ImageProcessor.class.getClassLoader().getResourceAsStream(Constants.DEFAULT_IMAGE));
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(imag, "png", baos);
			baos.flush();
			byte[] iconBytes = baos.toByteArray();
			baos.close();
			return iconBytes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String defaultIconPath(HttpServletRequest request, String datasourceType){
		String scheme = request.getScheme() + "://";
	    String serverName = request.getServerName();
	    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
	    String contextPath = request.getContextPath();
	    return scheme + serverName + serverPort + contextPath + "/img/"+datasourceType+"-icon-default.png";
	}
	
	
	public static void main(String[] args) {
		String imageBase64 = "data:image/png;base64,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";
		try {
			Files.write(convertIconFromDBToByte(imageBase64), new java.io.File("prova.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static List<Integer> getTags(String tags)throws Exception{

		List<Integer> idTags = new ArrayList<Integer>();
		
		if (tags != null) {

			ObjectMapper mapper = new ObjectMapper();
			List<TagJson> listTagJson = mapper.readValue(tags, new TypeReference<List<TagJson>>() {
			});

			for (TagJson tagJson : listTagJson) {
				idTags.add(tagJson.getId_tag());
			}
		}

		return idTags;
	}
	
}
