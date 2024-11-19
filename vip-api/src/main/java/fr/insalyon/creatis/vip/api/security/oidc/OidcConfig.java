package fr.insalyon.creatis.vip.api.security.oidc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collection;
import java.io.FileInputStream;
import java.net.URI;

import fr.insalyon.creatis.vip.api.CarminProperties;

@Configuration
public class OidcConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Environment env;
    private final Collection<OidcServer> servers;

    static private class OidcServer {
        public String issuer;
        public String resource;

        OidcServer(String issuer, String resource) {
            this.issuer = issuer;
            this.resource = resource;
        }
    }

    @Autowired
    public OidcConfig(Environment env, Resource vipConfigFolder) throws Exception {
        this.env = env;
        // Build the list of OIDC servers from config file. If OIDC is disabled, just create an empty list.
        ArrayList<OidcServer> servers = new ArrayList<>();
        if (isOIDCActive()) {
            final String basename = "keycloak.json";
            try {
                // read and parse keycloak.json file to get OIDC server URL and clientId
                String filename = vipConfigFolder.getFile().getAbsoluteFile() + "/" + basename;
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(new FileInputStream(filename));
                String baseURL = node.get("auth-server-url").asText();
                String realm = node.get("realm").asText();
                String resource = node.get("resource").asText();
                // Build OIDC server URL from auth-server-url + realm name (this is Keycloak-specific).
                // We use URI.resolve() instead of just concatenation, to correctly handle optional '/' at the end of baseURL.
                URI url = new URI(baseURL).resolve("realms/" + realm);
                servers.add(new OidcServer(url.toASCIIString(), resource));
            } catch (Exception exception) {
                // Many errors are possible here:
                // IOException in FileInputStream (can't read file), JsonProcessingException in readTree (bad JSON),
                // URISyntaxException in URI() (bad URL syntax), null exceptions in get().asText() (missing JSON key),
                // and probably more...
                // As long as isOIDCActive(), we do not try to handle them: just log and throw, causing a boot-time error.
                logger.error("Failed loading {}", basename, exception);
                throw exception;
            }
        }
        this.servers = servers;
    }

    public boolean isOIDCActive() {
        return env.getProperty(CarminProperties.KEYCLOAK_ACTIVATED, Boolean.class, Boolean.FALSE);
    }

    // list of issuers URLs
    public Collection<String> getServers() {
        return servers.stream().map((server) -> server.issuer).toList();
    }

    // get resource name property for a given issuer URL
    public String getIssuerResourceName(String issuer) {
        for (OidcServer server: servers) {
            if (server.issuer.equals(issuer)) {
                return server.resource;
            }
        }
        return null;
    }
}
