/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.simulationgui.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.HoverEvent;
import com.smartgwt.client.widgets.events.HoverHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModel;
import fr.cnrs.i3s.neusemstore.vip.semantic.simulation.model.client.bean.SimulationObjectModelUtil;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.simulationgui.client.SimulationGUIConstants;
import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import fr.insalyon.creatis.vip.simulationgui.client.gwtgl.ObjectModel;
import fr.insalyon.creatis.vip.simulationgui.client.rpc.VTKController;
import fr.insalyon.creatis.vip.simulationgui.client.rpc.VTKControllerAsync;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 *  @author Kevin Moulin, Rafael Silva
 */
public class SimulationGUITab extends Tab {

   

    private ToolStrip toolStrip;
    private DefineSceneSection defineSceneSection;
    // private final DefineParamsSection defineParamsSection;
    private VTKControllerAsync VTK = VTKController.Util.getInstance();
    private ModalWindow modal = null;
    private String dynaStringTab[];
    private Label modelLabel = new Label("Model");
    private LinkedHashMap<String, String> mapNameUri = new LinkedHashMap<String, String>();
    private String uri = "";
    private ToolStripButton exampleButton = new ToolStripButton("Load example model");
    static private String modelStorageURL = "";
    static private String modelURI;
    //dans le constructeur, creer les 4 tabs. Les ajouter/enlever du Layout en fonction des cases cochees
    Data3D[][] res_mod;
    int mod_const = 0;
    int mod_lenght = 0;

    private boolean test = true;
    static String modelName;
    
    public SimulationGUITab() {
        init();
    }

    public SimulationGUITab(String modelURI, String modelName, String modelStorageURL, List<String> modalities, boolean test) {
        this.modelName = modelName;
        this.modelURI = modelURI;
        init();
        this.test = test;      
        loadModel(modelURI);

//        for (String mod: modalities)
//            defineSceneSection.enableBox(mod);
    }

    private void init() {
        this.setTitle(Canvas.imgHTML(SimulationGUIConstants.ICON_EDITOR) + " "
                + this.modelName);
        this.setID(SimulationGUIConstants.TAB_EDITOR);
        this.setCanClose(true);

        SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setAnimateSections(true);
        sectionStack.setCanResizeSections(true);
        sectionStack.showSection(0);
        sectionStack.hideSection(1);
        defineSceneSection = new DefineSceneSection();
        sectionStack.setSections(defineSceneSection);

        toolStrip = new ToolStrip();
        modal = new ModalWindow(toolStrip);
        exampleButton.setIcon(SimulationGUIConstants.ICON_EXAMPLE);
        exampleButton.setActionType(SelectionType.CHECKBOX);
        toolStrip.setWidth100();
        toolStrip.setHeight(20);
        toolStrip.addButton(exampleButton);

        VLayout vLayout = new VLayout();
        vLayout.addMember(toolStrip);
        vLayout.addMember(sectionStack);

        this.setPane(vLayout);
        initControl();
//        SimulationGUIControlBoxModel.getInstance().checkBoxBox();

    }

    static String getModelURI() {
        return modelURI;
    }

    /**
     * Hover
     */
    private void loadModel(String uri) {
       
        defineSceneSection.showModal("Downloading model");
        VTK.rebuildObjectModelFromTripleStore(uri, new AsyncCallback<SimulationObjectModel>() {

            public void onSuccess(final SimulationObjectModel result) {

                showModalityBoxes(result);

                modelStorageURL = result.getStorageURL();
                modelURI = result.getURI();
                defineSceneSection.showModal("Rendering model");
               
                
                VTK.downloadAndUnzipModel(modelStorageURL, new AsyncCallback<Data3D[][]>() {

                    public void onSuccess(Data3D[][] result2) {
                        defineSceneSection.hideModal();

                        if(result2.length>=4)
                            ObjectModel.getInstance().addModel(result2);
                        SimulationGUIControlBoxModel.getInstance().setTreeNode(result2);

                    }

                    public void onFailure(Throwable caught) {
                        // Show the RPC error message to the user
                        defineSceneSection.hideModal();
                        Layout.getInstance().setWarningMessage("Error during the rendering: " + caught.getMessage());
                         //refreshLaunchTabValue();
                    }
                });

            }

            public void onFailure(Throwable caught) {
                defineSceneSection.hideModal();

                Layout.getInstance().setWarningMessage("Cannot download model");
            }
        });
    }

    private void initControl() {

        exampleButton.setCanHover(Boolean.TRUE);
        exampleButton.addHoverHandler(new HoverHandler() {

            public void onHover(HoverEvent event) {
                String prompt = "Show a standard model";
                exampleButton.setPrompt(prompt);
            }
        });

    }

    public static String getModelStorage() {

        return modelStorageURL;
    }

    private void refreshLaunchTabValue() {

        SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_US).refreshLaunchTabValue();
        SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_MRI).refreshLaunchTabValue();
        SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_CT).refreshLaunchTabValue();
        SimulationGUIControlBox.getInstance(SimulationGUIConstants.CLASS_PET).refreshLaunchTabValue();
    }

    private void showModalityBoxes(SimulationObjectModel model) {
        if(SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.IRM))
            defineSceneSection.enableBox("MRI");
        else 
            defineSceneSection.disableBox("MRI");
        

        if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.PET))
            defineSceneSection.enableBox("PET");
        else
            defineSceneSection.disableBox("PET");
            
        if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.CT)) 
            defineSceneSection.enableBox("CT");
        else
            defineSceneSection.disableBox("CT");
        
        if (SimulationObjectModelUtil.isReadyForSimulation(model, SimulationObjectModelUtil.Modality.US)) 
            defineSceneSection.enableBox("US");
        else
            defineSceneSection.disableBox("US");
    }
    
     static String getModelName() {
        return modelName;
    }
}
