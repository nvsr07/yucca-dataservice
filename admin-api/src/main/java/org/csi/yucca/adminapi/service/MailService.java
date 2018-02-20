package org.csi.yucca.adminapi.service;

import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.request.PostTenantSocialRequest;
import org.csi.yucca.adminapi.request.TenantRequest;
import org.csi.yucca.adminapi.util.EmailInfo;

public interface MailService {
	
	void sendEmail(final EmailInfo emailInfo);

	void sendStreamRequestInstallationEmail(final DettaglioStream dettaglioStream);

	void sendStreamRequestUninstallationEmail(final DettaglioStream dettaglioStream);
	
	void sendTenantRequestInstallationEmail(final PostTenantSocialRequest tenantRequest);
	
	void sendTenantCreationEmail(final TenantRequest tenantRequest);
	
}
