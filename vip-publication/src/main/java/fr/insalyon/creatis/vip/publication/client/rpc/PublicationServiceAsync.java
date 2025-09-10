package fr.insalyon.creatis.vip.publication.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.publication.client.bean.Publication;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva, Sorina Pop
 */
public interface PublicationServiceAsync {

    void getPublications(AsyncCallback<List<Publication>> asyncCallback);

    void removePublication(Long id, AsyncCallback<Void> asyncCallback);

    void addPublication(Publication pub, AsyncCallback<Void> asyncCallback);

    void updatePublication(Publication pub, AsyncCallback<Void> asyncCallback);

    void parseBibtexText(String text, AsyncCallback<List<Publication>> asyncCallback);
}
