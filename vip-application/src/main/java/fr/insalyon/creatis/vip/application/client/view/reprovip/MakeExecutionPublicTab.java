package fr.insalyon.creatis.vip.application.client.view.reprovip;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesOutputFile;
import fr.insalyon.creatis.vip.application.client.rpc.*;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesParser;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.*;

public class MakeExecutionPublicTab extends Tab {

    private PublicExecution publicExecution;

    private VLayout makeExecutionPublicLayout;
    private TextItem nameOfTheExecutionSimulationField;
    private TextItem authorNameField;
    private TextAreaItem commentsItem;
    private DynamicForm outputFilesForm;

    private ReproVipServiceAsync reproVipServiceAsync = ReproVipService.Util.getInstance();

    public MakeExecutionPublicTab(PublicExecution publicExecution) {

        this.setID(ApplicationConstants.TAB_MAKE_EXECUTION_PUBLIC);
        this.setTitle("Make execution public");
        this.setCanClose(true);

        this.publicExecution = publicExecution;

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        Layout.getInstance().getModal().show("Loading...", true);

        configureExecutionPublicLayout();

        nameOfTheExecutionSimulationField.setValue(publicExecution.getSimulationName());
        authorNameField.setValue(publicExecution.getAuthor());
        commentsItem.setValue(publicExecution.getComments());

        vLayout.addMember(makeExecutionPublicLayout);

        this.setPane(vLayout);
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
                        SC.warn("This application does not have any output");
                    } else {
                        List<CheckboxItem> checkboxes = new ArrayList<>();
                        for (BoutiquesOutputFile outputFile : applicationTool.getOutputFiles()) {
                            CheckboxItem checkbox = new CheckboxItem(outputFile.getName(), outputFile.getName());
                            checkboxes.add(checkbox);
                        }
                        outputFilesForm.setFields(checkboxes.toArray(new CheckboxItem[0]));
                    }
                } catch (InvalidBoutiquesDescriptorException exception) {
                    SC.warn("Error when parsing application descriptor: " + exception.getMessage());
                } finally {
                    Layout.getInstance().getModal().hide();
                }
            }
        };
        WorkflowService.Util.getInstance().getApplicationDescriptorString(
                publicExecution.getApplicationName(), publicExecution.getApplicationVersion(), descriptorCallback);
    }

    private void configureExecutionPublicLayout() {

        nameOfTheExecutionSimulationField = FieldUtil.getTextItem(300, false, "", null);
        authorNameField = FieldUtil.getTextItem(300, false, "", null);
        commentsItem = new TextAreaItem("comment", "");
        commentsItem.setHeight(80);
        commentsItem.setWidth(300);
        commentsItem.setShowTitle(false);

        outputFilesForm = new DynamicForm();

        IButton submitButton = new IButton("Make execution public");

        submitButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                String nameSimulation = nameOfTheExecutionSimulationField.getValueAsString();
                String author = authorNameField.getValueAsString();
                String comments = commentsItem.getValueAsString();

                PublicExecution newPublicExecution =
                        new PublicExecution(id, nameSimulation,
                                publicExecution.getApplicationName(), publicExecution.getApplicationVersion(),
                                PublicExecution.PublicExecutionStatus.REQUESTED, author, comments);

                reproVipServiceAsync.addPublicExecution(newPublicExecution, new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                        SC.warn("Failed to add execution: " + caught.getMessage());
                    }
                    public void onSuccess(Void result) {
                        SC.say("Execution added successfully!");
                    }
                });
            }
        });

        Label descriptionLabel = new Label("Choose your output file(s):");
        descriptionLabel.setHeight(20);
        descriptionLabel.setWidth100();

        makeExecutionPublicLayout = WidgetUtil.getVIPLayout(600);
        makeExecutionPublicLayout.addMember(
        WidgetUtil.getLabel("<b>Subject to validation by VIP administrators, this functionality allows to push " +
                "the results and execution traces of the concerned VIP workflow on <a href=\"https://zenodo.org/\">Zenodo</a>." +
                " A DOI is retrieved in exchange, allowing to easily identify and share your results with the community." +
                "<br/>" +
                "Please make sure you are allowed to share the output data publicly and do not hesitate to contact " +
                "<a href=\"mailto:vip-support@creatis.insa-lyon.fr\">vip-support@cretais.insa-lyon.fr</a> if you have any questions.Please list here the references " +
                "of the publications that you made using VIP. These references " +
                "may be used by the VIP team to justify the use of computing " +
                "and storage resources. <br/></b>", 20));
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Name of the execution simulation", nameOfTheExecutionSimulationField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Author name", authorNameField);
        WidgetUtil.addFieldToVIPLayout(makeExecutionPublicLayout, "Comments", commentsItem);
        makeExecutionPublicLayout.addMember(descriptionLabel);
        makeExecutionPublicLayout.addMember(outputFilesForm);
        makeExecutionPublicLayout.addMember(submitButton);
    }
}
