package org.csi.yucca.dataservice.insertdataapi.jms;

import java.util.Iterator;
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
import org.apache.activemq.RedeliveryPolicy;
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
	private Map<String, MessageConsumer> consumers = new ConcurrentHashMap<String, MessageConsumer>();

	
	
	public void run() {

		try {
			log.info("[JMSConsumerMainThread::run] Starting connection...");
			
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					SDPInsertApiConfig.getInstance().getJMSUrl());
			connectionFactory.setMaxThreadPoolSize(2000);
			RedeliveryPolicy policy = new RedeliveryPolicy();
			policy.setInitialRedeliveryDelay(1000);
			policy.setBackOffMultiplier(3);
			policy.setUseExponentialBackOff(true);
			policy.setMaximumRedeliveryDelay(15*60*1000);
			policy.setMaximumRedeliveries(24*4);
			
			connectionFactory.setRedeliveryPolicy(policy);
			connectionFactory.setUserName(SDPInsertApiConfig.getInstance().getJMSUsername());
			connectionFactory.setPassword(SDPInsertApiConfig.getInstance().getJMSPassword());

			// Create a Connection
			connection = connectionFactory.createConnection();
			connection.start();
			connection.setExceptionListener(this);


			while (true)
			{
				log.info("[JMSConsumerMainThread::run] Get tenant list and update sessions...");
				SDPInsertApiMongoDataAccess mongoAccess=new SDPInsertApiMongoDataAccess();
				Set<String> tenants;
				try {
					tenants = mongoAccess.getTenantList();
					Iterator<String> iter = sessions.keySet().iterator();
					while (iter.hasNext())
					{
						String oldTenant = (String) iter.next();
						if (!tenants.contains(oldTenant))
						{
							consumers.get(oldTenant).close();
							sessions.get(oldTenant).close();
							consumers.remove(oldTenant);
							sessions.remove(oldTenant);
							log.info("[JMSConsumerMainThread::run] Disconnected for tenant:"+ oldTenant);
						}
					}
					iter = tenants.iterator();
					while (iter.hasNext()) {
						String newTenant = (String) iter.next();
						if (!sessions.containsKey(newTenant)) // new tenant!
						{
							Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
							Destination destination = session.createQueue(VIRTUAL_QUEUE_CONSUMER_INSERTAPI_INPUT+"."+newTenant+".*");
							log.info("[JMSConsumerMainThread::run] Connected to queue:"+ destination.toString());
							MessageConsumer consumer = session.createConsumer(destination);
							consumer.setMessageListener(new JMSMessageListener(newTenant));
							sessions.put(newTenant, session);
							consumers.put(newTenant, consumer);
						}
					}
				} catch (MongoAccessException e) {
					log.error("[JMSConsumerMainThread::run] Error reading tenant list... continue with old list", e);
				}
				Thread.sleep(5*60*1000);
			}
			
		} 
		catch (InterruptedException ie) {
			log.warn("[JMSConsumerMainThread::run] JMSConsumerMainThread shutdown");
			closing();
		} catch (Exception e) {
			log.error("[JMSConsumerMainThread::run] Error on Starting connection..."+e.getMessage(), e);
			
		}
	}
	
	private void closing()
	{
		log.info("[JMSConsumerMainThread::run] Closing connection...");
		try {
			Iterator<MessageConsumer> iter =consumers.values().iterator(); 
			while (iter.hasNext()) {
				iter.next().close();
			}
			
			Iterator<Session> iter2 =sessions.values().iterator(); 
			while (iter2.hasNext()) {
				iter2.next().close();
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
