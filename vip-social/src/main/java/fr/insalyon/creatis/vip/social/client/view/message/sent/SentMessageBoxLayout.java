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
package fr.insalyon.creatis.vip.social.client.view.message.sent;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.bean.Message;
import fr.insalyon.creatis.vip.social.client.rpc.SocialService;
import fr.insalyon.creatis.vip.social.client.rpc.SocialServiceAsync;

/**
 *
 * @author Rafael Silva
 */
public class SentMessageBoxLayout extends HLayout {

    private Message message;

    public SentMessageBoxLayout(Message message) {

        this.message = message;

        this.setMembersMargin(2);
        this.setWidth100();
        this.setHeight(50);
        if (message.isRead()) {
            this.setBackgroundColor("#FFFFFF");
        } else {
            this.setBackgroundColor("#D8E8EB");
        }

        configureImageLayout();
        configureMainLayout();
        configureDateLayout();
        configureActionLayout();
    }

    /**
     * Configures the image layout.
     */
    private void configureImageLayout() {

        VLayout imgLayout = new VLayout();
        imgLayout.setPadding(2);
        imgLayout.setWidth(50);
        imgLayout.setHeight(50);
        imgLayout.setAlign(Alignment.CENTER);

        imgLayout.addMember(new Img(SocialConstants.ICON_USER, 48, 48));
        this.addMember(imgLayout);
    }

    /**
     * Configures the main layout of a message.
     */
    private void configureMainLayout() {

        VLayout mainLayout = new VLayout(2);
        mainLayout.setWidth("*");
        mainLayout.setHeight(50);
        mainLayout.setAlign(Alignment.CENTER);

        StringBuilder sb = new StringBuilder();
        int count = 0;
        int numOfReceivers = message.getReceivers().length;
        for (User user : message.getReceivers()) {
            if (count == SocialConstants.MESSAGE_MAX_RECEIVERS_DISPLAY) {
                sb.append(" and ").append(numOfReceivers - count).append(" other users");
                break;
            } else {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(user.getFullName());
                count++;
            }
        }

        mainLayout.addMember(WidgetUtil.getLabel("<strong>" + sb.toString() + "</strong>: " 
                + message.getTitle(), 15, Cursor.HAND));

        mainLayout.addMember(WidgetUtil.getLabel("<font color=\"#666666\">"
                + message.getMessage().substring(0, 50) + "...</font>", 15, Cursor.HAND));

        mainLayout.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                new SentMessageViewerWindow(message).show();
            }
        });

        this.addMember(mainLayout);
    }

    /**
     * Configures message posted date.
     */
    private void configureDateLayout() {

        VLayout dateLayout = new VLayout(5);
        dateLayout.setHeight(50);
        dateLayout.setWidth(150);
        dateLayout.setAlign(VerticalAlignment.TOP);

        dateLayout.addMember(WidgetUtil.getLabel("<font color=\"#666666\">"
                + message.getPosted() + "</font>", 15));

        this.addMember(dateLayout);
    }

    /**
     * Configures actions related to a message.
     */
    private void configureActionLayout() {

        VLayout actionLayout = new VLayout(5);
        actionLayout.setHeight(50);
        actionLayout.setWidth(30);
        actionLayout.setAlign(VerticalAlignment.TOP);

        Img removeImg = new Img(SocialConstants.ICON_REMOVE, 16, 16);
        removeImg.setCursor(Cursor.HAND);
        removeImg.setPrompt("Remove");
        removeImg.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                remove();
            }
        });
        actionLayout.addMember(removeImg);

        this.addMember(actionLayout);
    }

    private void remove() {

        SC.confirm("Do you really want to remove this message?", new BooleanCallback() {

            public void execute(Boolean value) {

                if (value != null && value) {
                    SocialServiceAsync service = SocialService.Util.getInstance();
                    AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        public void onFailure(Throwable caught) {
                            SC.warn("Unable to remove message:<br />" + caught.getMessage());
                        }

                        public void onSuccess(Void result) {
                            destroy();
                        }
                    };
                    service.removeMessage(message.getId(), callback);
                }
            }
        });
    }
}
