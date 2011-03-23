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
package fr.insalyon.creatis.vip.datamanagement.client.view.panel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Ext;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.data.PagingMemoryProxy;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.datamanagement.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.TransferPoolServiceAsync;

/**
 *
 * @author Rafael Silva
 */
public class OperationDetailPanel extends Panel {

    private Store store;

    public OperationDetailPanel() {

        this.setId("dm-operation-detail-panel");
        this.setLayout(new FitLayout());
        this.setAutoScroll(true);
        this.setMargins(0, 0, 0, 0);
        this.setBorder(false);

        GridPanel grid = new GridPanel();
        grid.setFrame(true);
        grid.setStripeRows(true);
        grid.setMargins(0, 0, 0, 0);
        grid.setWidth(450);

        PagingMemoryProxy proxy = new PagingMemoryProxy(new Object[][]{new Object[]{}});
        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("dm-op-prop"),
                    new StringFieldDef("dm-op-value")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        store = new Store(proxy, reader);
        grid.setStore(store);

        ColumnModel columnModel = new ColumnModel(new BaseColumnConfig[]{
                    new ColumnConfig("Property", "dm-op-prop", 100, true),
                    new ColumnConfig("Value", "dm-op-value", 340, true)
                });        
        grid.setColumnModel(columnModel);

        this.add(grid);
    }

    public void loadData(String operationId) {
        TransferPoolServiceAsync service = TransferPoolService.Util.getInstance();
        AsyncCallback<PoolOperation> callback = new AsyncCallback<PoolOperation>() {

            public void onFailure(Throwable caught) {
                Ext.get("dm-operation-detail-panel").unmask();
                MessageBox.alert("Error", "Error executing get operation details: " + caught.getMessage());
            }

            public void onSuccess(PoolOperation result) {
                if (result != null) {
                    Object[][] data = new Object[][]{
                        {"Type", result.getType()},
                        {"Status", result.getStatus()},
                        {"Source", result.getSource()},
                        {"Destination", result.getDest()},
                        {"Date", result.getRegistration().toString()},
                        {"Owner", result.getUser()}};

                    PagingMemoryProxy proxy = new PagingMemoryProxy(data);
                    store.setDataProxy(proxy);
                    store.load();
                    store.commitChanges();

                } else {
                    MessageBox.alert("Error", "Unable to get operation details.");
                }
                Ext.get("dm-operation-detail-panel").unmask();
            }
        };
        service.getOperationById(operationId, Context.getInstance().getAuthentication().getProxyFileName(), callback);
        Ext.get("dm-operation-detail-panel").mask("Loading...");
    }
}
