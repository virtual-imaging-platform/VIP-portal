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
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.publication.client.rpc.PublicationService;

/**
 *
 * @author Nouha Boujelben
 */
public class PublicationGrid extends ListGrid {

    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;
    private DetailViewer detailViewer;
    DataSource ds;
   

    public PublicationGrid() {
        ds = new Data();
        grid = new ListGrid() {
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if (CoreModule.user != null && rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    final ImgButton loadImg = getImgButton(CoreConstants.ICON_EDIT, "Edit");
                    loadImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            PublicationLayout.edit(rollOverRecord.getAttribute("id"), rollOverRecord.getAttribute("title"),
                                    rollOverRecord.getAttribute("type"),
                                    rollOverRecord.getAttribute("typeName"), rollOverRecord.getAttribute("authors"), rollOverRecord.getAttribute("date"), rollOverRecord.getAttribute("doi"), rollOverRecord.getAttribute("vipApplication"));
                        }
                    });
                    final ImgButton deleteImg = getImgButton(CoreConstants.ICON_DELETE, "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final String id = rollOverRecord.getAttribute("id");
                            SC.ask("Do you really want to remove the publication \""
                                    + rollOverRecord.getAttribute("title") + " ?", new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value != null && value) {
                                        remove(Long.parseLong(id));
                                    }
                                }
                            });

                        }
                    });

                    
                        rollOverCanvas.addMember(loadImg);
                        rollOverCanvas.addMember(deleteImg);
                    
                }
                return rollOverCanvas;
            }

            private ImgButton getImgButton(String imgSrc, String prompt) {
                ImgButton button = new ImgButton();
                button.setShowDown(false);
                button.setShowRollOver(false);
                button.setLayoutAlign(Alignment.CENTER);
                button.setSrc(imgSrc);
                button.setPrompt(prompt);
                button.setHeight(16);
                button.setWidth(16);
                return button;
            }

            @Override
            protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {


                detailViewer = new DetailViewer();
                detailViewer.setWidth(400);
                if (CoreModule.user.isSystemAdministrator()) {
                    detailViewer.setFields(
                            new DetailViewerField("title", "Title"),
                            new DetailViewerField("type", "Type"),
                            new DetailViewerField("typeName", "Journal, Conference or Book Name"),
                            new DetailViewerField("authors", "Authors"),
                            new DetailViewerField("date", "Date"),
                            new DetailViewerField("vipApplication", "VIP Application"),
                            new DetailViewerField("doi", "Doi"),
                            new DetailViewerField("vipAuthor", "Owner"));
                } else {
                    detailViewer.setFields(
                            new DetailViewerField("title", "Title"),
                            new DetailViewerField("type", "Type"),
                            new DetailViewerField("typeName", "Journal, Conference or Book Name"),
                            new DetailViewerField("authors", "Authors"),
                            new DetailViewerField("date", "Date"),
                            new DetailViewerField("vipApplication", "VIP Application"),
                            new DetailViewerField("doi", "Doi"));
                }

                detailViewer.setData(new Record[]{record});

                return detailViewer;
            }
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowEmptyMessage(true);
        grid.setShowAllRecords(false);
        grid.setShowRowNumbers(true);
        grid.setCanHover(true);
        grid.setShowHover(true);
        grid.setShowHoverComponents(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setFilterOnKeypress(true);
        grid.setDataSource(ds);
        grid.setAutoFetchData(Boolean.TRUE);
        ListGridField publicationId = new ListGridField("id", "ID");
        ListGridField pubOwner = new ListGridField("vipAuthor", "Owner");
        grid.setFields(publicationId, new ListGridField("title", "Title"),
                new ListGridField("type", "Type"),
                new ListGridField("typeName", "Journal, Conference or Book Name"),
                new ListGridField("authors", "Authors"),
                new ListGridField("date", "Date"),
                new ListGridField("vipApplication", "VIP Application"),
                pubOwner);

        publicationId.setHidden(true);

        if (CoreModule.user != null && CoreModule.user.isSystemAdministrator()) {
            pubOwner.setHidden(false);
        } else {
            pubOwner.setHidden(true);
        }
        grid.setSortField("title");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellClickHandler(new CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                if (CoreModule.user.getLevel() == UserLevel.Administrator) {
                    PublicationLayout.edit(event.getRecord().getAttribute("id"), event.getRecord().getAttribute("title"),
                            event.getRecord().getAttribute("type"),
                            event.getRecord().getAttribute("typeName"), event.getRecord().getAttribute("authors"), event.getRecord().getAttribute("date"), event.getRecord().getAttribute("doi"),event.getRecord().getAttribute("vipApplication"));
                }
            }
        });
    }

    private void remove(Long id) {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
               getPubTab().getModal().hide();
                Layout.getInstance().setWarningMessage("Unable to remove publication:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                Layout.getInstance().setNoticeMessage("222");
                getPubTab().getModal().hide();
                Layout.getInstance().setNoticeMessage("The publication was successfully removed!");
                loadData();
            }
        };
       getPubTab().getModal().show("Removing publication '" + "'...", true);

        PublicationService.Util.getInstance().removePublication(id, callback);

    }

    public ListGrid getGrid() {
        return grid;
    }

    public DataSource getDs() {
        return ds;
    }

    protected void loadData() {

        getPubTab().loadPublication();
    }

    private PublicationTab getPubTab() {
        return (PublicationTab) Layout.getInstance().
                getTab(PublicationConstants.TAB_PUBLICATION);
    }
}
