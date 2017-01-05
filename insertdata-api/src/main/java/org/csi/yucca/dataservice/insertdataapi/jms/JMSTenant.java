package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class JMSTenant {

	private Session sessionConsumer;
	private Session sessionProducer;
	private MessageConsumer consumer;
	private MessageProducer producer;

	public JMSTenant() {
		super();
	}

	public JMSTenant(Session sessionConsumer, MessageConsumer consumer, Session sessionProducer) {
		super();
		this.setSessionConsumer(sessionConsumer);
		this.consumer = consumer;
		this.sessionProducer = sessionProducer;
		//this.producer = producer;
	}

	public void closeAll() throws JMSException {
		if (getSessionConsumer() != null)
			getSessionConsumer().close();
		if (getSessionProducer() != null)
			getSessionProducer().close();
		if (getConsumer() != null)
			getConsumer().close();
		if (getProducer() != null)
			getProducer().close();

	}

	public Session getSessionProducer() {
		return sessionProducer;
	}

	public void setSessionProducer(Session sessionProducer) {
		this.sessionProducer = sessionProducer;
	}

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}

	public MessageProducer getProducer() {
		return producer;
	}

	public void setProducer(MessageProducer producer) {
		this.producer = producer;
	}

	public Session getSessionConsumer() {
		return sessionConsumer;
	}

	public void setSessionConsumer(Session sessionConsumer) {
		this.sessionConsumer = sessionConsumer;
	}

}
