package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.property.AbstractPropertyWindow;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class NodeInfoWindow extends AbstractPropertyWindow {

    private String simulationID;
    private String siteName;
    private String nodeName;

    public NodeInfoWindow(String simulationID, String jobID, String siteName, String nodeName) {

        super("Node Information for Job ID " + jobID, 550, 240);
        this.simulationID = simulationID;
        this.siteName = siteName;
        this.nodeName = nodeName;
        loadData();
    }

    private void loadData() {
        JobServiceAsync service = JobService.Util.getInstance();
        final AsyncCallback<Node> callback = new AsyncCallback<Node>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get node information:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Node result) {
                PropertyRecord[] data;
                if (result != null) {
                    data = new PropertyRecord[]{
                        new PropertyRecord("Site Name", result.getSite()),
                        new PropertyRecord("Node Name", result.getNode_name()),
                        new PropertyRecord("Numbers of CPUs", result.getNcpus() + ""),
                        new PropertyRecord("CPU Model Name", result.getCpu_model_name()),
                        new PropertyRecord("CPU Mhz", result.getCpu_mhz() + ""),
                        new PropertyRecord("CPU Cache Size", (result.getCpu_cache_size() / 1024) + " MB"),
                        new PropertyRecord("CPU Bogomips", result.getCpu_bogomips() + ""),
                        new PropertyRecord("Total Memory", (result.getMem_total() / 1024 / 1024) + " GB")
                    };
                } else {
                    data = new PropertyRecord[]{
                        new PropertyRecord("Site Name", ""),
                        new PropertyRecord("Node Name", ""),
                        new PropertyRecord("Numbers of CPUs", ""),
                        new PropertyRecord("CPU Model Name", ""),
                        new PropertyRecord("CPU Mhz", ""),
                        new PropertyRecord("CPU Cache Size", ""),
                        new PropertyRecord("CPU Bogomips", ""),
                        new PropertyRecord("Total Memory", "")
                    };
                }
                grid.setData(data);
            }
        };
        service.getNode(simulationID, siteName, nodeName, callback);
    }
}
