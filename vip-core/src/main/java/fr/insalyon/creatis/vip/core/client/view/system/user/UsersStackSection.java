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
package fr.insalyon.creatis.vip.core.client.view.system.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class UsersStackSection extends SectionStackSection {

    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public UsersStackSection() {

        this.setTitle("Users");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        configureGrid();

        VLayout vLayout = new VLayout();
        vLayout.setMaxHeight(400);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.addMember(grid);
        
        modal = new ModalWindow(vLayout);

        this.addItem(vLayout);
        loadData();
    }

    private void configureGrid() {
        grid = new ListGrid() {

            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {

                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    ImgButton loadImg = getImgButton(CoreConstants.ICON_EDIT, "Edit Groups");
                    loadImg.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            edit(rollOverRecord.getAttribute("email"));
                        }
                    });
                    ImgButton deleteImg = getImgButton(CoreConstants.ICON_DELETE, "Delete");
                    deleteImg.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            final String email = rollOverRecord.getAttribute("email");
                            SC.confirm("Do you really want to remove the user \""
                                    + email + "\"?", new BooleanCallback() {

                                public void execute(Boolean value) {
                                    if (value != null && value) {
                                        remove(email);
                                    }
                                }
                            });
                        }
                    });
                    rollOverCanvas.addMember(loadImg);
                    rollOverCanvas.addMember(deleteImg);
                }
                return rollOverCanvas;
            }

            private ImgButton getImgButton(String imgSrc, String prompt) {

                ImgButton button = new ImgButton();
                button.setShowDown(false);
                button.setShowRollOver(false);
                button.setLayoutAlign(Alignment.CENTER);
                button.setSrc(imgSrc);
                button.setPrompt(prompt);
                button.setHeight(16);
                button.setWidth(16);
                return button;
            }
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField confirmedField = new ListGridField("confirmed", "Confirmed");
        confirmedField.setType(ListGridFieldType.BOOLEAN);
        ListGridField firstNameField = new ListGridField("firstName", "First Name");
        ListGridField lastNameField = new ListGridField("lastName", "Last Name");
        ListGridField emailField = new ListGridField("email", "Email");
        ListGridField institutionField = new ListGridField("institution", "Institution");
        ListGridField phoneField = new ListGridField("phone", "Phone");

        grid.setFields(confirmedField, firstNameField, lastNameField, emailField, 
                institutionField, phoneField);
        grid.setSortField("firstName");
        grid.setSortDirection(SortDirection.ASCENDING);
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            public void onCellDoubleClick(CellDoubleClickEvent event) {
                edit(event.getRecord().getAttribute("email"));
            }
        });
    }

    public void loadData() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get users list:<br />" + caught.getMessage());
            }

            public void onSuccess(List<User> result) {
                modal.hide();
                List<UserRecord> dataList = new ArrayList<UserRecord>();

                for (User u : result) {
                    dataList.add(new UserRecord(u.getFirstName(), u.getLastName(),
                            u.getEmail(), u.getInstitution(), u.getPhone(),
                            u.isConfirmed(), u.getFolder()));
                }
                grid.setData(dataList.toArray(new UserRecord[]{}));
            }
        };
        modal.show("Loading Users...", true);
        service.getUsers(callback);
    }

    private void remove(String email) {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to remove user:<br />" + caught.getMessage());
            }

            public void onSuccess(Void result) {
                modal.hide();
                SC.say("The user was successfully removed!");
                loadData();
            }
        };
        modal.show("Deleting user '" + email + "'...", true);
        service.removeUser(email, callback);
    }

    private void edit(String email) {
        ManageUsersTab usersTab = (ManageUsersTab) Layout.getInstance().
                getTab(CoreConstants.TAB_MANAGE_USERS);
        usersTab.setUser(email);
    }
}
