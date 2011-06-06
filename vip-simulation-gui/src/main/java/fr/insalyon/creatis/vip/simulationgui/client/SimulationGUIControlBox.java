/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.simulationgui.client;



import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.FloatRangeValidator;
import com.smartgwt.client.widgets.layout.Portlet;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Scene;

/**
 *
 * @author moulin
 */
public class SimulationGUIControlBox extends Portlet 
{
     /////////////////////Data Member//////////////////////
    
     private static SimulationGUIControlBox instance; 
     private SpinnerItem spinnerx = new SpinnerItem();
     private SpinnerItem spinnery = new SpinnerItem();
     private SpinnerItem spinnerz = new SpinnerItem(); 
     private SpinnerItem spinnerax = new SpinnerItem();
     private SpinnerItem spinneray = new SpinnerItem();
     private SpinnerItem spinneraz = new SpinnerItem(); 
     private Slider hSlider = new Slider("Pitch"); 
     private float bob;
     SimulationGUIControlBox(String contents)
     {
          this.setTitle(contents);  
          final DynamicForm form = new DynamicForm();
          final DynamicForm form2 = new DynamicForm(); 
      
          spinnerx.setName("x");  
          spinnerx.setDefaultValue(0);  
          
          spinnery.setName("y");  
          spinnery.setDefaultValue(0);  
          
          spinnerz.setName("z");  
          spinnerz.setDefaultValue(-2);  
     
        
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
           
          
        
             
            hSlider.setVertical(false);  
            hSlider.setMinValue(1);  
            hSlider.setMaxValue(1000);  
            hSlider.setNumValues(1000);  
            hSlider.setTop(200);  
            hSlider.setLeft(100);   
            
            form.setFields(spinnerx,spinnery,spinnerz);
            form2.setFields(spinnerax,spinneray,spinneraz);
            setControl();
           
            
            this.addItem(form2);
            this.addItem(form);
            this.addItem(hSlider);
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
               // SC.say(" spinnery :"+ (Float.valueOf(spinnery.getValueAsString())));
                Scene.getInstance().setValueTranslation(Float.valueOf(spinnerx.getValueAsString()),Float.valueOf(spinnery.getValueAsString()),Float.valueOf(spinnerz.getValueAsString()));
            }
        });
         spinnery.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
               // SC.say(" spinnery :"+ (Float.valueOf(spinnery.getValueAsString())));
                Scene.getInstance().setValueTranslation(Float.valueOf(spinnerx.getValueAsString()),Float.valueOf(spinnery.getValueAsString()),Float.valueOf(spinnerz.getValueAsString()));
            }
        });
          spinnerz.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
               // SC.say(" spinnery :"+ (Float.valueOf(spinnery.getValueAsString())));
                Scene.getInstance().setValueTranslation(Float.valueOf(spinnerx.getValueAsString()),Float.valueOf(spinnery.getValueAsString()),Float.valueOf(spinnerz.getValueAsString()));
            }
        });
       spinnerax.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
               // SC.say(" spinnery :"+ (Float.valueOf(spinnery.getValueAsString())));
                Scene.getInstance().setValueRotation(Integer.valueOf(spinnerax.getValueAsString()),Integer.valueOf(spinneray.getValueAsString()),Integer.valueOf(spinneraz.getValueAsString()));
            }
        });
         spinneray.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
               // SC.say(" spinnery :"+ (Float.valueOf(spinnery.getValueAsString())));
                Scene.getInstance().setValueRotation(Integer.valueOf(spinnerax.getValueAsString()),Integer.valueOf(spinneray.getValueAsString()),Integer.valueOf(spinneraz.getValueAsString()));
            }
        });
        spinneraz.addChangedHandler(new ChangedHandler(){

            public void onChanged(ChangedEvent event) {
               // SC.say(" spinnery :"+ (Float.valueOf(spinnery.getValueAsString())));
                Scene.getInstance().setValueRotation(Integer.valueOf(spinnerax.getValueAsString()),Integer.valueOf(spinneray.getValueAsString()),Integer.valueOf(spinneraz.getValueAsString()));
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
  
 }
