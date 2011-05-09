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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.TransferPoolServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.DataRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class OperationLayout extends VLayout {

    private static OperationLayout instance;
    private ToolStrip toolStrip;
    private ListGrid grid;

    public static OperationLayout getInstance() {
        if (instance == null) {
            instance = new OperationLayout();
        }
        return instance;
    }

    private OperationLayout() {

        this.setWidth(300);
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);
        
        configureToolStrip();
        configureGrid();

        this.addMember(toolStrip);
        this.addMember(grid);
        
        loadData();
    }
    
    private void configureToolStrip() {
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        
        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon("icon-refresh.png");
        refreshButton.setPrompt("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                loadData();
            }
        });
        toolStrip.addButton(refreshButton);
    }
    
    private void configureGrid() {
        grid = new ListGrid();
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setCanAcceptDroppedRecords(true);
        
        ListGridField icoField = new ListGridField("icon", " ", 30);
        icoField.setAlign(Alignment.CENTER);
        icoField.setType(ListGridFieldType.IMAGE);
        icoField.setImageURLSuffix(".png");
        icoField.setImageWidth(12);
        icoField.setImageHeight(12);
        
        ListGridField statusField = new ListGridField("status", " ", 30);
        statusField.setAlign(Alignment.CENTER);
        statusField.setType(ListGridFieldType.IMAGE);
        statusField.setImageURLSuffix(".png");
        statusField.setImageWidth(12);
        statusField.setImageHeight(12);
        
        ListGridField nameField = new ListGridField("name", "Name");
        
        grid.setFields(icoField, statusField, nameField);
        
        grid.addRecordDropHandler(new RecordDropHandler() {

            public void onRecordDrop(RecordDropEvent event) {
                DataRecord[] records = (DataRecord[]) event.getDropRecords();
                String s = "";
                for (DataRecord data : records) {
                    s += data.getName() + "<br />";
                }
                SC.say(s);
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
                    for (PoolOperation o : result) {
                        dataList.add(new OperationRecord(o.getType(), o.getStatus(), o.getSource()));
                    }
                    grid.setData(dataList.toArray(new OperationRecord[]{}));
                    
                } else {
                    SC.warn("Unable to get list of operations.");
                }
            }
        };
        Context context = Context.getInstance();
        service.getOperations(context.getUserDN(), context.getProxyFileName(), callback);
    }
}
