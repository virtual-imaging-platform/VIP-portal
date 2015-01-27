/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.User;
import java.util.List;

/**
 *
 * @author Tristan Glatard
 */
public class UserBusiness {
    
    // General 
    
    public void newUser(User user) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public void updateUser(User user) throws ApiException { throw new ApiException("Not implemented yet"); } //??? discuss update methods in general
    
    public void deleteUser(String userIdentifier) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public User getUser(String userIdentifier) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public List<User> getUsers() throws ApiException { throw new ApiException("Not implemented yet"); }
    
    // Specific
    
    public List<User> getUsersOfStudy(String studyIdentifier) throws ApiException { throw new ApiException("Not implemented yet");}
}
