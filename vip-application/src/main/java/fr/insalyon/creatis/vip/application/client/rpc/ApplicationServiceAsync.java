package fr.insalyon.creatis.vip.application.client.rpc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.ApplicationStatus;
import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.core.models.Pair;

public interface ApplicationServiceAsync {

    public void getPublicApplications(AsyncCallback<List<Application>> asyncCallback);
    
    public void getApplications(AsyncCallback<Map<Application, List<AppVersion>>> asyncCallback);

    public void getManageableApplications(AsyncCallback<Map<Application, Set<Resource>>> asyncCallback);
    
    public void getVersions(String applicationName, AsyncCallback<List<AppVersion>> asyncCallback);
    
    public void getVersion(String applicationName, String applicationVersion, AsyncCallback <AppVersion> asyncCallback);

    public void add(Application application, AsyncCallback<String> asyncCallback);

    public void update(Application application, AsyncCallback<String> asyncCallback);

    public void remove(String name, AsyncCallback<String> asyncCallback);
    
    public void addVersion(AppVersion version, AsyncCallback<Void> asyncCallback);

    public void updateVersion(AppVersion version, AsyncCallback<Void> asyncCallback);

    public void removeVersion(String applicationName, String version, AsyncCallback<Void> asyncCallback);

    public void publishVersion(String applicationName, String version, AsyncCallback<String> asyncCallback);

    public void getApplicationsAndUsers(AsyncCallback<List<String>[]> asyncCallback);
    
    public void getApplicationStatus(AsyncCallback<ApplicationStatus> asyncCallback);
    
    public void getCitation(String applicationName, AsyncCallback<String> asyncCallback);
    
    public void addEngine(Engine engine, AsyncCallback<Void> asyncCallback);
    
    public void updateEngine(Engine engine, AsyncCallback<Void> asyncCallback);
    
    public void removeEngine(String engineName, AsyncCallback<Void> asyncCallback);
    
    public void getEngines(AsyncCallback<List<Engine>> asyncCallback);
    
    public void addResource(Resource resource, AsyncCallback<String> asyncCallback);

    public void removeResource(Resource resource, AsyncCallback<String> asyncCallback);

    public void updateResource(Resource resource, AsyncCallback<String> asyncCallback);

    public void getResources(AsyncCallback<List<Resource>> asyncCallback);

    public void addTag(Tag tag, AsyncCallback<Void> asyncCallback);

    public void removeTag(Tag tag, AsyncCallback<Void> asyncCallback);

    public void updateTag(Tag oldTag, Tag newTag, AsyncCallback<Void> asyncCallback);

    public void getTags(AsyncCallback<List<Tag>> asyncCallback);

    public void getNonBoutiquesTags(AsyncCallback<List<Tag>> asyncCallback);

    public void getTags(AppVersion appVersion, AsyncCallback<List<Tag>> asyncCallback);

    public void isAppUsableWithCurrentUser(String appName, String appVersion, AsyncCallback<Pair<Boolean, String>> asyncCallback);
}
