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

import com.google.gwt.user.client.DOM;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtextux.client.data.PagingMemoryProxy;
import fr.insalyon.creatis.vip.datamanagement.client.bean.PoolOperation;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractOperationPanel extends Panel {

    private String operationId;
    protected Store store;
    protected Menu menu;

    public AbstractOperationPanel(String id, String title) {

        this.setId(id);
        this.setTitle(title);
        this.setWidth(300);
        this.setLayout(new FitLayout());
        this.setMargins(0, 0, 0, 0);
        this.setBorder(false);
        this.add(getGrid());
    }

    private GridPanel getGrid() {

        GridPanel grid = new GridPanel();
        grid.setFrame(true);
        grid.setStripeRows(true);
        grid.setMargins(0, 0, 0, 0);

        CheckboxSelectionModel cbSelectionModel = new CheckboxSelectionModel();

        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("typeico"),
                    new StringFieldDef("fileName"),
                    new StringFieldDef("operationid")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        PagingMemoryProxy proxy = new PagingMemoryProxy(new Object[][]{new Object[]{}});

        store = new Store(proxy, reader);
        store.setSortInfo(new SortState("typeico", SortDir.DESC));
        grid.setStore(store);

        grid.addGridRowListener(new GridRowListenerAdapter() {

            @Override
            public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                Record record = grid.getStore().getRecordAt(rowIndex);
                operationId = record.getAsString("operationid");
                showMenu(e);
            }

            @Override
            public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
                DOM.eventPreventDefault(e.getBrowserEvent());
                onRowClick(grid, rowIndex, e);
            }
        });

        ColumnConfig idColumn = new ColumnConfig("ID", "operationid", 30);
        idColumn.setHidden(true);

        BaseColumnConfig[] columns = {
            new CheckboxColumnConfig(cbSelectionModel),
            getIcoTypeColumnConfig(),
            new ColumnConfig("File Name", "fileName", 235),
            idColumn};
        ColumnModel columnModel = new ColumnModel(columns);
        grid.setColumnModel(columnModel);
        grid.setSelectionModel(cbSelectionModel);

        return grid;
    }

    /**
     *
     * @param operations
     */
    public void loadData(List<PoolOperation> operations) {
        Object[][] data = new Object[operations.size()][3];
        int i = 0;
        for (PoolOperation op : operations) {
            data[i][0] = op.getStatus();
            data[i][1] = op.getSource();
            data[i][2] = op.getId();
            i++;
        }
        PagingMemoryProxy proxy = new PagingMemoryProxy(data);
        store.setDataProxy(proxy);
        store.load();
        store.commitChanges();
    }

    /**
     *
     * @return
     */
    protected ColumnConfig getIcoTypeColumnConfig() {
        ColumnConfig icoColumn = new ColumnConfig("", "typeico", 25);
        icoColumn.setRenderer(new Renderer() {

            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex,
                    int colNum, Store store) {
                String status = ((String) value);
                String image = "icon-queued.png";
                if (status.equals("Running")) {
                    image = "icon-running.png";
                } else if (status.equals("Done")) {
                    image = "icon-done.png";
                } else if (status.equals("Failed")) {
                    image = "icon-failed.png";
                } else if (status.equals("Rescheduled")) {
                    image = "icon-rescheduled.png";
                }
                return "<img src=\"./images/" + image + "\" style=\"border: 0\"/>";
            }
        });
        return icoColumn;
    }

    protected abstract void showMenu(EventObject e);

    public String getOperationId() {
        return operationId;
    }
}
