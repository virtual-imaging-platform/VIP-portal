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
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Silva
 */
public class RemoveAccountLayout extends AbstractFormLayout {

    private CheckboxItem confirmField;
    private IButton removeButton;

    public RemoveAccountLayout() {

        super(300, 120);
        addTitle("Delete Account", CoreConstants.ICON_ACCOUNT_REMOVE);

        configure();
    }

    private void configure() {

        this.addMember(WidgetUtil.getLabel("Removing your account will remove "
                + "all your personal data and simulations", 20));

        confirmField = new CheckboxItem("confirm", "<font color=\"#808080\">Yes, I want to delete my account</font>");
        confirmField.setRequired(true);
        confirmField.setWidth(300);
        confirmField.setAlign(Alignment.LEFT);
        
        this.addMember(FieldUtil.getForm(confirmField));

        removeButton = new IButton("Delete VIP Account");
        removeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                if (confirmField.validate() && confirmField.getValueAsBoolean()) {
                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<User> callback = new AsyncCallback<User>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.say("Unable to delete account:<br />" + caught.getMessage());
                        }

                        @Override
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

        this.addMember(removeButton);
    }
}
