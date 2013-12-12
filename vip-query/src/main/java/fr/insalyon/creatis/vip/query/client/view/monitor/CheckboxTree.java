/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;

/**
 *
 * @author nouha
 */
public class CheckboxTree extends AbstractFormLayout {
    private IButton saveButton;
    private IButton executeButton;
    private TextItem querynameField;
    private RichTextEditor description;
    private GinsengTreeNode rollOverRecord;
    String type;
    String name;
    TreeGrid treeGrid = new TreeGrid();
    boolean no = false;

    public CheckboxTree() {

        super("80%", "100%");
        final Tree GTree = new Tree();
        GTree.setModelType(TreeModelType.PARENT);
        GTree.setRootValue(1);
        GTree.setNameProperty("Name");
        GTree.setIdField("Id");
        GTree.setParentIdField("ReportsTo");
        GTree.setOpenProperty("isOpen");
        GTree.setData(new GinsengTree().getGinsengData());

        Tree BTree = new Tree();
        BTree.setModelType(TreeModelType.PARENT);
        BTree.setRootValue(1);
        BTree.setNameProperty("Name");
        BTree.setIdField("Id");
        BTree.setParentIdField("ReportsTo");
        BTree.setOpenProperty("isOpen");
        BTree.setData(new GinsengTree().getGinsengBaseData());

        final TreeGrid BTreeGrid = new TreeGrid();
        BTreeGrid.setHeight(70);
        BTreeGrid.setLoadDataOnDemand(false);
        BTreeGrid.setNodeIcon(QueryConstants.ICON_BASE);

        BTreeGrid.setFolderIcon(QueryConstants.ICON_PROPERTIES);
        BTreeGrid.setShowOpenIcons(false);
        BTreeGrid.setShowDropIcons(false);
        BTreeGrid.setClosedIconSuffix("");

        BTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        BTreeGrid.setAutoFetchData(true);
        BTreeGrid.setData(BTree);

        //pour monter les lignes bleu
        BTreeGrid.setShowSelectedStyle(true);
        BTreeGrid.setShowPartialSelection(true);
        BTreeGrid.setCascadeSelection(true);


        Canvas rollUnderCanvasProperties = new Canvas();
        rollUnderCanvasProperties.setAnimateFadeTime(1000);
        rollUnderCanvasProperties.setAnimateShowEffect(AnimationEffect.FADE);
        rollUnderCanvasProperties.setBackgroundColor("#00ffff");
        rollUnderCanvasProperties.setOpacity(50);
        //can also override ListGrid.getRollUnderCanvas(int rowNum, int colNum)  
        treeGrid.setRollUnderCanvasProperties(rollUnderCanvasProperties);
        treeGrid.setShowSelectionCanvas(true);
        treeGrid.setAnimateSelectionUnder(true);

        treeGrid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
            public void onSelectionUpdated(SelectionUpdatedEvent event) {
                ListGridRecord[] list = treeGrid.getSelectedRecords();
                int n = list.length;
                rollOverRecord = (GinsengTreeNode) list[n - 1];
                type = rollOverRecord.getAttribute("Type");
                name = rollOverRecord.getAttribute("Name");
                getQueryExplorerTb().setForm();
            }
        });

        treeGrid.setLoadDataOnDemand(false);
        treeGrid.setNodeIcon(QueryConstants.ICON_BASE);

        treeGrid.setFolderIcon(QueryConstants.ICON_PROPERTIES);
        treeGrid.setShowOpenIcons(false);
        treeGrid.setShowDropIcons(false);
        treeGrid.setClosedIconSuffix("");

        treeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);



        treeGrid.setAutoFetchData(true);
        treeGrid.setData(GTree);

        //pour monter les lignes bleu
        treeGrid.setShowSelectedStyle(true);
        treeGrid.setShowPartialSelection(true);
        treeGrid.setCascadeSelection(true);





        querynameField = FieldUtil.getTextItem("100%", null);


        description = new RichTextEditor();
        description.setHeight(80);

        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setControlGroups("styleControls", "editControls",
                "colorControls");
        addField100("Name", querynameField);

        this.addMember(WidgetUtil.getLabel("<b>Description</b>", 15));
        this.addMember(description);
        this.addMember(BTreeGrid);
        TreeGridField nameField = new TreeGridField("Name", "Name");
        TreeGridField typeField = new TreeGridField("Type", "Type");
        TreeGridField restriction = new TreeGridField("Restriction", "Restriction");
        TreeGridField select = new TreeGridField("Select", "View");
        select.setType(ListGridFieldType.BOOLEAN);
        select.setCanEdit(true);
        
        



        treeGrid.setFields(nameField,select,typeField, restriction);
        typeField.setHidden(true);
        this.addMember(treeGrid);
        HLayout layout = new HLayout();

        layout.setHeight(30);


        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVE,
                new ClickHandler() {
            ///nouha// String body_val=null;
            @Override
            public void onClick(ClickEvent event) {
                //rollOverRecord.setAttribute("Restriction", "sfkjsqfqsk");
            }
        });



        executeButton = WidgetUtil.getIButton("Execute", QueryConstants.ICON_LAUNCH,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            }
        });


        layout.addMember(saveButton);
        layout.addMember(executeButton);
        layout.setMembersMargin(10);

        this.setBorder("1px solid #C0C0C0");
        this.addMember(layout);
    }

    public String getType() {
        return type;

    }

    public void addRestriction(String value) {
        rollOverRecord.setAttribute("Restriction", value);
    }

    public void refrechGrid() {
        treeGrid.refreshFields();
    }

    public String getName() {
        return name;

    }

    public QueryExplorerTab getQueryExplorerTb() {

        QueryExplorerTab queryExplorerTab = (QueryExplorerTab) Layout.getInstance().
                getTab(QueryConstants.TAB_QUERYEXPLORER);
        return queryExplorerTab;
    }
}
