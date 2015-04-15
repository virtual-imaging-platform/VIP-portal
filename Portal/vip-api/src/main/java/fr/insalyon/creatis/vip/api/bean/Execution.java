/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.bean;

import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyParameterValuePair;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard
 */
@XmlType(name = "Execution")
public class Execution extends Object {

    // mandatory arguments
    @XmlElement(name = "pipelineIdentifier", required = true)
    private String pipelineIdentifier;
    @XmlElement(name = "timeout", required = true)
    int timeout;
    @XmlElement(name = "status", required = true)
    ExecutionStatus status;
    @XmlElement(name = "inputValues", required = true)
    ArrayList<StringKeyParameterValuePair> inputValues; // TODO minOccur shouldn't be 0;
    @XmlElement(name = "returnedFiles", required = true)
    ArrayList<StringKeyParameterValuePair> returnedFiles;

    // optional arguments
    @XmlElement(name = "studyIdentifier")
    String studyIdentifier;
    @XmlElement(name = "errorCode")
    Integer errorCode;
    @XmlElement(name = "stdout")
    String stdout;
    @XmlElement(name = "stderr")
    String stderr;
    @XmlElement(name = "startDate")
    Integer startDate;
    @XmlElement(name = "endDate")
    Integer endDate;

    @XmlType(name = "ExecutionStatus")
    public static enum ExecutionStatus {

        Initializing, Ready, Running, Finished, InitializationFailed, ExecutionFailed, Unknown, Killed
    }
    
    public Execution() {
    }

}
