/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import fr.insalyon.creatis.vip.common.client.view.property.AbstractPropertyWindow;
import fr.insalyon.creatis.vip.common.client.view.property.PropertyRecord;
import fr.insalyon.creatis.vip.portal.client.bean.Node;
import fr.insalyon.creatis.vip.portal.client.rpc.JobService;
import fr.insalyon.creatis.vip.portal.client.rpc.JobServiceAsync;

/**
 *
 * @author Rafael Silva
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

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get node: " + caught.getMessage());
            }

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
