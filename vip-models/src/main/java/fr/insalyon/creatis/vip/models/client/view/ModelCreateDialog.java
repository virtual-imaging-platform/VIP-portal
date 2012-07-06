/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.*;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.TextItem;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.RichTextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ColumnTree;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import java.util.List;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import java.util.LinkedHashMap;

/**
 *
 * @author cervenansky
 */
public class ModelCreateDialog extends Window {

    private ButtonItem validateBt = null;
    private TextItem searchText = null;
    private TextItem resultText = null;
    private ModelServiceAsync ms = null;
    private CheckboxItem meshCheck = null;
    private CheckboxItem voxelCheck = null;
    private CheckboxItem LUTCheck = null;
    private ComboBoxItem physicalCombo = null;
    private CheckboxItem anaCheck = null;
    private CheckboxItem pathoCheck = null;
    private CheckboxItem geoCheck = null;
    private CheckboxItem forCheck = null;
    private CheckboxItem exCheck = null;
    private TextItem valueLabel = null;
    private SearchTreeNode[] resultData;
    private Tree resultTree;
    private TreeGrid resultTreeGrid;
    private TreeGridField labelField;
    private HLayout hLayout = new HLayout();
    Record selectRecord = null;
    private String ontoName;
    DynamicForm extensionForm = null;
    private ColumnTree columnTree;
    private RadioGroupItem typeRadio = null;
    private ModelTreeGrid tree = null;
    private int tp = -1;
    private int ins = -1;
    private int type = -1;
    private String filename = "";

    public ModelCreateDialog(ModelTreeGrid treegrid) {
        ms = ModelService.Util.getInstance();
        tree = treegrid;
        init();
    }

    public void addInfo(int extension, int timepoint, int instant, String name) {
        manageExt(extension);
        type = extension;
        tp = timepoint;
        ins = instant;
        filename = name;
        this.setTitle("add Object at timepoint: " + timepoint + " instant: " + instant);
    }

    public void init() {

        this.setTitle("add Object");
        this.setWidth(980);
        this.setHeight(610);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();
        // this.setDismissOnOutsideClick(true);
        // layer type
        meshCheck = new CheckboxItem();
        meshCheck.enable();
        meshCheck.setTitle("mesh2");

        voxelCheck = new CheckboxItem();
        voxelCheck.enable();
        voxelCheck.setTitle("voxel");

        LUTCheck = new CheckboxItem();
        LUTCheck.enable();
        LUTCheck.setTitle("LUT");

        LinkedHashMap<String, String> lutMap = new LinkedHashMap<String, String>();
        for(String lut : tree.getLutMap())
            lutMap.put(lut, lut);
        physicalCombo = new ComboBoxItem();
        physicalCombo.setTitle("physical parameters:");
        physicalCombo.setValueMap(lutMap);
        physicalCombo.disable();

        valueLabel = new TextItem();
        valueLabel.setName("label");
        valueLabel.setTitle("Label: ");


        LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
        typeMap.put("mesh", "mesh");
        typeMap.put("voxel", "voxel");
        typeMap.put("LUT", "LUT");
        typeMap.put("Map", "Map");

        typeRadio = new RadioGroupItem();
        typeRadio.setDefaultValue("exampleStyleOnline");
        typeRadio.setVertical(false);
        typeRadio.setShowTitle(false);
        typeRadio.setValueMap(typeMap);
        typeRadio.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                if ((String) event.getValue() == "voxel") {
                    
                    valueLabel.setCanEdit(true);
                    searchText.setCanEdit(true);
                    physicalCombo.disable();
                    type = 1;
                } else if ((String) event.getValue() == "mesh") {
                    type = 0;
                    
                    valueLabel.setCanEdit(false);
                    searchText.setCanEdit(true);
                    physicalCombo.disable();
                } else if ((String) event.getValue() == "LUT") {
                    type = 2;
                    
                    valueLabel.setCanEdit(false);
                    searchText.setValue("");
                    searchText.setCanEdit(false);
                    physicalCombo.enable();
                } else    if ((String) event.getValue() == "Map") {
                    type = 3;
                    
                    valueLabel.setCanEdit(false);
                    searchText.setValue("");
                    searchText.setCanEdit(false);
                    physicalCombo.enable();
                }
            }
        });

        extensionForm = new DynamicForm();
        extensionForm.setNumCols(12);
        extensionForm.setFields(typeRadio, physicalCombo, valueLabel);//meshCheck, voxelCheck, LUTCheck);//, physicalCombo, valueLabel);
        this.addItem(extensionForm);
        DynamicForm searchForm = new DynamicForm();
        searchForm.setNumCols(4);
        //  extForm.setFields(meshCheck, voxelCheck, LUTCheck, physicalCombo );

        // search part
        searchText = new TextItem();
        searchText.setWidth(400);
        searchText.setTitle("<nobr><b>Search - (add '~' at the end of the word for approximated search)</nobr></b>");
        searchText.setTitleOrientation(TitleOrientation.TOP);

        //searchText = new RichTextItem();
        //searchText.setWidth(400);
        ButtonItem searchBt = new ButtonItem();
        searchBt.setTitle("Search");
        searchBt.setTooltip("search throught the VIP ontology the nearest term");
        searchBt.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                boolean[] scope = new boolean[]{anaCheck.getValueAsBoolean(), pathoCheck.getValueAsBoolean(),
                    geoCheck.getValueAsBoolean(), forCheck.getValueAsBoolean(), exCheck.getValueAsBoolean()};
                String toSearch = searchText.getValueAsString();

                AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {

                    public void onFailure(Throwable caught) {
                        SC.warn("Cant search through the ontology");
                    }

                    public void onSuccess(List<String[]> result) {
                        int i = 0;
                        for (Record rec : resultTreeGrid.getRecords()) {
                            resultTreeGrid.removeData(rec);
                        }
                        resultData = new SearchTreeNode[result.size()];

                        for (String[] ii : result) {
                            resultTreeGrid.addData(new SearchTreeNode(ii[0], ii[1], ii[2]));
                        }
                    }
                };
                ms.searchWithScope(toSearch, scope, callback);
            }
        });

        searchForm.setFields(searchText, searchBt);
        this.addItem(searchForm);

        resultTree = new Tree();
        resultTree.setModelType(TreeModelType.PARENT);
        resultTree.setRootValue("1");
        resultTree.setNameProperty("name");
        resultTree.setIdField("ElementId");
        resultTree.setParentIdField("ReportsTo");
        resultTree.setOpenProperty("isOpen");
        resultTree.setData(resultData);


        TreeGridField fieldname = new TreeGridField("name");
        fieldname.setCanSort(false);
        TreeGridField fieldtype = new TreeGridField("type");
        fieldtype.setCanSort(false);
        TreeGridField fieldscore = new TreeGridField("score");
        fieldscore.setCanSort(false);

        resultTreeGrid = new TreeGrid();
        resultTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        resultTreeGrid.setAlign(Alignment.CENTER);
        resultTreeGrid.setSelectionType(SelectionStyle.SINGLE);
        resultTreeGrid.deselectAllRecords();
//        resultTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
//        resultTreeGrid.setSelectionType(SelectionStyle.SIMPLE);
//        resultTreeGrid.setShowSelectedStyle(false);
//        resultTreeGrid.setShowPartialSelection(true);
//        resultTreeGrid.setShowBackgroundComponent(true);
//        resultTreeGrid.deselectAllRecords();
        resultTreeGrid.setFields(fieldname, fieldtype, fieldscore);

        resultTreeGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                selectRecord = event.getRecord();

            }
        });


        columnTree = new ColumnTree();
        columnTree.setWidth100();
        columnTree.setHeight(205);
        columnTree.setAutoFetchData(true);
        columnTree.setNodeIcon("icons/16/person.png");
        columnTree.setFolderIcon("icons/16/person.png");
        columnTree.setShowOpenIcons(false);
        columnTree.setShowDropIcons(false);
        columnTree.setClosedIconSuffix("");

        columnTree.setShowHeaders(true);
        columnTree.setShowNodeCount(true);
        columnTree.setLoadDataOnDemand(false);


        // search part
//        TextAreaItem layerText = new TextAreaItem();
//layerText.setHeight(0);
//layerText.setWidth(0);
//        layerText.setTitle("<nobr><b>select the layer associated to the term</nobr></b>");
//       layerText.setT

        DynamicForm layerTextForm = new DynamicForm();
        layerTextForm.setNumCols(2);
        //      layerTextForm.setFields(layerText);        
        this.addItem(layerTextForm);

        // layer type
        anaCheck = new CheckboxItem();
        anaCheck.setValue(true);
        anaCheck.setTitle("Anatomy");

        pathoCheck = new CheckboxItem();
        pathoCheck.setValue(true);
        pathoCheck.setTitle("Pathology");

        geoCheck = new CheckboxItem();
        geoCheck.setValue(true);
        geoCheck.setTitle("Geometrical");

        forCheck = new CheckboxItem();
        forCheck.setValue(true);
        forCheck.setTitle("Foreign-body");

        exCheck = new CheckboxItem();
        exCheck.setValue(true);
        exCheck.setTitle("External-agent");

        DynamicForm layerForm = new DynamicForm();
        layerForm.setNumCols(12);
        layerForm.setFields(anaCheck, pathoCheck, geoCheck, forCheck, exCheck);
        this.addItem(layerForm);

        hLayout.setWidth100();
        hLayout.setHeight(400);
        //hLayout.addMember(columnTree);
        hLayout.addMember(resultTreeGrid);
        this.addItem(hLayout);



        ButtonItem submitButton = new ButtonItem("Validate");
        submitButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            public void onClick(
                    com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (type == 0 || type == 1) {
                    int label = 1;
                    if (type ==1) 
                        label = Integer.parseInt(valueLabel.getValueAsString());
                    tree.addObjectItem(tp, ins, type, filename, selectRecord.getAttribute("name"),
                            selectRecord.getAttribute("type"), label);
                } else if (type == 2 || type == 3) {
                    tree.addPhysicalItem(tp, ins, type, filename, getLayerforLUT(), physicalCombo.getValueAsString());
                }
                hide();
                refresh();
            }
        });
        DynamicForm validateForm = new DynamicForm();
        validateForm.setNumCols(8);
        validateForm.setMethod(FormMethod.POST);

        validateForm.setFields(submitButton);
        this.addItem(validateForm);
    }
//   @Override 
//   public HandlerRegistration addCloseClickHandler(CloseClickHandler handler) { 
//       SC.confirm("Close window?", new BooleanCallback() {	 
//           @Override public void execute(Boolean value) {
//               if(value){ //what to do to cancel the closing? 
//               } 
//           } 
//       }); //what should it return? 
//       return super.addCloseClickHandler(handler); }

    private void manageExt(int extension) {
        switch (extension) {
            // case mhd
            case 0:
                typeRadio.setDefaultValue("mesh");
                physicalCombo.disable();
                break;
            // case vtk
            case 1:
                typeRadio.setDefaultValue("voxel");
                physicalCombo.disable();
                break;
            case 2:
                typeRadio.setDefaultValue("LUT");
                physicalCombo.enable();
                break;
        }
    }

    public DynamicForm getFormExt() {
        return extensionForm;
    }

    public String getOntoName() {
        return selectRecord.getAttribute("name");
    }

    public void setInstant(int ins) {
    }

    public void setTimePoint(int tp) {
    }

    public String getLayerforLUT()
    {
        String title = "";
        if(anaCheck.getValueAsBoolean()==true)
            title = anaCheck.getTitle();
        else if(pathoCheck.getValueAsBoolean()==true)
             title = pathoCheck.getTitle();
        else if(geoCheck.getValueAsBoolean()==true)
             title = geoCheck.getTitle();
        else if(forCheck.getValueAsBoolean()==true)
             title = forCheck.getTitle();
        else if(exCheck.getValueAsBoolean()==true)
             title = exCheck.getTitle();
        if(title.isEmpty())
            return "";
        else
            return tree.getTypeFromMap(title).toString();
    }
    
    
    
    public String getLayer() {
        return selectRecord.getAttribute("type");
    }

    public int getLabel() {
        return Integer.parseInt(valueLabel.getValueAsString());
    }

    private void refresh() {
        for (Record rec : resultTreeGrid.getRecords()) {
            resultTreeGrid.removeData(rec);
        }
        valueLabel.setValue("");
        typeRadio.setValue("");
        searchText.setValue("");

    }

    public static class SearchTreeNode extends TreeNode {

        public SearchTreeNode(String name, String type, String score) {
            setAttribute("name", name);
            setAttribute("type", type);
            setAttribute("score", score);

        }
    }
}
