/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.bean;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Tristan Glatard
 */
public class PipelineExecution {
    
    private String identifier;
    
    private Pipeline pipeline;
    
    private User owner;
    
    private List <ParameterValue> inputValues;
    
    private List <ParameterValue> outputValues;
    
    private Status status;
    
    // Does a pipeline execution belong to a Study?
    
    

    /**
     *
     * @author Tristan Glatard
     */
    public class ParameterValue {
        private String value;
    }
    
    public enum Status {
    NEW, QUEUED, RUNNING, COMPLETED, FAILED; // ??? has to be dicussed. In a first step, we should limit the number of statuses
    }
}
