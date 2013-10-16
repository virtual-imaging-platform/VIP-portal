/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.menu.*;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;

import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;

import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.ParameterValue;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import fr.insalyon.creatis.vip.query.client.view.QueryExecutionRecord;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Boujelben
 */
public class QueryHistoryTab extends Tab {

    protected ModalWindow modal;
    protected ListGrid grid;
    ListGridField linkField;
    protected String user = null;
    protected String status = null;
    protected Date startDate = null;
    protected Date endDate = null;
    private ListGridRecord rollOverRecord;
    private DetailViewer detailViewer;
    DataSource ds;
    boolean state = true;

    public QueryHistoryTab() {


        this.setTitle(Canvas.imgHTML(QueryConstants.ICON_QUERYHISTORY) + "Query History");
        this.setID(QueryConstants.TAB_QUERYHISTORY);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        configureGrid();
        modal = new ModalWindow(grid);


        VLayout vLayout = new VLayout();
        vLayout.addMember(new HistoryToolStrip(modal));

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);

        SectionStackSection gridSection = new SectionStackSection();
        gridSection.setCanCollapse(false);
        gridSection.setShowHeader(false);
        gridSection.addItem(grid);

        sectionStack.setSections(gridSection);
        vLayout.addMember(sectionStack);
        this.setPane(vLayout);
        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid() {
            @Override
            protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {

                String fieldName = this.getFieldName(colNum);
               ImgButton button = null;

                if (fieldName.equals("buttonField")) {
                    button = new ImgButton();
                   button.setShowDown(false);  
                   button.setShowRollOver(false);  
                    button.setAlign(Alignment.CENTER);  
                   
                    button.setHeight(16);  
                    button.setWidth(16);


                    if (record.getAttribute("status") == "completed") {
                        
                       button.setSrc(QueryConstants.ICON_LINK);  
                   
                        button.addClickHandler(new ClickHandler() {
                            public void onClick(ClickEvent event) {

                                Window.open(GWT.getModuleBaseURL() + "/filedownload?queryid="
                                        + record.getAttribute("queryExecutionID").toString() + "&path=" + record.getAttribute("pathFileResult"), "", "");
                            }
                        });
                    } else if (record.getAttribute("status") == "failed") {
                        button.setSrc(QueryConstants.ICON_ERROR);
                        
                        button.addClickHandler(new ClickHandler() {
                            public void onClick(ClickEvent event) {
                                SC.say("<html><font color=\"red\">ERROR!</font></html>", record.getAttribute("pathFileResult"));
                            }
                        });


                    }


                }
                if (record.getAttribute("status") == "completed" || record.getAttribute("status") == "failed") {
                    return button;
                } else {
                    return null;
                }




            }

            ;




            @Override
            protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {
                rollOverRecord = this.getRecord(rowNum);

                detailViewer = new DetailViewer();

                detailViewer.setWidth(200);
                DetailViewerField name = new DetailViewerField("name", "name");
                DetailViewerField type = new DetailViewerField("value", "value");
                detailViewer.setFields(name, type);

                Long executionID = record.getAttributeAsLong("queryExecutionID");
                //appel rpc
                final AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Layout.getInstance().setWarningMessage("Unable to save Query Execution " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<String[]> result) {
                        List<ParameterValue> dataList = new ArrayList<ParameterValue>();;
                        for (String[] s : result) {
                            dataList.add(new ParameterValue(s[0], s[1]));
                        }
                        detailViewer.setData(dataList.toArray(new ParameterValue[]{}));
                        detailViewer.setEmptyMessage("No parameters to display");
                        detailViewer.setBackgroundColor("white");
                        detailViewer.setEmptyMessageStyle("2px solid black center");
                        detailViewer.setBorder("1px solid gray");
                    }
                };
                QueryService.Util.getInstance().getParameterValue(executionID, callback);

                return detailViewer;

            }
        };


        grid.addRowContextClickHandler(new RowContextClickHandler() {
            public void onRowContextClick(RowContextClickEvent event) {
                ListGridRecord record = event.getRecord();
                if (record.getAttribute("status") == "failed") {
                    Menu menu = new Menu();
                    final String queryExecutionID=record.getAttribute("queryExecutionID");

                    MenuItem relaunch = new MenuItem("Relauch", QueryConstants.ICON_EXECUTE);
                     
                    relaunch.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
                        public void onClick(MenuItemClickEvent event) {

                            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {

                                    Layout.getInstance().setWarningMessage("Unable to relaunch this query" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(Void result) {
                                }
                            };
                            QueryService.Util.getInstance().updateQueryExecutionStatusWaiting("waiting",Long.parseLong(queryExecutionID), callback);

                        }
                    });
                    menu.addItem(relaunch);
                    

                    grid.setContextMenu(menu);
                } else if(record.getAttribute("status") == "waiting" ||record.getAttribute("status") == "running" ){

                    Menu menu = new Menu();
                     final String queryExecutionID=record.getAttribute("queryExecutionID");
                    MenuItem kill = new MenuItem("kill", QueryConstants.ICON_KILL);
                    
                      kill.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
                        public void onClick(MenuItemClickEvent event) {

                            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {

                                    Layout.getInstance().setWarningMessage("Unable to relaunch this query" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(Void result) {
                                }
                            };
                            QueryService.Util.getInstance().updateQueryExecutionStatusFailed("failed",Long.parseLong(queryExecutionID), callback);

                        }
                    });
                    
                    
                    
                    
                    
                    
                    
                    
                    menu.addItem(kill);
                    grid.setContextMenu(menu);
                }
                else{
                    Menu menu = new Menu();
                    menu=null;
                    grid.setContextMenu(menu);
                }





            }
        });


        ds = new Data();

        grid.setCanHover(true);
        grid.setShowHover(true);
        grid.setShowHoverComponents(true);
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowRecordComponents(true);
        grid.setShowRecordComponentsByCell(true);
        //
        grid.setFilterOnKeypress(true);

        grid.setDataSource(ds);
        grid.setAutoFetchData(Boolean.TRUE);

        grid.setDataFetchMode(FetchMode.LOCAL);

        grid.setShowAllRecords(false);
        grid.setShowRowNumbers(true);
        grid.setShowEmptyMessage(true);
        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

        grid.setEmptyMessage("<br>No data available");




        ListGridField executionID = new ListGridField("queryExecutionID", "queryExecutionID");
        ListGridField pathFileResult = new ListGridField("pathFileResult", "pathFileResult");

        ListGridField version = new ListGridField("version", "Version");
        version.setWidth(60);
        ListGridField statuss = new ListGridField("status", "Status");
        statuss.setWidth(60);
        ListGridField date = new ListGridField("dateExecution", "Execution Start Time");
        date.setWidth(120);
        ListGridField buttonField = new ListGridField("buttonField", "Result");
        buttonField.setAlign(Alignment.CENTER);
        buttonField.setWidth(60);

        grid.setFields(
                FieldUtil.getIconGridField("statusIcon"),
                executionID,
                new ListGridField("name", "Query Execution Name"),
                new ListGridField("query", "Query"),
                version,
                new ListGridField("executer", "Executer"),
                date,
                statuss,
                buttonField,
                pathFileResult);


        executionID.setHidden(true);
        pathFileResult.setHidden(true);
    }

    public void loadData() {
        final AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of queries Execution:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String[]> result) {
                modal.hide();
                List<QueryExecutionRecord> dataList = new ArrayList<QueryExecutionRecord>();

                for (String[] q : result) {

                    Timestamp ts = Timestamp.valueOf(q[5]);
                    dataList.add(new QueryExecutionRecord(q[0], q[1], q[2], q[3], q[4], ts, q[6], q[7], q[8]));


                }
                grid.setData(dataList.toArray(new QueryExecutionRecord[]{}));
                ds.setTestData(dataList.toArray(new QueryExecutionRecord[]{}));

            }
        };


        modal.show("Loading queries execution...", true);
        QueryService.Util.getInstance().getQueryHistory(callback);
    }

    public void setFilter() {

        if (state == false) {
            grid.setShowFilterEditor(false);
            state = true;
        } else {
            grid.setShowFilterEditor(true);
            state = false;
        }

    }

    public ListGridRecord[] getGridSelection() {
        return grid.getSelectedRecords();



    }
}
