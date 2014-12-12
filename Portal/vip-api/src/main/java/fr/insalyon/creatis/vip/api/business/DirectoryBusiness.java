/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.Directory;
import fr.insalyon.creatis.vip.api.bean.Study;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class DirectoryBusiness {
    public void newDirectory(Directory directory) throws ApiException {
        throw new ApiException("Not implemented yet");
    }
    
    public void deleteDirectory(String directoryIdentifier) throws ApiException {
        throw new ApiException("Not implemented yet");
    }
    
    public Directory getDirectory(String directoryIdentifier) throws ApiException {
        throw new ApiException("Not implemented yet");
    }
    
    public List<Directory> getDirectories(String studyIdentifier) throws ApiException{
        throw new ApiException("Not implemented yet");
    }
    
    public void downloadDirectory(String directoryIdentifier) throws ApiException{throw new ApiException("Not implemented yet");}
    
}
