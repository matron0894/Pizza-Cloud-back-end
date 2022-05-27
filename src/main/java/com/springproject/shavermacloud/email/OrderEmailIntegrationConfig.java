package com.springproject.shavermacloud.email;

import com.springproject.shavermacloud.email.dao.EmailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;

@Slf4j
@Configuration
public class OrderEmailIntegrationConfig {

    //Imap Inbound Adapter — Service Activator — Service Activator — Filter — Jpa Outbound Adapter


    //JAVA DSL
    @Bean
    public IntegrationFlow mailListener(
            EmailProperties emailProps,
            EmailToOrderTransformer emailToOrderTransformer,
            OrderSubmitMessageHandler orderSubmitHandler) {

        log.debug(emailProps.getImapUrl());

        return IntegrationFlows.from(Mail.imapInboundAdapter(
                                        emailProps.getImapUrl())
                                .userFlag("testSIUserFlag")
                                .simpleContent(true)
                                .javaMailProperties(p -> {
                                    p.put("mail.debug", "true");
                                    p.put("mail.imaps.ssl.trust", "*");
                                    p.put("mail.store.protocol", "imaps");
                                    p.put("mail.imap.ssl.enable", "true");
                                }),
                        e -> e.autoStartup(true)
                                .poller(Pollers.fixedDelay(emailProps.getPollRate())))
                //.<javax.mail.Message>handle((payload, headers) -> (payload))
                //.transform(Mail.toStringTransformer())
                    .transform(emailToOrderTransformer)
                .handle(orderSubmitHandler)
                .get();
    }

/*
    //вспомогательный поток - сгенерируем данные, отправим Animals в почтовый ящик.
    //но можно вручную отправить пиьсма с текстом
    @Bean
    public IntegrationFlow sendMailFlow() {
        return IntegrationFlows.from("sendMailChannel")
                .handle(Mail.outboundAdapter("smtp.mail.ru")
                                .port(25)
                                .credentials(this.email, this.password)
                                .javaMailProperties(p -> {
                                    p.put("mail.debug", "true");
                                    p.put("mail.smtp.ssl.trust", "*");
                                    p.put("mail.smtp.starttls.enable", "true");
                                }),
                        e -> e.id("sendMailEndpoint"))
                .get();
    }*/

}
