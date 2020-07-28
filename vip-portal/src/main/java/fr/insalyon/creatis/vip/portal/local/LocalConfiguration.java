package fr.insalyon.creatis.vip.portal.local;

import fr.insalyon.creatis.vip.core.server.business.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Configuration
@Profile("local")
public class LocalConfiguration {

    @Autowired
    public LocalConfiguration(
            Resource vipConfigFolder,
            ConfigurableEnvironment env) throws IOException {
        Resource configFileResource = new FileSystemResource(
                vipConfigFolder.getFile().toPath().resolve("vip-local.conf"));
        env.getPropertySources().addLast(
                new ResourcePropertySource(configFileResource)
        );
    }
}
