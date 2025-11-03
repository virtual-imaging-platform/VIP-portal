package fr.insalyon.creatis.vip.datamanager.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.VipException;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerException extends VipException implements IsSerializable {

    public DataManagerException() {
    }

    public DataManagerException(String message) {
        super(message);
    }

    public DataManagerException(Throwable thrwbl) {
        super(thrwbl);
    }
}
