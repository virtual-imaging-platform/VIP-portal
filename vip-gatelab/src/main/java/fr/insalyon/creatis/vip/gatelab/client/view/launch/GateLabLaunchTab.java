/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchTab;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchFormLayout;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabService;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabServiceAsync;

/**
 *
 * @author Sorina Camarasu, Rafael Ferreira da Silva
 */
public class GateLabLaunchTab extends AbstractLaunchTab {

    private LoadMacWindow loadMacWindow;
    private String baseDir;
    private IButton loadMacButton;

    public GateLabLaunchTab(String applicationName) {

        super(applicationName);
        layout.clear();
        initComplete(this);

        baseDir = DataManagerConstants.ROOT + "/Home/myGateSimus/inputs";

        loadData();
    }

    private void loadData() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Descriptor> callback = new AsyncCallback<Descriptor>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to download application source file:<br />" + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(Descriptor descriptor) {

                launchFormLayout = new LaunchFormLayout(applicationName, null, descriptor.getDescription());
                layout.addMember(launchFormLayout);

                launchFormLayout.setSourcesLayoutVisibible(false);

                for (Source source : descriptor.getSources()) {
                    launchFormLayout.addSource(new GateLabSourceLayout(source.getName(), source.getDescription()));
                }

                configureLoadMacButton();
                launchFormLayout.addButtons(5, loadMacButton);

                modal.hide();
                modal = launchFormLayout.getModal();

                configureInputsLayout(false);
            }
        };
        modal.show("Loading launch panel...", true);
        service.getApplicationDescriptor(applicationName, callback);
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

    public void uploadMacComplete(String inputTgz, String simuType, String nbPart) {

        if (loadMacWindow != null) {
            loadMacWindow.destroy();
            loadMacWindow = null;

            modal.hide();

            String[] it = inputTgz.split(" = ");
            setInputValue(it[0], baseDir.concat("/").concat(it[1]));
            //We do not fill in the parallelization type automaticlaly for the moment
            //String[] st = simuType.split(" = ");
            //setInputValue(st[0], st[1]);
            String[] np = nbPart.split(" = ");
            setInputValue(np[0], np[1]);

            String[] st = simuType.split(" = ");
            setInputValue(st[0], st[1]);

            loadMacButton.hide();
            launchFormLayout.setSourcesLayoutVisibible(true);
            
            configureLaunchButton();
            configureSaveInputsButton();
            
            launchFormLayout.addButtons(launchButton, saveInputsButton);
        }
    }

    public void close() {

        if (loadMacWindow != null) {
            loadMacWindow.destroy();
            loadMacWindow = null;

            modal.hide();
        }
    }

    public void report(String message) {

        close();
        GateLabServiceAsync service = GateLabService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                Layout.getInstance().setWarningMessage("Unable to report problem:<br />" + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                Layout.getInstance().setNoticeMessage("Problem successfully reported.", 10);
            }
        };
        modal.show("Reporting problem...", true);
        service.reportProblem(message.replaceAll("\\n", "<br />"), callback);
    }

    private native void initComplete(GateLabLaunchTab uploadMac) /*-{
     $wnd.uploadMacComplete = function (inputTgz,simuType,nbPart) {
     uploadMac.@fr.insalyon.creatis.vip.gatelab.client.view.launch.GateLabLaunchTab::uploadMacComplete(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(inputTgz,simuType,nbPart);
     };
     $wnd.close = function () {
     uploadMac.@fr.insalyon.creatis.vip.gatelab.client.view.launch.GateLabLaunchTab::close()();
     };
     $wnd.report = function (message) {
     uploadMac.@fr.insalyon.creatis.vip.gatelab.client.view.launch.GateLabLaunchTab::report(Ljava/lang/String;)(message);
     };
     }-*/;

    /**
     * Launches a simulation.
     */
    @Override
    protected void launch() {

        WidgetUtil.setLoadingIButton(launchButton, "Launching...");
        
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                resetLaunchButton();
                Layout.getInstance().setWarningMessage("Unable to launch the simulation:<br />" + caught.getMessage(), 10);
            }

            public void onSuccess(Void result) {
                resetLaunchButton();
                Layout.getInstance().setNoticeMessage("Simulation '<b>" + getSimulationName() + "</b>' successfully launched.", 10);
            }
        };
        service.launchSimulation(getParametersMap(), applicationName,
                getSimulationName(), callback);
    }
}
