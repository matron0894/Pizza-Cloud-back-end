package com.springproject.shavermacloud.email.dao;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "prodcloud.email")
@Component
public class EmailProperties {
    private String username;
    private String password;
    private String host;
    private String mailbox;
    private long pollRate = 30_000L; //опрос каждые 30 секунд


    public String getImapUrl() {
        return String.format("imaps://%s:%s@%s/%s", this.username, this.password, this.host, this.mailbox);
    }
}