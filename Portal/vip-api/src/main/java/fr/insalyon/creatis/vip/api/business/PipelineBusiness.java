/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.Pipeline;
import fr.insalyon.creatis.vip.api.bean.Study;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class PipelineBusiness {
    
    // General
    
    public void newPipeline(Pipeline pipeline) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public void updatePipeline(Pipeline pipeline) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public void deletePipeline(String pipelineIdentifier) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public Pipeline getPipeline(String serviceIdentifier) throws ApiException { throw new ApiException("Not implemented yet"); } 
    
    public List<Pipeline> getPipelines() throws ApiException { throw new ApiException("Not implemented yet"); } // returns all the pipelines that the user can access, regardless of the study
    
    // Specific to Pipeline
    
    public List<Pipeline> getPipelinesOfStudy(String studyIdentifier) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    
    
    
}
