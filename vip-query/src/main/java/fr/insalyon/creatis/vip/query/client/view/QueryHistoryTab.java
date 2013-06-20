/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view;
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
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;


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
        grid.setFields(
                FieldUtil.getIconGridField("statusIco"),
                new ListGridField("queryName", "Query Execution Name"),
                new ListGridField("query", "Query"),
                new ListGridField("queryVersion", "Version"),
                new ListGridField("description", "Description"),
                new ListGridField("executer", "Executer"),
                new ListGridField("dateExecution", "Execution Start Time"),
                new ListGridField("status", "Status"),
                new ListGridField("dateEndExecution", "Execution End Time"),
                new ListGridField("urlResult", "Result Data"));
      
                
               
      }
    
}
