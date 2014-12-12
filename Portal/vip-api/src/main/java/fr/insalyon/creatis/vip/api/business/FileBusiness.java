/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.File;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class FileBusiness {
    public void newFile(File file) throws ApiException { throw new ApiException("Not implemented"); } // HTTP upload
    
    public void deleteFile(String fileIdentifier) throws ApiException{ throw new ApiException("Not implemented"); }
    
    public List<File> getFilesOfDirectory(String directoryIdentifier) throws ApiException{ throw new ApiException("Not implemented"); }
    
    public List<File> getFilesOfStudy(String studyIdentifier) throws ApiException{ throw new ApiException("Not implemented"); } // doesn't return the directories
    
    public List<File> getFiles() throws ApiException { throw new ApiException("Not implemented");}
    
    public File getFile(String fileIdentifier) throws ApiException{ throw new ApiException("Not implemented"); } 
    
    public void downloadFile(String fileIdentifier) throws ApiException{ throw new ApiException("Not implemented"); }
    
    //move file to another directory?
}
