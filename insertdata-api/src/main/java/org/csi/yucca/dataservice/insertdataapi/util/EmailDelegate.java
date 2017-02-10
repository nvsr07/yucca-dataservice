package org.csi.yucca.dataservice.insertdataapi.util;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailDelegate {

	private String hostMailServer;
	private Properties properties;

	public EmailDelegate() {
		hostMailServer = SDPInsertApiConfig.getInstance().getMailServer();
		properties = System.getProperties();
		properties.setProperty("mail.smtp.host", hostMailServer);
	}

	public void sendEmail(String toAddress, String fromAddress, String subject, String body) {

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(Session.getDefaultInstance(properties));

			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
			message.setSubject(subject);
			message.setText(body);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully.... to "+ toAddress);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}