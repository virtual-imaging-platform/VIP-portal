package fr.insalyon.creatis.vip.core.server.business.base;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;

@Service
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

}
