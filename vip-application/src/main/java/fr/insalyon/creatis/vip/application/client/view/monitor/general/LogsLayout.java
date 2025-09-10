package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.monitor.ViewerWindow;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationTileRecord;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class LogsLayout extends AbstractFormLayout {

    private String simulationID;
    private TileGrid tileGrid;

    public LogsLayout(String simulationID) {

        super("100%", "145px");
        addTitle("Execution Logs", ApplicationConstants.ICON_LOG);

        this.simulationID = simulationID;
        configureTileGrid();
    }

    private void configureTileGrid() {

        tileGrid = new TileGrid();
        tileGrid.setWidth100();
        tileGrid.setHeight100();
        tileGrid.setTileWidth(110);
        tileGrid.setTileHeight(90);
        tileGrid.setBackgroundColor("#F5F5F5");

        tileGrid.setCanReorderTiles(true);
        tileGrid.setShowAllRecords(true);
        tileGrid.setAnimateTileChange(true);
        tileGrid.setShowEdges(false);

        DetailViewerField pictureField = new DetailViewerField("picture");
        pictureField.setType("image");
        pictureField.setImageHeight(48);
        pictureField.setImageWidth(48);
        DetailViewerField commonNameField = new DetailViewerField("commonName");

        tileGrid.setFields(pictureField, commonNameField);

        tileGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                ApplicationTileRecord record = (ApplicationTileRecord) event.getRecord();
                parse(record.getName());
            }
        });
        tileGrid.setData(new ApplicationTileRecord[]{
                    new ApplicationTileRecord(ApplicationConstants.APP_SIMULATION_OUT, "", ApplicationConstants.APP_IMG_SIMULATION_OUT),
                    new ApplicationTileRecord(ApplicationConstants.APP_SIMULATION_ERROR, "", ApplicationConstants.APP_IMG_SIMULATION_ERROR)
                });
        
        this.addMember(tileGrid);
    }

    private void parse(String applicationName) {

        if (applicationName.equals(ApplicationConstants.APP_SIMULATION_OUT)) {
            new ViewerWindow("Execution Output File", simulationID,
                    "", "workflow", ".out").show();

        } else if (applicationName.equals(ApplicationConstants.APP_SIMULATION_ERROR)) {
            new ViewerWindow("Execution Error File", simulationID,
                    "", "workflow", ".err").show();
        }
    }
}
