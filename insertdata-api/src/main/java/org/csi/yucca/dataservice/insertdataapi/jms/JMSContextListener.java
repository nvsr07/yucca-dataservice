package org.csi.yucca.dataservice.insertdataapi.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.csi.yucca.dataservice.insertdataapi.util.SDPInsertApiConfig;

public class JMSContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		Thread thread = new Thread(new JMSConsumerMainThread());
        thread.start();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

}
