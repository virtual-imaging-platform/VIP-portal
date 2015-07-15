/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
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
        activateItem.setIcon(CoreConstants.ICON_ACTIVATE);
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
