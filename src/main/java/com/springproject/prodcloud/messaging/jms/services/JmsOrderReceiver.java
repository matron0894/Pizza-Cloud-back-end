package com.springproject.prodcloud.messaging.jms.services;

import com.springproject.prodcloud.domain.Order;
import com.springproject.prodcloud.messaging.OrderReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class JmsOrderReceiver implements OrderReceiver {

    private JmsTemplate jms;

    @Autowired
    public JmsOrderReceiver(JmsTemplate jms) {
        this.jms = jms;
    }

    @Value("${prodcloud.order.queue}")
    String jmsQueue;


    @Override
    public Order receiveOrder() {
        return (Order) jms.receiveAndConvert(jmsQueue);
    }

}
