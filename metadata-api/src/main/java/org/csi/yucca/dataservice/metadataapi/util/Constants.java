package org.csi.yucca.dataservice.metadataapi.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Constants {
	public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.S";


	public static final DateFormat DEFAULT_FIELD_DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.getDefault());
	
	public static final String OPENDATA_EXPORT_FORMAT_CKAN = "ckan";
	
	public static final String[] SUPPORTED_LANGUAGES = new String[] { "it", "en" };


	public static final Integer SEARCH_MAX_RESULT = 10;

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
