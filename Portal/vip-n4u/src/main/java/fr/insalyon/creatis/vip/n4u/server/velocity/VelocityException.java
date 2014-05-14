/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.server.velocity;

/**
 *
 * @author Nouha Boujelben
 */
public class VelocityException extends Exception {
/**
 * 
 * @param message 
 */
    public VelocityException(String message) {
        super(message);
    }
/**
 * 
 * @param cause 
 */
    public VelocityException(Throwable cause) {
        super(cause);
    }
}
