package fr.insalyon.creatis.vip.publication.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.publication.client.bean.Publication;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva, Sorina Pop
 */
public interface PublicationService extends RemoteService {

    String SERVICE_URI = "/publicationservice";

    class Util {

        public static PublicationServiceAsync getInstance() {

            PublicationServiceAsync instance = GWT.create(PublicationService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    void addPublication(Publication pub) throws CoreException;

    void updatePublication(Publication pub) throws CoreException;

    List<Publication> getPublications() throws CoreException;

    void removePublication(Long id) throws CoreException;

    List<Publication> parseBibtexText(String text) throws CoreException;

}
