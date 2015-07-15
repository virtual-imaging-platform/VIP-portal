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
package fr.insalyon.creatis.vip.application.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ChangeSimulationUserLayout extends VLayout {

    private ModalWindow modal;
    private String simulationID;
    private String simulationName;
    private String currentUser;
    private SelectItem usersPickList;
    private IButton saveButton;

    public ChangeSimulationUserLayout(final ModalWindow modal, String simulationID, 
            String simulationName, String currentUser) {

        this.modal = modal;
        this.modal.show(false);
        this.simulationID = simulationID;
        this.simulationName = simulationName;
        this.currentUser = currentUser;

        this.setWidth(350);
        this.setHeight(150);
        this.setPadding(5);
        this.setMembersMargin(3);
        this.setBorder("1px solid #E0E0E0");
        this.setBackgroundColor("#FFFFFF");
        this.setOverflow(Overflow.AUTO);

        configure();
        loadUsers();
    }

    private void configure() {

        HLayout titleLayout = new HLayout(5);
        titleLayout.setWidth100();
        titleLayout.setHeight(20);

        Label titleLabel = WidgetUtil.getLabel("<b>Change simulation user</b>",
                CoreConstants.ICON_PERSONAL, 30);
        titleLabel.setWidth100();
        titleLayout.addMember(titleLabel);
        titleLayout.addMember(WidgetUtil.getSpaceLabel(16));
        titleLayout.addMember(WidgetUtil.getIconLabel(CoreConstants.ICON_CLOSE, "Close", 16, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                modal.hide();
                destroy();
            }
        }));
        this.addMember(titleLayout);

        usersPickList = new SelectItem();
        usersPickList.setShowTitle(false);
        usersPickList.setWidth(280);
        usersPickList.setRequired(true);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (usersPickList.validate()) {
                    save(usersPickList.getValueAsString());
                }
            }
        });

        this.addMember(WidgetUtil.getLabel("<b>Users</b>", 15));
        this.addMember(FieldUtil.getForm(usersPickList));
        this.addMember(saveButton);
    }

    @Override
    protected void onDraw() {
        moveTo(Layout.getInstance().getLayoutCanvas().getVisibleWidth() / 2 - 175,
                Layout.getInstance().getLayoutCanvas().getVisibleHeight() / 2 - 75);
    }

    private void save(String user) {

        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to change simulation user:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                ((SimulationsTab) Layout.getInstance().getTab(ApplicationConstants.TAB_MONITOR)).loadData();
            }
        };
        if (!user.equals(currentUser)) {
            modal.hide();
            destroy();
            modal.show("Changing user of simulation" + simulationName + " to '" + user + "'...", true);
            WorkflowService.Util.getInstance().changeSimulationUser(simulationID, user, callback);
        } else {
            modal.hide();
            destroy();
        }
    }

    private void loadUsers() {

        final AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load users:<br />" + caught.getMessage());
                usersPickList.setValues(currentUser);
            }

            @Override
            public void onSuccess(List<User> result) {
                List<String> dataList = new ArrayList<String>();
                for (User user : result) {
                    dataList.add(user.getFirstName() + " " + user.getLastName());
                }
                usersPickList.setValueMap(dataList.toArray(new String[]{}));
                usersPickList.setValue(currentUser);
            }
        };
        ConfigurationService.Util.getInstance().getUsers(callback);
    }
}
