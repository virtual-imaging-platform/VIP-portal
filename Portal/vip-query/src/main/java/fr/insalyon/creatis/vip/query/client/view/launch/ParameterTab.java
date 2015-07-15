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
package fr.insalyon.creatis.vip.query.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;

import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;

import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import fr.insalyon.creatis.vip.query.client.view.monitor.QueryHistoryTab;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class ParameterTab extends AbstractFormLayout {

    private Long queryVersionID;
    private VLayout mainLayout;
    private IButton launchButton;
    private TextItem executionName;
    private Label description;
    private Label titleDes;
    private Label queryCreateur;
    private Label queryCreateurValue;
    private Label parameterLab;
    DynamicForm execution;
    private List<TextItem> arrList;
    private Long queryExecutionID;
    QueryHistoryTab tab;
    private boolean descriptionE;
    private boolean parametersE = false;

    public ParameterTab(Long queryVersionID) {
        super();
        this.addTitle("Query Execution", QueryConstants.ICON_EXECUTE_VERSION);
        this.queryVersionID = queryVersionID;


        mainLayout = new VLayout();
        mainLayout.setMembersMargin(10);
        titleDes = new Label("<strong>Query description</strong>");
        titleDes.setHeight(20);
        Label title = new Label("<strong>Execution Name</strong>");
        title.setHeight(20);
        getDescriptionQueryMaker(queryVersionID);

        executionName = new TextItem();
        executionName.setShowTitle(false);
        executionName.setTitleOrientation(TitleOrientation.TOP);
        executionName.setWidth("*");
        executionName.setKeyPressFilter("^([0-9.,A-Za-z-+/_() ])+$");
        executionName.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                event.getItem().validate();
            }
        });
        executionName.setValidators(ValidatorUtil.getStringValidator());







        execution = new DynamicForm();
        execution.setNumCols(1);
        execution.setFields(executionName);

        mainLayout.setPadding(5);
        if (descriptionE == true) {
            mainLayout.addMember(title, 2);
            mainLayout.addMember(execution, 3);
        } else {
            mainLayout.addMember(title, 0);
            mainLayout.addMember(execution, 1);
        }


        parameterLab = new Label("<strong>Parameter</strong>");
        parameterLab.setHeight(20);
        loadParameter();
        configure();
        this.addMember(mainLayout);
    }

    private void loadParameter() {

        final AsyncCallback<List<Parameter>> callback;
        callback = new AsyncCallback<List<Parameter>>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to Load parameter" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Parameter> result) {
                if (!result.isEmpty()) {
                    if (descriptionE == true) {
                        mainLayout.addMember(parameterLab, 4);
                    } else {
                        mainLayout.addMember(parameterLab, 2);
                    }

                    arrList = new ArrayList<TextItem>();
                    DynamicForm dynamicForm;

                    for (Parameter q : result) {
                        TextItem value;
                        dynamicForm = new DynamicForm();
                        dynamicForm.setWidth100();
                        dynamicForm.setNumCols(1);
                        dynamicForm.setPadding(5);
                        dynamicForm.setMargin(5);

                        HLayout hlayout = new HLayout();
                        hlayout.setBorder("1px solid #C0C0C0");
                        hlayout.setBackgroundColor("#F5F5F5");
                        hlayout.setPadding(5);
                        hlayout.setMembersMargin(5);
                        value = new TextItem();
                        value.setWidth("100%");
                        value.setTitle(q.getName());
                        value.setKeyPressFilter("[0-9.,A-Za-z-+/_() ]");
                        value.addChangedHandler(new ChangedHandler() {
                            @Override
                            public void onChanged(ChangedEvent event) {
                                event.getItem().validate();
                            }
                        });
                        value.setTitleOrientation(TitleOrientation.TOP);
                        value.setName(String.valueOf(q.getParameterID()));

                        value.setPrompt("<b> Description: </b>" + q.getDescription() + "<br><b> Type: </b>" + q.getType() + "<br><b> Example: </b>" + q.getExample());
                        arrList.add(value);
                        dynamicForm.setFields(value);
                        hlayout.addMember(dynamicForm);
                        if (descriptionE == true) {
                            mainLayout.addMember(hlayout, 5);
                            parametersE = true;
                        } else {
                            mainLayout.addMember(hlayout, 3);
                            parametersE = true;
                        }
                    }

                }
            }
        };
        QueryService.Util.getInstance().getParameter(queryVersionID, callback);
    }

    private void configure() {

        launchButton = new IButton();
        launchButton = WidgetUtil.getIButton("Execute", QueryConstants.ICON_LAUNCH,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("Unable to save Query Execution " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Long result) {
                        if (parametersE == false) {
                            getBody(queryVersionID, result, false);
                            queryExecutionID = result;
                        } else {
                            for (TextItem t : arrList) {

                                saveValue(new Value(t.getValueAsString(), Long.parseLong(t.getName()), result));
                            }
                            queryExecutionID = result;

                        }

                        tab = new QueryHistoryTab();
                        Layout.getInstance().addTab(tab);
                    }
                };
                if (executionName.validate()) {
                    QueryService.Util.getInstance().addQueryExecution(new QueryExecution(queryVersionID, "waiting", executionName.getValueAsString(), " "), callback);
                } else {
                    Layout.getInstance().setWarningMessage("Invalid Query Execution");
                }
            }
        });


        mainLayout.addMember(launchButton, 7);
        queryCreateur = new Label("<strong>Query Author</strong>");
        queryCreateur.setHeight(20);
        queryCreateurValue = new Label("");
        queryCreateurValue.setHeight(20);
        mainLayout.addMember(queryCreateur);
        mainLayout.addMember(queryCreateurValue);

    }

    private void saveValue(Value value) {
        final AsyncCallback<Long> callback = new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to save Value " + caught.getMessage());
            }

            @Override
            public void onSuccess(Long result) {
                getBody(queryVersionID, queryExecutionID, true);
            }
        };
        QueryService.Util.getInstance().addValue(value, callback);

    }

    private void getBody(Long queryVersionID, Long executonID, boolean parameter) {
        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to get Body" + caught.getMessage());
                update("", "failed", queryExecutionID);
            }

            @Override
            public void onSuccess(String result) {

                //getUrlResult(result, "csv");
                update(result, "waiting", queryExecutionID);

            }
        };

        QueryService.Util.getInstance().getBody(queryVersionID, executonID, parameter, callback);


    }

    private void update(String bodyResult, String status, Long executonID) {
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to update" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
            }
        };

        QueryService.Util.getInstance().updateQueryExecution(bodyResult, status, executonID, callback);


    }

    /*
     private void getUrlResult(String query, String format) {

     final AsyncCallback<String> callback = new AsyncCallback<String>() {
     @Override
     public void onFailure(Throwable caught) {

     Layout.getInstance().setWarningMessage("Unable to get result" + caught.getMessage());
     update("", "failed", queryExecutionID);
     tab.loadData();
     }

     @Override
     public void onSuccess(String result) {


     update(result, "completed", queryExecutionID);
     Layout.getInstance().setNoticeMessage("The query was successfully executed");


     tab.loadData();


     }
     };

     EndPointSparqlService.Util.getInstance().getUrlResult(query, format, callback);



     }
     * */
    private void getDescriptionQueryMaker(Long queryVersionID) {
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Unable to get description of query" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {



                if (result.get(1).length() == 0 || result.get(1).isEmpty() || result.get(1) == "" || result.get(1).equals(null) || result.get(1) == "<br>" || result.get(1) == " ") {
                    descriptionE = false;
                } else {
                    descriptionE = true;
                    mainLayout.addMember(titleDes, 0);
                    description = new Label(result.get(1));
                    description.setHeight(20);
                    mainLayout.addMember(description, 1);
                }

                queryCreateurValue.setContents(result.get(0));


            }
        };

        QueryService.Util.getInstance().getDescriptionQueryMaker(queryVersionID, callback);


    }
}
