package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class JMSMessageListener implements MessageListener {

	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text;
			try {
				text = textMessage.getText();
	            System.out.println("Received: " + text);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            System.out.println("Received: " + message);
        }

	}

}
