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
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Silva
 */
public class SignUpTab extends Tab {

    private ModalWindow modal;
    private VLayout signupLayout;
    private TextItem firstNameField;
    private TextItem lastNameField;
    private TextItem emailField;
    private TextItem confirmEmailField;
    private TextItem institutionField;
    private TextItem phoneField;
    private PasswordItem passwordField;
    private PasswordItem confirmPasswordField;
    private RadioGroupItem accountRadioGroupItem;
    private TextAreaItem commentsItem;
    private CheckboxItem acceptField;
    private IButton signupButton;

    public SignUpTab() {

        this.setID(CoreConstants.TAB_SIGNUP);
        this.setTitle("Sign Up");
        this.setCanClose(true);

        VLayout vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        modal = new ModalWindow(vLayout);

        configureSignupLayout();
        vLayout.addMember(signupLayout);

        this.setPane(vLayout);
    }

    private void configureSignupLayout() {

        firstNameField = FieldUtil.getTextItem(300, false, "", null);
        lastNameField = FieldUtil.getTextItem(300, false, "", null);

        emailField = FieldUtil.getTextItem(300, false, "", "[a-zA-Z0-9_.\\-+@]");
        emailField.setName("email");
        confirmEmailField = FieldUtil.getTextItem(300, false, "", "[a-zA-Z0-9_.\\-+@]");

        emailField.setValidators(ValidatorUtil.getEmailValidator());
        confirmEmailField.setValidators(ValidatorUtil.getEmailValidator());

        institutionField = FieldUtil.getTextItem(300, false, "", null);
        phoneField = FieldUtil.getTextItem(150, false, "", "[0-9\\(\\)\\-+. ]");

        passwordField = FieldUtil.getPasswordItem(150, 32);
        confirmPasswordField = FieldUtil.getPasswordItem(150, 32);

        accountRadioGroupItem = new RadioGroupItem();
        accountRadioGroupItem.setShowTitle(false);
        accountRadioGroupItem.setVertical(false);
        accountRadioGroupItem.setValueMap(CoreModule.accountTypes.toArray(new String[]{}));
        accountRadioGroupItem.setRequired(true);
        accountRadioGroupItem.setWidth(300);

        commentsItem = new TextAreaItem("comment", "");
        commentsItem.setHeight(80);
        commentsItem.setWidth(300);
        commentsItem.setShowTitle(false);

        acceptField = new CheckboxItem("acceptTerms", "I accept the <a href=\"documentation/terms.html\">terms of use</a>.");
        acceptField.setRequired(true);
        acceptField.setWidth(300);
        acceptField.setAlign(Alignment.LEFT);

        signupButton = new IButton("Sign Up");
        signupButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                if (firstNameField.validate() & lastNameField.validate()
                        & emailField.validate() & confirmEmailField.validate()
                        & institutionField.validate() & phoneField.validate()
                        & passwordField.validate() & confirmPasswordField.validate()
                        & accountRadioGroupItem.validate() & acceptField.validate()
                        & acceptField.getValueAsBoolean()) {

                    if (!emailField.getValueAsString().equals(confirmEmailField.getValueAsString())) {
                        SC.warn("E-mails do not match. Please verify the entered e-mail.");
                        emailField.focusInItem();
                        return;
                    }

                    if (!passwordField.getValueAsString().equals(confirmPasswordField.getValueAsString())) {
                        SC.warn("Passwords do not match. Please verify the entered password.");
                        passwordField.focusInItem();
                        return;
                    }

                    User user = new User(
                            firstNameField.getValueAsString().trim(),
                            lastNameField.getValueAsString().trim(),
                            emailField.getValueAsString().trim(),
                            institutionField.getValueAsString().trim(),
                            passwordField.getValueAsString(),
                            phoneField.getValueAsString().trim());

                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Unable to signing up:<br />" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            modal.hide();
                            SC.say("Your membership request was successfully processed.\n"
                                    + "An activation code was sent to your email.\n"
                                    + "This code will be requested on your first login.");
                            signin();
                        }
                    };
                    modal.show("Signing up...", true);
                    service.signup(user, commentsItem.getValueAsString(),
                            accountRadioGroupItem.getValueAsString(), callback);
                }
            }
        });

        signupLayout = WidgetUtil.getVIPLayout(320);
        addField("First Name", firstNameField);
        addField("Last Name", lastNameField);
        addField("E-mail", emailField);
        addField("Re-enter E-mail", confirmEmailField);
        addField("Institution", institutionField);
        addField("Phone", phoneField);
        addField("Password", passwordField);
        addField("Re-enter Password", confirmPasswordField);
        addField("Account Type", accountRadioGroupItem);
        addField("Comments", commentsItem);
        signupLayout.addMember(FieldUtil.getForm(acceptField));
        signupLayout.addMember(signupButton);
    }
    
    private void addField(String title, FormItem item) {
        
        signupLayout.addMember(WidgetUtil.getLabel("<b>" + title + "</b>", 15));
        signupLayout.addMember(FieldUtil.getForm(item));
    }

    private void signin() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<User> callback = new AsyncCallback<User>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                if (caught.getMessage().contains("Authentication failed")) {
                    SC.warn("The username or password you entered is incorrect.");
                } else {
                    SC.warn("Unable to signing in:\n" + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(User result) {
                modal.hide();
                Modules.getInstance().parseAccountType(accountRadioGroupItem.getValueAsString());
                Layout.getInstance().removeTab(CoreConstants.TAB_SIGNIN);
                Layout.getInstance().removeTab(CoreConstants.TAB_SIGNUP);

                Cookies.setCookie(CoreConstants.COOKIES_USER,
                        result.getEmail(), CoreConstants.COOKIES_EXPIRATION_DATE,
                        null, "/", false);
                Cookies.setCookie(CoreConstants.COOKIES_SESSION,
                        result.getSession(), CoreConstants.COOKIES_EXPIRATION_DATE,
                        null, "/", false);

                Layout.getInstance().authenticate(result);
            }
        };
        modal.show("Signing in...", true);
        service.signin(emailField.getValueAsString().trim(),
                passwordField.getValueAsString(), callback);
    }
}
