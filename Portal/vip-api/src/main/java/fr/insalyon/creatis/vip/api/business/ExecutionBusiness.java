/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.PipelineExecution;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class PipelineExecutionBusiness {
    
    // General 
    
    public void newPipelineExecution(PipelineExecution pe) throws ApiException { throw new ApiException("Not implemented yet");}
    
    public void updatePipelineExecution(PipelineExecution pe) throws ApiException { throw new ApiException("Not implemented yet");}
    
    public void deletePipelineExecution(String peIdentifier) throws ApiException { throw new ApiException("Not implemented yet");} // i.e. clean
    
    public PipelineExecution getPipelineExecution(String peIdentifier) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public List<PipelineExecution> getPipelineExecutions() throws ApiException { throw new ApiException("Not implemented yet"); } // return all the pipeline executions accessible by the current user
    
    // Specific
    
    public void killPipelineExecution(String jobIdentifier) throws ApiException { throw new ApiException("Not implemented yet");}
    
    public List<PipelineExecution> getPipelineExecutionsOfPipeline(String peIdentifier) throws ApiException { throw new ApiException("Not implemented yet");}
    
    public List<PipelineExecution> getPipelineExecutionsOfUser(String userIdentifier) throws ApiException { throw new ApiException("Not implemented yet");}
}
