package fr.insalyon.creatis.vip.datamanager.client.view.system.operation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ManageOperationsContextMenu extends Menu {

    public ManageOperationsContextMenu(final ModalWindow modal, final OperationRecord operation) {

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem clearItem = new MenuItem("Remove Operation");
        clearItem.setIcon(CoreConstants.ICON_CLEAR);
        clearItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you want to remove this operation?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value != null && value) {
                            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                            AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    modal.hide();
                                    Layout.getInstance().setWarningMessage("Unable to remove operation:<br />" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    modal.hide();
                                    ManageOperationsTab tab = (ManageOperationsTab) Layout.getInstance().getTab(DataManagerConstants.TAB_MANAGE_OPERATIONS);
                                    tab.loadData();
                                }
                            };
                            modal.show("Removing operation...", true);
                            service.removeOperationById(operation.getId(), callback);
                        }
                    }
                });
            }
        });
        this.setItems(clearItem);
    }
}
