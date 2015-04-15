/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.Execution;
import fr.insalyon.creatis.vip.api.bean.Execution.ExecutionStatus;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyParameterValuePair;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyValuePair;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Tristan Glatard
 */
public class ExecutionBusiness {
    
    public static Execution getExecution(String executionId) throws ApiException { throw new ApiException("Not implemented yet");}
    public static void updateExecution(String executionId, ArrayList<StringKeyValuePair> values) throws ApiException { throw new ApiException("Not implemented yet");};
    public static String initExecution(String pipelineId, ArrayList<StringKeyParameterValuePair> inputValues, Integer timeoutInSeconds, String executionName, String studyId, Boolean playExecution) throws ApiException { throw new ApiException("Not implemented yet");};;
    public static ExecutionStatus playExecution ( String executionId )throws ApiException { throw new ApiException("Not implemented yet");};
    public static void killExecution ( String executionId ) throws ApiException { throw new ApiException("Not implemented yet");};
    public static void deleteExecution ( String executionId, Boolean deleteFiles ) throws ApiException { throw new ApiException("Not implemented yet");};
    public static ArrayList<URL> getExecutionResults ( String executionId, String protocol ) throws ApiException { throw new ApiException("Not implemented yet");};
    
}
