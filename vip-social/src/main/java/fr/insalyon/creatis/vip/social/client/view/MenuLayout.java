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
package fr.insalyon.creatis.vip.social.client.view;

import fr.insalyon.creatis.vip.social.client.view.common.AbstractMainLayout;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.social.client.SocialConstants;
import fr.insalyon.creatis.vip.social.client.view.message.MessageLayout;
import fr.insalyon.creatis.vip.social.client.view.message.group.GroupsLayout;
import fr.insalyon.creatis.vip.social.client.view.message.sent.SentMessageLayout;

/**
 *
 * @author Rafael Silva
 */
public class MenuLayout extends VLayout {

    public MenuLayout() {

        this.setWidth(250);
        this.setHeight100();
        this.setPadding(5);
        this.setOverflow(Overflow.AUTO);
        this.setAlign(VerticalAlignment.TOP);
        this.setMembersMargin(5);
        this.setBackgroundColor("#293638");

        VLayout appImgLayout = new VLayout(5);
        appImgLayout.setWidth100();
        appImgLayout.setHeight(100);
        appImgLayout.setDefaultLayoutAlign(Alignment.CENTER);

        appImgLayout.addMember(new Img(SocialConstants.IMG_SOCIAL, 64, 64));
        this.addMember(appImgLayout);

        VLayout menuLayout = new VLayout(5);

        menuLayout.addMember(getLabel(SocialConstants.ICON_MESSAGE,
                SocialConstants.MENU_MESSAGE, new MessageLayout()));

        menuLayout.addMember(getLabel(SocialConstants.ICON_MESSAGE_SENT,
                SocialConstants.MENU_MESSAGE_SENT, new SentMessageLayout()));

        menuLayout.addMember(getLabel(SocialConstants.ICON_GROUP, 
                SocialConstants.MENU_GROUP, new GroupsLayout()));

        this.addMember(menuLayout);
    }

    private Label getLabel(String icon, String contents, final AbstractMainLayout layout) {

        Label label = WidgetUtil.getLabel("<font color=\"#FFFFFF\">" + contents
                + "</font>", icon, 12, Cursor.HAND);

        label.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                SocialTab socialTab = (SocialTab) Layout.getInstance().getTab(SocialConstants.TAB_SOCIAL);
                socialTab.setLayout(layout);
                socialTab.loadData();
            }
        });

        return label;
    }
}
