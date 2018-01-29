package org.csi.yucca.adminapi.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DettaglioDataset extends Dataset {

	private String jdbcdbschema;
	private String importedfiles;

	private String solrcollectionname;
	private String phoenixtablename;
	private String phoenixschemaname;

	public String getJdbcdbschema() {
		return jdbcdbschema;
	}

	public void setJdbcdbschema(String jdbcdbschema) {
		this.jdbcdbschema = jdbcdbschema;
	}

	public String getImportedfiles() {
		return importedfiles;
	}

	public void setImportedfiles(String importedfiles) {
		this.importedfiles = importedfiles;
	}

	public Component[] deserializeComponents() throws JsonParseException, JsonMappingException, IOException {
		Component[] deserializedComponents = null;
		if (getComponents() != null) {

			ObjectMapper mapper = new ObjectMapper();
			deserializedComponents = mapper.readValue(getComponents(), Component[].class);

		}
		return deserializedComponents;

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

}
