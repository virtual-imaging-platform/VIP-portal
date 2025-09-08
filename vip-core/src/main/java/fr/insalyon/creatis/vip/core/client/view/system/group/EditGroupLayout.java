package fr.insalyon.creatis.vip.core.client.view.system.group;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.system.user.UserRecord;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditGroupLayout extends AbstractFormLayout {

    private String oldName = null;
    private boolean newGroup = true;

    private TextItem nameItem;
    private CheckboxItem isPublicField;
    private SelectItem typeFieldList;
    private CheckboxItem isAutoField;

    private IButton saveButton;
    private IButton removeButton;
    private ListGrid itemsGrid;
    private ListGrid usersGrid;
    private ModalWindow usersGridModal;
    private Label dynamicLabel;
    private ModalWindow itemsGridModal;
    private HLayout rollOverCanvas;
    private ListGridRecord rollOverRecord;

    public EditGroupLayout() {
        super(380, 450);
        addTitle("Add/Edit Group", CoreConstants.ICON_GROUP);

        configure();
    }

    private void configure() {
        nameItem = FieldUtil.getTextItem(350, null);

        isPublicField = new CheckboxItem();
        isPublicField.setShowTitle(false);
        isPublicField.setWidth(350);

        typeFieldList = new SelectItem();
        typeFieldList.setShowTitle(false);
        typeFieldList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        typeFieldList.setValueMap(GroupType.getValues());
        typeFieldList.setWidth(350);

        isAutoField = new CheckboxItem();
        isAutoField.setShowTitle(false);
        isAutoField.setWidth(350);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (nameItem.validate()) {
                            save(nameItem.getValueAsString().trim(),
                                    isPublicField.getValueAsBoolean(),
                                    typeFieldList.getValueAsString(),
                                    isAutoField.getValueAsBoolean());
                        }
                    }
                });

        removeButton = WidgetUtil.getIButton("Remove", CoreConstants.ICON_DELETE,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (nameItem.validate()) {
                            remove(nameItem.getValueAsString().trim());
                        }
                    }
                });
        removeButton.setDisabled(true);

        usersGrid = buildUsersGrid();
        usersGridModal = new ModalWindow(usersGrid);

        itemsGrid = buildItemsGrid();
        itemsGridModal = new ModalWindow(itemsGrid);

        dynamicLabel = WidgetUtil.getLabel("", 15);

        addField("Name", nameItem);
        addField("Public", isPublicField);
        addField("Group Type", typeFieldList);
        addField("Auto", isAutoField);
        addMember(WidgetUtil.getLabel("<b>Users</b>", 15));
        addMember(usersGrid);
        addMember(dynamicLabel);
        addMember(itemsGrid);
        addButtons(saveButton, removeButton);
    }

    public void setGroup(String name, boolean isPublic, String type, boolean auto) {
        if (name != null) {
            oldName = name;
            nameItem.setValue(name);
            isPublicField.setValue(isPublic);
            typeFieldList.setValue(type);
            isAutoField.setValue(auto);
            newGroup = false;
            removeButton.setDisabled(false);
            loadUsers();
            loadItems(name);
            dynamicLabel.setContents("<b>" + type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase() + " List" + "</b>");

        } else {
            oldName = null;
            nameItem.setValue("");
            isPublicField.setValue(true);
            newGroup = true;
            removeButton.setDisabled(true);
        }
    }

    private void save(String name, boolean isPublic, String type, boolean auto) {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        WidgetUtil.setLoadingIButton(saveButton, "Saving...");

        if (newGroup) {
            service.addGroup(new Group(name, isPublic, type, auto), getCallback("add"));
        } else {
            service.updateGroup(oldName, new Group(name, isPublic, type, auto), getCallback("update"));
        }
    }

    private void remove(final String name) {
        SC.ask("Do you really want to remove \"" + name + "\" group?", new BooleanCallback() {
            @Override
            public void execute(Boolean value) {
                if (value) {
                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    WidgetUtil.setLoadingIButton(removeButton, "Removing...");
                    service.removeGroup(name, getCallback("remove"));
                }
            }
        });
    }

    private AsyncCallback<Void> getCallback(final String text) {
        return new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setWarningMessage("Unable to " + text + " group:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                WidgetUtil.resetIButton(removeButton, "Remove", CoreConstants.ICON_DELETE);
                Layout.getInstance().setNoticeMessage("You successfully " + text + " a group:<br />");

                setGroup(null, false, null, false);
                ((ManageGroupsTab) Layout.getInstance().getTab(
                        CoreConstants.TAB_MANAGE_GROUPS)).loadGroups();
            }
        };
    }

    private ListGrid buildUsersGrid() {
        ListGrid grid = new ListGrid() {
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {

                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    rollOverCanvas.addMember(FieldUtil.getImgButton(
                            CoreConstants.ICON_DELETE, "Remove user from this group",
                            new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    final String email = rollOverRecord.getAttribute("email");
                                    SC.ask("Do you really want to remove the user \""
                                            + email + "\" from this group?", new BooleanCallback() {
                                        @Override
                                        public void execute(Boolean value) {
                                            if (value) {
                                                removeUserFromGroup(email);
                                            }
                                        }
                                    });
                                }
                            }));
                }
                return rollOverCanvas;
            }
        };
        setGridOptions(
            grid,
            "username", 
            FieldUtil.getIconGridField("countryCodeIcon"),
            new ListGridField("username", "Name"));
        return grid;
    }

    private ListGrid buildItemsGrid() {
        ListGrid grid = new ListGrid() {
            @Override
            protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {

                rollOverRecord = this.getRecord(rowNum);

                if (rollOverCanvas == null) {
                    rollOverCanvas = new HLayout(3);
                    rollOverCanvas.setSnapTo("TR");
                    rollOverCanvas.setWidth(50);
                    rollOverCanvas.setHeight(22);

                    rollOverCanvas.addMember(FieldUtil.getImgButton(
                            CoreConstants.ICON_DELETE, "Remove item from this group",
                            new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    final String item = rollOverRecord.getAttribute("item");
                                    SC.ask("Do you really want to remove the item \""
                                            + item + "\" from this group?", new BooleanCallback() {
                                        @Override
                                        public void execute(Boolean value) {
                                            if (value) {
                                                removeItemFromGroup(item, nameItem.getValueAsString());
                                            }
                                        }
                                    });
                                }
                            }));
                }
                return rollOverCanvas;
            }
        };
        setGridOptions(
            grid, 
            "item",
            new ListGridField("item", "Name"));
        return grid;
    }

    private void setGridOptions(ListGrid grid, String sortfield, ListGridField... fields) {
        grid.setWidth(350);
        grid.setHeight100();
        grid.setShowRollOverCanvas(true);
        grid.setShowRowNumbers(true);
        grid.setCanHover(true);
        grid.setShowHoverComponents(true);
        grid.setEmptyMessage("<br>No data available.");
        grid.setFields(fields);
        grid.setSortField(sortfield);
    }

    private void loadUsers() {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<User>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                usersGridModal.hide();
                Layout.getInstance().setWarningMessage("Unable to load users:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<User> result) {
                usersGridModal.hide();
                List<UserRecord> dataList = new ArrayList<>();

                for (User user : result) {
                    dataList.add(new UserRecord(user));
                }
                usersGrid.setData(dataList.toArray(new UserRecord[]{}));
            }
        };
        usersGridModal.show("Loading users from \"" + oldName + "\" group...", true);
        service.getUsersFromGroup(oldName, callback);
    }

    private void loadItems(String groupName) {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                itemsGridModal.hide();
                Layout.getInstance().setWarningMessage("Unable to load items:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                itemsGridModal.hide();
                List<ItemGroupRecord> dataList = new ArrayList<>();

                result.forEach((i) -> {
                    dataList.add(new ItemGroupRecord(i));
                });
                itemsGrid.setData(dataList.toArray(new ItemGroupRecord[]{}));
            }
        };
        itemsGridModal.show("Loading items from \"" + groupName + "\" group...", true);
        service.getItemsGroup(groupName, callback);
    }

    private void removeItemFromGroup(String item, String groupName) {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                itemsGridModal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove item:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                itemsGridModal.hide();
                loadItems(groupName);
            }
        };
        itemsGridModal.show("Removing \"" + item + "\"<br />from \"" + groupName + "\"...", true);
        service.removeItemFromGroup(item, groupName, callback);
    }

    private void removeUserFromGroup(String email) {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                usersGridModal.hide();
                Layout.getInstance().setWarningMessage("Unable to remove user:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                usersGridModal.hide();
                loadUsers();
            }
        };
        usersGridModal.show("Removing \"" + email + "\"<br />from \"" + oldName + "\"...", true);
        service.removeUserFromGroup(email, oldName, callback);
    }
}
