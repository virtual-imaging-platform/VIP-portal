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
package fr.insalyon.creatis.vip.applicationimporter.client.view.applicationdisplay;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.ApplicationImporterService;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.applicationimporter.client.view.JSONUtil;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class DisplayTab extends Tab {

    // Layouts
    VLayout globalLayout;
    GeneralLayout generalLayout;
    InputLayout inputsLayout;
    OutputLayout outputsLayout;
    VIPLayout vipLayout;

    // JSON object representing the application.
    JSONObject jsonObject;

    // Fields in the JSON object.
    String name;
    String toolVersion;
    String description;
    String commandLine;
    String dockerImage;
    String dockerIndex;
    String schemaVersion;
    List<BoutiquesInput> inputs;
    List<BoutiquesOutputFile> outputFiles;

    // Modal
    private ModalWindow modal;

    public DisplayTab(String tabIcon, String tabId, String tabName) {
        this.setTitle(Canvas.imgHTML(tabIcon) + " " + tabName.trim());
        this.setID(tabId);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        configure();
        inputs = new ArrayList<BoutiquesInput>();
        outputFiles = new ArrayList<BoutiquesOutputFile>();
        modal = new ModalWindow(globalLayout);
        this.setPane(globalLayout);
    }

    /**
     * Creates the general layout of the tab.
     */
    private void configure() {
        globalLayout = new VLayout();
        globalLayout.setWidth100();
        globalLayout.setHeight100();
        globalLayout.setMargin(6);
        globalLayout.setMembersMargin(5);

        generalLayout = new GeneralLayout("50%", "100%");

        inputsLayout = new InputLayout("50%", "100%");
        outputsLayout = new OutputLayout("50%", "100%");
        vipLayout = new VIPLayout("50%", "100%");

        HLayout hLayout1 = new HLayout();
        hLayout1.setMembersMargin(10);
        hLayout1.setHeight("50%");
        hLayout1.addMember(generalLayout);
        hLayout1.addMember(outputsLayout);
        globalLayout.addMember(hLayout1);

        HLayout hLayout2 = new HLayout();
        hLayout2.setMembersMargin(10);
        hLayout2.setHeight("50%");
        hLayout2.addMember(inputsLayout);
        hLayout2.addMember(vipLayout);
        globalLayout.addMember(hLayout2);

        IButton createApplicationButton;
        createApplicationButton = WidgetUtil.getIButton("Create application", Constants.ICON_LAUNCH, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                createApplication(jsonObject, vipLayout.getApplicationLocation(), vipLayout.getVIPClasses());
            }
        });
        globalLayout.addMember(createApplicationButton);
    }

    /**
     * Populates the class with instance variables containing values in the JSON
     * object, and refreshes the display.
     *
     * @param jsonObject
     */
    public void setJSONObject(JSONObject jsonObject) {

        this.jsonObject = jsonObject;

        // Parses JSON object;
        name = JSONUtil.getPropertyAsString(jsonObject, "name");
        toolVersion = JSONUtil.getPropertyAsString(jsonObject, "tool-version");
        description = JSONUtil.getPropertyAsString(jsonObject, "description");
        commandLine = JSONUtil.getPropertyAsString(jsonObject, "command-line");
        dockerImage = JSONUtil.getPropertyAsString(jsonObject, "docker-image");
        dockerIndex = JSONUtil.getPropertyAsString(jsonObject, "docker-index");
        schemaVersion = JSONUtil.getPropertyAsString(jsonObject, "schema-version");

        JSONArray inputJSONArray = JSONUtil.getPropertyAsArray(jsonObject, "inputs");
        if (inputJSONArray != null) {
            for (int i = 0; i < inputJSONArray.size(); i++) {
                BoutiquesInput bi = new BoutiquesInput();
                bi.setJSON(inputJSONArray.get(i).isObject());
                inputs.add(bi);
            }
        }

        JSONArray outputJSONArray = JSONUtil.getPropertyAsArray(jsonObject, "output-files");
        if (outputJSONArray != null) {
            for (int i = 0; i < outputJSONArray.size(); i++) {
                BoutiquesOutputFile bof = new BoutiquesOutputFile();
                bof.setJSON(outputJSONArray.get(i).isObject());
                outputFiles.add(bof);
            }
        }
        updateDisplay();
    }

    /**
     * Updates the values of the interface fields based on the object
     * attributes.
     */
    private void updateDisplay() {

        // Update general information
        generalLayout.setName(name);
        generalLayout.setVersion(toolVersion);
        generalLayout.setDescription(description);
        generalLayout.setCommandLine(commandLine);
        generalLayout.setDockerImage(dockerImage);
        generalLayout.setDockerIndex(dockerIndex);
        generalLayout.setSchemaVersion(schemaVersion);

        // Update inputs and outputs
        inputsLayout.setInputs(inputs);
        outputsLayout.setOutputFiles(outputFiles);
    }

    private void createApplication(JSONObject jsonObject, String applicationLocation, String[] VIPClasses) {
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage(caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("Application successfully created.");
            }
        };
        modal.show("Creating application...", true);
        ApplicationImporterService.Util.getInstance().createApplication(jsonObject.toString(), vipLayout.getApplicationLocation(), vipLayout.getVIPClasses(), callback);
    }

}
