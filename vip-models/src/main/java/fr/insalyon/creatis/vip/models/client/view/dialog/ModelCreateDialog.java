/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.models.client.view.dialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;

import com.smartgwt.client.types.*;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.TextItem;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import java.util.List;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.form.fields.events.IconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.IconClickHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.models.client.view.ModelTreeGrid;
import java.util.ArrayList;
import java.util.LinkedHashMap;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cervenansky
 */
public class ModelCreateDialog extends Window {

    private TextItem searchText = null;
    private Label searchFilterLabel = null;
    private Label selectFileLabel = null;
    private Label selectLayerLabel = null;
    private ModelServiceAsync ms = null;
    private SelectItem physicalCombo = null;
    private CheckboxItem anaCheck = null;
    private CheckboxItem pathoCheck = null;
    private CheckboxItem geoCheck = null;
    private CheckboxItem forCheck = null;
    private CheckboxItem exCheck = null;
    private SearchTreeNode[] resultData;
    private Tree resultTree;
    private TreeGrid resultTreeGrid;
    private HLayout hLayout = null;
    private Record selectRecord = null;
    private DynamicForm extensionForm = null;
    private RadioGroupItem typeRadio = null;
    private RadioGroupItem layerRadio = null;
    private ModelTreeGrid tree = null;
    private int tp = -1;
    private int ins = -1;
    private int type = 1;
    private String filename = "";
    private DynamicForm layerForm = null;
    private DynamicForm searchForm = null;
    private TreeGrid resultTG = null;
    private Logger logger = null;
    DynamicForm validateForm = null;
    private static final String HELPTEXT = "<br><b>Term : </b> Clicking the button search,"
            + "it allows to search the specified term"
            + " and after to select the wanted value in grid results."
            + "<br><br><b>Ontology : </b> the search will be performed through the Ontology"
            + " OntoVip 0.4 . User can specify the kind of layer to explore: Anatomy, Pathology, ..."
            + "<br><br><b>Search : </b>For meshes object, a new search will remove the previous search."
            + "For voxels object, the previous search  is not removed by the fact user can select different terms"
            + " represented by one organ. But only one type of layer is allowed by object.";
    private static final String PRIORITYTEXT = "<br><b>Priority : </b>"
            + " the priority value is for the flattening. This ordering"
            + "is based on a rough layer priority guess assuming that geometry"
            + "and anatomy are always superseded by pathology that"
            + "is in turn overlaid by external bodies and foreign agents."
            + " The values are between 1 and 4. 1 is the default value and"
            + " 4 the high value."
            + " <br>By default, the priority is set to 1. To change it, edit the value.";

    public ModelCreateDialog(ModelTreeGrid treegrid) {
        ms = ModelService.Util.getInstance();
        tree = treegrid;
        logger = Logger.getLogger("ModelCreateDialog");
        logger.log(Level.SEVERE, "init");
        init();
    }

    public void addInfo(int extension, int timepoint, int instant, String name) {
        removeForms();
        manageExt(extension);
        type = extension;
        tp = timepoint;
        ins = instant;
        filename = name;
        this.setTitle("add Object at timepoint: " + timepoint + " instant: " + instant);
        initForms();
    }

    public void updateTree(ModelTreeGrid treegrid) {
        tree = treegrid;
    }

    public void init() {

        this.setTitle("add Object");
        this.setWidth(980);
        this.setHeight(610);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

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

        typeRadio.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {

            public void onChanged(com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
                changeType((String) event.getValue());
            }
        });
        initForms();

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

    private void initForms() {
        if (type == 0 || type == 1) {
            initObjectForms();
        } else {
            initPhysicalForms();
        }
        createOKBt();
    }

    private void createOKBt() {
        ButtonItem submitButton = new ButtonItem("OK");
        submitButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            public void onClick(
                    com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {

                if (type == 0 || type == 1) {
                    if (isObjectsValidated() == true) {
                         addObjectItem();
                        //if (addObjectItem()) {
                            hide();
                            refresh();
                        //}


                    } else {
                        SC.say("you have to select a semantic term.");
                    }

                } else if (type == 2 || type == 3) {
                    if (physicalCombo.getValues().length != 0) {
                        tree.addPhysicalItems(tp, ins, type, filename, getLayerforLUT(), physicalCombo.getValues());
                        hide();
                        refresh();
                    } else {
                        SC.say("you have to select at least one physical parameters.");
                    }

                }

            }
        });
        validateForm = new DynamicForm();
        validateForm.setNumCols(8);
        validateForm.setMethod(FormMethod.POST);

        validateForm.setFields(submitButton);
        this.addItem(validateForm);
    }

    private void addObjectItem() {
        if (type == 0) {
            addMeshesItem();
            
        } else {
            addVoxelsItem();
            
        }
    }

    private void addVoxelsItem() {


        boolean bmix = true;
        for (ListGridRecord rd : resultTG.getSelectedRecords()) {
            if (rd.getAttributeAsString("type") != resultTG.getSelectedRecords()[0].getAttributeAsString("type")) {
                bmix = false;
                break;
            }
        }
        if (!bmix) {
            Layout.getInstance().setWarningMessage("Can't mix different types of layer for one object.");
        } else {
            String[] names = new String[resultTG.getSelectedRecords().length];
            String[] types = new String[resultTG.getSelectedRecords().length];
            int[] labels = new int[resultTG.getSelectedRecords().length];
            int i = 0;
            for (ListGridRecord rd : resultTG.getSelectedRecords()) {
                names[i]= rd.getAttribute("name");
                labels[i] = rd.getAttributeAsInt("label");
                types[i] = rd.getAttribute("type");
                i++;
            }
                
                tree.addObjectItems(tp, ins, type, filename,names, types, labels); 
                        
            }
        
    }

    private void addMeshesItem() {

        int label;
        String[] names = new String[resultTG.getSelectedRecords().length];
        String[] types = new String[resultTG.getSelectedRecords().length];
        int[] labels = new int[resultTG.getSelectedRecords().length];
        ListGridRecord[] records = resultTG.getSelectedRecords();
        int i = 0;
        for (ListGridRecord rd : records) {
            String slabel = rd.getAttribute("priority");
            label = 1;
            if (slabel != null) {
                label = Integer.parseInt(slabel);
            }
            if (label > 4) {
                label = 4;
            }
            names[i] = rd.getAttribute("name");
            types[i] = rd.getAttribute("type");
            labels[i] = label;
            i++;
        }
        tree.addObjectItems(tp, ins, type, filename, names, types, labels);

    }

    private boolean isObjectsValidated() {
        boolean bhide = true;
        ListGridRecord[] records = resultTG.getSelectedRecords();
        if (records.length == 0) {
            Layout.getInstance().setNoticeMessage("No entity selected.");
            bhide = false;
        } else {
            for (ListGridRecord rd : records) {
                if (type == 1) {
                    String slabel = rd.getAttributeAsString("label");
                    if (slabel.isEmpty()) {
                        SC.say("labels missing for entities. Cannot add object.");
                        bhide = false;
                        break;
                    } else {
                        ArrayList<String> labels = tree.getObjectsLabel(tp, ins, rd.getAttribute("type"));
                        for (String lb : labels) {
                            if (lb.equals(slabel)) {

                                SC.say("Label already used for " + rd.getAttribute("name")
                                        + ". Please choose another value.");
                                bhide = false;
                                break;
                            }
                        }
                    }
                }
            }

        }
        return bhide;
    }

    private void manageExt(int extension) {
        switch (extension) {
            // case mhd
            case 0:
                typeRadio.setDefaultValue("mesh");
                break;
            // case vtk
            case 1:
                typeRadio.setDefaultValue("voxel");
                break;
            case 2:
                typeRadio.setDefaultValue("LUT");
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

    public String getLayerforLUT() {
        String title = layerRadio.getValueAsString();
        if (title.isEmpty()) {
            return "";
        } else if (title.equals("All")) {
            return "All";
        } else {


            return tree.getTypeFromMap(title).toString();
        }
    }

    public String getLayer() {
        return selectRecord.getAttribute("type");
    }

    private void refresh() {
        for (Record rec : resultTreeGrid.getRecords()) {
            resultTreeGrid.removeData(rec);
        }
        typeRadio.setValue("");
        searchText.setValue("");

    }

    public TreeGrid createVoxelTreeGrid() {
        resultTG = new TreeGrid();
        TreeGridField fieldname = new TreeGridField("name");
        fieldname.setCanSort(false);
        fieldname.setCanEdit(false);
        TreeGridField fieldtype = new TreeGridField("type");
        fieldtype.setCanSort(false);
        fieldname.setCanEdit(false);
        TreeGridField fieldscore = new TreeGridField("score");
        fieldscore.setCanSort(false);
        fieldname.setCanEdit(false);

        resultTG = new TreeGrid();
        resultTG.setAlign(Alignment.CENTER);
        resultTG.deselectAllRecords();
        resultTG.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        if (type == 0) //mesh
        {
            resultTG.setSelectionType(SelectionStyle.SINGLE);
            resultTG.setFields(fieldname, fieldtype, fieldscore);



            IsIntegerValidator iiv = new IsIntegerValidator();
            IntegerRangeValidator integerRangeValidator = new IntegerRangeValidator();
            integerRangeValidator.setMin(1);
            integerRangeValidator.setMax(4);
            TreeGridField priorityField = new TreeGridField("priority");
            priorityField.setCanSort(false);
            priorityField.setCanEdit(true);
            priorityField.setPrompt(PRIORITYTEXT);
            priorityField.setBaseStyle("myHighGridCell");
            //priorityField.setValidators(iiv,integerRangeValidator);
            resultTG.setSelectionType(SelectionStyle.SINGLE);
            resultTG.setFields(fieldname, fieldtype, fieldscore, priorityField);
        } else //voxel
        {
            IntegerRangeValidator integerRangeValidator = new IntegerRangeValidator();
            integerRangeValidator.setMin(0);
            //DoesntContainValidator val = new DoesntContainValidator();
            LabelValidator val = new LabelValidator();
            LabelEmptyValidator vale = new LabelEmptyValidator();
            TreeGridField labelField = new TreeGridField("label");
            labelField.setCanSort(false);
            labelField.setCanEdit(true);

            labelField.setValidators(integerRangeValidator, val, vale);
            labelField.setBaseStyle("myHighGridCell");
            //   labelField.setRequired(true);

            labelField.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {

                public void onChanged(com.smartgwt.client.widgets.grid.events.ChangedEvent event) {

                    ArrayList<String> labels = tree.getObjectsLabel(tp, ins, String.valueOf(resultTG.getRecord(event.getRowNum()).getAttribute("type")));
                    //tree.getTypeFromMap(resultTG.getRecord(event.getRowNum()).getAttribute("type")));

                    for (String label : labels) {
                        if (label.equals(event.getValue().toString())) {
                            Layout.getInstance().setWarningMessage("Label already used. Please choose another value.");
                        }
                    }

                }
            });
            resultTG.setSelectionType(SelectionStyle.SIMPLE);
            resultTG.setFields(fieldname, fieldtype, fieldscore, labelField);
        }
        resultTG.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (type == 0 && selectRecord != null) {
                    selectRecord.setAttribute("priority", "");
                }
                selectRecord = event.getRecord();
                if (type == 0) //mesh
                {
                    if (selectRecord.getAttribute("priority") == null || selectRecord.getAttribute("priority").isEmpty()) {
                        selectRecord.setAttribute("priority", 1);
                        resultTG.redraw();
                    }
                }

            }
        });
        return resultTG;
    }

    public void initPhysicalForms() {
        selectFileLabel = new Label("<b>Select file type</b>");
        selectFileLabel.setAlign(Alignment.LEFT);
        selectFileLabel.setWidth100();
        selectFileLabel.setHeight(20);
        selectFileLabel.setBackgroundColor("#F2F2F2");
        this.addItem(selectFileLabel);

        LinkedHashMap<String, String> lutMap = new LinkedHashMap<String, String>();
        for (String lut : tree.getLutMap()) {
            lutMap.put(lut, lut);
        }
        physicalCombo = new SelectItem();
        physicalCombo.setTitle("Physical parameters:");
        physicalCombo.setMultiple(true);
        physicalCombo.setMultipleAppearance(MultipleAppearance.PICKLIST);
        physicalCombo.setValueMap(lutMap);
        physicalCombo.setPrompt("select the paramater(s) represented by the file.");
        physicalCombo.enable();

        extensionForm = new DynamicForm();
        extensionForm.setFields(typeRadio, physicalCombo);
        this.addItem(extensionForm);

        selectLayerLabel = new Label("<b>Select Layer</b>");
        selectLayerLabel.setAlign(Alignment.LEFT);
        selectLayerLabel.setWidth100();
        selectLayerLabel.setHeight(20);
        selectLayerLabel.setBackgroundColor("#F2F2F2");
        this.addItem(selectLayerLabel);

        LinkedHashMap<String, String> layerMap = new LinkedHashMap<String, String>();
        layerMap.put("Anatomy", "Anatomy");
        layerMap.put("Pathology", "Pathology");
        layerMap.put("Geometry", "Geometry");
        layerMap.put("Foreign body", "Foreign body");
        layerMap.put("External agent", "External agent");
        if (type == 3) {
            layerMap.put("All", "All");
        }

        layerRadio = new RadioGroupItem();
        layerRadio.setDefaultValue("exampleStyleOnline");
        layerRadio.setVertical(false);
        layerRadio.setShowTitle(false);
        layerRadio.setValueMap(layerMap);
        layerRadio.setValue("Anatomy");
        layerForm = new DynamicForm();
        layerForm.setFields(layerRadio);
//        SearchLayout = new VLayout();
//        SearchLayout.addMember(layerForm);
//        this.addItem(SearchLayout);
        this.addItem(layerForm);

    }

    private void removePhysicalForms() {
        this.removeItem(selectFileLabel);
        this.removeItem(extensionForm);
        this.removeItem(layerForm);
        this.removeItem(validateForm);
        this.removeItem(selectLayerLabel);

    }

    private void removeObjectForms() {
        this.removeItem(selectFileLabel);
        this.removeItem(extensionForm);
        this.removeItem(searchForm);
        this.removeItem(searchFilterLabel);
        this.removeItem(layerForm);
        this.removeItem(hLayout);
        this.removeItem(validateForm);
    }

    private void removeForms() {
        if (type == 2 || type == 3) {
            removePhysicalForms();
        } else {
            removeObjectForms();
        }
    }

    public void changeType(String typeName) {
        if (typeName.equals("voxel")) {
            removeForms();
            type = 1;
            initObjectForms();

        } else if (typeName.equals("mesh")) {

            removeForms();
            type = 0;
            initObjectForms();

        } else if (typeName.equals("LUT")) {
            removeForms();
            type = 2;
            initPhysicalForms();

        } else if (typeName.equals("Map")) {
            removeForms();
            type = 3;
            initPhysicalForms();

        }
        createOKBt();
        redraw();
        typeRadio.setValue(typeName);

    }

    public void initObjectForms() {

        selectFileLabel = new Label("<b>Select file type</b>");
        selectFileLabel.setAlign(Alignment.LEFT);
        selectFileLabel.setWidth100();
        selectFileLabel.setHeight(20);
        selectFileLabel.setBackgroundColor("#F2F2F2");
        this.addItem(selectFileLabel);

        extensionForm = new DynamicForm();
        extensionForm.setFields(typeRadio);
        this.addItem(extensionForm);

        searchForm = new DynamicForm();
        searchForm.setNumCols(4);

        // search part
        searchText = new TextItem();
        searchText.setWidth(400);
        searchText.setTitle("<nobr><b>Associate object name(s) to the file.</nobr></b>");
        searchText.setTitleOrientation(TitleOrientation.TOP);
        searchText.setValue(filename.substring(0, filename.lastIndexOf(".")).replaceAll("_", " "));
        ButtonItem searchBt = new ButtonItem();
        searchBt.setTitle("Search");
        searchBt.setTooltip("Search in OntoVIP");
        searchBt.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                search();

            }
        });

        FormItemIcon icon = new FormItemIcon();
        icon.setSrc(CoreConstants.ICON_HELP);
        searchText.setIcons(icon);
        searchText.addIconClickHandler(new IconClickHandler() {

            public void onIconClick(IconClickEvent event) {
                SC.say(HELPTEXT);
            }
        });

        searchForm.setFields(searchText, searchBt);
        this.addItem(searchForm);

        // search part
        searchFilterLabel = new Label("<b>Search filters</b>");
        searchFilterLabel.setAlign(Alignment.LEFT);
        searchFilterLabel.setWidth100();
        searchFilterLabel.setHeight(20);
        searchFilterLabel.setBackgroundColor("#F2F2F2");
        this.addItem(searchFilterLabel);


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

        layerForm = new DynamicForm();
        layerForm.setNumCols(12);
        layerForm.setFields(anaCheck, pathoCheck, geoCheck, forCheck, exCheck);
        this.addItem(layerForm);

        resultTree = new Tree();
        resultTree.setModelType(TreeModelType.PARENT);
        resultTree.setRootValue("1");
        resultTree.setNameProperty("name");
        resultTree.setIdField("ElementId");
        resultTree.setParentIdField("ReportsTo");
        resultTree.setOpenProperty("isOpen");
        resultTree.setData(resultData);
        resultTreeGrid = createVoxelTreeGrid();
        hLayout = new HLayout();
        hLayout.setWidth100();
        hLayout.setHeight(400);

        hLayout.addMember(resultTreeGrid);
        this.addItem(hLayout);

      

    }

    public static class SearchTreeNode extends TreeNode {

        public SearchTreeNode(String name, String type, String score) {
            setAttribute("name", name);
            setAttribute("type", type);
            setAttribute("score", score);

        }
    }

    class LabelValidator extends CustomValidator {

        @Override
        protected boolean condition(Object value) {
            boolean bvalid = true;
            ArrayList<String> labels = tree.getObjectsLabel(tp, ins, String.valueOf(resultTG.getSelectedRecord().getAttribute("type")));
            for (String label : labels) {
                if (label.equals(String.valueOf(value)) || String.valueOf(value).isEmpty()) {
                    bvalid = false;
                }
            }
            return bvalid;
        }
    }

    class LabelEmptyValidator extends CustomValidator {

        @Override
        protected boolean condition(Object value) {
            boolean bvalid = true;
            value.toString().isEmpty();
            if (value.toString().isEmpty()) {
                bvalid = false;
            }
            return bvalid;
        }
    }

    public class LabelRecord extends ListGridRecord {

        private ModelTreeGrid mtree = null;

        public LabelRecord() {
        }

        public LabelRecord(String name, String type, String score, String label, ModelTreeGrid tree) {
            setName(name);
            setType(type);
            setScore(score);
            setLabel(label);
            mtree = tree;
        }

        public LabelRecord(String name, String type, String score, ModelTreeGrid tree) {
            setName(name);
            setType(type);
            setScore(score);
        }

        private void setName(String name) {
            setAttribute("name", name);
        }

        private void setType(String type) {
            setAttribute("type", type);
        }

        private void setScore(String score) {
            setAttribute("score", score);
        }

        private void setLabel(String label) {
            setAttribute("label", label);
        }

        private void getLabel(String label) {
            setAttribute("label", label);
        }
    }

    public void search() {
        boolean[] scope = new boolean[]{anaCheck.getValueAsBoolean(), pathoCheck.getValueAsBoolean(),
            geoCheck.getValueAsBoolean(), forCheck.getValueAsBoolean(), exCheck.getValueAsBoolean()};
        String toSearch = searchText.getValueAsString().replaceAll("~", "");

        if (toSearch == null || toSearch.isEmpty()) {
            Layout.getInstance().setWarningMessage("Can't search in the ontology.Please add a term.");
        } else {
            AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {

                public void onFailure(Throwable caught) {
                    Layout.getInstance().setWarningMessage("Cant search through the ontology");
                }

                public void onSuccess(List<String[]> result) {
                    int i = 0;
                    if (type == 0) {
                        for (Record rec : resultTreeGrid.getRecords()) {
                            resultTreeGrid.removeData(rec);
                        }
                    }
                    resultData = new SearchTreeNode[result.size()];

                    for (String[] ii : result) {
                        resultTreeGrid.addData(new SearchTreeNode(ii[0], ii[1], ii[2]));
                    }
                }
            };
            toSearch += "~";
            ms.searchWithScope(toSearch, scope, callback);
        }
    }
}
