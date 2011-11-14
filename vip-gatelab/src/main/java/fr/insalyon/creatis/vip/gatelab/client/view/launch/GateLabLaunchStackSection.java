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
package fr.insalyon.creatis.vip.gatelab.client.view.launch;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchStackSection;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.common.AppletHTMLPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sorina Camarasu
 */
public class GateLabLaunchStackSection extends AbstractLaunchStackSection {

    private VLayout nameLayout;
    private VLayout inputsLayout;
    private VLayout begInputsLayout;
    private VLayout advInputsLayout;
    private IButton loadMac;
    private String baseDir;
    private AppletHTMLPane myApplet;
    private Map<String, String> paramsMap;
    private LoadMacWindow loadMacWindow;
    //private ListGrid inputsListGrid;
    //private ListGridField categoryField;

    //private GateLabLaunchTab myLaunchTab;
    public GateLabLaunchStackSection(String applicationName) {
        super(applicationName);

        initComplete(this);

        nameLayout = new VLayout(5);
        inputsLayout = new VLayout(5);
        advInputsLayout = new VLayout(3);
        begInputsLayout = new VLayout(3);
        loadMac = new IButton("Load Main MacFile");
        modal = new ModalWindow(vLayout);

        baseDir = "/vip/Home/myGateSimus/inputs";

        initComplete(this);

        configureForm();

        //this.addItem(layout);
        loadData();
    }

    public void uploadMacComplete(String inputTgz, String simuType, String nbPart) {

        //this.layout.removeChild(myApplet);
        if (loadMacWindow != null) {
            loadMacWindow.destroy();
            loadMacWindow = null;

            modal.hide();
            //SC.say("File name is " + inputTgz + " simu type " + simuType);
            //add info to the map and launch button

            String[] it = inputTgz.split(" = ");
            setInputValue(it[0], baseDir.concat("/").concat(it[1]));
            //We do not fill in the parallelization type automaticlaly for the moment
            //String[] st = simuType.split(" = ");
            //setInputValue(st[0], st[1]);
            String[] np = nbPart.split(" = ");
            setInputValue(np[0], np[1]);

            inputsLayout.setVisible(true);

        }
    }

    private native void initComplete(GateLabLaunchStackSection uploadMac) /*-{
    $wnd.uploadMacComplete = function (inputTgz,simuType,nbPart) {
    uploadMac.@fr.insalyon.creatis.vip.gatelab.client.view.launch.GateLabLaunchStackSection::uploadMacComplete(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(inputTgz,simuType,nbPart);
    };
    }-*/;

    private void configureForm() {

        nameLayout.setWidth100();
        nameLayout.setAutoHeight();

        inputsLayout.setWidth100();
        inputsLayout.setAutoHeight();
        inputsLayout.setVisible(false);

        begInputsLayout.setAutoHeight();
        advInputsLayout.setAutoHeight();

        loadMac.setWidth(150);
        loadMac.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                //set the correct path for upload

                loadMacWindow = new LoadMacWindow(modal, baseDir);
                loadMacWindow.show();

                /*
                myApplet = new AppletHTMLPane(
                "DataUpload",
                "fr.insalyon.creatis.vip.gatelab.applet.loadmac.LoadMac",
                "vip-gatelab-applet.jar", 800, 450,
                Context.getInstance().getUser(),
                Context.getInstance().getUserDN(),
                Context.getInstance().getProxyFileName(),
                baseDir, false, false);
                layout.addChild(myApplet);
                 *
                 */
                //layout.show();
                //layout.showMember(stack, null);
            }
        });

        inputsLayout.addMember(begInputsLayout);
        inputsLayout.addMember(FieldUtil.getForm(new TextItem("Advanced inputs")));
        inputsLayout.addMember(advInputsLayout);

        vLayout.addMember(nameLayout);
        vLayout.addMember(inputsLayout);
    }

    public void loadNameLayout() {
        
        nameLayout.addMember(getSimulatioNameLayout());
        HLayout macLayout = new HLayout(5);
        macLayout.setAlign(VerticalAlignment.CENTER);
        macLayout.setMargin(20);
        macLayout.addMember(loadMac);
        nameLayout.addMember(macLayout);
    }

    /**
     * Loads input values from string.
     *
     * @param values Input values
     */
    public void loadInput(String name, String values) {

        simulationNameItem.setValue(name);
        Map<String, String> valuesMap = new HashMap<String, String>();

        for (String input : values.split("<br />")) {
            String[] s = input.split(" = ");
            valuesMap.put(s[0], s[1] != null ? s[1] : "");
        }

        StringBuilder sb = new StringBuilder();
        for (GateLabInput input : getGateLabInputs()) {
            String value = valuesMap.get(input.getName());

            if (value != null) {
                input.setValue(value);
            } else {
                sb.append("Could not find value for parameter \""
                        + input.getName() + "\"<br />");
            }
        }
        if (sb.length() > 0) {
            SC.warn(sb.toString());
        }

        inputsLayout.setVisible(true);
    }

    /**
     * Sets a value to an input name. The value should be in the following forms:
     *
     * For single list field: a string
     * For multiple list fields: strings separated by '; '
     * For ranges: an string like 'Start: 0 - Stop: 0 - Step: 0'
     *
     * @param inputName
     * @param value
     */
    public void setInputValue(String inputName, String value) {

        for (GateLabInput input : getGateLabInputs()) {
            if (input.getName().equals(inputName)) {
                input.setValue(value);
            }
        }
    }

    /**
     * Loads simulation sources list.
     */
    protected void loadData() {

        nameLayout.removeMembers(nameLayout.getMembers());
        inputsLayout.removeMembers(inputsLayout.getMembers());

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Descriptor> callback = new AsyncCallback<Descriptor>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing get application sources list: " + caught.getMessage());
            }

            public void onSuccess(Descriptor d) {

                if (d != null) {

                    //nameLayout.addMember(getSimulatioNameLayout());
                    loadNameLayout();
                    List<Source> sources = d.getSources();
                    for (Source source : sources) {
                        inputsLayout.addMember(new GateLabInput(source.getName(), source.getDescription()));
                        //inputsListGrid.se
                    }

                    HLayout buttonsLayout = new HLayout(5);
                    buttonsLayout.setAlign(VerticalAlignment.CENTER);
                    buttonsLayout.setMargin(20);
                    inputsLayout.addMember(buttonsLayout);

                    IButton launchButton = new IButton("Launch");
                    launchButton.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            if (validate()) {
                                launch();
                            } else {
                                SC.warn("Cannot launch. Some inputs are not valid.");
                            }
                        }
                    });
                    buttonsLayout.addMember(launchButton);
                    buttonsLayout.addMember(getSaveInputsButton());
                    modal.hide();
                    
                } else {
                    modal.hide();
                    SC.warn("Unable to download application source file.");
                }
            }
        };
        modal.show("Loading Launch Panel...", true);
        service.getApplicationDescriptor(applicationName, callback);
    }

    /**
     * Validates the form before launch a simulation.
     *
     * @return Result of the validation
     */
    private boolean validate() {
        
        boolean valid = simulationNameItem.validate();
        for (GateLabInput input : getGateLabInputs()) {
            if (!input.validate()) {
                valid = false;
            }

        }
        return valid;
    }

    /**
     * Launches a simulation.
     */
    private void launch() {
        
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error while launching simulation: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                SC.say("Simulation " + simulationNameItem.getValueAsString() + "successefully lauched");
            }
        };
        modal.show("Launching simulation...", true);
        service.launchSimulation(getParametersMap(), applicationName, simulationNameItem.getValueAsString(), callback);
    }

    /**
     * Gets a map of parameters.
     *
     * @return Map of parameters
     */
    public Map<String, String> getParametersMap() {

        Map<String, String> paramsMap = new HashMap<String, String>();

        for (GateLabInput input : getGateLabInputs()) {
            paramsMap.put(input.getName(), input.getValue());

        }
        return paramsMap;
    }

    /*
    public List<GateLabInput> getGateLabInputs() {
    
    List<GateLabInput> inputList = new ArrayList<GateLabInput>();
    
    for (Canvas canvas : inputsLayout.getMembers()) {
    //inputsLayout contains begInputsLayout and advInputsLayout
    for (Canvas inputCanvas : canvas.getChildren()) {
    if (inputCanvas instanceof GateLabInput) {
    GateLabInput input = (GateLabInput) inputCanvas;
    inputList.add(input);
    }
    }
    }
    
    return inputList;
    
    }
     *
     */
    /**
     * Gets the list of all GateLabInputs objects.
     *
     * @return list of all GateLabInputs objects
     */
    public List<GateLabInput> getGateLabInputs() {

        List<GateLabInput> inputList = new ArrayList<GateLabInput>();

        for (Canvas inputCanvas : inputsLayout.getMembers()) {

            if (inputCanvas instanceof GateLabInput) {
                GateLabInput input = (GateLabInput) inputCanvas;
                inputList.add(input);
            }

        }
        return inputList;
    }
}
