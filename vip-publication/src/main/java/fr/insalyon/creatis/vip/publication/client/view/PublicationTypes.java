package fr.insalyon.creatis.vip.publication.client.view;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Nouha boujelben
 */
public enum PublicationTypes implements IsSerializable{

    ConferenceArticle("Article In Conference Proceedings"), Journal("Journal Article"), BookChapter("Book Chapter"), Other("Other");

    private PublicationTypes() {
    }
    private String value;

    private PublicationTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
