
package fr.insalyon.creatis.vip.query.client.view.monitor;

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


import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author boujelben
 */
public class SearchStackSection extends SectionStackSection {

    private DynamicForm form;
    private SelectItem userItem;
    
    private SelectItem statusItem;
   
    private DateItem startDateItem;
    private DateItem endDateItem;
    private IButton searchButton;
    private IButton resetButton;

    public SearchStackSection() {

        this.setTitle(Canvas.imgHTML(QueryConstants.ICON_SEARCH) + " Search");
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

        //loadData();
    }

    private void configureForm() {

        form = new DynamicForm();
        form.setMargin(5);
        form.setWidth(500);
        form.setNumCols(4);

        userItem = new SelectItem("userFilter", "User");
       
        statusItem = new SelectItem("statusFilter", "Status");
      

        startDateItem = new DateItem("startDateFilter", "Start Date");
        startDateItem.setUseTextField(true);

        endDateItem = new DateItem("endDateFilter", "End Date");
        endDateItem.setUseTextField(true);

        searchButton = WidgetUtil.getIButton("Search", null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                QueryHistoryTab simulationsTab = (QueryHistoryTab) Layout.getInstance().getTab(QueryConstants.TAB_QUERYHISTORY);

                String userText = userItem.getValueAsString();
                simulationsTab.setUser(userText == null || userText.isEmpty() || userText.equals("All") ? null : userText);

               
                String statusText = statusItem.getValueAsString();
                simulationsTab.setStatus(statusText == null || statusText.isEmpty() || statusText.equals("All") ? null : statusText);
                
               
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
                
                statusItem.setValue("All");
          
                startDateItem.setValue("");
                endDateItem.setValue("");
            }
        });

        form.setFields(userItem, startDateItem,
                endDateItem, statusItem);
    }
/*
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
               // statusMap.put(SimulationStatus.Completed.name(), SimulationStatus.Completed.name());
               // statusMap.put(SimulationStatus.Running.name(), SimulationStatus.Running.name());
              //  statusMap.put(SimulationStatus.Killed.name(), SimulationStatus.Killed.name());
               // if (CoreModule.user.isSystemAdministrator()) {
                  //  statusMap.put(SimulationStatus.Cleaned.name(), SimulationStatus.Cleaned.name());
               // }
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
        service.getApplicationsAndUsers(ApplicationModule.reservedClasses, callback);
    }
    * */
}
