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
package fr.insalyon.creatis.vip.application.client.view.system.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
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
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationsLayout extends VLayout {

    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    private boolean onlyPublicApps;

    public ApplicationsLayout(boolean onlyPublicApps) {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        this.onlyPublicApps = onlyPublicApps;

        configureActions();
        configureGrid();
        modal = new ModalWindow(grid);
        loadData();
    }

    private void configureActions() {

        ToolstripLayout toolstrip = new ToolstripLayout();

        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));

        if( ! onlyPublicApps) {
            LabelButton addButton = new LabelButton("Add Application", CoreConstants.ICON_ADD);
            addButton.setWidth(150);
            addButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                            getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                    appsTab.setApplication(null, null, null, null);
                }
            });
            toolstrip.addMember(addButton);

            LabelButton refreshButton = new LabelButton("Refresh", CoreConstants.ICON_REFRESH);
            refreshButton.setWidth(150);
            refreshButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    loadData();
                }
            });
            toolstrip.addMember(refreshButton);

            this.addMember(toolstrip);
        }
    }

    private void configureGrid() {

        grid = new ListGrid() {
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if ( ! onlyPublicApps && rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    ImgButton loadImg = getImgButton(CoreConstants.ICON_EDIT, "Edit");
                    loadImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            edit(rollOverRecord.getAttribute("name"),
                                    rollOverRecord.getAttribute("owner"),
                                    rollOverRecord.getAttribute("classes"),
                                    rollOverRecord.getAttribute("citation"));
                        }
                    });
                    ImgButton deleteImg = getImgButton(CoreConstants.ICON_DELETE, "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final String name = rollOverRecord.getAttribute("name");
                            SC.ask("Do you really want to remove the application \""
                                    + name + "\"?", new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value) {
                                        remove(name);
                                    }
                                }
                            });
                        }
                    });
                    rollOverCanvas.addMember(loadImg);
                    if (! CoreModule.user.isDeveloper()) {
                        rollOverCanvas.addMember(deleteImg);
                    }
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
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");
        if (onlyPublicApps){
            grid.setFields(new ListGridField("name", "Application Name"),
                    new ListGridField("classes", "Classes"),
                    new ListGridField("groups", "Groups"));
        } else {
            ListGridField ownerField = new ListGridField("owner", "Owner");
            ownerField.setHidden(true);
            grid.setFields(new ListGridField("name", "Application Name"),
                    new ListGridField("ownerFullName", "Owner"),
                    ownerField,
                    new ListGridField("classes", "Classes"));
        }
        grid.setSortField("name");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellClickHandler(new CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {

                edit(event.getRecord().getAttribute("name"),
                        event.getRecord().getAttribute("owner"),
                        event.getRecord().getAttribute("classes"),
                        event.getRecord().getAttribute("citation"));
            }
        });
        this.addMember(grid);
    }

    public void loadData() {

        final AsyncCallback<List<Application>> callback = new AsyncCallback<List<Application>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of applications:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Application> result) {
                modal.hide();
                List<ApplicationRecord> dataList = new ArrayList<ApplicationRecord>();

                for (Application app : result) {
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sbg = new StringBuilder();
                    for (String className : app.getApplicationClasses()) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(className);
                    }

                    if(onlyPublicApps) {
                        for (String group : app.getApplicationGroups()) {
                            if (sbg.length() > 0) {
                                sbg.append(", ");
                            }
                            sbg.append(group);
                        }
                    }
                    if(onlyPublicApps) {
                        dataList.add(new ApplicationRecord(app.getName(), app.getOwner(), app.getFullName(), sb.toString(), app.getCitation(), sbg.toString()));
                    } else {
                        dataList.add(new ApplicationRecord(app.getName(), app.getOwner(), app.getFullName(), sb.toString(), app.getCitation()));
                    }
                }
                grid.setData(dataList.toArray(new ApplicationRecord[]{}));
            }
        };
        modal.show("Loading applications...", true);
        if (onlyPublicApps) {
            ApplicationService.Util.getInstance().getPublicApplications(callback);
        } else {
            ApplicationService.Util.getInstance().getApplications(callback);
        }
    }

    private void remove(String name) {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove application:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("The application was successfully removed!");
                loadData();
            }
        };
        modal.show("Removing application '" + name + "'...", true);
        ApplicationService.Util.getInstance().remove(name, callback);
    }

    private void edit(String name, String owner, String classes, String citation) {

        ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);

        appsTab.loadVersions(name);
        appsTab.setApplication(name, owner, classes, citation);
    }
}
