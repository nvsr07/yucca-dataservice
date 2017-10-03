package org.csi.yucca.adminapi.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.csi.yucca.adminapi.util.Constants;

public class Util {

	public static String dateString(Timestamp timestamp) {
		if (timestamp == null) return "";
		return new SimpleDateFormat(Constants.CLIENT_FORMAT_DATE).format(timestamp);
	}

}
