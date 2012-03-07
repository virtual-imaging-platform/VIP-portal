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
package fr.insalyon.creatis.vip.core.client.view.auth;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Silva
 */
public class ActivationTab extends Tab {

    private ModalWindow modal;
    private VLayout validateLayout;
    private DynamicForm resendForm;
    private TextItem codeField;
    private IButton validateButton;

    public ActivationTab() {

        this.setID(CoreConstants.TAB_ACTIVATION);
        this.setTitle("Account Activation");

        VLayout vLayout = new VLayout(10);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setAlign(Alignment.CENTER);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        modal = new ModalWindow(vLayout);

        configureValidateLayout();
        configureResendForm();

        vLayout.addMember(validateLayout);
        vLayout.addMember(resendForm);

        this.setPane(vLayout);
    }

    private void configureValidateLayout() {

        validateLayout = WidgetUtil.getVIPLayout(300, 120);

        codeField = FieldUtil.getTextItem(280, false, "", "[a-zA-Z0-9\\-]");

        validateButton = new IButton("Activate");
        validateButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (codeField.validate()) {

                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<User> callback = new AsyncCallback<User>() {

                        public void onFailure(Throwable caught) {
                            modal.hide();
                            if (caught.getMessage().contains("Activation failed")) {
                                SC.warn("Unable to activate account.<br />The code you entered is incorrect.");
                            } else {
                                SC.warn("Unable to activate account:<br />" + caught.getMessage());
                            }
                        }

                        public void onSuccess(User result) {
                            modal.hide();
                            Layout.getInstance().removeTab(CoreConstants.TAB_ACTIVATION);
                            Layout.getInstance().authenticate(result);
                        }
                    };
                    modal.show("Activating account...", true);
                    service.activate(codeField.getValueAsString().trim(), callback);
                }
            }
        });

        validateLayout.addMember(WidgetUtil.getLabel("<b>Activation Code</b>", 15));
        validateLayout.addMember(FieldUtil.getForm(codeField));
        validateLayout.addMember(validateButton);
    }

    private void configureResendForm() {

        LinkItem resendLink = new LinkItem("link");
        resendLink.setShowTitle(false);
        resendLink.setLinkTitle("Lost your code? Click here and we will resend it to you.");
        resendLink.setWidth(300);
        resendLink.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                final AsyncCallback<String> callback = new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                        modal.hide();
                        SC.warn("Unable to resend activation code:\n" + caught.getMessage());
                    }

                    public void onSuccess(String result) {
                        modal.hide();
                        SC.say("An activation code was sent to:\n" + result);
                    }
                };
                modal.show("Resending activation code...", true);
                service.sendActivationCode(callback);
            }
        });

        resendForm = FieldUtil.getForm(resendLink);
        resendForm.setWidth(300);
    }
}
