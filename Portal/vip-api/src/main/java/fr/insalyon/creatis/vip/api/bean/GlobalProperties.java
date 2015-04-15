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
@XmlType(name = "GlobalProperties")
public class GlobalProperties extends Object {

    @XmlElement(name = "APIErrorCodesAndMessages")
    private ArrayList<IntKeyStringValuePair> APIErrorCodesAndMessages;

    @XmlElement(name = "supportedTransferProtocols", required=true)
    private ArrayList<String> supportedTransferProtocols;

    @XmlElement(name = "supportedModules", required=true)
    private ArrayList<Module> supportedModules;
    
    @XmlElement(name = "email")
    private String email;
    
    @XmlElement(name = "platformDescription")
    private String platformDescription;
    
    @XmlElement(name = "minAuthorizedExecutionTimeout", required=true)
    private Integer minAuthorizedExecutionTimeout;
    
    @XmlElement(name = "maxAuthorizedExecutionTimeout", required=true)
    private Integer maxAuthorizedExecutionTimeout;
    
    @XmlElement(name = "defaultExecutionTimeout", required=true)
    private Integer defaultExecutionTimeout;
    
    @XmlElement(name = "isKillExecutionSupported", required=true)
    private Boolean isKillExecutionSupported;
    
    @XmlElement(name = "defaultStudy")
    private String defaultStudy;
    
    @XmlElement(name = "supportedAPIVersion", required=true)
    private String supportedAPIVersion;
    
    public GlobalProperties() {
    }

}
