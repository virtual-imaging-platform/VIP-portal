/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VisibilityMode;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
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
     private DetailViewer detailViewer ;
     DataSource ds;
     boolean state=true;
    

    
    
    
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

        grid = new ListGrid(){
             @Override  
            protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {  
                
              detailViewer = new DetailViewer();
                
              detailViewer.setWidth(200); 
              DetailViewerField name=new DetailViewerField("name", "name");
              DetailViewerField type=new DetailViewerField("value", "value");
              detailViewer.setFields(name,type);
               
              Long executionID=record.getAttributeAsLong("queryExecutionID");
                //appel rpc
                final AsyncCallback <List<String[]>> callback = new AsyncCallback<List<String[]>>() {
                     @Override
                     public void onFailure(Throwable caught) {
                
                     Layout.getInstance().setWarningMessage("Unable to save Query Execution " + caught.getMessage());
                     }
                    
                     @Override
                    public void onSuccess(List<String[]> result) {
                           List<ParameterValue> dataList = new ArrayList<ParameterValue>(); ;
                         for(String[] s:result)
                         {
                       
                      // DetailViewerField name=new DetailViewerField(s[0], s[1]);
                       dataList.add(new ParameterValue(s[0],s[1]));
                       
                
                         }
                   detailViewer.setData(dataList.toArray(new ParameterValue[]{}));
                   detailViewer.setEmptyMessage("No parameters to display");
                   detailViewer.setEmptyMessageStyle("1px solid black center");
                   
                   detailViewer.setBorder("1px solid gray");
                    
                   
                          }
                    };
                   QueryService.Util.getInstance().getParameterValue(executionID, callback);
          
                 return  detailViewer;
                
            }  
             
        };
        ds=new Data();
        
        grid.setCanHover(true);
        grid.setShowHover(true);
        grid.setShowHoverComponents(true); 
        grid.setWidth100();
        grid.setHeight100();
        //
        grid.setFilterOnKeypress(true);
       // loadData();
        grid.setDataSource(ds);
        grid.setAutoFetchData(Boolean.TRUE);
       
        grid.setDataFetchMode(FetchMode.LOCAL);
        
        grid.setShowAllRecords(false);
        grid.setShowRowNumbers(true);
        grid.setShowEmptyMessage(true);
        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        
        grid.setEmptyMessage("<br>No data available");
        linkField =new ListGridField("urlResult", "Result Data");
        linkField.setType(ListGridFieldType.LINK);
        linkField.setWidth(150);  
        linkField.setAlign(Alignment.CENTER);  
        linkField.setLinkText(Canvas.imgHTML(QueryConstants.ICON_LINK, 16, 16, "info", "align=center", null)); 
         ListGridField executionID=new ListGridField("queryExecutionID", "queryExecutionID");
        
        ListGridField version = new ListGridField("version", "Version");
        version.setWidth(60);
        ListGridField statuss = new ListGridField("status", "Status");
        statuss.setWidth(60);
        ListGridField date =new ListGridField("dateExecution", "Execution Start Time");
        date.setWidth(120);
        
        
        grid.setFields(
                FieldUtil.getIconGridField("statusIcon"),
                executionID,
                new ListGridField("name", "Query Execution Name"),
                new ListGridField("query", "Query"),
                version,
                new ListGridField("executer", "Executer"),
                date,
                statuss,
                linkField
                );
        
           executionID.setHidden(true);
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

                for (String[] q : result ) {
                   
                    Timestamp ts = Timestamp.valueOf(q[5]);
                    dataList.add(new QueryExecutionRecord (q[0],q[1],q[2],q[3],q[4],ts,q[6],q[7]));
                    
                
                }
                grid.setData(dataList.toArray(new QueryExecutionRecord[]{}));
                
               
                ds.setTestData(dataList.toArray(new QueryExecutionRecord[]{}));
               
                }
                    
                
            
        };
        
       
        modal.show("Loading queries execution...", true);
        QueryService.Util.getInstance().getQueryHistory(callback);
    }
          
         public void setFilter()
         {
           
             if (state==false){
             grid.setShowFilterEditor(false);
             state=true;
             }
             else {
             grid.setShowFilterEditor(true);
             state=false;
}
             
         }
        
    public ListGridRecord[] getGridSelection() {
         return grid.getSelectedRecords();
         
        
         
          }
    
    
  
   
    
}
