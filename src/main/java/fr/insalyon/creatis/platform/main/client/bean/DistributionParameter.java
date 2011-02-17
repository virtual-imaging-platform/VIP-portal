/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.platform.main.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author glatard
 */
public class DistributionParameter implements IsSerializable {

    private String name;
    private String symbol;

    public DistributionParameter() {
    }

    public DistributionParameter(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
