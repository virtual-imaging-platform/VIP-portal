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
@XmlType(name = "StringKeyValuePair")
public class StringKeyValuePair {
    @XmlElement(name = "key", required = true)
    public String key;
    @XmlElement(name = "value", required = true)
    public java.lang.Object value;
}
