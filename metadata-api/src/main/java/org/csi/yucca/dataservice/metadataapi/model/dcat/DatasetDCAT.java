package org.csi.yucca.dataservice.metadataapi.model.dcat;

import java.util.ArrayList;
import java.util.List;
import org.csi.yucca.dataservice.metadataapi.model.dcat.VCTypeDCAT;

public class DatasetDCAT {

	private String description; // metadata.info.description
	private String title; // metadata.info.datasetName
	private VcardDCAT contactPoint = new VcardDCAT();
	private ArrayList<String> keyword = new ArrayList<String>(); // metadata.info.tags
																	// []
	private AgentDSDCAT publisher = new AgentDSDCAT(); // dct:publisher
	private String theme; // metadata.info.dataDomain
	private String accessRights; // metadata.info.visibility
	private String accrualPeriodicity; // metadata.info.fps
	private String identifier; // metadata.datasetCode + "_" +
								// metadata.datasetVersion
	private String landingPage; // url su portale del dataset (da valorizzare a
								// runtime)
	//private String spatial = "WGS84/UTM 32N";
	private String type = "dcat:Dataset"; // oppure metadata.configData.type ??
	private Long modified; // opendata.dataUpdateDate
	private String versionInfo; // metadata.datasetVersion
	private String subTheme; // metadata.info.codSubDomain
	private AgentDSDCAT rightsHolder = new AgentDSDCAT();;
	//private String creator = "CSI PIEMONTE";

	private List<DistributionDCAT> distribution = new ArrayList<DistributionDCAT>();

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public VcardDCAT getContactPoint() {
		return contactPoint;
	}
	
	public void setContactPoint(VcardDCAT contactPoint){
		this.contactPoint = contactPoint;
	}

	public List<DistributionDCAT> getDistribution() {
		return distribution;
	}

	public void setDistribution(List<DistributionDCAT> distribution) {
		this.distribution = distribution;
	}

	public void addDistribution(DistributionDCAT dist) {
		this.distribution.add(dist);
	}

	public ArrayList<String> getKeyword() {
		return keyword;
	}

	public void setKeyword(ArrayList<String> keyword) {
		this.keyword = keyword;
	}

	public void addKeyword(String keyword) {
		this.keyword.add(keyword);
	}

	public AgentDSDCAT getPublisher() {
		return publisher;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(String accessRights) {
		this.accessRights = accessRights;
	}

	public String getAccrualPeriodicity() {
		return accrualPeriodicity;
	}

	public void setAccrualPeriodicity(String accrualPeriodicity) {
		this.accrualPeriodicity = accrualPeriodicity;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	/*public String getSpatial() {
		return spatial;
	}*/

	public String getType() {
		return type;
	}

	public Long getModified() {
		return modified;
	}

	public void setModified(Long modified) {
		this.modified = modified;
	}

	public String getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	public String getSubTheme() {
		return subTheme;
	}

	public void setSubTheme(String subTheme) {
		this.subTheme = subTheme;
	}

	public AgentDSDCAT getRightsHolder() {
		return rightsHolder;
	}

	/*public String getCreator() {
		return creator;
	}*/
}
