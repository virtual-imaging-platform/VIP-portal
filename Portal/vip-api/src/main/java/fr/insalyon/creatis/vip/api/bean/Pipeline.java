/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.bean;

import fr.insalyon.creatis.vip.api.bean.pairs.IntKeyStringValuePair;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard
 */
@XmlType(name = "Pipeline")
public class Pipeline extends Object {
    
    @XmlElement(name = "identifier", required=true)
    private String identifier;
    @XmlElement(name = "name", required=true)
    private String name;
    @XmlElement(name = "description", required = true)
    private String description;
    @XmlElement(name = "version", required = true)
    private String version;
    @XmlElement(name = "parameters", required = true)
    ArrayList<PipelineParameter> parameters;
    @XmlElement(name = "errorCodesAndMessages")
    private ArrayList<IntKeyStringValuePair> errorCodesAndMessages;

    public Pipeline() {
    }
}
