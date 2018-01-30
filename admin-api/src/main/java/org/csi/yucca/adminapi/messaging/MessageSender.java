package org.csi.yucca.adminapi.messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
 
    @Autowired
    JmsTemplate jmsTemplate;
 
//    public void sendMessage(final Tenant tenant) {
//    	 
//        jmsTemplate.send(new MessageCreator(){
//                @Override
//                public Message createMessage(Session session) throws JMSException{
//                    ObjectMessage objectMessage = session.createObjectMessage(tenant);
//                    return objectMessage;
//                }
//            });
//    }
    public void sendMessage(final String msg) {
    	 
        jmsTemplate.send(new MessageCreator(){
                @Override
                public Message createMessage(Session session) throws JMSException{
                    Message objectMessage = session.createTextMessage(msg);
                    return objectMessage;
                }
            });
    }
 
}