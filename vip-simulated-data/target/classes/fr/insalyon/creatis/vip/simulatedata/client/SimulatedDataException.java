/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulatedata.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard
 */
public class SimulatedDataException extends Exception implements IsSerializable  {
     public SimulatedDataException() {
    }

    public SimulatedDataException(Throwable thrwbl) {
        super(thrwbl);
    }

    public SimulatedDataException(String message) {
        super(message);
    }
}
