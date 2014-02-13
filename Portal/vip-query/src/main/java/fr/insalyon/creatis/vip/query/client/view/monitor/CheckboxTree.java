/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.Visibility;
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
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.events.EditorEnterEvent;
import com.smartgwt.client.widgets.grid.events.EditorEnterHandler;

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
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import fr.insalyon.creatis.vip.query.client.rpc.EndPointSparqlService;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.ParameterValue;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import fr.insalyon.creatis.vip.query.client.view.QueryExecutionRecord;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nouha Boujelben
 */
public class CheckboxTree extends AbstractFormLayout {

    private IButton saveButton;
    private IButton executeButton;
    private TextItem queryNameField;
    private RichTextEditor description;
    private   GinsengTreeNode rollOverRecord;
    private  GinsengTreeNode rollOverRecord2;
    private  GinsengTreeNode rollOverRecord3;
    public  Label body;
    private int treeLength;
    public  DynamicForm formLimit;
    
  
    private String type;
    private String name;
    TreeGrid treeGrid = new TreeGrid();
    TreeGrid bTreeGrid = new TreeGrid();
    List<GinsengTreeNode> ginsengData;
    QueryHistoryTab tab;
    CheckboxItem isPublic;
    boolean state = false;
    boolean isPublicValue = false;
    private Tree bTree;
    private Tree gTree;
    public TextItem limitValue;

    public  CheckboxTree() {

        super("100%", "100%");
        configure();

    }

    public void configure() {
        gTree = new Tree();
        gTree.setModelType(TreeModelType.PARENT);
        gTree.setRootValue(1);
        gTree.setNameProperty("Name");
        gTree.setIdField("Id");
        gTree.setParentIdField("ReportsTo");
        gTree.setOpenProperty("isOpen");
        gTree.setIsFolderProperty("isFolder");


        bTree = new Tree();
        bTree.setModelType(TreeModelType.PARENT);
        bTree.setRootValue(1);
        bTree.setNameProperty("Name");
        bTree.setIdField("Id");
        bTree.setParentIdField("ReportsTo");
        bTree.setOpenProperty("isOpen");
        TreeNode[] ginsengBaseData = new TreeNode[]{
        new GinsengTreeNode("2", "1", "http://e-ginseng.org/graph/I3S", "nothing", false, "",true,"")};
        bTree.setData(ginsengBaseData);


        bTreeGrid.setHeight(70);
        bTreeGrid.setLoadDataOnDemand(false);
        bTreeGrid.setNodeIcon(QueryConstants.ICON_BASE);
        bTreeGrid.setFolderIcon(QueryConstants.ICON_PROPERTIES);
        bTreeGrid.setShowOpenIcons(false);
        bTreeGrid.setShowDropIcons(false);
        bTreeGrid.setClosedIconSuffix("");
        bTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        bTreeGrid.setAutoFetchData(true);
        bTreeGrid.setData(bTree);
        //pour monter les lignes bleu
        //default all data base selected
        bTreeGrid.setSelectionProperty("isSelected");
        bTreeGrid.setShowSelectedStyle(true);
        bTreeGrid.setShowPartialSelection(true);
        bTreeGrid.setCascadeSelection(true);

        bTreeGrid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
            public void onSelectionUpdated(SelectionUpdatedEvent event) {
                generateBody();
            }
        });

        treeGrid.setSelectionProperty("isSelected");
        treeGrid.addFolderOpenedHandler((new FolderOpenedHandler() {
            public void onFolderOpened(FolderOpenedEvent event) {
                if (event.getNode().getAttribute("PropName") != "empty" && event.getNode().getAttribute("open") == "false") {

                    loadProperties(event.getNode().getAttribute("PropName"), event.getNode().getAttribute("Id"), event.getNode().getAttribute("Name"));
                    event.getNode().setAttribute("open", true);
                }
            }
        }));


        treeGrid.setShowSelectionCanvas(true);
        treeGrid.setAnimateSelectionUnder(true);
        treeGrid.addCellClickHandler((new CellClickHandler() {
            public void onCellClick(CellClickEvent event) {
                if (event.getColNum() == 1) {
                    rollOverRecord = (GinsengTreeNode) event.getRecord();
                    type = rollOverRecord.getAttribute("Type");
                    name = rollOverRecord.getAttribute("Name");
                    getQueryExplorerTb().setForm(event.getRecord().getAttribute("Restriction"));
                }
            }
        }));



        treeGrid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
            public void onSelectionUpdated(SelectionUpdatedEvent event) {
                generateBody();
            }
        });

        treeGrid.setWidth100();
        treeGrid.setLoadDataOnDemand(false);
        treeGrid.setNodeIcon(QueryConstants.ICON_BASE);
        treeGrid.setFolderIcon(QueryConstants.ICON_PROPERTIES);
        treeGrid.setShowOpenIcons(false);
        treeGrid.setShowDropIcons(false);
        treeGrid.setClosedIconSuffix("");
        treeGrid.setSelectionType(SelectionStyle.MULTIPLE);
        treeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        treeGrid.setAutoFetchData(true);
        //pour monter les lignes bleu
        treeGrid.setShowSelectedStyle(true);
        treeGrid.setShowPartialSelection(true);
        //treeGrid.setData(GTree);
        treeGrid.setCascadeSelection(true);
        queryNameField = FieldUtil.getTextItem("100%", null);

        description = new RichTextEditor();
        description.setHeight(80);
        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setControlGroups("styleControls", "editControls",
                "colorControls");
        addField100("Query Name", queryNameField);

        this.addMember(WidgetUtil.getLabel("<b>Query Description</b>", 15));
        this.addMember(description);
        this.addMember(WidgetUtil.getLabel("<b>Data Sources</b>", 15));
        this.addMember(bTreeGrid);
        TreeGridField nameField = new TreeGridField("Name", "Name");
        TreeGridField typeField = new TreeGridField("Type", "Type");
        TreeGridField restriction = new TreeGridField("Restriction", "Restriction");
     
        TreeGridField groupBy = new TreeGridField("GroupBy", "Group By");
        TreeGridField orderBy = new TreeGridField("OrderBy", "Order By");
        //orderBy.setType(ListGridFieldType.BOOLEAN);
        FormItem editorType = new RadioGroupItem();
        orderBy.setEditorType(editorType);
        orderBy.setType(ListGridFieldType.BOOLEAN);
        orderBy.setCanEdit(true);
        groupBy.setType(ListGridFieldType.BOOLEAN);
        groupBy.setCanEdit(true);
 
        treeGrid.setFields(nameField, typeField, restriction, groupBy, orderBy);
        treeGrid.setCanResizeFields(true);
        typeField.setHidden(true);
        orderBy.setHidden(true);
        groupBy.setHidden(true);

        this.addMember(WidgetUtil.getLabel("<b>Selected Fields</b>", 15));
        this.addMember(treeGrid);
        HLayout layout = new HLayout();
        layout.setHeight(30);
        VLayout vlayout1 = new VLayout();
        vlayout1.setHeight(60);
        vlayout1.setMembersMargin(5);

        VLayout vlayout2 = new VLayout();
        vlayout2.setHeight(60);
        vlayout2.setMembersMargin(5);
        body = new Label("");
        body.setHeight(100);
        body.setWidth100();
        body.setBackgroundColor("White");
        body.setStyleName("exampleTitle");
        body.setVisibility(Visibility.HIDDEN);
        body.setAnimateTime(1000); // milliseconds

        isPublic = new CheckboxItem();
        isPublic.setName("Public");
        isPublic.setTitle("Make Query Public");
        isPublic.setPrompt("A public query can be executed by all GINSENG users");
        isPublic.setRedrawOnChange(true);
        isPublic.setValue(false);
        isPublic.setShowTitle(false);
        DynamicForm form = new DynamicForm();
        form.setFields(isPublic);
        form.setWidth100();
        isPublic.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {
            public void onChange(ChangeEvent event) {
                isPublicValue = (Boolean) event.getValue();
            }
        });

       
        formLimit = new DynamicForm();

        limitValue=new TextItem();
        limitValue.setTitle("Limit");
        limitValue.setName("Limit");

        limitValue.setWidth(100);
        /**limitValue.setKeyPressFilter("[0-9]");
        limitValue.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                event.getItem().validate();
            }
        });
        * */
        formLimit.setFields(limitValue);
        limitValue.setVisible(false);

        formLimit.setWidth100();
       


      

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVE,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Query q = new Query(queryNameField.getValueAsString().trim());
                try {
                    save(q);
                } catch (QueryException ex) {
                    Logger.getLogger(CheckboxTree.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });


        executeButton = WidgetUtil.getIButton("Execute", QueryConstants.ICON_LAUNCH,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (body.getContents().isEmpty() || body.getContents() == "") {
                    generateBody();
                }
                final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Layout.getInstance().setWarningMessage("Unable to save Query Execution " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Long result) {

                        Layout.getInstance().setNoticeMessage("The query was successfully launched");
                        tab = new QueryHistoryTab();
                        Layout.getInstance().addTab(tab);

                    }
                };

                QueryService.Util.getInstance().addQueryExecution(new QueryExecution("waiting", queryNameField.getValueAsString(), body.getContents()), callback);
                tab.loadData();
            }
        });

        layout.addMember(saveButton);
        layout.addMember(executeButton);
        layout.setMembersMargin(10);

        vlayout1.addMember(form);
        vlayout1.addMember(layout);

        vlayout2.addMember(formLimit);
        //vlayout2.addMember(forme);

        HLayout mainLayout=new HLayout(); 
        mainLayout.addMember(vlayout1);
        mainLayout.addMember(vlayout2);
        this.setBorder("1px solid #C0C0C0");
        this.addMember(mainLayout);
        this.addMember(body);
    }

    private void save(Query query) throws QueryException {

        final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to save Query <br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Long result) {
                if (body.getContents().isEmpty() || body.getContents() == "") {
                    generateBody();
                }

                String desc = description.getValue().trim();
                desc = desc.replaceAll("<br><br>", "<br>");
                desc = desc.replaceAll("<div><br></div>", "");
                while (desc.indexOf("<br>") == 0) {
                    desc = desc.replaceFirst("<br>", "");
                }

                while (desc.lastIndexOf("<br>") == desc.length() - 4) {
                    desc = desc.substring(0, desc.length() - 4);
                }

                savev(new QueryVersion(1L, result, desc, body.getContents(), isPublicValue));
                


            }
        };

        QueryService.Util.getInstance().add(query, callback);
    }

    private void savev(QueryVersion version) {
        final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                // WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to save query:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Long result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                Layout.getInstance().setNoticeMessage("The query was successfully added");

            }
        };
        QueryService.Util.getInstance().addVersion(version, true, callback);

    }

    public String getType() {
        return type;

    }

    public void addRestriction(String value) {
        rollOverRecord.setAttribute("Restriction", value);
        treeGrid.selectRecord(rollOverRecord);

    }

    public void refrechGrid() {
        treeGrid.refreshFields();
    }

    public String getName() {
        return name;

    }

    public  void generateBody() {
        body.setContents("");
        body.setContents(body.getContents() + " PREFIX semehr: &lt;http://www.mnemotix.com/ontology/semEHR#&gt;");
        ListGridRecord[] list = treeGrid.getSelectedRecords();
        int n = list.length;
        //Bloc select

        for (int i = 0; i < n; i++) {
            rollOverRecord2 = (GinsengTreeNode) list[i];
            if (i == 0) {
                body.setContents(body.getContents() + "<br/>" + "Select" + " " + "?" + rollOverRecord2.getAttribute("Name").toLowerCase());
            } else {

                body.setContents(body.getContents() + " " + "?" + rollOverRecord2.getAttribute("Name").toLowerCase());

            }
        }
        //Bloc from
        ListGridRecord[] list2 = bTreeGrid.getSelectedRecords();
        int n1 = list2.length;


        for (int i = 0; i < n1; i++) {
            rollOverRecord3 = (GinsengTreeNode) list2[i];

            body.setContents(body.getContents() + "<br/>" + "From " + "&lt;" + rollOverRecord3.getAttribute("Name") + "&gt;");
        }

        //bloc where
        for (int i = 0; i < n; i++) {
            rollOverRecord2 = (GinsengTreeNode) list[i];
            if (i == 0) {
                body.setContents(body.getContents() + "<br/>" + "WHERE{" + "<br/>" + rollOverRecord2.getAttribute("Value") + "." + "<br/>");
            } else if (i == n - 1) {
                body.setContents(body.getContents() + rollOverRecord2.getAttribute("Value") + "<br/>");
            } else {
                body.setContents(body.getContents() + rollOverRecord2.getAttribute("Value") + "." + "<br/>");
            }
        }
        //Bloc filter

        for (int i = 0; i < n; i++) {
            rollOverRecord2 = (GinsengTreeNode) list[i];
            if (rollOverRecord2.getAttribute("Restriction") != "") {
                if (rollOverRecord2.getAttribute("Restriction").indexOf("regex") != -1) {
                    body.setContents(body.getContents() + "FILTER regex(" + "?" + rollOverRecord2.getAttribute("Name").toLowerCase() + "," + "&quot;" + rollOverRecord2.getAttribute("Restriction").replaceAll("regex:", "") + "&quot;" + ")" + "<br/>");
                } else {
                    body.setContents(body.getContents() + "FILTER (" + "?" + rollOverRecord2.getAttribute("Name").toLowerCase() + " " + rollOverRecord2.getAttribute("Restriction") + ")" + "<br/>");
                }
            }
        }
        //
        body.setContents(body.getContents() + "}");

        //Bloc order By

        for (int i = 0; i < n; i++) {
            rollOverRecord2 = (GinsengTreeNode) list[i];
            if (rollOverRecord2.getAttribute("OrderBy") == "true") {

                body.setContents(body.getContents() + "<br/>" + "order by " + "?" + rollOverRecord2.getAttribute("Name").toLowerCase().replaceAll(" ", ""));

            }
        }
        //Bloc Group By

        for (int i = 0; i < n; i++) {
            rollOverRecord2 = (GinsengTreeNode) list[i];
            if (rollOverRecord2.getAttribute("GroupBy") == "true") {

                body.setContents(body.getContents() + "<br/>" + "group by " + "?" + rollOverRecord2.getAttribute("Name").toLowerCase().replaceAll(" ", ""));

            }
        }

        //Bloc Limit
        if (!limitValue.getValueField().equals(null) ) {
            
            body.setContents(body.getContents() + "<br/>" + "Limit " + limitValue.getValue().toString());
        }
        //else{
       // body.setContents(body.getContents());
       // }
    }
    public QueryExplorerTab getQueryExplorerTb() {

        QueryExplorerTab queryExplorerTab = (QueryExplorerTab) Layout.getInstance().
                getTab(QueryConstants.TAB_QUERYEXPLORER);
        return queryExplorerTab;
    }

    public void loadData() {

        final AsyncCallback<List<String[]>> callback;
        callback = new AsyncCallback<List<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get result" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String[]> result) {

                int i = 1;
                ginsengData = new ArrayList<GinsengTreeNode>();

                for (String[] s : result) {
                    i++;
                    ginsengData.add(new GinsengTreeNode("" + i, "1", s[0].replace("@en", "").replaceAll(" ", ""), s[0].replace("@en", "").replaceAll(" ", ""), QueryConstants.ICON_BASE, "string", false, "", false, "?" + s[0].toLowerCase().replace("@en", "").replaceAll(" ", "") + " rdf:type " + s[1].replace("http://www.mnemotix.com/ontology/semEHR#", "semehr:"), true, false, false, false));
                }
                gTree.setData(ginsengData.toArray(new GinsengTreeNode[]{}));
                treeGrid.setData(gTree);
                treeLength = i;
            }
        };
        EndPointSparqlService.Util.getInstance().getUrlResultFormatTable("label", "x", callback);
    }

    private void loadProperties(String prop, final String nodeParent, final String nodeParentName) {

        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + "\n"
                + "PREFIX semehr: <http://www.mnemotix.com/ontology/semEHR#> " + "\n"
                + "select ?property ?label ?range (bound(?class) as ?isClass) " + "\n"
                + "WHERE {" + "\n"
                + " {" + "\n"
                + " {" + "\n"
                + " ?property rdfs:domain semehr:" + prop + "\n"
                + " }" + "\n"
                + " UNION" + "\n"
                + " {" + "\n"
                + " ?property rdfs:domain ?class ." + "\n"
                + " semehr:" + prop + " rdfs:subClassOf ?class" + "\n"
                + " }" + "\n"
                + " }" + "\n"
                + " ?property rdfs:label ?label" + "\n"
                + " optional {" + "\n"
                + " ?property rdfs:range ?range ." + "\n"
                + " optional {" + "\n"
                + " ?range a ?class" + "\n"
                + " filter ( ?class = rdfs:Class )" + "\n"
                + " }" + "\n"
                + " }" + "\n"
                + " }" + "\n";

        final AsyncCallback<List<String[]>> callback;
        callback = new AsyncCallback<List<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get result" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String[]> result) {
                for (String[] s : result) {
                    treeLength++;
                    //empty c'est une propriété
                    if (s[3] == "false^^http://www.w3.org/2001/XMLSchema#boolean") {
                        gTree.add(new GinsengTreeNode("" + treeLength, nodeParent, s[0].replace("@en", "").replaceAll(" ", ""), s[2], QueryConstants.ICON_ATTRIBUT, s[2], false, "", false, "?" + nodeParentName.toLowerCase() + " " + s[1].replace("http://www.mnemotix.com/ontology/semEHR#", "semehr:") + " ?" + s[0].toLowerCase().replace("@en", "").replaceAll(" ", ""), false, false, false, false), gTree.findById(nodeParent));
                    } else {
                        gTree.add(new GinsengTreeNode("" + treeLength, nodeParent, s[0].replace("@en", "").replaceAll(" ", ""), s[2].substring(s[2].indexOf("#") + 1), QueryConstants.ICON_BASE, "string", false, "", false, "?" + nodeParentName.toLowerCase() + " " + s[1].replace("http://www.mnemotix.com/ontology/semEHR#", "semehr:") + " ?" + s[0].toLowerCase().replace("@en", "").replaceAll(" ", "") + "." + "?" + s[0].toLowerCase().replace("@en", "").replaceAll(" ", "") + " rdf:type " + s[2].replace("http://www.mnemotix.com/ontology/semEHR#", "semehr:"), true, false, false, false), gTree.findById(nodeParent));
                    }

                }

            }
        };
        EndPointSparqlService.Util.getInstance().getUrlResultFormatTable(query, "label", "property", "range", "isClass", callback);
    }
}
