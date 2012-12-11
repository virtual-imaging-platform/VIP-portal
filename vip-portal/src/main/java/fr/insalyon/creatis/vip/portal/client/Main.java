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
package fr.insalyon.creatis.vip.portal.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import fr.insalyon.creatis.vip.application.client.ApplicationModule;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerModule;
import fr.insalyon.creatis.vip.docs.client.DocsModule;
import fr.insalyon.creatis.vip.gatelab.client.GateLabModule;
import fr.insalyon.creatis.vip.models.client.ModelModule;
import fr.insalyon.creatis.vip.simulatedata.client.SimulatedDataModule;
import fr.insalyon.creatis.vip.simulationgui.client.SimulationGUIModule;
import fr.insalyon.creatis.vip.social.client.SocialModule;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Main implements EntryPoint {

    public void onModuleLoad() {

        Layout.getInstance().getModal().show("Loading VIP " + CoreConstants.VERSION, true);
        
        // Modules
        Modules modulesInit = Modules.getInstance();
        modulesInit.add(new CoreModule());
        modulesInit.add(new DocsModule());
        modulesInit.add(new ModelModule());
        modulesInit.add(new SimulationGUIModule());
        modulesInit.add(new SimulatedDataModule());
        modulesInit.add(new ApplicationModule());
        modulesInit.add(new GateLabModule());
        modulesInit.add(new SocialModule());
        modulesInit.add(new DataManagerModule());
        // End-Modules

        final String ticket = Window.Location.getParameter("ticket");
        String login = Window.Location.getParameter("login");
        if (ticket == null && (login == null || !login.equals("CASN4U"))) {
            //regular VIP authentication
            configureVIP();
        } else {
            configureN4U(ticket);
        }
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

        if (Cookies.getCookieNames().contains(CoreConstants.COOKIES_USER)) {
            email = Cookies.getCookie(CoreConstants.COOKIES_USER);
            session = Cookies.getCookie(CoreConstants.COOKIES_SESSION);
        }
        // End-Cookies

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<User> callback = new AsyncCallback<User>() {

            public void onFailure(Throwable caught) {
                Layout.getInstance().getModal().hide();
                SC.warn("Error while configuring VIP:<br />" + caught.getMessage());
            }

            public void onSuccess(User user) {
                Layout.getInstance().getModal().hide();
                Layout.getInstance().authenticate(user);
            }
        };
        service.configure(email, session, callback);
    }

    private void configureN4U(String ticket) {
        //N4U authentication
        if (ticket == null) {
            //if the user has no ticket, get one
            displayLoginView();
        } else {
            //sign in with N4U ticket
            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

            final AsyncCallback<User> callback = new AsyncCallback<User>() {

                public void onFailure(Throwable caught) {
                    Layout.getInstance().getModal().hide();
                    if (caught.getMessage().contains("Authentication failed")) {
                        displayLoginView();
                    } else {
                        configureVIP();
                    }
                }

                public void onSuccess(User result) {
                    Layout.getInstance().getModal().hide();

                    Cookies.setCookie(CoreConstants.COOKIES_USER,
                            result.getEmail(), CoreConstants.COOKIES_EXPIRATION_DATE,
                            null, "/", false);
                    Cookies.setCookie(CoreConstants.COOKIES_SESSION,
                            result.getSession(), CoreConstants.COOKIES_EXPIRATION_DATE,
                            null, "/", false);

                    Layout.getInstance().authenticate(result);
                }
            };
            Layout.getInstance().getModal().show("Signing in with CAS...", true);
            service.signin(ticket, callback);
        }
    }
}
