package com.springproject.shavermacloud.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@ConfigurationProperties(prefix = "product.orders")
//@ConfigurationProperties works best with hierarchical properties that all have the same prefix
@Data
@Validated
public class OrderProps {

    @Min(value = 5, message = "must be between 5 and 25")
    @Max(value = 25, message = "must be between 5 and 25")
    private int pageSize = 20;
}

/*Note: If we don't use @Configuration in the POJO,
 then we need to add @EnableConfigurationProperties(ConfigProperties.class) in the main Spring application class
  to bind the properties into the POJO*/