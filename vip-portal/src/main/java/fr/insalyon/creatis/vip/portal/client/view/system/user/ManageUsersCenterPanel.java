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
package fr.insalyon.creatis.vip.portal.client.view.system.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegExp;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.layout.VerticalLayout;
import fr.insalyon.creatis.vip.portal.client.bean.User;
import fr.insalyon.creatis.vip.portal.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.portal.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.common.panel.CheckboxPanel;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import fr.insalyon.creatis.vip.portal.client.view.common.panel.AbstractPanel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class ManageUsersCenterPanel extends AbstractPanel {

    private static ManageUsersCenterPanel instance;
    private Store groupsStore;
    private Store usersStore;
    private ComboBox groupsCB;
    private ComboBox usersCB;
    private FormPanel formPanel;
    private FieldSet fieldSet;
    private Toolbar topToolbar;
    private ToolbarButton newButton;
    private Button saveButton;
    private Button removeButton;
    private boolean newUser;
    private TextField dnField;
    private CheckboxPanel groupPanel;

    public static ManageUsersCenterPanel getInstance() {
        if (instance == null) {
            instance = new ManageUsersCenterPanel();
        }
        return instance;
    }

    private ManageUsersCenterPanel() {
        super(new String[]{"Administrator"});
    }

    @Override
    protected void buildPanel() {

        this.setTitle("User Management");
        this.setLayout(new VerticalLayout(15));
        this.setMargins(0, 0, 0, 0);
        this.setPaddings(10, 5, 5, 5);
        this.setAutoScroll(true);

        // FormPanel
        formPanel = new FormPanel();
        formPanel.setBorder(false);
        fieldSet = new FieldSet("User");
        fieldSet.setWidth(600);
        formPanel.add(fieldSet);
        formPanel.setVisible(false);

        groupsStore = new SimpleStore(new String[]{"groups-filter"}, new Object[][]{new Object[]{}});
        groupsStore.load();

        groupsCB = FieldUtil.getComboBox("combo-groups-filter", "Group", 250, 
                "Select a Group...", groupsStore, "groups-filter");
        groupsCB.setId("combo-groups-filter");
        groupsCB.setTypeAhead(true);
        groupsCB.setValueField("groups-filter");
        groupsCB.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {
                usersCB.setValue("");
                usersStore.filter("groups-filter", new RegExp(comboBox.getValue()));
            }
        });
        this.add(groupsCB);

        usersStore = new SimpleStore(new String[]{"users-name", "users-dn", "groups-filter"}, new Object[][]{new Object[]{}});
        usersStore.load();

        usersCB = FieldUtil.getComboBox("combo-users-name", "User Name", 250,
                "Select a User...", usersStore, "users-name");
        usersCB.setId("combo-users-name");
        usersCB.setTypeAhead(true);
        usersCB.setValueField("users-dn");
        usersCB.setLinked(true);
        usersCB.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {

                String dn = comboBox.getValue();

                ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                final AsyncCallback<User> callback = new AsyncCallback<User>() {

                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Error", "Error executing get user\n" + caught.getMessage());
                    }

                    public void onSuccess(User result) {
                        newUser = false;
                        usersCB.setValue("");
                        removeButton.setVisible(true);
                        dnField.setReadOnly(true);
                        dnField.setValue(result.getDistinguishedName());
                        formPanel.setVisible(true);
                        loadGroupData(result.getGroups());
                    }
                };
                service.getUser(dn, callback);
            }
        });
        this.add(usersCB);

        // FormPanel
        MultiFieldPanel dnMFP = FieldUtil.getMultiFieldPanel("mfp-users-name");

        dnField = FieldUtil.getTextField("users-name", 400, "DN", false);
        dnMFP.addToRow(dnField, 510);
        fieldSet.add(dnMFP);

        groupPanel = new CheckboxPanel("users-group", "Groups:", 350);
        fieldSet.add(groupPanel);

        // Form Buttons
        saveButton = new Button("Save", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                String dn = dnField.getValueAsString();
                List<String> groups = groupPanel.getSelectedValues();

                if (dn.trim().equals("") || groups.isEmpty()) {
                    MessageBox.alert("Error", "You should provide the dn and select at least one group for the user.");
                    return;
                }

                Map<String, String> groupsMap = new HashMap<String, String>();
                for (String groupValue : groups) {
                    if (groupValue.equals("Administrator")) {
                        groupsMap.put(groupValue, "admin");
                    } else {
                        String[] gv = groupValue.split(" - ");
                        if (groupsMap.containsKey(gv[0])) {
                            if (!groupsMap.get(gv[0]).equals("admin")) {
                                groupsMap.put(gv[0], gv[1].toLowerCase());
                            }
                        } else {
                            groupsMap.put(gv[0], gv[1].toLowerCase());
                        }
                    }
                }

                save(new User(dn, groupsMap));
            }
        });
        formPanel.addButton(saveButton);

        removeButton = new Button("Remove", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                final String dn = dnField.getValueAsString();

                MessageBox.confirm("Confirm", "Do you really want to remove the user \"" + dn + "\"?",
                        new MessageBox.ConfirmCallback() {

                            public void execute(String btnID) {
                                if (btnID.toLowerCase().equals("yes")) {
                                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

                                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                        public void onFailure(Throwable caught) {
                                            MessageBox.alert("Error", "Error executing remove user\n" + caught.getMessage());
                                        }

                                        public void onSuccess(Void result) {
                                            formPanel.setVisible(false);
                                            loadComboUserData();
                                            MessageBox.alert("The user was successfully removed!");
                                        }
                                    };
                                    service.removeUser(dn, callback);
                                }
                            }
                        });
            }
        });
        formPanel.addButton(removeButton);

        this.add(formPanel);

        // Toolbar
        topToolbar = new Toolbar();
        newButton = new ToolbarButton("New User", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                removeButton.setVisible(false);
                formPanel.setVisible(true);
                dnField.setValue("");
                dnField.setReadOnly(false);
                newUser = true;
                loadGroupData(new HashMap<String, String>());
            }
        });
        topToolbar.addButton(newButton);
        this.setTopToolbar(topToolbar);

        loadComboUserData();
        loadComboGroupData();
    }

    private void loadComboUserData() {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get users list\n" + caught.getMessage());
            }

            public void onSuccess(List<User> result) {
                Object[][] data = new Object[result.size()][3];
                for (int i = 0; i < result.size(); i++) {
                    User u = result.get(i);
                    data[i][0] = u.getOrganizationUnit() + " / " + u.getCanonicalName();
                    data[i][1] = u.getDistinguishedName();
                    String groups = "";
                    for (String groupName : u.getGroups().keySet()) {
                        groups += groupName + "##";
                    }
                    data[i][2] = groups;
                }

                MemoryProxy proxy = new MemoryProxy(data);
                usersStore.setDataProxy(proxy);
                usersStore.load();
                usersStore.commitChanges();
                groupsCB.setValue("");
            }
        };
        service.getUsers(callback);
    }

    private void loadComboGroupData() {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get groups list\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                Object[][] data = new Object[result.size()][1];
                for (int i = 0; i < result.size(); i++) {
                    data[i][0] = result.get(i);
                }

                MemoryProxy proxy = new MemoryProxy(data);
                groupsStore.setDataProxy(proxy);
                groupsStore.load();
                groupsStore.commitChanges();
            }
        };
        service.getGroups(callback);
    }

    private void loadGroupData(final Map<String, String> groups) {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get groups list\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                groupPanel.removeCheckBoxes();
                for (String groupName : result) {
                    if (!groupName.equals("Administrator")) {
                        String role = groups.get(groupName);
                        boolean isAdmin = false;
                        boolean isUser = false;
                        if (role != null) {
                            if (role.equals("admin")) {
                                isAdmin = true;
                            } else {
                                isUser = true;
                            }
                        }
                        groupPanel.addCheckbox(groupName + " - Admin", groupName + "--admin", isAdmin);
                        groupPanel.addCheckbox(groupName + " - User", groupName + "--user", isUser);
                    } else {
                        boolean checked = groups.get(groupName) != null ? true : false;
                        groupPanel.addCheckbox(groupName, groupName, checked);
                    }
                }
                groupPanel.doLayout();
            }
        };
        service.getGroups(callback);
    }

    private void save(User user) {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

        if (newUser) {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Error executing add user\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    processSaveResult(result);
                }
            };
            service.addUser(user, callback);

        } else {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Error executing update user\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    processSaveResult(result);
                }
            };
            service.updateUser(user, callback);
        }
    }

    private void processSaveResult(String result) {
        if (!result.contains("Error: ")) {
            formPanel.setVisible(false);
            loadComboUserData();
        }
        MessageBox.alert(result);
    }
}
