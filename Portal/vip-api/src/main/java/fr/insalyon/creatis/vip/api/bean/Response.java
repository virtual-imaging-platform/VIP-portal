/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.bean;

import fr.insalyon.creatis.vip.api.bean.pairs.PairOfPipelineAndBooleanLists;
import fr.insalyon.creatis.vip.api.bean.pairs.PipelineKeyBooleanValuePair;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
    @XmlElements(value = { 
        @XmlElement(name = "returnedValue", type=Pipeline.class),
        @XmlElement(name = "returnedValue", type=Execution.class),
        @XmlElement(name = "returnedValue", type=GlobalProperties.class),
        @XmlElement(name = "returnedValue", type=String.class),
        @XmlElement(name = "returnedValue", type=Execution.ExecutionStatus.class),
        @XmlElement(name = "returnedValue", type=PipelineKeyBooleanValuePair.class),
        @XmlElement(name = "returnedValue", type=PairOfPipelineAndBooleanLists.class),
    } )         
    java.lang.Object returnedValue; // java.lang.Object so that Strings can be returned to

    public Response() {} // needed for SOAP serialization
    
    public Response(int statusCode, String errorMessage, java.lang.Object returnedValue) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.returnedValue = returnedValue;
    }
    
    
}
