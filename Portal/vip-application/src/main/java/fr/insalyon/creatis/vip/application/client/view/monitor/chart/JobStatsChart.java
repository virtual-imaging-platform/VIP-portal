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
package fr.insalyon.creatis.vip.application.client.view.monitor.chart;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.application.client.view.monitor.chart.WorkflowStatsChart;
import fr.insalyon.creatis.vip.application.client.view.monitor.chart.JobStatsChart;
import fr.insalyon.creatis.vip.application.client.view.monitor.chart.GeneralBarChart;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author camarasu
 */
public class JobStatsChart extends AbstractChart {

    public JobStatsChart(List<String> data, VLayout chartLayout, ListGrid grid) {
        super(data, chartLayout, grid);
    }

    public void build() {

        chartLayout.removeMembers(chartLayout.getMembers());
        int completedJobs = 0;
        int cancelledJobs = 0;
        int failedJobs = 0;
        int stalledJobs = 0;
        int totalJobs = 0;
        long completedJobsExec = 0, cancelledJobsExec = 0, failedJobsExec = 0, stalledJobsExec = 0, totalJobsExec = 0;

        // add data
        /*
        grid.setData(new PropertyRecord[]{
        new PropertyRecord("Completed Jobs", data.get(0) + ""),
        new PropertyRecord("completed WaitingTime", data.get(1) + ""),
        new PropertyRecord("completed ExecutionTime", data.get(2) + ""),
        new PropertyRecord("completed InputTime", data.get(3) + ""),
        new PropertyRecord("completed OutputTime", data.get(4) + ""),
        new PropertyRecord("Cancelled Jobs", data.get(5) + ""),
        new PropertyRecord("Cancelled WaitingTime", data.get(6) + ""),
        new PropertyRecord("Cancelled ExecutionTime", data.get(7) + ""),
        new PropertyRecord("Cancelled InputTime", data.get(8) + ""),
        new PropertyRecord("Cancelled OutputTime", data.get(9) + ""),
        new PropertyRecord("failedApplication Jobs", data.get(10) + ""),
        new PropertyRecord("failedApplication WaitingTime", data.get(11) + ""),
        new PropertyRecord("failedApplication ExecutionTime", data.get(12) + ""),
        new PropertyRecord("failedApplication InputTime", data.get(13) + ""),
        new PropertyRecord("failedApplication OutputTime", data.get(14) + ""),
        new PropertyRecord("failedInput Jobs", data.get(15) + ""),
        new PropertyRecord("failedInput WaitingTime", data.get(16) + ""),
        new PropertyRecord("failedInput ExecutionTime", data.get(17) + ""),
        new PropertyRecord("failedInput InputTime", data.get(18) + ""),
        new PropertyRecord("failedInput OutputTime", data.get(19) + ""),
        new PropertyRecord("failedInput Jobs", data.get(15) + ""),
        new PropertyRecord("failedInput WaitingTime", data.get(16) + ""),
        new PropertyRecord("failedInput ExecutionTime", data.get(17) + ""),
        new PropertyRecord("failedInput InputTime", data.get(18) + ""),
        new PropertyRecord("failedInput OutputTime", data.get(19) + ""),
        new PropertyRecord("failedOutput Jobs", data.get(20) + ""),
        new PropertyRecord("failedOutput WaitingTime", data.get(21) + ""),
        new PropertyRecord("failedOutput ExecutionTime", data.get(22) + ""),
        new PropertyRecord("failedOutput InputTime", data.get(23) + ""),
        new PropertyRecord("failedOutput OutputTime", data.get(24) + ""),
        new PropertyRecord("failedStalled Jobs", data.get(25) + ""),
        new PropertyRecord("failedStalled WaitingTime", data.get(26) + ""),
        new PropertyRecord("failedStalled ExecutionTime", data.get(27) + ""),
        new PropertyRecord("failedStalled InputTime", data.get(28) + ""),
        new PropertyRecord("failedStalled OutputTime", data.get(29) + "")
        });
         * 
         */
        //PropertyRecord[] p = new PropertyRecord[data.size()];
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) != null) {
                addRowData(data.get(i));
                String[] v = data.get(i).split("##");
                //p[i] = new PropertyRecord(v[0], v[1]);
                switch (i) {
                    case 0:
                        completedJobs = completedJobs + Integer.parseInt(v[1]);
                        break;
                    case 2:
                        completedJobsExec = completedJobsExec + Integer.parseInt(v[1]);
                        break;
                    case 5:
                        cancelledJobs = cancelledJobs + Integer.parseInt(v[1]);
                        break;
                    case 7:
                        cancelledJobsExec = cancelledJobsExec + Integer.parseInt(v[1]);
                        break;
                    case 10:
                        failedJobs = failedJobs + Integer.parseInt(v[1]);
                        break;
                    case 12:
                        failedJobsExec = failedJobsExec + Integer.parseInt(v[1]);
                        break;
                    case 15:
                        failedJobs = failedJobs + Integer.parseInt(v[1]);
                        break;
                    case 17:
                        failedJobsExec = failedJobsExec + Integer.parseInt(v[1]);
                        break;
                    case 20:
                        failedJobs = failedJobs + Integer.parseInt(v[1]);
                        break;
                    case 22:
                        failedJobsExec = failedJobsExec + Integer.parseInt(v[1]);
                        break;
                    case 25:
                        stalledJobs = stalledJobs + Integer.parseInt(v[1]);
                    case 27:
                        stalledJobsExec = stalledJobsExec + Integer.parseInt(v[1]);

                }
            }

        }
        totalJobs = completedJobs + cancelledJobs + failedJobs + stalledJobs;

        grid.setData(new PropertyRecord[]{
                    new PropertyRecord("Completed Jobs", completedJobs + ""),
                    new PropertyRecord("Cancelled Jobs", cancelledJobs + ""),
                    new PropertyRecord("Failed Jobs", failedJobs + ""),
                    new PropertyRecord("Stalled Jobs", stalledJobs + ""),
                    new PropertyRecord("Total Jobs", totalJobs + "")
                });
        //grid.setCursor(Cursor.TEXT);


        ListGrid gridExecution = new ListGrid();
        gridExecution.setWidth(280);
        gridExecution.setHeight(145);
        gridExecution.setShowAllRecords(true);

        ListGridField propertyField = new ListGridField("property", "Job Types");
        ListGridField valueField = new ListGridField("value", "Execution Times (sec)");
        valueField.setAlign(Alignment.RIGHT);
        valueField.setCellFormatter(new CellFormatter() {

            @Override
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {

                if (value == null) {
                    return null;
                }
                try {
                    NumberFormat nf = NumberFormat.getDecimalFormat();
                    return nf.format(Double.parseDouble((String) value));
                } catch (Exception e) {
                    return value.toString();
                }
            }
        });

        gridExecution.setFields(propertyField, valueField);
        totalJobsExec = completedJobsExec + cancelledJobsExec + failedJobsExec + stalledJobsExec;
        gridExecution.setData(new PropertyRecord[]{
                    new PropertyRecord("Completed-jobs execution time", completedJobsExec + ""),
                    new PropertyRecord("Cancelled-jobs execution time", cancelledJobsExec + ""),
                    new PropertyRecord("Failed-jobs execution time", failedJobsExec + ""),
                    new PropertyRecord("Stalled-jobs execution time", stalledJobsExec + ""),
                    new PropertyRecord("Total execution time", totalJobsExec + "")
                });
        chartLayout.addMember(gridExecution);
        gridExecution.setCanSelectCells(true);
        gridExecution.setCanSelectText(true);
        gridExecution.setCanEdit(true);
        chartLayout.addMember(getRowDataImg());
    }
}
