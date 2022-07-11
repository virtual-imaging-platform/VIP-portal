package fr.insalyon.creatis.vip.api.security.keycloak;

import fr.insalyon.creatis.vip.core.client.bean.User;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

public class SpringKeycloakPrincipal extends KeycloakPrincipal<KeycloakSecurityContext> {

    private final User vipUser;

    public SpringKeycloakPrincipal(User vipUser, String name, KeycloakSecurityContext context) {
        super(name, context);
        this.vipUser = vipUser;
    }

    public User getVipUser() {
        return vipUser;
    }
}
