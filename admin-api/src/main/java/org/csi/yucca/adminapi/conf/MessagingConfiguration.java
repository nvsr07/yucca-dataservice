package org.csi.yucca.adminapi.conf;

import java.util.Arrays;

import javax.jms.ConnectionFactory;
 
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.csi.yucca.adminapi.messaging.MessageReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

/**
 * 
 * http://websystique.com/springmvc/spring-4-mvc-jms-activemq-annotation-based-example/
 * 
 * http://websystique.com/springmvc/spring-4-mvc-jms-activemq-annotation-based-example/
 * 
 * https://examples.javacodegeeks.com/enterprise-java/spring/spring-jms-example/
 * 
 * @author gianfranco.stolfa
 *
 */


@Configuration
@PropertySource(value = { "classpath:jms-message.properties" })
public class MessagingConfiguration {
 
	@Value("${default.broker.url}")
	private String defaultBrokerUrl;

//	@Value("${jms.queue}")
//	private String jmsQueue;
	
	@Value("${response.queue}")
	private String responseQueue;

	@Value("${user}")
	private String user;

	@Value("${password}")
	private String password;

	
    @Autowired
    MessageReceiver messageReceiver;
    
    
    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(defaultBrokerUrl);
        connectionFactory.setTrustedPackages(Arrays.asList("com.websystique.spring"));
        connectionFactory.setUserName(user);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }
 
    /*
     * Unused.
     */
    @Bean
    public ConnectionFactory cachingConnectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setTargetConnectionFactory(connectionFactory());
        connectionFactory.setSessionCacheSize(10);
        return connectionFactory;
    }
 
    /*
     * Message listener container, used for invoking messageReceiver.onMessage on message reception.
     */
//    @Bean
//    public MessageListenerContainer getContainer(){
//        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
//        container.setDestinationName(jmsQueue);
//        container.setMessageListener(messageReceiver);
//        return container;
//    }
 
    /*
     * Used for Sending Messages.
     */
    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setDefaultDestinationName(responseQueue);
        return template;
    }
     
     
    @Bean
    MessageConverter converter(){
        return new SimpleMessageConverter();
    }
     
}