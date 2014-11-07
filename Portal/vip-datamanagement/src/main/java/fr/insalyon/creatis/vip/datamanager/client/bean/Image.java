/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard
 */
public class Image implements IsSerializable {
    String imageDir;
    String relativeURL;
    Integer zdim;
    
    public Image(){}

    public Image(String imageDir, Integer zdim, String relativeURL) {
        this.imageDir = imageDir;
        this.zdim = zdim;
        this.relativeURL = relativeURL;
    }

    public String getRelativeURL(){
        return relativeURL;
    }
    
    public String getImageDir() {
        return imageDir;
    }

    public Integer getZdim() {
        return zdim;
    }
    
    
}
