package fr.insalyon.creatis.vip.integrationtest;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.util.Assert;

import fr.insalyon.creatis.vip.api.SpringRestApiConfig;
import fr.insalyon.creatis.vip.api.controller.PlatformController;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.SpringCoreConfig;

/**
 * Created by abonnet on 7/21/16.
 *
 * Test the the global spring configuration, almost nothing is mocked
 *
 */
@SpringJUnitWebConfig(value = { SpringRestApiConfig.class, SpringCoreConfig.class })
public class DefaultSpringConfigurationIT {

    @Autowired
    private PlatformController platformController;


    @BeforeAll
    static void configureHomePath() throws Exception {
        String fakeHomePath = Paths.get(ClassLoader.getSystemResource("TestHome").toURI())
                .toAbsolutePath().toString();
        System.setProperty("user.home", fakeHomePath);
    }

    @Test
    public void propertiesShouldBePresent() throws VipException {
        // test that the platform properties generation does not throw any exception
        Assert.notNull(platformController.getPlatformProperties(), "platform properties should be present");
    }

}
