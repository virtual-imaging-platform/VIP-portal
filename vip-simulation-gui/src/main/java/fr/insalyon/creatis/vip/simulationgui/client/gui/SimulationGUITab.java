

package fr.insalyon.creatis.vip.simulationgui.client.gui;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelLight;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.models.client.rpc.ModelService;
import fr.insalyon.creatis.vip.models.client.rpc.ModelServiceAsync;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.ObjectModel;
import fr.insalyon.creatis.vip.simulationgui.client.rpc.VTKController;
import fr.insalyon.creatis.vip.simulationgui.client.rpc.VTKControllerAsync;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 *  @author moulin
 */
class SimulationGUITab extends Tab{
    private final ToolStrip toolStrip;
    private final DefineSceneSection defineSceneSection;
   // private final DefineParamsSection defineParamsSection;
    private VTKControllerAsync VTK =  VTKController.Util.getInstance();
    private ModelServiceAsync MAP = ModelService.Util.getInstance();  
    private ModalWindow modal;  
    private String dynaStringTab[];
    private SelectItem modelBox = new SelectItem("model");
    private Map<String, String> mapNameUri = new HashMap<String, String>();
    private String uri;
    private ToolStripButton runButton = new ToolStripButton("Load example");  
    static private String modelStorageURL;
    //dans le constructeur, creer les 4 tabs. Les ajouter/enlever du Layout en fonction des cases cochees
    
    public SimulationGUITab() {
        this.setTitle("New Simulation");
        this.setID("new-simulation-tab");
        this.setCanClose(true);
        VLayout vLayout = new VLayout();
        final SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);
        sectionStack.setCanResizeSections(true);
        sectionStack.showSection(0);
        sectionStack.hideSection(1);
        defineSceneSection = new DefineSceneSection();       
        sectionStack.setSections(defineSceneSection);
        
        toolStrip = new ToolStrip();
        modal = new ModalWindow(toolStrip);
         runButton.setIcon("icon-information.png");  
        runButton.setActionType(SelectionType.CHECKBOX);
        toolStrip.setWidth100();
        toolStrip.setHeight(20);
        toolStrip.addButton(runButton);     
        toolStrip.addSeparator();  
        toolStrip.addFormItem(modelBox); 
        vLayout.addMember(toolStrip);
        vLayout.addMember(sectionStack);
          
        this.setPane(vLayout);
        initRPC();
        
    }
   private void initRPC()
   {
         runButton.addClickHandler( new ClickHandler(){           
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
            defineSceneSection.showModal("Object making");
            String path= "/home/moulin/NetBeansProjects/Portal/vip-simulation-gui/src/main/resources";
            String path2= "/home/moulin/ressources";
                
                VTK.downloadAndUnzipExample(path2, new AsyncCallback<Data3D[][]>(){
							public void onSuccess(Data3D[][] result) 
                                                        {       
                                                                defineSceneSection.hideModal();
                                                                SimulationGUIControlBoxModel.getInstance().setTreeNode(result);
                                                                ObjectModel.getInstance().addModel(result);
                                                                
							}
                                                        public void onFailure(Throwable caught) 
                                                        {
                                                            defineSceneSection.hideModal();
								// Show the RPC error message to the user
							    SC.say("VTK error");
                                                               
                                                        }
         
						});
                          
            }
        });
         modal.show("Loading list of model from grid", true);
         MAP.listAllModels( new AsyncCallback<List<SimulationObjectModelLight>>(){
                              public void onSuccess(List<SimulationObjectModelLight> result) 
                                                        {     
                                                            modal.hide();
                                                             dynaStringTab=new String[result.size()];
                                                             int i=0;
                                                             for (SimulationObjectModelLight s : result){	                                                                                                                               
                                                             dynaStringTab[i]=s.getModelName();
                                                             mapNameUri.put(s.getModelName(),s.getURI()); 
                                                             i++;
                                                            }
                                                            modelBox.setValueMap(dynaStringTab);
                                                            toolStrip.setHeight(10);
							}
                                                        public void onFailure(Throwable caught) 
                                                        {							    
                                                            modelBox.setValue("Model can't be load");
                                                            modal.hide();
                                                            toolStrip.setHeight(10);
                                                        }
         
	}); 
         
         modelBox.addChangeHandler(new ChangeHandler() {  
            public void onChange(ChangeEvent event) {  
                String selectedItem = (String) event.getValue();  
                uri=mapNameUri.get(selectedItem);            
                defineSceneSection.showModal("Object downloading ");
                MAP.rebuildObjectModelFromTripleStore(uri,  new AsyncCallback<SimulationObjectModel>(){
                         
                              public void onSuccess(final SimulationObjectModel result) 
                                                        {   
                                                            modelStorageURL=result.getStorageURL();
                                                            defineSceneSection.showModal("Object making");
                                                            VTK.downloadAndUnzipModel(modelStorageURL,Context.getInstance().getProxyFileName(),Context.getInstance().getUser(), new AsyncCallback<Data3D[][]>(){
                                                            public void onSuccess(Data3D[][] result2) 
                                                            {       
                                                                defineSceneSection.hideModal();
                                                                 ObjectModel.getInstance().addModel(result2);
                                                                 SimulationGUIControlBoxModel.getInstance().setTreeNode(result2);
                                                                 refreshLaunchTabValue();
                                                            }
                                                            public void onFailure(Throwable caught) 
                                                            {
                                                                    // Show the RPC error message to the user
                                                                defineSceneSection.hideModal();
                                                                 SC.say("Error during the making");

                                                            }

                                                                            });
                          
							}
                                                        public void onFailure(Throwable caught) 
                                                        {
                                                            defineSceneSection.hideModal();
							    SC.say("url error");
                                                            modelBox.setValue("Grid error");
                                                        }
         
	       }); 
            }  
        });
         
   }
   static public String getModelStorage()
   {
       return modelStorageURL;
   }
    private void refreshLaunchTabValue()
     {   
         SimulationGUIControlBox.getInstance("US","").refreshLaunchTabValue();
         SimulationGUIControlBox.getInstance("MRI","").refreshLaunchTabValue();
         SimulationGUIControlBox.getInstance("CT","").refreshLaunchTabValue();
         SimulationGUIControlBox.getInstance("PET","").refreshLaunchTabValue();
     } 
}
