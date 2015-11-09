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
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.Execution;
import fr.insalyon.creatis.vip.api.bean.Execution.ExecutionStatus;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyParameterValuePair;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyValuePair;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Tristan Glatard
 */
public class ExecutionBusiness extends AuthenticatedApiBusiness {
    
    public ExecutionBusiness(WebServiceContext wsContext) throws ApiException {
        super(wsContext);
    }
    
    public Execution getExecution(String executionId) throws ApiException { throw new ApiException("Not implemented yet");}
    public void updateExecution(String executionId, ArrayList<StringKeyValuePair> values) throws ApiException { throw new ApiException("Not implemented yet");};
    public String initExecution(String pipelineId, ArrayList<StringKeyParameterValuePair> inputValues, Integer timeoutInSeconds, String executionName, String studyId, Boolean playExecution) throws ApiException { throw new ApiException("Not implemented yet");};;
    public ExecutionStatus playExecution ( String executionId )throws ApiException { throw new ApiException("Not implemented yet");};
    public void killExecution ( String executionId ) throws ApiException { throw new ApiException("Not implemented yet");};
    public void deleteExecution ( String executionId, Boolean deleteFiles ) throws ApiException { throw new ApiException("Not implemented yet");};
    public ArrayList<URL> getExecutionResults ( String executionId, String protocol ) throws ApiException { throw new ApiException("Not implemented yet");};
    
}
