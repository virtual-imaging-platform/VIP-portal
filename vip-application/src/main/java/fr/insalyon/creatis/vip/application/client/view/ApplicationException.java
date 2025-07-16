package fr.insalyon.creatis.vip.application.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.VipException;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationException extends VipException {

    /* Reserved codes : 2xxx : vip-application */
    public enum ApplicationError implements VipError {
        PLATFORM_MAX_EXECS(2000),
        USER_MAX_EXECS(2001),
        WRONG_APPLICATION_DESCRIPTOR(2002);


        private Integer code;
        ApplicationError(Integer code) { this.code = code; }
        @Override
        public Integer getCode() { return code; }

        static {
            addMessage(ApplicationError.PLATFORM_MAX_EXECS, "Max number of running executions reached on the platform.", 0);
            addMessage(ApplicationError.USER_MAX_EXECS, "Max number of running executions reached.<br />You already have {} running executions.", 1);
            addMessage(ApplicationError.WRONG_APPLICATION_DESCRIPTOR, "Error getting application descriptor for {}.", 1);
        }
    }

    public ApplicationException() {
    }

    public ApplicationException(Throwable thrwbl) {
        super(thrwbl);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(ApplicationError applicationError, Object... params) {
        super(applicationError, params);
    }

    public ApplicationException(ApplicationError applicationError, Throwable cause, Object... params) {
        super(applicationError, cause, params);
    }
}
