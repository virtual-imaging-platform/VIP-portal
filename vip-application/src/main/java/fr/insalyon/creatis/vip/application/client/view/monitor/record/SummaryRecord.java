package fr.insalyon.creatis.vip.application.client.view.monitor.record;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Rafael Silva
 */
public class SummaryRecord extends ListGridRecord {

    public SummaryRecord() {
    }
    
    public SummaryRecord(String state, int jobs) {
        
        setState(state);
        setJobs(jobs);
    }
    
    public void setState(String state) {
        setAttribute("states", state);
    }
    
    public String getState() {
        return getAttributeAsString("states");
    }
    
    public void setJobs(int jobs) {
        setAttribute("jobs", jobs);
    }
    
    public String getJobs() {
        return getAttributeAsString("jobs");
    }
}
