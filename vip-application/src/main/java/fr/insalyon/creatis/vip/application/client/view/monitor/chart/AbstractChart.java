package fr.insalyon.creatis.vip.application.client.view.monitor.chart;

import com.smartgwt.client.types.Cursor;
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
        img.setPrompt("Row Data");
        img.setCursor(Cursor.HAND);
        img.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                
                new ViewerWindow("Row Data", "", rowData.toString()).show();
            }
        });
        
        return img;
    }
}
