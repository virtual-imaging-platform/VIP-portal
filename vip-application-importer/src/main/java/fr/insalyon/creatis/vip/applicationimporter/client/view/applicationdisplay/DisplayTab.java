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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesParser;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
import fr.insalyon.creatis.vip.applicationimporter.client.ApplicationImporterException;
import fr.insalyon.creatis.vip.applicationimporter.client.rpc.ApplicationImporterService;
import fr.insalyon.creatis.vip.applicationimporter.client.view.Constants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

public class DisplayTab extends Tab {

    // Layouts
    private VLayout globalLayout;
    private GeneralLayout generalLayout;
    private InputLayout inputsLayout;
    private OutputLayout outputsLayout;
    private VIPLayout vipLayout;
    private final ModalWindow modal;
    private BoutiquesApplication boutiquesTool;

    public DisplayTab(String tabIcon, String tabId, String tabName) {
        this.setTitle(Canvas.imgHTML(tabIcon) + " " + tabName.trim());
        this.setID(tabId);
        this.setCanClose(true);
        this.setAttribute("paneMargin", 0);
        configure();
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
                createApplication();
            }
        });
        createApplicationButton.setWidth(120);
        globalLayout.addMember(createApplicationButton);
    }

    public static BoutiquesApplication parseJSON(String jsonDescriptor)
        throws ApplicationImporterException {

        BoutiquesApplication boutiquesApplication = null;
        try {
            boutiquesApplication = new BoutiquesParser().parseApplication(jsonDescriptor);
            verifyBoutiquesTool(boutiquesApplication);
        } catch (InvalidBoutiquesDescriptorException exception) {
            Layout.getInstance().setWarningMessage("Unable to parse application descriptor:<br />"
                    + exception.getMessage(), 10);
            throw new ApplicationImporterException(exception.getMessage(), exception);
        }
        return boutiquesApplication;
    }

    /**
     * Populates the class with instance variables containing values in the JSON
     * object, and refreshes the display.
     * @param boutiquesTool BoutiquesApplication
     */
    public void setBoutiqueTool(BoutiquesApplication boutiquesTool) {
        this.boutiquesTool = boutiquesTool;
        this.setTitle(boutiquesTool.getName());
        generalLayout.setTool(boutiquesTool);
        inputsLayout.setInputs(boutiquesTool.getInputs());
        outputsLayout.setOutputFiles(boutiquesTool.getOutputFiles());
    }

    private static void verifyBoutiquesTool(BoutiquesApplication boutiquesTool)
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
            vipLayout.getIsRunOnGrid(),
            vipLayout.getOverwrite(),
            callback);
    }
}
