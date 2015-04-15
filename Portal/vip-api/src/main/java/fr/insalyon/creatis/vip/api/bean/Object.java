/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 

package fr.insalyon.creatis.vip.api.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard
 */
@XmlType(name = "Object")
public class Object {
    @XmlElement(name = "identifier", required=true)
    private String identifier;
    @XmlElement(name = "name", required=true)
    private String name;
    public Object() {
    }

}
