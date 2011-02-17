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
package fr.insalyon.creatis.platform.main.client.view.application.launch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FitLayout;
import fr.insalyon.creatis.platform.main.client.bean.WorkflowInput;
import fr.insalyon.creatis.platform.main.client.rpc.WorkflowService;
import fr.insalyon.creatis.platform.main.client.rpc.WorkflowServiceAsync;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public class SaveInputWindow extends Window {

    private TextField name;

    public SaveInputWindow(final String appName, final Map<String, String> parametersMap) {

        this.setTitle("Save Inputs");
        this.setWidth(350);
        this.setHeight(110);
        this.setClosable(true);
        this.setLayout(new FitLayout());

        FormPanel formPanel = new FormPanel();
        formPanel.setWidth(340);
        formPanel.setLabelWidth(100);
        formPanel.setPaddings(10, 5, 5, 5);

        name = new TextField("Experiment Name", "name", 200);
        name.setAllowBlank(false);
        formPanel.add(name);

        Button save = new Button("Save", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                if (name.getText().equals("")) {
                    MessageBox.alert("Error", "An experiment name is required.");
                    return;
                }

                StringBuffer sb = new StringBuffer();
                for (String k : parametersMap.keySet()) {
                    sb.append(k + "=" + parametersMap.get(k) + "--");
                }

                WorkflowInput wi = new WorkflowInput(appName, name.getText(), sb.toString());

                WorkflowServiceAsync service = WorkflowService.Util.getInstance();
                final AsyncCallback<String> callback = new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Error", "Error while saving simulation inputs: " + caught.getMessage());
                    }

                    public void onSuccess(String result) {
                        if (!result.contains("Error:")) {
                            close();
                        }
                        MessageBox.alert(result);
                    }
                };
                service.addWorkflowInput(wi, callback);
            }
        });
        formPanel.addButton(save);

        Button cancel = new Button("Cancel", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                close();
            }
        });
        formPanel.addButton(cancel);

        this.add(formPanel);
        this.show();
    }
}
