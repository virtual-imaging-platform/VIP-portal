/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.LabelButton;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class DebugLayout extends VLayout {

    private DebugLayout instance;
    private String simulationID;
    private int jobID;
    private VLayout tasksLayout;
    private HLayout menuLayout;
    private LabelButton outputLabel;
    private LabelButton errorLabel;
    private LabelButton appOutputLabel;
    private LabelButton appErrorLabel;
    private LabelButton scriptLabel;
    private Label infoLabel;
    private HLayout bottomLayout;
    private LabelButton downloadLabel;
    private String selectedTaskID;
    private Timer refresher;
    private String loadedFile;

    public DebugLayout(String simulationID, int jobID) {

        this.instance = this;
        this.simulationID = simulationID;
        this.jobID = jobID;

        this.setWidth(800);
        this.setHeight((int) (Layout.getInstance().getLayoutCanvas().getVisibleHeight() * 0.8));
        this.setPadding(5);
        this.setMembersMargin(3);
        this.setBorder("1px solid #E0E0E0");
        this.setBackgroundColor("#FFFFFF");
        this.setOverflow(Overflow.AUTO);

        HLayout titleLayout = new HLayout(5);
        titleLayout.setWidth100();
        titleLayout.setHeight(20);

        Label titleLabel = WidgetUtil.getLabel("<b>Debugging #" + jobID + "</b>",
                ApplicationConstants.ICON_MONITOR_DEBUG, 30);
        titleLabel.setWidth100();
        titleLayout.addMember(titleLabel);
        titleLayout.addMember(WidgetUtil.getSpaceLabel(16));
        titleLayout.addMember(WidgetUtil.getIconLabel(CoreConstants.ICON_CLOSE, "Close", 16, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                destroy();
            }
        }));
        this.addMember(titleLayout);

        Label tasksLabel = WidgetUtil.getLabel("<font color=\"#666666\">List of Tasks</font>",
                ApplicationConstants.ICON_MONITOR_TASKS, 16);
        tasksLabel.setBorder("1px solid #E2E2E2");
        tasksLabel.setBackgroundColor("#F2F2F2");
        tasksLabel.setPadding(3);
        tasksLabel.setAlign(Alignment.CENTER);
        this.addMember(tasksLabel);

        configureTasksLayout();
        configureMenuLayout();
        configureInfoLabel();
        configureBottomLayout();

        loadTasks();
    }

    @Override
    protected void onDraw() {
        moveTo(Layout.getInstance().getLayoutCanvas().getVisibleWidth() / 2 - 400, 35);
    }

    private void configureTasksLayout() {

        tasksLayout = new VLayout(3);
        tasksLayout.setWidth100();
        tasksLayout.setHeight(120);
        tasksLayout.setOverflow(Overflow.AUTO);

        this.addMember(tasksLayout);
    }

    private void configureMenuLayout() {

        menuLayout = new HLayout(10);
        menuLayout.setWidth100();
        menuLayout.setHeight(25);

        appOutputLabel = new LabelButton("Application Output File", ApplicationConstants.ICON_MONITOR_OUTPUT_FILE);
        appOutputLabel.setWidth(170);
        appOutputLabel.setDisabled(true);
        appOutputLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadFile(SimulationFileType.ApplicationOutputFile);
                appOutputLabel.setSelected(true);
                appErrorLabel.setSelected(false);
                outputLabel.setSelected(false);
                errorLabel.setSelected(false);
                scriptLabel.setSelected(false);
            }
        });
        menuLayout.addMember(appOutputLabel);

        appErrorLabel = new LabelButton("Application Error File", ApplicationConstants.ICON_MONITOR_ERROR_FILE);
        appErrorLabel.setWidth(170);
        appErrorLabel.setDisabled(true);
        appErrorLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadFile(SimulationFileType.ApplicationErrorFile);
                appOutputLabel.setSelected(false);
                appErrorLabel.setSelected(true);
                outputLabel.setSelected(false);
                errorLabel.setSelected(false);
                scriptLabel.setSelected(false);
            }
        });
        menuLayout.addMember(appErrorLabel);

        outputLabel = new LabelButton("Output File", ApplicationConstants.ICON_MONITOR_OUTPUT_FILE);
        outputLabel.setDisabled(true);
        outputLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadFile(SimulationFileType.OutputFile);
                appOutputLabel.setSelected(false);
                appErrorLabel.setSelected(false);
                outputLabel.setSelected(true);
                errorLabel.setSelected(false);
                scriptLabel.setSelected(false);
            }
        });
        menuLayout.addMember(outputLabel);

        errorLabel = new LabelButton("Error File", ApplicationConstants.ICON_MONITOR_ERROR_FILE);
        errorLabel.setDisabled(true);
        errorLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadFile(SimulationFileType.ErrorFile);
                appOutputLabel.setSelected(false);
                appErrorLabel.setSelected(false);
                outputLabel.setSelected(false);
                errorLabel.setSelected(true);
                scriptLabel.setSelected(false);
            }
        });
        menuLayout.addMember(errorLabel);

        scriptLabel = new LabelButton("Script File", ApplicationConstants.ICON_MONITOR_OUTPUT_FILE);
        scriptLabel.setDisabled(true);
        scriptLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadFile(SimulationFileType.ScriptFile);
                appOutputLabel.setSelected(false);
                appErrorLabel.setSelected(false);
                outputLabel.setSelected(false);
                errorLabel.setSelected(false);
                scriptLabel.setSelected(true);
            }
        });
        menuLayout.addMember(scriptLabel);

        this.addMember(menuLayout);
    }

    private void configureInfoLabel() {

        infoLabel = new Label();
        infoLabel.setWidth100();
        infoLabel.setHeight100();
        infoLabel.setPadding(5);
        infoLabel.setBorder("1px solid #E2E2E2");
        infoLabel.setCanSelectText(true);
        infoLabel.setContents("<font color=\"#666666\">No data available.</font>");
        infoLabel.setOverflow(Overflow.AUTO);
        infoLabel.setValign(VerticalAlignment.TOP);

        this.addMember(infoLabel);
    }

    private void configureBottomLayout() {

        bottomLayout = new HLayout(10);
        bottomLayout.setWidth100();
        bottomLayout.setHeight(25);

        LabelButton closeLabel = new LabelButton("Close", CoreConstants.ICON_CLOSE);
        closeLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                refresher.cancel();
                destroy();
            }
        });
        bottomLayout.addMember(closeLabel);

        downloadLabel = new LabelButton("Download File", ApplicationConstants.ICON_MONITOR_DOWNLOAD);
        downloadLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                com.google.gwt.user.client.Window.open(
                        GWT.getModuleBaseURL()
                        + "/getfileservice?filepath=" + loadedFile
                        + "&" + CoreConstants.COOKIES_SESSION
                        + "=" + Cookies.getCookie(CoreConstants.COOKIES_SESSION),
                        "", "");
            }
        });
        downloadLabel.setDisabled(true);
        bottomLayout.addMember(downloadLabel);

        this.addMember(bottomLayout);
    }

    private void loadTasks() {

        AsyncCallback<List<Task>> callback = new AsyncCallback<List<Task>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load tasks:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Task> result) {
                Task t = null;
                for (Task task : result) {
                    if (t == null) {
                        t = task;
                    }
                    tasksLayout.addMember(new TaskLayout(instance, simulationID, task));
                }
                refresh();
                selectTask(t.getId(), t.getStatus());
            }
        };
        JobService.Util.getInstance().getTasks(simulationID, jobID, callback);
    }

    /**
     * Loads a file and displays it in the panel.
     *
     * @param folder File's folder
     * @param fileName File's name
     * @param extension File's extension
     */
    private void loadFile(SimulationFileType fileType) {

        AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load file:<br />" + caught.getMessage());
                infoLabel.setContents("<font color=\"#666666\">No data available.</font>");
                infoLabel.setIcon(null);
            }

            @Override
            public void onSuccess(String[] result) {
                loadedFile = result[0];
                downloadLabel.setDisabled(false);
                infoLabel.setContents(result[1].replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br />"));
                infoLabel.setIcon(null);
            }
        };
        infoLabel.setContents("Loading file contents...");
        infoLabel.setIcon(CoreConstants.ICON_LOADING);
        JobService.Util.getInstance().readSimulationFile(simulationID, selectedTaskID, fileType, callback);
    }
    
    /**
     * 
     * @param taskID
     * @param status 
     */
    public void selectTask(String taskID, TaskStatus status) {

        for (Canvas canvas : tasksLayout.getMembers()) {
            if (canvas instanceof TaskLayout) {
                TaskLayout taskLayout = (TaskLayout) canvas;
                if (taskLayout.getTaskID().equals(taskID)) {
                    taskLayout.setSelected(true);
                    selectedTaskID = taskID;
                    if (status == TaskStatus.COMPLETED || status == TaskStatus.ERROR) {
                        outputLabel.setDisabled(false);
                        errorLabel.setDisabled(false);
                        appOutputLabel.setDisabled(false);
                        appOutputLabel.setSelected(true);
                        appErrorLabel.setDisabled(false);
                        loadFile(SimulationFileType.ApplicationOutputFile);
                    } else {
                        outputLabel.setDisabled(true);
                        errorLabel.setDisabled(true);
                        appOutputLabel.setDisabled(true);
                        appErrorLabel.setDisabled(true);
                        scriptLabel.setSelected(true);
                        loadFile(SimulationFileType.ScriptFile);
                    }
                    scriptLabel.setDisabled(false);
                    infoLabel.setContents("<font color=\"#666666\">No data available.</font>");
                } else {
                    taskLayout.setSelected(false);
                }
            }
        }
    }

    private void refresh() {

        refresher = new Timer() {
            @Override
            public void run() {
                AsyncCallback<List<Task>> callback = new AsyncCallback<List<Task>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Layout.getInstance().setWarningMessage("Unable to load tasks:<br />" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(List<Task> result) {
                        for (Task task : result) {
                            boolean contains = false;
                            for (Canvas canvas : tasksLayout.getMembers()) {
                                if (canvas instanceof TaskLayout) {
                                    TaskLayout taskLayout = (TaskLayout) canvas;
                                    if (taskLayout.getTaskID().equals(task.getId())) {
                                        contains = true;
                                        if (task.getStatus() != taskLayout.getStatus()) {
                                            taskLayout.setStatus(task.getStatus(), task.getExitCode(), task.getMinorStatus());
                                        }
                                        break;
                                    }
                                }
                            }
                            if (!contains) {
                                tasksLayout.addMember(new TaskLayout(instance, simulationID, task));
                            }
                        }
                    }
                };
                JobService.Util.getInstance().getTasks(simulationID, jobID, callback);
            }
        };
        refresher.scheduleRepeating(20000);
    }
}
