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
@XmlType(name = "PipelineParameter")
public class PipelineParameter {

    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "type", required = true)
    private ParameterType type;
    @XmlElement(name = "isOptional", required = true)
    private Boolean isOptional;
    @XmlElement(name = "isReturnedValue", required = true)
    private Boolean isReturnedValue;
    @XmlElement(name = "defaultValue")
    private ParameterTypedValue defaultValue;
    @XmlElement(name = "description")
    private String description;
}
