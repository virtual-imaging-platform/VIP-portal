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
package fr.insalyon.creatis.vip.core.client.view.auth;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
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
import com.smartgwt.client.widgets.layout.HLayout;
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
    private Label infoVipLayout;
    private Label infoContactlayout;
    private Label infoPublicationLayout;
    private Label infoCodeSource;
    private Label infoContactus;
    private DynamicForm newForm;
    private TextItem emailField;
    private PasswordItem passwordField;
    private CheckboxItem remembermeField;
    private IButton signinButton;

    public SignInTab() {

        HLayout hLayout = new HLayout(5);
        hLayout.setWidth100();
        hLayout.setHeight100();
        hLayout.setOverflow(Overflow.AUTO);
        hLayout.setPadding(10);

        this.setID(CoreConstants.TAB_SIGNIN);
        this.setTitle("Sign In");

        VLayout loginVLayout = new VLayout(10);
        //loginVLayout.setShowEdges(true);
        //loginVLayout.setEdgeShowCenter(true);
        loginVLayout.setWidth100();
        loginVLayout.setHeight100();
        loginVLayout.setMargin(170);
        loginVLayout.setOverflow(Overflow.AUTO);
        loginVLayout.setAlign(Alignment.CENTER);
        loginVLayout.setDefaultLayoutAlign(Alignment.LEFT);

        VLayout infoVLayout = new VLayout(10);
        infoVLayout.setShowEdges(true);
        infoVLayout.setEdgeSize(1);
        infoVLayout.setEdgeOffset(10);
        infoVLayout.setEdgeMarginSize(100);
        infoVLayout.setWidth100();
        infoVLayout.setHeight100();
        infoVLayout.setOverflow(Overflow.AUTO);
        infoVLayout.setAlign(Alignment.CENTER);
        infoVLayout.setDefaultLayoutAlign(Alignment.RIGHT);

        configureNewForm();
        configureSigninLayout();
        testLayoutInfo();
        loginVLayout.addMember(signinLayout);
        loginVLayout.addMember(newForm);
        infoVLayout.addMember(infoVipLayout);
        infoVLayout.addMember(infoContactlayout);
        infoVLayout.addMember(infoPublicationLayout);
        infoVLayout.addMember(infoCodeSource);
        infoVLayout.addMember(infoContactus);

        hLayout.addMember(loginVLayout);
        hLayout.addMember(infoVLayout);

        this.setPane(hLayout);
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
                Layout.getInstance().addTab(
                    CoreConstants.TAB_SIGNUP, SignUpTab::new);
            }
        });

        LinkItem recoverAccount = FieldUtil.getLinkItem("link_reset", "Forgot your password?",
                new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                Layout.getInstance().addTab(
                    CoreConstants.TAB_RECOVERY, RecoveryTab::new);
            }
        });

        LinkItem egiAccount = FieldUtil.getLinkItem("link_redirection", "Connection with EGI Check-in.",
                new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        Window.Location.assign("/oauth2/authorize-client/egi");
                    }
                });

        newForm = FieldUtil.getForm(createAccount, recoverAccount, egiAccount);
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
                        Cookies.removeCookie(CoreConstants.COOKIES_USER, "/");
                        Cookies.removeCookie(CoreConstants.COOKIES_SESSION, "/");
                    }

                    Layout.getInstance().authenticate(result);
                }
            };
            service.signin(emailField.getValueAsString().trim(),
                    passwordField.getValueAsString(), callback);
            WidgetUtil.setLoadingIButton(signinButton, "Signing in...");
        }
    }

    private void testLayoutInfo(){
        Canvas canvas = new Canvas();
        String image = "crystal/128/apps/jabber_protocol.png";

        Img starImg3 = new Img(image, 240, 137);
        starImg3.setImageType(ImageStyle.STRETCH);
        starImg3.setBorder("1px solid gray");
        starImg3.setLeft(600);
        canvas.addChild(starImg3);

        infoVipLayout= WidgetUtil.getLabel("The Virtual Imaging Platform (VIP) is a web portal for medical simulation and image data analysis. It leverages resources available in the EGI biomed Virtual Organisation to offer an open service to academic researchers worldwide. In March 2022, VIP counts 1380 registered users and about 20 applications publicly available. Since its early beginnings in 2011, VIP has facilited (i) the access to distributed computing resources, (ii) the access to scientific applications available as a service and (iii) application sharing worldwide. In the last few years, VIP has addressed interoperability and reproducibility concerns, in the larger scope of a FAIR (Findable, Accessible, Interoperable, Reusable) approach to scientific data analysis.", 30);
        infoContactlayout = WidgetUtil.getLabel("Documentation of Virtual Imaging Platform and its embedded applications is available here :  <a href=\"https://vip.creatis.insa-lyon.fr/documentation/\">Documentation VIP</a>",20);
        infoPublicationLayout = WidgetUtil.getLabel("The list of all publications related to VIP is here :  <a href=\"https://www.creatis.insa-lyon.fr/vip/more-publications.html\">Publications VIP</a>",20);
        infoCodeSource = WidgetUtil.getLabel("Virtual Imaging Platform source code :  <a href=\"https://github.com/virtual-imaging-platform\">Github VIP</a>",20);
        infoContactus = WidgetUtil.getLabel("Contact us : vip-support@creatis.insa-lyon.fr",20);

        canvas.draw();

    }
}
