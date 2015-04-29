/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.applicationimporter.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Nouha Boujelben
 */
public class ApplicationImporterException extends Exception implements IsSerializable {

    public ApplicationImporterException() {
    }

    public ApplicationImporterException(String message) {
        super(message);
    }

    public ApplicationImporterException(Throwable thrwbl) {
        super(thrwbl);
    }
}
