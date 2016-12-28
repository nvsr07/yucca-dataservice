package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class JMSConsumerMainThread implements Runnable, ExceptionListener {
	
	private Connection connection;
	private Session session;
	private MessageConsumer consumer;

	public void run() {

		try {
			// Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					SDPInsertApiConfig.getInstance().getJMSUrl());
			connectionFactory.setMaxThreadPoolSize(2000);
			connectionFactory.setUserName(SDPInsertApiConfig.getInstance()
					.getJMSUsername());
			connectionFactory.setPassword(SDPInsertApiConfig.getInstance()
					.getJMSPassword());

			// Create a Connection
			connection = connectionFactory.createConnection();
			connection.start();
			connection.setExceptionListener(this);

			// Create a Session
			session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);

			// Create the destination (Topic or Queue)
			Destination destination = session.createQueue("VirtualQueueConsumer.insertapi.input.>");

			// Create a MessageConsumer from the Session to the Topic or Queue
			MessageConsumer consumer = session.createConsumer(destination);
			
			consumer.setMessageListener(new JMSMessageListener());
			// Wait for a message
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}
	
	public void closing()
	{
		try {
			consumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void onException(JMSException ex) {
		System.out.println("JMS Exception occured. Shutting down client.");
	}

}
