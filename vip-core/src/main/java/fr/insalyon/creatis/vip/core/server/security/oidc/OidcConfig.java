package fr.insalyon.creatis.vip.core.server.security.oidc;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.net.URI;

import fr.insalyon.creatis.vip.core.server.CarminProperties;

/* OidcConfig : defines the list of OIDC bearer token providers,
 * i.e. things that provide a way to generate and validate bearer tokens for authentication on VIP API.
 * Such providers are typically implemented by Keycloak servers.
 */
@Service
public class OidcConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Server server;
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
    public OidcConfig(Server server, Resource vipConfigFolder) throws IOException, URISyntaxException, BusinessException {
        this.server = server;
        // Build the list of OIDC servers from config file. If OIDC is disabled, just create an empty list.
        HashMap<String, OidcServer> servers = new HashMap<>();
        if (isOIDCActive()) {
            // Many errors are possible here:
            // IOException in getFile() (can't read file), JsonProcessingException in readTree() (bad JSON),
            // URISyntaxException in URI() (bad URL syntax), NullPointerException in get().asText() (missing JSON key),
            // and probably more...
            // As long as isOIDCActive(), we do not try to handle them: just let them bubble up, causing a boot-time error.
            final String basename = "vip-oidc.json";

            // Read and parse vip-oidc.json file into a list of OIDC servers.
            // See https://github.com/virtual-imaging-platform/VIP-portal/wiki/API-authentication
            // for vip-oidc.json file format.
            //
            // A note on Keycloak vs OIDC: Keycloak is a specific server software, providing an implementation of OIDC.
            // In order to favor future compatibility with other OIDC implementations, things that are Keycloak-specific
            // should be explicitly documented as such:
            // - A Keycloak URL is typically https://hostname/realms/<realmName>. The concept of "realm" is Keycloak-specific.
            // - Spring OIDC does an HTTP GET on <url>/.well-known/openid-configuration to get the various OIDC endpoints.
            //   Since our base url can be anything and does not explicitly involve realms, this is generic OIDC.
            // - How roles are encoded in the JWT, and the concept of "resource", is Keycloak-specific.
            // So, currently, our only Keycloak-specific behaviour is how roles are mapped in OIDCResolver.parseAuthorities().
            // Adding a new "type" field in the OidcServer object, which defaults to type=keycloak and conditions how roles are mapped,
            // would allow to extend compatibility with other OIDC servers.
            File file = vipConfigFolder.getFile().toPath().resolve(basename).toFile();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(file);
            if (!(rootNode.hasNonNull("servers") && rootNode.get("servers").isArray())) {
                throw new BusinessException("Failed parsing " + basename + ": missing mandatory field: servers");
            }
            for (JsonNode serverNode : rootNode.get("servers")) {
                if (!(serverNode.hasNonNull("url"))) {
                    throw new BusinessException("Failed parsing " + basename + ": missing mandatory field: url");
                }
                // url field is mandatory: here we just check for its presence, content will be validated by URI() below
                String baseURL = serverNode.get("url").asText();
                // optional fields
                Boolean useResourceRoleMappings = false;
                if (serverNode.hasNonNull("use-resource-role-mapping")) {
                    useResourceRoleMappings = serverNode.get("use-resource-role-mapping").asBoolean();
                }
                String resourceName = "";
                if (serverNode.hasNonNull("resource")) {
                    resourceName = serverNode.get("resource").asText();
                }
                // Add a new OIDC server to our config.
                URI url = new URI(baseURL);
                String issuer = url.toASCIIString();
                if (servers.containsKey(issuer)) {
                    throw new BusinessException("Failed parsing " + basename + ": duplicate issuers");
                }
                servers.put(issuer, new OidcServer(issuer, useResourceRoleMappings, resourceName));
            }
        }
        this.servers = servers;
    }

    public boolean isOIDCActive() {
        return server.getEnvProperty(CarminProperties.KEYCLOAK_ACTIVATED, Boolean.class, Boolean.FALSE);
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
