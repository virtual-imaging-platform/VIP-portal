/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.bean;

import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class Pipeline {
    
    private String identifier; //the LFN of the workflow document in our case
    
    private String name;
    
    private double version;
    
    private List<String> inputs; //not the values, just the inputs
    
    private List<String> outputs; //not the values, just the outputs
    
    private List<Study> studies; // list of studies that can access this pipeline
    
    public Pipeline(){
    }
}
