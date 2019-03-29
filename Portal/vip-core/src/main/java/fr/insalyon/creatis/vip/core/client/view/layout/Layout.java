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
package fr.insalyon.creatis.vip.core.client.view.layout;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
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
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.auth.ActivationTab;
import fr.insalyon.creatis.vip.core.client.view.auth.SignInTab;
import fr.insalyon.creatis.vip.core.client.view.common.MessageWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.toolstrip.MainToolStrip;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Layout {

    private static Logger logger = Logger.getLogger(Layout.class.getName());

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
            Cookies.setCookie(CoreConstants.COOKIES_USER, user.getEmail(),
                              CoreConstants.COOKIES_EXPIRATION_DATE, null, "/", false);
            Cookies.setCookie(CoreConstants.COOKIES_SESSION, user.getSession(),
                              CoreConstants.COOKIES_EXPIRATION_DATE, null, "/", false);

            if (user.isConfirmed()) {
                Modules.getInstance().initializeModules(user);
            } else {
                addTab(CoreConstants.TAB_ACTIVATION,
                       new Layout.TabFactory() {
                           public Tab create() { return new ActivationTab(); }
                       });
            }
        } else {
            if (!Cookies.isCookieEnabled()) {
                setWarningMessage(
                    "Unable to sign in: cookies must be enabled.");
            }
            addTab(CoreConstants.TAB_SIGNIN,
                   new Layout.TabFactory() {
                       public Tab create() { return new SignInTab(); }
                   });
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

                Cookies.setCookie(CoreConstants.COOKIES_USER, null, new Date(0), null, "/", false);
                Cookies.setCookie(CoreConstants.COOKIES_SESSION, null, new Date(0), null, "/", false);

                for (Tab tab : centerTabSet.getTabs()) {
                    centerTabSet.removeTab(tab);
                }
                MainToolStrip.getInstance().reset();
                authenticate(null);
                Modules.getInstance().finalizeModules();
            }
        };
        ConfigurationService.Util.getInstance().signout(callback);
    }

    /**
     * Add a tab.
     *
     * @deprecated Left for legacy code, in vip-query.  You should use the other
     *             variant of the addTab method.
     */
    @Deprecated
    public void addTab(Tab tab) {
        String id = tab.getID();
        if (centerTabSet.getTab(id) != null) {
            centerTabSet.removeTab(id);
        }
        centerTabSet.addTab(tab);
        centerTabSet.selectTab(id);
     }

    public Tab addTab(String id, TabFactory factory) {
        Tab tab = centerTabSet.getTab(id);
        if (tab == null) {
            tab = factory.create();
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
        Tab tab = getTab(id);
        if (tab != null) {
            removeTab(tab);
        }
    }

    public void removeTab(Tab tab) {
        if (centerTabSet.getTab(tab.getID()) != null) {
            centerTabSet.removeTab(tab);
        }
    }

    /**
     *
     * @param message
     * @param delay
     */
    public void setMessage(String message, int delay) {
        messageWindow.setMessage(message, "#FFFFFF", null, delay);
    }

    /**
     *
     * @param message
     */
    public void setNoticeMessage(String message) {
        setNoticeMessage(message, 15);
    }

    /**
     *
     * @param message
     * @param delay
     */
    public void setNoticeMessage(String message, int delay) {
        messageWindow.setMessage(message, "#B3CC99", CoreConstants.ICON_SUCCESS, delay);
    }

    /**
     *
     * @param message
     */
    public void setWarningMessage(String message) {
        setWarningMessage(message, 10);
    }

    /**
     *
     * @param message
     * @param delay
     */
    public void setWarningMessage(String message, int delay) {
        messageWindow.setMessage(message, "#F79191", CoreConstants.ICON_WARNING, delay);
    }

    public static interface TabFactory {
        Tab create();
    }

    public static class TabFactoryAndId {
        public final TabFactory factory;
        public final String id;

        public TabFactoryAndId(TabFactory factory, String id) {
            this.factory = factory;
            this.id = id;
        }
    }
}
