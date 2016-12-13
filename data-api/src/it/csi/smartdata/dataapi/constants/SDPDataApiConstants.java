package it.csi.smartdata.dataapi.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmType;

public interface SDPDataApiConstants {

	public static final long SDP_MAX_DOC_FOR_ORDERBY=100000;
	
	public static final String SDP_WEB_FILTER_PATTERN="/SmartDataOdataService.svc/";
	public static final String SDP_WEB_SERVLET_URL="/SDPOdataServlet.svc/";
//	public static final String SDP_WEB_BASE_URL="http://localhost:8080/odataServer/SmartDataOdataService.svc/";
	public static final String SDP_WEB_CSVSERVLET_URL="/SDPOdataCSVservlet.svc/";
	
	
	public static final String SDP_ODATA_DEFAULT_SERVICE_FACTORY="it.csi.smartdata.dataapi.odataSDPServiceFactory";
	
	
	
	
	
	
	public static final String ENTITY_SET_NAME_UPLOADDATA = "DataEntities";
	
	
	//BINARY - 1.2
	public static final String ENTITY_SET_NAME_BINARY = "Binaries";
	public static final String ENTITY_NAME_BINARY = "Binary";
	public static final String COMPLEX_TYPE_BINARYREF = "BinaryRef";
	public static final String ASSOCIATION_NAME_DATASETUPLOAD_BINARY ="DataEntity_Binary_Binary_DataEntity";
	public static final String ASSOCIATION_SET_DATASETUPLOAD_BINARY = "DataEntities_Binaries";

	public static final String ROLE_DATASETUPLOAD_BINARY="DataEntity_Binary";
	public static final String ROLE_BINARY_DATASETUPLOAD="Binary_DataEntity";
	
	
	
	
	public static final String ENTITY_SET_NAME_SMARTOBJECT = "SmartObjects";
	public static final String ENTITY_SET_NAME_STREAMS = "Streams";
	public static final String ENTITY_SET_NAME_MEASURES = "Measures";
	public static final String ENTITY_SET_NAME_MEASURES_STATS = "MeasuresStats";

	public static final String ENTITY_SET_NAME_SOCIAL = "SocialFeeds";
	public static final String ENTITY_SET_NAME_SOCIAL_STATS = "SocialFeedsStats";
	public static final String ENTITY_NAME_SOCIAL = "SocialFeed";
	public static final String ENTITY_NAME_SOCIAL_STATS = "SocialFeedStat";

	
	
	public static final String SMART_ENTITY_CONTAINER="SmartDataEntityContainer";

	public static final String ENTITY_NAME_UPLOADDATA = "DataEntity";
	public static final String ENTITY_NAME_UPLOADDATA_HISTORY = "DataEntityHistory";

	
	public static final String ENTITY_NAME_SMARTOBJECT = "SmartObject";
	public static final String ENTITY_NAME_STREAMS = "Stream";
	public static final String ENTITY_NAME_MEASURES = "Measure";
	public static final String ENTITY_NAME_MEASURES_STATS = "MeasureStat";
//	public static final String ENTITY_NAME_MEASUREVALUES = "MeasureValue";
//	public static final String ENTITY_NAME_MEASURECOMPONENTS = "MeasureCopmponent";


	public static final String ASSOCIATION_NAME_MEASURE_STREAM ="Measure_Stream_Stream_Measure";
	public  static final String ASSOCIATION_SET_MEASURE_STREAM = "Measures_Streams";

	public static final String ROLE_MEASURE_STREAM="Measure_Stream";
	public static final String ROLE_STREAM_MEASURE="Stream_Measure";
	
	public static final String SDPCONFIG_CONSTANTS_TYPE_API="api";
	public static final String SDPCONFIG_CONSTANTS_TYPE_STREAM="stream";
	public static final String SDPCONFIG_CONSTANTS_TYPE_DATASET="dataset";
	
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM="apiMultiStream";
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK="apiMultiBulk";
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISOCIAL="apiMultiSocial";
	
	
	
	
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_DATASETBULK="bulkDataset";
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_DATASETSTREAM="streamDataset";
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_DATASETSOCIAL="socialDataset";
	
	
	
	public static final Map<String,EdmSimpleTypeKind> SDP_DATATYPE_MAP = new HashMap<String, EdmSimpleTypeKind>() {{
		put("Boolean" ,EdmSimpleTypeKind.Boolean);
		put("boolean" ,EdmSimpleTypeKind.Boolean);
		put("String"  ,EdmSimpleTypeKind.String);
		put("string"  ,EdmSimpleTypeKind.String);
		put("Int"     ,EdmSimpleTypeKind.Int32);
		put("int"     ,EdmSimpleTypeKind.Int32);
		put("Long"    ,EdmSimpleTypeKind.Int64);
		put("long"    ,EdmSimpleTypeKind.Int64);
		put("Double"  ,EdmSimpleTypeKind.Double);
		put("double"  ,EdmSimpleTypeKind.Double);
		put("Data"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("data"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("Date"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("date"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("DatetimeOffset"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("datetimeoffset"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("DateTime"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("datetime"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("dateTime"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("Time"    ,EdmSimpleTypeKind.DateTimeOffset);
		put("time"    ,EdmSimpleTypeKind.DateTimeOffset);
//		put("Float"    ,EdmSimpleTypeKind.Decimal);
//		put("float"    ,EdmSimpleTypeKind.Decimal);
		put("Float"    ,EdmSimpleTypeKind.Double);
		put("float"    ,EdmSimpleTypeKind.Double);


		put("longitude"    ,EdmSimpleTypeKind.Double);
		put("latitude"    ,EdmSimpleTypeKind.Double);
		
		
		//1.2 binary
		put("binary"    ,EdmSimpleTypeKind.Binary);
		
		
		
	}};

	public static final Map<String,String> SDP_DATATYPE_SOLRSUFFIX = new HashMap<String, String>() {{
		put("Boolean" ,"_b");
		put("boolean" ,"_b");
		put("String"  ,"_s");
		put("string"  ,"_s");
		put("Int"     ,"_i");
		put("int"     ,"_i");
		put("Long"    ,"_l");
		put("long"    ,"_l");
		put("Double"  ,"_d");
		put("double"  ,"_d");
		put("Data"    ,"_dt");
		put("data"    ,"_dt");
		put("Date"    ,"_dt");
		put("date"    ,"_dt");
		put("DatetimeOffset"    ,"_dt");
		put("datetimeoffset"    ,"_dt");
		put("DateTime"    ,"_dt");
		put("datetime"    ,"_dt");
		put("dateTime"    ,"_dt");
		put("Time"    ,"_dt");
		put("time"     ,"_dt");
//		put("Float"    ,EdmSimpleTypeKind.Decimal);
//		put("float"    ,EdmSimpleTypeKind.Decimal);
		put("Float"    ,"_f");
		put("float"    ,"_f");


		put("longitude"    ,"_d");
		put("latitude"    ,"_d");
		
		
		//1.2 binary
		put("binary"    ,"_s");
		
		
		
	}};
	
	
	public static final Map<String,String> SDP_DATATYPE_PHOENIXTYPES = new HashMap<String, String>() {{
		put("Boolean" ,"TINYINT");
		put("boolean" ,"TINYINT");
		put("String"  ,"VARCHAR");
		put("string"  ,"VARCHAR");
		put("Int"     ,"INTEGER");
		put("int"     ,"INTEGER");
		put("Long"    ,"BIGINT");
		put("long"    ,"BIGINT");
		put("Double"  ,"DOUBLE");
		put("double"  ,"DOUBLE");
		put("Data"    ,"TIMESTAMP");
		put("data"    ,"TIMESTAMP");
		put("Date"    ,"TIMESTAMP");
		put("date"    ,"TIMESTAMP");
		put("DatetimeOffset"    ,"TIMESTAMP");
		put("datetimeoffset"    ,"TIMESTAMP");
		put("DateTime"    ,"TIMESTAMP");
		put("datetime"    ,"TIMESTAMP");
		put("dateTime"    ,"TIMESTAMP");
		put("Time"    ,"TIMESTAMP");
		put("time"     ,"TIMESTAMP");
//		put("Float"    ,EdmSimpleTypeKind.Decimal);
//		put("float"    ,EdmSimpleTypeKind.Decimal);
		put("Float"    ,"FLOAT");
		put("float"    ,"FLOAT");


		put("longitude"    ,"DOUBLE");
		put("latitude"    ,"DOUBLE");
		
		
		//1.2 binary
		put("binary"    ,"VARCHAR");
		
		
		
	}};

	
	
	public static final ArrayList<String> SDP_STATISTICS_OPERATIONS = new ArrayList<String>(Arrays.asList(
			"avg",
			"min",
			"max",
			"first",
			"last",
			"sum"
			)); 
	

	
	
	
}
