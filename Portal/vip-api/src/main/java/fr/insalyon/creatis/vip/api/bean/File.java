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
public class File {
    String identifier;// do we want file identifiers?
    String relativePath; //relative path in the study
    Study study; // can a file be registered in several studies? I hope not...
    String localPath;//local path, used for upload
    // user?
    // directory?
    
    public File(){}

    public String getRelativePath() {
        return this.relativePath;
    }

    public Study getStudy() {
        return study;
    }
}
