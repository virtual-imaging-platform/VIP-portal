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
public class ModelCreateObjectDialog extends Window {


    private TextItem searchText = null;
    private Label searchFilterLabel = null;
    private Label selectFileLabel = null;
    private ModelServiceAsync ms = null;
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
 
    public ModelCreateObjectDialog(ModelTreeGrid treegrid,  int timepoint, int instant) {
        ms = ModelService.Util.getInstance();
        tree = treegrid;
        tp = timepoint;
        ins = instant;
        init();
    }

   

    public void updateTree(ModelTreeGrid treegrid) {
        tree = treegrid;
    }

    public void init() {
        this.setTitle("add an Object at timepoint: " + tp + " instant: " + ins);
        this.setWidth(980);
        this.setHeight(610);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();
        initObjectForms();
        createOKBt();
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

    private void createOKBt() {
        ButtonItem submitButton = new ButtonItem("OK");
        submitButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            public void onClick(
                    com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                    addObjectItem();
                    hide();
                    refresh();
               
//                    if (isObjectsValidated() == true) {
//                        addObjectItem();
//                            hide();
//                            refresh();
//                        
//
//                    } else {
//                        SC.say("you have to select a semantic term.");
//                    }
            }

        });
        validateForm = new DynamicForm();
        validateForm.setNumCols(8);
        validateForm.setMethod(FormMethod.POST);

        validateForm.setFields(submitButton);
        this.addItem(validateForm);
    }

  

    private void addObjectItem() {

        int[] ilabels = new int[resultTG.getSelectedRecords().length];
        String[] types = new String[resultTG.getSelectedRecords().length];
        String[] names = new String[resultTG.getSelectedRecords().length];
         ArrayList<String> labels = tree.getObjectsLabel(tp, ins, resultTG.getSelectedRecords()[0].getAttribute("type"));
         int value = 0;
         if(labels == null || labels.isEmpty())
         {
             value = 1000;
         }
         else
         {
             for(String lab : labels)
             {
                 if(value < Integer.parseInt(lab))
                     value = Integer.parseInt(lab);
             }
         }
         int index = 0;
         for (ListGridRecord rd : resultTG.getSelectedRecords())
         {
            ilabels[index]= ++value;
            names[index]= rd.getAttribute("name");
            types[index]= rd.getAttribute("type");
            index++;
         }
         tree.addVirtualItems(tp, ins,  names, types, ilabels);
        
//            for (ListGridRecord rd : resultTG.getSelectedRecords()) {
//                int label = rd.getAttributeAsInt("label");
//                tree.addVirtualItem(tp, ins,  rd.getAttribute("name"),                        rd.getAttribute("type"), label);
               
          //  }
        
      
    }

   
   
    public String getOntoName() {
        return selectRecord.getAttribute("name");
    }

  
    

    public String getLayer() {
        return selectRecord.getAttribute("type");
    }

    private void refresh() {
        for (Record rec : resultTreeGrid.getRecords()) {
            resultTreeGrid.removeData(rec);
        }
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
       
            IntegerRangeValidator integerRangeValidator = new IntegerRangeValidator();
            integerRangeValidator.setMin(-1);
            integerRangeValidator.setMax(1000);
            //DoesntContainValidator val = new DoesntContainValidator();
            LabelValidator val = new LabelValidator();
            LabelEmptyValidator vale = new LabelEmptyValidator();
            TreeGridField labelField = new TreeGridField("label");
            labelField.setCanSort(false);
            labelField.setCanEdit(true);

            //labelField.setValidators(integerRangeValidator, val, vale);
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
            resultTG.setFields(fieldname, fieldtype, fieldscore);
        
        resultTG.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (type == 0 && selectRecord != null) {
                    selectRecord.setAttribute("priority", "");
                }
                selectRecord = event.getRecord();
               }
        });
        return resultTG;
    }


    public void initObjectForms() {

        selectFileLabel = new Label("<b>Select semantic term</b>");
        selectFileLabel.setAlign(Alignment.LEFT);
        selectFileLabel.setWidth100();
        selectFileLabel.setHeight(20);
        selectFileLabel.setBackgroundColor("#F2F2F2");
        this.addItem(selectFileLabel);


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
