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
package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.ProcessorStatus;
import fr.insalyon.creatis.vip.application.client.bean.Processor;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.ProcessorRecord;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ProcessorsWindow extends Window {

    private String simulationID;
    private ModalWindow modal;
    private ListGrid grid;

    public ProcessorsWindow(String simulationID) {

        this.simulationID = simulationID;

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_PROCESSOR) + " Simulation Activities");
        this.setWidth100();
        this.setHeight(220);
        this.setShowCloseButton(false);

        configureGrid();
        modal = new ModalWindow(grid);

        this.addItem(grid);
    }

    private void configureGrid() {

        grid = new ListGrid() {

            @Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {

                String fieldName = getFieldName(colNum);

                if (fieldName.equals("status")) {
                    ProcessorRecord processorRecord = (ProcessorRecord) record;
                    ProcessorStatus status = processorRecord.getStatus();

                    if (status == ProcessorStatus.Unstarted) {
                        return "color:#7F7F7F;";

                    } else if (status == ProcessorStatus.Active) {
                        return "color:#009900;";

                    } else if (status == ProcessorStatus.Completed) {
                        return "color:#287fd6;";

                    } else if (status == ProcessorStatus.Failed) {
                        return "color:#d64949;";
                    }

                } else if (fieldName.equals("completed")) {
                    return "color:#287fd6;";

                } else if (fieldName.equals("queued")) {
                    return "color:#009900;";

                } else if (fieldName.equals("failed")) {
                    return "color:#d64949;";
                }

                return super.getCellCSSText(record, rowNum, colNum);
            }
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField nameField = new ListGridField("name", "Name");
        ListGridField statusField = new ListGridField("status", "Status");
        statusField.setWidth(80);
        ListGridField completedField = getNumberField("completed", "Completed");
        ListGridField queuedField = getNumberField("queued", "Active");
        ListGridField failedField = getNumberField("failed", "Failed");

        grid.setFields(nameField, statusField, completedField, queuedField, failedField);
    }

    /**
     * 
     */
    public void loadData() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<Processor>> callback = new AsyncCallback<List<Processor>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to load processors:<br />" + caught.getMessage());
            }

            public void onSuccess(List<Processor> result) {
                modal.hide();

                List<ProcessorRecord> data = new ArrayList<ProcessorRecord>();

                for (Processor processor : result) {
                    data.add(new ProcessorRecord(processor.getName(), processor.getStatus(),
                            processor.getCompleted(), processor.getQueued(), processor.getFailed()));
                }
                grid.setData(data.toArray(new ProcessorRecord[]{}));
            }
        };
        modal.show("Loading processors...", true);
        service.getProcessors(simulationID, callback);
    }
    
    /**
     * 
     * @param name
     * @param title
     * @return 
     */
    private ListGridField getNumberField(String name, String title) {
        
        ListGridField field = new ListGridField(name, title);
        field.setWidth(70);
        field.setAlign(Alignment.RIGHT);
        
        return field;
    }
}
