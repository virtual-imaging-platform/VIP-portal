package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.core.client.bean.User;
import org.slf4j.Logger;

import java.util.function.Supplier;

public abstract class ApiController {

    protected Supplier<User> currentUserSupplier;

    public ApiController(Supplier<User> currentUserSupplier) {
        this.currentUserSupplier = currentUserSupplier;
    }

    protected User currentUser() {
        return currentUserSupplier.get();
    }

    protected void logMethodInvocation(Logger logger, String methodName, Object... parameters) {
        Object user = currentUser() != null ? currentUser() : "Anonymous";
        logger.info( "({}) Calling API method {}} ({})", user, methodName, parameters);
    }

}
