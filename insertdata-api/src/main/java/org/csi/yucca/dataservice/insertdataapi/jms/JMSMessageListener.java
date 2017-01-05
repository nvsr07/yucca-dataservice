package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

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
			log.info("forwardMessage message id: " + message.getJMSMessageID());
			 //producer output.${tenant.code}.${source.code}_${stream.code}
			Destination destinationProducer = sessionProducer.createTopic(VIRTUAL_QUEUE_PRODUCER_INSERTAPI_OUTPUT+".sandbox.pippo");
			log.info("[JMSConsumerMainThread::run] Connected to queue:"+ destinationProducer.toString());
			MessageProducer producer = sessionProducer.createProducer(destinationProducer);
			

			message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.send(message);
		} catch (Throwable e) {
			log.error("[JMSProducerMainThread::forwardMessage] " + e.getMessage());
		}
		finally{
			long elapsed = System.currentTimeMillis() - start;
			log.info("forwardMessage elapsed: " + elapsed);
		}
	}

}
