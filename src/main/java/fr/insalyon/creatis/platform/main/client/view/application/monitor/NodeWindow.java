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
package fr.insalyon.creatis.platform.main.client.view.application.monitor;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import fr.insalyon.creatis.platform.main.client.bean.Node;

/**
 *
 * @author Rafael Silva
 */
public class NodeWindow extends Window {

    public NodeWindow(Node node, String jobID) {

        this.setTitle("Node Information for Job ID " + jobID);
        this.setAutoScroll(true);
        this.setWidth(470);
        this.setHeight(260);
        this.setResizable(false);

        Object[][] data;
        if (node != null) {
            data = new Object[][]{
                        {"Site Name", node.getSite()},
                        {"Node Name", node.getNode_name()},
                        {"Numbers of CPUs", node.getNcpus()},
                        {"CPU Model Name", node.getCpu_model_name()},
                        {"CPU Mhz", node.getCpu_mhz()},
                        {"CPU Cache Size", (node.getCpu_cache_size() / 1024) + " MB"},
                        {"CPU Bogomips", node.getCpu_bogomips()},
                        {"Total Memory", (node.getMem_total() / 1024 / 1024) + " GB"}
                    };
        } else {
            data = new Object[][]{
                        {"Site Name", ""},
                        {"Node Name", ""},
                        {"Numbers of CPUs", ""},
                        {"CPU Model Name", ""},
                        {"CPU Mhz", ""},
                        {"CPU Cache Size", ""},
                        {"CPU Bogomips", ""},
                        {"Total Memory", ""}
                    };
        }

        MemoryProxy proxy = new MemoryProxy(data);
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("nodes-prop"),
                    new StringFieldDef("nodes-value")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        Store store = new Store(proxy, reader);
        store.load();

        ColumnConfig propsConfig = new ColumnConfig("Property", "nodes-prop", 148, true);
        ColumnConfig valueConfig = new ColumnConfig("Value", "nodes-value", 298, true);

        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{
                    propsConfig,
                    valueConfig
                });

        EditorGridPanel grid = new EditorGridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setWidth(450);

        this.add(grid);
        this.show();
    }
}
