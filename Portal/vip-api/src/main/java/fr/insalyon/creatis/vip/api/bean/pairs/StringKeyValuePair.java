/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.bean.pairs;

import fr.insalyon.creatis.vip.api.bean.Execution;
import fr.insalyon.creatis.vip.api.bean.ParameterType;
import fr.insalyon.creatis.vip.api.bean.ParameterTypedValue;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard
 */
@XmlType(name = "StringKeyValuePair")
public class StringKeyValuePair {
    @XmlElement(name = "key", required = true)
    public String key;   
    @XmlElements(value = { 
        @XmlElement(name = "value", type=String.class, required=true),
        @XmlElement(name = "value", type=int.class, required=true),
        @XmlElement(name = "value", type=Execution.ExecutionStatus.class, required=true),
        @XmlElement(name = "value", type=ParameterTypedValue.class, required=true)
    } )
    public java.lang.Object value;
}
