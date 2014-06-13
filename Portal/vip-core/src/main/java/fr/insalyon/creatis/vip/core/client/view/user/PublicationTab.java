package fr.insalyon.creatis.vip.core.client.view.user;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.user.publication.EditPublicationLayout;
import fr.insalyon.creatis.vip.core.client.view.user.publication.PublicationLayout;

/**
 *
 * @author Nouha Boujelben
 */
public class PublicationTab extends Tab {

    PublicationLayout publicationLayout;
    EditPublicationLayout editPublicationLayout;

    public PublicationTab() {
        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_PUBLICATION) + " "
                + CoreConstants.APP_PUBLICATIONS);
        this.setID(CoreConstants.TAB_PUBLICATION);
        this.setCanClose(true);

        HLayout hLayout = new HLayout(5);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);
        hLayout.setPadding(10);

        hLayout.addMember(publicationLayout = new PublicationLayout());
        hLayout.addMember(editPublicationLayout = new EditPublicationLayout());
        this.setPane(hLayout);

    }

    public void setPublication(String id, String title, String type, String nameType, String authors, String date, String doi) {
        editPublicationLayout.setPublication(id, title, type, nameType, authors, date, doi);
    }

    public void loadPublication() {
        publicationLayout.loadData();
    }
}
