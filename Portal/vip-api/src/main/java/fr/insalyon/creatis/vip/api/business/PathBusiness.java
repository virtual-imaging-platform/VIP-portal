/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.Path;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class PathBusiness {

    // General 
    
    public void newPath(Path path) throws ApiException { throw new ApiException("Not implemented"); } //mkdir or "touch" 
    
    public void updatePath(Path path) throws ApiException { throw new ApiException("Not implemented"); } // e.g. mv
    
    public void deletePath(String pathIdentifier) throws ApiException{ throw new ApiException("Not implemented"); }
    
    public Path getPath(String pathIdentifier /* or absolute path? */) throws ApiException{ throw new ApiException("Not implemented"); } // get attributes of Path
    
    public List<Path> getPaths() throws ApiException { throw new ApiException("Not implemented");} // lists all paths accessible by user
    
    // Specific to Path
    
    public List<Path> getPaths(String pathIdentifier) throws ApiException { throw new ApiException("Not implemented");} // lists Paths in directory
    
    public String downloadPath(String fileIdentifier, String protocol) throws ApiException{ throw new ApiException("Not implemented"); } // returns a URL where the Path can be accessed. SFTP  must be supported.
    
    public void uploadPath(Path path, String url) throws ApiException{ throw new ApiException("Not implemented"); }
      
    public boolean isDirectory(Path path) throws ApiException { throw new ApiException("Not implemented"); }
    
    public boolean isFile(Path path) throws ApiException { throw new ApiException("Not implemented"); }
    
}
