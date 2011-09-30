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

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationTileRecord;

/**
 *
 * @author Rafael Silva
 */
public class LogsWindow extends Window {

    private String simulationID;
    private TileGrid tileGrid;

    public LogsWindow(String simulationID) {

        this.simulationID = simulationID;

        this.setTitle(Canvas.imgHTML(ApplicationConstants.ICON_LOG) + " Simulation Logs");
        this.setWidth100();
        this.setHeight(140);
        this.setShowCloseButton(false);

        configureTileGrid();
        this.addItem(tileGrid);
    }

    private void configureTileGrid() {

        tileGrid = new TileGrid();
        tileGrid.setWidth100();
        tileGrid.setHeight100();
        tileGrid.setTileWidth(110);
        tileGrid.setTileHeight(100);

        tileGrid.setCanReorderTiles(true);
        tileGrid.setShowAllRecords(true);
        tileGrid.setAnimateTileChange(true);
        tileGrid.setShowEdges(false);

        DetailViewerField pictureField = new DetailViewerField("picture");
        pictureField.setType("image");
        DetailViewerField commonNameField = new DetailViewerField("commonName");

        tileGrid.setFields(pictureField, commonNameField);

        tileGrid.addRecordClickHandler(new RecordClickHandler() {

            public void onRecordClick(RecordClickEvent event) {
                ApplicationTileRecord record = (ApplicationTileRecord) event.getRecord();
                parse(record.getName());
            }
        });
        tileGrid.setData(new ApplicationTileRecord[]{
                    new ApplicationTileRecord(ApplicationConstants.APP_SIMULATION_OUT, ApplicationConstants.APP_IMG_SIMULATION_OUT),
                    new ApplicationTileRecord(ApplicationConstants.APP_SIMULATION_ERROR, ApplicationConstants.APP_IMG_SIMULATION_ERROR)
                });
    }

    private void parse(String applicationName) {

        if (applicationName.equals(ApplicationConstants.APP_SIMULATION_OUT)) {
            new FileViewerWindow("Simulation Output File", simulationID,
                    "", "workflow", ".out").show();

        } else if (applicationName.equals(ApplicationConstants.APP_SIMULATION_ERROR)) {
            new FileViewerWindow("Simulation Error File", simulationID,
                    "", "workflow", ".err").show();
        }
    }
}
