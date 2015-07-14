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

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
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
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva, glatard
 */
public class SignInTab extends Tab {

    private VLayout signinLayout;
    private VLayout orLayout;
    private DynamicForm newForm;
    private TextItem emailField;
    private PasswordItem passwordField;
    private CheckboxItem remembermeField;
    private IButton signinButton;
    private Img personaImage;

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

        configureNewForm();
        configureSigninLayout();

        injectMozillaPersonaScripts();

        vLayout.addMember(personaImage);
        vLayout.addMember(orLayout);
        vLayout.addMember(signinLayout);
        vLayout.addMember(newForm);

        this.setPane(vLayout);
    }

    private void configureSigninLayout() {

        emailField = FieldUtil.getTextItem(230, false, "", "[a-zA-Z0-9_.\\-+@]");
        emailField.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    signin();
                }
            }
        });

        passwordField = new PasswordItem("password", "");
        passwordField.setWidth(230);
        passwordField.setLength(32);
        passwordField.setShowTitle(false);
        passwordField.setRequired(true);
        passwordField.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    signin();
                }
            }
        });

        remembermeField = new CheckboxItem("rememberme", "<font color=\"#808080\">Keep me logged in</font>");
        remembermeField.setWidth(230);
        remembermeField.setValue(true);
        remembermeField.setAlign(Alignment.LEFT);

        signinButton = new IButton("Sign in");
        signinButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                signin();
            }
        });

        personaImage = new Img(CoreConstants.ICON_MOZILLA_PERSONA, 205, 30);
        personaImage.setImageWidth(205);
        personaImage.setImageHeight(30);
        personaImage.setImageType(ImageStyle.CENTER);
        personaImage.setBorder("0px solid gray");
        personaImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                personaLogin();
            }
        });

        orLayout = new VLayout();
        orLayout.setWidth(250);
        orLayout.setHeight(70);
        orLayout.addMember(new Label("<center><b>or</b></center>"));

        signinLayout = WidgetUtil.getVIPLayout(250, 150);
        WidgetUtil.addFieldToVIPLayout(signinLayout, "Email", emailField);
        WidgetUtil.addFieldToVIPLayout(signinLayout, "Password", passwordField);
        signinLayout.addMember(FieldUtil.getForm(remembermeField));
        signinLayout.addMember(signinButton);
    }

    private void configureNewForm() {

        LinkItem createAccount = FieldUtil.getLinkItem("link_create", "Create an account.",
                new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                Layout.getInstance().addTab(new SignUpTab());
            }
        });

        LinkItem recoverAccount = FieldUtil.getLinkItem("link_reset", "Forgot your password?",
                new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                Layout.getInstance().addTab(new RecoveryTab());
            }
        });

        newForm = FieldUtil.getForm(createAccount, recoverAccount);
        newForm.setWidth(250);
    }

    private void signin() {

        if (emailField.validate() & passwordField.validate()) {

            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
            final AsyncCallback<User> callback = new AsyncCallback<User>() {
                @Override
                public void onFailure(Throwable caught) {
                    WidgetUtil.resetIButton(signinButton, "Sign in", null);
                    if (caught.getMessage().contains("Authentication failed")) {
                        Layout.getInstance().setWarningMessage("The username or password you entered is incorrect.", 10);
                    } else {
                        Layout.getInstance().setWarningMessage("Unable to signing in:\n" + caught.getMessage(), 10);
                    }
                }

                @Override
                public void onSuccess(User result) {
                    WidgetUtil.resetIButton(signinButton, "Sign in", null);
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
            service.signin(emailField.getValueAsString().trim(),
                    passwordField.getValueAsString(), callback);
            WidgetUtil.setLoadingIButton(signinButton, "Signing in...");
        }
    }

    private static native void personaLogin() /*-{
     loginPersona();
     }-*/;

    private void injectMozillaPersonaScripts() {
        //put that in CoreConstants
        final String personaUrl = "https://login.persona.org/include.js";
        final String localUrl = "/js/login-persona.js";
        injectScript(personaUrl);
        injectScript(localUrl);
    }

    private void injectScript(final String url) {
        ScriptInjector.fromUrl(url).setCallback(
                new Callback() {
            public void onFailure(Object reason) {
                Window.alert("Script load failed: " + url);
            }

            public void onSuccess(Object result) {
            }
        }).inject();
    }
}
