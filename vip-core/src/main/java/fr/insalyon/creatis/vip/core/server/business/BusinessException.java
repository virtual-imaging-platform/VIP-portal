package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.vip.core.client.VipException;

/**
 *
 * @author Rafael Silva
 */

public class BusinessException extends VipException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable thrwbl) {
        super(thrwbl);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(VipError vipError, Object... params) {
        super(vipError, params);
    }

    public BusinessException(VipError vipError, Throwable cause, Object... params) {
        super(vipError, cause, params);
    }

    public BusinessException(String message, VipError vipError) {
        super(message, vipError);
    }
}
