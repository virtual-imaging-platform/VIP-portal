/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.common.Context;
import fr.insalyon.creatis.vip.portal.client.view.common.FieldUtil;
import fr.insalyon.creatis.vip.portal.client.view.layout.AbstractLeftPanel;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class MonitorLeftPanel extends AbstractLeftPanel {

    private static MonitorLeftPanel instance;
    private Store usersStore;
    private Store appsStore;
    private Store statusStore;
    private DateField startDate;
    private DateField endDate;
    private ComboBox appCB;
    private ComboBox userCB;
    private ComboBox statusCB;
    private String user;
    private String currentUser;

    public static MonitorLeftPanel getInstance() {
        if (instance == null) {
            instance = new MonitorLeftPanel();
        }
        return instance;
    }

    private MonitorLeftPanel() {
        super(null);
        collapsed = false;
        currentUser = Context.getInstance().getAuthentication().getUserName().split(" / ")[0];
    }

    public void setApplicationClass(String applicationClass) {
        this.title = applicationClass;
    }

    @Override
    public Panel getPanel() {

        loadCombosData();

        if (isAuthorized()) {
            FormPanel formPanel = new FormPanel();
            formPanel.setTitle(title + " Filter");
            formPanel.setBorder(false);
            formPanel.setLayout(new FormLayout());
            formPanel.setLabelAlign(Position.TOP);
            formPanel.setMargins(0, 0, 0, 0);
            formPanel.setPaddings(10, 5, 5, 5);
            formPanel.setHeight(300);

            // Users
            usersStore = FieldUtil.getComboBoxStore("workflow-filter-username");
            userCB = FieldUtil.getComboBox("workflow-filter-user", "User", 180,
                    "Select User", usersStore, "workflow-filter-username");
            if (isAdmin()) {
                formPanel.add(userCB, new AnchorLayoutData("95%"));
            } else {
                user = currentUser;
            }

            // Applications
            appsStore = FieldUtil.getComboBoxStore("workflow-filter-appname");
            appCB = FieldUtil.getComboBox("workflow-filter-app", title, 180,
                    "Select Application", appsStore, "workflow-filter-appname");
            formPanel.add(appCB, new AnchorLayoutData("95%"));

            // Status
            statusStore = FieldUtil.getComboBoxStore("workflow-filter-status");
            statusCB = FieldUtil.getComboBox("workflow-filter-stat", "Status", 180,
                    "Select Status", statusStore, "workflow-filter-status");
            formPanel.add(statusCB, new AnchorLayoutData("95%"));

            // Start Date
            startDate = FieldUtil.getDateField("workflow-filter-startDate", 180, "Start Date");
            formPanel.add(startDate);

            // End Date
            endDate = FieldUtil.getDateField("workflow-filter-endDate", 180, "End Date");
            formPanel.add(endDate);

            Button submit = new Button("Submit", new ButtonListenerAdapter() {

                @Override
                public void onClick(Button button, EventObject e) {
                    if (endDate.getValue() != null && startDate.getValue() != null) {
                        if (endDate.getValue().before(startDate.getValue())) {
                            MessageBox.alert("Error", "End date should be after or equal to start date.");
                            return;
                        }
                    }
                    if (userCB.getValue() != null && !userCB.getValueAsString().equals("All") && !userCB.getValueAsString().equals("")) {
                        user = userCB.getValue();
                    } else {
                        user = null;
                    }
                    String app = null;
                    if (appCB.getValue() != null && !appCB.getValueAsString().equals("All") && !appCB.getValueAsString().equals("")) {
                        app = appCB.getValueAsString();
                    }
                    String status = null;
                    if (statusCB.getValue() != null && !statusCB.getValueAsString().equals("All") && !statusCB.getValueAsString().equals("")) {
                        status = statusCB.getValueAsString();
                    }
                    Date sDate;
                    if (startDate.getValue() != null && !startDate.getValueAsString().equals("")) {
                        sDate = startDate.getValue();
                    } else {
                        sDate = null;
                    }
                    Date eDate;
                    if (endDate.getValue() != null && !endDate.getValueAsString().equals("")) {
                        eDate = endDate.getValue();
                    } else {
                        eDate = null;
                    }

                    WorkflowsPanel wp = (WorkflowsPanel) Layout.getInstance().getCenterPanelTab("app-workflows-panel");
                    wp.loadWorkflowData(user, app, status, sDate, eDate);
                    Ext.get("app-workflows-grid").mask("Loading data...");
                }
            });
            Button reset = new Button("Reset", new ButtonListenerAdapter() {

                @Override
                public void onClick(Button button, EventObject e) {
                    userCB.clearValue();
                    appCB.clearValue();
                    statusCB.clearValue();
                    startDate.setValue("");
                    endDate.setValue("");
                }
            });

            formPanel.addButton(submit);
            formPanel.addButton(reset);
            return formPanel;
        }

        Panel panel = new Panel();
        panel.setHtml("<p style=\"font-size: 10px\"><strong>Authorization Error</strong></p>");
        return panel;
    }

    private void loadCombosData() {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>[]> callback = new AsyncCallback<List<String>[]>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get users and applications lists\n" + caught.getMessage());
            }

            public void onSuccess(List<String>[] result) {
                Object[][] usersData = new Object[result[0].size() + 1][1];
                usersData[0][0] = "All";
                for (int i = 1; i < result[0].size() + 1; i++) {
                    usersData[i][0] = result[0].get(i - 1);
                }

                MemoryProxy usersProxy = new MemoryProxy(usersData);
                usersStore.setDataProxy(usersProxy);
                usersStore.load();
                usersStore.commitChanges();

                Object[][] appsData = new Object[result[1].size() + 1][1];
                appsData[0][0] = "All";
                for (int i = 1; i < result[1].size() + 1; i++) {
                    appsData[i][0] = result[1].get(i - 1);
                }
                MemoryProxy appsProxy = new MemoryProxy(appsData);
                appsStore.setDataProxy(appsProxy);
                appsStore.load();
                appsStore.commitChanges();

                Object[][] statusData = new Object[][]{
                    new Object[]{"All"},
                    new Object[]{"Completed"},
                    new Object[]{"Killed"},
                    new Object[]{"Running"}
                };
                MemoryProxy statusProxy = new MemoryProxy(statusData);
                statusStore.setDataProxy(statusProxy);
                statusStore.load();
                statusStore.commitChanges();
            }
        };
        service.getApplicationsAndUsersList(title, callback);
    }
}
