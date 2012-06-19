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
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants.GROUP_ROLE;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
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
 * @author Rafael Silva
 */
public class EditUserStackSection extends SectionStackSection {

    private ModalWindow modal;
    private VLayout editUserLayout;
    private Label nameLabel;
    private Label emailLabel;
    private SelectItem levelPickList;
    private SelectItem groupsPickList;
    private SelectItem countryPickList;
    private CheckboxItem confirmedField;

    public EditUserStackSection() {

        this.setTitle("Edit User");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        VLayout vLayout = new VLayout(15);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setMargin(5);

        configure();

        vLayout.addMember(editUserLayout);
        modal = new ModalWindow(editUserLayout);

        this.addItem(vLayout);
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

        IButton saveButton = new IButton("Save");
        saveButton.setWidth(70);
        saveButton.addClickHandler(new ClickHandler() {

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

        editUserLayout = WidgetUtil.getVIPLayout(380, 300);
        editUserLayout.addMember(nameLabel);
        editUserLayout.addMember(emailLabel);
        addField("Level", levelPickList);
        addField("Groups", groupsPickList);
        addField("Country", countryPickList);
        editUserLayout.addMember(FieldUtil.getForm(confirmedField));
        editUserLayout.addMember(saveButton);
    }

    private void addField(String title, FormItem item) {

        editUserLayout.addMember(WidgetUtil.getLabel("<b>" + title + "</b>", 15));
        editUserLayout.addMember(FieldUtil.getForm(item));
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
        final AsyncCallback<Map<String, CoreConstants.GROUP_ROLE>> callback = new AsyncCallback<Map<String, CoreConstants.GROUP_ROLE>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get list of groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String, GROUP_ROLE> result) {
                modal.hide();

                List<String> groups = new ArrayList<String>();
                for (String group : result.keySet()) {

                    if (group.equals(CoreConstants.GROUP_SUPPORT)) {
                        groups.add(group);

                    } else {
                        if (result.get(group) == CoreConstants.GROUP_ROLE.Admin) {
                            groups.add(group + " (" + CoreConstants.GROUP_ROLE.Admin.name() + ")");

                        } else {
                            groups.add(group + " (" + CoreConstants.GROUP_ROLE.User.name() + ")");
                        }
                    }
                }
                groupsPickList.setValues(groups.toArray(new String[]{}));
            }
        };
        modal.show("Loading user's groups...", true);
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
                modal.hide();
                SC.warn("Unable to update user:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                nameLabel.setContents("");
                emailLabel.setContents("");
                levelPickList.setValues(new String[]{});
                groupsPickList.setValues(new String[]{});
                countryPickList.setValues(new String[]{});
                confirmedField.setValue(false);
                ((ManageUsersTab) Layout.getInstance().getTab(CoreConstants.TAB_MANAGE_USERS)).loadUsers();
                SC.say("User successfully updated.");
            }
        };
        modal.show("Updating user...", true);
        service.updateUser(email, level, countryCode, groups, callback);
    }

    /**
     * Loads list of groups.
     */
    private void loadData() {

        levelPickList.setValueMap(UserLevel.toStringArray());

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                SC.warn("Unable to get groups list:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {

                List<String> dataList = new ArrayList<String>();
                for (String g : result) {
                    if (g.equals(CoreConstants.GROUP_SUPPORT)) {
                        dataList.add(g);
                    } else {
                        dataList.add(g + " (" + CoreConstants.GROUP_ROLE.Admin.name() + ")");
                        dataList.add(g + " (" + CoreConstants.GROUP_ROLE.User.name() + ")");
                    }
                }
                groupsPickList.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        service.getGroups(callback);
    }
}
