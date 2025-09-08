package fr.insalyon.creatis.vip.gatelab.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.VipException;

/**
 *
 * @author Rafael Silva
 */
public class GateLabException extends VipException implements IsSerializable {

    public GateLabException() {
    }

    public GateLabException(String string) {
        super(string);
    }

    public GateLabException(Throwable thrwbl) {
        super(thrwbl);
    }
}
