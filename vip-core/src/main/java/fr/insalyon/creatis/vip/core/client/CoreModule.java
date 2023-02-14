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
package fr.insalyon.creatis.vip.core.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationsTileGrid;
import fr.insalyon.creatis.vip.core.client.view.contact.ContactTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.layout.toolstrip.MainToolStrip;
import fr.insalyon.creatis.vip.core.client.view.main.GeneralTileGrid;
import fr.insalyon.creatis.vip.core.client.view.main.HomeTab;
import fr.insalyon.creatis.vip.core.client.view.main.SystemParser;
import fr.insalyon.creatis.vip.core.client.view.main.SystemTileGrid;
import fr.insalyon.creatis.vip.core.client.view.user.AccountTab;
import fr.insalyon.creatis.vip.core.client.view.user.UserMenuButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class CoreModule extends Module {

    public static User user;
    private static GeneralTileGrid generalTileGrid;
    private static SystemTileGrid systemTileGrid;
    private static HomeTab homeTab;
    private static Map<String,Runnable> homePageActions;
    public static List<String> accountTypes;

    public CoreModule() {

        init();
    }

    @Override
    public void load() {

        // Add tile grids
        homeTab.addTileGrid(generalTileGrid);
        if (user.isSystemAdministrator() || user.isGroupAdmin() || user.isDeveloper()) {
            systemTileGrid.addParser(new SystemParser());
            homeTab.addTileGrid(systemTileGrid);
        }

        // Configure User's toolstrip
        MainToolStrip.getInstance().addMenuButton(new UserMenuButton(user));

        // Home Tab
        Layout.getInstance().addTab(CoreConstants.TAB_HOME, () -> homeTab);

        // open account tab to accept the terms of use if necessary
        // Also open the tab for users with no group :
        // It was possible with former Mozilla Persona but keep it just in case

        if (!user.hasGroups() || !user.hasAcceptTermsOfUse()) {
            final AccountTab accountTab =
                (AccountTab) Layout.getInstance().addTab(
                    CoreConstants.TAB_ACCOUNT, AccountTab::new);
            if (!user.hasAcceptTermsOfUse()) {
                showDialog("Please accept our Terms of Use", accountTab);
            }
        }

        // check if the user has requested an email change
        if (user.getNextEmail() != null) {
            Layout.getInstance().setNoticeMessage("You have requested an email address change. Please validate it in your account page", 10);
        }

        //call to terms of use
         if (user.hasAcceptTermsOfUse()) {
        final AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Cannot get last update of Terms of Use" + caught.getMessage(), 10);

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    final AccountTab accountTab =
                        (AccountTab) Layout.getInstance().addTab(
                            CoreConstants.TAB_ACCOUNT, AccountTab::new);
                    showDialog("Our Terms of Use have changed. Please accept them again.", accountTab);

                }
            }
        };
        ConfigurationService.Util.getInstance().compare(callback);
        }

    }

    @Override
    public void postLoading() {

        // Experiencing problems button
        ToolStripButton helpButton = new ToolStripButton("Experiencing problems?");
        helpButton.setIcon(CoreConstants.ICON_HELP);
        helpButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(
                    CoreConstants.TAB_CONTACT, ContactTab::new);
            }
        });

        MainToolStrip.getInstance().addFill();
        MainToolStrip.getInstance().addMember(helpButton);
    }

    @Override
    public void terminate(Set<Tab> removedTabs) {
        init();
    }

    /**
     * Adds an application parser to the general tile grid.
     *
     * @param parser Application parser
     */
    public static void addGeneralApplicationParser(ApplicationParser parser) {

        generalTileGrid.addParser(parser);
    }

    /**
     * Adds an application parser to the system tile grid.
     *
     * @param parser Application parser
     */
    public static void addSystemApplicationParser(ApplicationParser parser) {

        systemTileGrid.addParser(parser);
    }

    /**
     * Adds a new applications tile grid to the home tab.
     *
     * @param tileGrid
     */
    public static void addApplicationsTileGrid(ApplicationsTileGrid tileGrid) {

        homeTab.addTileGrid(tileGrid);

    }

    /**
     * Adds a layout to the home tab.
     *
     * @param layout
     */
    public static void addLayoutToHomeTab(VLayout layout) {

        homeTab.addToRightLayout(layout);
    }

    public static Map<String, Runnable> getHomePageActions() {
        return homePageActions;
    }

    private void init() {

        user = null;
        generalTileGrid = new GeneralTileGrid();
        systemTileGrid = new SystemTileGrid();
        homeTab = new HomeTab();
        if (homePageActions == null) {
            // it must not be removed after a logout
            homePageActions = new HashMap<>();
        }
    }

    private void showDialog(String message, final AccountTab tab) {
        final Dialog dialog = new Dialog();
        dialog.setHeight("40%");
        dialog.setWidth(500);
        dialog.setTitle("Terms Of Use");
        dialog.addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(CloseClickEvent event) {
                dialog.destroy();
                Layout.getInstance().signout();
            }
        });
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        Frame frame = new Frame("documentation/terms.html");
        frame.setWidth("420");
        Label l = new Label(message);
        l.setHeight(100);
        l.setWidth100();
        dialog.setMembersMargin(2);
        dialog.addItem(l);
        dialog.addItem(frame);
        dialog.setButtons(ok, cancel);
        dialog.setIsModal(Boolean.TRUE);
        cancel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dialog.destroy();
                Layout.getInstance().signout();
            }
        });
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //mettre a jour le champs de la base de donner
                ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Layout.getInstance().setWarningMessage("can't update field terms of use" + caught.getMessage(), 10);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        tab.getLayouttermsOfUse().load();
                        dialog.destroy();
                    }
                };
                service.updateTermsOfUse(callback);
            }
        });

        dialog.draw();
    }


}
