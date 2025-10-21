package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.models.InOutData;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;
import fr.insalyon.creatis.vip.gatelab.client.GateLabConstants;
import fr.insalyon.creatis.vip.gatelab.client.rpc.GateLabService;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabDownloadsLayout extends HLayout {

    private String simulationID;
    private GateLabDownloadButtonLayout inputLayout;
    private GateLabDownloadButtonLayout outputLayout;
    private Label inputLabel;
    private Label outputLabel;
    private String input;
    private String output;

    public GateLabDownloadsLayout(String simulationID) {

        this.simulationID = simulationID;
        this.setWidth100();
        this.setHeight(100);
        this.setMembersMargin(30);

        inputLabel = WidgetUtil.getLabel("<font color=\"#666666\"><em>Loading information...</em></font>", 15);
        outputLabel = WidgetUtil.getLabel("<font color=\"#666666\"><em>Not yet available.</em></font>", 15);

        inputLayout = new GateLabDownloadButtonLayout("Input",
                GateLabConstants.ICON_DOWNLOAD_INPUT, inputLabel);
        outputLayout = new GateLabDownloadButtonLayout("Output",
                GateLabConstants.ICON_DOWNLOAD_OUTPUT, outputLabel);

        this.addMember(inputLayout);
        this.addMember(outputLayout);

        loadInputData();
        loadData();
    }

    private void loadInputData() {

        AsyncCallback<Map<String, String>> callback = new AsyncCallback<Map<String, String>>() {
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load input information:<br />" + caught.getMessage());
            }

            public void onSuccess(Map<String, String> result) {
                input = result.get("inputlink");
                inputLabel.setContents("<font color=\"#666666\">" + input + "</font>");
                inputLayout.setDisabled(false);
                inputLayout.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        downloadFile(input);
                    }
                });
            }
        };
        GateLabService.Util.getInstance().getGatelabWorkflowInputs(simulationID, callback);
    }

    private void loadData() {

        WorkflowServiceAsync serviceOut = WorkflowService.Util.getInstance();
        AsyncCallback<List<InOutData>> callback = new AsyncCallback<List<InOutData>>() {
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get simulation output:<br />" + caught.getMessage());
            }

            public void onSuccess(List<InOutData> result) {
                for (InOutData data : result) {
                    if (data.getProcessor().toLowerCase().contains("result")) {
                        output = data.getPath();
                        outputLabel.setContents("<font color=\"#666666\">" + output + "</font>");
                        outputLayout.setDisabled(false);
                        outputLayout.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                downloadFile(output);
                            }
                        });
                        break;
                    }

                }
            }
        };
        serviceOut.getOutputData(simulationID, callback);
    }

    public void update() {
        loadData();
    }

    private void downloadFile(String path) {

        AsyncCallback<String> callback = new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to download file:<br />" + caught.getMessage());
            }

            public void onSuccess(String result) {
                Layout.getInstance().setNoticeMessage("File added to transfer queue.");
                OperationLayout.getInstance().addOperation(result);
                ((DataManagerSection) Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
            }
        };
        DataManagerService.Util.getInstance().downloadFile(path, callback);
    }
}
