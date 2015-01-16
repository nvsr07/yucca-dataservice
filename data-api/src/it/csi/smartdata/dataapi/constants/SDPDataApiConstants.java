package it.csi.smartdata.dataapi.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;

public interface SDPDataApiConstants {

	
	public static final String SDP_WEB_FILTER_PATTERN="/SmartDataOdataService.svc/";
	public static final String SDP_WEB_SERVLET_URL="/SDPOdataServlet.svc/";
//	public static final String SDP_WEB_BASE_URL="http://localhost:8080/odataServer/SmartDataOdataService.svc/";
	
	
	public static final String SDP_ODATA_DEFAULT_SERVICE_FACTORY="it.csi.smartdata.dataapi.odataSDPServiceFactory";
	
	
	
	
	
	
	public static final String ENTITY_SET_NAME_UPLOADDATA = "DataEntities";
	
	
	
	public static final String ENTITY_SET_NAME_SMARTOBJECT = "SmartObjects";
	public static final String ENTITY_SET_NAME_STREAMS = "Streams";
	public static final String ENTITY_SET_NAME_MEASURES = "Measures";
	public static final String ENTITY_SET_NAME_MEASURES_STATS = "MeasuresStats";


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
	public static final String SDPCONFIG_CONSTANTS_TYPE_DATASET="stream";
	
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM="apiMultiStream";
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK="apiMultiBulk";
	
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_DATASETBULK="bulkDataset";
	public static final String SDPCONFIG_CONSTANTS_SUBTYPE_DATASETSTREAM="streamDataset";
	
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
