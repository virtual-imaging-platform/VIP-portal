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
package fr.insalyon.creatis.vip.portal.client.view.application.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.VerticalLayout;
import fr.insalyon.creatis.vip.portal.client.bean.Authentication;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.common.FieldUtil;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import fr.insalyon.creatis.vip.portal.client.view.common.window.lfn.SelectLFNBrowserWindow;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.MonitorLeftPanel;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.WorkflowPanel;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.WorkflowsPanel;
import fr.insalyon.creatis.vip.portal.client.view.common.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class LaunchPanel extends Panel {

    private String rootPath = "/grid/biomed";
    private String workflowName;
    private String applicationClass;
    private FieldSet fieldSet;
    private String[] parameterType = {"List", "Range", "Tag"};
    private int countID = 0;
    private int countListID = 0;
    private int panelId;
    private LaunchPanel instance;
    private Button launchButton;

    public LaunchPanel(final String applicationClass, final String workflowName, final int panelId) {

        this.applicationClass = applicationClass;
        this.panelId = panelId;
        this.workflowName = workflowName;
        this.instance = this;
        this.setTitle("Launch " + workflowName);
        this.setMargins(0, 0, 0, 0);
        this.setPaddings(5, 5, 5, 5);
        this.setLayout(new VerticalLayout(15));
        this.setAutoScroll(true);
        this.setId("launch-panel-" + panelId);

        FormPanel formPanel = new FormPanel();
        formPanel.setBorder(false);
        formPanel.setId("launch-formpanel-" + panelId);

        fieldSet = new FieldSet("Input Data");
        fieldSet.setId("launch-fieldset-" + panelId);
        fieldSet.setWidth(665);

        formPanel.add(fieldSet);

        launchButton = new Button("Launch", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                launchWorkflow();
            }
        });
        formPanel.addButton(launchButton);

        this.add(formPanel);

        // Load inputs button
        Toolbar toolbar = new Toolbar();
        ToolbarButton loadButton = new ToolbarButton("Load Inputs Data", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                new LoadInputWindow(workflowName, instance);
            }
        });
        toolbar.addButton(loadButton);
        toolbar.addSeparator();

        // Save inputs button
        ToolbarButton saveButton = new ToolbarButton("Save Inputs Data", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                new SaveInputWindow(workflowName, getParamatersMap());
            }
        });
        toolbar.addButton(saveButton);

        this.setTopToolbar(toolbar);

        // Load simulation sources list
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get application sources list: " + caught.getMessage());
            }

            public void onSuccess(List<String> result) {

                if (result != null) {
                    for (String source : result) {
                        String label = source.replace(" ", "-");
                        String id = label + "-" + panelId + "-" + countID;
                        MultiFieldPanel mfp = FieldUtil.getMultiFieldPanel("mfp-" + id);
                        mfp.addToRow(getTypeComboBox(id, label), 190);
                        addTextRowType("tf-" + id, mfp, "", true);
                        fieldSet.add(mfp);
                        launchButton.setDisabled(false);
                    }
                    countID++;
                    fieldSet.doLayout();
                } else {
                    launchButton.setDisabled(true);
                    MessageBox.alert("Error", "Unable to download application source file.");
                }
                Ext.get("launch-panel-" + panelId).unmask();
            }
        };
        Authentication auth = Context.getInstance().getAuthentication();
        service.getWorkflowSources(auth.getProxyFileName(), workflowName, callback);
    }

    private ComboBox getTypeComboBox(String comboID, String label) {

        Store store = new SimpleStore("launch-parameter-type", (Object[]) parameterType);
        store.load();

        ComboBox cb = new ComboBox();
        cb.setId("combo-" + comboID);
        cb.setFieldLabel(label);
        cb.setStore(store);
        cb.setTypeAhead(true);
        cb.setMode(ComboBox.LOCAL);
        cb.setTriggerAction(ComboBox.ALL);
        cb.setDisplayField("launch-parameter-type");
        cb.setValueField("launch-parameter-type");
        cb.setWidth(80);
        cb.setReadOnly(true);
        cb.setForceSelection(true);
        cb.setValue(parameterType[0]);
        cb.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {
                String selectedOption = comboBox.getValue();
                String id = comboBox.getId().substring(comboBox.getId().indexOf("-") + 1);

                fieldSet.remove("mfp-" + id);
                fieldSet.remove("panel-t-" + id);
                fieldSet.remove("panel-l-" + id);
                id = id.substring(0, id.lastIndexOf("-"));
                id += "-" + countID++;

                MultiFieldPanel mfp = FieldUtil.getMultiFieldPanel("mfp-" + id);
                ComboBox cb = getTypeComboBox(id, comboBox.getFieldLabel());
                cb.setValue(selectedOption);
                mfp.addToRow(cb, 190);

                if (selectedOption.equals(parameterType[0])) {
                    addTextRowType("tf-" + id, mfp, "", true);

                } else if (selectedOption.equals(parameterType[1])) {
                    mfp.addToRow(FieldUtil.getNumberField("nf-start-" + id, 70, "Start", "Start", true), 80);
                    mfp.addToRow(FieldUtil.getNumberField("nf-stop-" + id, 70, "Stop", "Stop", true), 80);
                    mfp.addToRow(FieldUtil.getNumberField("nf-step-" + id, 70, "Step", "Step", true), 80);

                } else {
                    TextField textField = FieldUtil.getTextField("tf-" + id, 350, "", true);
                    textField.addListener(new TextFieldListenerAdapter() {

                        @Override
                        public void onChange(Field field, Object newVal, Object oldVal) {

                            String tag = field.getValueAsString();
                            String id = field.getId().substring(field.getId().indexOf("-") + 1);

                            Panel panel = (Panel) fieldSet.findByID("panel-t-" + id);
                            List<String> currentTags = new ArrayList<String>();

                            if (panel == null) {
                                panel = new Panel();
                                panel.setId("panel-t-" + id);
                                panel.setWidth(600);
                                panel.setBorder(false);
                                panel.setMargins(0, 0, 0, 0);

                            } else {
                                for (Component c : panel.getComponents()) {
                                    currentTags.add(c.getId());
                                }
                            }

                            List<String> tagElements = new ArrayList<String>();

                            char[] tagArray = tag.toCharArray();
                            for (int i = 0; i < tag.length(); i++) {
                                if (tagArray[i] == '$') {
                                    if (i + 2 < tag.length()) {
                                        if (tagArray[i + 1] == 'L' || tagArray[i + 1] == 'R') {
                                            tagElements.add("" + tagArray[i + 1] + tagArray[i + 2]);
                                            i += 2;
                                        }
                                    }
                                }
                            }

                            for (String t : tagElements) {
                                if (!currentTags.contains("mfp-t-" + id + "-" + t)) {
                                    MultiFieldPanel mfp = FieldUtil.getMultiFieldPanel("mfp-t-" + id + "-" + t);

                                    if (t.startsWith("L")) {
                                        addTextRowType("it-t-" + id + "-" + t, mfp, "$" + t, false);

                                    } else {
                                        mfp.addToRow(FieldUtil.getNumberField("it-t-nf-start-" + id + "-" + t, 70, "$" + t, "Start", false), 180);
                                        mfp.addToRow(FieldUtil.getNumberField("it-t-nf-stop-" + id + "-" + t, 70, "", "Stop", true), 75);
                                        mfp.addToRow(FieldUtil.getNumberField("it-t-nf-step-" + id + "-" + t, 70, "", "Step", true), 80);
                                    }
                                    panel.add(mfp);
                                }
                            }

                            for (String t : currentTags) {
                                if (!tagElements.contains(t.substring(t.lastIndexOf("-") + 1))) {
                                    panel.remove(t);
                                }
                            }

                            fieldSet.add(panel);
                            fieldSet.doLayout();
                        }
                    });
                    mfp.addToRow(textField, new ColumnLayoutData(1));
                }
                fieldSet.add(mfp);
                fieldSet.doLayout();
            }
        });
        return cb;
    }

    private TextField addTextRowType(final String id, MultiFieldPanel mfp, String label, boolean hideLabel) {

        if (hideLabel) {
            Button addButton = new Button("+");
            addButton.setId("btadd-" + id);
            addButton.addListener(new ButtonListenerAdapter() {

                @Override
                public void onClick(Button button, EventObject e) {
                    String id = button.getId().substring(button.getId().indexOf("tf-") + 3);
                    addOneMoreTextRow(id, "");
                }
            });
            mfp.addToRow(addButton, 35);
        }

        int size = !hideLabel ? 460 : 355;
        TextField textField = FieldUtil.getTextField(id, 350, label, hideLabel);
        mfp.addToRow(textField, size);

        Button browseButton = new Button("Browse");
        browseButton.setId("btbws-" + id);
        browseButton.addListener(new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                new SelectLFNBrowserWindow(rootPath, id, fieldSet);
            }
        });
        mfp.addToRow(browseButton, 60);
        return textField;
    }

    private void addOneMoreTextRow(String id, String value) {

        Panel panel = (Panel) fieldSet.findByID("panel-l-" + id);

        if (panel == null) {
            MultiFieldPanel mfp = (MultiFieldPanel) fieldSet.getComponent("mfp-" + id);
            panel = (Panel) mfp.getComponent(0);
            ComboBox comboBox = (ComboBox) panel.getComponent("combo-" + id);
            panel = (Panel) mfp.getComponent(2);
            TextField tf = (TextField) panel.getComponent("tf-" + id);
            String valueTF = tf.getValueAsString();
            fieldSet.remove("mfp-" + id);

            id = id.substring(0, id.lastIndexOf("-")) + "-" + countID++;
            mfp = FieldUtil.getMultiFieldPanel("mfp-" + id);
            ComboBox cb = getTypeComboBox(id, comboBox.getFieldLabel());
            cb.setValue("List");
            mfp.addToRow(cb, 190);
            tf = addTextRowType("tf-" + id, mfp, "", true);
            tf.setValue(valueTF);
            fieldSet.add(mfp);

            panel = createListPanel(id);
            fieldSet.add(panel);
        }

        panel.add(createListMultiFieldPanel(id, value));
        fieldSet.doLayout();
    }

    private Panel createListPanel(String id) {
        Panel panel = new Panel();
        panel.setId("panel-l-" + id);
        panel.setWidth(640);
        panel.setBorder(false);
        panel.setMargins(0, 190, 0, 0);

        return panel;
    }

    private MultiFieldPanel createListMultiFieldPanel(String id, String value) {

        id += "-" + (countListID++);
        MultiFieldPanel mfp = FieldUtil.getMultiFieldPanel("mfp-l-" + id);
        Button removeButton = new Button("-");
        removeButton.setId("btrem-" + id);
        removeButton.setWidth("30");
        removeButton.addListener(new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {

                String cid = button.getId().substring(button.getId().indexOf("-") + 1);
                String pid = cid.substring(0, cid.lastIndexOf("-"));
                Panel panel = (Panel) fieldSet.findByID("panel-l-" + pid);
                String x = "";
                for (Component c : panel.getComponents()) {
                    x += c.getId() + " ";
                }

                panel.remove("mfp-l-" + cid);

                if (panel.getComponents().length == 0) {
                    fieldSet.remove("panel-l-" + pid);
                }
                fieldSet.doLayout();
            }
        });
        mfp.addToRow(removeButton, 35);
        TextField newTF = FieldUtil.getTextField("tf-" + id, 350, "", true);
        newTF.setValue(value);
        mfp.addToRow(newTF, 355);

        Button browseButton = new Button("Browse");
        browseButton.setId("btbws-" + id);
        browseButton.addListener(new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                new SelectLFNBrowserWindow(rootPath, button.getId().substring(button.getId().indexOf("-") + 1), fieldSet);
            }
        });
        mfp.addToRow(browseButton, 60);

        return mfp;
    }

    private void launchWorkflow() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error while launching simulation: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                String workflowID = result.substring(result.lastIndexOf("/") + 1, result.lastIndexOf("."));
                Ext.get("launch-panel-" + panelId).unmask();
                Layout layout = Layout.getInstance();
                if (layout.hasCenterPanelTab("app-workflows-panel")) {
                    layout.setActiveCenterPanel("app-workflows-panel");
                } else {
                    WorkflowsPanel panel = WorkflowsPanel.getInstance();
                    layout.setCenterPanel(panel);
                    panel.setApplicationClass(applicationClass);
                    panel.loadWorkflowData();
                }
                MonitorLeftPanel monitorLeftPanel = MonitorLeftPanel.getInstance();
                monitorLeftPanel.setApplicationClass(applicationClass);
                layout.setLeftPanel(monitorLeftPanel);
                layout.addCenterPanel(new WorkflowPanel(workflowID, "Running"));
            }
        };
        Ext.get("launch-panel-" + panelId).mask("Launching simulation...");

        Authentication auth = Context.getInstance().getAuthentication();
        service.launchWorkflow(getParamatersMap(), workflowName, auth.getProxyFileName(), callback);
    }

    /**
     * 
     * @return Map of input parameters
     */
    private Map<String, String> getParamatersMap() {

        Map<String, String> parametersMap = new HashMap<String, String>();

        for (Component component : fieldSet.getComponents()) {
            if (component.getId().startsWith("mfp-")) {
                MultiFieldPanel mfp = (MultiFieldPanel) component;
                for (Component mfpC : mfp.getComponents()) {
                    Panel panel = (Panel) mfpC;
                    for (Component c : panel.getComponents()) {
                        if (c.getId().startsWith("tf-")) {
                            String generalID = c.getId().replace("tf-", "");
                            TextField tf = (TextField) c;
                            ComboBox cb = (ComboBox) fieldSet.findByID("combo-" + generalID);
                            String fieldValue = tf.getValueAsString();

                            Panel listPanel = (Panel) fieldSet.findByID("panel-l-" + generalID);
                            if (listPanel != null) {
                                for (Component cp : listPanel.getComponents()) {
                                    MultiFieldPanel mfpp = (MultiFieldPanel) cp;
                                    for (Component mfppc : mfpp.getComponents()) {
                                        Panel mfppp = (Panel) mfppc;
                                        for (Component cl : mfppp.getComponents()) {
                                            if (cl.getId().contains("tf-")) {
                                                TextField tl = (TextField) cl;
                                                fieldValue += "@@" + tl.getValueAsString();
                                            }
                                        }
                                    }
                                }
                            }
                            parametersMap.put(cb.getFieldLabel(), fieldValue);

                        } else if (c.getId().startsWith("nf-start-")) {
                            String generalID = c.getId().replace("nf-start-", "");
                            NumberField nfStart = (NumberField) c;
                            NumberField nfStop = (NumberField) fieldSet.findByID("nf-stop-" + generalID);
                            NumberField nfStep = (NumberField) fieldSet.findByID("nf-step-" + generalID);
                            String value = nfStart.getValueAsString() + "##" + nfStop.getValueAsString() + "##" + nfStep.getValueAsString();
                            ComboBox cb = (ComboBox) fieldSet.findByID("combo-" + generalID);
                            parametersMap.put(cb.getFieldLabel(), value);
                        }
                    }
                }
            }
            // TODO handle TAGs
        }
        return parametersMap;
    }

    public void loadInput(String result) {

        for (Component c : fieldSet.getComponents()) {
            fieldSet.remove(c.getId());
        }

        String[] inputs = result.split("--");

        for (String in : inputs) {
            String[] pair = in.split("=");
            String label = pair[0];
            String id = label.replace(" ", "-") + "-" + panelId + "-" + countID;
            Panel panel = null;

            MultiFieldPanel mfp = FieldUtil.getMultiFieldPanel("mfp-" + id);
            ComboBox cb = getTypeComboBox(id, label);

            if (pair.length == 1) {
                cb.setValue("List");
                mfp.addToRow(cb, 190);
                addTextRowType("tf-" + id, mfp, "", true);

            } else {
                if (pair[1].contains("##")) {
                    cb.setValue("Range");
                    mfp.addToRow(cb, 190);

                    NumberField startField = FieldUtil.getNumberField("nf-start-" + id, 70, "Start", "Start", true);
                    NumberField stopField = FieldUtil.getNumberField("nf-stop-" + id, 70, "Stop", "Stop", true);
                    NumberField stepField = FieldUtil.getNumberField("nf-step-" + id, 70, "Step", "Step", true);

                    String[] v = pair[1].split("##");
                    if (v.length == 3) {
                        stepField.setValue(v[2]);
                    }
                    if (v.length == 2) {
                        stopField.setValue(v[1]);
                    }
                    startField.setValue(v[0]);

                    mfp.addToRow(startField, 80);
                    mfp.addToRow(stopField, 80);
                    mfp.addToRow(stepField, 80);

                } else {
                    cb.setValue("List");
                    mfp.addToRow(cb, 190);
                    TextField textField = addTextRowType("tf-" + id, mfp, "", true);

                    if (pair[1].contains("@@")) {
                        String[] v = pair[1].split("@@");
                        panel = createListPanel(id);

                        for (int i = 0; i < v.length; i++) {
                            if (i == 0) {
                                textField.setValue(v[i]);
                            } else {
                                panel.add(createListMultiFieldPanel(id, v[i]));
                            }
                        }
                    } else {
                        textField.setValue(pair[1]);
                    }
                }
            }
            fieldSet.add(mfp);
            if (panel != null) {
                fieldSet.add(panel);
            }
        }
        countID++;
        fieldSet.doLayout();
        Ext.get("launch-panel-" + panelId).unmask();
    }
}
