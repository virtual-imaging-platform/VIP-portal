/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.portal.client.view.system.configuration.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.portal.client.bean.User;
import fr.insalyon.creatis.vip.portal.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.portal.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class EditUserStackSection extends SectionStackSection {

    private boolean newUser = true;
    private DynamicForm form;
    private TextItem nameItem;
    private SelectItem groupsPickList;

    public EditUserStackSection() {

        this.setTitle("Add User");
        this.setCanCollapse(true);
        this.setExpanded(true);
        this.setResizeable(true);

        configureForm();

        VLayout vLayout = new VLayout(15);
        vLayout.setHeight100();
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setMargin(5);
        vLayout.addMember(form);

        this.addItem(vLayout);
        loadData();
    }

    private void configureForm() {
        form = new DynamicForm();
        form.setWidth(500);

        nameItem = new TextItem("name", "DN");
        nameItem.setWidth(350);
        nameItem.setRequired(true);

        groupsPickList = new SelectItem();
        groupsPickList.setTitle("Groups");
        groupsPickList.setMultiple(true);
        groupsPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        groupsPickList.setWidth(350);

        ButtonItem saveItem = new ButtonItem("Save");
        saveItem.setWidth(50);
        saveItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    
                    String[] values = groupsPickList.getValues();                   
                    Map<String, String> map = new HashMap<String, String>();
                    
                    for (String v : values) {
                        if (v.equals("Administrator")) {
                            map.put(v, "admin");
                        } else {
                            String name = v.substring(0, v.indexOf(" ("));
                            String role = v.contains("(admin)") ? "admin" : "user";
                            if (map.get(name) == null || role.equals("admin")) {
                                map.put(name, role);
                            }
                        }
                    }
                    save(new User(nameItem.getValueAsString(), map));
                }
            }
        });

        form.setFields(nameItem, groupsPickList, saveItem);
    }

    /**
     * Sets a user to edit or creates a blank form.
     * 
     * @param name User name
     * @param groups User's groups
     */
    public void setUser(String name, String groups) {
        if (name != null) {
            this.setTitle("Editing User: " + name);
            this.nameItem.setValue(name);
            this.nameItem.setDisabled(true);
            this.groupsPickList.setValues(groups.split(", "));
            this.newUser = false;
        } else {
            this.setTitle("Add User");
            this.nameItem.setValue("");
            this.nameItem.setDisabled(false);
            this.groupsPickList.setValues(new String[]{});
            this.newUser = true;
        }
    }

    private void save(User user) {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

        if (newUser) {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    SC.warn("Error executing add user\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        ManageUsersTab usersTab = (ManageUsersTab) Layout.getInstance().
                                getTab("manage-users-tab");
                        usersTab.loadUsers();
                        setUser(null, null);
                    }
                    SC.say(result);
                }
            };
            service.addUser(user, callback);

        } else {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    SC.warn("Error executing update user\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        ManageUsersTab usersTab = (ManageUsersTab) Layout.getInstance().
                                getTab("manage-users-tab");
                        usersTab.loadUsers();
                        setUser(null, null);
                    }
                    SC.say(result);
                }
            };
            service.updateUser(user, callback);
        }
    }

    /**
     * Loads groups list
     */
    private void loadData() {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get groups list\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {

                List<String> dataList = new ArrayList<String>();
                for (String g : result) {
                    if (g.equals("Administrator")) {
                        dataList.add(g);
                    } else {
                        dataList.add(g + " (admin)");
                        dataList.add(g + " (user)");
                    }
                }
                groupsPickList.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        service.getGroups(callback);
    }
}
