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
		log.info("Class JMSDestination=["+message.getJMSDestination().getClass().getCanonicalName()+"]");
		if (message.getJMSDestination() instanceof ActiveMQDestination)
		{
			ActiveMQDestination mqDest = (ActiveMQDestination) message.getJMSDestination();
			log.info("ActiveMQDestination physicalName=["+mqDest.getPhysicalName()+"]");
		}
		
		JMSMessageListener.streamService.dataInsert(message.toString(), codTenant, "", "", "");
		}
		catch (JMSException e)
		{
			log.error("Error", e);
		}
		
	}

}
