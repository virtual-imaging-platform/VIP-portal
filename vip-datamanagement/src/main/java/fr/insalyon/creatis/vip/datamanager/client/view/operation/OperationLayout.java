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
package fr.insalyon.creatis.vip.datamanager.client.view.operation;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class OperationLayout extends VLayout {

    private static OperationLayout instance;
    private Timer timer;
    private ModalWindow modal;
    private OperationToolStrip toolStrip;
    private ListGrid grid;

    public static OperationLayout getInstance() {
        if (instance == null) {
            instance = new OperationLayout();
        }
        return instance;
    }

    private OperationLayout() {

        this.setWidth(400);
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureGrid();

        modal = new ModalWindow(grid);
        toolStrip = new OperationToolStrip(modal);
        this.addMember(toolStrip);
        this.addMember(grid);

        loadData();

        timer = new Timer() {

            public void run() {
                loadData();
            }
        };
    }

    private void configureGrid() {
        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

        ListGridField iconField = FieldUtil.getIconGridField("typeIcon");
        ListGridField statusField = FieldUtil.getIconGridField("statusIcon");
        ListGridField nameField = new ListGridField("name", "Name");

        grid.setFields(iconField, statusField, nameField);

        grid.addCellContextClickHandler(new CellContextClickHandler() {

            public void onCellContextClick(CellContextClickEvent event) {
                event.cancel();
                if (event.getColNum() != 0) {
                    ListGridRecord record = event.getRecord();
                    new OperationContextMenu(modal, (OperationRecord) record).showContextMenu();
                }
            }
        });
    }

    public void loadData() {
        TransferPoolServiceAsync service = TransferPoolService.Util.getInstance();
        AsyncCallback<List<PoolOperation>> callback = new AsyncCallback<List<PoolOperation>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get files list: " + caught.getMessage());
            }

            public void onSuccess(List<PoolOperation> result) {

                List<OperationRecord> dataList = new ArrayList<OperationRecord>();
                if (result != null) {
                    boolean hasActiveOperations = false;
                    for (PoolOperation o : result) {
                        if (o.getStatus().equals("Running") || o.getStatus().equals("Queued")) {
                            hasActiveOperations = true;
                        }
                        dataList.add(new OperationRecord(o.getId(), o.getType(),
                                o.getStatus(), o.getSource(), o.getDest(),
                                o.getRegistration().toString(), o.getUser()));
                    }
                    grid.setData(dataList.toArray(new OperationRecord[]{}));
                    modal.hide();

                    if (!hasActiveOperations) {
                        timer.cancel();
                    }

                } else {
                    modal.hide();
                    SC.warn("Unable to get list of operations.");
                }
            }
        };
        modal.show("Loading operations...", true);
        Context context = Context.getInstance();
        service.getOperations(context.getUserDN(), context.getProxyFileName(), callback);
    }

    public ListGridRecord[] getGridSelection() {
        return grid.getSelection();
    }

    public void activateAutoRefresh() {
        timer.scheduleRepeating(15000);
    }
}
