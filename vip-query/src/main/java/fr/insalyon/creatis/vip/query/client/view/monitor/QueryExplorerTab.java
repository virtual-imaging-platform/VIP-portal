/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;


import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import com.smartgwt.client.widgets.tab.events.TabTitleChangedEvent;
import com.smartgwt.client.widgets.tab.events.TabTitleChangedHandler;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;

/**
 *
 * @author nouha
 */
public class QueryExplorerTab extends Tab {

    private final CheckboxTree checkboxTree;
    private final CheckboxTreeRestriction checkboxTreeRestriction;
    

    public QueryExplorerTab() {

        checkboxTree = new CheckboxTree();
        checkboxTreeRestriction=new CheckboxTreeRestriction();
        HLayout layout=new HLayout();
        this.setTitle(Canvas.imgHTML(QueryConstants.ICON_EXPLORE) + "Query Explorer");
        this.setID(QueryConstants.TAB_QUERYEXPLORER);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        layout.setMembersMargin(5);
        layout.addMember(checkboxTree);
        layout.addMember(checkboxTreeRestriction);
        this.setPane(layout);

    }

    public CheckboxTree getCheckboxTree() {
        return checkboxTree;
    }
    
    public CheckboxTree.EmployeeTreeNode getRollOverRecord(){
    
        return checkboxTree.getRollOverRecord();
       
    }
    public String getType(){
    
        return checkboxTree.getType();
       
    }
     public void setForm(){
         checkboxTreeRestriction.setForm();
     }
     
      public String getName(){
    
        return checkboxTree.getName();
       
    }
    
}
