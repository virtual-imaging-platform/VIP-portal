package fr.insalyon.creatis.vip.core.integrationtest;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.UserBusiness;
import fr.insalyon.creatis.vip.core.server.security.session.SessionAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAdminSecurityContextFactory.class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface WithMockAdmin { }

class WithMockAdminSecurityContextFactory
        implements WithSecurityContextFactory<WithMockAdmin> {

    private UserBusiness userBusiness;

    @Autowired
    public WithMockAdminSecurityContextFactory(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockAdmin customUser) {
        SessionAuthenticationProvider provider = new SessionAuthenticationProvider();
        User adminUser = null;
        try {
            adminUser = userBusiness.getUserWithGroups(ServerMockConfig.TEST_ADMIN_EMAIL);
        } catch (VipException e) {
            throw new RuntimeException(e);
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(provider.createAuthenticationFromUser(adminUser));
        return context;
    }

}