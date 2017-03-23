package org.csi.yucca.datainsert.exception;


public class InsertApiBaseException extends Exception{

	
	public static final String ERROR_CODE_STREAM_NOT_FOUND="E011";
	public static final String ERROR_CODE_INVALID_JSON="E012";
	public static final String ERROR_CODE_INVALID_COMPONENTS="E013";
	public static final String ERROR_CODE_DATASET_DATASETVERSION_INVALID="E014";
	public static final String ERROR_CODE_DATASET_MAXRECORDS="E015";

	
	public static final String ERROR_CODE_INPUT_SENSOR_MANCANTE="E016";
	public static final String ERROR_CODE_INPUT_STREAM_MANCANTE="E017";
	public static final String ERROR_CODE_INPUT_DATA_NOTARRAY="E018";
	public static final String ERROR_CODE_INPUT_DUPLICATE="E019";
	public static final String ERROR_CODE_INPUT_INVALID_DATA_VALUE="E020";
	
	
	private String errorName=null;
	private String errorCode=null;
	private String output=null;
	private String errorMessage=null;
	
	private String getMessageFromCode(String errorCode) {
		if (ERROR_CODE_STREAM_NOT_FOUND.equals(errorCode)) return "Stream unknown";
		else if (ERROR_CODE_INVALID_JSON.equals(errorCode)) return "Json validation failed";
		else if (ERROR_CODE_INVALID_COMPONENTS.equals(errorCode)) return "Json components are not coherent with stream definition";
		else if (ERROR_CODE_DATASET_DATASETVERSION_INVALID.equals(errorCode)) return "Dataset or dataset version validation failed";
		else if (ERROR_CODE_DATASET_MAXRECORDS.equals(errorCode)) return "Input size validation failed";
		else if (ERROR_CODE_INPUT_SENSOR_MANCANTE.equals(errorCode)) return "Input validation failed: missing sensor information";
		else if (ERROR_CODE_INPUT_STREAM_MANCANTE.equals(errorCode)) return "Input validation failed: missing stream/application information";
		else if (ERROR_CODE_INPUT_DATA_NOTARRAY.equals(errorCode)) return "Input validation failed: input object must be an array";
		else if (ERROR_CODE_INPUT_DUPLICATE.equals(errorCode)) return "Input validation failed: duplicate input block ";
		else if (ERROR_CODE_INPUT_INVALID_DATA_VALUE.equals(errorCode)) return "Invalid data value ";
		else return "unknown error";
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public InsertApiBaseException(String errorCode, Throwable cause, String additionalMessage) {
		super(cause);
		this.errorCode=errorCode;
		this.errorName=getMessageFromCode(errorCode)+(additionalMessage != null ? additionalMessage : "" ) ;
	}

	public InsertApiBaseException(String errorCode, String additionalMessage) {
		super();
		this.errorCode=errorCode;
		this.errorName=getMessageFromCode(errorCode)+(additionalMessage != null ? additionalMessage : "" ) ;
	}

	public InsertApiBaseException(String errorCode, Throwable cause) {
		super(cause);
		this.errorCode=errorCode;
		this.errorName=getMessageFromCode(errorCode);
	}

	public InsertApiBaseException(String errorCode) {
		super();
		this.errorCode=errorCode;
		this.errorName=getMessageFromCode(errorCode);
	}
	
	
	
	
}
