package org.csi.yucca.dataservice.insertdataapi.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiBaseException;
import org.csi.yucca.dataservice.insertdataapi.exception.InsertApiRuntimeException;
import org.csi.yucca.dataservice.insertdataapi.mongo.SDPInsertApiMongoConnectionSingleton;
import org.csi.yucca.dataservice.insertdataapi.service.StreamService;
import org.csi.yucca.dataservice.insertdataapi.util.NamedThreadFactory;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class JMSMessageListener implements MessageListener {
	private static final Log log = LogFactory.getLog("org.csi.yucca.datainsert");

	private static StreamService streamService = new StreamService();
	private final String codTenant;

	private ConnectionFactory connectionFactoryExternal;

	public static final String VIRTUAL_QUEUE_PRODUCER_INSERTAPI_OUTPUT = "output";

	ExecutorService sendMessageService;
	
	public JMSMessageListener(String codTenant, ConnectionFactory connectionFactoryExternal) {
		this.codTenant = codTenant;
		this.connectionFactoryExternal = connectionFactoryExternal;
		
		sendMessageService = Executors.newSingleThreadExecutor(new NamedThreadFactory("JMS-forward-message-" + codTenant));
	}

	public void onMessage(Message message) {

		try {
			if (message instanceof TextMessage) {
				TextMessage txtMessage = (TextMessage) message;
				log.debug("[JMSMessageListener::onMessage]  JMSListener=[" + codTenant + "] -> msg" + txtMessage.getText());
				try {
					
					if (SDPInsertApiMongoConnectionSingleton.getInstance().getDataDbConfiguration(codTenant).getForwardToBrokerFromCEP()!=null &&
							SDPInsertApiMongoConnectionSingleton.getInstance().getDataDbConfiguration(codTenant).getForwardToBrokerFromCEP().equals(false))
					{
						sendMessageService.execute(createSendMessageRunnable(connectionFactoryExternal, txtMessage));
					}
					log.debug("[JMSMessageListener::onMessage] send");

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

	private void forwardMessage(ConnectionFactory connectionFactoryExternal, TextMessage message) {
		long start = System.currentTimeMillis();
		try {
			
			if (((ActiveMQMessage) message).getRedeliveryCounter() == 0) {
				String smartObject_stream = JMSMessageListener.streamService.getSmartobject_StreamFromJson(codTenant, message.getText());
				log.debug("[JMSMessageListener::forwardMessage] first key:" + smartObject_stream);
				
				Connection connectionExternal = connectionFactoryExternal.createConnection();

				Session sessionProducer = connectionExternal.createSession(false, Session.AUTO_ACKNOWLEDGE);

				Destination destinationProducer = sessionProducer.createTopic(VIRTUAL_QUEUE_PRODUCER_INSERTAPI_OUTPUT + "." + codTenant + "." + smartObject_stream);
				log.debug("[JMSMessageListener::forwardMessage] Connected to queue:" + destinationProducer.toString());
				MessageProducer producer = sessionProducer.createProducer(destinationProducer);

				message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
				producer.send(message);
				producer.close();
				sessionProducer.close();
				connectionExternal.close();
			}
			else {
				log.info("[JMSMessageListener::forwardMessage] skipped message for redelivery:" +message.getText());
			}

		} catch (Throwable e) {
			log.error("[JMSProducerMainThread::forwardMessage] Error: " + e.getMessage(),e);
		} finally {
			long elapsed = System.currentTimeMillis() - start;
			log.debug("forwardMessage elapsed: " + elapsed);
		}
	}
	
	
	private Runnable createSendMessageRunnable(final ConnectionFactory connectionFactoryExternal, final TextMessage message){

	    Runnable sendMessageRunnable = new Runnable(){
	        public void run(){
				log.info("[JMSMessageListener::onMessage]  forwardMessage");

	        	forwardMessage(connectionFactoryExternal, message);
	        }
	    };

	    return sendMessageRunnable;

	}
}
