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
package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
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
                    if (data.getProcessor().equals("merged_result")) {
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
