/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor;

import fr.insalyon.creatis.vip.portal.client.view.application.monitor.record.JobRecord;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.menu.JobsContextMenu;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import fr.insalyon.creatis.vip.portal.client.bean.Job;
import fr.insalyon.creatis.vip.portal.client.rpc.JobService;
import fr.insalyon.creatis.vip.portal.client.rpc.JobServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class JobsStackSection extends SectionStackSection {

    private String simulationID;
    private ListGrid grid;

    public JobsStackSection(String simulationID) {
        
        this.simulationID = simulationID;
        this.setTitle("Job Details");
        this.setCanCollapse(true);
        this.setExpanded(false);
        this.setResizeable(true);

        configureGrid();
        this.addItem(grid);

        loadData();
    }

    private void configureGrid() {
        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowRowNumbers(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setCanExpandRecords(true);
        grid.setExpansionMode(ExpansionMode.DETAIL_FIELD);

        ListGridField jobIDField = new ListGridField("jobID", "Job ID");
        ListGridField statusField = new ListGridField("status", "Status");
        ListGridField minorField = new ListGridField("minorStatus", "Minor Status");
        ListGridField commandField = new ListGridField("command", "Command");
        commandField.setHidden(true);

        grid.setFields(jobIDField, statusField, minorField, commandField);

        grid.setGroupStartOpen(GroupStartOpen.ALL);
        grid.setGroupByField("command");
        grid.setDetailField("parameters");

        grid.addRowContextClickHandler(new RowContextClickHandler() {

            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                JobRecord job = (JobRecord) event.getRecord();
                new JobsContextMenu(simulationID, job).showContextMenu();
            }
        });
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            public void onCellDoubleClick(CellDoubleClickEvent event) {
                grid.expandRecord(event.getRecord());
            }
        });
    }

    public void loadData() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<Job>> callback = new AsyncCallback<List<Job>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get jobs list: " + caught.getMessage());
            }

            public void onSuccess(List<Job> result) {
                List<JobRecord> dataList = new ArrayList<JobRecord>();
                for (Job j : result) {
                    dataList.add(new JobRecord(j.getId(), j.getStatus(),
                            j.getCommand(), j.getFileName(), j.getExitCode(),
                            j.getSiteName(), j.getNodeName(), j.getParameters()));
                }
                grid.setData(dataList.toArray(new JobRecord[]{}));
            }
        };
        service.getJobsList(simulationID, callback);
    }
}
