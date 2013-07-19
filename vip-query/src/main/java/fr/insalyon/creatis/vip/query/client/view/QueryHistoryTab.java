/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VisibilityMode;
import fr.insalyon.creatis.vip.query.client.view.monitor.HistoryToolStrip;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.query.client.bean.QueryRecord;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Boujelben
 */
public class QueryHistoryTab extends Tab {
     
     private  SectionStackSection searchSection;
     protected ModalWindow modal;
     protected ListGrid grid;
    
    
    
    public QueryHistoryTab() {
         
         
         
        this.setTitle(Canvas.imgHTML(QueryConstants.ICON_QUERYHISTORY) + "Query History");
        this.setID(QueryConstants.TAB_QUERYHISTORY);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
         configureGrid();
         modal = new ModalWindow(grid);
         loadData();

        VLayout vLayout = new VLayout();
        vLayout.addMember(new HistoryToolStrip(modal));

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);

        SectionStackSection gridSection = new SectionStackSection();
        gridSection.setCanCollapse(false);
        gridSection.setShowHeader(false);
        gridSection.addItem(grid);

         searchSection =new SectionStackSection();

        sectionStack.setSections(gridSection, searchSection);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);
        
        
        
     }
      private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowRowNumbers(true);
        grid.setShowEmptyMessage(true);
        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        grid.setEmptyMessage("<br>No data available.");
       ListGridField linkField =new ListGridField("urlResult", "Result Data");
       linkField.setType(ListGridFieldType.LINK);
       
        grid.setFields(
                FieldUtil.getIconGridField("statusIco"),
                new ListGridField("name", "Query Execution Name"),
                new ListGridField("query", "Query"),
                new ListGridField("version", "Version"),
                new ListGridField("executer", "Executer"),
                new ListGridField("dateExecution", "Execution Start Time"),
                new ListGridField("status", "Status"),
                linkField);
           
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
                   
                    
                    dataList.add(new QueryExecutionRecord (q[0],q[1],q[2],q[3],q[4],q[5],q[6]));
                }
                grid.setData(dataList.toArray(new QueryExecutionRecord[]{}));
            }
        };
        modal.show("Loading queries execution...", true);
        QueryService.Util.getInstance().getQueryHistory(callback);
    }
    
}
