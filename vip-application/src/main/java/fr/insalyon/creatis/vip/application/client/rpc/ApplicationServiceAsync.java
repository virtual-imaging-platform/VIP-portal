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

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.ApplicationStatus;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public interface ApplicationServiceAsync {

    public void signout(AsyncCallback<Void> asyncCallback);

    public void getPublicApplications(AsyncCallback<List<Application>> asyncCallback);
    
    public void getApplications(AsyncCallback<List<Application>> asyncCallback);
    
    public void getApplications(String className, AsyncCallback<List<String[]>> asyncCallback);

    public void getApplicationsByClass(String className, AsyncCallback<List<String[]>> asyncCallback);
    
    public void getVersions(String applicationName, AsyncCallback<List<AppVersion>> asyncCallback);
    
    public  void getVersion(String applicationName, String applicationVersion,AsyncCallback <AppVersion> asyncCallback);

    public void add(Application application, AsyncCallback<Void> asyncCallback);

    public void update(Application application, AsyncCallback<Void> asyncCallback);

    public void remove(String name, AsyncCallback<Void> asyncCallback);
    
    public void addVersion(AppVersion version, String[] tags, String[] resources, AsyncCallback<Void> asyncCallback);

    public void updateVersion(AppVersion version, String[] tags, String[] resources, AsyncCallback<Void> asyncCallback);

    public void removeVersion(String applicationName, String version, AsyncCallback<Void> asyncCallback);

    public void publishVersion(String applicationName, String version, AsyncCallback<String> asyncCallback);

    public void removeClass(String name, AsyncCallback<Void> asyncCallback);

    public void getClasses(AsyncCallback<List<AppClass>> asyncCallback);
    
    public void addClass(AppClass c, AsyncCallback<Void> asyncCallback);

    public void updateClass(AppClass c, AsyncCallback<Void> asyncCallback);

    public void getApplicationsAndUsers(List<String> reservedClasses, AsyncCallback<List<String>[]> asyncCallback);
    
    public void getApplicationStatus(AsyncCallback<ApplicationStatus> asyncCallback);
    
    public void getCitation(String applicationName, AsyncCallback<String> asyncCallback);
    
    public void addEngine(Engine engine, AsyncCallback<Void> asyncCallback);
    
    public void updateEngine(Engine engine, AsyncCallback<Void> asyncCallback);
    
    public void removeEngine(String engineName, AsyncCallback<Void> asyncCallback);
    
    public void getEngines(AsyncCallback<List<Engine>> asyncCallback);
    
    public void getReservedClasses(AsyncCallback <HashMap<String, Integer>> asyncCallback);

    public void addResource(Resource resource, AsyncCallback<Void> asyncCallback);

    public void removeResource(Resource resource, AsyncCallback<Void> asyncCallback);

    public void updateResource(Resource resource, AsyncCallback<Void> asyncCallback);

    public void updateResourcesReference(String[] select, AppVersion appVersion, AsyncCallback<Void> asyncCallback) throws ApplicationException;

    public void getResources(AsyncCallback<List<Resource>> asyncCallback);

    public void getResourcesFrom(AppVersion appVersion, AsyncCallback<List<Resource>> asyncCallback);

    public void addTag(Tag tag, AsyncCallback<Void> asyncCallback);

    public void removeTag(Tag tag, AsyncCallback<Void> asyncCallback);

    public void updateTag(Tag tag, String newName, AsyncCallback<Void> asyncCallback);

    public void updateTagsReference(String[] select, AppVersion appVersion, AsyncCallback<Void> asyncCallback) throws ApplicationException;

    public void getTags(AsyncCallback<List<Tag>> asyncCallback);

    public void getTagsFrom(AppVersion appVersion, AsyncCallback<List<Tag>> asyncCallback);
}
