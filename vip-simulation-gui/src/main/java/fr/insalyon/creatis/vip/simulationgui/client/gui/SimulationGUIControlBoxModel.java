
package fr.insalyon.creatis.vip.simulationgui.client.gui;

import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Portlet;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.NodeClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeClickHandler;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;

import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Object3D;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.ObjectModel;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Scene;

/**
 *
 * @author moulin
 */

public class SimulationGUIControlBoxModel extends Portlet{

     /////////////////////Data Member//////////////////////
     private DynamicForm form = new DynamicForm();
     private DynamicForm form2 = new DynamicForm(); 
     private DynamicForm form3 = new DynamicForm(); 
     private SpinnerItem spinnerx = new SpinnerItem();
     private SpinnerItem spinnery = new SpinnerItem();
     private SpinnerItem spinnerz = new SpinnerItem(); 
     private SpinnerItem spinnerax = new SpinnerItem();
     private SpinnerItem spinneray = new SpinnerItem();
     private SpinnerItem spinneraz = new SpinnerItem(); 
     private Slider hSlider = new Slider("Step"); 
     private String id;
     private ObjectModel objectModel;
     private CheckboxItem modBox;  
     private CheckboxItem modAxis;  
     private CheckboxItem model;  
     private boolean enable=false;
     private TreeNode[] elementData;
     private Tree elementTree;
     private TreeGrid elementTreeGrid; 
     
     private HLayout hLayout1 =new HLayout();
     private HLayout hLayout2 =new HLayout();
     static private SimulationGUIControlBoxModel instance;
     
     
    public static SimulationGUIControlBoxModel getInstance() {
        if (instance == null) {
        instance = new SimulationGUIControlBoxModel();
          }
               return instance;
     }
 
     private SimulationGUIControlBoxModel()
     {
          this.setTitle("Model");
          id="Model";
          objectModel= ObjectModel.getInstance();
          
          
         
          
          spinnerx.setName("x");  
          spinnerx.setDefaultValue(0);  
          
          spinnery.setName("y");  
          spinnery.setDefaultValue(0);  
          
          spinnerz.setName("z");  
          spinnerz.setDefaultValue(0);  
     
        
          spinnerax.setName("angle x");  
          spinnerax.setDefaultValue(0);  
          spinnerax.setMax(360);
          spinnerax.setMin(-360);
        
          
          spinneray.setName("angle y");  
          spinneray.setDefaultValue(0);  
          spinneray.setMax(360);
          spinneray.setMin(-360);
        
          
          spinneraz.setName("angle z");  
          spinneraz.setDefaultValue(0);  
          spinneraz.setMax(360);
          spinneraz.setMin(-360);
           
          
          form.setFields(spinnerx,spinnery,spinnerz);
          form2.setFields(spinnerax,spinneray,spinneraz);
          
          
          modAxis=new CheckboxItem("Axis");
          model= new CheckboxItem("Model");
          modBox=new CheckboxItem("Bounding box");
            
          modBox.setValue(true);
          model.setValue(true);
          modAxis.setValue(true); 
          
            
          form3.setAutoFocus(false);  
          form3.setNumCols(6);  
          form3.setFields(modBox,modAxis,model);
          
          hSlider.setVertical(false);
          hSlider.setMinValue(1f);  
          hSlider.setMaxValue(10f);  
          hSlider.setNumValues(1000);    
          hSlider.setTop(200);  
          hSlider.setLeft(100);
          
          hLayout1.setWidth100();
          hLayout1.setHeight(100);
          hLayout1.addMember(hSlider);
           
          
          elementTree = new Tree();  
          elementTree.setModelType(TreeModelType.PARENT);  
          elementTree.setRootValue("1");
          elementTree.setNameProperty("Element");  
          elementTree.setIdField("ElementId");  
          elementTree.setParentIdField("ReportsTo");  
          elementTree.setOpenProperty("isOpen");  
          elementTree.setData(elementData);  
         
  
          elementTreeGrid = new TreeGrid();    
          // elementTreeGrid.setHeight(150);  
         // elementTreeGrid.setWidth(260);  
          elementTreeGrid.setData(elementTree);  
          elementTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);  
          elementTreeGrid.setShowSelectedStyle(false);  
          elementTreeGrid.setShowPartialSelection(true); 
          elementTreeGrid.setSelectionType(SelectionStyle.SIMPLE);
          elementTreeGrid.setCascadeSelection(true);  
          elementTreeGrid.draw();
          
          hLayout2.setWidth100();
          hLayout2.setHeight(400);
          hLayout2.addMember(elementTreeGrid);
          
          //treeAndNode();
          
          setControl();
         
            
          this.addItem(form2);
          this.addItem(form);
          this.addItem(form3);    
          this.addItem(hLayout1);
          this.addItem(hLayout2);
     }
     private void setControl()
     {
                    hSlider.addValueChangedHandler(new ValueChangedHandler() {  
                        public void onValueChanged(ValueChangedEvent event) {  
                            int value = event.getValue();  
                            spinnerx.setStep(value);  
                            spinnery.setStep(value);
                            spinnerz.setStep(value);
                        }  
                    });  
      
          spinnerx.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
               objectModel.setTranslateX(Float.valueOf(spinnerx.getValueAsString()));
               refreshLaunchTabValue();
               Scene.getInstance().refreshScreen();
            }
        });
         spinnery.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
               objectModel.setTranslateY(Float.valueOf(spinnery.getValueAsString()));
               refreshLaunchTabValue();
               Scene.getInstance().refreshScreen();
            }
        });
          spinnerz.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
                objectModel.setTranslateZ(Float.valueOf(spinnerz.getValueAsString()));
                refreshLaunchTabValue();
                Scene.getInstance().refreshScreen();
            }
        });
       spinnerax.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
                objectModel.setAngleX(Integer.valueOf(spinnerax.getValueAsString()));
                refreshLaunchTabValue();
                Scene.getInstance().refreshScreen();
            }
        });
         spinneray.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
                objectModel.setAngleY(Integer.valueOf(spinneray.getValueAsString()));
                refreshLaunchTabValue();
                Scene.getInstance().refreshScreen();
            }
        });
        spinneraz.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event){
                objectModel.setAngleZ(Integer.valueOf(spinneraz.getValueAsString()));
                refreshLaunchTabValue();
                Scene.getInstance().refreshScreen();
            }
            
        });
      
             modBox.addChangeHandler(new ChangeHandler() 
             {
                public void onChange(ChangeEvent event) {
                if(modBox.getValueAsBoolean())objectModel.disable("box");
                else objectModel.enable("box");
                 Scene.getInstance().refreshBuffer();
                 Scene.getInstance().refreshScreen();              
             }
             });
              modAxis.addChangeHandler(new ChangeHandler() 
             {
                public void onChange(ChangeEvent event) {
                if(modAxis.getValueAsBoolean())objectModel.disable("axis");
                else objectModel.enable("axis");
                Scene.getInstance().refreshBuffer();
                Scene.getInstance().refreshScreen();  
             }
             });
             model.addChangeHandler(new ChangeHandler() 
             {
                public void onChange(ChangeEvent event) {
                if(model.getValueAsBoolean())objectModel.disable("model");
                else objectModel.enable("model");
                Scene.getInstance().refreshBuffer();
                Scene.getInstance().refreshScreen();   
             }
             });
         
          
                   
     }
     public float[] getTabValue()
     {
         float Tab[] =new float[6];
         Tab[0]=spinnerx.getAttributeAsFloat(id);
         Tab[1]=spinnery.getAttributeAsFloat(id);
         Tab[2]=spinnerz.getAttributeAsFloat(id);
         Tab[3]=spinnerax.getAttributeAsFloat(id);
         Tab[4]=spinneray.getAttributeAsFloat(id);
         Tab[5]=spinneraz.getAttributeAsFloat(id);
         return Tab;           
     }
     public Object3D getObjectSimulateur()
     {
       if(enable)return objectModel;
       else return null;
     }
     public void enableView()
     {
      enable=true;

     }
     public void disableView()
     {
      enable=false;
     }

    public void setTreeNode(Data3D[][] DATA)
    { 
        
       int i=0;
        for(Data3D[]d: DATA)
        {
            i+=d.length;
        } 
        String s=" i initial : "+i;
        elementData =  new TreeNode[i];
        i=0;
        for(Data3D[] d:DATA)
        {
          for(Data3D d1 :d)
           {
                if(d1.getID().endsWith(".mhd"))
                {
                    elementData[i]= new ElementTreeNode(d1.getType(),"1",d1.getType(),false);
                    s+="/////";
                }
                else elementData[i]= new ElementTreeNode(d1.getID(),d1.getType(),d1.getID(), false);    
                 s+=" ["+i+"] "+" Type : "+d1.getType()+" name " +d1.getID(); 
                i++;
           }
        }
        hLayout2.removeMember(elementTreeGrid);
        
        // SC.say(s+" i  : "+i);
         
        elementTree = new Tree();  
        elementTree.setModelType(TreeModelType.PARENT);  
        elementTree.setRootValue("1");
        elementTree.setNameProperty("Element");  
        elementTree.setIdField("ElementId");  
        elementTree.setParentIdField("ReportsTo");  
        elementTree.setOpenProperty("isOpen");  
        elementTree.setData(elementData);  
        
  
        elementTreeGrid = new TreeGrid();    
       // elementTreeGrid.setHeight(150);  
       // elementTreeGrid.setWidth(260);  
        elementTreeGrid.setData(elementTree); 
        elementTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);  
        elementTreeGrid.setSelectionType(SelectionStyle.SIMPLE);
        elementTreeGrid.setShowSelectedStyle(false); 
        elementTreeGrid.setShowPartialSelection(true);  
        elementTreeGrid.setCascadeSelection(true);  
        elementTreeGrid.selectAllRecords();
        elementTreeGrid.draw();
        elementTreeGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
            public void onSelectionChanged(SelectionEvent event) {
               objectModel.unsetElement(event.getSelection());
            }
        });
        
        hLayout2.addMember(elementTreeGrid);
    }
    public static class ElementTreeNode extends TreeNode {  
        public ElementTreeNode(String elementId, String reportsTo, String element, boolean isOpen) {  
            setAttribute("ElementId", elementId);  
            setAttribute("ReportsTo", reportsTo);  
            setAttribute("Element", element);   
            setAttribute("isOpen", isOpen);  
        }  
    } 
    private void refreshLaunchTabValue()
     {   
         SimulationGUIControlBox.getInstance("US","").refreshLaunchTabValue();
         SimulationGUIControlBox.getInstance("MRI(TEST)","").refreshLaunchTabValue();
         SimulationGUIControlBox.getInstance("CT","").refreshLaunchTabValue();
         SimulationGUIControlBox.getInstance("PET","").refreshLaunchTabValue();
     } 
}

