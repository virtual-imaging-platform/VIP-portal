package fr.insalyon.creatis.vip.application.client.view.reprovip;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.data.Record;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.application.client.bean.WorkflowData;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesOutputFile;
import fr.insalyon.creatis.vip.application.client.rpc.*;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.BoutiquesParser;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
import fr.insalyon.creatis.vip.core.client.bean.Pair;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeExecutionPublicTab extends Tab {

    private List<WorkflowData> workflowsData;

    private VLayout makeExecutionPublicLayout;

    private TextItem experienceNameField;
    private TextItem authorNameField;
    private TextAreaItem commentsItem;

    private HLayout rollOverCanvas;

    private ListGrid selectedOutputs;
    private ListGrid othersOuputs;

    public MakeExecutionPublicTab(List<WorkflowData> workflowsData, String authors) {
        setID(ApplicationConstants.TAB_MAKE_EXECUTION_PUBLIC);
        setTitle("Make execution public");
        setCanClose(true);

        this.workflowsData = workflowsData;

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        Layout.getInstance().getModal().show("Loading...", true);

        configureExecutionPublicLayout();

        experienceNameField.setValue("");
        authorNameField.setValue(authors);
        commentsItem.setValue("");
        vLayout.addMember(makeExecutionPublicLayout);
        setPane(vLayout);

        loadOutputsNames();
    }

    private void configureExecutionPublicLayout() {
        experienceNameField = FieldUtil.getTextItem(400, false, "", null);
        authorNameField = FieldUtil.getTextItem(400, false, "", null);
        commentsItem = new TextAreaItem("comment", "");
        commentsItem.setHeight(150);
        commentsItem.setWidth(400);
        commentsItem.setShowTitle(false);

        IButton submitButton = new IButton("Make execution public");

        submitButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (validate()) {
                    final PublicExecution execution = new PublicExecution(
                        experienceNameField.getValueAsString(),
                        workflowsData,
                        PublicExecution.PublicExecutionStatus.REQUESTED,
                        authorNameField.getValueAsString(),
                        commentsItem.getValueAsString(),
                        getOutputIdsSelected(),
                        null);

                    ReproVipService.Util.getInstance().addPublicExecution(execution, new AsyncCallback<Void>() {
                        public void onFailure(Throwable caught) {
                            SC.warn("Failed to add execution: " + caught.getMessage());
                        }
                        public void onSuccess(Void result) {
                            SC.say("Execution added successfully!");
                        }
                    });
                }
            }
        });

        Label descriptionLabel = new Label("Choose your output file(s):");
        descriptionLabel.setHeight(20);
        descriptionLabel.setWidth100();

        makeExecutionPublicLayout = WidgetUtil.getVIPLayout(1000);
        makeExecutionPublicLayout.addMember(
        WidgetUtil.getLabel("<b>Subject to validation by VIP administrators, this functionality allows to push " +
                "the results and execution traces of the concerned VIP workflow(s) on <a href=\"https://zenodo.org/\">Zenodo</a>." +
                " A DOI is retrieved in exchange, allowing to easily identify and share your results with the community." +
                "<br/>" +
                "Please make sure you are allowed to share the output data publicly and do not hesitate to contact " +
                "<a href=\"mailto:vip-support@creatis.insa-lyon.fr\">vip-support@creatis.insa-lyon.fr</a> if you have any questions.Please list here the references " +
                "of the publications that you made using VIP. These references " +
                "may be used by the VIP team to justify the use of computing " +
                "and storage resources. <br/></b>", 20));
        makeExecutionPublicLayout.addMember(new LayoutSpacer(10, 20));

        HLayout info = new HLayout();
        VLayout subLeft = new VLayout();
        VLayout subRight = new VLayout();

        WidgetUtil.addFieldToVIPLayout(subLeft, "Name of the execution simulation", experienceNameField);
        WidgetUtil.addFieldToVIPLayout(subLeft, "Author name", authorNameField);
        WidgetUtil.addFieldToVIPLayout(subRight, "Comments", commentsItem);
        info.addMember(subLeft);
        info.addMember(new LayoutSpacer());
        info.addMember(subRight);

        selectedOutputs = createList("Selected Outputs");
        othersOuputs = createList("Others Outputs");

        HLayout outputs = new HLayout();
        outputs.addMember(listWithTitle(selectedOutputs));
        outputs.addMember(new LayoutSpacer());
        outputs.addMember(listWithTitle(othersOuputs));

        makeExecutionPublicLayout.addMember(info);
        makeExecutionPublicLayout.addMember(descriptionLabel);
        makeExecutionPublicLayout.addMember(outputs);
        makeExecutionPublicLayout.addMember(submitButton);
    }

    private boolean validate() {
        if ( ! experienceNameField.validate()
                || ! authorNameField.validate()
                || ! commentsItem.validate()) {
            SC.warn("The form is invalid");
            return false;
        }
        if (getOutputIdsSelected().isEmpty()) {
            SC.warn("Please select at least one output");
            return false;
        }
        if ( ! experienceNameField.getValueAsString().matches("[a-zA-Z0-9_-]+")) {
            SC.warn("Only alphanumerics characters and (-,_) are authorized!");
            return false;
        }
        return true;
    }

    private List<String> getOutputIdsSelected() {
        List<String> outputsNames = new ArrayList<>();

        for (var record : selectedOutputs.getDataAsRecordList().toArray()) {
            outputsNames.add(record.getAttribute("workflowId") + "-" + record.getAttribute("outputId"));
        }

        return outputsNames;
    }

    private void loadOutputsNames() {
        PublicExecution execution = new PublicExecution(workflowsData);
        List<Pair<String, String>> appsData = execution.getAppsAndVersions();
        AsyncCallback<List<String>> descriptorCallback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().getModal().hide();
                SC.warn("Error retrieving applications descriptors: " + caught.getMessage());
            }
            @Override
            public void onSuccess(List<String> descriptors) {
                Map<Pair<String, String>, List<BoutiquesOutputFile>> allOutputs = new HashMap<>();
                BoutiquesParser parser = new BoutiquesParser();

                try {
                    for (String descriptor : descriptors) {
                        BoutiquesApplication app = parser.parseApplication(descriptor);
    
                        allOutputs.putIfAbsent(
                            new Pair<>(app.getName(), app.getToolVersion()),
                            new ArrayList<>(app.getOutputFiles()));
                    }
    
                    for (var workflow : workflowsData) {
                        var listOutputs = allOutputs.get(new Pair<>(workflow.getAppName(), workflow.getAppVersion()));
    
                        for (BoutiquesOutputFile output : listOutputs) {
                            registerApplicationOutput(
                                workflow.getWorkflowId(), workflow.getAppName(), workflow.getAppVersion(), 
                                output.getName(), output.getId());
                        }
                    }
                } catch (InvalidBoutiquesDescriptorException exception) {
                    SC.warn("Error when parsing application descriptor: " + exception.getMessage());
                } finally {
                    Layout.getInstance().getModal().hide();
                }
            }
        };
        WorkflowService.Util.getInstance().getApplicationsDescriptorsString(
            appsData, descriptorCallback);
    }

    private ListGrid createList(String name) {
        ListGrid list = new ListGrid() {
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);
                }
                return rollOverCanvas;
            }
        };
        list.setTitle(name);
        list.setShowRollOver(false);
        list.setWidth(400);
        list.setHeight(250);
        list.setDragDataAction(DragDataAction.MOVE);
        list.setCanDragRecordsOut(true);
        list.setCanAcceptDroppedRecords(true);
        list.setCanReorderRecords(true);
        list.setFields(
            new ListGridField("workflowId", "Workflow ID"),
            new ListGridField("applicationName", "App Name"),
            new ListGridField("applicationVersion", "App Version"),
            new ListGridField("outputName", "Output Name"));

        return list;
    }

    private void registerApplicationOutput(String workflowId, String appName, String appVersion, String outputName, String outputId) {
        Record record = new Record();

        record.setAttribute("workflowId", workflowId);
        record.setAttribute("applicationName", appName);
        record.setAttribute("applicationVersion", appVersion);
        record.setAttribute("outputName", outputName);
        record.setAttribute("outputId", outputId);

        othersOuputs.addData(record);
    }

    private VLayout listWithTitle(ListGrid list) {
        VLayout vLayout = new VLayout();

        vLayout.addMember(WidgetUtil.getLabel("<b>"+list.getTitle()+"</b>", 50));
        vLayout.addMember(list);
        return vLayout;
    }
}
