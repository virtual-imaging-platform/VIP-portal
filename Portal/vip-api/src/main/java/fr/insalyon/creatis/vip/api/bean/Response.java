/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard
 */
@XmlSeeAlso({Object.class, Execution.class, GlobalProperties.class, Pipeline.class})
@XmlType(name = "Response")
public class Response {
    @XmlElement(name = "statusCode", required=true)
    int statusCode;
    @XmlElement(name = "errorMessage")
    String errorMessage;
    @XmlElement(name = "returnedValue")
    java.lang.Object returnedValue; // java.lang.Object so that Strings can be returned to

    public Response() {} // needed for SOAP serialization
    
    public Response(int statusCode, String errorMessage, java.lang.Object returnedValue) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.returnedValue = returnedValue;
    }
    
    
}
