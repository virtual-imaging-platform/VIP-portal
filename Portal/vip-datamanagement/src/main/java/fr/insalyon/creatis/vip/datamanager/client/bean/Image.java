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
    String URLSlices;
    Integer zdim;
    
    public Image(){}

    public Image(String URLSlices, Integer zdim) {
        this.URLSlices = URLSlices;
        this.zdim = zdim;
    }

    public String getURLSlices() {
        return URLSlices;
    }

    public Integer getZdim() {
        return zdim;
    }
    
    
}
