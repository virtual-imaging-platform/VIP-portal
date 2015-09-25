/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard, Nouha Boujelben
 */
public class SSH implements IsSerializable {
    private String email;
    private String name;
    private String user;
    private String host;
    private String port;
    private String transfertType;

    private String directory;
    private String status;


    public SSH() {
    }

    public SSH(String email, String name, String user, String host, String port, String transfertType,String  directory , String status) {
        this.email = email;
        this.name = name;
        this.user = user;
        this.host = host;
        this.port = port;
        this.transfertType=transfertType;
        this.directory = directory;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDirectory() {
        return directory;
    }
    
    public String getTransfertType() {
        return transfertType;
    }

    public String getStatus() {
        return status;
    }
    
    public String getEmail(){
        return email;
    }
    
            
    
}
