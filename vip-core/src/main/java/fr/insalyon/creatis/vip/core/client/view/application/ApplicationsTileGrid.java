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

            private int LINE_MAX_CHAR = 18;

            public String format(Object value, Record record, DetailViewerField field) {

                String[] words = value.toString().split(" ");
                StringBuilder finalName = new StringBuilder();
                StringBuilder currentLine = new StringBuilder();
                int lineNumber = 0;
                int wordIndex = 0;
                while (lineNumber < 3 && wordIndex < words.length) {
                    String s = words[wordIndex];
                    if (currentLine.length() + s.length() > (LINE_MAX_CHAR - 1)) {
                        if (currentLine.length() > 0) {
                            if (lineNumber > 0) {
                                finalName.append("<br/>");
                            }
                            finalName.append(buildLine(currentLine));
                            lineNumber++;
                        }
                        currentLine = new StringBuilder(s);
                    } else {
                        currentLine.append(" ");
                        currentLine.append(s);
                    }
                    wordIndex++;
                }
                if (lineNumber < 3) {
                    if (lineNumber > 0) {
                        finalName.append("<br/>");
                    }
                    finalName.append(buildLine(currentLine));
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
