package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.util.Util;

public class DettaglioDatasetResponse extends DatasetResponse {
	
	private Integer idDataSource;
	private Integer dataSourceIsActive;
	private Integer dataSourceIsManager;
	private String startingestiondate;
	private String importfiletype;
	private String dataSourceCopyright;
	private Integer isOpendata;
	private String dataSourceLastUpdate;
	private String dataSourceDisclaimer;
	private String dataSourceIcon;
	private String jdbcdbschema;
	private String importedfiles;
	
	private List<ComponentResponse> components = new ArrayList<ComponentResponse>();
	private List<TenantResponse> sharingTenants = new ArrayList<TenantResponse>();
	private LicenseResponse license;
	private OpenDataResponse openData;
	private DcatResponse dcat;
	
	public DettaglioDatasetResponse(DettaglioDataset dettaglioDataset) throws Exception {
		
		super(dettaglioDataset);

		this.jdbcdbschema = dettaglioDataset.getJdbcdbschema();
		this.importedfiles = dettaglioDataset.getImportedfiles();
		this.dcat = new DcatResponse(dettaglioDataset.getDcat());
		this.idDataSource = dettaglioDataset.getIdDataSource();
		this.dataSourceIsActive = dettaglioDataset.getDataSourceIsActive();
		this.dataSourceIsManager = dettaglioDataset.getDataSourceIsManager();
		this.license = new LicenseResponse(dettaglioDataset.getLicense());
		this.startingestiondate = Util.dateString(dettaglioDataset.getStartingestiondate());
		this.importfiletype = dettaglioDataset.getImportfiletype();
		this.dataSourceCopyright = dettaglioDataset.getDataSourceCopyright();
		this.openData = new OpenDataResponse(dettaglioDataset);
		this.isOpendata = dettaglioDataset.getDataSourceIsOpendata();
		this.dataSourceLastUpdate = dettaglioDataset.getDataSourceLastUpdate();
		this.dataSourceDisclaimer = dettaglioDataset.getDataSourceDisclaimer();
		this.dataSourceIcon = dettaglioDataset.getDataSourceIcon();
		Util.addSharingTenants(dettaglioDataset.getSharingTenant(), this.sharingTenants);
		Util.addComponents(dettaglioDataset.getComponents(), this.components);
	}

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

	public Integer getIdDataSource() {
		return idDataSource;
	}

	public void setIdDataSource(Integer idDataSource) {
		this.idDataSource = idDataSource;
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

	public String getDataSourceCopyright() {
		return dataSourceCopyright;
	}

	public void setDataSourceCopyright(String dataSourceCopyright) {
		this.dataSourceCopyright = dataSourceCopyright;
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

	public LicenseResponse getLicense() {
		return license;
	}

	public void setLicense(LicenseResponse license) {
		this.license = license;
	}

	public OpenDataResponse getOpenData() {
		return openData;
	}

	public void setOpenData(OpenDataResponse openData) {
		this.openData = openData;
	}

	public DcatResponse getDcat() {
		return dcat;
	}

	public void setDcat(DcatResponse dcat) {
		this.dcat = dcat;
	}
}
