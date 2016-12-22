package org.csi.yucca.dataservice.metadataapi.model.dcat;


public class MetadataDCAT {
	public static final String METADATA_TYPE_STREAM = "stream";
	public static final String METADATA_TYPE_DATASET = "dataset";

	private AgentDCAT agent; 
	private CatalogDCAT catalog;
	private DistributionDCAT distribution;
	private String identifier; //dct:identifier
	private String name; //foaf:name
	private String versionInfo; //owl:versionInfo
	private String Frequency; //dct:Frequency
	private VcardDCAT vcard;
	
	public AgentDCAT getAgent() {
		return agent;
	}
	public void setAgent(AgentDCAT agent) {
		this.agent = agent;
	}
	public CatalogDCAT getCatalog() {
		return catalog;
	}
	public void setCatalog(CatalogDCAT catalog) {
		this.catalog = catalog;
	}
	public DistributionDCAT getDistribution() {
		return distribution;
	}
	public void setDistribution(DistributionDCAT distribution) {
		this.distribution = distribution;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersionInfo() {
		return versionInfo;
	}
	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}
	public String getFrequency() {
		return Frequency;
	}
	public void setFrequency(String frequency) {
		Frequency = frequency;
	}
	public VcardDCAT getVcard() {
		return vcard;
	}
	public void setVcard(VcardDCAT vcard) {
		this.vcard = vcard;
	}
	
/*
	public static String createCodeFromStream(String tenantCode, String smartobjectCode, String streamCode) {
		return tenantCode + "." + smartobjectCode + "_" + streamCode;
	}

	public static Metadata createFromStoreSearchItem(StoreMetadataItem item, String lang) {

		Metadata metadata = new Metadata();
		// metadata.setCode(item.getName().replaceAll("_odata", ""));
		metadata.setCode(cleadMainCode(item.getName()));

		// String detailUrl = Config.getInstance().getStoreBaseUrl() +
		// "site/blocks/secure/detail.jag?action=getInlineContent&provider=admin&apiName="
		// + item.getName() + "&version=" + item.getVersion()
		// + "&docName=" + item.getName();

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
		metadata.setLatitude(item.getExtraLatitude());
		metadata.setLongitude(item.getExtraLongitude());

		if (item.getTags() != null) {
			metadata.setTagCodes(item.getTags().split("\\s*,\\s*"));
			metadata.setTags(I18nDelegate.translateMulti(metadata.getTagCodes(), lang));
		}
		String detailUrl = Config.getInstance().getMetadataapiBaseUrl() + "detail/" + item.getExtraCodiceTenant() + "/";
		if (item.getName().endsWith("_odata")) {
			metadata.setType(METADATA_TYPE_DATASET);
			metadata.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + item.getExtraCodiceTenant() + "/" + metadata.getCode());
			Dataset dataset = new Dataset();

			String[] nameSplitted = item.getName().split("_");
			String datasetId = nameSplitted[nameSplitted.length - 2];
			dataset.setDatasetId(new Long(datasetId));
			metadata.setDataset(dataset);

			detailUrl += metadata.getCode();

		} else if (item.getName().endsWith("_stream")) {
			metadata.setType(METADATA_TYPE_STREAM);
			metadata.setCode(item.getName().substring(0, item.getName().length() - 7));
			metadata.setName(item.getExtraCodiceStream() + " " + item.getExtraVirtualEntityCode());
			metadata.setIcon(Config.getInstance().getMetadataapiBaseUrl() + "resource/icon/" + item.getExtraCodiceTenant() + "/"
					+ item.getExtraVirtualEntityCode() + "/" + item.getExtraCodiceStream());

			detailUrl += item.getExtraVirtualEntityCode() + "/" + item.getExtraCodiceStream();

		}
		metadata.setDetailUrl(detailUrl);

		if (!Util.isEmpty(item.getExtraVirtualEntityCode())) {
			Smartobject smartobject = new Smartobject();
			smartobject.setCode(item.getExtraVirtualEntityCode());
			smartobject.setName(item.getExtraVirtualEntityName());
			smartobject.setDescription(item.getExtraVirtualEntityDescription());
			smartobject.setLatitude(item.getExtraLatitude());
			smartobject.setLongitude(item.getExtraLongitude());
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

	private static String cleadMainCode(String mainCode) {
		if (mainCode.endsWith("_odata"))
			mainCode = mainCode.substring(0, mainCode.lastIndexOf("_odata"));
		else if (mainCode.endsWith("_stream"))
			mainCode = mainCode.substring(0, mainCode.lastIndexOf("_stream"));

		return mainCode;

	}

	public static Metadata createFromStoreDocStream(StoreDoc doc, String lang) {
		Metadata metadata = new Metadata();
		DocStreamContent content = DocStreamContent.fromJson(doc.getContent());

		if (content != null && content.getStreams() != null && content.getStreams().getStream() != null) {

			metadata.setType(METADATA_TYPE_STREAM);
			org.csi.yucca.dataservice.metadataapi.model.store.output.doc.Stream docStream = content.getStreams().getStream();

			// metadata.setCode(Metadata.createCodeFromStream(docStream.getCodiceTenant(),
			// docStream.getCodiceVirtualEntity(),
			// docStream.getCodiceStream()));
			metadata.setCode(cleadMainCode(doc.getApiName()));

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
					StreamComponent component = new StreamComponent();
					component.setDatatype(element.getDataType());
					component.setMeasureunit(element.getMeasureUnit());
					component.setName(element.getNome());
					component.setPhenomenon(element.getPhenomenon());
					component.setTolerance(element.getTolerance());
					components[counter] = component;
					counter++;
				}
				stream.setComponents(components);
			}
			metadata.setStream(stream);

			if (docStream.getSaveData()) {
				Dataset dataset = new Dataset();

				String datasetType = Dataset.DATASET_TYPE_STREAM;
				if (smartobject.getType().equals(Smartobject.SMARTOBJECT_TYPE_TWITTER))
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
			if (docStream.getOpendata() != null) {
				metadata.setIsopendata(docStream.getOpendata().getIsOpendata());

				Opendata opendata = new Opendata();
				opendata.setAuthor(docStream.getOpendata().getAuthor());
				opendata.setDataUpdateDate(docStream.getOpendata().getDataUpdateDate());
				opendata.setLanguage(docStream.getOpendata().getLanguage());
				opendata.setMetadaUpdateDate(docStream.getOpendata().getMetadaUpdateDate());

				metadata.setOpendata(opendata);
			}

		}

		return metadata;
	}

	public static Metadata createFromStoreDocDataset(StoreDoc doc, String lang) {
		Metadata metadata = new Metadata();
		DocDatasetContent content = DocDatasetContent.fromJson(doc.getContent());
		if (content != null) {
			metadata.setType(METADATA_TYPE_DATASET);
			metadata.setCode(cleadMainCode(doc.getApiName()));
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
			metadata.setRegistrationDate(content.getInfo().getRegistrationDate());
			// metadata.setTenantName(docStream.getNomeTenant());//FIXME non
			// viene
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
			dataset.setDatasetId(content.getIdDataset());
			dataset.setImportFileType(content.getInfo().getImportFileType());

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
					// column.setMeasureunit(field.getMeasureunit()); //FIXME
					// non
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
		}
		return metadata;
	}

	public String getCkanPackageId() {
		return "smartdatanet.it_" + getCode();
	}

	public static String getApiNameFromCkanPackageId(String packageId) {
		return packageId.substring(packageId.indexOf("_") + 1);

	}

	public String toCkan() {
		org.csi.yucca.dataservice.metadataapi.model.ckan.Dataset ckanDataset = new org.csi.yucca.dataservice.metadataapi.model.ckan.Dataset();
		ckanDataset.setId(getCkanPackageId());
		ckanDataset.setName(getCkanPackageId());
		ckanDataset.setTitle(getName());
		ckanDataset.setNotes(getDescription());
		ckanDataset.setVersion(getVersion());

		String metadataUrl = Config.getInstance().getUserportalBaseUrl() + "#/dataexplorer/dataset/" + getTenantCode() + "/" + getCode();

		ckanDataset.setUrl(metadataUrl);

		Resource resourceApiOdata = new Resource();
		resourceApiOdata.setDescription("Api Odata");
		resourceApiOdata.setFormat("ODATA");
		String exposedApiBaseUrl = Config.getInstance().getExposedApiBaseUrl();
		String apiOdataUrl = exposedApiBaseUrl + getCode();
		resourceApiOdata.setUrl(apiOdataUrl);
		ckanDataset.addResource(resourceApiOdata);

		Resource resourceDownload = new Resource();
		resourceDownload.setDescription("Csv download url");
		resourceDownload.setFormat("CSV");

		String downloadCsvUrl = exposedApiBaseUrl + getCode() + "/download/" + getDataset().getDatasetId() + "/";

		if (METADATA_TYPE_DATASET.equals(getType()) && Dataset.DATASET_TYPE_BULK.equals(getDataset().getDatasetType())) {
			downloadCsvUrl += "all";
		} else {
			downloadCsvUrl += "current";
		}

		resourceDownload.setUrl(downloadCsvUrl);
		ckanDataset.addResource(resourceDownload);
		ExtraV2 extras = new ExtraV2();
		if (getDomain() != null) {
			extras.setTopic(getDomain());
			extras.setHidden_field(getDomain());
		}

		if (getOpendata() != null) {
			ckanDataset.setAuthor(getOpendata().getAuthor());
			if (getOpendata().getMetadaUpdateDate() != null)
				ckanDataset.setMetadata_created(Util.formatDateCkan(getOpendata().getMetadaUpdateDate()));
			if (getOpendata().getMetadaCreateDate() != null)
				extras.setMetadata_created(Util.formatDateCkan(getOpendata().getMetadaCreateDate()));
			if (getOpendata().getMetadaUpdateDate() != null)
				extras.setMetadata_modified(Util.formatDateCkan(getOpendata().getMetadaUpdateDate()));
			if (getRegistrationDate() != null)
				extras.setPackage_created(Util.formatDateCkan(getRegistrationDate()));
			if (getOpendata().getDataUpdateDate() != null)
				extras.setPackage_modified(Util.formatDateCkan(new Date(getOpendata().getDataUpdateDate())));

		}

		ckanDataset.setLicense(getLicense());
		ckanDataset.setIsopen(getOpendata() != null && getOpendata().isOpendata());

		if (getTags() != null) {
			for (String tag : getTags()) {
				ckanDataset.addTag(tag);
			}
		}

		if (ckanDataset.getResources() != null) {
			List<String> resourcesList = new LinkedList<String>();
			for (Resource resource : ckanDataset.getResources()) {
				resourcesList.add(resource.createResourceV2());

			}
			Map<String, List<String>> extrasList = new HashMap<String, List<String>>();
			extrasList.put("resource", resourcesList);
			ckanDataset.setExtrasList(extrasList);
		}

		if (getDataset().getImportFileType() != null)
			extras.setPackage_type(getDataset().getImportFileType());
		ckanDataset.setExtras(extras);
		return ckanDataset.toJson();

	}
*/

}
