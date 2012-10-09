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
package fr.insalyon.creatis.vip.core.client.view.system.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import fr.insalyon.creatis.vip.core.client.bean.Group;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditUserLayout extends AbstractFormLayout {

    private Label nameLabel;
    private Label emailLabel;
    private SelectItem levelPickList;
    private SelectItem groupsPickList;
    private SelectItem countryPickList;
    private CheckboxItem confirmedField;
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

        groupsPickList = new SelectItem();
        groupsPickList.setShowTitle(false);
        groupsPickList.setMultiple(true);
        groupsPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        groupsPickList.setWidth(350);

        countryPickList = new SelectItem();
        countryPickList.setShowTitle(false);
        countryPickList.setValueMap(CountryCode.getCountriesMap());
        countryPickList.setValueIcons(CountryCode.getCodesMap());
        countryPickList.setImageURLPrefix(CoreConstants.FOLDER_FLAGS);
        countryPickList.setImageURLSuffix(".png");
        countryPickList.setRequired(true);
        countryPickList.setWidth(350);

        confirmedField = new CheckboxItem();
        confirmedField.setTitle("Confirmed");
        confirmedField.setDisabled(true);
        confirmedField.setWidth(350);

        saveButton = WidgetUtil.getIButton("Save", CoreConstants.ICON_SAVED,
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (levelPickList.validate() & countryPickList.validate()) {

                            String[] values = groupsPickList.getValues();
                            Map<String, CoreConstants.GROUP_ROLE> map = new HashMap<String, CoreConstants.GROUP_ROLE>();

                            for (String v : values) {
                                if (v.equals(CoreConstants.GROUP_SUPPORT)) {
                                    map.put(v, CoreConstants.GROUP_ROLE.User);

                                } else {
                                    String name = v.substring(0, v.indexOf(" ("));
                                    CoreConstants.GROUP_ROLE role = v.contains("("
                                            + CoreConstants.GROUP_ROLE.Admin.name() + ")")
                                            ? CoreConstants.GROUP_ROLE.Admin
                                            : CoreConstants.GROUP_ROLE.User;

                                    if (map.get(name) == null || role == CoreConstants.GROUP_ROLE.Admin) {
                                        map.put(name, role);
                                    }
                                }
                            }
                            save(emailLabel.getContents(),
                                    UserLevel.valueOf(levelPickList.getValueAsString()),
                                    CountryCode.valueOf(countryPickList.getValueAsString()),
                                    map);
                        }
                    }
                });
        saveButton.setDisabled(true);

        this.addMember(nameLabel);
        this.addMember(emailLabel);
        addField("Level", levelPickList);
        addField("Groups", groupsPickList);
        addField("Country", countryPickList);
        this.addMember(FieldUtil.getForm(confirmedField));
        this.addMember(saveButton);
    }

    /**
     * Sets a user to edit.
     *
     * @param name User's name
     * @param email User's email
     * @param confirmed If the user confirmed his account
     * @param level User's level
     * @param countryCode User's country code
     */
    public void setUser(String name, String email, boolean confirmed,
            String level, String countryCode) {

        this.nameLabel.setContents(name);
        this.emailLabel.setContents(email);
        this.confirmedField.setValue(confirmed);
        this.levelPickList.setValue(level);
        this.countryPickList.setValue(countryCode);

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<Map<Group, GROUP_ROLE>> callback = new AsyncCallback<Map<Group, GROUP_ROLE>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<Group, GROUP_ROLE> result) {

                List<String> userGroups = new ArrayList<String>();

                for (Group group : result.keySet()) {
                    if (result.get(group) != CoreConstants.GROUP_ROLE.None) {
                        userGroups.add(
                                group.getName().equals(CoreConstants.GROUP_SUPPORT)
                                ? group.getName()
                                : group.getName() + " (" + result.get(group).name() + ")");
                    }
                }
                groupsPickList.setValues(userGroups.toArray(new String[]{}));
                saveButton.setDisabled(false);
            }
        };
        service.getUserGroups(email, callback);
    }

    /**
     *
     * @param email User's email
     * @param level User's level
     * @param groups List of groups
     */
    private void save(String email, UserLevel level, CountryCode countryCode,
            Map<String, CoreConstants.GROUP_ROLE> groups) {

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
                groupsPickList.setValues(new String[]{});
                countryPickList.setValues(new String[]{});
                confirmedField.setValue(false);
                ((ManageUsersTab) Layout.getInstance().getTab(CoreConstants.TAB_MANAGE_USERS)).loadUsers();
                Layout.getInstance().setNoticeMessage("User successfully updated.");
            }
        };
        WidgetUtil.setLoadingIButton(saveButton, "Updating user...");
        service.updateUser(email, level, countryCode, groups, callback);
    }

    /**
     * Loads list of groups.
     */
    private void loadData() {

        levelPickList.setValueMap(UserLevel.toStringArray());

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<Group>> callback = new AsyncCallback<List<Group>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get groups list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Group> result) {

                List<String> dataList = new ArrayList<String>();
                for (Group g : result) {
                    if (g.getName().equals(CoreConstants.GROUP_SUPPORT)) {
                        dataList.add(g.getName());
                    } else {
                        dataList.add(g.getName() + " (" + CoreConstants.GROUP_ROLE.Admin.name() + ")");
                        dataList.add(g.getName() + " (" + CoreConstants.GROUP_ROLE.User.name() + ")");
                    }
                }
                groupsPickList.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        service.getGroups(callback);
    }
}
