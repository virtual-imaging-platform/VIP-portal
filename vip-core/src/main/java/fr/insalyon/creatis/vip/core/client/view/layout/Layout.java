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
package fr.insalyon.creatis.vip.core.client.view.layout;

import com.google.gwt.user.client.Cookies;
import fr.insalyon.creatis.vip.core.client.view.layout.toolstrip.MainToolStrip;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.auth.ActivationTab;
import fr.insalyon.creatis.vip.core.client.view.auth.SignInTab;
import java.util.Date;

/**
 *
 * @author Rafael Silva
 */
public class Layout {

    private static Layout instance;
    private VLayout vLayout;
    private CenterTabSet centerTabSet;
    private SectionStack mainSectionStack;
    private ModalWindow modal;

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

    public ModalWindow getModal() {
        return modal;
    }

    /**
     * Authenticates a user.
     * 
     */
    public void authenticate(User user) {

        if (user != null) {

            Cookies.setCookie(CoreConstants.COOKIES_USER, user.getEmail(),
                    CoreConstants.COOKIES_EXPIRATION_DATE, null, "/", false);
            Cookies.setCookie(CoreConstants.COOKIES_SESSION, user.getSession(),
                    CoreConstants.COOKIES_EXPIRATION_DATE, null, "/", false);

            if (user.isConfirmed()) {
                Modules.getInstance().initializeModules(user);

            } else {
                addTab(new ActivationTab());
            }
        } else {
            addTab(new SignInTab());
        }
    }

    /**
     * Signs out.
     * 
     */
    public void signout() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error while signing out:<br />" + caught.getMessage());
            }

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
        service.signout(callback);
    }

    public void addTab(Tab tab) {
        addTab(tab, true);
    }

    public void addTab(Tab tab, boolean select) {
        if (centerTabSet.getTab(tab.getID()) == null) {
            centerTabSet.addTab(tab);
        }
        if (select) {
            centerTabSet.selectTab(tab.getID());
        }
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
}
