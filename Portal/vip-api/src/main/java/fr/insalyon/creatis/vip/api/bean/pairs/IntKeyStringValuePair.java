/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.bean.pairs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard
 */
@XmlType(name = "IntKeyStringValuePair")
public class IntKeyStringValuePair {

    @XmlElement(name = "key", required = true)
    public Integer key;
    @XmlElement(name = "value", required = true)
    public String value;
}
