package fr.insalyon.creatis.vip.social.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.VipException;

/**
 *
 * @author Rafael Silva
 */
public class SocialException extends VipException implements IsSerializable {

    public SocialException() {
    }

    public SocialException(String string) {
        super(string);
    }

    public SocialException(Throwable thrwbl) {
        super(thrwbl);
    }
}
