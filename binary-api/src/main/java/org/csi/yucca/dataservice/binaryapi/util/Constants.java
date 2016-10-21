package org.csi.yucca.dataservice.binaryapi.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Constants {

	public static final String API_NAMESPACE_BASE = "it.csi.smartdata.odata";
	public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.S";
	public static final int MAX_NUM_ROW_DATA_DOWNLOAD = 10000;
	public static final String DEFAULT_IMAGE = "smart.png";
	public static final int DEFAULT_IMAGE_WIDTH = 256;
	public static final int DEFAULT_IMAGE_HEIGHT = 256;
	public static final String DEFAULT_ODATA_IMAGE = "odataOverlay.png";
	public static final Integer SEARCH_MAX_RESULT = 10;
	public static final String OUTPUT_FORMAT_CKAN = "ckan"; 
	public static final String[] SUPPORTED_LANGUAGES = new String[] { "it", "en" };
	public static final DateFormat DEFAULT_FIELD_DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.getDefault());
	
	public static final DateFormat ISO_DATE_FORMAT() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
		isoDateFormat.setTimeZone(tz);
		return isoDateFormat;
	}
	
	public static void main(String[] args) {
		System.out.println(DEFAULT_FIELD_DATE_FORMAT);
		DateFormat formatter = Constants.DEFAULT_FIELD_DATE_FORMAT;
		formatter.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
		System.out.println(formatter.toString());
	}
}
