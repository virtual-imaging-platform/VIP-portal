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

import com.google.gwt.json.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.layout.*;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.applicationimporter.client.*;
import fr.insalyon.creatis.vip.applicationimporter.client.bean.BoutiquesTool;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.ApplicationImporterService;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.HashMap;
import java.util.logging.*;

public class DisplayTab extends Tab {

    // Layouts
    private VLayout globalLayout;
    private GeneralLayout generalLayout;
    private InputLayout inputsLayout;
    private OutputLayout outputsLayout;
    private VIPLayout vipLayout;
    private final ModalWindow modal;
    private BoutiquesTool boutiquesTool;
    private HashMap<String, BoutiquesTool> bts = null;

    public DisplayTab(String tabIcon, String tabId, String tabName) {
        this.setTitle(Canvas.imgHTML(tabIcon) + " " + tabName.trim());
        this.setID(tabId);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        configure();
        boutiquesTool = new BoutiquesTool();
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

        globalLayout.addMember(hLayout2);
        IButton createApplicationButton;
        createApplicationButton = WidgetUtil.getIButton("Create application", Constants.ICON_LAUNCH, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boutiquesTool.setApplicationLFN(vipLayout.getApplicationLocation() + "/" + boutiquesTool.getName());
                if (!vipLayout.getApplicationType().contains("standalone")) {
                    createApplicationWithAddDesc();
                } else {
                    createApplication();
                }

            }
        });
        createApplicationButton.setWidth(120);
        globalLayout.addMember(createApplicationButton);
    }

    /**
     * Creates an application depending on other descriptors for the MICCAI
     * challenge. The results of application should be evaluated to different
     * metric methods.
     *
     */
    public void createApplicationWithAddDesc() {
        bts = new HashMap<String, BoutiquesTool>();
        // Fisrt callback to get mectric descriptor
        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to read JSON file :" + caught.getMessage());
            }

            @Override
            public void onSuccess(String jsonFileContent) {
                try {
                    bts.put("metric", JSONUtil.parseBoutiquesTool(JSONParser.parseStrict(jsonFileContent).isObject()));
                    bts.get("metric").setApplicationLFN(vipLayout.getApplicationLocation() + "/" + boutiquesTool.getName());

                    //second callback to get metadata descripotr
                    final AsyncCallback<String> callback2 = new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Layout.getInstance().setWarningMessage("Unable to read JSON file :" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(String jsonFileContent) {
                            try {
                                bts.put("adaptater", JSONUtil.parseBoutiquesTool(JSONParser.parseStrict(jsonFileContent).isObject()));
                                bts.get("adaptater").setApplicationLFN(vipLayout.getApplicationLocation() + "/" + boutiquesTool.getName());
                                //Finally, launch
                                createApplication();
                            } catch (ApplicationImporterException ex) {
                                Logger.getLogger(DisplayTab.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    };
                    ApplicationImporterService.Util.getInstance().readAndValidateBoutiquesFile(vipLayout.getDescriptorLocation() + "/" + Constants.APP_IMPORTER_CHALLENGE_METADATA,
                            callback2);
                } catch (ApplicationImporterException ex) {
                    Logger.getLogger(DisplayTab.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        ApplicationImporterService.Util.getInstance().readAndValidateBoutiquesFile(vipLayout.getDescriptorLocation() + "/" + Constants.APP_IMPORTER_CHALLENGE_METRIC, callback);
    }

    public static BoutiquesTool parseJSON(JSONObject jsonObject)
        throws ApplicationImporterException {

        BoutiquesTool boutiquesTool = JSONUtil.parseBoutiquesTool(jsonObject);
        verifyBoutiquesTool(boutiquesTool);
        return boutiquesTool;
    }

    /**
     * Populates the class with instance variables containing values in the JSON
     * object, and refreshes the display.
     */
    public void setBoutiqueTool(BoutiquesTool boutiquesTool) {
        this.boutiquesTool = boutiquesTool;
        this.setTitle(boutiquesTool.getName());
        generalLayout.setTool(boutiquesTool);
        inputsLayout.setInputs(boutiquesTool.getInputs());
        outputsLayout.setOutputFiles(boutiquesTool.getOutputFiles());
    }

    private static void verifyBoutiquesTool(BoutiquesTool boutiquesTool)
        throws ApplicationImporterException {

        if (boutiquesTool.getName() == null) {
            throw new ApplicationImporterException("Boutiques file must have a name property");
        }
        if (boutiquesTool.getName().matches(".*\\s.*")) {
            throw new ApplicationImporterException("Application name should not have a space in it");
        }
        if (boutiquesTool.getToolVersion() == null) {
            throw new ApplicationImporterException("Boutiques file must have a tool-version property");
        }
        if (boutiquesTool.getAuthor() == null) {
            throw new ApplicationImporterException("Boutiques file must have an author");
        }
    }

    /**
     * Creates a standalone application
     *
     */
    private void createApplication() {
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
        ApplicationImporterService.Util.getInstance().createApplication(
            boutiquesTool,
            vipLayout.getApplicationType(),
            vipLayout.getTag(),
            bts,
            vipLayout.getIsRunOnGrid(),
            vipLayout.getOverwrite(),
            false,
            callback);
    }
}
