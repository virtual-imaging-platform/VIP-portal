/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.Service;
import fr.insalyon.creatis.vip.api.bean.Study;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class ServiceBusiness {
    
    public void newService(Service service) throws ApiException {
        throw new ApiException("Not implemented yet");
    }
    
    public void deleteService(String serviceIdentifier) throws ApiException {
        throw new ApiException("Not implemented yet");
    }
    
    public List<Service> getServices() throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public List<Service> getServicesOfStudy(String studyIdentifier) throws ApiException{
        throw new ApiException("Not implemented yet");
    }
    
    public Service getService(String serviceIdentifier) throws ApiException{
        throw new ApiException("Not implemented yet");
    }
}
