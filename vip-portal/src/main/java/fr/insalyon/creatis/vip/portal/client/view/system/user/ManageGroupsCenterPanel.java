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
import com.gwtext.client.core.Ext;
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
import fr.insalyon.creatis.vip.common.client.bean.Authentication;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.portal.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.portal.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import fr.insalyon.creatis.vip.portal.client.view.common.panel.AbstractPanel;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ManageGroupsCenterPanel extends AbstractPanel {

    private static ManageGroupsCenterPanel instance;
    private Authentication auth;
    private Store store;
    private ComboBox cb;
    private FormPanel formPanel;
    private FieldSet fieldSet;
    private Toolbar topToolbar;
    private ToolbarButton newButton;
    private Button saveButton;
    private Button removeButton;
    private String oldName;
    private TextField nameField;

    public static ManageGroupsCenterPanel getInstance() {
        if (instance == null) {
            instance = new ManageGroupsCenterPanel();
        }
        return instance;
    }

    private ManageGroupsCenterPanel() {
        super(new String[]{"Administrator"});
        this.auth = Context.getInstance().getAuthentication();
    }

    @Override
    protected void buildPanel() {

        this.setTitle("Group Management");
        this.setId("vip-group-mng");
        this.setLayout(new VerticalLayout(15));
        this.setMargins(0, 0, 0, 0);
        this.setPaddings(10, 5, 5, 5);
        this.setAutoScroll(true);

        // FormPanel
        formPanel = new FormPanel();
        formPanel.setBorder(false);
        fieldSet = new FieldSet("Group");
        fieldSet.setWidth(600);
        formPanel.add(fieldSet);
        formPanel.setVisible(false);

        store = new SimpleStore("groups-name", new Object[]{});
        store.load();

        cb = FieldUtil.getComboBox("combo-groups-name", "Group Name", 250,
                "Select a Group...", store, "groups-name");
        cb.setId("combo-groups-name");
        cb.setTypeAhead(true);
        cb.setValueField("groups-name");
        cb.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {

                String name = comboBox.getValue();
                removeButton.setVisible(true);
                nameField.setValue(name);
                oldName = name;
                formPanel.setVisible(true);
                cb.setValue("");
            }
        });

        this.add(cb);

        // FormPanel
        MultiFieldPanel nameMFP = FieldUtil.getMultiFieldPanel("mfp-group-name");

        nameField = FieldUtil.getTextField("group-name", 400, "Name", false);
        nameMFP.addToRow(nameField, 510);
        fieldSet.add(nameMFP);

        // Form Buttons
        saveButton = new Button("Save", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                String name = nameField.getValueAsString();

                if (name.trim().equals("")) {
                    MessageBox.alert("Error", "You should provide a name to the group.");
                    return;
                }
                save(name);
            }
        });
        formPanel.addButton(saveButton);

        removeButton = new Button("Remove", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                final String name = nameField.getValueAsString();

                if (name.equals("Administrator")) {
                    MessageBox.alert("Error", "You can't remove the System Administrator group.");
                    return;
                }

                MessageBox.confirm("Confirm", "Do you really want to remove the group \"" + name + "\"?",
                        new MessageBox.ConfirmCallback() {

                            public void execute(String btnID) {
                                if (btnID.toLowerCase().equals("yes")) {
                                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

                                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                        public void onFailure(Throwable caught) {
                                            MessageBox.alert("Error", "Error executing remove group\n" + caught.getMessage());
                                            Ext.get("vip-group-mng").unmask();
                                        }

                                        public void onSuccess(Void result) {
                                            formPanel.setVisible(false);
                                            loadComboData();
                                            Ext.get("vip-group-mng").unmask();
                                            MessageBox.alert("The group was successfully removed!");
                                        }
                                    };
                                    service.removeGroup(auth.getProxyFileName(), name, callback);
                                    Ext.get("vip-group-mng").mask("Removing group...");
                                }
                            }
                        });
            }
        });
        formPanel.addButton(removeButton);
        
        this.add(formPanel);

        // Toolbar
        topToolbar = new Toolbar();
        newButton = new ToolbarButton("New Group", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                removeButton.setVisible(false);
                formPanel.setVisible(true);
                nameField.setValue("");
                oldName = null;
            }
        });
        topToolbar.addButton(newButton);
        this.setTopToolbar(topToolbar);

        loadComboData();
    }

    private void loadComboData() {
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
                store.setDataProxy(proxy);
                store.load();
                store.commitChanges();
            }
        };
        service.getGroups(callback);
    }

    private void save(final String groupName) {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();

        if (oldName == null) {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Error executing add group\n" + caught.getMessage());
                    Ext.get("vip-group-mng").unmask();
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        formPanel.setVisible(false);
                        loadComboData();
                    }
                    Ext.get("vip-group-mng").unmask();
                    MessageBox.alert(result);
                }
            };
            service.addGroup(auth.getProxyFileName(), groupName, callback);

        } else {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Error executing update group\n" + caught.getMessage());
                    Ext.get("vip-group-mng").unmask();
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        formPanel.setVisible(false);
                        loadComboData();
                        Layout.getInstance().addClassButton(groupName);
                    }
                    Ext.get("vip-group-mng").unmask();
                    MessageBox.alert(result);
                }
            };
            service.updateGroup(auth.getProxyFileName(), oldName, groupName, callback);
        }
        Ext.get("vip-group-mng").mask("Saving group...");
    }
}
