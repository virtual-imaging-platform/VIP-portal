/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.n4u.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author nouha
 */
public enum EnumTypes implements IsSerializable {
    String("string"),
    Text("text"),
    Integer("integer"),
    Float("float"),
    Double("double"),
    Combo("combo"),
    File("file"),
    CheckBox("checkbox"),
    Radio("radio");
    
    private String type;

    private EnumTypes() {
    }

    private EnumTypes(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}
