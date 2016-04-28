package org.csi.yucca.dataservice.metadataapi.model.output;

import java.util.Date;
import java.util.List;

import org.csi.yucca.dataservice.metadataapi.delegate.i18n.I18nDelegate;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreDoc;
import org.csi.yucca.dataservice.metadataapi.model.store.output.StoreMetadataItem;
import org.csi.yucca.dataservice.metadataapi.model.store.output.doc.ConfigData;
import org.csi.yucca.dataservice.metadataapi.model.store.output.doc.DocDatasetContent;
import org.csi.yucca.dataservice.metadataapi.model.store.output.doc.DocStreamContent;
import org.csi.yucca.dataservice.metadataapi.model.store.output.doc.Element;
import org.csi.yucca.dataservice.metadataapi.model.store.output.doc.Field;
import org.csi.yucca.dataservice.metadataapi.model.store.output.doc.Info;
import org.csi.yucca.dataservice.metadataapi.model.store.output.doc.Position;
import org.csi.yucca.dataservice.metadataapi.model.store.output.doc.Tag;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Util;
import org.csi.yucca.dataservice.metadataapi.util.json.JSonHelper;

import com.google.gson.Gson;

public class Metadata {
	public static final String METADATA_TYPE_STREAM = "stream";
	public static final String METADATA_TYPE_DATASET = "dataset";

	private String name; // cerco logica usata per lo store
	private String code; // codice da usare per il dettaglio
	private String version;
	private String description; // sempre dallo store se stream-> nome stream
	private String type;
	private String domain;
	private String requestorname;
	private String requestorsurname;
	private String requestoremail;
	private String tenantCode;
	private String tenantName;
	private String[] tagCodes;
	private String[] tags;
	private String icon;
	private String visibility;
	private String[] sharedtenants; // FIXME non mettere
	private Boolean isopendata;
	private String author;
	private String language;
	private Date datalastupdate; // FIXME non mettere
	private String externalreference;
	private Boolean ispublished; // FIXME non mettere
	private String license;
	private String disclaimer;
	private String copyright;

	private Stream stream;
	private Dataset dataset;
	private Opendata opendata;

	public Metadata() {
	}

	public String toJson() {
		Gson gson = JSonHelper.getInstance();
		return gson.toJson(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRequestorname() {
		return requestorname;
	}

	public void setRequestorname(String requestorname) {
		this.requestorname = requestorname;
	}

	public String getRequestorsurname() {
		return requestorsurname;
	}

	public void setRequestorsurname(String requestorsurname) {
		this.requestorsurname = requestorsurname;
	}

	public String getRequestoremail() {
		return requestoremail;
	}

	public void setRequestoremail(String requestoremail) {
		this.requestoremail = requestoremail;
	}

	public String[] getTagCodes() {
		return tagCodes;
	}

	public void setTagCodes(String[] tagCodes) {
		this.tagCodes = tagCodes;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String[] getSharedtenants() {
		return sharedtenants;
	}

	public void setSharedtenants(String[] sharedtenants) {
		this.sharedtenants = sharedtenants;
	}

	public Boolean getIsopendata() {
		return isopendata;
	}

	public void setIsopendata(Boolean isopendata) {
		this.isopendata = isopendata;
	}

	public String getAuthor() {
		return author;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getDatalastupdate() {
		return datalastupdate;
	}

	public void setDatalastupdate(Date datalastupdate) {
		this.datalastupdate = datalastupdate;
	}

	public String getExternalreference() {
		return externalreference;
	}

	public void setExternalreference(String externalreference) {
		this.externalreference = externalreference;
	}

	public Boolean getIspublished() {
		return ispublished;
	}

	public void setIspublished(Boolean ispublished) {
		this.ispublished = ispublished;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
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

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public Opendata getOpendata() {
		return opendata;
	}

	public void setOpendata(Opendata opendata) {
		this.opendata = opendata;
	}

	public static String createCodeFromStream(String tenantCode, String smartobjectCode, String streamCode) {
		return tenantCode + "." + smartobjectCode + "_" + streamCode;
	}

	public static Metadata createFromStoreSearchItem(StoreMetadataItem item, String lang) {

		Metadata metadata = new Metadata();
		metadata.setCode(item.getName().replaceAll("_odata", ""));
		metadata.setVersion(item.getVersion());
		metadata.setName(item.getDescription());
		metadata.setDescription(item.getExtraApiDescription());
		metadata.setDomain(I18nDelegate.translate(item.getExtraDomain(), lang));
		metadata.setVisibility(item.getVisibility());
		metadata.setLicense(item.getExtraLicence());
		metadata.setDisclaimer(item.getExtraDisclaimer());
		metadata.setCopyright(item.getExtraCopyright());
		metadata.setTenantCode(item.getExtraCodiceTenant());
		metadata.setTenantName(item.getExtraNomeTenant());

		if (item.getTags() != null) {
			metadata.setTagCodes(item.getTags().split("\\s*,\\s*"));
			metadata.setTags(I18nDelegate.translateMulti(metadata.getTagCodes(), lang));
		}
		if (item.getName().endsWith("_odata")) {
			metadata.setType(METADATA_TYPE_DATASET);
			metadata.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + item.getExtraCodiceTenant() + "/" + metadata.getCode());
		} else if (item.getName().endsWith("_stream")) {
			metadata.setType(METADATA_TYPE_STREAM);
			metadata.setCode(item.getName().substring(0, item.getName().length() - 7));
			metadata.setName(item.getExtraCodiceStream() + " " + item.getExtraVirtualEntityCode());
			metadata.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + item.getExtraCodiceTenant() + "/"
					+ item.getExtraVirtualEntityCode() + "/" + item.getExtraCodiceStream());
		}

		if (!Util.isEmpty(item.getExtraVirtualEntityCode())) {
			Smartobject smartobject = new Smartobject();
			smartobject.setCode(item.getExtraVirtualEntityCode());
			smartobject.setName(item.getExtraVirtualEntityName());
			smartobject.setDescription(item.getExtraVirtualEntityDescription());

			Stream stream = metadata.getStream() == null ? new Stream() : metadata.getStream();
			stream.setSmartobject(smartobject);
			metadata.setStream(stream);
		}

		// private String provider;
		// private String rates;
		// private String endpoint;
		// private String thumbnailurl;
		// private String visibleRoles;
		// private String docName;
		// private String docSummary;
		// private String docSourceURL;
		// private String docFilePath;
		// private String extraApi;

		// private String extraVirtualEntityName;
		// private String extraVirtualEntityDescription;
		// private String extraVirtualEntityCode;
		// private String extraApiDescription;
		// private String extraDomain;
		// private String tags;

		return metadata;
	}

	public static Metadata createFromStoreDocStream(StoreDoc doc, String lang) {
		Metadata metadata = new Metadata();
		DocStreamContent content = DocStreamContent.fromJson(doc.getContent());

		if (content.getStreams() != null && content.getStreams().getStream() != null) {

			metadata.setType(METADATA_TYPE_STREAM);
			org.csi.yucca.dataservice.metadataapi.model.store.output.doc.Stream docStream = content.getStreams().getStream();

			metadata.setCode(Metadata.createCodeFromStream(docStream.getCodiceTenant(), docStream.getCodiceVirtualEntity(), docStream.getCodiceStream()));
			metadata.setVersion("" + docStream.getDeploymentVersion());
			metadata.setName(docStream.getNomeStream() + " - " + docStream.getVirtualEntityName());
			metadata.setDescription(docStream.getNomeStream() + " - " + docStream.getVirtualEntityName() + " - " + docStream.getVirtualEntityDescription());

			metadata.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + docStream.getCodiceTenant() + "/"
					+ docStream.getCodiceVirtualEntity() + "/" + docStream.getCodiceStream());

			metadata.setDomain(I18nDelegate.translate(docStream.getDomainStream(), lang));
			metadata.setVisibility(docStream.getVisibility());
			metadata.setLicense(docStream.getLicence());
			metadata.setDisclaimer(docStream.getDisclaimer());
			metadata.setCopyright(docStream.getCopyright());
			metadata.setTenantCode(docStream.getCodiceTenant());
			metadata.setTenantName(docStream.getNomeTenant());

			if (docStream.getStreamTags() != null && docStream.getStreamTags().getTag() != null && docStream.getStreamTags().getTag().size() > 0) {
				String[] tagCodes = new String[docStream.getStreamTags().getTag().size()];
				int counter = 0;
				for (Tag tagCode : docStream.getStreamTags().getTag()) {
					tagCodes[counter] = tagCode.getTagCode();
					counter++;
				}
				metadata.setTagCodes(tagCodes);
				metadata.setTags(I18nDelegate.translateMulti(metadata.getTagCodes(), lang));
			}

			Smartobject smartobject = new Smartobject();
			smartobject.setCode(docStream.getCodiceVirtualEntity());
			smartobject.setName(docStream.getVirtualEntityName());
			smartobject.setDescription(docStream.getVirtualEntityDescription());
			smartobject.setCategory(docStream.getCategoriaVirtualEntity());
			smartobject.setType(docStream.getTipoVirtualEntity());
			if (docStream.getTipoVirtualEntity().equals(Smartobject.SMARTOBJECT_TYPE_TWITTER)) {
				smartobject.setTwtCount(docStream.getTwtCount());
				smartobject.setTwtGeolocLat(docStream.getTwtGeolocLat());
				smartobject.setTwtGeolocLon(docStream.getTwtGeolocLon());
				smartobject.setTwtGeolocRadius(docStream.getTwtGeolocRadius());
				smartobject.setTwtRatePercentage(docStream.getTwtRatePercentage());
				smartobject.setTwtMaxStreams(docStream.getTwtMaxStreamsOfVE());
				smartobject.setTwtQuery(docStream.getTwtQuery());
				smartobject.setTwtLang(docStream.getTwtLang());
			}

			if (docStream.getVirtualEntityPositions() != null && docStream.getVirtualEntityPositions().getPosition() != null
					&& docStream.getVirtualEntityPositions().getPosition().size() > 0) {
				Position position = docStream.getVirtualEntityPositions().getPosition().get(0);
				smartobject.setAltitude(position.getElevation());
				smartobject.setBuilding(position.getBuilding());
				smartobject.setFloor(Util.nvlt(position.getFloor()));
				smartobject.setLatitude(position.getLat());
				smartobject.setLongitude(position.getLon());
				smartobject.setRoom(Util.nvlt(position.getRoom()));
			}
			Stream stream = new Stream();
			stream.setCode(docStream.getCodiceStream());
			stream.setName(docStream.getNomeStream());
			stream.setFps(docStream.getFps());
			stream.setSavedata(docStream.getSaveData());
			stream.setSmartobject(smartobject);
			
			if (docStream.getComponenti() != null && docStream.getComponenti().getElement() != null && docStream.getComponenti().getElement().size() > 0) {

				StreamComponent[] components = new StreamComponent[docStream.getComponenti().getElement().size()];
				int counter = 0;
				for (Element element : docStream.getComponenti().getElement()) {
					StreamComponent component  = new StreamComponent();
					component.setDatatype(element.getDataType());
					component.setMeasureunit(element.getMeasureUnit());
					component.setName(element.getNome());
					component.setPhenomenon(element.getPhenomenon());
					component.setTolerance(element.getTolerance());
					components[counter]  = component;
					counter++;
				}
				stream.setComponents(components);
			}
			metadata.setStream(stream);

			if (docStream.getSaveData()) {
				Dataset dataset = new Dataset();
				
				String datasetType = Dataset.DATASET_TYPE_STREAM;
				if(smartobject.getType().equals(Smartobject.SMARTOBJECT_TYPE_TWITTER))
					datasetType = Dataset.DATASET_TYPE_SOCIAL;
				
				dataset.setDatasetType(datasetType);
				
				if (docStream.getComponenti() != null && docStream.getComponenti().getElement() != null && docStream.getComponenti().getElement().size() > 0) {
					DatasetColumn[] columns = new DatasetColumn[docStream.getComponenti().getElement().size()];

					int counter = 0;
					for (Element component : docStream.getComponenti().getElement()) {
						DatasetColumn column = new DatasetColumn();
						column.setAlias(component.getPhenomenon());
						column.setName(component.getNome());
						column.setIskey(false);
						column.setDatatype(component.getDataType());
						column.setMeasureunit(component.getMeasureUnit());
						columns[counter] = column;
						counter++;
					}
					dataset.setColumns(columns);
				}
				metadata.setDataset(dataset);
			}
		}

		return metadata;
	}

	public static Metadata createFromStoreDocDataset(StoreDoc doc, String lang) {
		Metadata metadata = new Metadata();
		DocDatasetContent content = DocDatasetContent.fromJson(doc.getContent());

		metadata.setType(METADATA_TYPE_DATASET);

		metadata.setCode(content.getDatasetCode());
		metadata.setVersion("" + content.getDatasetVersion());
		ConfigData configData = content.getConfigData();
		Info info = content.getInfo();
		metadata.setName(info.getDatasetName());
		metadata.setDescription(info.getDescription());

		metadata.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + configData.getTenantCode() + "/" + content.getDatasetCode());

		metadata.setDomain(I18nDelegate.translate(info.getDataDomain(), lang));
		metadata.setVisibility(info.getVisibility());
		metadata.setLicense(info.getLicense());
		metadata.setDisclaimer(info.getDisclaimer());
		metadata.setCopyright(info.getCopyright());
		metadata.setTenantCode(configData.getTenantCode());
		// metadata.setTenantName(docStream.getNomeTenant());//FIXME non viene
		// restituito?

		if (info.getTags() != null && info.getTags().size() > 0) {
			String[] tagCodes = new String[info.getTags().size()];
			int counter = 0;
			for (Tag tagCode : info.getTags()) {
				tagCodes[counter] = tagCode.getTagCode();
				counter++;
			}
			metadata.setTagCodes(tagCodes);
			metadata.setTags(I18nDelegate.translateMulti(metadata.getTagCodes(), lang));
		}

		Dataset dataset = new Dataset();
		dataset.setDatasetType(configData.getSubtype());
		dataset.setCode(content.getDatasetCode());
		DatasetColumn[] columns = null;
		if (info.getFields() != null && info.getFields().size() > 0) {
			List<Field> fields = info.getFields();
			columns = new DatasetColumn[fields.size()];
			int counter = 0;
			for (Field field : fields) {
				DatasetColumn column = new DatasetColumn();
				column.setAlias(field.getFieldAlias());
				column.setDatatype(field.getDataType());
				column.setDateformat(field.getDataType());
				column.setIskey(field.getIsKey());
				// column.setMeasureunit(field.getMeasureunit()); //FIXME non
				// viene restituito?
				column.setName(field.getFieldName());

				columns[counter] = column;
				counter++;
			}
		}
		dataset.setColumns(columns);
		metadata.setDataset(dataset);
		if (content.getOpendata() != null) {
			metadata.setIsopendata(content.getOpendata().getIsOpendata());

			Opendata opendata = new Opendata();
			opendata.setAuthor(content.getOpendata().getAuthor());
			opendata.setDataUpdateDate(content.getOpendata().getDataUpdateDate());
			opendata.setLanguage(content.getOpendata().getLanguage());
			opendata.setMetadaUpdateDate(content.getOpendata().getMetadaUpdateDate());

			metadata.setOpendata(opendata);
		}
		return metadata;
	}
}
