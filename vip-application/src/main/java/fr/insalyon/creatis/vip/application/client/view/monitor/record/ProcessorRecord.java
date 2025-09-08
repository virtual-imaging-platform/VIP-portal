package fr.insalyon.creatis.vip.application.client.view.monitor.record;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProcessorStatus;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ProcessorRecord extends ListGridRecord {

    private final String ATT_NAME = "name";
    private final String ATT_STATUS = "status";
    private final String ATT_COMPLETED = "completed";
    private final String ATT_QUEUED = "queued";
    private final String ATT_FAILED = "failed";
    
    public ProcessorRecord() {
    }

    public ProcessorRecord(String name, ProcessorStatus status,
            int completed, int queued, int failed) {

        setAttribute(ATT_NAME, name);
        setAttribute(ATT_STATUS, status.name());
        setAttribute(ATT_COMPLETED, completed);
        setAttribute(ATT_QUEUED, queued);
        setAttribute(ATT_FAILED, failed);
    }

    public String getName() {
        return getAttributeAsString(ATT_NAME);
    }

    public ProcessorStatus getStatus() {
        return ProcessorStatus.valueOf(getAttributeAsString(ATT_STATUS));
    }

    public int getCompleted() {
        return getAttributeAsInt(ATT_COMPLETED);
    }

    public int getQueued() {
        return getAttributeAsInt(ATT_QUEUED);
    }

    public int getFailed() {
        return getAttributeAsInt(ATT_FAILED);
    }
}
