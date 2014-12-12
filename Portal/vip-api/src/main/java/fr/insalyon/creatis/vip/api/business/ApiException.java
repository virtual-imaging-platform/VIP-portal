/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.business;

/**
 *
 * @author Tristan Glatard
 */
public class ApiException extends Exception {

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable thrwbl) {
        super(thrwbl);
    }
}
