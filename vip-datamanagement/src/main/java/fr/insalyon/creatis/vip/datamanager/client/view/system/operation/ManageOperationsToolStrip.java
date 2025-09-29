package fr.insalyon.creatis.vip.datamanager.client.view.system.operation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ManageOperationsToolStrip extends ToolStrip {

    public ManageOperationsToolStrip(final ModalWindow modal) {

        this.setWidth100();

        ToolStripButton refreshButton = new ToolStripButton("Refresh");
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ManageOperationsTab tab = (ManageOperationsTab) Layout.getInstance().getTab(DataManagerConstants.TAB_MANAGE_OPERATIONS);
                tab.loadData();
            }
        });
        this.addButton(refreshButton);

        ToolStripButton clearSelectedOperations = new ToolStripButton("Remove Selected Operations");
        clearSelectedOperations.setIcon(CoreConstants.ICON_CLEAR);
        clearSelectedOperations.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.confirm("Do you really want to remove all selected operations?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value != null && value) {
                            final ManageOperationsTab tab = (ManageOperationsTab) Layout.getInstance().getTab(DataManagerConstants.TAB_MANAGE_OPERATIONS);
                            List<String> ids = new ArrayList<String>();

                            for (ListGridRecord record : tab.getGridSelection()) {
                                OperationRecord op = (OperationRecord) record;
                                ids.add(op.getId());
                            }

                            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                            AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    modal.hide();
                                    Layout.getInstance().setWarningMessage("Unable to remove operations:<br />" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    modal.hide();
                                    tab.loadData();
                                }
                            };
                            modal.show("Removing operations...", true);
                            service.removeOperations(ids, callback);
                        }
                    }
                });
            }
        });
        this.addButton(clearSelectedOperations);
    }
}
