/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server;

/**
 *
 * @author nouha
 */
public class N4uException extends Exception {

    public N4uException(String message) {
        super(message);
    }

    public N4uException(Throwable thrwbl) {
        super(thrwbl);
    }
}
