/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
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
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.common.ToolstripLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.system.user.UserRecord;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.List;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryRecord;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersionRecord;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.rpc.QueryServiceAsync;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import fr.insalyon.creatis.vip.query.client.view.QueryMakerTab;
import java.sql.Timestamp;
/**
 *
 * @author Boujelben
 */
public class QueryLayout extends VLayout {
    private boolean sb;
    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public QueryLayout() {
        this.setWidth100();
        this.setHeight(200);
        this.setOverflow(Overflow.AUTO);
        configureActions();
        configureGrid();
        modal = new ModalWindow(grid);
        loadData();
    }
    
    
    
    private void configureActions() {
        ToolstripLayout toolstrip = new ToolstripLayout();
        toolstrip.addMember(WidgetUtil.getSpaceLabel(15));
        LabelButton addButton = new LabelButton("Add Query", CoreConstants.ICON_ADD);
        addButton.setWidth(150);
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              setEdit(false,"","","");              
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

    
    
    
    
    private void configureGrid() {
        grid = new ListGrid(){
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
                             
                            //  setQuery(false);
                                   
                        }
                    }); 
                    ImgButton deleteImg = getImgButton(CoreConstants.ICON_DELETE, "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final String version = rollOverRecord.getAttribute("queryversionID");
                            final Long versionid=new Long(version);    
                            SC.ask("Do you really want to remove this Version \""
                                    + version + "\"?", new BooleanCallback() {
                                @Override
                                public void execute(Boolean value) {
                                    if (value) {
                                        removeVersion(versionid);
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
        };
       grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowRollOverCanvas(true);
        
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");
        ListGridField idversion=new ListGridField("queryversionID", "queryversionID");
        grid.setFields(new ListGridField("name", "Name"),
               
                new ListGridField("dateCreation", "Date Creation"),
                
                 new ListGridField("version", "Version"),
                idversion);
       grid.setSortField("name");
       idversion.setHidden(true);
       grid.setSortDirection(SortDirection.ASCENDING);  
       
       grid.addCellClickHandler(new CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {      
           setQuery(true);
                   }
    
                   });
              
                
            
        
        
        
        this.addMember(grid);
    }
    
   

    public void loadData() {
         
           
           
        final AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of queries:<br />" + caught.getMessage());
            }
 
            @Override
            public void onSuccess(List<String[]>result) {
                modal.hide();
               List<QueryRecord> dataList = new ArrayList<QueryRecord>();

                for (String[] q : result) {
                    
                    dataList.add(new QueryRecord(q[0],q[1],q[2],q[3]));
                }
                grid.setData(dataList.toArray(new QueryRecord[]{}));
        }
        };
            
      
        QueryService.Util.getInstance().getQureies(callback);
          
    }
    
     
    private void removeVersion(Long versionid){

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove query Version:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("The query version was successfully removed!");
                loadData();
            }
        };
        modal.show("Removing application '" + versionid + "'...", true);
        
        QueryService.Util.getInstance().removeVersion(versionid, callback);
    }
    
     private void setEdit(boolean savebutton,String name, String desciption, String body) {

        QueryMakerTab queryTab = (QueryMakerTab) Layout.getInstance().
                getTab(QueryConstants.TAB_QUERYMAKER);
        
        queryTab.setQuery(savebutton,name, desciption, body);
        
       
    }
    
     private void setQuery(boolean sbb){
           String version = rollOverRecord.getAttribute("queryversionID");
           Long versionid=new Long(version);
            sb=sbb;
            final AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to get list of queries:<br />" + caught.getMessage());
            }
 
            @Override
            public void onSuccess(List<String[]>result) {

                for (String[] q : result) {
                   
                    setEdit(sb,q[0],q[1],q[2]);
                }
            }
        };
           QueryService.Util.getInstance().getQuerie(versionid, callback);
        
         
    
                }

}
        
         

