package fr.insalyon.creatis.vip.core.client.view.system.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;

import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditUserLayout extends AbstractFormLayout {

    private Label nameLabel;
    private Label emailLabel;
    private SelectItem levelPickList;
    private SelectItem groupsAppsPickList;
    private SelectItem groupsRrcsPickList;
    private SelectItem countryPickList;
    private SpinnerItem maxRunningSimulationsItem;
    private CheckboxItem confirmedField;
    private CheckboxItem lockedField;
    private StaticTextItem missingResources;
    private IButton saveButton;

    public EditUserLayout() {
        super(380, 300);
        addTitle("Edit User", CoreConstants.ICON_PERSONAL);

        configure();
        loadData();
    }

    private void configure() {

        nameLabel = WidgetUtil.getLabel("", 15);
        nameLabel.setCanSelectText(true);

        emailLabel = WidgetUtil.getLabel("", 15);
        emailLabel.setCanSelectText(true);

        levelPickList = new SelectItem();
        levelPickList.setShowTitle(false);
        levelPickList.setWidth(350);
        levelPickList.setRequired(true);

        groupsAppsPickList = new SelectItem();
        groupsAppsPickList.setShowTitle(false);
        groupsAppsPickList.setMultiple(true);
        groupsAppsPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        groupsAppsPickList.setWidth(350);

        groupsRrcsPickList = new SelectItem();
        groupsRrcsPickList.setShowTitle(false);
        groupsRrcsPickList.setMultiple(true);
        groupsRrcsPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        groupsRrcsPickList.setWidth(350);

        countryPickList = new SelectItem();
        countryPickList.setShowTitle(false);
        countryPickList.setValueMap(CountryCode.getCountriesMap());
        countryPickList.setValueIcons(CountryCode.getCodesMap());
        countryPickList.setImageURLPrefix(CoreConstants.FOLDER_FLAGS);
        countryPickList.setImageURLSuffix(".png");
        countryPickList.setRequired(true);
        countryPickList.setWidth(350);

        maxRunningSimulationsItem = new SpinnerItem();
        maxRunningSimulationsItem.setShowTitle(false);
        maxRunningSimulationsItem.setDefaultValue(1);
        maxRunningSimulationsItem.setMin(1);
        maxRunningSimulationsItem.setMax(100);
        maxRunningSimulationsItem.setStep(1);

        confirmedField = new CheckboxItem();
        confirmedField.setTitle("Confirmed");
        confirmedField.setDisabled(true);
        confirmedField.setWidth(350);
        
        lockedField = new CheckboxItem();
        lockedField.setTitle("Locked");
        lockedField.setDisabled(false);
        lockedField.setWidth(350);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (levelPickList.validate() & countryPickList.validate()) {

                    String[] values = Stream.concat(
                        Arrays.stream(groupsAppsPickList.getValues()),
                        Arrays.stream(groupsRrcsPickList.getValues())).toArray(String[]::new);
                    Map<String, CoreConstants.GROUP_ROLE> map = new HashMap<String, CoreConstants.GROUP_ROLE>();

                    for (String v : values) {
                        String name = v.substring(0, v.indexOf(" ("));
                        CoreConstants.GROUP_ROLE role = v.contains("("
                                + CoreConstants.GROUP_ROLE.Admin.name() + ")")
                                ? CoreConstants.GROUP_ROLE.Admin
                                : CoreConstants.GROUP_ROLE.User;

                        if (map.get(name) == null || role == CoreConstants.GROUP_ROLE.Admin) {
                            map.put(name, role);
                        }
                    }
                    save(emailLabel.getContents(),
                            UserLevel.valueOf(levelPickList.getValueAsString()),
                            CountryCode.valueOf(countryPickList.getValueAsString()),
                            Integer.parseInt(maxRunningSimulationsItem.getValueAsString()),
                            map, lockedField.getValueAsBoolean());
                }
            }
        });
        saveButton.setDisabled(true);

        missingResources = new StaticTextItem();
        missingResources.setTextAlign(Alignment.LEFT);
        missingResources.setWidth(350);

        addMember(nameLabel);
        addMember(emailLabel);
        addField("Level", levelPickList);
        addField("Applications Groups", groupsAppsPickList);
        addField("Resources Groups", groupsRrcsPickList);
        addField("Country", countryPickList);
        addField("Max Running Simulations", maxRunningSimulationsItem);
        addMember(FieldUtil.getForm(confirmedField));
        addMember(FieldUtil.getForm(lockedField));
        addField("Remarks", missingResources);
        addMember(saveButton);
    }

    public void setUser(String name, String email, boolean confirmed,
            String level, String countryCode, int maxRunningSimulations, boolean locked) {

        this.nameLabel.setContents(name);
        this.emailLabel.setContents(email);
        this.confirmedField.setValue(confirmed);
        this.lockedField.setValue(locked);
        this.levelPickList.setValue(level);
        this.countryPickList.setValue(countryCode);
        this.maxRunningSimulationsItem.setValue(maxRunningSimulations);

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Map<Group, GROUP_ROLE>> callback = new AsyncCallback<Map<Group, GROUP_ROLE>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<Group, GROUP_ROLE> result) {

                List<String> userGroupsRsrc = new ArrayList<>();
                List<String> userGroupsApps = new ArrayList<>();

                for (Group group : result.keySet()) {
                    if (result.get(group) != CoreConstants.GROUP_ROLE.None) {
                        if (group.getType().equals(GroupType.APPLICATION)) {
                            userGroupsApps.add(group.getName() + " (" + result.get(group).name() + ")");
                        } else {
                            userGroupsRsrc.add(group.getName() + " (" + result.get(group).name() + ")");
                        }
                    }
                }
                groupsAppsPickList.setValues(userGroupsApps.toArray(new String[]{}));
                groupsRrcsPickList.setValues(userGroupsRsrc.toArray(new String[]{}));
                saveButton.setDisabled(false);
            }
        };
        service.getUserGroups(email, callback);
        loadMissingResources(email);
    }

    private void save(String email, UserLevel level, CountryCode countryCode,
            int maxRunningSimulations, Map<String, CoreConstants.GROUP_ROLE> groups, boolean locked) {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                Layout.getInstance().setWarningMessage("Unable to update user:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                WidgetUtil.resetIButton(saveButton, "Save", CoreConstants.ICON_SAVED);
                saveButton.setDisabled(true);
                nameLabel.setContents("");
                emailLabel.setContents("");
                levelPickList.setValues(new String[]{});
                groupsAppsPickList.setValues(new String[]{});
                groupsRrcsPickList.setValues(new String[]{});
                countryPickList.setValues(new String[]{});
                confirmedField.setValue(false);
                lockedField.setValue(false);
                ((ManageUsersTab) Layout.getInstance().getTab(CoreConstants.TAB_MANAGE_USERS)).loadUsers();
                Layout.getInstance().setNoticeMessage("User successfully updated.");
            }
        };
        WidgetUtil.setLoadingIButton(saveButton, "Updating user...");
        service.updateUser(email, level, countryCode, maxRunningSimulations, groups, locked, callback);
    }

    private void loadData() {
        levelPickList.setValueMap(UserLevel.toStringArray());

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<Group>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get groups list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Group> result) {
                result = result.stream().filter((g) -> ! g.isAuto()).collect(Collectors.toList()); // to avoid admin to manage Automatic groups
                List<String> dataRsrc = new ArrayList<>();
                List<String> dataApps = new ArrayList<>();

                for (Group g : result) {
                    if (g.getType().equals(GroupType.APPLICATION)) {
                        dataApps.add(g.getName() + " (" + CoreConstants.GROUP_ROLE.Admin.name() + ")");
                        dataApps.add(g.getName() + " (" + CoreConstants.GROUP_ROLE.User.name() + ")");
                    } else {
                        dataRsrc.add(g.getName() + " (" + CoreConstants.GROUP_ROLE.Admin.name() + ")");
                        dataRsrc.add(g.getName() + " (" + CoreConstants.GROUP_ROLE.User.name() + ")");
                    }
                }
                groupsAppsPickList.setValueMap(dataApps.toArray(new String[]{}));
                groupsRrcsPickList.setValueMap(dataRsrc.toArray(new String[]{}));
            }
        };
        service.getGroups(callback);
    }

    private void loadMissingResources(String email) {
        missingResources.setValue("");

        final ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get groups list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                if (result.size() != 0) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("This resources groups might be missing for this user: ");
                    builder.append(String.join(", ", result));

                    missingResources.setValue(builder.toString());
                } else {
                    missingResources.setValue("Everything seems good :)");
                }
            }
        };
        service.getMissingGroupsRessources(email, callback);
    }
}
