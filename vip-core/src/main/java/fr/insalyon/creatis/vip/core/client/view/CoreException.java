package fr.insalyon.creatis.vip.core.client.view;

import fr.insalyon.creatis.vip.core.client.VipException;

public class CoreException extends VipException {

    public CoreException() { }

    public CoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreException(Throwable thrwbl) {
        super(thrwbl);
    }

    public CoreException(String message) {
        super(message);
    }
}
