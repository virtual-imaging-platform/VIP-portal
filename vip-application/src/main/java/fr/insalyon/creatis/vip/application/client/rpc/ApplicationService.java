package fr.insalyon.creatis.vip.application.client.rpc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.ApplicationStatus;
import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.Pair;

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

    public List<Application> getPublicApplications() throws VipException;

    public Map<Application, List<AppVersion>> getApplications() throws VipException;

    public Map<Application, Set<Resource>> getManageableApplications() throws VipException;
    
    public List<AppVersion> getVersions(String applicationName) throws VipException;
    
    public AppVersion getVersion(String applicationName, String applicationVersion) throws VipException;

    public String add(Application application) throws VipException;

    public String update(Application application) throws VipException;

    public String remove(String name) throws VipException;
    
    public void addVersion(AppVersion version) throws VipException;

    public void updateVersion(AppVersion version) throws VipException;

    public void removeVersion(String applicationName, String version) throws VipException;

    public String publishVersion(String applicationName, String version) throws VipException;

    public List<String>[] getApplicationsAndUsers() throws VipException;
    
    public ApplicationStatus getApplicationStatus() throws VipException;
    
    public String getCitation(String applicationName) throws VipException;
    
    public void addEngine(Engine engine) throws VipException;
    
    public void updateEngine(Engine engine) throws VipException;
    
    public void removeEngine(String engineName) throws VipException;
    
    public List<Engine> getEngines() throws VipException;
    
    public String addResource(Resource resource) throws VipException;

    public String removeResource(Resource resource) throws VipException;

    public String updateResource(Resource resource) throws VipException;

    public List<Resource> getResources() throws VipException;

    public void addTag(Tag tag) throws VipException;

    public void removeTag(Tag tag) throws VipException;

    public void updateTag(Tag oldTag, Tag newTag) throws VipException;

    public List<Tag> getTags() throws VipException;

    public List<Tag> getNonBoutiquesTags() throws VipException;

    public List<Tag> getTags(AppVersion appVersion) throws VipException;

    public Pair<Boolean, String> isAppUsableWithCurrentUser(String appName, String appVersion) throws VipException;
}
