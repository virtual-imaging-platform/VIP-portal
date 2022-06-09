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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.publication.client.bean.Publication;
import fr.insalyon.creatis.vip.publication.client.rpc.PublicationService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Nouha Boujelben
 */
public class PublicationLayout extends VLayout {

    PublicationGrid publicationGrid;
    boolean state = true;
   

    public PublicationLayout(ModalWindow modal) {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);
        publicationGrid = new PublicationGrid();
        configureActions(modal);
        configureGrid();
        loadData(modal);

    }

    private void configureActions(final ModalWindow modal) {

        ToolstripLayout toolstrip = new ToolstripLayout();

        LabelButton searchButton = new LabelButton("Search", CoreConstants.ICON_SEARCH);
        searchButton.setWidth(150);
        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setFilter();
            }
        });

        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));
        toolstrip.addMember(searchButton);

        if(CoreModule.user != null) {
            LabelButton addButton = new LabelButton("Add Publication", CoreConstants.ICON_ADD);
            addButton.setWidth(150);
            addButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    edit(null, null, null, null, null, null, null, null);
                }
            });
            toolstrip.addMember(addButton);

            LabelButton refreshButton = new LabelButton("Refresh", CoreConstants.ICON_REFRESH);
            refreshButton.setWidth(150);
            refreshButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    loadData(modal);
                }
            });
            toolstrip.addMember(refreshButton);
        }

        this.addMember(toolstrip);
    }

    private void configureGrid() {

        this.addMember(publicationGrid.getGrid());
    }

    public void loadData(final ModalWindow modal) {
        final AsyncCallback<List<Publication>> callback = new AsyncCallback<List<Publication>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of publications:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Publication> result) {
               modal.hide();
                List<PublicationRecord> dataList = new ArrayList<PublicationRecord>();
                for (Publication pub : result) {
                    dataList.add(new PublicationRecord(pub.getId(), pub.getTitle(), pub.getType(), pub.getTypeName(), pub.getDate(), pub.getAuthors(), pub.getDoi(), pub.getVipAuthor(), pub.getVipApplication()));
                }
                publicationGrid.getGrid().setData(dataList.toArray(new PublicationRecord[]{}));
                publicationGrid.getDs().setTestData(dataList.toArray(new PublicationRecord[]{}));
            }
        };
      modal.show("Loading Publications...", true);
        PublicationService.Util.getInstance().getPublications(callback);
    }

    protected static void edit(String id, String title, String type, String typeName, String authors, String date, String doi, String vipApplication) {

        PublicationTab pubTab = (PublicationTab) Layout.getInstance().
                getTab(PublicationConstants.TAB_PUBLICATION);
        pubTab.setPublication(id, title, type, typeName, authors, date, doi, vipApplication);
    }

    public void setFilter() {

        if (state == false) {
            publicationGrid.getGrid().setShowFilterEditor(false);
            state = true;
        } else {
            publicationGrid.getGrid().setShowFilterEditor(true);
            state = false;
        }

    }

   
}
