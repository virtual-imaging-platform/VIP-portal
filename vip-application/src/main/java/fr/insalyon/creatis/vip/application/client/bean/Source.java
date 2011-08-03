/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author camarasu
 */
public class Source implements IsSerializable {

    private String name, userLevel, description;

    public Source() {
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public Source(String name, String userLevel, String description) {
        this.name = name;
        this.userLevel = userLevel;
        this.description = description;
    }
}
