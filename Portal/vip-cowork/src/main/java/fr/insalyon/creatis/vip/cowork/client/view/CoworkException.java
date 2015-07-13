/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.cowork.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard
 */
public class CoworkException  extends Exception implements IsSerializable {

    public CoworkException() {
    }

    public CoworkException(String message) {
        super(message);
    }

    public CoworkException(Throwable thrwbl) {
        super(thrwbl);
    }
    
}
