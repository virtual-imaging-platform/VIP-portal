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
package fr.insalyon.creatis.platform.main.client.view.system.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
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
import fr.insalyon.creatis.platform.main.client.bean.AppClass;
import fr.insalyon.creatis.platform.main.client.rpc.ApplicationService;
import fr.insalyon.creatis.platform.main.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.platform.main.client.rpc.ConfigurationService;
import fr.insalyon.creatis.platform.main.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.platform.main.client.view.common.panel.CheckboxPanel;
import fr.insalyon.creatis.platform.main.client.view.common.FieldUtil;
import fr.insalyon.creatis.platform.main.client.view.common.panel.AbstractPanel;
import fr.insalyon.creatis.platform.main.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ManageClassesCenterPanel extends AbstractPanel {

    private static ManageClassesCenterPanel instance;
    private Store store;
    private ComboBox cb;
    private FormPanel formPanel;
    private FieldSet fieldSet;
    private Toolbar topToolbar;
    private ToolbarButton newButton;
    private Button saveButton;
    private Button removeButton;
    private boolean newClass;
    private TextField nameField;
    private CheckboxPanel groupPanel;

    public static ManageClassesCenterPanel getInstance() {
        if (instance == null) {
            instance = new ManageClassesCenterPanel();
        }
        return instance;
    }

    private ManageClassesCenterPanel() {
        super(new String[]{"Administrator"});
        //, "Class", "classes");
    }

    @Override
    protected void buildPanel() {

        this.setTitle("Class Management");
        this.setLayout(new VerticalLayout(15));
        this.setMargins(0, 0, 0, 0);
        this.setPaddings(10, 5, 5, 5);
        this.setAutoScroll(true);

        // FormPanel
        formPanel = new FormPanel();
        formPanel.setBorder(false);
        fieldSet = new FieldSet("Class");
        fieldSet.setWidth(600);
        formPanel.add(fieldSet);
        formPanel.setVisible(false);

        store = new SimpleStore("classes-name", new Object[]{});
        store.load();

        cb = FieldUtil.getComboBox("combo-classes-name", "Class Name", 250,
                "Select a Class...", store, "classes-name");
        cb.setId("combo-classes-name");
        cb.setTypeAhead(true);
        cb.setValueField("classes-name");
        cb.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {

                String name = comboBox.getValue();

                ApplicationServiceAsync service = ApplicationService.Util.getInstance();
                final AsyncCallback<AppClass> callback = new AsyncCallback<AppClass>() {

                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Error", "Error executing get user\n" + caught.getMessage());
                    }

                    public void onSuccess(AppClass result) {
                        newClass = false;
                        removeButton.setVisible(true);
                        nameField.setValue(result.getName());
                        nameField.setReadOnly(true);
                        formPanel.setVisible(true);
                        cb.setValue("");
                        loadGroupData(result.getGroups());
                    }
                };
                service.getClass(name, callback);
            }
        });
        this.add(cb);

        // FormPanel
        MultiFieldPanel nameMFP = FieldUtil.getMultiFieldPanel("mfp-class-name");

        nameField = FieldUtil.getTextField("class-name", 400, "Name", false);
        nameMFP.addToRow(nameField, 510);
        fieldSet.add(nameMFP);

        groupPanel = new CheckboxPanel("classes-group", "User Groups:", 350);
        fieldSet.add(groupPanel);

        // Form Buttons
        saveButton = new Button("Save", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                String name = nameField.getValueAsString();
                List<String> groups = groupPanel.getSelectedValues();
                groups.add("Administrator");

                if (name.trim().equals("")) {
                    MessageBox.alert("Error", "You should provide a name to the class.");
                    return;
                }
                save(new AppClass(name, groups));
            }
        });
        formPanel.addButton(saveButton);

        removeButton = new Button("Remove", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                final String name = nameField.getValueAsString();

                MessageBox.confirm("Confirm", "Do you really want to remove the class \"" + name + "\"?",
                        new MessageBox.ConfirmCallback() {

                            public void execute(String btnID) {
                                if (btnID.toLowerCase().equals("yes")) {
                                    ApplicationServiceAsync service = ApplicationService.Util.getInstance();

                                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                        public void onFailure(Throwable caught) {
                                            MessageBox.alert("Error", "Error executing remove class\n" + caught.getMessage());
                                        }

                                        public void onSuccess(Void result) {
                                            formPanel.setVisible(false);
                                            loadComboData();
                                            MessageBox.alert("The class was successfully removed!");
                                        }
                                    };
                                    service.removeClass(name, callback);
                                }
                            }
                        });
            }
        });
        formPanel.addButton(removeButton);

        this.add(formPanel);

        // Toolbar
        topToolbar = new Toolbar();
        newButton = new ToolbarButton("New Class", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                removeButton.setVisible(false);
                formPanel.setVisible(true);
                nameField.setValue("");
                nameField.setReadOnly(false);
                newClass = true;
                loadGroupData(new ArrayList<String>());
            }
        });
        topToolbar.addButton(newButton);
        this.setTopToolbar(topToolbar);

        loadComboData();
    }

    private void loadComboData() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<AppClass>> callback = new AsyncCallback<List<AppClass>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get classes list\n" + caught.getMessage());
            }

            public void onSuccess(List<AppClass> result) {
                Object[][] data = new Object[result.size()][1];
                for (int i = 0; i < result.size(); i++) {
                    data[i][0] = result.get(i).getName();
                }

                MemoryProxy proxy = new MemoryProxy(data);
                store.setDataProxy(proxy);
                store.load();
                store.commitChanges();
            }
        };
        service.getClasses(callback);
    }

    private void loadGroupData(final List<String> groups) {
        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get groups list\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                groupPanel.removeCheckBoxes();
                for (String groupName : result) {
                    if (!groupName.equals("Administrator")) {
                        boolean checked = groups.contains(groupName) ? true : false;
                        groupPanel.addCheckbox(groupName, groupName, checked);
                    }
                }
                groupPanel.doLayout();
            }
        };
        service.getGroups(callback);
    }

    private void save(final AppClass appClass) {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();

        if (newClass) {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Error executing add class\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        formPanel.setVisible(false);
                        loadComboData();
                        Layout.getInstance().addClassButton(appClass.getName());
                    }
                    MessageBox.alert(result);
                }
            };
            service.addClass(appClass, callback);

        } else {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Error executing update class\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        formPanel.setVisible(false);
                        loadComboData();
                    }
                    MessageBox.alert(result);
                }
            };
            service.updateClass(appClass, callback);
        }
    }
}
