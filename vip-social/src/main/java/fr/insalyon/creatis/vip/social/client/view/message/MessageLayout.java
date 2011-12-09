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
package fr.insalyon.creatis.vip.social.client.view.message;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;
import fr.insalyon.creatis.vip.social.client.view.AbstractMainLayout;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class MessageLayout extends AbstractMainLayout {

    private VLayout messagesLayout;

    public MessageLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);
        this.setMembersMargin(10);
        this.setPadding(5);

        this.addMember(WidgetUtil.getLabel("<p style=\"font-size: 13pt\"><strong>"
                + SocialConstants.MENU_MESSAGE + "</strong></p>",
                SocialConstants.ICON_MESSAGE, 17));

        configureButtons();

        messagesLayout = new VLayout(5);
        messagesLayout.setWidth100();
        messagesLayout.setHeight100();
        messagesLayout.setPadding(5);
        messagesLayout.setOverflow(Overflow.AUTO);
        messagesLayout.setAlign(VerticalAlignment.TOP);
        messagesLayout.setBackgroundColor("#F2F2F2");

        loadData();

        this.addMember(messagesLayout);
    }

    public void loadData() {

        messagesLayout.removeMembers(messagesLayout.getMembers());
        SocialServiceAsync service = SocialService.Util.getInstance();
        AsyncCallback<List<Message>> callback = new AsyncCallback<List<Message>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Unable to get list of messages:<br />" + caught.getMessage());
            }

            public void onSuccess(List<Message> result) {

                for (Message message : result) {
                    messagesLayout.addMember(new MessageBoxLayout(message));
                }
            }
        };
        service.getMessagesByUser(callback);
    }

    private void configureButtons() {

        if (CoreModule.user.isSystemAdministrator()) {
            HLayout buttonsLayout = new HLayout(5);
            buttonsLayout.setWidth100();

            Label composeLabel = WidgetUtil.getLabel("New Message", SocialConstants.ICON_COMPOSE, 15, Cursor.HAND);
            composeLabel.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    new MessageComposerWindow().show();
                }
            });
            buttonsLayout.addMember(composeLabel);

            Label refreshLabel = WidgetUtil.getLabel("Refresh", SocialConstants.ICON_REFRESH, 15, Cursor.HAND);
            refreshLabel.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    loadData();
                }
            });
            buttonsLayout.addMember(refreshLabel);

            this.addMember(buttonsLayout);
        }
    }
}
