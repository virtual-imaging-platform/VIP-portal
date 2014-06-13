/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.client.view.user.publication;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Nouha Boujelben
 */
public class PublicationRecord extends ListGridRecord {

    public PublicationRecord(Long id, String title, String type, String typeName, String date, String authors, String doi) {

        setAttribute("id", id);
        setAttribute("title", title);
        setAttribute("type", type);
        setAttribute("typeName", typeName);
        setAttribute("date", date);
        setAttribute("authors", authors);
        setAttribute("doi", doi);
    }
}
