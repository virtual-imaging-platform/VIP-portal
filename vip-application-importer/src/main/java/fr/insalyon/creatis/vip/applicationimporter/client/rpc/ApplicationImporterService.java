package fr.insalyon.creatis.vip.applicationimporter.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.application.models.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.applicationimporter.client.ApplicationImporterException;

public interface ApplicationImporterService extends RemoteService {

    public static final String SERVICE_URI = "/applicationimporterservice";

    public static class Util {

        public static ApplicationImporterServiceAsync getInstance() {
            ApplicationImporterServiceAsync instance = (ApplicationImporterServiceAsync) GWT.create(ApplicationImporterService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    String readAndValidateBoutiquesFile(String fileLFN) throws ApplicationImporterException;

    void createApplication(BoutiquesApplication bt, boolean overwriteVersion, List<Tag> tags, List<String> resources) throws ApplicationImporterException;

    List<Tag> getBoutiquesTags(String boutiquesJsonFile) throws ApplicationImporterException;
}
