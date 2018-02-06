package org.csi.yucca.adminapi.service.impl;

import javax.mail.internet.MimeMessage;

import org.csi.yucca.adminapi.model.DettaglioStream;
import org.csi.yucca.adminapi.request.PostTenantSocialRequest;
import org.csi.yucca.adminapi.service.MailService;
import org.csi.yucca.adminapi.util.EmailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@PropertySources({
    @PropertySource("classpath:email-service.properties")
})
public class MailServiceImpl implements MailService {
	
	@Value("${req.action.mail-to}")
	private String requestActionMailTo;
	
	@Value("${req.action.mail-from}")
	private String requestActionMailfrom;
	
	@Value("${req.action.mail-name}")
	private String requestActionMailName;
	
	@Autowired
	JavaMailSender mailSender;
	
	
	@Override
	public void sendEmail(EmailInfo emailInfo) {

		MimeMessagePreparator preparator = getMessagePreparator(emailInfo);

		try {
			mailSender.send(preparator);
		} 
		catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
		
	}
	
	/**
	 * 
	 * @param emailInfo
	 * @return
	 */
	private MimeMessagePreparator getMessagePreparator(final EmailInfo emailInfo) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
//				MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");             
				MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, "UTF-8");             
                mimeMsgHelperObj.setTo(emailInfo.getEmail());
                mimeMsgHelperObj.setFrom(emailInfo.getFrom());               
                mimeMsgHelperObj.setText(emailInfo.getMessage());
                mimeMsgHelperObj.setSubject(emailInfo.getSubject());
			}
			
		};
		
		return preparator;
	}
	
	@Override
	public void sendTenantRequestInstallationEmail(final PostTenantSocialRequest tenantRequest){
		
		String emailMessage = buildTenantRequestInstallationEmailMessage(tenantRequest);
		
		String emailSubject = buildTenantRequestInstallationEmailSubject(tenantRequest);
		
		sendEmail(emailSubject, emailMessage );
		
	}
	
	@Override
	public void sendStreamRequestInstallationEmail(final DettaglioStream dettaglioStream){
		sendStreamRrequestActionEmail(dettaglioStream, "Stream installation request");
	}
	
	@Override
	public void sendStreamRequestUninstallationEmail(final DettaglioStream dettaglioStream){
		sendStreamRrequestActionEmail(dettaglioStream, "Stream uninstallation request");	
	}
	
	/**
	 * 
	 * @param subject
	 * @param message
	 */
	private void sendEmail(String subject, String message){
		sendEmail( EmailInfo.build(requestActionMailTo)
				.from(requestActionMailfrom)
				.message(message)
				.name(requestActionMailName)
				.subject(subject));
	}
	
	/**
	 * 
	 * @param dettaglioStream
	 * @param actionDescription
	 */
	private void sendStreamRrequestActionEmail(final DettaglioStream dettaglioStream, String actionDescription){
		
		String emailMessage = buildStreamRrequestActionEmailMessage(dettaglioStream, actionDescription);
		
		String emailSubject = buildStreamRrequestActionEmailSubject(dettaglioStream, actionDescription);		
		
		sendEmail(emailSubject, emailMessage );
	}

	/**
	 * 
	 * @param dettaglioStream
	 * @param actionDescription
	 * @return
	 */
	private String buildStreamRrequestActionEmailMessage(DettaglioStream dettaglioStream, String actionDescription){
		StringBuilder emailMessage = new StringBuilder()
				.append(actionDescription).append(": ").append("\n\n")
		        .append("Stream Code: ").append(dettaglioStream.getStreamcode()).append("\n")
		        .append("Smart Object Code: ").append(dettaglioStream.getSmartObjectCode()).append("\n")
		        .append("Version: ").append(dettaglioStream.getDatasourceversion()).append("\n")
		        .append("Tenant Code: ").append(dettaglioStream.getTenantCode()).append("\n");
		
		
		return emailMessage.toString();
	}

	/**
	 * 
	 * @param dettaglioStream
	 * @param actionDescription
	 * @return
	 */
	private String buildStreamRrequestActionEmailSubject(DettaglioStream dettaglioStream, String actionDescription){
		StringBuilder emailSubject = new StringBuilder()
				.append(actionDescription).append(": ")
				.append("stream code").append(" [").append(dettaglioStream.getStreamcode())       .append("], ")
				.append("tenant code").append(" [").append(dettaglioStream.getTenantCode())       .append("], ")
				.append("version")    .append(" [").append(dettaglioStream.getDatasourceversion()).append("]");		
		return emailSubject.toString();
	}
	
	/**
	 * 
	 * @param tenantRequest
	 * @return
	 */
	private String buildTenantRequestInstallationEmailMessage(PostTenantSocialRequest tenantRequest){
		StringBuilder emailMessage = new StringBuilder()
				.append("Tenant installation request: ").append("\n\n")
		        .append("Tenant Code: " + tenantRequest.getTenantcode()).append("\n")
		        .append("Username: " + tenantRequest.getUsername()).append("\n")
		        .append("Name: " + tenantRequest.getUserfirstname()).append("\n")
		        .append("Surname: " + tenantRequest.getUserlastname()).append("\n")
		        .append("Email: " + tenantRequest.getUseremail()).append("\n");
		return emailMessage.toString();
	}
	
	/**
	 * 
	 * @param tenantRequest
	 * @return
	 */
	private String buildTenantRequestInstallationEmailSubject(PostTenantSocialRequest tenantRequest){
		StringBuilder emailSubject = new StringBuilder()
				.append("Tenant installation request: ")
				.append("tenant code").append(" [").append(tenantRequest.getTenantcode()).append("]");		
		
		return emailSubject.toString();
	}
	
}