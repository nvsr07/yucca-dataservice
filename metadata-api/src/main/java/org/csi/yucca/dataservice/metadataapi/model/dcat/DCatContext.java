package org.csi.yucca.dataservice.metadataapi.model.dcat;

public class DCatContext {
	private String adms;
	private String dcat;
	private String dcatapit;
	private String dcterms;
	private String dct;
	private String foaf;
	private String owl;
	private String rdf;
	private String rdfs;
	private String schema;
	private String skos;
	private String vcard;
	private String xsd;

	public DCatContext() {
		super();
		adms = "http://www.w3.org/ns/adms#";
		dcat = "http://www.w3.org/ns/dcat#";
		dcatapit= "http://dati.gov.it/onto/dcatapit#\"";
		dcterms = "http://purl.org/dc/terms/";
		setDct("http://purl.org/dc/terms/");
		foaf = "http://xmlns.com/foaf/0.1/";
		owl = "http://www.w3.org/2002/07/owl#";
		rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		rdfs = "http://www.w3.org/2000/01/rdf-schema#";
		schema = "http://schema.org/";
		skos = "http://www.w3.org/2004/02/skos/core#";
		vcard = "http://www.w3.org/2006/vcard/ns#";
		xsd = "http://www.w3.org/2001/XMLSchema#";
		

	
	}

	public String getAdms() {
		return adms;
	}

	public void setAdms(String adms) {
		this.adms = adms;
	}

	public String getDcat() {
		return dcat;
	}

	public void setDcat(String dcat) {
		this.dcat = dcat;
	}

	public String getDcterms() {
		return dcterms;
	}

	public void setDcterms(String dcterms) {
		this.dcterms = dcterms;
	}

	public String getFoaf() {
		return foaf;
	}

	public void setFoaf(String foaf) {
		this.foaf = foaf;
	}

	public String getRdf() {
		return rdf;
	}

	public void setRdf(String rdf) {
		this.rdf = rdf;
	}

	public String getRdfs() {
		return rdfs;
	}

	public void setRdfs(String rdfs) {
		this.rdfs = rdfs;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getVcard() {
		return vcard;
	}

	public void setVcard(String vcard) {
		this.vcard = vcard;
	}

	public String getXsd() {
		return xsd;
	}

	public void setXsd(String xsd) {
		this.xsd = xsd;
	}

	public String getDcatapit() {
		return dcatapit;
	}

	public void setDcatapit(String dcatapit) {
		this.dcatapit = dcatapit;
	}

	public String getOwl() {
		return owl;
	}

	public void setOwl(String owl) {
		this.owl = owl;
	}

	public String getSkos() {
		return skos;
	}

	public void setSkos(String skos) {
		this.skos = skos;
	}

	public String getDct() {
		return dct;
	}

	public void setDct(String dct) {
		this.dct = dct;
	}

}
