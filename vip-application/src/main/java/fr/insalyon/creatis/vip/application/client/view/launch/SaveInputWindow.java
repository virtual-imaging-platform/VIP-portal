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
package fr.insalyon.creatis.vip.application.client.view.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class SaveInputWindow extends Window {

    private DynamicForm form;
    private TextItem nameItem;

    public SaveInputWindow(final String applicationClass, final String appName,
            final Map<String, String> parametersMap) {

        this.setTitle("Save Inputs for " + appName);
        this.setWidth(350);
        this.setHeight(110);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        form = new DynamicForm();
        form.setHeight100();
        form.setWidth100();
        form.setPadding(5);
        form.setLayoutAlign(VerticalAlignment.BOTTOM);

        nameItem = new TextItem("name", "Name");
        nameItem.setRequired(true);
        nameItem.setWidth(200);

        ButtonItem saveButton = new ButtonItem("saveButton", "Save");
        saveButton.setWidth(60);
        saveButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                if (form.validate()) {
                    StringBuilder sb = new StringBuilder();
                    for (String k : parametersMap.keySet()) {
                        sb.append(k);
                        sb.append("=");
                        String value = parametersMap.get(k);
                        if (value == null) {
                            value = "";
                        } else if (value.contains("null")) {
                            value = value.replaceAll("null", "");
                        }
                        sb.append(value);
                        sb.append("--");
                    }

                    SimulationInput wi = new SimulationInput(appName,
                            nameItem.getValueAsString(), sb.toString());

                    WorkflowServiceAsync service = WorkflowService.Util.getInstance();
                    final AsyncCallback<String> callback = new AsyncCallback<String>() {

                        public void onFailure(Throwable caught) {
                            SC.warn("Error while saving simulation inputs: " + caught.getMessage());
                        }

                        public void onSuccess(String result) {
                            if (!result.contains("Error:")) {
                                LaunchTab launchTab = (LaunchTab) Layout.getInstance().
                                        getTab("launch-" + applicationClass.toLowerCase() + "-tab");
                                launchTab.loadInputsList();
                                destroy();
                            }
                            SC.warn(result);
                        }
                    };
                    service.addWorkflowInput(Context.getInstance().getUserDN(), wi, callback);
                }
            }
        });
        form.setFields(nameItem, saveButton);
        this.addItem(form);
    }
}
