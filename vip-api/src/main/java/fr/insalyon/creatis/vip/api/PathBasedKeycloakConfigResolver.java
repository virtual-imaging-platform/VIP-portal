package fr.insalyon.creatis.vip.api;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author khalilKes keycloak path resolver to allow external keycloak.json
 *
 */
@Configuration
public class PathBasedKeycloakConfigResolver implements KeycloakConfigResolver {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Resource vipConfigFolder;

    private KeycloakDeployment deployment;


    @Override
    public KeycloakDeployment resolve(HttpFacade.Request request) {
        if (deployment != null) {
            return deployment;
        }

        InputStream is;
        try {
            is = new FileInputStream( vipConfigFolder.getFile().getAbsoluteFile() + "/keycloak.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        deployment = KeycloakDeploymentBuilder.build(is);
        return deployment;
    }
}
