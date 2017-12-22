package org.csi.yucca.adminapi.response;

import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.util.Util;

public class DatasetDettaglioResponse {

	private String startingestiondate;
	private String importfiletype;
	private Integer iddataset;
	private String datasetcode;
	private String datasetname;
	private String description;
	private DatasetTypeResponse datasetType;
	private DatasetSubtypeResponse datasetSubtype;
	private Integer idDataSourceBinary;
	private Integer datasourceversionBinary;
	private Boolean availablehive;
	private Boolean availablespeed;
	private Boolean istransformed;
	private String dbhiveschema;
	private String dbhivetable;
	private String jdbcdburl;
	private String jdbcdbname;
	private String jdbcdbtype;
	private String jdbctablename;
	private String importedfiles;
	private String jdbcdbschema;

	public DatasetDettaglioResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DatasetDettaglioResponse(DettaglioDataset dettaglioDataset) {
		super();
		this.startingestiondate = Util.dateString(dettaglioDataset.getStartingestiondate());
		this.importfiletype = dettaglioDataset.getImportfiletype();
		this.iddataset = dettaglioDataset.getIddataset();
		this.datasetcode = dettaglioDataset.getDatasetcode();
		this.datasetname = dettaglioDataset.getDatasetname();
		this.description = dettaglioDataset.getDescription();
		this.datasetType = new DatasetTypeResponse(dettaglioDataset);
		this.datasetSubtype = new DatasetSubtypeResponse(dettaglioDataset);
		this.idDataSourceBinary = dettaglioDataset.getIdDataSourceBinary();
		this.datasourceversionBinary = dettaglioDataset.getDatasourceversionBinary();
		this.availablehive = dettaglioDataset.getAvailablehive() == Util.booleanToInt(true);
		this.availablespeed = dettaglioDataset.getAvailablespeed() == Util.booleanToInt(true);
		this.istransformed = dettaglioDataset.getIstransformed() == Util.booleanToInt(true);
		this.dbhiveschema = dettaglioDataset.getDbhiveschema();
		this.dbhivetable = dettaglioDataset.getDbhivetable();
		this.jdbcdburl = dettaglioDataset.getJdbcdburl();
		this.jdbcdbname = dettaglioDataset.getJdbcdbname();
		this.jdbcdbtype = dettaglioDataset.getJdbcdbtype();
		this.jdbctablename = dettaglioDataset.getJdbctablename();
		this.importedfiles = dettaglioDataset.getImportedfiles();
		this.jdbcdbschema = dettaglioDataset.getJdbcdbschema();
	}

	public String getStartingestiondate() {
		return startingestiondate;
	}

	public void setStartingestiondate(String startingestiondate) {
		this.startingestiondate = startingestiondate;
	}

	public String getImportfiletype() {
		return importfiletype;
	}

	public void setImportfiletype(String importfiletype) {
		this.importfiletype = importfiletype;
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

	public DatasetTypeResponse getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(DatasetTypeResponse datasetType) {
		this.datasetType = datasetType;
	}

	public DatasetSubtypeResponse getDatasetSubtype() {
		return datasetSubtype;
	}

	public void setDatasetSubtype(DatasetSubtypeResponse datasetSubtype) {
		this.datasetSubtype = datasetSubtype;
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

	public Boolean getAvailablehive() {
		return availablehive;
	}

	public void setAvailablehive(Boolean availablehive) {
		this.availablehive = availablehive;
	}

	public Boolean getAvailablespeed() {
		return availablespeed;
	}

	public void setAvailablespeed(Boolean availablespeed) {
		this.availablespeed = availablespeed;
	}

	public Boolean getIstransformed() {
		return istransformed;
	}

	public void setIstransformed(Boolean istransformed) {
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

	public String getImportedfiles() {
		return importedfiles;
	}

	public void setImportedfiles(String importedfiles) {
		this.importedfiles = importedfiles;
	}

	public String getJdbcdbschema() {
		return jdbcdbschema;
	}

	public void setJdbcdbschema(String jdbcdbschema) {
		this.jdbcdbschema = jdbcdbschema;
	}

}
