package fr.insalyon.creatis.vip.core.server.business.base;

import java.util.List;
import java.util.function.Supplier;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.business.CoreUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.security.common.CurrentUserRolesBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.PageBuilder;

public abstract class CommonBusiness {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Server server;
    protected CorePermissions permissions;
    protected Supplier<User> userSupplier;
    protected CurrentUserRolesBusiness currentUserRolesBusiness;
    protected PageBuilder pageBuilder;


    @Autowired
    public void setServer(Server server) {
        this.server = server;
    }

    @Autowired
    public void setUserSupplier(Supplier<User> userSupplier) {
        this.userSupplier = userSupplier;
    }

    @Autowired
    public void setCurrentUserRolesBusiness(CurrentUserRolesBusiness currentUserRolesBusiness) {
        this.currentUserRolesBusiness = currentUserRolesBusiness;
    }

    @Autowired
    public void setCorePermissions(CorePermissions corePermissions) {
        this.permissions = corePermissions;
    }

    @Autowired
    public void setPageBuilder(PageBuilder pageBuilder) {
        this.pageBuilder = pageBuilder;
    }

    public Server getServer() {
        return server;
    }

    public User getUser() {
        return userSupplier.get();
    }

    public String getUserEmail() {
        return getUser().getEmail();
    }

    public UserLevel getUserLevel() {
        return getUser().getLevel();
    }

    public boolean userHasRole(String role) {
        return currentUserRolesBusiness.hasRole(role);
    }

    protected void assertStringInputNotNullNotBlank(String value, String inputName, String errorContext) throws VipException {
        if (value == null || value.isBlank()) {
            logger.error("Null or blank input [{}]. Context: {}", inputName, errorContext);
            throw new VipException(DefaultError.BAD_INPUT_FIELD, inputName, errorContext);
        }
    }

    protected void assertInputIsValid(boolean isValid, String inputName, String errorContext) throws VipException {
        if ( ! isValid) {
            logger.error("Invalid input [{}]. Context: {}", inputName, errorContext);
            throw new VipException(DefaultError.BAD_INPUT_FIELD, inputName, errorContext);
        }
    }
}
