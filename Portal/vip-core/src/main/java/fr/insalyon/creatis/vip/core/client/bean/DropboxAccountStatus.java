/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard
 */
public class DropboxAccountStatus implements IsSerializable {
    public enum AccountStatus{
        UNLINKED,
        UNCONFIRMED,
        AUTHENTICATION_FAILED,
        OK
    }
   
    
}
