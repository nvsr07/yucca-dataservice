package org.csi.yucca.adminapi.response.builder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.csi.yucca.adminapi.model.join.IngestionConfiguration;
import org.csi.yucca.adminapi.response.IngestionConfigurationResponse;

public class IngestionConfigurationResponseBuilder{
	
	private String jdbcNativeType;
	private String hiveType;
	private String table;
	private String column;
	private String comments;
	private String datasetCode;
	private String domain;
	private String subdomain;
	private String visibility;
	private Integer opendata;
	private Timestamp registrationDate;
	private String dbName;
	private String dbSchema;
	private String dbUrl;
	private Integer columnIndex;

	public IngestionConfigurationResponseBuilder jdbcNativeType(String jdbcNativeType){
		this.jdbcNativeType = jdbcNativeType;
		return this;
	}
	public IngestionConfigurationResponseBuilder hiveType(String hiveType){
		this.hiveType = hiveType;
		return this;
	}
	public IngestionConfigurationResponseBuilder table(String table){
		this.table = table;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder column(String column){
		this.column = column;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder comments(String comments){
		this.comments = comments;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder datasetCode(String datasetCode){
		this.datasetCode = datasetCode;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder domain(String domain){
		this.domain = domain;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder subdomain(String subdomain){
		this.subdomain = subdomain;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder visibility(String visibility){
		this.visibility = visibility;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder opendata(Integer opendata){
		this.opendata = opendata;
		return this;
	}

	public IngestionConfigurationResponseBuilder registrationDate(Timestamp registrationDate){
		this.registrationDate = registrationDate; 
		return this;
	}
	
	public IngestionConfigurationResponseBuilder dbName(String dbName){
		this.dbName = dbName;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder dbSchema(String dbSchema){
		this.dbSchema = dbSchema;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder dbUrl(String dbUrl){
		this.dbUrl = dbUrl;
		return this;
	}
	
	public IngestionConfigurationResponseBuilder columnIndex(Integer columnIndex){
		this.columnIndex = columnIndex;
		return this;
	}

	public IngestionConfigurationResponseBuilder(IngestionConfiguration model) {
		super();
		this.table = model.getTable();
		this.column = model.getColumn();
		this.comments = model.getComments();
		this.datasetCode = model.getDatasetCode();
		this.domain = model.getDomain();
		this.subdomain = model.getSubdomain();
		this.visibility = model.getVisibility();
		this.opendata = model.getOpendata();
		this.registrationDate = model.getRegistrationDate();
		this.dbName = model.getDbName();
		this.dbSchema = model.getDbSchema();
		this.dbUrl = model.getDbUrl();
		this.columnIndex = model.getColumnIndex();
		this.hiveType = model.getHiveType();
		this.jdbcNativeType = model.getJdbcNativeType();
	}

	public IngestionConfigurationResponse build(String dateformat){
		
		IngestionConfigurationResponse response = new IngestionConfigurationResponse();

		response.setTable(table);
		response.setColumn(column);
		response.setComments(comments != null ? comments.replaceAll("[\\t\\n\\r]+", " ") : "");
		response.setDatasetCode(datasetCode);
		response.setDomain(domain != null ? domain.toLowerCase() : "");
		response.setSubdomain(subdomain != null ? subdomain.toLowerCase() : "");
		response.setVisibility(visibility != null ? visibility : "");
		response.setOpendata(opendata == 1);
		response.setRegistrationDate(registrationDate != null ? new SimpleDateFormat(dateformat).format(registrationDate) : "");
		response.setDbName(dbName != null ? dbName : "");
		response.setDbSchema(dbSchema != null ? dbSchema : "");
		response.setDbUrl(dbUrl != null ? dbUrl : "");
		response.setColumnIndex(columnIndex != null ? columnIndex : 0);
		response.setJdbcNativeType(jdbcNativeType);
		response.setHiveType(hiveType);

		return response;
	}

	
}
