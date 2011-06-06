/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.insalyon.creatis.vip.simulationgui.client;






import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.PortalLayout;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.Scene;


/**
 *
 * @author glatard
 */
class DefineSceneSection extends SectionStackSection {

    int compteur=0;
    static public Scene sceneDraw = Scene.getInstance();
    private SimulationGUIControlBox boxUS= new SimulationGUIControlBox("US");
    private SimulationGUIControlBox boxMRI= new SimulationGUIControlBox("MRI");
    private SimulationGUIControlBox boxSCAN= new SimulationGUIControlBox("CT Scan");
    private SimulationGUIControlBox boxPET= new SimulationGUIControlBox("PET");
        
    public DefineSceneSection() {
        this.setTitle("Scene");
           
        final Label labelAnswer = new Label("Your answer here...");  
        labelAnswer.setTop(50);  
        labelAnswer.setWidth(300);
        final VLayout Vlayout1 = new VLayout();
        final HLayout Hlayout1 = new HLayout();
        final PortalLayout portalLayout1 = new PortalLayout(2);
 
        portalLayout1.setShowColumnMenus(false);        
         DynamicForm form = new DynamicForm();
         
             final CheckboxItem US = new CheckboxItem();
             US.setTitle("Ultrasound");
             US.setValue(false);
             US.addChangeHandler(new ChangeHandler() {
                public void onChange(ChangeEvent event) 
                {
                    if(US.getValueAsBoolean())
                    {
                       portalLayout1.removePortlet(boxUS);
                       Vlayout1.draw();
                       compteur--;
                    }
                    else
                    {
                       
                       portalLayout1.addPortlet(boxUS,compteur % 2,0);
                       Vlayout1.draw();
                       compteur++;
                    }
                }
             });
             
             final CheckboxItem MRI = new CheckboxItem();
             MRI.setTitle("MRI");
             MRI.addChangeHandler(new ChangeHandler() 
             {
                public void onChange(ChangeEvent event) {
                if(MRI.getValueAsBoolean())
                {
                   portalLayout1.removePortlet(boxMRI);
                   Vlayout1.draw();
                   compteur--;
                }
                else
                {
                   portalLayout1.addPortlet(boxMRI,compteur % 2,0);
                   Vlayout1.draw();
                   compteur++;
                }
                 
             }
             });
               
             final CheckboxItem Scan = new CheckboxItem();
             Scan.setTitle("CT Scan");
             Scan.addChangeHandler(new ChangeHandler() 
             {
                public void onChange(ChangeEvent event) {
                if(Scan.getValueAsBoolean())
                {
                   portalLayout1.removePortlet(boxSCAN);
                   Vlayout1.draw();
                   compteur--;
                }
                else
                {
                   portalLayout1.addPortlet(boxSCAN, compteur % 2,0);
                   Vlayout1.draw();
                   compteur++;
                }
                 
             }
             });
              
             final CheckboxItem Pet = new CheckboxItem();
             Pet.setTitle("PET Scan");
             Pet.addChangeHandler(new ChangeHandler() 
             {
                public void onChange(ChangeEvent event) {
                if(Pet.getValueAsBoolean())
                {
                   portalLayout1.removePortlet(boxPET);
                   Vlayout1.draw();
                   compteur--;
                }
                else
                {
                   portalLayout1.addPortlet(boxPET, compteur % 2,0);
                   Vlayout1.draw();
                   compteur++;
                }
                 
             }
             });
              
              
          form.setFields(US, MRI, Scan, Pet);
             
          
           
        
        Vlayout1.addMember(form);
        Vlayout1.addMember(portalLayout1);
        Hlayout1.addMember(sceneDraw);
        Hlayout1.addMember(Vlayout1);
        
        
        
        
        this.addItem(Hlayout1);
        Vlayout1.draw();
        //showTimer();
    }
   /* private void showTimer() {
     Timer timer = new Timer() {
                       
                        public void run() {
                             /*SC.confirm("Proceed with Operation get AJAX?", new BooleanCallback() {
          public void execute(Boolean value) {
         
               }
               });
                            sceneDraw.setValueTranslation(boxUS.getTabValueAxis());
                            sceneDraw.setValueRotation(boxUS.getTabValueAngle());
                        }
                };
                timer.scheduleRepeating(5);
        }*/


}
