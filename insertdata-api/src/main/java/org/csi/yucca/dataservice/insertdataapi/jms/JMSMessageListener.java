package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.service.StreamService;

public class JMSMessageListener implements MessageListener {
	private static final Log log=LogFactory.getLog("org.csi.yucca.datainsert");
	
	private static StreamService streamService = new StreamService();
	private String codTenant;
	
	public JMSMessageListener(String codTenant){
		this.codTenant = codTenant;
	}
	
	public void onMessage(Message message) {
		
		
		try {
			if (message instanceof TextMessage)
			{
				TextMessage txtMessage = (TextMessage)message ;
				log.debug("[JMSMessageListener::onMessage]  JMSListener=["+codTenant+"] -> msg"+ txtMessage.getText());
				JMSMessageListener.streamService.dataInsert(txtMessage.getText(), codTenant, "", "", "");
			}
			else 
			{
				log.warn("[JMSMessageListener::onMessage]  No textMessage"+message);
			}
		} catch (JMSException e) {
			log.error("[JMSMessageListener::onMessage]  textMessage problem", e);
		}
		
		
		
	}

}
