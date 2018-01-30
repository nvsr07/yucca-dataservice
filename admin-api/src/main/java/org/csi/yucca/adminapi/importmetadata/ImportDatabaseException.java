package org.csi.yucca.adminapi.importmetadata;

public class ImportDatabaseException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final String INVALID_DB_TYPE = "Database type not supported";
	public static final String CONNECTION_FAILED = "Connection failed";

	private String detail;

	public ImportDatabaseException(String message, String detail) {
		super(message);
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String toJson() {
		String json = "{\"error\":\"" + getMessage() + "\"";
		if (getDetail() != null)
			json += ",\"detail\":\"" + getDetail().replaceAll("[^\\w\\s]", "").replace("\n", " ").replace("\r", "") + "\"";
		json += "}";
		return json;
	}

	public static void main(String[] args) {
		ImportDatabaseException importDatabaseException = new ImportDatabaseException("ciao", "ciaaa  odkpdsf. safdds++**safd �");
		System.out.println("-- " + importDatabaseException.toJson());
	}

}
