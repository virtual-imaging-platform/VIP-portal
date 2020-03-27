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
package fr.insalyon.creatis.vip.core.client.view.user.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GroupBoxLayout extends HLayout {

    private String name;
    private boolean isPublic;
    private GROUP_ROLE role;
    private boolean isMember;
    private Label memberLabel;
    private Label actionLabel;

    public GroupBoxLayout(String name, boolean isPublic, GROUP_ROLE role) {

        this.name = name;
        this.isPublic = isPublic;
        this.role = role;
        this.isMember = role == GROUP_ROLE.None ? false : true;

        this.setWidth100();
        this.setHeight(50);
        this.setBackgroundColor("#FFFFFF");
        this.setBorder("1px solid #C0C0C0");

        configureInfo();
        configureActions();
    }

    private void configureInfo() {

        VLayout vLayout = new VLayout();
        vLayout.setPadding(5);
        vLayout.setWidth100();
        vLayout.setHeight100();

        if (isPublic) {
            vLayout.addMember(WidgetUtil.getLabel("<b>" + name + "</b>", 15));
        } else {
            vLayout.addMember(WidgetUtil.getLabel("<b>" + name + "</b>", CoreConstants.ICON_LOCK, 15));
        }

        memberLabel = isMember
                ? WidgetUtil.getLabel("<font color=\"#666666\">Already a member (" + role.name() + ")</font>", 15)
                : WidgetUtil.getLabel("<font color=\"#666666\">Not a member</font>", 15);
        vLayout.addMember(memberLabel);

        this.addMember(vLayout);
    }

    private void configureActions() {

        VLayout vLayout = new VLayout();
        vLayout.setPadding(5);
        vLayout.setWidth(50);
        vLayout.setHeight100();

        if (isPublic) {
            String contents = isMember ? "<font color=\"#C0C0C0\">Leave Group</font>" : "Join Group";
            actionLabel = WidgetUtil.getLabel(contents, 15, Cursor.HAND);
            actionLabel.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {

                    if (isMember) {
                        SC.ask("Do you really want to leave the group '" + name + "'?", new BooleanCallback() {

                            @Override
                            public void execute(Boolean value) {
                                if (value) {
                                    leave();
                                }
                            }
                        });
                    } else {
                        join();
                    }
                }
            });

            vLayout.addMember(actionLabel);
        }

        this.addMember(vLayout);
    }

    private void leave() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to leave '" + name + "':<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                isMember = false;
                role = GROUP_ROLE.None;
                memberLabel.setContents("<font color=\"#666666\">Not a member</font>");
                actionLabel.setContents("Join Group");
            }
        };
        service.removeUserFromGroup(null, name, callback);
    }

    private void join() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to join '" + name + "':<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                isMember = true;
                role = GROUP_ROLE.User;
                memberLabel.setContents("<font color=\"#666666\">Already a member (" + role.name() + ")</font>");
                actionLabel.setContents("<font color=\"#C0C0C0\">Leave Group</font>");
            }
        };
        service.addUserToGroup(name, callback);
    }
}
