/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
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
