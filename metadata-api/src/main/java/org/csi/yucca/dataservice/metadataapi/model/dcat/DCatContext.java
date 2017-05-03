package org.csi.yucca.dataservice.metadataapi.model.dcat;

public class DCatContext {
	private String adms;
	private String dcat;
	private String dcterms;
	private String foaf;
	private String rdf;
	private String rdfs;
	private String schema;
	private String vcard;
	private String xsd;

	public DCatContext() {
		super();
		adms = "http://www.w3.org/ns/adms#";
		dcat = "http://www.w3.org/ns/dcat#";
		dcterms = "http://purl.org/dc/terms/";
		foaf = "http://xmlns.com/foaf/0.1/";
		rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		rdfs = "http://www.w3.org/2000/01/rdf-schema#";
		schema = "http://schema.org/";
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

}
