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
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.GeoMap;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class LocationLayout extends AbstractFormLayout {

    private String simulationID;
    private VLayout chartLayout;
    private VLayout innerChartLayout;

    public LocationLayout(String simulationID) {

        super("100%", "250px");
        addTitle("Simulation Execution Location", CoreConstants.ICON_WORLD);
        this.simulationID = simulationID;

        configureChart();
        modal = new ModalWindow(chartLayout);
    }

    private void configureChart() {

        chartLayout = new VLayout();
        chartLayout.setWidth("100%");
        chartLayout.setHeight("100%");

        innerChartLayout = new VLayout();
        innerChartLayout.setWidth("100%");
        innerChartLayout.setHeight("100%");

        chartLayout.addMember(innerChartLayout);
        this.addMember(chartLayout);
    }

    public void loadData() {

        JobServiceAsync service = JobService.Util.getInstance();
        AsyncCallback<Map<String, Integer>> callback = new AsyncCallback<Map<String, Integer>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to load locations:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String, Integer> result) {
                modal.hide();
                VisualizationUtils.loadVisualizationApi(getGeoMapRunnable(result), GeoMap.PACKAGE);
            }
        };
        modal.show("Loading locations...", true);
        service.getCountriesMap(simulationID, callback);
    }

    private Runnable getGeoMapRunnable(final Map<String, Integer> data) {

        return new Runnable() {

            @Override
            public void run() {

                GeoMap.Options options = GeoMap.Options.create();
                options.setWidth("100%");
                options.setHeight("100%");

                DataTable dataTable = DataTable.create();
                dataTable.addColumn(AbstractDataTable.ColumnType.STRING, "Country");
                dataTable.addColumn(AbstractDataTable.ColumnType.NUMBER, "Simulated Jobs");
                dataTable.addRows(data.size());

                int i = 0;
                for (String country : data.keySet()) {
                    dataTable.setValue(i, 0, CountryCode.valueOf(country).getCountryName());
                    dataTable.setValue(i, 1, data.get(country));
                    i++;
                }
                
                GeoMap geoMap = new GeoMap(dataTable, options);

                chartLayout.removeMember(innerChartLayout);
                innerChartLayout = new VLayout();
                innerChartLayout.setWidth("100%");
                innerChartLayout.setHeight("100%");
                innerChartLayout.addMember(geoMap);
                chartLayout.addMember(innerChartLayout);
            }
        };
    }
}
