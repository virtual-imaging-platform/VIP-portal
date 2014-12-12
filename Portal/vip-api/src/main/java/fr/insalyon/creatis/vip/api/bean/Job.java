/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.bean;

import java.util.Map;

/**
 *
 * @author Tristan Glatard
 */
public class Job {
    private String identifier;
    private Service service;
    private User owner;
    private Map<Service.Input, ParameterValue> inputValues;
    private Map<Service.Output, ParameterValue> outputValues;
    private Status status;
    // does a job belong to a study?

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
