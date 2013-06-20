/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Boujelben
 */
public class ParameterLayout extends VLayout{
    
    
    
    
   private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public  ParameterLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureActions();
        configureGrid();
        modal = new ModalWindow(grid);

        //loadData();
    }

    private void configureActions() {

        ToolstripLayout toolstrip = new ToolstripLayout();

        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));

        LabelButton addButton = new LabelButton("Edit", CoreConstants.ICON_EDIT);
        addButton.setWidth(150);
        /*addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
                appsTab.setApplication(null, null, null);
            }
        });
        * */
       
        toolstrip.addMember(addButton);

        LabelButton refreshButton = new LabelButton("Refresh", CoreConstants.ICON_REFRESH);
        refreshButton.setWidth(150);
        /*refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadData();
            }
        });
        * */
        
        toolstrip.addMember(refreshButton);

        this.addMember(toolstrip);
    }

    private void configureGrid() {

        grid = new ListGrid();
        /*{
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    ImgButton loadImg = getImgButton(CoreConstants.ICON_EDIT, "Edit");
                    loadImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            edit(rollOverRecord.getAttribute("name"),
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
        };*/
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setFields(new ListGridField("name", "Name"),
                new ListGridField("description", "Description"),
                 new ListGridField("examples", "Examples")
               );
      /*  grid.setSortField("name");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellClickHandler(new CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {

                edit(event.getRecord().getAttribute("name"),
                        event.getRecord().getAttribute("classes"),
                        event.getRecord().getAttribute("citation"));
            }
        });
        
        */
        this.addMember(grid);
    }
    
    
/*
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
                    for (String className : app.getApplicationClasses()) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(className);
                    }
                    dataList.add(new ApplicationRecord(app.getName(), sb.toString(), app.getCitation()));
                }
                grid.setData(dataList.toArray(new ApplicationRecord[]{}));
            }
        };
        modal.show("Loading applications...", true);
        ApplicationService.Util.getInstance().getApplications(callback);
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

    private void edit(String name, String classes, String citation) {

        ManageApplicationsTab appsTab = (ManageApplicationsTab) Layout.getInstance().
                getTab(ApplicationConstants.TAB_MANAGE_APPLICATION);
        
        appsTab.loadVersions(name);
        appsTab.setApplication(name, classes, citation);
    }
}*/
}


    
    
    
    
    
    
    
    
