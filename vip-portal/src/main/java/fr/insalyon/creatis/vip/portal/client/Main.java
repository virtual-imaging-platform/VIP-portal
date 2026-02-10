package fr.insalyon.creatis.vip.portal.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;

import fr.insalyon.creatis.vip.application.client.ApplicationModule;
import fr.insalyon.creatis.vip.applicationimporter.client.ApplicationImporterModule;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.models.UsageStats;
import fr.insalyon.creatis.vip.core.models.User;
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
        service.configure(callback);
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
