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

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;

/**
 *
 * @author Rafael Silva
 */
public class SignInTab extends Tab {

    private ModalWindow modal;
    private DynamicForm signinForm;
    private DynamicForm newForm;
    private TextItem emailField;
    private PasswordItem passwordField;
    private CheckboxItem remembermeField;
    private IButton signinButton;

    public SignInTab() {

        this.setID(CoreConstants.TAB_SIGNIN);
        this.setTitle("Sign In");

        VLayout vLayout = new VLayout(10);
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setAlign(Alignment.CENTER);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        modal = new ModalWindow(vLayout);

        configureSigninForm();
        configureSigninButton();
        configureNewForm();

        vLayout.addMember(signinForm);
        vLayout.addMember(signinButton);
        vLayout.addMember(newForm);

        this.setPane(vLayout);
    }

    private void configureSigninForm() {

        emailField = FieldUtil.getTextItem(300, true, "Your Email", "[a-zA-Z0-9_.\\-+@]");
        emailField.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    signin();
                }
            }
        });

        passwordField = new PasswordItem("password", "Password");
        passwordField.setWidth(150);
        passwordField.setLength(32);
        passwordField.setRequired(true);
        passwordField.addKeyPressHandler(new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    signin();
                }
            }
        });
        
        remembermeField = new CheckboxItem("rememberme", "Keep me logged in");
        remembermeField.setWidth(150);
        remembermeField.setValue(false);

        signinForm = FieldUtil.getForm(emailField, passwordField, remembermeField);
        signinForm.setWidth(500);
        signinForm.setTitleWidth(150);
        signinForm.setBorder("1px solid #F6F6F6");
        signinForm.setBackgroundColor("#EBEEFF");
        signinForm.setPadding(5);
    }

    private void configureSigninButton() {

        signinButton = new IButton("Sign in");
        signinButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                signin();
            }
        });
    }

    private void configureNewForm() {

        LinkItem createAccount = new LinkItem("link");
        createAccount.setShowTitle(false);
        createAccount.setLinkTitle("New to VIP? Create an account.");
        createAccount.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                Layout.getInstance().addTab(new SignUpTab());
            }
        });

        newForm = FieldUtil.getForm(createAccount);
        newForm.setWidth(500);
    }

    private void signin() {
        
        if (signinForm.validate()) {

            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
            final AsyncCallback<User> callback = new AsyncCallback<User>() {

                public void onFailure(Throwable caught) {
                    modal.hide();
                    if (caught.getMessage().contains("Authentication failed")) {
                        SC.warn("The username or password you entered is incorrect.");
                    } else {
                        SC.warn("Unable to signing in:\n" + caught.getMessage());
                    }
                }

                public void onSuccess(User result) {
                    modal.hide();
                    Layout.getInstance().removeTab(CoreConstants.TAB_SIGNIN);
                    Layout.getInstance().removeTab(CoreConstants.TAB_SIGNUP);
                    
                    if (remembermeField.getValueAsBoolean()) {
                        
                        Cookies.setCookie(CoreConstants.COOKIES_USER, 
                                result.getEmail(), CoreConstants.COOKIES_EXPIRATION_DATE,
                                null, "/", false);
                        Cookies.setCookie(CoreConstants.COOKIES_SESSION, 
                                result.getSession(), CoreConstants.COOKIES_EXPIRATION_DATE,
                                null, "/", false);
                    
                    } else {
                        
                        Cookies.setCookie(CoreConstants.COOKIES_USER, 
                                null, CoreConstants.COOKIES_EXPIRATION_DATE,
                                null, "/", false);
                        Cookies.setCookie(CoreConstants.COOKIES_SESSION, 
                                null, CoreConstants.COOKIES_EXPIRATION_DATE,
                                null, "/", false);
                    }
                    
                    Layout.getInstance().authenticate(result);
                }
            };
            modal.show("Signing in...", true);
            service.signin(emailField.getValueAsString().trim(),
                    passwordField.getValueAsString(), callback);
        }
    }
}
