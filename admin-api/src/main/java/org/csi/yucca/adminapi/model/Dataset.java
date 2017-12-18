package org.csi.yucca.adminapi.model;

import java.sql.Timestamp;

public class Dataset extends Dettaglio implements IOrganization, IStatus, IDomain, ISubdomain{
	


	private Integer isactive; 
	private Integer ismanager; 
	private Integer idDataSource;
	private Integer datasourceversion;
	private Integer iddataset;
	private String datasetcode;
	private String datasetname;
	private String description;

	private Timestamp startingestiondate;
	private Timestamp endingestiondate;
	private String importfiletype;
	private Integer idDatasetType;
	private Integer idDatasetSubtype;
	private String solrcollectionname;
	private String phoenixtablename;
	private String phoenixschemaname;
	private Integer availablehive;
	private Integer availablespeed;
	private Integer istransformed;
	private String dbhiveschema;
	private String dbhivetable;
	private Integer idDataSourceBinary;
	private Integer datasourceversionBinary;
	private String jdbcdburl;
	private String jdbcdbname;
	private String jdbcdbtype;
	private String jdbctablename;
	private String datasetTypeDescription;
	private String datasetType;
	private String datasetSubtype;
	private String datasetSubtypeDescription;

	
	
	public Integer getIsactive() {
		return isactive;
	}

	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}

	public Integer getIsmanager() {
		return ismanager;
	}

	public void setIsmanager(Integer ismanager) {
		this.ismanager = ismanager;
	}

	public String getDatasetTypeDescription() {
		return datasetTypeDescription;
	}

	public void setDatasetTypeDescription(String datasetTypeDescription) {
		this.datasetTypeDescription = datasetTypeDescription;
	}

	public String getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}

	public String getDatasetSubtype() {
		return datasetSubtype;
	}

	public void setDatasetSubtype(String datasetSubtype) {
		this.datasetSubtype = datasetSubtype;
	}

	public String getDatasetSubtypeDescription() {
		return datasetSubtypeDescription;
	}

	public void setDatasetSubtypeDescription(String datasetSubtypeDescription) {
		this.datasetSubtypeDescription = datasetSubtypeDescription;
	}

	public Integer getIdDataSource() {
		return idDataSource;
	}

	public void setIdDataSource(Integer idDataSource) {
		this.idDataSource = idDataSource;
	}

	public Integer getDatasourceversion() {
		return datasourceversion;
	}

	public void setDatasourceversion(Integer datasourceversion) {
		this.datasourceversion = datasourceversion;
	}

	public Integer getIddataset() {
		return iddataset;
	}

	public void setIddataset(Integer iddataset) {
		this.iddataset = iddataset;
	}

	public String getDatasetcode() {
		return datasetcode;
	}

	public void setDatasetcode(String datasetcode) {
		this.datasetcode = datasetcode;
	}

	public String getDatasetname() {
		return datasetname;
	}

	public void setDatasetname(String datasetname) {
		this.datasetname = datasetname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getStartingestiondate() {
		return startingestiondate;
	}

	public void setStartingestiondate(Timestamp startingestiondate) {
		this.startingestiondate = startingestiondate;
	}

	public Timestamp getEndingestiondate() {
		return endingestiondate;
	}

	public void setEndingestiondate(Timestamp endingestiondate) {
		this.endingestiondate = endingestiondate;
	}

	public String getImportfiletype() {
		return importfiletype;
	}

	public void setImportfiletype(String importfiletype) {
		this.importfiletype = importfiletype;
	}

	public Integer getIdDatasetType() {
		return idDatasetType;
	}

	public void setIdDatasetType(Integer idDatasetType) {
		this.idDatasetType = idDatasetType;
	}

	public Integer getIdDatasetSubtype() {
		return idDatasetSubtype;
	}

	public void setIdDatasetSubtype(Integer idDatasetSubtype) {
		this.idDatasetSubtype = idDatasetSubtype;
	}

	public String getSolrcollectionname() {
		return solrcollectionname;
	}

	public void setSolrcollectionname(String solrcollectionname) {
		this.solrcollectionname = solrcollectionname;
	}

	public String getPhoenixtablename() {
		return phoenixtablename;
	}

	public void setPhoenixtablename(String phoenixtablename) {
		this.phoenixtablename = phoenixtablename;
	}

	public String getPhoenixschemaname() {
		return phoenixschemaname;
	}

	public void setPhoenixschemaname(String phoenixschemaname) {
		this.phoenixschemaname = phoenixschemaname;
	}

	public Integer getAvailablehive() {
		return availablehive;
	}

	public void setAvailablehive(Integer availablehive) {
		this.availablehive = availablehive;
	}

	public Integer getAvailablespeed() {
		return availablespeed;
	}

	public void setAvailablespeed(Integer availablespeed) {
		this.availablespeed = availablespeed;
	}

	public Integer getIstransformed() {
		return istransformed;
	}

	public void setIstransformed(Integer istransformed) {
		this.istransformed = istransformed;
	}

	public String getDbhiveschema() {
		return dbhiveschema;
	}

	public void setDbhiveschema(String dbhiveschema) {
		this.dbhiveschema = dbhiveschema;
	}

	public String getDbhivetable() {
		return dbhivetable;
	}

	public void setDbhivetable(String dbhivetable) {
		this.dbhivetable = dbhivetable;
	}

	public Integer getIdDataSourceBinary() {
		return idDataSourceBinary;
	}

	public void setIdDataSourceBinary(Integer idDataSourceBinary) {
		this.idDataSourceBinary = idDataSourceBinary;
	}

	public Integer getDatasourceversionBinary() {
		return datasourceversionBinary;
	}

	public void setDatasourceversionBinary(Integer datasourceversionBinary) {
		this.datasourceversionBinary = datasourceversionBinary;
	}

	public String getJdbcdburl() {
		return jdbcdburl;
	}

	public void setJdbcdburl(String jdbcdburl) {
		this.jdbcdburl = jdbcdburl;
	}

	public String getJdbcdbname() {
		return jdbcdbname;
	}

	public void setJdbcdbname(String jdbcdbname) {
		this.jdbcdbname = jdbcdbname;
	}

	public String getJdbcdbtype() {
		return jdbcdbtype;
	}

	public void setJdbcdbtype(String jdbcdbtype) {
		this.jdbcdbtype = jdbcdbtype;
	}

	public String getJdbctablename() {
		return jdbctablename;
	}

	public void setJdbctablename(String jdbctablename) {
		this.jdbctablename = jdbctablename;
	}

}
