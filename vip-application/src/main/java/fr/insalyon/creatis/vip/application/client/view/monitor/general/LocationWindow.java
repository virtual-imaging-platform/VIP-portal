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
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.record.LocationRecord;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class LocationWindow extends Window {

    private String simulationID;
    private ModalWindow modal;
    private ListGrid grid;

    public LocationWindow(String simulationID) {

        this.simulationID = simulationID;
        
        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_WORLD) + " Simulation Execution Location");
        this.setWidth100();
        this.setHeight(170);
        this.setShowCloseButton(false);
        
        configureGrid();
        modal = new ModalWindow(grid);

        this.addItem(grid);
    }
    
    private void configureGrid() {
        
        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(true);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");
        
        ListGridField icoField = FieldUtil.getIconGridField("icon");
        ListGridField nameField = new ListGridField("country", "Country");
        ListGridField jobsField = new ListGridField("jobs", "Tasks");
        jobsField.setWidth(70);
        jobsField.setAlign(Alignment.RIGHT);
        
        grid.setFields(icoField, nameField, jobsField);
        grid.setSortField("country");
    }
    
    public void loadData() {
        
        JobServiceAsync service = JobService.Util.getInstance();
        AsyncCallback<Map<String, Integer>> callback = new AsyncCallback<Map<String, Integer>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to load locations:<br />" + caught.getMessage());
            }

            public void onSuccess(Map<String, Integer> result) {
                modal.hide();
                
                List<LocationRecord> data = new ArrayList<LocationRecord>();
                
                for (String code : result.keySet()) {
                    data.add(new LocationRecord(code, result.get(code)));
                }
                grid.setData(data.toArray(new LocationRecord[]{}));
            }
        };
        modal.show("Loading locations...", true);
        service.getCountriesMap(simulationID, callback);
    }
}
