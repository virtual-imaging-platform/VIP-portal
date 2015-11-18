/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
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
    @XmlElement(name = "identifier", required=true)
    private String identifier;
    @XmlElement(name = "name", required=true)
    private String name;
    @XmlElement(name = "pipelineIdentifier", required = true)
    private String pipelineIdentifier;
    @XmlElement(name = "timeout")
    int timeout;
    @XmlElement(name = "status", required = true)
    ExecutionStatus status;
    @XmlElement(name = "inputValue", required = true)
    ArrayList<StringKeyParameterValuePair> inputValues; // TODO minOccur shouldn't be 0;
    @XmlElement(name = "returnedFile")
    ArrayList<StringKeyParameterValuePair> returnedFiles;

    // optional arguments
    @XmlElement(name = "studyIdentifier")
    String studyIdentifier;
    @XmlElement(name = "errorCode")
    Integer errorCode;
    @XmlElement(name = "startDate")
    Long startDate;
    @XmlElement(name = "endDate")
    Long endDate;

    @XmlType(name = "ExecutionStatus")
    public static enum ExecutionStatus {
        Initializing, Ready, Running, Finished, InitializationFailed, ExecutionFailed, Unknown, Killed
    }
    
    public Execution() {
        inputValues = new ArrayList<>();
        returnedFiles = new ArrayList<>();
    }

    public Execution(String identifier,
                     String name,
                     String pipelineIdentifier,
                     int timeout,
                     ExecutionStatus status,
                     String studyIdentifier,
                     Integer errorCode,
                     Long startDate,
                     Long endDate) {
        this();
        this.identifier = identifier;
        this.name = name == null ? identifier : name; // null names sometimes happen due to a race condition in VIP.
        this.pipelineIdentifier = pipelineIdentifier;
        this.timeout = timeout;
        this.status = status;
        this.studyIdentifier = studyIdentifier;
        this.errorCode = errorCode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getPipelineIdentifier() {
        return pipelineIdentifier;
    }

    public int getTimeout() {
        return timeout;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public ArrayList<StringKeyParameterValuePair> getInputValues() {
        return inputValues;
    }

    public ArrayList<StringKeyParameterValuePair> getReturnedFiles() {
        return returnedFiles;
    }

    public String getStudyIdentifier() {
        return studyIdentifier;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getEndDate() {
        return endDate;
    }
    
    public void clearReturnedFiles() {
        returnedFiles = null;
    }

}
