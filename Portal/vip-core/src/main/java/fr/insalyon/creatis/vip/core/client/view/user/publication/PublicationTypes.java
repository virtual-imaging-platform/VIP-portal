/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.view.user.publication;

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
