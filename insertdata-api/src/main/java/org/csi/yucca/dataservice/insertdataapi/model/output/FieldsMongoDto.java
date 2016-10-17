package org.csi.yucca.dataservice.insertdataapi.model.output;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

public class FieldsMongoDto {

	public static final String DATA_TYPE_INT = "int";
	public static final String DATA_TYPE_LONG = "long";
	public static final String DATA_TYPE_DOUBLE = "double";
	public static final String DATA_TYPE_FLOAT = "float";
	public static final String DATA_TYPE_STRING = "string";
	public static final String DATA_TYPE_BOOLEAN = "boolean";
	public static final String DATA_TYPE_DATETIME = "dateTime";
	public static final String DATA_TYPE_LON = "longitude";
	public static final String DATA_TYPE_LAT = "latitude";	
	public static final String DATA_TYPE_BINARY = "binary";	
	

	private static Map<String,String> testMap;
	static {

		testMap= new HashMap<String, String>();
		testMap.put(DATA_TYPE_INT, DATA_TYPE_INT);
		testMap.put(DATA_TYPE_LONG, DATA_TYPE_LONG);
		testMap.put(DATA_TYPE_DOUBLE, DATA_TYPE_DOUBLE);
		testMap.put(DATA_TYPE_STRING, DATA_TYPE_STRING);
		testMap.put(DATA_TYPE_BOOLEAN, DATA_TYPE_BOOLEAN);
		testMap.put(DATA_TYPE_DATETIME, DATA_TYPE_DATETIME);
		testMap.put(DATA_TYPE_LON, DATA_TYPE_LON);
		testMap.put(DATA_TYPE_LAT, DATA_TYPE_LAT);
		testMap.put(DATA_TYPE_FLOAT, DATA_TYPE_FLOAT);
		testMap.put(DATA_TYPE_BINARY, DATA_TYPE_BINARY);


	}

	private boolean successChecked= false;
	
	public boolean isSuccessChecked() {
		return successChecked;
	}
	public void setSuccessChecked(boolean successChecked) {
		this.successChecked = successChecked;
	}

	private static final Pattern r8601 = Pattern.compile(""
			+ "(\\d{4})"
			+ "-(\\d{2})"
			+ "-(\\d{2})"
			+ "T"
				+ "(\\d{2}):"
				+ "(\\d{2}):"
				+ "(\\d{2})"
				+ "((\\.(\\d{3}))?)"
			+ ""
			+ "($|(((\\+|-)(\\d{2}):(\\d{2}))))");	
//	private static final Pattern r8601 = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T((\\d{2}):"+
//            "(\\d{2}):(\\d{2})\\.(\\d{3})?)((\\+|-)(\\d{2}):?(\\d{2}))?");	

	
	//
	
	public FieldsMongoDto (String fieldName,String fieldType) throws Exception{
		this.setFieldName(fieldName);
		this.setFieldType(fieldType);
	}
	public FieldsMongoDto (String fieldName,String fieldType,long datasetId,long datasetVersion) throws Exception{
		this.setFieldName(fieldName);
		this.setFieldType(fieldType);
		this.setDatasetId(datasetId);
		this.setDatasetVersion(datasetVersion);
	}

	private String fieldName=null;
	private String fieldType=null;
	private long datasetId=-1;
	private long datasetVersion=-1;	
	public long getDatasetId() {
		return datasetId;
	}
	public void setDatasetId(long datasetId) {
		this.datasetId = datasetId;
	}
	public long getDatasetVersion() {
		return datasetVersion;
	}
	public void setDatasetVersion(long datasetVersion) {
		this.datasetVersion = datasetVersion;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) throws Exception{
		if (null== fieldName || fieldName.trim().length()<=0) throw new Exception("invalid field");
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) throws Exception{
		if (null== fieldType || fieldType.trim().length()<=0 || null==testMap.get(fieldType)) throw new Exception("invalid Type");
		this.fieldType = fieldType.trim();
	}


	public boolean validateValue(String valueToCheck) {
		if (this.datasetVersion!=1 && valueToCheck==null) return true ;
		if (this.datasetVersion==1 && valueToCheck==null) return false ;
		
		if (DATA_TYPE_INT.equals(this.fieldType)) return validateInt(valueToCheck); 
		if (DATA_TYPE_LONG.equals(this.fieldType)) return validateLong(valueToCheck); 
		if (DATA_TYPE_DOUBLE.equals(this.fieldType)) return validateDouble(valueToCheck); 
		if (DATA_TYPE_FLOAT.equals(this.fieldType)) return validateFloat(valueToCheck); 
		if (DATA_TYPE_STRING.equals(this.fieldType)) return validateString(valueToCheck); 
		if (DATA_TYPE_BOOLEAN.equals(this.fieldType)) return validateBoolean(valueToCheck); 
		if (DATA_TYPE_DATETIME.equals(this.fieldType)) return validateDate(valueToCheck); 
		if (DATA_TYPE_LON.equals(this.fieldType)) return validateDouble(valueToCheck); 
		if (DATA_TYPE_LAT.equals(this.fieldType)) return validateDouble(valueToCheck); 
		if (DATA_TYPE_BINARY.equals(this.fieldType)) return validateBinary(valueToCheck); 
		return false;
	}

	private boolean validateInt(String valueToCheck) {
		try {
			Integer.parseInt(valueToCheck); 
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	private boolean validateDate_old(String valueToCheck) {
		Matcher m = r8601.matcher(valueToCheck);
		if (!m.lookingAt()) return false;		
		
		return true;
	}


	private boolean validateDate(String valueToCheck) {
		try {
			Object o= null;
			SimpleDateFormat format = new SimpleDateFormat(_msDateFormat);
			format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
			o = format.parse(valueToCheck, new ParsePosition(0));
			if (o == null) {
				// try older format with no ms
				format = new SimpleDateFormat(_secDateFormat);
				format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				o = format.parse(valueToCheck, new ParsePosition(0));
				if (o == null) {
					// try timezone
					format = new SimpleDateFormat(_msDateFormat_TZ);
					format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
					o = format.parse(valueToCheck, new ParsePosition(0));
					if (o == null) {
						// try older format timezone
						format = new SimpleDateFormat(_secDateFormat_TZ);
						format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
						o = format.parse(valueToCheck, new ParsePosition(0));
					}
					if (o == null) {
						// try isoDate with JAXB
						Calendar cal = DatatypeConverter.parseDateTime(valueToCheck);
						o = cal.getTime();
					}

				}


			}


			if (o==null ) return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static final String _msDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String _secDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String _msDateFormat_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String _secDateFormat_TZ = "yyyy-MM-dd'T'HH:mm:ssZ";


	private boolean validateString(String valueToCheck) {
		return true;
	}
	private boolean validateBinary(String valueToCheck) {
		return true;
	}
	private boolean validateLong(String valueToCheck) {
		try {
			Long.parseLong(valueToCheck); 
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	private boolean validateDouble(String valueToCheck) {
		try {
			Double.parseDouble(valueToCheck); 
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	private boolean validateBoolean(String valueToCheck) {
		
		if (!("true".equalsIgnoreCase(valueToCheck.trim()) || "false".equalsIgnoreCase(valueToCheck.trim()))) return false;
		
		
		return true;
	}
	private boolean validateFloat(String valueToCheck) {
		try {
			Float.parseFloat(valueToCheck); 
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	
	public String getInsertJson (String value) {
		String ret=this.fieldName+" : ";
		if (value==null) {
			ret+="null";
			return ret;
		}
		if (DATA_TYPE_INT.equals(this.fieldType)) ret+=value;
		if (DATA_TYPE_LONG.equals(this.fieldType)) ret+=value;
		if (DATA_TYPE_DOUBLE.equals(this.fieldType)) ret+=value;
		if (DATA_TYPE_FLOAT.equals(this.fieldType)) ret+=value;
		if (DATA_TYPE_STRING.equals(this.fieldType)) ret+="\""+value.replace("\"", "\\\"")+"\"";
		//if (DATA_TYPE_STRING.equals(this.fieldType)) ret+="\""+value+"\"";
		if (DATA_TYPE_BOOLEAN.equals(this.fieldType)) ret+=value; 
		//if (DATA_TYPE_DATETIME.equals(this.fieldType)) ret+="ISODate(\""+value+"\")";
		if (DATA_TYPE_DATETIME.equals(this.fieldType)) ret+="{$date : \""+value+"\"}";
		if (DATA_TYPE_LON.equals(this.fieldType)) ret+=value;
		if (DATA_TYPE_LAT.equals(this.fieldType)) ret+=value;
		if (DATA_TYPE_BINARY.equals(this.fieldType)) ret+="\""+value.replace("\"", "\\\"")+"\"";
		
		
		//{ "$date" : "2013-02-07T09:09:09.212Z"}
		
		return ret;
	}
}
