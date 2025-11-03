package fr.insalyon.creatis.vip.publication.client.view;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Nouha Boujelben
 */
public class PublicationTab extends Tab {
    PublicationLayout publicationLayout;
    EditPublicationLayout editPublicationLayout;
    BibtexLayout bibtexLayout;
    private ModalWindow modal;

    public PublicationTab() {
        this.setTitle(Canvas.imgHTML(PublicationConstants.ICON_PUBLICATION) + " "
                + PublicationConstants.APP_PUBLICATIONS);
        this.setID(PublicationConstants.TAB_PUBLICATION);
        this.setCanClose(true);
        VLayout vLayout = new VLayout(5);
        vLayout.setWidth100();
        vLayout.setPadding(10);
        vLayout.setMembersMargin(5);

        VLayout vLayout2 = new VLayout(5);
        vLayout2.setWidth100();
        vLayout2.setPadding(10);
        vLayout2.setMembersMargin(5);

        HLayout hLayout = new HLayout(5);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);
        hLayout.setMembersMargin(5);

        if(CoreModule.user != null) {
            vLayout2.addMember(editPublicationLayout = new EditPublicationLayout());
            vLayout2.addMember(bibtexLayout = new BibtexLayout());
        }

        modal = new ModalWindow(hLayout);

        hLayout.addMember(publicationLayout = new PublicationLayout( modal));
        if(CoreModule.user != null) {
            hLayout.addMember(vLayout2);
            vLayout.addMember(new PublicationInfoTab());
        }
        vLayout.addMember(hLayout);

        this.setPane(vLayout);

    }

    public void setPublication(String id, String title, String type, String nameType, String authors, String date, String doi, String vipApplication) {
        editPublicationLayout.setPublication(id, title, type, nameType, authors, date, doi, vipApplication);
    }

    public void loadPublication() {
        publicationLayout.loadData( modal);
    }
  
    public ModalWindow getModal() {
        return modal;
    }
    
}
