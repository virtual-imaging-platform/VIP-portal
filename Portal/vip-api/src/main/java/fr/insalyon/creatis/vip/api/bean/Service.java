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
public class Service {
    private String identifier; //the LFN of the workflow document in our case
    private String name;
    private double version;
    private List<Input> inputs; //not the values, just the inputs
    private List<Output> outputs; //not the values, just the outputs
    private List<Study> studies; // list of studies that can access this service
    public Service(){
    }

    /**
     *
     * @author glatard
     */
    public static enum Type {
        STRING, FILE
    }

    /**
     *
     * @author Tristan Glatard
     */
    public class Output {

        private String name;
        private Type type;
    }

    /**
     *
     * @author Tristan Glatard
     */
    public class Input {

        private String name;
        private Type type;
    }
}
