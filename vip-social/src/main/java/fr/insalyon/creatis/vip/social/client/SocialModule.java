package fr.insalyon.creatis.vip.social.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.layout.toolstrip.MainToolStrip;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;
import fr.insalyon.creatis.vip.social.client.view.SocialParser;
import fr.insalyon.creatis.vip.social.client.view.SocialTab;

import java.util.Set;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SocialModule extends Module {

    private static ToolStripButton socialButton;
    private static Timer timer;

    @Override
    public void load() {

        CoreModule.addGeneralApplicationParser(new SocialParser());

        socialButton = new ToolStripButton();
        socialButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(
                    SocialConstants.TAB_SOCIAL, SocialTab::new);
            }
        });

        MainToolStrip.getInstance().addSeparator();
        MainToolStrip.getInstance().addButton(socialButton);
        verifyMessages();

        timer = new Timer() {

            public void run() {
                verifyMessages();
            }
        };
        timer.scheduleRepeating(5 * 60000);
    }

    @Override
    public void postLoading() {
    }

    @Override
    public void terminate(Set<Tab> removedTabs) {
        timer.cancel();
    }

    public static void verifyMessages() {

        SocialServiceAsync service = SocialService.Util.getInstance();
        AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {

            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Connection lost with the server.", 20);
                timer.cancel();
            }

            public void onSuccess(Integer result) {

                if (result > 0) {
                    socialButton.setTitle(Canvas.imgHTML(SocialConstants.ICON_SOCIAL)
                            + " " + Canvas.imgHTML(SocialConstants.ICON_MESSAGE_NEW));
                    socialButton.setPrompt(SocialConstants.APP_SOCIAL + " - New Message");

                } else {
                    socialButton.setTitle(Canvas.imgHTML(SocialConstants.ICON_SOCIAL));
                    socialButton.setPrompt(SocialConstants.APP_SOCIAL);
                }
            }
        };
        service.verifyMessages(callback);
    }
}
