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
    private EmployeeTreeNode rollOverRecord;

    public EmployeeTreeNode getRollOverRecord() {
        return rollOverRecord;
    }
    String type;
    String name;
    
     public static final TreeNode[] employeeData = new  TreeNode[] {  
            new EmployeeTreeNode("3", "1", "base", "nothing", true,""),  
            new EmployeeTreeNode("189", "3", "http://e-ginseng.org/graph/I3S", "nothing", false,""),  
            new EmployeeTreeNode("265", "189", "Person", "nothing", false,""),  
            new EmployeeTreeNode("264", "189", "Medical Bag", "Medical Bag", false,""), 
            new EmployeeTreeNode("263", "189", "Medical Event", "Medical Event", false,""), 
            new EmployeeTreeNode("262", "189", "Clinical Variable", "Clinical Variable", false,""), 
            
            new EmployeeTreeNode("4", "265", "Patient", "Patient", false,""), 
            new EmployeeTreeNode("5", "265", "Physician", "Physician", false,""), 
            
             
            new EmployeeTreeNode("6", "263", "Baby", "baby", false,""),
            new EmployeeTreeNode("7", "263", "FCV", "FCV", false,""), 
            new EmployeeTreeNode("8", "263", "RSPA", "RSPA", false,""), 
            new EmployeeTreeNode("9", "263", "DICOM_Image_Study", "DICOM_Image_Study", false,""),
            
            
            new EmployeeTreeNode("10", "262", "PregnancyDuration", "PregnancyDuration", false,""), 
            new EmployeeTreeNode("11", "262", "Weight", "Weight", false,""), 
            new EmployeeTreeNode("12", "262", "DernierFCV", "DernierFCV", false,""), 
            new EmployeeTreeNode("13", "262", "DateDebutGrossesse", "DernierFCV", false,""), 
            
            new EmployeeTreeNode("14", "4", "ID", "int", false,""), 
            new EmployeeTreeNode("15", "4", "hasStudy", "hasStudy", false,""), 
            new EmployeeTreeNode("45", "4", "hasMedicalBag", "hasMedicalBag", false,""), 
            new EmployeeTreeNode("16", "4", "First Name", "First Name", false,""), 
            new EmployeeTreeNode("17", "4", "Last Name", "Last Name", false,""), 
            new EmployeeTreeNode("18", "4", "Adresse", "Adresse", false,""), 
            
            new EmployeeTreeNode("19", "5", "First Name", "string", false,""), 
            new EmployeeTreeNode("20", "5", "Last Name", "string", false,""), 
            new EmployeeTreeNode("21", "5", "Adresse", "Adresse", false,""),
            
            new EmployeeTreeNode("22", "18", "Number", "int", false,""),
            new EmployeeTreeNode("23", "18", "Street", "Street", false,""),
            new EmployeeTreeNode("24", "18", "Zip", "Zip", false,""),
            new EmployeeTreeNode("25", "18", "City", "City", false,""),
            
            
            new EmployeeTreeNode("44", "21", "Number", "int", false,""),
            new EmployeeTreeNode("26", "21", "Street", "Street", false,""),
            new EmployeeTreeNode("27", "21", "Zip", "Zip", false,""),
            new EmployeeTreeNode("28", "21", "City", "City", false,""),
            
            new EmployeeTreeNode("29", "6", "externalRefID", "int", false,""),
            new EmployeeTreeNode("30", "6", "date", "date", false,""),
            new EmployeeTreeNode("31", "6", "hasPysician", "hasPysician", false,""),
             
                
            new EmployeeTreeNode("32", "7", "externalRefID", "externalRefID", false,""),
            new EmployeeTreeNode("33", "7", "date", "date", false,""),
            new EmployeeTreeNode("34", "7", "hasPysician", "hasPysician", false,""),
             
            new EmployeeTreeNode("35", "8", "externalRefID", "externalRefID", false,""),
            new EmployeeTreeNode("36", "8", "date", "date", false,""),
            new EmployeeTreeNode("37", "8", "hasPysician", "hasPysician", false,""),
             
            new EmployeeTreeNode("38", "9", "externalRefID", "externalRefID", false,""),
            new EmployeeTreeNode("39", "9", "date", "date", false,""),
            new EmployeeTreeNode("40", "9", "hasPysician", "hasPysician", false,""),
            
            new EmployeeTreeNode("41", "264", "description", "description", false,""),
            new EmployeeTreeNode("42", "264", "date", "date", false,""),
            new EmployeeTreeNode("43", "264", "hasMedicalEvent", "hasMedicalEvent", false,""),
            
            new EmployeeTreeNode("46", "10", "acquisationDate", "acquisationDate", false,""),
            new EmployeeTreeNode("47", "10", "label", "label", false,""),
            new EmployeeTreeNode("48", "10", "value", "value", false,""),
            new EmployeeTreeNode("49", "10", "unit", "unit", false,""),
            
            
            new EmployeeTreeNode("50", "11", "acquisationDate", "acquisationDate", false,""),
            new EmployeeTreeNode("51", "11", "label", "label", false,""),
            new EmployeeTreeNode("52", "11", "value", "value", false,""),
            new EmployeeTreeNode("53", "11", "unit", "unit", false,""),
            
            new EmployeeTreeNode("54", "12", "acquisationDate", "acquisationDate", false,""),
            new EmployeeTreeNode("55", "12", "label", "label", false,""),
            new EmployeeTreeNode("56", "12", "value", "value", false,""),
            new EmployeeTreeNode("57", "12", "unit", "unit", false,""),
            
            new EmployeeTreeNode("58", "13", "acquisationDate", "acquisationDate", false,""),
            new EmployeeTreeNode("59", "13", "label", "label", false,""),
            new EmployeeTreeNode("60", "13", "value", "value", false,""),
            new EmployeeTreeNode("61", "13", "unit", "unit", false,""),
            new EmployeeTreeNode("188", "3", "http://ns.inria.fr/edelweiss/2010/kgram/entailment", "Mgr Syst P P", false,""),
            
           
            

    };  

    public CheckboxTree() {
   
        super(650,580);
        Tree employeeTree = new Tree();  
        employeeTree.setModelType(TreeModelType.PARENT);  
        employeeTree.setRootValue(1);  
        employeeTree.setNameProperty("Name");  
        employeeTree.setIdField("EmployeeId");  
        employeeTree.setParentIdField("ReportsTo");  
        employeeTree.setOpenProperty("isOpen");  
        employeeTree.setData(employeeData);
        
        
        final TreeGrid employeeTreeGrid = new TreeGrid();
            
           
        
        
        /*
            @Override  
            protected Canvas getExpansionComponent(final ListGridRecord record) {  
  
                final ListGrid grid = this;  
                VLayout layout = new VLayout(5);  
               layout.setWidth100();
               layout.setHeight(100);
  
                HLayout hLayout = new HLayout(10);  
                hLayout.setAlign(Alignment.CENTER);  
                
  
               
                layout.addMember( addGrid(new ListGridRecord[]{employeeData[3],employeeData[4]}));  
                return layout;  
            }  
            
        };  
              
           
            
            */
            
            
            
        
      Canvas rollUnderCanvasProperties = new Canvas();  
        rollUnderCanvasProperties.setAnimateFadeTime(1000);  
        rollUnderCanvasProperties.setAnimateShowEffect(AnimationEffect.FADE);  
        rollUnderCanvasProperties.setBackgroundColor("#00ffff");  
        rollUnderCanvasProperties.setOpacity(50);  
        //can also override ListGrid.getRollUnderCanvas(int rowNum, int colNum)  
        employeeTreeGrid.setRollUnderCanvasProperties(rollUnderCanvasProperties); 
         employeeTreeGrid.setShowSelectionCanvas(true);  
         employeeTreeGrid.setAnimateSelectionUnder(true); 
          
            employeeTreeGrid.addCellClickHandler(new CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
               rollOverRecord = (EmployeeTreeNode) event.getRecord();
               
               type=rollOverRecord.getAttribute("Type");
               name=rollOverRecord.getAttribute("Name");
               getQueryExplorerTb().setForm();
               
                
              
            }
        });
            
         
            
            /*
            @Override
            protected Canvas createRecordComponent( ListGridRecord record, Integer colNum) {
              String fieldName = this.getFieldName(colNum);
                IButton button = new IButton();

                if (fieldName.equals("nameField")) {
                  
                    button.setHeight(18);  
                    button.setWidth(65);                      
                    button.setTitle("Info"); 
                   
   
                }
                return button;
            }
            };
            */
            
            /*
             @Override  
            protected Canvas getExpansionComponent(final ListGridRecord record) {  
  
                final TreeGrid  grid = this;  
  
                VLayout layout = new VLayout(5); 
                
                layout.setPadding(5); 
                layout.addMember(saveButton);
                return layout;
             }
            
          **/
           
  

       // employeeTreeGrid.setDrawAheadRatio(4);  
         //employeeTreeGrid.setCanExpandRecords(true);
        employeeTreeGrid.setLoadDataOnDemand(false);  
        employeeTreeGrid.setNodeIcon(QueryConstants.ICON_BASE);
       
        employeeTreeGrid.setFolderIcon(QueryConstants.ICON_PROPERTIES);  
        employeeTreeGrid.setShowOpenIcons(false);  
        employeeTreeGrid.setShowDropIcons(false);  
        employeeTreeGrid.setClosedIconSuffix("");  
       
        employeeTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX); 
        employeeTreeGrid.setAutoFetchData(true);  
        employeeTreeGrid.setData(employeeTree);  
        
        //pour monter les lignes bleu
        employeeTreeGrid.setShowSelectedStyle(true);  
        employeeTreeGrid.setShowPartialSelection(true);  
        employeeTreeGrid.setCascadeSelection(true);
   
          
       
        
       TreeGridField nameField = new TreeGridField("Name","Name");
       TreeGridField typeField = new TreeGridField("Type","Type");
       TreeGridField restriction = new TreeGridField("Restriction","Restriction");
      
      

      employeeTreeGrid.setFields(nameField,typeField,restriction);
      typeField.setHidden(true);
  /*
        employeeTreeGrid.addDrawHandler(new DrawHandler() {  
            public void onDraw(DrawEvent event) {  
                employeeTreeGrid.getTree().openAll();  
            }  
        });  
        */
  
        
  
        final CheckboxItem partialSelection = new CheckboxItem("partialSelect", "Allow Partial Selection");  
        partialSelection.setDefaultValue(true);  
        partialSelection.addChangeHandler(new ChangeHandler() {  
            public void onChange(ChangeEvent event) {  
                boolean selected = partialSelection.getValueAsBoolean();  
                employeeTreeGrid.setShowPartialSelection(!selected);  
                employeeTreeGrid.redraw();  
            }  
        });  
 
    
        querynameField = FieldUtil.getTextItem(400, null);


        description = new RichTextEditor();
        description.setHeight(100);
       
        description.setOverflow(Overflow.HIDDEN);
        description.setShowEdges(true);
        description.setControlGroups("styleControls", "editControls",
                "colorControls");
        addField("Name", querynameField);
        this.addMember(WidgetUtil.getLabel("<b>Description</b>", 15));
        this.addMember(description);
       
        this.addMember(employeeTreeGrid); 
        HLayout layout=new HLayout();
     
        layout.setHeight(30);
       
        
        saveButton= WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVE,
                new ClickHandler() {
            ///nouha// String body_val=null;
            @Override
            public void onClick(ClickEvent event) {
                rollOverRecord.setAttribute("Restriction", "sfkjsqfqsk");
                
            }
        });
        
        
        
        executeButton= WidgetUtil.getIButton("Execute", QueryConstants.ICON_LAUNCH,
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
     
    
    public String getType(){
        return type;
        
    };
     
    
    
    public String getName(){
        return name;
        
    };
     
      public static class EmployeeTreeNode extends TreeNode {  
        public EmployeeTreeNode(String employeeId, String reportsTo, String name, String type, boolean isOpen,String restriction) {  
            setAttribute("EmployeeId", employeeId);  
            setAttribute("ReportsTo", reportsTo);  
            setAttribute("Name", name);  
            setAttribute("Type", type);  
            setAttribute("isOpen", isOpen);
            setAttribute("Restriction", restriction);
            
             
        }  
    }
      
      
       public QueryExplorerTab getQueryExplorerTb() {

        QueryExplorerTab queryExplorerTab = (QueryExplorerTab) Layout.getInstance().
                getTab(QueryConstants.TAB_QUERYEXPLORER);
        return queryExplorerTab;
    }
      /*
      
      public Canvas addGrid(ListGridRecord[] records){
       ListGrid sousgrid = new ListGrid(){
           
           @Override  
            protected Canvas getExpansionComponent(final ListGridRecord record) {  
  
                final ListGrid grid = this;  
                VLayout layout = new VLayout(5);  
                HLayout hLayout = new HLayout(10);  
                hLayout.setAlign(Alignment.CENTER);  
                layout.addMember(hLayout);  
                return layout;  
            }  
            
        
       };
       sousgrid.setDrawAheadRatio(4);  
       sousgrid.setCanExpandRecords(true);
       sousgrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE); 
       sousgrid.setAutoFetchData(true);  
       sousgrid.setData(records );  
       sousgrid.setShowSelectedStyle(true);    
       TreeGridField nameField = new TreeGridField("Name");
       sousgrid.setFields(nameField);
  
       
       
       return sousgrid;
      
      }
     
    ***/
      
      
    
}
