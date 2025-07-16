package fr.insalyon.creatis.vip.application.client.view.monitor.chart;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import java.util.List;

/**
 *
 * @author camarasu
 */
public class WorkflowStatsChart  extends AbstractChart {

    public WorkflowStatsChart(List<String> data, VLayout chartLayout, ListGrid grid) {
        super(data, chartLayout, grid);
    }

    public void build() {

        chartLayout.removeMembers(chartLayout.getMembers());

        // add data

                      PropertyRecord[] p=new PropertyRecord[data.size()];
                        for(int i=0; i< data.size(); i++){
                            if(data.get(i) != null){
                                addRowData(data.get(i));
                                String[] v = data.get(i).split("##");
                                p[i]=new PropertyRecord(v[0], v[1]);
                            }
                            
                        }

                        grid.setData(p);
                        //grid.setCursor(Cursor.TEXT);
                      
                        chartLayout.addMember(getRowDataImg());
    }
}