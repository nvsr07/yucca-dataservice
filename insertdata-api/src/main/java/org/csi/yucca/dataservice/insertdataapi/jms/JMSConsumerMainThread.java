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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class JMSConsumerMainThread implements Runnable, ExceptionListener {
	private static final Log log=LogFactory.getLog("org.csi.yucca.datainsert");

	private Connection connection;
	private Session session;
	private MessageConsumer consumer;

	public void run() {

		try {
			log.info("[JMSConsumerMainThread::run] Starting connection...");
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
			log.error("[JMSConsumerMainThread::run] Error on Starting connection..."+e.getMessage(), e);

		}
	}
	
	public void closing()
	{
		log.info("[JMSConsumerMainThread::run] Closing connection...");
		try {
			consumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			log.error("[JMSConsumerMainThread::run] Error on Closing connection..."+e.getMessage(), e);
		}
		log.info("[JMSConsumerMainThread::run] Closed");

	}

	public synchronized void onException(JMSException ex) {
		System.out.println("JMS Exception occured. Shutting down client.");
	}

}
