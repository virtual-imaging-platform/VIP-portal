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
package fr.insalyon.creatis.vip.application.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.ApplicationHomeParser;
import fr.insalyon.creatis.vip.application.client.view.ApplicationSystemParser;
import fr.insalyon.creatis.vip.application.client.view.ApplicationTileGrid;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.layout.CenterTabSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationModule extends Module {

    public static List<String> reservedClasses;

    public ApplicationModule() {

        reservedClasses = new ArrayList<String>();
        addAccountType(ApplicationConstants.ACCOUNT_VIP);
    }

    @Override
    public void load() {

        CoreModule.addGeneralApplicationParser(new ApplicationHomeParser());
        CoreModule.addSystemApplicationParser(new ApplicationSystemParser());

        // Applications Tile Grid
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<AppClass>> callback = new AsyncCallback<List<AppClass>>() {

            public void onFailure(Throwable caught) {
                SC.say("Unable to load classes:<br />" + caught.getMessage());
            }

            public void onSuccess(List<AppClass> result) {

                for (AppClass appClass : result) {
                    if (!reservedClasses.contains(appClass.getName())) {
                        CoreModule.addApplicationsTileGrid(
                                new ApplicationTileGrid(appClass.getName()));
                    }
                }
            }
        };
        service.getClasses(callback);

        // Simulation close tab
        CenterTabSet.getInstance().addCloseClickHandler(new CloseClickHandler() {

            public void onCloseClick(TabCloseClickEvent event) {
                Tab tab = event.getTab();
                try {
                    ((AbstractSimulationTab) tab).destroy();
                } catch (ClassCastException ex) {
                    // do nothing
                }
            }
        });
    }

    @Override
    public void postLoading() {
    }

    @Override
    public boolean parseAccountType(String accountType) {

        if (accountType.equals(ApplicationConstants.ACCOUNT_VIP)) {
            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
            AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    SC.say("Unable to add user to group '" + ApplicationConstants.GROUP_VIP
                            + "':<br />" + caught.getMessage());
                }

                public void onSuccess(Void result) {
                }
            };
            service.addUserToGroup(ApplicationConstants.GROUP_VIP, callback);
            return true;
        }
        return false;
    }

    @Override
    public void terminate() {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                SC.say("Unable to signout:<br />" + caught.getMessage());
            }

            public void onSuccess(Void result) {
            }
        };
        service.signout(callback);
    }
}
