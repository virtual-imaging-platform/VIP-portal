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
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import fr.insalyon.creatis.vip.core.client.Modules;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class UsersLayout extends VLayout {

    private ModalWindow modal;
    private ListGrid grid;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;
    private DetailViewer detailViewer;

    public UsersLayout() {

        this.setWidth100();
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        configureGrid();
        modal = new ModalWindow(grid);

        this.addMember(new UsersToolStrip());
        this.addMember(grid);

        loadData();
    }

    private void configureGrid() {

        grid = new ListGrid() {

            @Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {

                if (getFieldName(colNum).equals("lastLogin")) {
                    UserRecord userRecord = (UserRecord) record;
                    long now = new Date().getTime();
                    long halfMonthDate = now - ((long) 15 * 24 * 3600000);
                    long oneMonthDate = now - ((long) 30 * 24 * 3600000);
                    long threeMonthsDate = now - ((long) 90 * 24 * 3600000);

                    if (userRecord.getDate().getTime() < threeMonthsDate) {
                        return "color:#D64949;";
                    } else if (userRecord.getDate().getTime() < oneMonthDate) {
                        return "color:#D68E63;";
                    } else if (userRecord.getDate().getTime() < halfMonthDate) {
                        return "color:#66CC66;";
                    } else {
                        return "color:#339900;";
                    }
                }
                return super.getCellCSSText(record, rowNum, colNum);
            }

            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {

                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);
                    rollOverCanvas.addMember(FieldUtil.getImgButton(
                            CoreConstants.ICON_EDIT, "Edit User", new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            edit(rollOverRecord.getAttribute("username"),
                                    rollOverRecord.getAttribute("email"),
                                    rollOverRecord.getAttributeAsBoolean("confirmed"),
                                    rollOverRecord.getAttribute("level"),
                                    rollOverRecord.getAttribute("countryCode"));
                        }
                    }));

                    rollOverCanvas.addMember(FieldUtil.getImgButton(
                            CoreConstants.ICON_DELETE, "Delete User", new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            final String email = rollOverRecord.getAttribute("email");
                            SC.ask("Do you really want to remove the user \""
                                    + email + "\"?", new BooleanCallback() {

                                @Override
                                public void execute(Boolean value) {
                                    if (value) {
                                        remove(email);
                                    }
                                }
                            });
                        }
                    }));
                }
                return rollOverCanvas;
            }

            @Override
            protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {

                DetailViewerField registrationField = new DetailViewerField("registration", "Registration");
                registrationField.setDateFormatter(DateDisplayFormat.TOUSSHORTDATETIME);

                DetailViewerField lastLoginField = new DetailViewerField("lastLogin", "Last Login");
                lastLoginField.setDateFormatter(DateDisplayFormat.TOUSSHORTDATETIME);

                detailViewer = new DetailViewer();
                detailViewer.setWidth(400);
                detailViewer.setFields(
                        new DetailViewerField("level", "Level"),
                        new DetailViewerField("email", "Email"),
                        new DetailViewerField("username", "Name"),
                        new DetailViewerField("institution", "Institution"),
                        new DetailViewerField("phone", "Phone"),
                        new DetailViewerField("countryName", "Country"),
                        registrationField, lastLoginField);
                detailViewer.setData(new Record[]{record});

                return detailViewer;
            }
        };
        grid.setWidth100();
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowAllRecords(false);
        grid.setShowEmptyMessage(true);
        grid.setShowRowNumbers(true);
        grid.setCanHover(true);
        grid.setShowHover(true);
        grid.setShowHoverComponents(true);
        grid.setEmptyMessage("<br>No data available.");

        ListGridField confirmedField = new ListGridField("confirmed", "Confirmed");
        confirmedField.setType(ListGridFieldType.BOOLEAN);

        grid.setFields(confirmedField,
                FieldUtil.getIconGridField("countryCodeIcon"),
                new ListGridField("username", "Name"),
                FieldUtil.getDateField("lastLogin", "Last Login"),
                new ListGridField("email", "Email"));
        grid.setSortField("firstName");
        grid.setSortDirection(SortDirection.ASCENDING);

        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {

                ListGridRecord record = event.getRecord();
                edit(record.getAttribute("username"),
                        record.getAttribute("email"),
                        record.getAttributeAsBoolean("confirmed"),
                        record.getAttribute("level"),
                        record.getAttribute("countryCode"));
            }
        });

        grid.addRowContextClickHandler(new RowContextClickHandler() {

            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                event.cancel();
                new UsersContextMenu(modal, event.getRecord().getAttribute("email"),
                        event.getRecord().getAttributeAsBoolean("confirmed")).showContextMenu();
            }
        });
    }

    public void loadData() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get users list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<User> result) {
                modal.hide();
                List<UserRecord> dataList = new ArrayList<UserRecord>();

                for (User user : result) {
                    dataList.add(new UserRecord(user));
                }
                grid.setData(dataList.toArray(new UserRecord[]{}));
            }
        };
        modal.show("Loading Users...", true);
        service.getUsers(callback);
    }

    private void remove(String email) {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<User> callback = new AsyncCallback<User>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to remove user:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(User result) {
                Modules.getInstance().userRemoved(result);
                modal.hide();
                SC.say("The user was successfully removed!");
                loadData();
            }
        };
        modal.show("Deleting user '" + email + "'...", true);
        service.removeUser(email, callback);
    }

    /**
     *
     * @param name
     * @param email
     * @param confirmed
     * @param level
     * @param countryCode
     */
    private void edit(String userName, String email, boolean confirmed,
            String level, String countryCode) {

        ManageUsersTab usersTab = (ManageUsersTab) Layout.getInstance().
                getTab(CoreConstants.TAB_MANAGE_USERS);
        usersTab.setUser(userName, email, confirmed, level, countryCode);
    }
}
