package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.sma.client.SMAClient;
import fr.insalyon.creatis.sma.client.SMAClientException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.business.proxy.ProxyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring configuration to make vip work in local, without any other
 * external service.
 *
 * This overrides ProxyClient and SmaClient.
 *
 * This also import properties from vip-local.conf to configure all the other
 * local components of this package
 */
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
        Map<String, Object> map = new HashMap<>();
        map.put("db.tableEngine", "");
        env.getPropertySources().addLast(new MapPropertySource("localh2properties", map));
    }

    /*
        Overrides only to do nothing and prevent original behavior that
        would fail
     */
    @Component
    @Primary
    public class ProxyClientLocal extends ProxyClient {

        public ProxyClientLocal(Server server) {
            super(server);
        }

        @Override
        public void checkProxy() throws BusinessException {
        }

        @Override
        public String getExistingTrustRootPath() {
            return null;
        }

        @Override
        public void copyFile(String source, String dest) {
        }
    }

    /**
     * Send nothing but prints mail in logs
     */
    @Component
    @Primary
    public class SmaClientLocal extends SMAClient {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        public SmaClientLocal() throws UnknownHostException {
            super((String) null, 0);
        }

        @Override
        public String sendEmail(String subject, String contents, String[] recipients, boolean direct, String username) throws SMAClientException {
            logger.info("sending {} mail from {} to {} ", subject, username, recipients);
            logger.info("Mail content : {} ", contents);
            return "localSMAOperation";
        }
    }
}
