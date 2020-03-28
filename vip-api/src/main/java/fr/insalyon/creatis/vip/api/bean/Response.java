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

import fr.insalyon.creatis.vip.api.bean.pairs.PairOfPipelineAndBooleanLists;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Tristan Glatard
 */
@XmlSeeAlso({Object.class, Execution.class, GlobalProperties.class, Pipeline.class, String[].class, Pipeline[].class, Execution[].class})
@XmlType(name = "Response")
public class Response {

    @XmlElement(name = "statusCode", required = true)
    int statusCode;
    @XmlElement(name = "errorMessage")
    String errorMessage;
    @XmlElements(value = {
        @XmlElement(name = "returnedValuePipeline", type = Pipeline.class),
        @XmlElement(name = "returnedValueListPipelines", type = Pipeline[].class),
        @XmlElement(name = "returnedValueListExecutions", type = Execution[].class),
        @XmlElement(name = "returnedValueExecution", type = Execution.class),
        @XmlElement(name = "returnedValueGlobalProp", type = GlobalProperties.class),
        @XmlElement(name = "returnedValueStr", type = String.class),
        @XmlElement(name = "returnedValueListStrings", type = String[].class),
        @XmlElement(name = "returnedValueStatus", type = ExecutionStatus.class),
        @XmlElement(name = "returnedValuePairPipeline", type = PairOfPipelineAndBooleanLists.class),})
    java.lang.Object returnedValue; // java.lang.Object so that Strings can be returned too

    public Response() {
    } // needed for SOAP serialization

    public Response(int statusCode, String errorMessage, java.lang.Object returnedValue) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.returnedValue = returnedValue;
    }

}
