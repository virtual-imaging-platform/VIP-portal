package fr.insalyon.creatis.vip.application.client.view.system.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesOutputFile;
import fr.insalyon.creatis.vip.application.client.rpc.*;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesParser;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.*;

public class MakeExecutionPublicTab extends Tab {
    private VLayout makeExecutionPublicLayout;
    private TextItem idOfTheExecutionField;
    private TextItem nameOfTheExecutionSimulationField;
    private TextItem nameOfTheExecutionApplicationField;
    private TextItem versionExecutionField;
    private TextItem statusExecutionField;
    private TextItem authorNameField;
    private TextAreaItem commentsItem;
    private IButton makeExecutionblicButton;
    private DynamicForm outputFilesForm;
    private ReproVipServiceAsync reproVipServiceAsync = ReproVipService.Util.getInstance();
    public MakeExecutionPublicTab(Execution execution) {

        this.setID(CoreConstants.TAB_MAKE_EXECUTION_PUBLIC);
        this.setTitle("Make execution public");
        this.setCanClose(true);

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        outputFilesForm = new DynamicForm();
        Layout.getInstance().getModal().show("Loading...", true);
        AsyncCallback<String> descriptorCallback = new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().getModal().hide();
                SC.warn("Error retrieving application descriptor: " + caught.getMessage());
            }
            @Override
            public void onSuccess(String descriptorString) {
                try {
                    BoutiquesApplication applicationTool = new BoutiquesParser().parseApplication(descriptorString);
                    if(applicationTool.getOutputFiles() == null || applicationTool.getOutputFiles().isEmpty()) {
                        SC.warn("Output File is empty");
                    } else {
                        Set<BoutiquesOutputFile> outputFiles = applicationTool.getOutputFiles();
                        if (outputFiles != null && !outputFiles.isEmpty()) {
                            CheckboxItem[] checkboxes = new CheckboxItem[outputFiles.size()];
                            int index = 0;
                            for (BoutiquesOutputFile outputFile : outputFiles) {
                                CheckboxItem checkbox = new CheckboxItem(outputFile.getName(), outputFile.getName());
                                checkboxes[index] = checkbox;
                                index++;
                            }
                            outputFilesForm.setFields(checkboxes);
                        }
                    }
                } catch (InvalidBoutiquesDescriptorException exception) {
                    SC.warn("Error when parsing application descriptor: " + exception.getMessage());
                } finally {
                    Layout.getInstance().getModal().hide();
                }
            }
        };

        WorkflowService.Util.getInstance().getApplicationDescriptorString(execution.getApplicationName(), execution.getVersion(), descriptorCallback);

        configureExecutionPublicLayout();
        idOfTheExecutionField.setValue(execution.getId());
        nameOfTheExecutionSimulationField.setValue(execution.getSimulationName());
        nameOfTheExecutionApplicationField.setValue(execution.getApplicationName());
        versionExecutionField.setValue(execution.getVersion());
        statusExecutionField.setValue(execution.getStatus());
        authorNameField.setValue(execution.getAuthor());
        commentsItem.setValue(execution.getComments());
        vLayout.addMember(makeExecutionPublicLayout);

        this.setPane(vLayout);
    }

    private void configureExecutionPublicLayout() {

        idOfTheExecutionField = FieldUtil.getTextItem(300, false, "", null);
        idOfTheExecutionField.setCanEdit(false);

        nameOfTheExecutionSimulationField = FieldUtil.getTextItem(300, false, "", null);

        nameOfTheExecutionApplicationField = FieldUtil.getTextItem(300, false, "", null);
        nameOfTheExecutionApplicationField.setCanEdit(false);

        authorNameField = FieldUtil.getTextItem(300, false, "", null);

        versionExecutionField= FieldUtil.getTextItem(300, false, "", null);
        versionExecutionField.setCanEdit(false);

        statusExecutionField = FieldUtil.getTextItem(300, false, "", null);
        statusExecutionField.setCanEdit(false);

        commentsItem = new TextAreaItem("comment", "");
        commentsItem.setHeight(80);
        commentsItem.setWidth(300);
        commentsItem.setShowTitle(false);

        outputFilesForm = new DynamicForm();

        makeExecutionblicButton = new IButton("Make execution public");

        makeExecutionblicButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                String id = idOfTheExecutionField.getValueAsString();
                String nameSimulation = nameOfTheExecutionSimulationField.getValueAsString();
                String nameApplication = nameOfTheExecutionApplicationField.getValueAsString();
                String version = versionExecutionField.getValueAsString();
                String status = statusExecutionField.getValueAsString();
                String author = authorNameField.getValueAsString();
                String comments = commentsItem.getValueAsString();

                Execution newExecution = new Execution(id, nameSimulation, nameApplication, version, status, author, comments);
                reproVipServiceAsync.addExecution(newExecution, new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                        SC.warn("Failed to add execution: " + caught.getMessage());
                    }
                    public void onSuccess(Void result) {
                        sendExecutionAdminEmail(newExecution);
                        SC.say("Execution added successfully!");
                    }
                });
            }
        });

        Label descriptionLabel = new Label("Choose your output file(s):");
        descriptionLabel.setHeight(20);
        descriptionLabel.setWidth100();

        makeExecutionPublicLayout = WidgetUtil.getVIPLayout(320);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "ID of the execution", idOfTheExecutionField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Name of the execution simulation", nameOfTheExecutionSimulationField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Name of the execution application", nameOfTheExecutionApplicationField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Version of the execution", versionExecutionField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Status of the execution", statusExecutionField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Author name", authorNameField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Comments", commentsItem);
        makeExecutionPublicLayout.addMember(descriptionLabel);
        makeExecutionPublicLayout.addMember(outputFilesForm);
        makeExecutionPublicLayout.addMember(makeExecutionblicButton);
    }

    private void sendExecutionAdminEmail(Execution execution) {
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to send email to admins:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                Layout.getInstance().setNoticeMessage("Email has been sent to admins.");
            }
        };
        reproVipServiceAsync.executionAdminEmail(execution, callback);
    }
}
