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
package fr.insalyon.creatis.vip.portal.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import fr.insalyon.creatis.vip.applicationimporter.client.ApplicationImporterModule;
import fr.insalyon.creatis.vip.application.client.ApplicationModule;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.bean.UsageStats;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerModule;
import fr.insalyon.creatis.vip.docs.client.DocsModule;
import fr.insalyon.creatis.vip.gatelab.client.GateLabModule;
import fr.insalyon.creatis.vip.publication.client.PublicationModule;
import fr.insalyon.creatis.vip.social.client.SocialModule;
import fr.insalyon.creatis.vip.visualization.client.VisualizationModule;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Main implements EntryPoint {

    @Override
    public void onModuleLoad() {

        String login = Window.Location.getParameter("login");

        // login might be null.
        if ("stats".equals(login)) {
            configureStats();
            return;
        }

        Layout.getInstance().getModal().show("Loading VIP " + CoreConstants.VERSION, true);

        Modules modulesInit = Modules.getInstance();
        modulesInit.add(new CoreModule());
        modulesInit.add(new DataManagerModule());
        modulesInit.add(new ApplicationModule());
        modulesInit.add(new PublicationModule());
        modulesInit.add(new DocsModule());
        modulesInit.add(new SocialModule());
        modulesInit.add(new GateLabModule());
        modulesInit.add(new ApplicationImporterModule());
        modulesInit.add(new VisualizationModule());

        configureVIP();
    }

    //redirect to N4U CAS authentication
    public void displayLoginView() {
        ConfigurationService.Util.getInstance().getCASLoginPageUrl(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.toString());
            }

            @Override
            public void onSuccess(String url) {
                // redirect to login page
                Window.Location.replace(url);
            }
        });
    }

    private void configureVIP() {

        // Cookies
        String email = null;
        String session = null;

        // this will actually always return null since we use httpOnly cookies
        // the actual values are retrieved in the service implementation itself
        if (Cookies.getCookieNames().contains(CoreConstants.COOKIES_USER)) {
            email = Cookies.getCookie(CoreConstants.COOKIES_USER);
            session = Cookies.getCookie(CoreConstants.COOKIES_SESSION);
        }
        // End-Cookies

        final ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<User> callback = new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().getModal().hide();
                SC.warn("Error while configuring VIP:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(User user) {
                Layout.getInstance().getModal().hide();
                Layout.getInstance().authenticate(user);
            }
        };
        service.configure(email, session, callback);
    }

    private void configureStats() {
        final ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        AsyncCallback<UsageStats> acb = new AsyncCallback<UsageStats>() {
            @Override
            public void onFailure(Throwable caught) {
                SC.say("Cannot get usage stats");
            }

            @Override
            public void onSuccess(UsageStats result) {
                final String message = "<center>Today <font color=\"red\" size=\"4\"><b>" + result.getnUsers() + "</b></font> users from <font color=\"red\" size=\"4\"><b>" + result.getnCountries() + "</b></font> countries can access VIP.</center>";

                final Label label = new Label(message);
                label.setWidth100();
                RootPanel.get().add(label);
            }
        };
        service.getUsageStats(acb);

    }
}
