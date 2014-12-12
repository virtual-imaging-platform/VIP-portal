/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.bean;

import java.util.Map;

/**
 *
 * @author Tristan Glatard
 */
public class User {
    private String identifier;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Map<Study,Study.Role> roles;

    public User(){}

    public User(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.identifier = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUserName() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }
    
}
