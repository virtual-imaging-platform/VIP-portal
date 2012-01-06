/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
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
public class RemoveAccountWindow extends Window {

    private ModalWindow modal;
    private IButton removeButton;

    public RemoveAccountWindow() {

        this.setTitle(Canvas.imgHTML(CoreConstants.ICON_ACCOUNT_REMOVE) + " Delete Account");
        this.setWidth100();
        this.setHeight(80);
        this.setShowCloseButton(false);

        VLayout vLayout = new VLayout(15);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setPadding(10);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        modal = new ModalWindow(vLayout);

        configureButton();

        vLayout.addMember(removeButton);

        this.addItem(vLayout);
    }

    private void configureButton() {

        removeButton = new IButton("Delete Account");
        removeButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to delete your account?<br />"
                        + "Note: All data will be erased.", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value) {
                            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                            final AsyncCallback<User> callback = new AsyncCallback<User>() {

                                public void onFailure(Throwable caught) {
                                    modal.hide();
                                    SC.say("Unable to delete account:<br />" + caught.getMessage());
                                }

                                public void onSuccess(User result) {
                                    Modules.getInstance().userRemoved(result);
                                    modal.hide();
                                    Layout.getInstance().signout();
                                    SC.say("Your account was successfully deleted.");
                                }
                            };
                            service.removeUser(callback);
                            modal.show("Removing account...", true);
                        }
                    }
                });
            }
        });
    }
}
