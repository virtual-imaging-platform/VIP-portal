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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SearchStackSection extends SectionStackSection {

    private DynamicForm form;
    private SelectItem userItem;
    private SelectItem simulationItem;
    private SelectItem statusItem;
    private SelectItem appClassItem;
    private DateItem startDateItem;
    private DateItem endDateItem;
    private IButton searchButton;
    private IButton resetButton;

    public SearchStackSection() {

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_SEARCH) + " Search");
        this.setExpanded(false);

        configureForm();

        HLayout hLayout = new HLayout(5);
        hLayout.setMargin(5);
        hLayout.addMember(searchButton);
        hLayout.addMember(resetButton);

        VLayout vLayout = new VLayout(5);
        vLayout.addMember(form);
        vLayout.addMember(hLayout);
        this.addItem(vLayout);

        loadData();
    }

    private void configureForm() {

        form = new DynamicForm();
        form.setMargin(5);
        form.setWidth(500);
        form.setNumCols(4);

        userItem = new SelectItem("userFilter", "User");
        simulationItem = new SelectItem("simualtionFilter", "Application");
        statusItem = new SelectItem("statusFilter", "Status");
        appClassItem = new SelectItem("classFilter", "Class");

        startDateItem = new DateItem("startDateFilter", "Start Date");
        startDateItem.setUseTextField(true);

        endDateItem = new DateItem("endDateFilter", "End Date");
        endDateItem.setUseTextField(true);

        searchButton = WidgetUtil.getIButton("Search", null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                SimulationsTab simulationsTab = (SimulationsTab) Layout.getInstance().getTab(ApplicationConstants.TAB_MONITOR);

                String userText = userItem.getValueAsString();
                simulationsTab.setUser(userText == null || userText.isEmpty() || userText.equals("All") ? null : userText);

                String simuText = simulationItem.getValueAsString();
                simulationsTab.setApp(simuText == null || simuText.isEmpty() || simuText.equals("All") ? null : simuText);

                String statusText = statusItem.getValueAsString();
                simulationsTab.setStatus(statusText == null || statusText.isEmpty() || statusText.equals("All") ? null : statusText);
                
                String appClassText = appClassItem.getValueAsString();
                simulationsTab.setAppClass(appClassText == null || appClassText.isEmpty() || appClassText.equals("All") ? null : appClassText);

                Date startDateValue = startDateItem.getValueAsDate();
                simulationsTab.setStartDate(startDateValue == null ? null : startDateValue);

                Date endDateValue = endDateItem.getValueAsDate();
                simulationsTab.setEndDate(endDateValue == null ? null : endDateValue);

                simulationsTab.loadData();
            }
        });

        resetButton = WidgetUtil.getIButton("Reset", null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                userItem.setValue("All");
                simulationItem.setValue("All");
                statusItem.setValue("All");
                appClassItem.setValue("All");
                startDateItem.setValue("");
                endDateItem.setValue("");
            }
        });

        form.setFields(userItem, startDateItem, simulationItem,
                endDateItem, statusItem, appClassItem);
    }

    private void loadData() {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<String>[]> callback = new AsyncCallback<List<String>[]>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(searchButton, "Search", null);
                Layout.getInstance().setWarningMessage("Unable to get users and applications lists:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String>[] result) {
                WidgetUtil.resetIButton(searchButton, "Search", null);
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
                statusMap.put(SimulationStatus.Completed.name(), SimulationStatus.Completed.name());
                statusMap.put(SimulationStatus.Running.name(), SimulationStatus.Running.name());
                statusMap.put(SimulationStatus.Killed.name(), SimulationStatus.Killed.name());
                statusMap.put(SimulationStatus.Failed.name(), SimulationStatus.Failed.name());
                if (CoreModule.user.isSystemAdministrator()) {
                    statusMap.put(SimulationStatus.Cleaned.name(), SimulationStatus.Cleaned.name());
                }
                statusItem.setValueMap(statusMap);
                statusItem.setValue("All");
                
                LinkedHashMap<String, String> appClassMap = new LinkedHashMap<String, String>();
                appClassMap.put("All", "All");
                for (String s : result[2]) {
                    appClassMap.put(s, s);
                }
                appClassItem.setValueMap(appClassMap);
                appClassItem.setValue("All");

            }
        };
        WidgetUtil.setLoadingIButton(searchButton, "Searching...");
        service.getApplicationsAndUsers(callback);
    }
}
