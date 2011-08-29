
package fr.insalyon.creatis.vip.simulationgui.client.gui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HoverEvent;
import com.smartgwt.client.widgets.events.HoverHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Portlet;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchTab;
import fr.insalyon.creatis.vip.core.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Object3D;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.ObjectModel;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.ObjectSimulateur;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Scene;
import fr.insalyon.creatis.vip.simulationgui.client.rpc.VTKController;
import fr.insalyon.creatis.vip.simulationgui.client.rpc.VTKControllerAsync;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author moulin
 */

public class SimulationGUIControlBox
{
     /////////////////////Data Member//////////////////////
   
     
     private Portlet portletControl;
     
     private HLayout hLayout1=new HLayout();
     private DynamicForm form = new DynamicForm();
     private DynamicForm form2 = new DynamicForm(); 
     private DynamicForm form3 = new DynamicForm(); 
     private DynamicForm form4 = new DynamicForm(); 
     private SpinnerItem spinnerx = new SpinnerItem();
     private SpinnerItem spinnery = new SpinnerItem();
     private SpinnerItem spinnerz = new SpinnerItem(); 
     private SpinnerItem spinnerax = new SpinnerItem();
     private SpinnerItem spinneray = new SpinnerItem();
     private SpinnerItem spinneraz = new SpinnerItem(); 
     private Slider hSlider = new Slider("Step"); 
     private String id;
     private Object3D simu;
     private CheckboxItem modBox;  
     private CheckboxItem modAxis;   
     private CheckboxItem masterCheckbox;  
     private boolean enable=false;
     private String applicationClass;
     private SelectItem simulatorSelectItem;
     
     private LaunchTab launchTab=null;
        
     private static HashMap<String,SimulationGUIControlBox> instances = new HashMap<String,SimulationGUIControlBox>();
     public static synchronized SimulationGUIControlBox getInstance(String key,String applicationClass)
        {
                SimulationGUIControlBox inst = instances.get(key);
                if (inst == null)
                {
                inst = new SimulationGUIControlBox(key,applicationClass);
                instances.put(key, inst);
                }
                return inst;
        }
     
     
     private SimulationGUIControlBox(String contents,String applicationClass)
     {
          this.applicationClass=applicationClass;

          id=contents;
          simu= new ObjectSimulateur(contents);
             
          masterCheckbox= new CheckboxItem(id);
          
          portletControl= new Portlet();
          portletControl.setTitle(id);
          
          simulatorSelectItem = new SelectItem("simulator ");
          form4.setFields(simulatorSelectItem);
          
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
          modBox=new CheckboxItem("Symbol");  
          modAxis.setValue(true);
          
          
          form3.setAutoFocus(false);  
          form3.setNumCols(6);  
          form3.setFields(modBox,modAxis);
          
          
          hSlider.setVertical(false);
          hSlider.setMinValue(1f);  
          hSlider.setMaxValue(10f);  
          hSlider.setNumValues(1000);  
            
          hSlider.setTop(200);  
          hSlider.setLeft(100);   
            
           
          hLayout1.addMember(hSlider);
          hLayout1.setWidth100();
          hLayout1.setHeight(50);
            
        
          
          portletControl.addItem(form4);
          portletControl.addItem(form2);
          portletControl.addItem(form);
          portletControl.addItem(form3);
          portletControl.addItem(hLayout1);
          simu = new ObjectSimulateur(id);
            
          loadFormSimulator();
          setControl();
          
          
         
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
               simu.setTranslateX(Float.valueOf(spinnerx.getValueAsString()));
               Scene.getInstance().refreshScreen();
               refreshLaunchTabValue();
            }
        });
         spinnery.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
               simu.setTranslateY(Float.valueOf(spinnery.getValueAsString()));
               Scene.getInstance().refreshScreen();
               refreshLaunchTabValue();
            }
        });
          spinnerz.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
                simu.setTranslateZ(Float.valueOf(spinnerz.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
       spinnerax.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
                simu.setAngleX(Integer.valueOf(spinnerax.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();  
            }
        });
         spinneray.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
                simu.setAngleY(Integer.valueOf(spinneray.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
        });
        spinneraz.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event){
                simu.setAngleZ(Integer.valueOf(spinneraz.getValueAsString()));
                Scene.getInstance().refreshScreen();
                refreshLaunchTabValue();
            }
            
        });
      
             modBox.addChangeHandler(new ChangeHandler() 
             {
                public void onChange(ChangeEvent event) {
                if(modBox.getValueAsBoolean())
                {
                    simu.disable("model");
                    simu.disable("box");
                    Scene.getInstance().refreshBuffer();
                    Scene.getInstance().refreshScreen();
                }
                else
                {
                    simu.enable("model");
                    simu.enable("box");
                    Scene.getInstance().refreshBuffer();
                    Scene.getInstance().refreshScreen();
                }
                 
             }
             });
              modAxis.addChangeHandler(new ChangeHandler() 
             {
                public void onChange(ChangeEvent event) {
                if(modAxis.getValueAsBoolean())
                {
                     simu.disable("axis");
                     Scene.getInstance().refreshBuffer();
                     Scene.getInstance().refreshScreen();
                }
                else
                {
                    simu.enable("axis");
                    Scene.getInstance().refreshBuffer();
                    Scene.getInstance().refreshScreen();
                }
                 
             }
             });
        
             simulatorSelectItem.addChangedHandler(new ChangedHandler() {  
              public void onChanged(ChangedEvent event) {
                 Layout.getInstance().removeTab(launchTab);
                 launchTab = new LaunchTab(applicationClass, simulatorSelectItem.getValueAsString());
                 launchTab.setCanClose(false);
                 Layout.getInstance().addTab(launchTab, false);
                 refreshLaunchTabValue();
            }
             });
             
             //////////////////////////// Hover ///////////////////////
              simulatorSelectItem.addItemHoverHandler(new ItemHoverHandler() {  
               public void onItemHover(ItemHoverEvent event) {  
                String prompt = "Change the version of simulator";  
                simulatorSelectItem.setPrompt(prompt);  
            }  
             }); 
             
               spinnerx.addItemHoverHandler(new ItemHoverHandler() {  
               public void onItemHover(ItemHoverEvent event) {  
                String prompt = "Translate the following object to this value in millimeter";  
                spinnerx.setPrompt(prompt);  
            }  
             }); 
               spinnery.addItemHoverHandler(new ItemHoverHandler() {  
               public void onItemHover(ItemHoverEvent event) {  
                String prompt = "Translate the following object to this value in millimeter"; 
                spinnery.setPrompt(prompt);  
            }  
             }); 
               spinnerz.addItemHoverHandler(new ItemHoverHandler() {  
               public void onItemHover(ItemHoverEvent event) {  
                String prompt = "Translate the following object to this value in millimeter";  
                spinnerz.setPrompt(prompt);  
            }  
             }); 
              spinnerax.addItemHoverHandler(new ItemHoverHandler() {  
               public void onItemHover(ItemHoverEvent event) {  
                String prompt = "Rotate the following object to this value in degree with XYZ convention";
                spinnerx.setPrompt(prompt);  
            }  
             }); 
               spinneray.addItemHoverHandler(new ItemHoverHandler() {  
               public void onItemHover(ItemHoverEvent event) {  
                String prompt = "Rotate the following object to this value in degree with XYZ convention";  
                spinnery.setPrompt(prompt);  
            }  
             }); 
               spinneraz.addItemHoverHandler(new ItemHoverHandler() {  
               public void onItemHover(ItemHoverEvent event) {  
                String prompt = "Rotate the following object to this value in degree with XYZ convention";  
                spinnerz.setPrompt(prompt);  
            }  
             }); 
                  modAxis.addItemHoverHandler(new ItemHoverHandler() {  
               public void onItemHover(ItemHoverEvent event) {  
                String prompt = "Show/hide the axis for the following object";  
                spinnerz.setPrompt(prompt);  
            }  
             }); 
                 modBox.addItemHoverHandler(new ItemHoverHandler() {  
               public void onItemHover(ItemHoverEvent event) {  
                String prompt = "Show/hide the symbol for the following object";  
                spinnerz.setPrompt(prompt);  
            }  
             }); 
                 hSlider.setCanHover(Boolean.TRUE);
             hSlider.addHoverHandler(new HoverHandler(){
            public void onHover(HoverEvent event) {
                String prompt = "Set the step for translation spinners";  
                hSlider.setPrompt(prompt);
            }
        });  

     }
     public void refreshLaunchTabValue()
     {
         float x=simu.getTranslateX()-ObjectModel.getInstance().getTranslateX();
         float y=simu.getTranslateY()-ObjectModel.getInstance().getTranslateY();
         float z=simu.getTranslateZ()-ObjectModel.getInstance().getTranslateZ();
         float ax=(ObjectModel.getInstance().getAngleX()-simu.getAngleX())%360;
         float ay=(ObjectModel.getInstance().getAngleY()-simu.getAngleY())%360;
         float az=(ObjectModel.getInstance().getAngleZ()-simu.getAngleZ())%360;
         launchTab.setInputValue("Transformation",x+";"+y+";"+z+";"+ax+";"+ay+";"+az+";");
         launchTab.setInputValue("Model", SimulationGUITab.getModelStorage());
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
      if(enable)return simu;
       else return null;
        
     }
     public void enableView()
     {
      enable=true;
      if(launchTab!=null)
      {
          
          Layout.getInstance().addTab(launchTab,false);
          refreshLaunchTabValue();
      }
      
     }
     public void disableView()
     {
      enable=false;
      if(launchTab!=null)
      {
          String s=launchTab.getTitle();
          Layout.getInstance().removeTab(launchTab);
          launchTab = new LaunchTab(applicationClass,s.replaceAll("Launch ", ""));
      }
     }
      public void setControlOnObject(Object3D mod)
     {
      simu=mod;
     }
     public Portlet getControlPortlet()
     {
         return portletControl;
     }
     public CheckboxItem getCheckbox()
     {
         return masterCheckbox;
     }
    private void loadFormSimulator()
    {     
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get workflow descriptors lists\n" + caught.getMessage());
                 simulatorSelectItem.setValue("No available Workflow");
                 launchTab=null;
            }

            public void onSuccess(List<String> result) {

               String dynaStringTab[]= new String [result.size()];
                for(int i=0;i<result.size();i++)
                {
                     dynaStringTab[i]=result.get(i);
                }
                simulatorSelectItem.setValueMap(dynaStringTab);
                simulatorSelectItem.setDefaultToFirstOption(true);
                launchTab = new LaunchTab(applicationClass,dynaStringTab[0]); //applicationclass a la place de Id si on veut lancé que la classe "simulation"
                launchTab.setCanClose(false);
                refreshLaunchTabValue();
            }
        };
        service.getApplicationsName(id, callback);
       }
 }
