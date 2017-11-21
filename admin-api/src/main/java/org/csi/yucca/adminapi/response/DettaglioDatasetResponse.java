package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.util.Util;

public class DettaglioDatasetResponse extends DatasetResponse {

	private DcatResponse dcat;
	private Integer idDataSource;
	private Integer datasourceversion;
	private Integer dataSourceIsActive;
	private Integer dataSourceIsManager;
	private LicenseResponse license;
	private String datasetSubtype;
	private String startingestiondate;
	private String importfiletype;
	private Integer idDatasetType;
	private Integer idDatasetSubtype;
	private Integer idDataSourceBinary;
	private Integer datasourceversionBinary;
	private String type;
	private String typeDescription;
	private String subtypeDescription;
	private String dataSourceCopyright;
	private OpenDataResponse openData;
	private Integer isOpendata;
	private String dataSourceLastUpdate;
	private String dataSourceDisclaimer;
	private String dataSourceIcon;

	private List<ComponentResponse> components = new ArrayList<ComponentResponse>();
	private List<TenantResponse> sharingTenants = new ArrayList<TenantResponse>();
	
	public DettaglioDatasetResponse(DettaglioDataset dettaglioDataset) throws Exception {
		
		super(dettaglioDataset);
		
		this.dcat = new DcatResponse(dettaglioDataset.getDcat());
		
		this.idDataSource = dettaglioDataset.getIdDataSource();
		this.datasourceversion = dettaglioDataset.getDatasourceversion();
		this.dataSourceIsActive = dettaglioDataset.getDataSourceIsActive();
		this.dataSourceIsManager = dettaglioDataset.getDataSourceIsManager();
		this.license = new LicenseResponse(dettaglioDataset.getLicense());
		this.datasetSubtype = dettaglioDataset.getDatasetSubtype();
		this.startingestiondate = Util.dateString(dettaglioDataset.getStartingestiondate());
		this.importfiletype = dettaglioDataset.getImportfiletype();
		this.idDatasetType = dettaglioDataset.getIdDatasetType();
		this.idDatasetSubtype = dettaglioDataset.getIdDatasetSubtype();
		this.idDataSourceBinary = dettaglioDataset.getIdDataSourceBinary();
		this.datasourceversionBinary = dettaglioDataset.getDatasourceversionBinary();
		this.type = dettaglioDataset.getDatasetType();
		this.typeDescription = dettaglioDataset.getDatasetTypeDescription();
		this.subtypeDescription = dettaglioDataset.getDatasetSubtypeDescription();
		this.dataSourceCopyright = dettaglioDataset.getDataSourceCopyright();
		this.openData = new OpenDataResponse(dettaglioDataset);
		this.isOpendata = dettaglioDataset.getDataSourceIsOpendata();
		this.dataSourceLastUpdate = dettaglioDataset.getDataSourceLastUpdate();
		this.dataSourceDisclaimer = dettaglioDataset.getDataSourceDisclaimer();
		this.dataSourceIcon = dettaglioDataset.getDataSourceIcon();
		Util.addSharingTenants(dettaglioDataset.getSharingTenant(), this.sharingTenants);
		Util.addComponents(dettaglioDataset.getComponents(), this.components);
	}

	public DcatResponse getDcat() {
		return dcat;
	}
	public void setDcat(DcatResponse dcat) {
		this.dcat = dcat;
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
	public Integer getDataSourceIsActive() {
		return dataSourceIsActive;
	}
	public void setDataSourceIsActive(Integer dataSourceIsActive) {
		this.dataSourceIsActive = dataSourceIsActive;
	}
	public Integer getDataSourceIsManager() {
		return dataSourceIsManager;
	}
	public void setDataSourceIsManager(Integer dataSourceIsManager) {
		this.dataSourceIsManager = dataSourceIsManager;
	}
	public LicenseResponse getLicense() {
		return license;
	}
	public void setLicense(LicenseResponse license) {
		this.license = license;
	}
	public String getDatasetSubtype() {
		return datasetSubtype;
	}
	public void setDatasetSubtype(String datasetSubtype) {
		this.datasetSubtype = datasetSubtype;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeDescription() {
		return typeDescription;
	}
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
	public String getSubtypeDescription() {
		return subtypeDescription;
	}
	public void setSubtypeDescription(String subtypeDescription) {
		this.subtypeDescription = subtypeDescription;
	}
	public String getDataSourceCopyright() {
		return dataSourceCopyright;
	}
	public void setDataSourceCopyright(String dataSourceCopyright) {
		this.dataSourceCopyright = dataSourceCopyright;
	}
	public OpenDataResponse getOpenData() {
		return openData;
	}
	public void setOpenData(OpenDataResponse openData) {
		this.openData = openData;
	}
	public Integer getIsOpendata() {
		return isOpendata;
	}
	public void setIsOpendata(Integer isOpendata) {
		this.isOpendata = isOpendata;
	}
	public String getDataSourceLastUpdate() {
		return dataSourceLastUpdate;
	}
	public void setDataSourceLastUpdate(String dataSourceLastUpdate) {
		this.dataSourceLastUpdate = dataSourceLastUpdate;
	}
	public String getDataSourceDisclaimer() {
		return dataSourceDisclaimer;
	}
	public void setDataSourceDisclaimer(String dataSourceDisclaimer) {
		this.dataSourceDisclaimer = dataSourceDisclaimer;
	}
	public String getDataSourceIcon() {
		return dataSourceIcon;
	}
	public void setDataSourceIcon(String dataSourceIcon) {
		this.dataSourceIcon = dataSourceIcon;
	}
	public List<ComponentResponse> getComponents() {
		return components;
	}
	public void setComponents(List<ComponentResponse> components) {
		this.components = components;
	}
	public List<TenantResponse> getSharingTenants() {
		return sharingTenants;
	}
	public void setSharingTenants(List<TenantResponse> sharingTenants) {
		this.sharingTenants = sharingTenants;
	}
	
	
}
