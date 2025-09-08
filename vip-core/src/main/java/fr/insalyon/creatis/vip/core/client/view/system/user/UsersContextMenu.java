package fr.insalyon.creatis.vip.core.client.view.system.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class UsersContextMenu extends Menu {

    private ModalWindow modal;
    private String email;

    public UsersContextMenu(ModalWindow modal, String email, boolean confirmed) {

        this.modal = modal;
        this.email = email;
        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem activateItem = new MenuItem("Activate");
        activateItem.setIcon(CoreConstants.ICON_ACTIVE);
        activateItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                activate();
            }
        });

        MenuItem removeItem = new MenuItem("Delete");
        removeItem.setIcon(CoreConstants.ICON_DELETE);
        removeItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                remove();
            }
        });

        if (confirmed) {
            this.setItems(removeItem);
        } else {
            this.setItems(activateItem, removeItem);
        }
    }

    private void activate() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to activate user:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                ((ManageUsersTab) Layout.getInstance().
                        getTab(CoreConstants.TAB_MANAGE_USERS)).loadUsers();
            }
        };
        modal.show("Activating user '" + email + "'...", true);
        service.activateUser(email, callback);
    }

    private void remove() {

        SC.confirm("Do you really want to remove the user \""
                + email + "\"?", new BooleanCallback() {

            @Override
            public void execute(Boolean value) {

                if (value != null && value) {
                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<User> callback = new AsyncCallback<User>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Unable to remove user:<br />" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(User result) {
                            modal.hide();
                            Modules.getInstance().userRemoved(result);
                            ((ManageUsersTab) Layout.getInstance().
                                    getTab(CoreConstants.TAB_MANAGE_USERS)).loadUsers();
                        }
                    };
                    modal.show("Deleting user '" + email + "'...", true);
                    service.removeUser(email, callback);
                }
            }
        });
    }
}
