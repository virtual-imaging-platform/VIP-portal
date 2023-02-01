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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
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
    private Label infoWelcomeVipLayout;
    private Label infoSpace;
    private Label infoVipNews;
    private Label infoVipLayout;
    private Label infoVipLog;
    private Label infoContactlayout;
    private Label infoPrivacyPolicy;
    private Label infoCodeSource;
    private Label infoContactus;
    private Label infoMail;
    private DynamicForm newForm;
    private DynamicForm newFormAppLayout;
    private DynamicForm newFormPubliLayout;
    private TextItem emailField;
    private PasswordItem passwordField;
    private CheckboxItem remembermeField;
    private IButton signinButton;
    private IButton createAnAccountButton;
    private HTMLPane egiButton;

    public SignInTab() {

        this.setID(CoreConstants.TAB_SIGNIN);
        this.setTitle("Sign In");



        VLayout loginVLayout = new VLayout(15);
        loginVLayout.setWidth100();
        loginVLayout.setHeight100();
        loginVLayout.setLayoutTopMargin(1);
        loginVLayout.setOverflow(Overflow.AUTO);
        loginVLayout.setAlign(Alignment.CENTER);
        loginVLayout.setDefaultLayoutAlign(Alignment.CENTER);

        VLayout hautLayout = new VLayout(15);
        hautLayout.setWidth100();
        hautLayout.setHeight(200);
        hautLayout.setLayoutLeftMargin(100);
        hautLayout.setLayoutRightMargin(100);
        hautLayout.setOverflow(Overflow.AUTO);
        hautLayout.setAlign(Alignment.CENTER);
        hautLayout.setDefaultLayoutAlign(Alignment.CENTER);
        hautLayout.setCanSelectText(true);

        VLayout middleLayout = new VLayout(5);
        middleLayout.setWidth100();
        middleLayout.setHeight(360);
        middleLayout.setOverflow(Overflow.AUTO);
        middleLayout.setAlign(Alignment.CENTER);
        middleLayout.setDefaultLayoutAlign(Alignment.CENTER);
        middleLayout.setCanSelectText(true);

        VerticalPanel middlePanel = new VerticalPanel();
        middlePanel.setSize("10%", "5%");
        middlePanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        middlePanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

        VerticalPanel egiPanel = new VerticalPanel();
        egiPanel.setSpacing(5);
        egiPanel.setSize("10%", "5%");
        egiPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        egiPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

        VLayout basLayout = new VLayout(15);
        basLayout.setWidth100();
        basLayout.setHeight(100);
        basLayout.setLayoutLeftMargin(100);
        basLayout.setCanSelectText(true);

        configureSigninLayout();
        testLayoutInfo();
        hautLayout.addMember(infoWelcomeVipLayout);
        hautLayout.addMember(infoSpace);
        hautLayout.addMember(infoVipLayout);
        hautLayout.addMember(infoContactus);
        hautLayout.addMember(infoVipLog);
        hautLayout.addMember(infoContactlayout);
        loginVLayout.addMember(hautLayout);
        middlePanel.add(signinLayout);
        egiPanel.add(egiButton);
        middleLayout.addMember(middlePanel);
        middleLayout.addMember(egiPanel);
        loginVLayout.addMember(middleLayout);
        basLayout.addMember(infoContactlayout);
        basLayout.addMember(infoPrivacyPolicy);
        basLayout.addMember(newFormAppLayout);
        basLayout.addMember(newFormPubliLayout);
        basLayout.addMember(infoCodeSource);
        basLayout.addMember(infoVipNews);
        basLayout.addMember(infoMail);
        loginVLayout.addMember(basLayout);
        loginVLayout.setCanSelectText(true);

        this.setPane(loginVLayout);
    }

    private void configureSigninLayout() {

        emailField = FieldUtil.getTextItem(230, false, "", "[a-zA-Z0-9_.\\-+@]");
        emailField.addKeyPressHandler(event -> {
            if (event.getKeyName().equals("Enter")) {
                signin();
            }
        });

        passwordField = new PasswordItem("password", "");
        passwordField.setWidth(230);
        passwordField.setLength(32);
        passwordField.setShowTitle(false);
        passwordField.setRequired(true);
        passwordField.addKeyPressHandler(event -> {
            if (event.getKeyName().equals("Enter")) {
                signin();
            }
        });

        remembermeField = new CheckboxItem("rememberme", "<font color=\"#808080\">Keep me logged in</font>");
        remembermeField.setWidth(230);
        remembermeField.setValue(true);
        remembermeField.setAlign(Alignment.LEFT);

        signinButton = new IButton("Sign in");
        signinButton.addClickHandler(event -> signin());

        LinkItem recoverAccount = FieldUtil.getLinkItem("link_reset", "Forgot your password?",
                event -> Layout.getInstance().addTab(
                        CoreConstants.TAB_RECOVERY, RecoveryTab::new));

        createAnAccountButton = new IButton("Create an account");
        createAnAccountButton.addClickHandler(event -> Layout.getInstance().addTab(
                CoreConstants.TAB_SIGNUP, SignUpTab::new));

        HorizontalPanel siginPanel = new HorizontalPanel();
        siginPanel.setSpacing(10);
        siginPanel.setSize("10%", "10%");
        siginPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        siginPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

        newForm = FieldUtil.getForm(recoverAccount);

        signinLayout = WidgetUtil.getVIPLayout(250, 110);
        WidgetUtil.addFieldToVIPLayout(signinLayout, "Email", emailField);
        WidgetUtil.addFieldToVIPLayout(signinLayout, "Password", passwordField);
        signinLayout.addMember(newForm);
        signinLayout.addMember(FieldUtil.getForm(remembermeField));
        siginPanel.add(signinButton);
        siginPanel.add(createAnAccountButton);
        signinLayout.addMember(siginPanel);

        egiButton = new HTMLPane();
        // add html code to use egi checkid custom css
        // add a empty line with <br/> otherwise the top is cropped
        egiButton.setContents("<br /><a href=\"/oauth2/authorize-client/egi\" class=\"button-blue-border\">Sign up with EGI Check-in</a>");
        egiButton.setWidth(400);
        egiButton.setHeight(80);
        egiButton.setAlign(Alignment.CENTER);

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
        infoWelcomeVipLayout = WidgetUtil.getLabel("<font size=\"6\"><b>Welcome on VIP, the Virtual Imaging Platform!</b></font>",20);
        infoWelcomeVipLayout.setWidth(900);
        infoWelcomeVipLayout.setStyleName("title");
        infoWelcomeVipLayout.setCanSelectText(true);
        infoSpace = WidgetUtil.getLabel(" ",20);
        infoVipLayout= WidgetUtil.getLabel("<font size=\"3\"><b>VIP is a web portal for medical imaging applications. " +
                "It allows you to access scientific applications as a service (directly through your web browser with no installation required), " +
                "as well as distributed computing resources in a transparent manner.</b></font>", 20);
        infoVipLog = WidgetUtil.getLabel("<font size=\"3\"><b>Please log in using the form below or the link " +
                "to the EGI Checkin federated authentication service, or create a new account if you don't have one.</b></font>",20);
        infoContactlayout = WidgetUtil.getLabel("<font size=\"3\"><b>Documentation of the Virtual " +
                "Imaging Platform and its embedded applications is available here:  <a href=\"https://vip.creatis.insa-lyon.fr/documentation/\">" +
                "VIP Documentation</a></b></font>",20);
        infoPrivacyPolicy = WidgetUtil.getLabel("<font size=\"3\"><b>The Virtual Imaging Platform Privacy Policy: " +
                " <a href=\"https://vip.creatis.insa-lyon.fr/documentation/privacypolicy.html\">VIP Privacy Policy</a>",20);

        LinkItem infoAppLayout = new LinkItem();
        infoAppLayout.setLinkTitle("<font size=\"3\"><b>The list of Applications related to the Virtual Imaging Platform </b></font>");
        infoAppLayout.setShowTitle(false);
        infoAppLayout.addClickHandler((com.smartgwt.client.widgets.form.fields.events.ClickHandler) event -> {
            CoreModule.getHomePageActions().get(CoreConstants.HOME_ACTION_SHOW_APPLICATIONS).run();            
        });

        LinkItem infoPubliLayout = new LinkItem();
        infoPubliLayout.setLinkTitle("<font size=\"3\"><b>The list of Publications related to the Virtual Imaging Platform </b></font>");
        infoPubliLayout.setShowTitle(false);
        infoPubliLayout.addClickHandler((com.smartgwt.client.widgets.form.fields.events.ClickHandler) event -> {
            CoreModule.getHomePageActions().get(CoreConstants.HOME_ACTION_SHOW_PUBLICATIONS).run();
        });

        newFormPubliLayout = FieldUtil.getForm(infoPubliLayout);


        newFormAppLayout = FieldUtil.getForm(infoAppLayout);
        infoCodeSource = WidgetUtil.getLabel("<font size=\"3\"><b>The Virtual Imaging Platform source code: " +
                " <a href=\"https://github.com/virtual-imaging-platform\">VIP Github</a>",20);
        infoContactus = WidgetUtil.getLabel("<font size=\"3\"><b>This portal is exclusively dedicated to non-commercial academic use, " +
                "as indicated in the <a href=\"https://vip.creatis.insa-lyon.fr/documentation/terms.html\">terms of use.</a> " +
                "For commercial use, please contact us at <a href=\"mailto:vip-support@creatis.insa-lyon.fr\">" +
                "vip-support@creatis.insa-lyon.fr</a>.</b></font>",20);
        infoMail = WidgetUtil.getLabel("<font size=\"3\"><b>Contact: <a href=\"mailto:vip-support@creatis.insa-lyon.fr\">" +
                "vip-support@creatis.insa-lyon.fr</a></b></font>",20);
        infoVipNews = WidgetUtil.getLabel("<font size=\"3\"><b>VIP News: <a href=\"https://www.creatis.insa-lyon.fr/vip/news.html\">" +
                "https://www.creatis.insa-lyon.fr/vip/news.html</a>",20);

        infoMail.setCanSelectText(true);
        infoContactus.setCanSelectText(true);
    }
}
