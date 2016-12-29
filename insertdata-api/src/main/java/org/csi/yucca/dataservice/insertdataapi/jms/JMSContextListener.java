package org.csi.yucca.dataservice.insertdataapi.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private  ExecutorService exService = null;
	

	public void contextInitialized(ServletContextEvent arg0) {
		log.info("[JMSContextListener::contextInitialized]");
		exService = Executors.newSingleThreadExecutor();
		exService.execute(new JMSConsumerMainThread());
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("[JMSContextListener::contextDestroyed]");
		exService.shutdownNow();
	}

}
