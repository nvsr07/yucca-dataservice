package org.csi.yucca.adminapi.request;

import org.csi.yucca.adminapi.request.OpenDataRequest;

public interface IDataSourceRequest {
	String getDisclaimer();
	String getName();
	Integer getIdSubdomain();
	String getVisibility();
	Boolean getUnpublished();
	String getRequestername();
	String getRequestersurname();
	String getRequestermail();
	Boolean getPrivacyacceptance();
	String getIcon();
	OpenDataRequest getOpenData();
}
