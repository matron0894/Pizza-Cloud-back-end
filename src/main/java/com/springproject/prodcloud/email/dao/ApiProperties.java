package com.springproject.prodcloud.email.dao;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "prodcloud.api")
@Component
public class ApiProperties {

    private String url;
}