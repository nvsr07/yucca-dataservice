package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JMSMessageListener implements MessageListener {
	private static final Log log=LogFactory.getLog("org.csi.yucca.datainsert");
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text;
			try {
				text = textMessage.getText();
	            log.info("---->Received: " + text);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
        	log.info("---->Received: " + message);
        }

	}

}
