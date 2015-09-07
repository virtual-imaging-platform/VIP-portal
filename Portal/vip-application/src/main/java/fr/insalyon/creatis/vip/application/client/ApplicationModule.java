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
package fr.insalyon.creatis.vip.application.client;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationHomeParser;
import fr.insalyon.creatis.vip.application.client.view.ApplicationSystemParser;
import fr.insalyon.creatis.vip.application.client.view.ApplicationTileGrid;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractSimulationTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.timeline.TimelineLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.layout.CenterTabSet;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.server.business.Server;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationModule extends Module {

    public static List<String> reservedClasses;
    public static List<String> reservedGateClasses;
    public static List<String> reservedTestClasses;

     public ApplicationModule() {
     
       
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load applet gatelab classes:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
             reservedGateClasses=result;
            }
        };
        ApplicationService.Util.getInstance().getAppletGateLabClasses(callback);
      
        final AsyncCallback<List<String>> callbackTest = new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load applet gatelab classes:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
             reservedTestClasses=result;
            }
        };
        ApplicationService.Util.getInstance().getAppletGateLabTestClasses(callbackTest);
          
    }

    @Override
    public void load() {
        reservedClasses = new ArrayList<String>();
        reservedClasses.addAll(reservedGateClasses);
        reservedClasses.addAll(reservedTestClasses);
        CoreModule.addGeneralApplicationParser(new ApplicationHomeParser());
        CoreModule.addSystemApplicationParser(new ApplicationSystemParser());
        CoreModule.addLayoutToHomeTab(TimelineLayout.getInstance());

        // Applications Tile Grid
        final AsyncCallback<List<AppClass>> callback = new AsyncCallback<List<AppClass>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load classes:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<AppClass> result) {

                for (AppClass appClass : result) {
                    if (!reservedClasses.contains(appClass.getName())) {
                        CoreModule.addApplicationsTileGrid(
                                new ApplicationTileGrid(appClass.getName()));
                    }
                }
            }
        };
        ApplicationService.Util.getInstance().getClasses(callback);

        // Simulation close tab
        CenterTabSet.getInstance().addCloseClickHandler(new CloseClickHandler() {
            @Override
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
    public void terminate() {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to signout:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
            }
        };
        ApplicationService.Util.getInstance().signout(callback);
        TimelineLayout.getInstance().terminate();
    }

    @Override
    public void userRemoved(User user) {

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to anonymize user data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
            }
        };
        WorkflowService.Util.getInstance().updateUser(user.getFullName(), "User-" + Random.nextInt(100000), callback);
    }

    @Override
    public void userUpdated(User oldUser, User updatedUser) {

        if (!oldUser.getFullName().equals(updatedUser.getFullName())) {
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    Layout.getInstance().setWarningMessage("Unable to anonymize user data:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                }
            };
            WorkflowService.Util.getInstance().updateUser(oldUser.getFullName(), updatedUser.getFullName(), callback);
        }
    }
    
    @Override
    public boolean requiresGridJob() { return true; }
}
