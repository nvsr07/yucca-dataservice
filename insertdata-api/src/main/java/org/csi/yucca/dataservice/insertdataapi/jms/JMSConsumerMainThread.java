package org.csi.yucca.dataservice.insertdataapi.jms;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.exception.MongoAccessException;
import org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertApiMongoDataAccess;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class JMSConsumerMainThread implements Runnable, ExceptionListener {
	public static final String VIRTUAL_QUEUE_CONSUMER_INSERTAPI_INPUT = "VirtualQueueConsumer.insertapi.input";

	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	private Connection connectionInternal;
	private Connection connectionExternal;
	private Map<String, JMSTenant> jmsTenants = new ConcurrentHashMap<String, JMSTenant>();

	public void run() {

		try {
			log.info("[JMSConsumerMainThread::run] Starting connection...");

			// internal Connection
			ActiveMQConnectionFactory connectionFactoryInternal = createConnection(SDPInsertApiConfig.getInstance().getJMSMbInternalUrl(), SDPInsertApiConfig.getInstance()
					.getJMSMbExternalUsername(), SDPInsertApiConfig.getInstance().getJMSMbInternalPassword(), 2000, 1000L, 3., true, new Long(15 * 60 * 1000), 24 * 4);

			// Create a Connection internal
			connectionInternal = connectionFactoryInternal.createConnection();
			connectionInternal.start();
			connectionInternal.setExceptionListener(this);

			// external Connection
			ActiveMQConnectionFactory connectionFactoryExternal = createConnection(SDPInsertApiConfig.getInstance().getJMSMbExternalUrl(), SDPInsertApiConfig.getInstance()
					.getJMSMbExternalUsername(), SDPInsertApiConfig.getInstance().getJMSMbExternalPassword(), 2000);

			// Create a Connection internal
			connectionExternal = connectionFactoryExternal.createConnection();
			connectionExternal.start();
			connectionExternal.setExceptionListener(this);

			while (true) {
				log.info("[JMSConsumerMainThread::run] Get tenant list and update sessions...");
				SDPInsertApiMongoDataAccess mongoAccess = new SDPInsertApiMongoDataAccess();
				Set<String> tenants;
				try {
					tenants = mongoAccess.getTenantList();
					Iterator<String> iter = jmsTenants.keySet().iterator();
					while (iter.hasNext()) {
						String oldTenant = (String) iter.next();
						if (!tenants.contains(oldTenant)) {
							jmsTenants.get(oldTenant).closeAll();
							jmsTenants.remove(oldTenant);
							log.info("[JMSConsumerMainThread::run] Disconnected for tenant:" + oldTenant);
						}
					}
					iter = tenants.iterator();
					while (iter.hasNext()) {
						String newTenant = (String) iter.next();
						if (!jmsTenants.containsKey(newTenant)) // new tenant!
						{
							Session sessionProducer = connectionExternal.createSession(false, Session.AUTO_ACKNOWLEDGE);

							Session sessionConsumer = connectionInternal.createSession(false, Session.AUTO_ACKNOWLEDGE);

							// Consumer
							Destination destinationConsumer = sessionConsumer.createQueue(VIRTUAL_QUEUE_CONSUMER_INSERTAPI_INPUT + "." + newTenant + ".*");
							log.info("[JMSConsumerMainThread::run] Connected to queue:" + destinationConsumer.toString());
							MessageConsumer consumer = sessionConsumer.createConsumer(destinationConsumer);

							consumer.setMessageListener(new JMSMessageListener(newTenant, sessionProducer));

							JMSTenant jmsTenant = new JMSTenant(sessionConsumer, consumer, sessionProducer);
							jmsTenants.put(newTenant, jmsTenant);
						}
					}
				} catch (MongoAccessException e) {
					log.error("[JMSConsumerMainThread::run] Error reading tenant list... continue with old list", e);
				}
				Thread.sleep(5 * 60 * 1000);
			}

		} catch (InterruptedException ie) {
			log.warn("[JMSConsumerMainThread::run] JMSConsumerMainThread shutdown");
			closing();
		} catch (Exception e) {
			log.error("[JMSConsumerMainThread::run] Error on Starting connection..." + e.getMessage(), e);

		}
	}

	private void closing() {
		log.info("[JMSConsumerMainThread::run] Closing connection...");
		try {
			Iterator<JMSTenant> iter = jmsTenants.values().iterator();
			while (iter.hasNext()) {
				iter.next().closeAll();
			}

			connectionInternal.close();
			connectionExternal.close();
		} catch (JMSException e) {
			log.error("[JMSConsumerMainThread::run] Error on Closing connection..." + e.getMessage(), e);
		}
		log.info("[JMSConsumerMainThread::run] Closed");

	}

	public synchronized void onException(JMSException ex) {
		System.out.println("JMS Exception occured. Shutting down client.");
	}

	private ActiveMQConnectionFactory createConnection(String url, String username, String password, Integer maxThreadPoolSize) {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

		if (maxThreadPoolSize != null)
			connectionFactory.setMaxThreadPoolSize(maxThreadPoolSize);

		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);

		return connectionFactory;
	}

	private ActiveMQConnectionFactory createConnection(String url, String username, String password, Integer maxThreadPoolSize, Long initialRedeliveryDelay,
			Double backOffMultiplier, Boolean useExponentialBackOff, Long maximumRedeliveryDelay, Integer maximumRedeliveries) {

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

		if (maxThreadPoolSize != null)
			connectionFactory.setMaxThreadPoolSize(maxThreadPoolSize);

		RedeliveryPolicy policy = new RedeliveryPolicy();
		if (initialRedeliveryDelay != null)
			policy.setInitialRedeliveryDelay(initialRedeliveryDelay);
		if (backOffMultiplier != null)
			policy.setBackOffMultiplier(backOffMultiplier);
		if (useExponentialBackOff != null)
			policy.setUseExponentialBackOff(useExponentialBackOff);
		if (maximumRedeliveryDelay != null)
			policy.setMaximumRedeliveryDelay(maximumRedeliveryDelay);
		if (maximumRedeliveries != null)
			policy.setMaximumRedeliveries(maximumRedeliveries);

		connectionFactory.setRedeliveryPolicy(policy);
		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);

		return connectionFactory;
	}

}
