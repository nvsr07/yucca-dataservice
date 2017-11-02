package org.csi.yucca.adminapi.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.csi.yucca.adminapi.util.Constants;

public class Util {

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

}
