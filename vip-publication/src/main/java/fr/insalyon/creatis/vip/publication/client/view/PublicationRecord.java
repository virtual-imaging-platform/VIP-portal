package fr.insalyon.creatis.vip.publication.client.view;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Nouha Boujelben
 */
public class PublicationRecord extends ListGridRecord {

    public PublicationRecord(Long id, String title, String type, String typeName, String date, String authors, String doi, String vipAuthor, String vipApplication) {

        setAttribute("id", id);
        setAttribute("title", title);
        setAttribute("type", type);
        setAttribute("typeName", typeName);
        setAttribute("date", date);
        setAttribute("authors", authors);
        setAttribute("doi", doi);
        setAttribute("vipAuthor", vipAuthor);
        setAttribute("vipApplication", vipApplication);
    }
}
