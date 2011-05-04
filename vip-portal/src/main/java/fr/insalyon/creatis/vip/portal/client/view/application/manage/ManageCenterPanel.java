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
package fr.insalyon.creatis.vip.portal.client.view.application.manage;

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
import fr.insalyon.creatis.vip.portal.client.bean.Application;
import fr.insalyon.creatis.vip.portal.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.portal.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import fr.insalyon.creatis.vip.portal.client.view.common.panel.AbstractPanel;
import fr.insalyon.creatis.vip.portal.client.view.common.window.lfn.SelectLFNBrowserWindow;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ManageCenterPanel extends AbstractPanel {

    private static ManageCenterPanel instance;
    private FormPanel formPanel;
    private FieldSet fieldSet;
    private Store store;
    private ComboBox cb;
    private Toolbar topToolbar;
    private ToolbarButton newButton;
    private Button saveButton;
    private Button removeButton;
    private TextField nameField;
    private TextField lfnField;
    private boolean newApplication;
    private String applicationClass;
    private List<String> classes;

    public static ManageCenterPanel getInstance() {
        if (instance == null) {
            instance = new ManageCenterPanel();
        }
        return instance;
    }

    private ManageCenterPanel() {
        super(null);
        classes = new ArrayList<String>();
    }

    public void setApplicationClass(String applicationClass) {
        this.applicationClass = applicationClass;
        this.title = applicationClass;
    }

    @Override
    protected void buildPanel() {
        this.setTitle("Application Management");
        this.setLayout(new VerticalLayout(15));
        this.setMargins(0, 0, 0, 0);
        this.setPaddings(10, 5, 5, 5);
        this.setAutoScroll(true);

        // FormPanel
        formPanel = new FormPanel();
        formPanel.setBorder(false);
        fieldSet = new FieldSet("Application");
        fieldSet.setWidth(600);
        formPanel.add(fieldSet);
        formPanel.setVisible(false);

        store = new SimpleStore("man-app-name", new Object[]{});
        store.load();

        cb = FieldUtil.getComboBox("combo-man-app-name", "Application Name", 250,
                "Select an Application...", store, "man-app-name");
        cb.setId("combo-man-app-name");
        cb.setTypeAhead(true);
        cb.setValueField("man-app-name");
        cb.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {

                String name = comboBox.getValue();

                ApplicationServiceAsync service = ApplicationService.Util.getInstance();
                final AsyncCallback<Application> callback = new AsyncCallback<Application>() {

                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Error", "Error executing get application\n" + caught.getMessage());
                    }

                    public void onSuccess(Application result) {
                        newApplication = false;
                        removeButton.setVisible(true);
                        nameField.setReadOnly(true);
                        nameField.setValue(result.getName());
                        lfnField.setValue(result.getLfn());
                        cb.setValue("");
                        formPanel.setVisible(true);
                        loadClassData(result.getApplicationClasses());
                    }
                };
                service.getApplication(name, callback);
            }
        });

        this.add(cb);

        // FormPanel
        MultiFieldPanel nameMFP = FieldUtil.getMultiFieldPanel("mfp-simulation-name");

        nameField = FieldUtil.getTextField("man-app-name", 400, "Name", false);
        nameMFP.addToRow(nameField, 510);
        fieldSet.add(nameMFP);

        MultiFieldPanel lfnMFP = FieldUtil.getMultiFieldPanel("mfp-simulation-lfn");

        lfnField = FieldUtil.getTextField("man-app-lfn", 400, "Application LFN", false);
        lfnMFP.addToRow(lfnField, 510);
        Button browseButton = new Button("Browse");
        browseButton.setId("btbws-simulation-lfn");
        browseButton.addListener(new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                new SelectLFNBrowserWindow("/grid/biomed", "man-app-lfn", fieldSet);
            }
        });
        lfnMFP.addToRow(browseButton, 60);
        fieldSet.add(lfnMFP);

        // Form Buttons
        saveButton = new Button("Save", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                String name = nameField.getValueAsString();
                String lfn = lfnField.getValueAsString();

                if (name.trim().equals("") || lfn.trim().equals("")) {
                    MessageBox.alert("Error", "You should provide the name and the LFN.");
                    return;
                }

                save(new Application(name, lfn, classes));
            }
        });
        formPanel.addButton(saveButton);

        removeButton = new Button("Remove", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                final String name = nameField.getValueAsString();
                final String lfn = lfnField.getValueAsString();

                MessageBox.confirm("Confirm", "Do you really want to remove the application \"" + name + "\"?",
                        new MessageBox.ConfirmCallback() {

                            public void execute(String btnID) {
                                if (btnID.toLowerCase().equals("yes")) {
                                    classes.remove(applicationClass);
                                    save(new Application(name, lfn, classes));
                                }
                            }
                        });
            }
        });
        formPanel.addButton(removeButton);

        this.add(formPanel);

        // Toolbar
        topToolbar = new Toolbar();
        newButton = new ToolbarButton("New Application", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                removeButton.setVisible(false);
                formPanel.setVisible(true);
                nameField.setValue("");
                nameField.setReadOnly(false);
                lfnField.setValue("");
                newApplication = true;
                loadClassData(new ArrayList<String>());
            }
        });
        topToolbar.addButton(newButton);
        this.setTopToolbar(topToolbar);
    }

    public void loadData() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get applications list\n" + caught.getMessage());
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
        service.getApplicationsName(applicationClass, callback);
    }

    private void loadClassData(List<String> appClasses) {
        classes = new ArrayList<String>(appClasses);
        if (newApplication) {
            classes.add(applicationClass);
        }
    }

    private void save(Application workflowDescriptor) {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();

        if (newApplication) {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Error executing update application\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        formPanel.setVisible(false);
                        loadData();
                    }
                    MessageBox.alert(result);
                }
            };
            service.add(workflowDescriptor, callback);

        } else {
            final AsyncCallback<String> callback = new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Error executing update application\n" + caught.getMessage());
                }

                public void onSuccess(String result) {
                    if (!result.contains("Error: ")) {
                        formPanel.setVisible(false);
                        loadData();
                    }
                    MessageBox.alert(result);
                }
            };
            service.update(workflowDescriptor, callback);
        }
    }
}
