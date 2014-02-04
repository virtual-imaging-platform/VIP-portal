/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.core.client.view.application;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.BackgroundRepeat;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.Overflow;

import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailFormatter;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public abstract class ApplicationsTileGrid extends TileGrid {

    protected String tileName;

    public ApplicationsTileGrid(String tileName) {

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



        DetailViewerField imageField = new DetailViewerField("picture");
        imageField.setType("image");

        DetailViewerField commonNameField = new DetailViewerField("applicationName");


        DetailViewerField applicationVersion = new DetailViewerField("applicationVersion");
        commonNameField.setCanHilite(false);

        commonNameField.setDetailFormatter(new DetailFormatter() {
            public String format(Object value, Record record, DetailViewerField field) {

                String[] words = value.toString().split(" ");
                int length = words.length;
                int max = 18;
                String tile = new String();
                for (String s : words) {
                    int l = tile.length() + s.length() + 1;
                    if (l > max) {
                        tile += "<br>";
                        max += 18;
                        tile += s + " ";
                    } else {
                        tile += s + " ";
                    }
                }
                String[] wordss = tile.toString().split("<br>");
                if (wordss.length > 3) {
                    return wordss[0] + "<br>" + wordss[1] + "<br>" + wordss[2] + "<br>" + wordss[3];
                } else {
                    return tile;
                }

            }
        });


        this.setFields(imageField, commonNameField, applicationVersion);
        this.setData(new ApplicationTileRecord[]{});
       if( this.isElementSet==false){
           this.setHeight("100%");
       }
        this.redraw();




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
        //this.setHeight("100%");
        //test with redraw
       // redraw();
        
    }

    protected void addApplication(String applicationName, String version, String applicationImage) {

        addApplication(new ApplicationTileRecord(applicationName, version, applicationImage));
       //this.setHeight("100%");


    }

    protected void addApplication(ApplicationTileRecord record) {

        this.addData(record);
         //this.setHeight("100%");

    }

    public abstract void parse(String applicationName, String version);

    public String getTileName() {
        return tileName;
    }
}
