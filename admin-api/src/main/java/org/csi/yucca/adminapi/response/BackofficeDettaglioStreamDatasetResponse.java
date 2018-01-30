package org.csi.yucca.adminapi.response;

import java.util.ArrayList;
import java.util.List;

import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.model.InternalDettaglioStream;
import org.csi.yucca.adminapi.model.join.DettaglioSmartobject;
import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class BackofficeDettaglioStreamDatasetResponse extends DataSourceResponse {

	private List<ComponentResponse> components = new ArrayList<ComponentResponse>();
	private List<TenantResponse> sharingTenants = new ArrayList<TenantResponse>();
	private OpenDataResponse opendata;
	private LicenseResponse license;
	private DcatResponse dcat;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private StreamDettaglioResponse stream;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private BackofficeDatasetDettaglioResponse dataset;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private BackofficeDatasetDettaglioResponse binarydataset;
	
	
	public BackofficeDettaglioStreamDatasetResponse() {
		super();
	}

	
	@JsonIgnore
	private void setAllParameter(DettaglioStream dettaglioStream)throws Exception{
		Util.addSharingTenants(dettaglioStream.getSharingTenant(), this.sharingTenants);
		Util.addComponents(dettaglioStream.getComponents(), this.components);
		this.opendata = new OpenDataResponse(dettaglioStream);
		this.license = new LicenseResponse(dettaglioStream.getLicense());
		this.dcat = new DcatResponse(dettaglioStream.getDcat());
	}
	
	@JsonIgnore
	private void setAllParameter(DettaglioDataset dettaglioDataset)throws Exception{
		Util.addSharingTenants(dettaglioDataset.getSharingTenant(), this.sharingTenants);
		Util.addComponents(dettaglioDataset.getComponents(), this.components);
		this.opendata = new OpenDataResponse(dettaglioDataset);
		this.license = new LicenseResponse(dettaglioDataset.getLicense());
		this.dcat = new DcatResponse(dettaglioDataset.getDcat());
	}
	
	public BackofficeDettaglioStreamDatasetResponse(
			DettaglioStream dettaglioStream, 
			DettaglioDataset dettaglioDataset, 
			DettaglioSmartobject dettaglioSmartobject, 
			List<InternalDettaglioStream> listInternalStream) throws Exception {
		
		super(dettaglioStream);
		setAllParameter(dettaglioStream);
		
		this.stream = new StreamDettaglioResponse(dettaglioStream, dettaglioSmartobject, listInternalStream);
		this.dataset = new BackofficeDatasetDettaglioResponse(dettaglioDataset);
	}
	
	public BackofficeDettaglioStreamDatasetResponse(DettaglioStream dettaglioStream, DettaglioSmartobject dettaglioSmartobject, 
			List<InternalDettaglioStream> listInternalStream) throws Exception {
		super(dettaglioStream);
		setAllParameter(dettaglioStream);
		this.stream = new StreamDettaglioResponse(dettaglioStream, dettaglioSmartobject, listInternalStream);
	}
	
	public BackofficeDettaglioStreamDatasetResponse(DettaglioDataset dettaglioDataset, 
													DettaglioDataset binaryDataset) throws Exception {
		super(dettaglioDataset);
		setAllParameter(dettaglioDataset);
		this.dataset = new BackofficeDatasetDettaglioResponse(dettaglioDataset);
		if (binaryDataset!=null)
			this.binarydataset = new BackofficeDatasetDettaglioResponse(binaryDataset);
	}

	public List<ComponentResponse> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentResponse> components) {
		this.components = components;
	}

	public List<TenantResponse> getSharingTenanst() {
		return sharingTenants;
	}

	public void setSharingTenants(List<TenantResponse> sharingTenants) {
		this.sharingTenants = sharingTenants;
	}

	public OpenDataResponse getOpendata() {
		return opendata;
	}

	public void setOpnedata(OpenDataResponse opendata) {
		this.opendata = opendata;
	}

	public LicenseResponse getLicense() {
		return license;
	}

	public void setLicense(LicenseResponse license) {
		this.license = license;
	}

	public DcatResponse getDcat() {
		return dcat;
	}

	public void setDcat(DcatResponse dcat) {
		this.dcat = dcat;
	}

	public StreamDettaglioResponse getStream() {
		return stream;
	}

	public void setStream(StreamDettaglioResponse stream) {
		this.stream = stream;
	}

	public BackofficeDatasetDettaglioResponse getDataset() {
		return dataset;
	}

	public void setDataset(BackofficeDatasetDettaglioResponse dataset) {
		this.dataset = dataset;
	}


	public BackofficeDatasetDettaglioResponse getBinarydataset() {
		return binarydataset;
	}


	public void setBinarydataset(BackofficeDatasetDettaglioResponse binarydataset) {
		this.binarydataset = binarydataset;
	}

}
