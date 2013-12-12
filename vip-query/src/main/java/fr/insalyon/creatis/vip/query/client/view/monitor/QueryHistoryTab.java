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
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
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
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
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
 * @author Nouha Boujelben
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
    boolean empty;
    private List<ParameterValue> dataList;

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


                    if (record.getAttribute("status") == Status.completed.toString()) {

                        button.setSrc(QueryConstants.ICON_LINK);
                        button.addClickHandler(new ClickHandler() {
                            public void onClick(ClickEvent event) {

                                Window.open(GWT.getModuleBaseURL() + "/filedownload?queryid="
                                        + record.getAttribute("queryExecutionID").toString() + "&path=" + record.getAttribute("pathFileResult") + "&name=" + record.getAttribute("name").toString(), "", "");
                            }
                        });
                    } else if (record.getAttribute("status") == Status.failed.toString()) {
                        button.setSrc(QueryConstants.ICON_ERROR);

                        button.addClickHandler(new ClickHandler() {
                            public void onClick(ClickEvent event) {
                                String error = record.getAttribute("pathFileResult");
                                if (error.length() < 70) {
                                    SC.say("Error", error);
                                } else {
                                    String[] words = error.toString().split(" ");
                                    int length = words.length;
                                    int max = 70;
                                    String msg = new String();
                                    for (String s : words) {
                                        int l = msg.length() + s.length() + 1;
                                        if (l > max) {
                                            msg += "<br>";
                                            max += 70;
                                            msg += s + " ";
                                        } else {
                                            msg += s + " ";
                                        }

                                    }
                                    
                                    /*
                                    int i = 0;
                                    String message = new String();
                                    while (i < n.length()) {
                                        if ((i + 65) > n.length()) {
                                            int k = n.length() - i;
                                            message += n.substring(i, k);
                                        } else {
                                            message += n.substring(i, i + 65) + "<br>";
                                        }
                                        i += 65;
                                        
                                    }
                                    * */

                                    SC.say("Error", msg);

                                }
                            }
                        });
                    }
                }
                if (record.getAttribute("status") == Status.completed.toString() || record.getAttribute("status") == Status.failed.toString()) {
                    return button;
                } else {
                    return null;
                }
            }

            ;
 
//canvas de type detailViewer 
            @Override
            protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {

                rollOverRecord = this.getRecord(rowNum);
                detailViewer = new DetailViewer();
                detailViewer.setWidth(200);
                DetailViewerField name = new DetailViewerField("name", "name");
                DetailViewerField type = new DetailViewerField("value", "value");
                detailViewer.setFields(name, type);
                
                dataList = new ArrayList<ParameterValue>();
                Long executionID = record.getAttributeAsLong("queryExecutionID");
                //appel rpc
                final AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("Unable to save Query Execution " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<String[]> result) {
                        for (String[] s : result) {
                            dataList.add(new ParameterValue(s[0], s[1]));
                        }
                        if (dataList.isEmpty()){
                        detailViewer.setVisible(false);}
                        else{
                        detailViewer.setData(dataList.toArray(new ParameterValue[]{}));
                        detailViewer.setBackgroundColor("white");
                        detailViewer.setBorder("1px solid gray");
                        detailViewer.setAutoFetchData(true);
                        }
                      
                    }
                };
                QueryService.Util.getInstance().getParameterValue(executionID, callback);
                return detailViewer;
            }
        ;
        };

        grid.addRowContextClickHandler(new RowContextClickHandler() {
            public void onRowContextClick(RowContextClickEvent event) {
                ListGridRecord record = event.getRecord();
                if (record.getAttribute("status") == Status.failed.toString()) {
                    Menu menu = new Menu();
                    final String queryExecutionID = record.getAttribute("queryExecutionID");

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
                            QueryService.Util.getInstance().updateQueryExecutionStatusWaiting(Status.waiting.toString(), Long.parseLong(queryExecutionID), callback);

                        }
                    });
                    menu.addItem(relaunch);


                    grid.setContextMenu(menu);
                } else if (record.getAttribute("status") == Status.waiting.toString() || record.getAttribute("status") == Status.running.toString()) {

                    Menu menu = new Menu();
                    final String queryExecutionID = record.getAttribute("queryExecutionID");
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
                            QueryService.Util.getInstance().updateQueryExecutionStatusFailed(Status.failed.toString(), Long.parseLong(queryExecutionID), callback);

                        }
                    });
                    menu.addItem(kill);
                    grid.setContextMenu(menu);
                } else {
                    Menu menu = new Menu();
                    menu = null;
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
        grid.setShowRecordComponents(true);
        grid.setShowRecordComponentsByCell(true);
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

        ListGridField statusFormatter = new ListGridField("statusFormatter", "Status");
        statusFormatter.setWidth(60);

        ListGridField date = new ListGridField("dateExecution", "Execution Start Time");
        date.setWidth(150);

        ListGridField dateEndExecution = new ListGridField("dateEndExecution", "Execution End Time");
        dateEndExecution.setWidth(150);

        ListGridField buttonField = new ListGridField("buttonField", "Result");
        buttonField.setAlign(Alignment.CENTER);
        buttonField.setWidth(60);
        grid.setFields(
                FieldUtil.getIconGridField("statusIcon"),
                executionID,
                new ListGridField("name", "Query Execution Name"),
                statusFormatter,
                new ListGridField("query", "Query"),
                date,
                version,
                new ListGridField("executer", "User"),
                dateEndExecution,
                statuss,
                buttonField,
                pathFileResult);
        executionID.setHidden(true);
        pathFileResult.setHidden(true);
        statuss.setHidden(true);
    }

    public void loadData() {
        
         String state=new String();
         
        if(CoreModule.user.isGroupAdmin(QueryConstants.QUERY_GROUP)||CoreModule.user.isSystemAdministrator()){
           state="admin";
        }
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
                String formatter = new String();
                for (String[] q : result) {

                   
                    if (q[6] == Status.completed.toString()) {
                        formatter = "Completed";
                    } else if (q[6] == Status.failed.toString()) {
                        formatter = "Failed";
                    } else if (q[6] == Status.running.toString()) {
                        formatter = "Running";
                    } else if (q[6] == Status.waiting.toString()) {
                        formatter = "Waiting";
                    }
                    dataList.add(new QueryExecutionRecord(q[0], q[1], q[2], q[3], q[4],q[5] , q[6], formatter, q[7], q[8],q[9]));


                }
                grid.setData(dataList.toArray(new QueryExecutionRecord[]{}));
                ds.setTestData(dataList.toArray(new QueryExecutionRecord[]{}));

            }
        };


        modal.show("Loading queries execution...", true);
        QueryService.Util.getInstance().getQueryHistory(state,callback);
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
