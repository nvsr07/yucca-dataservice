package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

@WebListener
public class JMSContextListener implements ServletContextListener {
	private static final Log log=LogFactory.getLog("org.csi.yucca.datainsert");
	JMSConsumerMainThread th;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("[JMSContextListener::contextDestroyed]");
		th.closing();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		log.info("[JMSContextListener::contextInitialized]");
		th = new JMSConsumerMainThread();
		Thread thread = new Thread(th);
        thread.start();
	}

}
