package fr.insalyon.creatis.vip.core.client.view.application;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.*;
import com.smartgwt.client.widgets.viewer.*;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;

import java.util.logging.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class ApplicationsTileGrid extends TileGrid {

    private static Logger logger = Logger.getLogger(ApplicationsTileGrid.class.getName());

    protected String tileName;

    public ApplicationsTileGrid(String tileName) {

        if (tileName.length() == 0)
            throw new IllegalArgumentException("ApplicationsTileGrid: tileName is empty");
        this.setID(CoreConstants.getTileGridId(tileName));
        this.tileName = tileName;
        this.setTileWidth(120);
        this.setTileHeight(130);
        this.setWidth("100%");
        this.setHeight("100%");
        this.setOverflow(Overflow.VISIBLE);
        this.setBorder("0px");
        this.setCanReorderTiles(true);
        this.setAnimateTileChange(true);
        this.setShowEdges(false);
        // The message can be seen even if there are tiles, maybe because tiles
        // are addes later, after the grid is created.  So we remove it
        // completely.
        this.setEmptyMessage("");

        DetailViewerField imageField = new DetailViewerField("picture");
        imageField.setType("image");
        // SmartGWT 13 now sets explicit width and height attributes on <img> tags,
        // which default to all available space in the tile, causing image distortion.
        // So we set the explicit width/height of our icons, which are all 48x48.
        imageField.setImageHeight(48);
        imageField.setImageWidth(48);

        DetailViewerField commonNameField = new DetailViewerField("applicationName");
        commonNameField.setCellStyle("normal");
        commonNameField.setCanHilite(false);


        DetailViewerField applicationVersion = new DetailViewerField("applicationVersion");
        applicationVersion.setCellStyle("normal");

        commonNameField.setDetailFormatter(new DetailFormatter() {

            private static final int LINE_MAX_CHAR = 18;
            private static final int MAX_LINES = 3;
            private static final String SEPARATOR_REGEX = "[ \\-_+]";

            public String format(Object value, Record record, DetailViewerField field) {
                String[] words = value.toString().split("(?=" + SEPARATOR_REGEX + "[a-zA-Z0-9])", -1);
                StringBuilder finalName = new StringBuilder();
                StringBuilder currentLine = new StringBuilder();
                int lineNumber = 0;
                int wordIndex = 0;
                while (lineNumber < MAX_LINES && wordIndex < words.length) {
                    String s = words[wordIndex++];
                    if (wordIndex < words.length && words[wordIndex].matches(SEPARATOR_REGEX)) {
                        s += words[wordIndex++];
                    }

                    if (currentLine.length() + s.length() <= LINE_MAX_CHAR - 1) {
                        currentLine.append(s);
                        continue;
                    }

                    if (currentLine.length() > 0) {
                        finalName.append(lineNumber > 0 ? "<br/>" : "")
                                .append(buildLine(currentLine));
                        lineNumber++;
                    }

                    currentLine.setLength(0);
                    currentLine.append(s);
                }

                if (lineNumber < MAX_LINES) {
                    finalName.append(lineNumber > 0 ? "<br/>" : "")
                            .append(buildLine(currentLine));
                }

                return finalName.toString();
            }

            private String buildLine(StringBuilder stringBuilder) {
                if (stringBuilder.length() > (LINE_MAX_CHAR)) {
                    stringBuilder.setLength(LINE_MAX_CHAR - 1);
                    stringBuilder.append('\u2026');
                }
                return stringBuilder.toString();
            }
        });


        this.setFields(imageField, commonNameField, applicationVersion);
        this.setData(new ApplicationTileRecord[]{});

        this.addRecordClickHandler(new RecordClickHandler() {
            @Override
            public void onRecordClick(RecordClickEvent event) {
                ApplicationTileRecord record = (ApplicationTileRecord) event.getRecord();

                parse(record.getApplicationName(), record.getApplicationVersion());
            }
        });
    }

    protected void addApplication(String applicationName, String applicationImage) {
        addApplication(new ApplicationTileRecord(applicationName, applicationImage));
    }

    protected void addApplication(String applicationName, String version, String applicationImage) {
        addApplication(new ApplicationTileRecord(applicationName, version, applicationImage));
    }

    protected void addApplication(ApplicationTileRecord record) {
        this.addData(record);
    }

    public abstract void parse(String applicationName, String version);

    public String getTileName() {
        return tileName;
    }
}
