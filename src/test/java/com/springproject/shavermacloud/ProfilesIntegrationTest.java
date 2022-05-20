package com.springproject.shavermacloud;

import com.springproject.shavermacloud.service.DevelopmentConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfilesIntegrationTest {

    @Autowired
    Environment environment;

    @Autowired
    DevelopmentConfig testBean;

    @Test
    public void testSpringProfiles() {
        for (final String profileName : environment.getActiveProfiles()) {
            System.out.println("Currently active profile - " + profileName);
        }
        Assert.assertEquals("dev", environment.getActiveProfiles()[0]);

        assertNotNull(testBean);

    }
}