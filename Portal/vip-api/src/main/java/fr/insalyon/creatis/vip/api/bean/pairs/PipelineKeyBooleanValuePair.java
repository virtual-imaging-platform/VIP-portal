/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.bean.pairs;

import fr.insalyon.creatis.vip.api.bean.Pipeline;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard
 */
@XmlType(name = "PipelineKeyBooleanValuePair")
public class PipelineKeyBooleanValuePair {

    @XmlElement(name = "key", required = true)
    public Pipeline key;
    @XmlElement(name = "value", required = true)
    public Boolean value;
}
