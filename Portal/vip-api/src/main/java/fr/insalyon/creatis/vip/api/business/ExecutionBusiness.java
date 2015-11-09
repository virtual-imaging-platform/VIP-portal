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
import fr.insalyon.creatis.vip.api.bean.ParameterTypedValue;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyParameterValuePair;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyValuePair;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.SimulationBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Tristan Glatard
 */
public class ExecutionBusiness extends AuthenticatedApiBusiness {
    
    public ExecutionBusiness(WebServiceContext wsContext) throws ApiException {
        super(wsContext);
    }
    
    public Execution getExecution(String executionId) throws ApiException { 
        try {
            WorkflowBusiness wb = new WorkflowBusiness();
            SimulationBusiness sb = new SimulationBusiness();
            
            // Get main execution object
            Simulation s = wb.getSimulation(executionId);
            if(!s.getUserName().equals(getUser().getEmail()) && !getUser().isSystemAdministrator())
                throw new ApiException("Execution id '"+executionId+"' is not available or user '"+getUser().getEmail()+"' cannot access it.");
            
            // Retrieve stdout and stderr.
            // Would be nice if stdout and stderr are requested through another call since reading these files can be costly.
            String stdout = sb.readFile(s.getID(), "", "workflow", ".out");
            String stderr = sb.readFile(s.getID(), "", "workflow", ".err");
            
            // Build Carmin's execution object
            Execution e =  new Execution(
                    s.getID(),
                    s.getSimulationName(), 
                    PipelineBusiness.getPipelineIdentifier(s.getApplicationName(), s.getApplicationVersion()),
                    0, // timeout (no timeout set in VIP)
                    VIPtoCarminStatus(s.getStatus()),
                    null, // study identifier (not available in VIP yet)
                    null, // error codes and mesasges (not available in VIP yet)
                    stdout,
                    stderr,
                    s.getDate().getTime(),
                    null // last status modification date (not available in VIP yet)
            );
            
            // Inputs
            List<InOutData> inputs = wb.getInputData(executionId, getUser().getFolder());
            for(InOutData iod : inputs){
                ParameterTypedValue value = new ParameterTypedValue(PipelineBusiness.getCarminType(iod.getType()),iod.getPath());
                StringKeyParameterValuePair skpv = new StringKeyParameterValuePair(iod.getProcessor(),value);
                e.getInputValues().add(skpv);
            }
            
            // Outputs
            List<InOutData> outputs = wb.getOutputData(executionId, getUser().getFolder());
            for(InOutData iod : outputs){
                ParameterTypedValue value = new ParameterTypedValue(PipelineBusiness.getCarminType(iod.getType()),iod.getPath());
                StringKeyParameterValuePair skpv = new StringKeyParameterValuePair(iod.getProcessor(),value);
                e.getReturnedFiles().add(skpv);
            }
            
            return e;
            
        } catch (BusinessException ex) {
            throw new ApiException(ex);
        }
        
    }
    public void updateExecution(String executionId, ArrayList<StringKeyValuePair> values) throws ApiException { throw new ApiException("Not implemented yet");};
    public String initExecution(String pipelineId, ArrayList<StringKeyParameterValuePair> inputValues, Integer timeoutInSeconds, String executionName, String studyId, Boolean playExecution) throws ApiException { throw new ApiException("Not implemented yet");};;
    public ExecutionStatus playExecution ( String executionId )throws ApiException { throw new ApiException("Not implemented yet");};
    public void killExecution ( String executionId ) throws ApiException { throw new ApiException("Not implemented yet");};
    public void deleteExecution ( String executionId, Boolean deleteFiles ) throws ApiException { throw new ApiException("Not implemented yet");};
    public ArrayList<URL> getExecutionResults ( String executionId, String protocol ) throws ApiException { throw new ApiException("Not implemented yet");};
    
    public static ExecutionStatus VIPtoCarminStatus(SimulationStatus s){
        
        switch(s){
            case Running:
                return ExecutionStatus.Running;
            case Completed:
                return ExecutionStatus.Finished;
            case Killed:
                return ExecutionStatus.Killed;
            case Cleaned:
                return ExecutionStatus.Unknown;
            case Queued:
                return ExecutionStatus.Ready;
            case Unknown:
                return ExecutionStatus.Unknown;
            default:
                return ExecutionStatus.Unknown;
        }
    }
    
}

