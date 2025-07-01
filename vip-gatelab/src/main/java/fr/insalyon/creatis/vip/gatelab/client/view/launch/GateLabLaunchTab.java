/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.gatelab.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplicationExtensions;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesInput;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesStringInput;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchFormLayout;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchTab;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;

import java.util.*;

/**
 *
 * @author Sorina Camarasu, Rafael Ferreira da Silva
 */
public class GateLabLaunchTab extends LaunchTab {

    private LoadMacWindow loadMacWindow;
    private String baseDir;
    private IButton loadMacButton;

    public static final String GATE_INPUT_ID = "gateInput";
    public static final String NB_JOBS_INPUT_ID = "numberOfJobs";
    public static final String MACFILE_INPUT_ID = "macfileName";

    public GateLabLaunchTab(String applicationName, String applicationVersion, String applicationClass) {
        super(applicationName, applicationVersion, applicationClass);
    }

    public GateLabLaunchTab(String applicationName, String applicationVersion, String applicationClass,
            String simulationName, Map<String, String> inputs) {
        super(applicationName, applicationVersion, applicationClass, simulationName, inputs);
    }

    @Override
    protected void init() {
        super.init();
        baseDir = DataManagerConstants.ROOT + "/Home/myGateSimus/inputs";
        this.showExamples = false;
        this.showSeparators = false;

        if (this.inputs == null) {
            // if inputs is null, it is NOT a relaunch and only the launch mac button must be shown first
            initComplete(this);
            configureLoadMacButton();
        }
    }

    private void configureLoadMacButton() {

        loadMacButton = WidgetUtil.getIButton("Load Main MacFile", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        loadMacWindow = new LoadMacWindow(modal, baseDir);
                        loadMacWindow.show();
                    }
                });
        loadMacButton.setWidth(150);
    }

    @Override
    protected void addExtensionAndCreateForm(
            BoutiquesApplication applicationTool, Boolean addResultsDirectoryInput, Runnable launchFormCreator) {
        verifyBoutiquesDescriptor(applicationTool);
        BoutiquesApplicationExtensions extensions = new BoutiquesApplicationExtensions(true);
        applicationTool.setBoutiquesExtensions(extensions);

        extensions.addNonListInputs(
                GATE_INPUT_ID, NB_JOBS_INPUT_ID, MACFILE_INPUT_ID);
        enrichNumberOfJobsInput(applicationTool, extensions);
        launchFormCreator.run();
    }

    private void verifyBoutiquesDescriptor(BoutiquesApplication applicationTool) {
        verifyBoutiquesInput(applicationTool, GATE_INPUT_ID, BoutiquesInput.InputType.FILE);
        verifyBoutiquesInput(applicationTool, NB_JOBS_INPUT_ID, BoutiquesInput.InputType.NUMBER);
        verifyBoutiquesInput(applicationTool, MACFILE_INPUT_ID, BoutiquesInput.InputType.STRING);
    }

    private void verifyBoutiquesInput(BoutiquesApplication applicationTool, String inputId, BoutiquesInput.InputType type) {
        Optional<BoutiquesInput> boutiquesInput =
                applicationTool.getInputs().stream().filter(input -> inputId.equals(input.getId())).findAny();
        LaunchFormLayout.assertCondition(boutiquesInput.isPresent(),
                "Missing {" + inputId + "} input in Gate descriptor");
        LaunchFormLayout.assertCondition(
                type.equals(boutiquesInput.get().getType()),
                "Input {" + inputId + "} must have a number type in Gate descriptor");
    }

    private void enrichNumberOfJobsInput(
            BoutiquesApplication applicationTool, BoutiquesApplicationExtensions extensions) {
        BoutiquesInput numberOfJobs = applicationTool.getInputs().stream()
                .filter(input -> NB_JOBS_INPUT_ID.equals(input.getId()))
                .findAny().orElseThrow(IllegalStateException::new);
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("1", "Short simulation of few minutes");
        map.put("10", "Simulation of few hours");
        map.put("100", "Simulation of a few days");
        map.put("500", "Simulation of more than a few days");
        LaunchFormLayout.assertCondition(
                numberOfJobs.getPossibleValues() != null && numberOfJobs.getPossibleValues().equals(map.keySet()),
                "Number of jobs in Gate descriptor does not have the expected values");
        extensions.addValueChoiceLabels(NB_JOBS_INPUT_ID, map);
    }


    @Override
    protected void onLaunchFormCreated() {
        if (this.inputs == null) {
            // on init, hide all inputs and launch buttons to put only the "load mac buttons"
            // only if inputs is null, otherwise it is a relaunch
            launchFormLayout.hideInputs();
            launchFormLayout.disableErrorsAndWarnings();
            launchFormLayout.setButtons(5, loadMacButton);
        }
    }

    @Override
    protected void onLaunchFormReady() {
        super.onLaunchFormReady();
        if (this.inputs != null) {
            customizeGateForm();
        }
    }

    //Bug #2368
    private native void initComplete(GateLabLaunchTab uploadMac) /*-{
     $wnd.uploadMacComplete = function (inputList) {
     uploadMac.@fr.insalyon.creatis.vip.gatelab.client.view.launch.GateLabLaunchTab::uploadMacComplete(Ljava/lang/String;)(inputList);
     };
     $wnd.close = function () {
     uploadMac.@fr.insalyon.creatis.vip.gatelab.client.view.launch.GateLabLaunchTab::close()();
     };
     }-*/;

    //Bug #2368
    public void uploadMacComplete(String inputList) {

        if (loadMacWindow != null) {
            loadMacWindow.destroy();
            loadMacWindow = null;

            modal.hide();
            // we get something like "gateInput = " + fileName + ", macfileName = " + mainMacFileName;
            String[] inputs = inputList.split(", ");
            Map<String,String> valuesMap = new HashMap<>();

            // first is a special case, we need to edit the path
            String[] it = inputs[0].split(" = ");
            valuesMap.put(it[0], baseDir.concat("/").concat(it[1]));

            String[] keyAndValue = inputs[1].split(" = ");
            valuesMap.put(keyAndValue[0], keyAndValue[1]);

            super.createButtons(); // override "load mac button" with "launch button"
            launchFormLayout.showInputs();
            launchFormLayout.enableErrorsAndWarnings();
            launchFormLayout.loadInputs(launchFormLayout.getSimulationName(), valuesMap, false);

            customizeGateForm();
        }
    }

    public void customizeGateForm() {
        // hide and disable some inputs
            launchFormLayout.makeInputUnmodifiable(GATE_INPUT_ID);
    }

    // called from JS
    public void close() {
        if (loadMacWindow != null) {
            loadMacWindow.destroy();
            loadMacWindow = null;

            modal.hide();
        }
    }
}
