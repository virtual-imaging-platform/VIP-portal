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
package fr.insalyon.creatis.vip.social.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.Module;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.layout.toolstrip.MainToolStrip;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;
import fr.insalyon.creatis.vip.social.client.view.SocialParser;
import fr.insalyon.creatis.vip.social.client.view.SocialTab;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class SocialModule extends Module {

    private static ToolStripButton socialButton;
    private Timer timer;

    @Override
    public void load() {

        CoreModule.addGeneralApplicationParser(new SocialParser());

        socialButton = new ToolStripButton();
        socialButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(new SocialTab());
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
    public void terminate() {

        timer.cancel();
    }

    public static void verifyMessages() {

        SocialServiceAsync service = SocialService.Util.getInstance();
        AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {

            public void onFailure(Throwable caught) {
                SC.warn("Unable to verify messages:<br />" + caught.getMessage());
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
