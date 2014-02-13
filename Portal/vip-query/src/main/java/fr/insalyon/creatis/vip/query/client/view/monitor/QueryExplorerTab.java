/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;

/**
 *
 * @author Nouha Boujelben
 */
public class QueryExplorerTab extends Tab {

    private final CheckboxTree checkboxTree;
    private final CheckboxTreeRestriction checkboxTreeRestriction;
    private final CheckboxesOption  checkBoxesOption;

    public QueryExplorerTab() {

        checkboxTree = new CheckboxTree();
        checkboxTreeRestriction = new CheckboxTreeRestriction();
        checkBoxesOption=new CheckboxesOption(checkboxTree);

        HLayout layout = new HLayout();
        layout.setWidth100();
        VLayout vLayout = new VLayout();
        vLayout.setWidth(400);
        
        this.setTitle(Canvas.imgHTML(QueryConstants.ICON_EXPLORE) + "Query Explorer");
        this.setID(QueryConstants.TAB_QUERYEXPLORER);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        layout.setMembersMargin(5);
        layout.addMember(checkboxTree, 0);
        vLayout.setMembersMargin(5);
        vLayout.addMember(checkboxTreeRestriction);
        vLayout.addMember(checkBoxesOption);  
        layout.addMember(vLayout); 
        this.setPane(layout);
        checkboxTree.loadData();

    }

    public CheckboxTree getCheckboxTree() {
        return checkboxTree;
    }

    public String getType() {

        return checkboxTree.getType();

    }

    public void setForm(String restriction) {
        checkboxTreeRestriction.setForm(restriction);
    }

    public String getName() {

        return checkboxTree.getName();

    }

    public void addRestriction(String value) {
        checkboxTree.addRestriction(value);
    }

    public void refrechGrid() {
        checkboxTree.refrechGrid();
    }
}
