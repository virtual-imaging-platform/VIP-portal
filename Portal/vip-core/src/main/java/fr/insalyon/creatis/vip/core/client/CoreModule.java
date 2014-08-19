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
package fr.insalyon.creatis.vip.core.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
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
import fr.insalyon.creatis.vip.core.client.view.user.PublicationTab;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class CoreModule extends Module {

    public static User user;
    private static GeneralTileGrid generalTileGrid;
    private static SystemTileGrid systemTileGrid;
    private static HomeTab homeTab;
    public static List<String> accountTypes;

    public CoreModule() {

        init();
    }

    @Override
    public void load() {

        // Add tile grids
        homeTab.addTileGrid(generalTileGrid);
        if (user.isSystemAdministrator() || user.isGroupAdmin()) {
            systemTileGrid.addParser(new SystemParser());
            homeTab.addTileGrid(systemTileGrid);
        }

        // Configure User's toolstrip        
        MainToolStrip.getInstance().addMenuButton(new UserMenuButton(user));

        // Home Tab
        Layout.getInstance().addTab(homeTab);

        // For users with no group (e.g. they just signed in using Mozilla Persona)

        if (!user.hasGroups() || !user.hasAcceptTermsOfUse()) {
            final AccountTab accountTab = new AccountTab();
            Layout.getInstance().addTab(accountTab);
            if (!user.hasAcceptTermsOfUse()) {
                showDialog("Please accept our Terms of Use", accountTab);
            }
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
                    final AccountTab accountTab = new AccountTab();
                    Layout.getInstance().addTab(accountTab);
                    showDialog("Our Terms of Use have changed. Please accept them again.", accountTab);

                }
            }
        };
        ConfigurationService.Util.getInstance().compare(callback);
        }

        //call to test last publication update
        final AsyncCallback<Boolean> callback2 = new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {

                Layout.getInstance().setWarningMessage("Cannot get the date of last publication update" + caught.getMessage(), 10);

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    showDialog(" You haven't updated your publications for a while. Please take a few minutes to review the list of publications that you made using VIP.");
                }

            }
        };
        ConfigurationService.Util.getInstance().testLastUpdatePublication(callback2);

    }

    @Override
    public void postLoading() {

        // Experiencing problems button
        ToolStripButton helpButton = new ToolStripButton("Experiencing problems?");
        helpButton.setIcon(CoreConstants.ICON_HELP);
        helpButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(new ContactTab());
            }
        });

        MainToolStrip.getInstance().addFill();
        MainToolStrip.getInstance().addMember(helpButton);
    }

    @Override
    public void terminate() {

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

    private void init() {

        generalTileGrid = new GeneralTileGrid();
        systemTileGrid = new SystemTileGrid();
        homeTab = new HomeTab();
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

    private void showDialog(String message) {
        final Dialog dialog = new Dialog();
        dialog.setTitle("Update publications");
        dialog.setMessage(message);
        dialog.setIcon("[SKIN]ask.png");
        dialog.addCloseClickHandler(new CloseClickHandler() {
            @Override
            public void onCloseClick(CloseClickEvent event) {
                dialog.destroy();
            }
        });
        Button ok = new Button("OK");
        ok.setWidth(180);
        Button anyPublication = new Button("I don't have any publication to add");
        anyPublication.setWidth(180);
        dialog.setButtons(ok, anyPublication);
        dialog.setIsModal(Boolean.TRUE);
        anyPublication.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        Layout.getInstance().setWarningMessage("can't update field lastUpdatePublication in VIPUsers " + caught.getMessage(), 10);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        dialog.destroy();
                    }
                };
                service.updateLastUpdatePublication(callback);
            }
        });
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(new PublicationTab());
                dialog.destroy();
            }
        });
        dialog.draw();
    }
}
