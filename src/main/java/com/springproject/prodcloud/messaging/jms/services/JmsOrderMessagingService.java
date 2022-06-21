package com.springproject.prodcloud.messaging.jms.services;

import com.springproject.prodcloud.domain.Order;
import com.springproject.prodcloud.messaging.OrderSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;

@Service
public class JmsOrderMessagingService implements OrderSender {

    private JmsTemplate jmsTemplate;

    @Autowired
    public JmsOrderMessagingService(JmsTemplate jms) {
        this.jmsTemplate = jms;
    }

    @Value("${prodcloud.order.queue}")
    String jmsQueue;

    @Override
    public void sendOrder(Order order) {
        jmsTemplate.convertAndSend(
                jmsQueue,  //destination of message
                order,
                this::addOrderSource);
    }

    private Message addOrderSource(Message message) throws JMSException {
        message.setStringProperty("X_ORDER_SOURCE", "WEB");
        return message;
    }

}
