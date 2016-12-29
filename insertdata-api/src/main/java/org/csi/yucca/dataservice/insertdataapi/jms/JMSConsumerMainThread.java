package org.csi.yucca.dataservice.insertdataapi.jms;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.SetUtils.SetView;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.exception.MongoAccessException;
import org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertApiMongoDataAccess;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class JMSConsumerMainThread implements Runnable, ExceptionListener {
	public static final String VIRTUAL_QUEUE_CONSUMER_INSERTAPI_INPUT = "VirtualQueueConsumer.insertapi.input";

	private static final Log log=LogFactory.getLog("org.csi.yucca.datainsert");

	private Connection connection;
	private Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	public void run() {

		try {
			log.info("[JMSConsumerMainThread::run] Starting connection...");
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					SDPInsertApiConfig.getInstance().getJMSUrl());
			connectionFactory.setMaxThreadPoolSize(2000);
			connectionFactory.setUserName(SDPInsertApiConfig.getInstance().getJMSUsername());
			connectionFactory.setPassword(SDPInsertApiConfig.getInstance().getJMSPassword());

			// Create a Connection
			connection = connectionFactory.createConnection();
			connection.start();
			connection.setExceptionListener(this);

			log.info("[JMSConsumerMainThread::run] Before Get tenant list and update sessions...");

			while (true)
			{
				log.info("[JMSConsumerMainThread::run] Get tenant list and update sessions...");
				SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
				Set<String> tenants;
				try {
					tenants = mongoAccess.getTenantList();
					while (sessions.keySet().iterator().hasNext())
					{
						String oldTenant = (String) sessions.keySet().iterator().next();
						if (!tenants.contains(oldTenant))
						{
							sessions.get(oldTenant).close();
							sessions.remove(oldTenant);
						}
					}
					while (tenants.iterator().hasNext()) {
						String newTenant = (String) tenants.iterator().next();
						if (!sessions.containsKey(newTenant)) // new tenant!
						{
							Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
							Destination destination = session.createQueue(VIRTUAL_QUEUE_CONSUMER_INSERTAPI_INPUT+"."+newTenant+".>");
							MessageConsumer consumer = session.createConsumer(destination);
							consumer.setMessageListener(new JMSMessageListener(newTenant));
						}
					}
				} catch (MongoAccessException e) {
					log.error("[JMSConsumerMainThread::run] Error reading tenant list... continue with old list", e);
				}
				Thread.sleep(10*1000);
			}
			
		} catch (Exception e) {
			log.error("[JMSConsumerMainThread::run] Error on Starting connection..."+e.getMessage(), e);

		}
	}
	
	public void closing()
	{
		log.info("[JMSConsumerMainThread::run] Closing connection...");
		try {
			while (sessions.values().iterator().hasNext()) {
				Session type = (Session) sessions.values().iterator().next();
				type.close();
			}
			
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
