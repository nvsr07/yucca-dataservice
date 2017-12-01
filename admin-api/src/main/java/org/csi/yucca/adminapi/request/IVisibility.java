package org.csi.yucca.adminapi.request;

import java.util.List;

public interface IVisibility {
	String getVisibility();
	LicenseRequest getLicense();
	OpenDataRequest getOpenData(); 
	List<SharingTenantRequest> getSharingTenants(); 
	String getCopyright();
}
