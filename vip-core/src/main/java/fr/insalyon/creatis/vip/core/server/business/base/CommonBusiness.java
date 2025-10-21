package fr.insalyon.creatis.vip.core.server.business.base;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;

public abstract class CommonBusiness {

    protected CorePermissions permissions;
    protected Supplier<User> userSupplier;
    protected ConfigurationBusiness configurationBusiness;

    @Autowired
    public void setUserSupplier(Supplier<User> userSupplier) {
        this.userSupplier = userSupplier;
    }

    @Autowired
    public void setConfigurationBusiness(ConfigurationBusiness configurationBusiness) {
        this.configurationBusiness = configurationBusiness;
    }

    @Autowired
    public void setCorePermissions(CorePermissions corePermissions) {
        this.permissions = corePermissions;
    }

    public User getUser() {
        return userSupplier.get();
    }

    public UserLevel getUserLevel() {
        return getUser().getLevel();
    }
}
