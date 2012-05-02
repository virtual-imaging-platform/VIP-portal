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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.chart.CheckpointChart;
import fr.insalyon.creatis.vip.application.client.view.monitor.chart.GeneralBarChart;
import fr.insalyon.creatis.vip.application.client.view.monitor.chart.JobFlowChart;
import fr.insalyon.creatis.vip.application.client.view.monitor.chart.SitesBarChart;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ChartsTab extends Tab {

    private String simulationID;
    private ModalWindow modal;
    private VLayout leftVLayout;
    private VLayout rightVLayout;
    private SelectItem chartsItem;
    private TextItem binItem;
    private ListGrid grid;

    public ChartsTab(String simulationID) {

        this.simulationID = simulationID;
        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_CHART));
        this.setPrompt("Performance Statistics");

        leftVLayout = new VLayout(15);
        leftVLayout.setWidth(280);
        leftVLayout.setHeight100();
        leftVLayout.setOverflow(Overflow.AUTO);

        rightVLayout = new VLayout(15);
        rightVLayout.setWidth100();
        rightVLayout.setHeight(600);
        rightVLayout.setOverflow(Overflow.AUTO);
        rightVLayout.setPadding(10);

        configureForm();
        configureGrid();

        HLayout hLayout = new HLayout(10);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);
        hLayout.addMember(leftVLayout);
        hLayout.addMember(rightVLayout);

        modal = new ModalWindow(hLayout);
        this.setPane(hLayout);
    }

    private void configureForm() {

        LinkedHashMap<String, String> chartsMap = new LinkedHashMap<String, String>();
        chartsMap.put("1", "Job Flow");
        chartsMap.put("2", "Checkpoints per Job");
        chartsMap.put("3", "Execution Times Histogram");
        chartsMap.put("4", "Download Times Histogram");
        chartsMap.put("5", "Upload Times Histogram");
        chartsMap.put("6", "Sites Histogram");
        chartsItem = new SelectItem();
        chartsItem.setShowTitle(false);
        chartsItem.setWidth(250);
        chartsItem.setValueMap(chartsMap);
        chartsItem.setEmptyDisplayValue("Select a chart...");
        chartsItem.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                int value = new Integer(chartsItem.getValueAsString());
                if (value == 1 || value == 2 || value == 6) {
                    binItem.setDisabled(true);
                } else {
                    binItem.setDisabled(false);
                }
            }
        });

        binItem = FieldUtil.getTextItem(50, false, "", "[0-9.]");
        binItem.setValue("100");

        IButton generateButton = new IButton("Generate Chart");
        generateButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                generateChart();
            }
        });

        VLayout formLayout = WidgetUtil.getVIPLayout(280, 185, false);
        formLayout.addMember(WidgetUtil.getLabel("<b>Performance Statistics</b>", ApplicationConstants.ICON_CHART, 30));
        WidgetUtil.addFieldToVIPLayout(formLayout, "Chart", chartsItem);
        WidgetUtil.addFieldToVIPLayout(formLayout, "Bin Size", binItem);
        formLayout.addMember(generateButton);

        leftVLayout.addMember(formLayout);
    }

    private void configureGrid() {

        grid = new ListGrid();
        grid.setWidth(280);
        grid.setHeight(145);
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField propertyField = new ListGridField("property", "Properties");
        ListGridField valueField = new ListGridField("value", "Value");
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

        grid.setFields(propertyField, valueField);

        leftVLayout.addMember(grid);
    }

    private void generateChart() {

        int value = Integer.parseInt(chartsItem.getValueAsString());
        int binSize = 100;
        if (binItem.getValueAsString() != null && !binItem.getValueAsString().isEmpty()) {
            binSize = Integer.parseInt(binItem.getValueAsString().trim());
        }

        switch (value) {
            case 1:
                plotJobsPerTime();
                break;
            case 2:
                plotCkptsPerJob();
                break;
            case 3:
                plotExecutionPerNumberOfJobs(binSize);
                break;
            case 4:
                plotDownloadPerNumberOfJobs(binSize);
                break;
            case 5:
                plotUploadPerNumberOfJobs(binSize);
                break;
            case 6:
                plotSiteHistogram();
        }
    }

    private void plotJobsPerTime() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                JobFlowChart.build(result, rightVLayout, grid);
                modal.hide();
            }
        };
        modal.show("Building job flow chart...", true);
        service.getJobFlow(simulationID, callback);
    }

    private void plotCkptsPerJob() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                CheckpointChart.build(result, rightVLayout, grid);
                modal.hide();
            }
        };
        modal.show("Building chart...", true);
        service.getCkptsPerJob(simulationID, callback);
    }

    /**
     *
     * @param binSize Size of the group
     */
    private void plotExecutionPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                GeneralBarChart.build(result, rightVLayout, grid, "Execution Time (sec)", "#00aa00", binSize);
                modal.hide();
            }
        };
        modal.show("Building chart...", true);
        service.getExecutionPerNumberOfJobs(simulationID, binSize, callback);
    }

    /**
     *
     * @param binSize
     */
    private void plotDownloadPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                GeneralBarChart.build(result, rightVLayout, grid, "Download Time (sec)", "#6699CC", binSize);
                modal.hide();
            }
        };
        modal.show("Building chart...", true);
        service.getDownloadPerNumberOfJobs(simulationID, binSize, callback);
    }

    /**
     *
     * @param binSize
     */
    private void plotUploadPerNumberOfJobs(final int binSize) {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                GeneralBarChart.build(result, rightVLayout, grid, "Upload Time (sec)", "#CC9966", binSize);
                modal.hide();
            }
        };
        modal.show("Building chart...", true);
        service.getUploadPerNumberOfJobs(simulationID, binSize, callback);
    }

    private void plotSiteHistogram() {

        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get chart data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                SitesBarChart.build(result, rightVLayout, grid);
                modal.hide();
            }
        };
        modal.show("Building chart...", true);
        service.getSiteHistogram(simulationID, callback);
    }
}
