package fr.insalyon.creatis.vip.core.server.dao;

import fr.insalyon.creatis.vip.core.client.VipException;

public class DAOException extends VipException {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable thrwbl) {
        super(thrwbl);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}