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
package fr.insalyon.creatis.vip.application.client.view.monitor.chart;

import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.monitor.ViewerWindow;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractChart {

    protected List<String> data;
    protected VLayout chartLayout;
    protected ListGrid grid;
    protected StringBuilder rowData;

    public AbstractChart(List<String> data, VLayout chartLayout, ListGrid grid) {

        this.data = data;
        this.chartLayout = chartLayout;
        this.grid = grid;
        this.rowData = new StringBuilder();
    }
    
    /**
     * 
     * @param values 
     */
    protected void addRowData(String values) {
        
        rowData.append(values.replaceAll("##", ","));
        rowData.append("\n");
    }
    
    /**
     * 
     * @return 
     */
    protected Img getRowDataImg() {
        
        Img img = new Img(ApplicationConstants.APP_IMG_SIMULATION_OUT);
        img.setWidth(48);
        img.setHeight(48);
        img.setTitle("Row Data");
        img.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                
                new ViewerWindow("Row Data", "", rowData.toString()).show();
            }
        });
        
        return img;
    }
}
