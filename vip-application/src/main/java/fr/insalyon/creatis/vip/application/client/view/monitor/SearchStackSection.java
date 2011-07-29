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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class SearchStackSection extends SectionStackSection {

    private String tabID;
    private boolean groupAdmin;
    private DynamicForm form;
    private SelectItem userItem;
    private SelectItem simulationItem;
    private SelectItem statusItem;
    private DateItem startDateItem;
    private DateItem endDateItem;
    private IButton submitButton;
    private IButton resetButton;

    public SearchStackSection(String tabID, boolean groupAdmin) {

        this.tabID = tabID;
        this.groupAdmin = groupAdmin;
        
        this.setTitle("Search");
        this.setExpanded(false);

        configureForm();
        
        HLayout hLayout = new HLayout(5);
        hLayout.addMember(submitButton);
        hLayout.addMember(resetButton);
        
        VLayout vLayout = new VLayout(5);
        vLayout.addMember(form);
        vLayout.addMember(hLayout);
        this.addItem(vLayout);

        loadData();
    }

    private void configureForm() {

        form = new DynamicForm();
        form.setWidth(500);
        form.setNumCols(4);

        userItem = new SelectItem("userFilter", "User");
        simulationItem = new SelectItem("simualtionFilter", "Simulation");
        statusItem = new SelectItem("statusFilter", "Status");

        startDateItem = new DateItem("startDateFilter", "Start Date");
        startDateItem.setUseTextField(true);

        endDateItem = new DateItem("endDateFilter", "End Date");
        endDateItem.setUseTextField(true);

        submitButton = new IButton("Submit");
        submitButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab(tabID);
                
                if (Context.getInstance().isSystemAdmin() || groupAdmin) {
                    String userText = userItem.getValueAsString();
                    simulationsTab.setUser(userText == null || userText.isEmpty() || userText.equals("All") ? null : userText);
                }

                if (!simulationItem.isDisabled()) {
                    String simuText = simulationItem.getValueAsString();
                    simulationsTab.setApp(simuText == null || simuText.isEmpty() || simuText.equals("All") ? null : simuText);
                }

                String statusText = statusItem.getValueAsString();
                simulationsTab.setStatus(statusText == null || statusText.isEmpty() || statusText.equals("All") ? null : statusText);

                Date startDateValue = startDateItem.getValueAsDate();
                simulationsTab.setStartDate(startDateValue == null ? null : startDateValue);

                Date endDateValue = endDateItem.getValueAsDate();
                simulationsTab.setEndDate(endDateValue == null ? null : endDateValue);

                simulationsTab.loadData();
            }
        });

        resetButton = new IButton("Reset");
        resetButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                userItem.setValue("All");
                simulationItem.setValue("All");
                statusItem.setValue("All");
                startDateItem.setValue("");
                endDateItem.setValue("");
            }
        });

        if (Context.getInstance().isSystemAdmin() || groupAdmin) {
            form.setFields(userItem, startDateItem, simulationItem,
                    endDateItem, statusItem);
        } else {
            form.setFields(simulationItem, startDateItem,
                    statusItem, endDateItem);
        }
    }

    private void loadData() {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>[]> callback = new AsyncCallback<List<String>[]>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get users and applications lists\n" + caught.getMessage());
            }

            public void onSuccess(List<String>[] result) {
                LinkedHashMap<String, String> usersMap = new LinkedHashMap<String, String>();
                usersMap.put("All", "All");
                for (String s : result[0]) {
                    usersMap.put(s, s);
                }
                userItem.setValueMap(usersMap);
                userItem.setValue("All");

                LinkedHashMap<String, String> simulationMap = new LinkedHashMap<String, String>();
                simulationMap.put("All", "All");
                for (String s : result[1]) {
                    simulationMap.put(s, s);
                }
                simulationItem.setValueMap(simulationMap);
                simulationItem.setValue("All");

                LinkedHashMap<String, String> statusMap = new LinkedHashMap<String, String>();
                statusMap.put("All", "All");
                statusMap.put("Completed", "Completed");
                statusMap.put("Running", "Running");
                statusMap.put("Killed", "Killed");
                if (Context.getInstance().isSystemAdmin()) {
                    statusMap.put("Cleaned", "Cleaned");
                }
                statusItem.setValueMap(statusMap);
                statusItem.setValue("All");
            }
        };
        service.getApplicationsAndUsersList(null, callback);
    }
}
