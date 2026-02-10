package fr.insalyon.creatis.vip.applicationimporter.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.application.models.boutiquesTools.BoutiquesApplication;

public interface ApplicationImporterServiceAsync {

    public void readAndValidateBoutiquesFile(String fileLFN, AsyncCallback<String> callback);

    public void createApplication(BoutiquesApplication bt, boolean overwriteVersion, List<Tag> tags, List<String> resources, AsyncCallback<Void> callback);

    public void getBoutiquesTags(String boutiquesJsonFile ,AsyncCallback<List<Tag>> asyncCallback);

}
