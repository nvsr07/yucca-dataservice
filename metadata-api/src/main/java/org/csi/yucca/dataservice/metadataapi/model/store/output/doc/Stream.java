package org.csi.yucca.dataservice.metadataapi.model.store.output.doc;

import java.util.Date;

public class Stream {
	private Integer twtMaxStreamsOfVE;
	private Integer twtRatePercentage;
	private Integer twtCount;
	private Double twtGeolocLat;
	private Double twtGeolocLon;
	private Double twtGeolocRadius;
	private String twtQuery;
	private String twtLang;
	private Integer idTenant;
	private Integer idVirtualEntity;
	private Integer idStream;
	private Integer idCategoriaVe;
	private Integer idTipoVe;
	private String nomeTenant;
	private String codiceTenant;
	private String virtualEntityName;
	private String virtualEntityDescription;
	private String codiceStream;
	private String nomeStream;
	private String codiceVirtualEntity;
	private String lastUpdate;
	private String lastMessage;
	private String statoStream;
	private String domainStream;
	private String licence;
	private String disclaimer;
	private String copyright;
	private String visibility;
	private String tipoVirtualEntity;
	private String categoriaVirtualEntity;
	private Boolean saveData;
	private Integer deploymentVersion;
	private String deploymentStatusCode;
	private String deploymentStatusDesc;
	private Integer publishStream;
	private Double fps;
	private Date registrationDate;
	private String nomeRichiedente;
	private String cognomeRichiedente;
	private String mailRichiedente;
	private Boolean accettazionePrivacy;
	private String streamIcon;

	private Components componenti;
	private StreamTags streamTags;
	private VirtualEntityPositions virtualEntityPositions;
	private Tenantssharing tenantssharing;
	private Opendata opendata;

	private int dcatReady;
	private String dcatCreatorName;
	private String dcatCreatorType;
	private String dcatCreatorId;
	private String dcatRightsHolderName;
	private String dcatRightsHolderType;
	private String dcatRightsHolderId;
	private String dcatNomeOrg;
	private String dcatEmailOrg;

	public int getDcatReady() {
		return dcatReady;
	}

	public void setDcatReady(int dcatReady) {
		this.dcatReady = dcatReady;
	}

	public String getDcatCreatorName() {
		return dcatCreatorName;
	}

	public void setDcatCreatorName(String dcatCreatorName) {
		this.dcatCreatorName = dcatCreatorName;
	}

	public String getDcatCreatorType() {
		return dcatCreatorType;
	}

	public void setDcatCreatorType(String dcatCreatorType) {
		this.dcatCreatorType = dcatCreatorType;
	}

	public String getDcatCreatorId() {
		return dcatCreatorId;
	}

	public void setDcatCreatorId(String dcatCreatorId) {
		this.dcatCreatorId = dcatCreatorId;
	}

	public String getDcatRightsHolderName() {
		return dcatRightsHolderName;
	}

	public void setDcatRightsHolderName(String dcatRightsHolderName) {
		this.dcatRightsHolderName = dcatRightsHolderName;
	}

	public String getDcatRightsHolderType() {
		return dcatRightsHolderType;
	}

	public void setDcatRightsHolderType(String dcatRightsHolderType) {
		this.dcatRightsHolderType = dcatRightsHolderType;
	}

	public String getDcatRightsHolderId() {
		return dcatRightsHolderId;
	}

	public void setDcatRightsHolderId(String dcatRightsHolderId) {
		this.dcatRightsHolderId = dcatRightsHolderId;
	}

	public String getDcatNomeOrg() {
		return dcatNomeOrg;
	}

	public void setDcatNomeOrg(String dcatNomeOrg) {
		this.dcatNomeOrg = dcatNomeOrg;
	}

	public String getDcatEmailOrg() {
		return dcatEmailOrg;
	}

	public void setDcatEmailOrg(String dcatEmailOrg) {
		this.dcatEmailOrg = dcatEmailOrg;
	}

	public Stream() {
		super();
	}

	public Integer getTwtMaxStreamsOfVE() {
		return twtMaxStreamsOfVE;
	}

	public void setTwtMaxStreamsOfVE(Integer twtMaxStreamsOfVE) {
		this.twtMaxStreamsOfVE = twtMaxStreamsOfVE;
	}

	public Integer getTwtRatePercentage() {
		return twtRatePercentage;
	}

	public void setTwtRatePercentage(Integer twtRatePercentage) {
		this.twtRatePercentage = twtRatePercentage;
	}

	public Integer getTwtCount() {
		return twtCount;
	}

	public void setTwtCount(Integer twtCount) {
		this.twtCount = twtCount;
	}

	public Double getTwtGeolocLat() {
		return twtGeolocLat;
	}

	public void setTwtGeolocLat(Double twtGeolocLat) {
		this.twtGeolocLat = twtGeolocLat;
	}

	public Double getTwtGeolocLon() {
		return twtGeolocLon;
	}

	public void setTwtGeolocLon(Double twtGeolocLon) {
		this.twtGeolocLon = twtGeolocLon;
	}

	public Double getTwtGeolocRadius() {
		return twtGeolocRadius;
	}

	public void setTwtGeolocRadius(Double twtGeolocRadius) {
		this.twtGeolocRadius = twtGeolocRadius;
	}

	public Integer getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(Integer idTenant) {
		this.idTenant = idTenant;
	}

	public Integer getIdVirtualEntity() {
		return idVirtualEntity;
	}

	public void setIdVirtualEntity(Integer idVirtualEntity) {
		this.idVirtualEntity = idVirtualEntity;
	}

	public Integer getIdStream() {
		return idStream;
	}

	public void setIdStream(Integer idStream) {
		this.idStream = idStream;
	}

	public Integer getIdCategoriaVe() {
		return idCategoriaVe;
	}

	public void setIdCategoriaVe(Integer idCategoriaVe) {
		this.idCategoriaVe = idCategoriaVe;
	}

	public Integer getIdTipoVe() {
		return idTipoVe;
	}

	public void setIdTipoVe(Integer idTipoVe) {
		this.idTipoVe = idTipoVe;
	}

	public String getNomeTenant() {
		return nomeTenant;
	}

	public void setNomeTenant(String nomeTenant) {
		this.nomeTenant = nomeTenant;
	}

	public String getCodiceTenant() {
		return codiceTenant;
	}

	public void setCodiceTenant(String codiceTenant) {
		this.codiceTenant = codiceTenant;
	}

	public String getVirtualEntityName() {
		return virtualEntityName;
	}

	public void setVirtualEntityName(String virtualEntityName) {
		this.virtualEntityName = virtualEntityName;
	}

	public String getVirtualEntityDescription() {
		return virtualEntityDescription;
	}

	public void setVirtualEntityDescription(String virtualEntityDescription) {
		this.virtualEntityDescription = virtualEntityDescription;
	}

	public String getCodiceStream() {
		return codiceStream;
	}

	public void setCodiceStream(String codiceStream) {
		this.codiceStream = codiceStream;
	}

	public String getNomeStream() {
		return nomeStream;
	}

	public void setNomeStream(String nomeStream) {
		this.nomeStream = nomeStream;
	}

	public String getCodiceVirtualEntity() {
		return codiceVirtualEntity;
	}

	public void setCodiceVirtualEntity(String codiceVirtualEntity) {
		this.codiceVirtualEntity = codiceVirtualEntity;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getStatoStream() {
		return statoStream;
	}

	public void setStatoStream(String statoStream) {
		this.statoStream = statoStream;
	}

	public String getDomainStream() {
		return domainStream;
	}

	public void setDomainStream(String domainStream) {
		this.domainStream = domainStream;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getTipoVirtualEntity() {
		return tipoVirtualEntity;
	}

	public void setTipoVirtualEntity(String tipoVirtualEntity) {
		this.tipoVirtualEntity = tipoVirtualEntity;
	}

	public String getCategoriaVirtualEntity() {
		return categoriaVirtualEntity;
	}

	public void setCategoriaVirtualEntity(String categoriaVirtualEntity) {
		this.categoriaVirtualEntity = categoriaVirtualEntity;
	}

	public Boolean getSaveData() {
		return saveData;
	}

	public void setSaveData(Boolean saveData) {
		this.saveData = saveData;
	}

	public Integer getDeploymentVersion() {
		return deploymentVersion;
	}

	public void setDeploymentVersion(Integer deploymentVersion) {
		this.deploymentVersion = deploymentVersion;
	}

	public String getDeploymentStatusCode() {
		return deploymentStatusCode;
	}

	public void setDeploymentStatusCode(String deploymentStatusCode) {
		this.deploymentStatusCode = deploymentStatusCode;
	}

	public String getDeploymentStatusDesc() {
		return deploymentStatusDesc;
	}

	public void setDeploymentStatusDesc(String deploymentStatusDesc) {
		this.deploymentStatusDesc = deploymentStatusDesc;
	}

	public Integer getPublishStream() {
		return publishStream;
	}

	public void setPublishStream(Integer publishStream) {
		this.publishStream = publishStream;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getNomeRichiedente() {
		return nomeRichiedente;
	}

	public void setNomeRichiedente(String nomeRichiedente) {
		this.nomeRichiedente = nomeRichiedente;
	}

	public String getCognomeRichiedente() {
		return cognomeRichiedente;
	}

	public void setCognomeRichiedente(String cognomeRichiedente) {
		this.cognomeRichiedente = cognomeRichiedente;
	}

	public String getMailRichiedente() {
		return mailRichiedente;
	}

	public void setMailRichiedente(String mailRichiedente) {
		this.mailRichiedente = mailRichiedente;
	}

	public Boolean getAccettazionePrivacy() {
		return accettazionePrivacy;
	}

	public void setAccettazionePrivacy(Boolean accettazionePrivacy) {
		this.accettazionePrivacy = accettazionePrivacy;
	}

	public String getStreamIcon() {
		return streamIcon;
	}

	public void setStreamIcon(String streamIcon) {
		this.streamIcon = streamIcon;
	}

	public Components getComponenti() {
		return componenti;
	}

	public void setComponenti(Components componenti) {
		this.componenti = componenti;
	}

	public StreamTags getStreamTags() {
		return streamTags;
	}

	public void setStreamTags(StreamTags streamTags) {
		this.streamTags = streamTags;
	}

	public VirtualEntityPositions getVirtualEntityPositions() {
		return virtualEntityPositions;
	}

	public void setVirtualEntityPositions(VirtualEntityPositions virtualEntityPositions) {
		this.virtualEntityPositions = virtualEntityPositions;
	}

	public Tenantssharing getTenantssharing() {
		return tenantssharing;
	}

	public void setTenantssharing(Tenantssharing tenantssharing) {
		this.tenantssharing = tenantssharing;
	}

	public Opendata getOpendata() {
		return opendata;
	}

	public void setOpendata(Opendata opendata) {
		this.opendata = opendata;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Double getFps() {
		return fps;
	}

	public void setFps(Double fps) {
		this.fps = fps;
	}

	public String getTwtQuery() {
		return twtQuery;
	}

	public void setTwtQuery(String twtQuery) {
		this.twtQuery = twtQuery;
	}

	public String getTwtLang() {
		return twtLang;
	}

	public void setTwtLang(String twtLang) {
		this.twtLang = twtLang;
	}

}
