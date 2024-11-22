package fr.insalyon.creatis.vip.api.security.oidc;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.net.URI;

import fr.insalyon.creatis.vip.api.CarminProperties;

@Service
public class OidcConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Environment env;
    private final Map<String, OidcServer> servers;

    public class OidcServer {
        public final String issuer;
        public final Boolean useResourceRoleMappings;
        public final String resourceName;

        OidcServer(String issuer, Boolean useResourceRoleMappings, String resourceName) {
            this.issuer = issuer;
            if (useResourceRoleMappings && (resourceName == null || resourceName.isEmpty())) {
                throw new IllegalArgumentException("useResourceRoleMappings enabled but no resourceName defined");
            }
            this.useResourceRoleMappings = useResourceRoleMappings;
            this.resourceName = resourceName;
        }
    }

    @Autowired
    public OidcConfig(Environment env, Resource vipConfigFolder) throws IOException, URISyntaxException, BusinessException {
        this.env = env;
        // Build the list of OIDC servers from config file. If OIDC is disabled, just create an empty list.
        HashMap<String, OidcServer> servers = new HashMap<>();
        if (isOIDCActive()) {
            // Many errors are possible here:
            // IOException in getFile() (can't read file), JsonProcessingException in readTree() (bad JSON),
            // URISyntaxException in URI() (bad URL syntax), NullPointerException in get().asText() (missing JSON key),
            // and probably more...
            // As long as isOIDCActive(), we do not try to handle them: just let them bubble up, causing a boot-time error.
            final String basename = "keycloak.json";
            // read and parse keycloak.json file into one OidcServer config
            File file = vipConfigFolder.getFile().toPath().resolve(basename).toFile();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(file);
            // mandatory fields: just check for their presence, content will be validated by URI() below
            String baseURL, realm;
            if (node.hasNonNull("auth-server-url") && node.hasNonNull("realm")) {
                baseURL = node.get("auth-server-url").asText();
                realm = node.get("realm").asText();
            } else {
                throw new BusinessException("Failed parsing " + basename + ": missing mandatory fields");
            }
            // optional fields
            Boolean useResourceRoleMappings;
            if (node.hasNonNull("use-resource-role-mapping")) {
                useResourceRoleMappings = node.get("use-resource-role-mapping").asBoolean();
            } else {
                useResourceRoleMappings = false;
            }
            String resourceName;
            if (node.hasNonNull("resource")) {
                resourceName = node.get("resource").asText();
            } else {
                resourceName = "";
            }

            // Build OIDC server URL from auth-server-url + realm name (this is Keycloak-specific).
            // We use URI.resolve() instead of just concatenation, to correctly handle optional '/' at the end of baseURL.
            URI url = new URI(baseURL).resolve("realms/" + realm);
            String issuer = url.toASCIIString();
            servers.put(issuer, new OidcServer(issuer, useResourceRoleMappings, resourceName));
        }
        this.servers = servers;
    }

    public boolean isOIDCActive() {
        return env.getProperty(CarminProperties.KEYCLOAK_ACTIVATED, Boolean.class, Boolean.FALSE);
    }

    // list of issuers URLs
    public Collection<String> getServers() {
        return servers.keySet();
    }

    // get resource name property for a given issuer URL
    public OidcServer getServerConfig(String issuer) {
        return servers.get(issuer);
    }
}
