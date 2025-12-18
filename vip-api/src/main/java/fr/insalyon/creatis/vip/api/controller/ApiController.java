package fr.insalyon.creatis.vip.api.controller;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.business.Server;

public abstract class ApiController {

    protected Supplier<User> currentUserSupplier;
    protected Server server;

    @Autowired
    public void setUpDependencies(Supplier<User> currentUserSupplier, Server server) {
        this.currentUserSupplier = currentUserSupplier;
        this.server = server;
    }

    protected User currentUser() {
        return currentUserSupplier.get();
    }

    protected void logMethodInvocation(Logger logger, String methodName, Object... parameters) {
        Object user = currentUser() != null ? currentUser() : "Anonymous";
        logger.debug( "({}) Calling API method {}} ({})", user, methodName, parameters);
    }

}
