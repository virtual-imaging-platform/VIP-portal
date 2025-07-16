package fr.insalyon.creatis.vip.applicationimporter.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.client.VipException;

/**
 *
 * @author Nouha Boujelben
 */
public class ApplicationImporterException extends VipException implements IsSerializable {

    public ApplicationImporterException() {
    }

    public ApplicationImporterException(String message) {
        super(message);
    }

    public ApplicationImporterException(Throwable thrwbl) {
        super(thrwbl);
    }

    public ApplicationImporterException(String message, Throwable parent) {
        super(message, parent);
    }
}
