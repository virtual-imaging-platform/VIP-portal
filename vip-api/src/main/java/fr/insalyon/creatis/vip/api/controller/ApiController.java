package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.core.client.bean.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.function.Supplier;

public abstract class ApiController {

    protected Supplier<User> currentUserSupplier;
    protected Environment env;

    @Autowired
    public void setUpDependencies(Supplier<User> currentUserSupplier, Environment env) {
        this.currentUserSupplier = currentUserSupplier;
        this.env = env;
    }

    protected User currentUser() {
        return currentUserSupplier.get();
    }

    protected void logMethodInvocation(Logger logger, String methodName, Object... parameters) {
        Object user = currentUser() != null ? currentUser() : "Anonymous";
        logger.debug( "({}) Calling API method {}} ({})", user, methodName, parameters);
    }

}
