package org.csi.yucca.adminapi.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.fusesource.hawtbuf.ByteArrayInputStream;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	

	
	public static <E> List<E> getListFromJsonString(String jsonString, Class<E> type){

		if(jsonString == null) return null;
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			return mapper.readValue(jsonString, new TypeReference<List<E>>(){});
			
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public static <T> T getFromJsonString(String jsonString, Class<T> type){

		if(jsonString == null) return null;
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(jsonString, type);
		} catch (Exception e) {
			return null;
		}
		
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
		if (booleanValue) {
			return 1;
		}
		return 0;
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
	
}
