/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.ApplicationStatus;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;

import java.util.List;
import java.util.Map;

public interface ApplicationService extends RemoteService {

    public static final String SERVICE_URI = "/applicationservice";

    public static class Util {

        public static ApplicationServiceAsync getInstance() {

            ApplicationServiceAsync instance = (ApplicationServiceAsync) GWT.create(ApplicationService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public Map<Application, List<AppVersion>> getPublicApplications() throws ApplicationException;

    public Map<Application, List<AppVersion>> getApplications() throws ApplicationException;
    
    public List<AppVersion> getVersions(String applicationName) throws ApplicationException;
    
    public AppVersion getVersion(String applicationName, String applicationVersion) throws ApplicationException;

    public String add(Application application) throws ApplicationException;

    public String update(Application application) throws ApplicationException;

    public String remove(String name) throws ApplicationException;
    
    public void addVersion(AppVersion version) throws ApplicationException;

    public void updateVersion(AppVersion version) throws ApplicationException;

    public void removeVersion(String applicationName, String version) throws ApplicationException;

    public String publishVersion(String applicationName, String version) throws ApplicationException;

    public List<String>[] getApplicationsAndUsers() throws ApplicationException;
    
    public ApplicationStatus getApplicationStatus() throws ApplicationException;
    
    public String getCitation(String applicationName) throws ApplicationException;
    
    public void addEngine(Engine engine) throws ApplicationException;
    
    public void updateEngine(Engine engine) throws ApplicationException;
    
    public void removeEngine(String engineName) throws ApplicationException;
    
    public List<Engine> getEngines() throws ApplicationException;
    
    public String addResource(Resource resource) throws ApplicationException;

    public String removeResource(Resource resource) throws ApplicationException;

    public String updateResource(Resource resource) throws ApplicationException;

    public List<Resource> getResources() throws ApplicationException;

    public void addTag(Tag tag) throws ApplicationException;

    public void removeTag(Tag tag) throws ApplicationException;

    public void updateTag(Tag tag, String newName) throws ApplicationException;

    public List<Tag> getTags() throws ApplicationException;

    public List<Tag> getTags(String appName, String appVersion) throws ApplicationException;

    public Boolean isAppUsableWithCurrentUser(String appName, String appVersion) throws ApplicationException;
}
