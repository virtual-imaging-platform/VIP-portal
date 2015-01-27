/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.bean;

/**
 *
 * @author Tristan Glatard
 */
public class Path {
    
    String identifier;
   
    // Attributes
    int size;
    String absolutePath; //by convention, the first part of the path is the name of a study. By convention, subdirectories must exist.
    String lastModification; //a string representing a date
        // rights on the path are handled in the study.
    
    public Path(){}
   
    // Getters and setters
    // ...
}
