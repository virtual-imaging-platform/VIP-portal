/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.bean.Account;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.ValidatorUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SignUpTab extends Tab {

    private VLayout signupLayout;
    private TextItem firstNameField;
    private TextItem lastNameField;
    private TextItem emailField;
    private TextItem confirmEmailField;
    private TextItem institutionField;
    private TextItem phoneField;
    private SelectItem countryField;
    private PasswordItem passwordField;
    private PasswordItem confirmPasswordField;
    private SelectItem accountTypeField;
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

        configureSignupLayout();
        vLayout.addMember(signupLayout);

        this.setPane(vLayout);
        
        loadAccountTypes();
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

        countryField = new SelectItem();
        countryField.setShowTitle(false);
        countryField.setValueMap(CountryCode.getCountriesMap());
        countryField.setValueIcons(CountryCode.getCodesMap());
        countryField.setImageURLPrefix(CoreConstants.FOLDER_FLAGS);
        countryField.setImageURLSuffix(".png");
        countryField.setRequired(true);

        passwordField = FieldUtil.getPasswordItem(150, 32);
        confirmPasswordField = FieldUtil.getPasswordItem(150, 32);

        accountTypeField = new SelectItem();
        accountTypeField.setShowTitle(false);
        accountTypeField.setRequired(true);

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
                        & countryField.validate()
                        & passwordField.validate() & confirmPasswordField.validate()
                        & accountTypeField.validate() & acceptField.validate()
                        & acceptField.getValueAsBoolean()) {

                    if (!emailField.getValueAsString().equals(confirmEmailField.getValueAsString())) {
                        Layout.getInstance().setWarningMessage("<b>E-mails</b> do not match. Please verify the entered e-mail.", 10);
                        emailField.focusInItem();
                        return;
                    }

                    if (!passwordField.getValueAsString().equals(confirmPasswordField.getValueAsString())) {
                        Layout.getInstance().setWarningMessage("<b>Passwords</b> do not match. Please verify the entered password.", 10);
                        passwordField.focusInItem();
                        return;
                    }

                    WidgetUtil.setLoadingIButton(signupButton, "Signing up...");
                    
                    User user = new User(
                            firstNameField.getValueAsString().trim(),
                            lastNameField.getValueAsString().trim(),
                            emailField.getValueAsString().trim(),
                            institutionField.getValueAsString().trim(),
                            passwordField.getValueAsString(),
                            phoneField.getValueAsString().trim(),
                            CountryCode.valueOf(countryField.getValueAsString()));

                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            WidgetUtil.resetIButton(signupButton, "Sign Up", null);
                            Layout.getInstance().setWarningMessage("Unable to signing up:<br />" + caught.getMessage(), 10);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            WidgetUtil.resetIButton(signupButton, "Sign Up", null);
                            Layout.getInstance().setNoticeMessage("Your membership request was successfully processed.<br />"
                                    + "An activation code was sent to your email.<br />"
                                    + "This code will be requested on your first login.", 15);
                            signin();
                        }
                    };
                    service.signup(user, commentsItem.getValueAsString(),
                            accountTypeField.getValueAsString(), callback);
                }
            }
        });

        signupLayout = WidgetUtil.getVIPLayout(320);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "First Name", firstNameField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "Last Name", lastNameField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "E-mail", emailField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "Re-enter E-mail", confirmEmailField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "Institution", institutionField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "Phone", phoneField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "Country", countryField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "Password", passwordField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "Re-enter Password", confirmPasswordField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "Account Type", accountTypeField);
        WidgetUtil.addFieldToVIPLayout(signupLayout, "Comments", commentsItem);
        signupLayout.addMember(FieldUtil.getForm(acceptField));
        signupLayout.addMember(signupButton);
    }

    private void signin() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<User> callback = new AsyncCallback<User>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught.getMessage().contains("Authentication failed")) {
                    Layout.getInstance().setWarningMessage("The username or password you entered is incorrect.", 10);
                } else {
                    Layout.getInstance().setWarningMessage("Unable to signing in:\n" + caught.getMessage(), 10);
                }
            }

            @Override
            public void onSuccess(User result) {
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
        service.signin(emailField.getValueAsString().trim(),
                passwordField.getValueAsString(), callback);
    }

    private void loadAccountTypes() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<Account>> callback = new AsyncCallback<List<Account>>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load account types:<br />" + caught.getMessage(), 10);
            }

            @Override
            public void onSuccess(List<Account> result) {
                
                String[] values = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    values[i] = result.get(i).getName();
                }
                accountTypeField.setValueMap(values);
            }
        };
        service.getAccounts(callback);
    }
}
