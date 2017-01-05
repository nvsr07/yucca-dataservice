package org.csi.yucca.dataservice.insertdataapi.jms;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.service.StreamService;

public class JMSMessageListener implements MessageListener {
	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	private static StreamService streamService = new StreamService();
	private final String codTenant;

	private Session sessionProducer;

	public static final String VIRTUAL_QUEUE_PRODUCER_INSERTAPI_OUTPUT = "output";

	public JMSMessageListener(String codTenant, Session sessionProducer) {
		this.codTenant = codTenant;
		this.sessionProducer = sessionProducer;
	}

	public void onMessage(Message message) {

		try {
			if (message instanceof TextMessage) {
				TextMessage txtMessage = (TextMessage) message;
				log.info("[JMSMessageListener::onMessage]  JMSListener=[" + codTenant + "] -> msg" + txtMessage.getText());
				try {

					forwardMessage(sessionProducer, txtMessage);
					JMSMessageListener.streamService.dataInsert(txtMessage.getText(), codTenant, message.getJMSMessageID(), "", "");

					// try {Thread.sleep(5000);} catch (InterruptedException e)
					// {} // for testing, to remove
				} catch (InsertApiBaseException e) {
					log.warn("[JMSMessageListener::onMessage]  Invalid message for JMS [" + e.getErrorCode() + "]: " + e.getErrorName());
				} catch (InsertApiRuntimeException e) {
					log.error("[JMSMessageListener::onMessage]  System error for JMS", e);
					throw e;
				}
			} else {
				log.warn("[JMSMessageListener::onMessage]  No textMessage" + message);
			}
		} catch (JMSException e) {
			log.error("[JMSMessageListener::onMessage]  textMessage problem", e);
		}

	}

	public void forwardMessage(Session sessionProducer, TextMessage message) {
		long start = System.currentTimeMillis();
		try {
			log.info("forwardMessage message destination: " + message.getJMSDestination());
			ActiveMQDestination activeMQDestination = (ActiveMQDestination) message.getJMSDestination();
			log.info("forwardMessage active mq message destination: " + ((ActiveMQMessage)message).getDestination());

			
			log.info("forwardMessage message physical name: " + activeMQDestination.getPhysicalName());
			log.info("forwardMessage message qualified name: " + activeMQDestination.getQualifiedName());
			log.info("forwardMessage message reference: " + activeMQDestination.getReference());

			if(activeMQDestination.getDestinationPaths()!=null){
				for (int j = 0; j < activeMQDestination.getDestinationPaths().length; j++) {
					log.info("forwardMessage message path["+j+"]: " + activeMQDestination.getDestinationPaths()[j]);
				}
			}
			else
				log.info("forwardMessage message path is null");
			
			if(activeMQDestination.getCompositeDestinations()!=null){
				for (int j = 0; j < activeMQDestination.getCompositeDestinations().length; j++) {
					log.info("forwardMessage message composite ["+j+"]: " + activeMQDestination.getCompositeDestinations()[j]);
				}
			}
			else
				log.info("forwardMessage message composite is null");

			
			log.info("forwardMessage message id: " + message.getJMSMessageID());
			log.info("forwardMessage message redelivered: " + message.getJMSRedelivered());
			log.info("forwardMessage message redeliveryCounter: " + ((ActiveMQMessage)message).getRedeliveryCounter());
			
			ActiveMQMessage activeMQMessage = (ActiveMQMessage)message;
			log.info("forwardMessage message originalDestination: " + activeMQMessage.getOriginalDestination());
			log.info("forwardMessage message from name: " + activeMQMessage.getFrom().getName());
			log.info("forwardMessage message from broker url: " + activeMQMessage.getFrom().getBrokerInfo().getBrokerURL());
			
			Enumeration propertyNames = activeMQMessage.getPropertyNames();
			while(propertyNames.hasMoreElements()){
			    String propertyName = (String) propertyNames.nextElement()			    		;
				log.info("forwardMessage message property("+propertyName+"): " + activeMQMessage.getProperty(propertyName));

			}

			// producer output.${tenant.code}.${source.code}_${stream.code}
			if (((ActiveMQMessage)message).getRedeliveryCounter()==0) {
				Destination destinationProducer = sessionProducer.createTopic(VIRTUAL_QUEUE_PRODUCER_INSERTAPI_OUTPUT + ".sandbox.pippo");
				log.info("[JMSConsumerMainThread::run] Connected to queue:" + destinationProducer.toString());
				MessageProducer producer = sessionProducer.createProducer(destinationProducer);

				message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
				producer.send(message);
			}
			
		} catch (Throwable e) {
			log.error("[JMSProducerMainThread::forwardMessage] " + e.getMessage());
		} finally {
			long elapsed = System.currentTimeMillis() - start;
			log.info("forwardMessage elapsed: " + elapsed);
		}
	}

}
