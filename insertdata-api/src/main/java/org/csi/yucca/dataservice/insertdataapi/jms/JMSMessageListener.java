package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
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
				try {
					JMSMessageListener.streamService.dataInsert(txtMessage.getText(), codTenant, message.getJMSMessageID(), "", "");
				} catch (InsertApiBaseException e) {
					log.warn("[JMSMessageListener::onMessage]  Invalid message for JMS ["+e.getErrorCode()+"]: "+e.getErrorName());
				} catch (InsertApiRuntimeException e) {
					log.error("[JMSMessageListener::onMessage]  System error for JMS",e);
					throw e;
				}
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
