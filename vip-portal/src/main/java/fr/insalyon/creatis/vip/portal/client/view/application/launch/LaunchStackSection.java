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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.view.selection.PathSelectionWindow;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class LaunchStackSection extends SectionStackSection {

    private ModalWindow modal;
    private String applicationClass;
    private String simulationName;
    private DynamicForm form;

    public LaunchStackSection(String applicationClass) {

        this.applicationClass = applicationClass;
        this.setShowHeader(false);

        VLayout vLayout = new VLayout();
        vLayout.setHeight100();
        vLayout.setMargin(10);
        vLayout.setOverflow(Overflow.AUTO);

        form = new DynamicForm();
        form.setWidth(650);
        form.setHeight100();
        vLayout.addMember(form);

        modal = new ModalWindow(vLayout);
        this.addItem(vLayout);
    }

    /**
     * Loads the descriptor file of an application
     * 
     * @param simulationName Name of the simulation
     */
    public void load(String simulationName) {
        this.simulationName = simulationName;
        loadData();
    }

    /**
     * Loads input values from string.
     * 
     * @param values Input values
     */
    public void loadInput(String values) {

        Map<String, String> valuesMap = new HashMap<String, String>();

        for (String input : values.split("<br />")) {
            String[] s = input.split(" = ");
            String v = s[1] != null ? s[1] : "";
            valuesMap.put(s[0].replace(" ", "-"), v);
        }

        for (Canvas c : form.getChildren()) {
            DynamicForm f = (DynamicForm) c;
            String name = f.getID().substring(0, f.getID().indexOf("-form-l"));
            String value = valuesMap.get(name);

            if (value != null) {
                if (value.contains("Start: ")) { // Range
                    f.getField(name + "-sel-l").setValue("Range");
                    String[] v = value.split("[:-]");
                    f.getField(name + "-start-l").setValue(v[1].trim());
                    f.getField(name + "-stop-l").setValue(v[3].trim());
                    f.getField(name + "-step-l").setValue(v[5].trim());

                } else { // List
                    f.getField(name + "-sel-l").setValue("List");
                    f.getField(name + "-list-l").setValue(value);
                }
                f.redraw();
            } else if (!name.equals("button")) {
                SC.warn("Could not find value for parameter \"" + name + "\"");
                return;
            }
        }
    }

    /**
     * Loads simulation sources list.
     */
    private void loadData() {

        for (Canvas c : form.getChildren()) {
            form.removeChild(c);
        }

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error executing get application sources list: " + caught.getMessage());
            }

            public void onSuccess(List<String> result) {

                if (result != null) {
                    int count = 0;

                    for (String source : result) {

                        String name = source.replace(" ", "-");

                        DynamicForm iForm = new DynamicForm();
                        iForm.setID(name + "-form-l");
                        iForm.setPadding(5);
                        iForm.setTop(count++ * 30);
                        iForm.setWidth(650);
                        iForm.setNumCols(10);
                        iForm.setFixedColWidths(true);
                        iForm.setColWidths(120, 75, "*", "*", "*", "*", "*", "*", "*", "*");

                        SelectItem selectItem = getTypeSelectItem(name, source);
                        final TextItem listItem = getTextItem(name, "-list-l", 350, false, "", "List", true, null);
                        ButtonItem browseItem = getButtonItem(name, "-brow-l", 60, " Browse ", "List");
                        browseItem.addClickHandler(new ClickHandler() {

                            public void onClick(ClickEvent event) {
                                new PathSelectionWindow(listItem).show();
                            }
                        });
                        TextItem startItem = getTextItem(name, "-start-l", 70, true, "Start", "Range", false, "[0-9.]");
                        TextItem stopItem = getTextItem(name, "-stop-l", 70, true, "Stop", "Range", false, "[0-9.]");
                        TextItem stepItem = getTextItem(name, "-step-l", 70, true, "Step", "Range", false, "[0-9.]");

                        iForm.setFields(selectItem, listItem, browseItem,
                                startItem, stopItem, stepItem);

                        form.addChild(iForm);
                    }
                    DynamicForm iForm = new DynamicForm();
                    iForm.setID("button-form-l");
                    iForm.setPadding(5);
                    iForm.setTop(count++ * 30);
                    iForm.setWidth(650);

                    ButtonItem launchButton = new ButtonItem("launch-l", " Launch ");
                    launchButton.setWidth(80);
                    launchButton.setAlign(Alignment.CENTER);
                    launchButton.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {
                            launch();
                        }
                    });
                    iForm.setFields(launchButton);
                    form.addChild(iForm);
                    
                    LaunchTab launchTab = (LaunchTab) Layout.getInstance().
                            getTab("launch-" + applicationClass.toLowerCase() + "-tab");
                    launchTab.enableSaveButton();
                    modal.hide();

                } else {
                    modal.hide();
                    SC.warn("Unable to download application source file.");
                }
            }
        };
        modal.show("Loading Launch Panel...", true);
        Context context = Context.getInstance();
        service.getWorkflowSources(context.getUser(),
                context.getProxyFileName(), simulationName, callback);
    }

    /**
     * 
     * @param name
     * @param label
     * @return 
     */
    private SelectItem getTypeSelectItem(String name, String label) {

        SelectItem selectItem = new SelectItem(name + "-sel-l", label);
        LinkedHashMap<String, String> selectMap = new LinkedHashMap<String, String>();
        selectMap.put("List", "List"); //TODO For multiple lists separe by '@@'
        selectMap.put("Range", "Range");
//        selectMap.put("Tag", "Tag"); //TODO Handle tags
        selectItem.setValueMap(selectMap);
        selectItem.setValue("List");
        selectItem.setWidth(75);
        selectItem.setRedrawOnChange(true);

        return selectItem;
    }

    /**
     * 
     * @param name
     * @param extName
     * @param size
     * @param showTitle
     * @param title
     * @param optionName
     * @param visible
     * @param keyPressFilter
     * @return 
     */
    private TextItem getTextItem(String name, final String extName, int size,
            boolean showTitle, String title, final String optionName,
            boolean visible, String keyPressFilter) {

        TextItem textItem = new TextItem(name + extName, title);
        textItem.setShowTitle(showTitle);
        textItem.setWidth(size);
        textItem.setStartRow(false);
        textItem.setVisible(visible);
        textItem.setKeyPressFilter(keyPressFilter);
        textItem.setAlign(Alignment.LEFT);

        textItem.setShowIfCondition(new FormItemIfFunction() {

            public boolean execute(FormItem item, Object value, DynamicForm form) {
                String name = item.getName().substring(0, item.getName().indexOf(extName));
                return form.getValue(name + "-sel-l").equals(optionName);
            }
        });

        return textItem;
    }

    /**
     * 
     * @param name
     * @param extName
     * @param size 
     * @param title
     * @param optionName
     * @return 
     */
    private ButtonItem getButtonItem(String name, final String extName, int size,
            String title, final String optionName) {

        ButtonItem buttonItem = new ButtonItem(name + extName, title);
        buttonItem.setWidth(size);
        buttonItem.setStartRow(false);
        buttonItem.setShowIfCondition(new FormItemIfFunction() {

            public boolean execute(FormItem item, Object value, DynamicForm form) {
                String name = item.getName().substring(0, item.getName().indexOf(extName));
                return form.getValue(name + "-sel-l").equals(optionName);
            }
        });
        buttonItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                //TODO Browse window
            }
        });

        return buttonItem;
    }

    /**
     * Launches a simulation
     */
    private void launch() {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Error while launching simulation: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                String simulationID = result.substring(result.lastIndexOf("/") + 1, result.lastIndexOf("."));
                modal.hide();
                SC.say("Simulation successfully launched with ID: " + simulationID);
            }
        };
        modal.show("Launching simulation...", true);
        Context context = Context.getInstance();
        service.launchWorkflow(context.getUser(), getParametersMap(),
                simulationName, context.getProxyFileName(), callback);
    }

    /**
     * Gets a map of parameters.
     * 
     * @return Map of parameters
     */
    public Map<String, String> getParametersMap() {

        Map<String, String> paramsMap = new HashMap<String, String>();

        for (Canvas c : form.getChildren()) {
            DynamicForm f = (DynamicForm) c;
            String name = f.getID().substring(0, f.getID().indexOf("-form-l"));

            if (!name.equals("button")) {
                String title = f.getField(name + "-sel-l").getTitle();

                if (((SelectItem) f.getField(name + "-sel-l")).getValueAsString().equals("List")) {
                    paramsMap.put(title,
                            ((TextItem) f.getField(name + "-list-l")).getValueAsString());
                } else {
                    paramsMap.put(title,
                            ((TextItem) f.getField(name + "-start-l")).getValueAsString() + "##"
                            + ((TextItem) f.getField(name + "-stop-l")).getValueAsString() + "##"
                            + ((TextItem) f.getField(name + "-step-l")).getValueAsString());
                }
            }
        }
        return paramsMap;
    }
}
