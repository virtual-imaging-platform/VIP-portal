/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Nouha Boujelben
 */
public class N4uException extends Exception implements IsSerializable {

    public N4uException() {
    }

    public N4uException(String message) {
        super(message);
    }

    public N4uException(Throwable thrwbl) {
        super(thrwbl);
    }
}
