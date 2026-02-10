package fr.insalyon.creatis.vip.core.client.view.layout;

import java.util.function.Supplier;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.MessageWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.toolstrip.MainToolStrip;
import fr.insalyon.creatis.vip.core.client.view.user.ActivationTab;
import fr.insalyon.creatis.vip.core.models.User;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Layout {

    private static Layout instance;
    private VLayout vLayout;
    private CenterTabSet centerTabSet;
    private SectionStack mainSectionStack;
    private ModalWindow modal;
    private MessageWindow messageWindow;

    public static Layout getInstance() {
        if (instance == null) {
            instance = new Layout();
        }
        return instance;
    }

    private Layout() {

        vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();

        messageWindow = new MessageWindow(vLayout);

        vLayout.addMember(MainToolStrip.getInstance());

        mainSectionStack = new SectionStack();
        mainSectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        mainSectionStack.setAnimateSections(true);
        mainSectionStack.setCanResizeSections(true);

        SectionStackSection mainTabSection = new SectionStackSection();
        mainTabSection.setCanCollapse(false);
        mainTabSection.setShowHeader(false);

        centerTabSet = CenterTabSet.getInstance();
        mainTabSection.addItem(centerTabSet);

        mainSectionStack.setSections(mainTabSection);

        vLayout.addMember(mainSectionStack);

        modal = new ModalWindow(vLayout);

        vLayout.draw();
    }

    public Canvas getLayoutCanvas() {
        return vLayout;
    }

    public ModalWindow getModal() {
        return modal;
    }

    /**
     * Authenticates a user.
     *
     */
    public void authenticate(User user) {
        if (user != null && Cookies.isCookieEnabled()) {
            if (user.isConfirmed()) {
                Modules.getInstance().initializeModules(user);
            } else {
                addTab(CoreConstants.TAB_ACTIVATION, ActivationTab::new);
            }
        } else {
            if (Cookies.isCookieEnabled()) {
                // delete cookies to not lock the user in case they are wrong
                Cookies.removeCookie(CoreConstants.COOKIES_USER, "/");
                Cookies.removeCookie(CoreConstants.COOKIES_SESSION, "/");
            } else {
                setWarningMessage(
                        "Unable to sign in: cookies must be enabled.");
            }
            Window.Location.assign("home.html");
        }
    }

    /**
     * Signs out.
     *
     */
    public void signout() {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                setWarningMessage("Error while signing out:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {

                Cookies.removeCookie(CoreConstants.COOKIES_USER, "/");
                Cookies.removeCookie(CoreConstants.COOKIES_SESSION, "/");

                Window.Location.replace("index.html");
            }
        };
        ConfigurationService.Util.getInstance().signout(callback);
    }

    public Tab addTab(String id, Supplier<Tab> factory) {
        Tab tab = centerTabSet.getTab(id);
        if (tab == null) {
            tab = factory.get();
            centerTabSet.addTab(tab);
        }
        centerTabSet.selectTab(id);
        return tab;
    }

    public void setActiveCenterTab(String id) {
        centerTabSet.selectTab(id);
    }

    public Tab getTab(String id) {
        return centerTabSet.getTab(id);
    }

    public void addMainSection(SectionStackSection section) {
        mainSectionStack.addSection(section);
    }

    public void removeMainSection(String sectionID) {
        mainSectionStack.removeSection(sectionID);
    }

    public SectionStackSection getMainSection(String sectionID) {
        return mainSectionStack.getSection(sectionID);
    }

    public void removeTab(String id) {
        Tab tab = centerTabSet.getTab(id);
        if (tab != null) {
            centerTabSet.removeTab(tab);
        }
    }

    public void setMessage(String message, int delay) {
        messageWindow.setMessage(message, "#FFFFFF", null, delay);
    }

    public void setInformationMessage(String message) {
        messageWindow.setMessage(message, "#F79D5C", CoreConstants.ICON_INFORMATION, 15);
    }

    public void setNoticeMessage(String message) {
        setNoticeMessage(message, 15);
    }

    public void setNoticeMessage(String message, int delay) {
        messageWindow.setMessage(message, "#B3CC99", CoreConstants.ICON_SUCCESS, delay);
    }

    public void setWarningMessage(String message) {
        setWarningMessage(message, 0);
    }

    public void setWarningMessage(String message, int delay) {
        messageWindow.setMessage(message, "#F79191", CoreConstants.ICON_WARNING, delay);
    }

    public void hideMessage() {
        messageWindow.hideMessage();
    }

    public static class TabFactoryAndId {
        public final Supplier<Tab> factory;
        public final String id;

        public TabFactoryAndId(Supplier<Tab> factory, String id) {
            this.factory = factory;
            this.id = id;
        }
    }
}
