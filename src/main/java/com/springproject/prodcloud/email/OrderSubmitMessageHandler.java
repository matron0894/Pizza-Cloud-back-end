package com.springproject.prodcloud.email;


import com.springproject.prodcloud.email.dao.ApiProperties;
import com.springproject.prodcloud.email.dao.EmailOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderSubmitMessageHandler implements GenericHandler<EmailOrder> {

    private RestTemplate rest;
    private ApiProperties apiProps;


        @Autowired
    public OrderSubmitMessageHandler(ApiProperties apiProps, RestTemplate rest) {
        this.apiProps = apiProps;
        this.rest = rest;
    }


    //отправляет заказ в Cloud API
    @Override
    public Object handle(EmailOrder order, MessageHeaders headers) {
        rest.postForObject(apiProps.getUrl(), order, String.class);
        return null;
    }

}
