/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.datamanager.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Tristan Glatard
 */
public class VisualizationItem implements IsSerializable{
    String URL;
    String localPath;
    
    public VisualizationItem(){}

    public VisualizationItem(String URL, String localPath) {
        this.URL = URL;
        this.localPath = localPath;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    
    
}
