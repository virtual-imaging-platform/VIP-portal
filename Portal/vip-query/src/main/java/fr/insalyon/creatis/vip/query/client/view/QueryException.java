/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Boujelben
 */
public class QueryException extends Exception implements IsSerializable {

    public QueryException() {
    }

    public QueryException(Throwable thrwbl) {
        super(thrwbl);
    }

    public QueryException(String message) {
        super(message);
    }
}
