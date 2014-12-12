/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.api.bean.Service;
import fr.insalyon.creatis.vip.api.bean.Study;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.rpc.ConfigurationServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard
 */
public class StudyBusiness {

    private static final Logger logger = Logger.getLogger(StudyBusiness.class);
    private final ConfigurationServiceImpl csi;

    public StudyBusiness() {
        csi = new ConfigurationServiceImpl();
    }

    public static Logger getLogger() {
        return logger;
    }

    public ConfigurationServiceImpl getCsi() {
        return csi;
    }

    //////////////////////////////////////// API methods /////////////////////////////////////
    public void newStudy(Study study) throws ApiException {
        throw new ApiException("Not implemented yet");
    }
    
    public void deleteStudy(String studyIdentifier) throws ApiException {
        throw new ApiException("Not implemented yet");
    }
    
    public Study getStudy(String studyIdentifier) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public List<Study> getStudies() throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public List<Study> getStudiesOfUser(String userIdentifier) throws ApiException { throw new ApiException("Not implemented yet"); }
    
    public List<Study> getStudiesOfService(String serviceIdentifier) throws ApiException { throw new ApiException("Not implemented yet;");}
    /// user-study 
    
    public void setRoleInStudies(List<String> studyIdentifier, List<String> userIdentifiers, Study.Role role) throws ApiException{ throw new ApiException("Not implemented yet"); }
    
    public void removeUsersFromStudies(List<String> studyIdentifier, List<String> userIdentifiers) throws ApiException { throw new ApiException("Not implemented yet."); }
       
    /// service-study
    
    public void addServicesToStudies(List<String> studyIdentifier, List<String> serviceIdentifiers) throws ApiException { throw new ApiException ("Not implemented yet");}

    public void removeServicesFromStudies(List<String> studyIdentifier, List<String> serviceIdentifiers) throws ApiException { throw new ApiException("Not implemented yet");}
    
    //////////////////////////////// Private methods ////////////////////////////
    
    private static User getUser(HttpServletRequest req) throws CoreException {
        return (User) req.getSession().getAttribute(CoreConstants.SESSION_USER);
    }

    private static boolean isInMap(Map<Group, CoreConstants.GROUP_ROLE> userGroups, Group group) {
        for (Group mapGroup : userGroups.keySet()) {
            if (mapGroup.getName().equals(group.getName())) {
                return true;
            }
        }
        return false;
    }

    
    /// temp implementation
//     public List<Study> getStudies(HttpServletRequest req) throws ApiException {
//
//        try {
//            // get user from request
//            getCsi().setSession(req.getSession());
//            User user = getUser(req);
//
//            //build result list
//            ArrayList<Study> studies = new ArrayList<Study>();
//            List<Group> publicGroups;
//
//            publicGroups = getCsi().getPublicGroups();
//
//            Map<Group, CoreConstants.GROUP_ROLE> userGroups = null;
//
//            if (user != null) { // i.e. user is authenticated
//                userGroups = getCsi().getUserGroups(null);
//                for (Group group : userGroups.keySet()) {
//                    studies.add(new Study(group.getName(), group.isPublicGroup(), group.isGridFile(), group.isGridJobs(), userGroups.get(group)));
//                }
//            }
//
//            // user may not be authenticated.
//            for (Group group : publicGroups) {
//                if (user == null || !isInMap(userGroups, group)) {
//                    studies.add(new Study(group.getName(), group.isPublicGroup(), group.isGridFile(), group.isGridJobs(), CoreConstants.GROUP_ROLE.None));
//                }
//            }
//
//            return studies;
//        } catch (CoreException ex) {
//            throw new ApiException(ex);
//        }
//    }
//
//    public Study getStudy(Study study, HttpServletRequest req) throws ApiException {
//        try {
//            // get user from request
//            getCsi().setSession(req.getSession());
//            User user = getUser(req);
//            Map<Group, CoreConstants.GROUP_ROLE> userGroups = null;
//            List<Group> publicGroups = getCsi().getPublicGroups();
//            String groupName = study.getIdentifier();
//            if (user != null) { // i.e. user is authenticated
//                userGroups = getCsi().getUserGroups(null);
//                for (Group group : userGroups.keySet()) {
//                    if (group.getName().equals(groupName)) {
//                        return new Study(group.getName(), group.isPublicGroup(), group.isGridFile(), group.isGridJobs(), userGroups.get(group));
//                    }
//                }
//            }
//
//            //user may not be authenticated
//            for (Group group : publicGroups) {
//                if ((user == null || !isInMap(userGroups, group)) && group.getName().equals(groupName)) {
//                    return new Study(group.getName(), group.isPublicGroup(), group.isGridFile(), group.isGridJobs(), CoreConstants.GROUP_ROLE.None);
//                }
//            }
//            return null;
//        } catch (CoreException ex) {
//            throw new ApiException(ex);
//        }
    }

