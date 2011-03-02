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
import com.gwtext.client.core.Position;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import fr.insalyon.creatis.vip.portal.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.portal.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import fr.insalyon.creatis.vip.portal.client.view.layout.AbstractLeftPanel;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;
import java.util.List;

/**
 *
 * @author Rafael Silva, Tristan Glatard
 */
public class LaunchLeftPanel extends AbstractLeftPanel {

    private static LaunchLeftPanel instance;
    private ComboBox simulationCB;
    private Store simulationsStore;
    private int panelId = 0;
    //the class of applications that are listed in the combobox
    private String applicationClass;

    public static LaunchLeftPanel getInstance() {
        if (instance == null) {
            instance = new LaunchLeftPanel();
        }
        return instance;
    }

    private LaunchLeftPanel() {
        super(null);
        collapsed = false;
    }

    public void setApplicationClass(String applicationClass) {
        this.applicationClass = applicationClass;
        this.title = "Launch " + applicationClass;
    }

    @Override
    public Panel getPanel() {

        FormPanel simulationFilterPanel = new FormPanel();
        simulationFilterPanel.setTitle(title + " Creation");
        simulationFilterPanel.setBorder(false);
        simulationFilterPanel.setLayout(new FormLayout());
        simulationFilterPanel.setLabelAlign(Position.TOP);
        simulationFilterPanel.setMargins(0, 0, 0, 0);
        simulationFilterPanel.setPaddings(10, 5, 5, 5);

        simulationsStore = FieldUtil.getComboBoxStore("simulation-lauch-name");
        simulationsStore.load();
        simulationCB = FieldUtil.getComboBox("simulation-filter-cb", title, 180, 
                "Select " + title, simulationsStore, "simulation-lauch-name");

        simulationFilterPanel.add(simulationCB, new AnchorLayoutData("95%"));

        Button create = new Button("Create " + applicationClass, new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                String simulation = simulationCB.getValue();
                if (simulation == null || simulation.equals("")) {
                    MessageBox.alert("Error", "You should select one type of " + applicationClass + ".");
                    return;
                }
                Layout.getInstance().setCenterPanel(new LaunchPanel(applicationClass, simulation, panelId));
                Ext.get("launch-panel-" + (panelId++)).mask("Loading Launch Panel...");
            }
        });
        simulationFilterPanel.addButton(create);
        loadComboData();

        return simulationFilterPanel;
    }

    private void loadComboData() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get workflow descriptors lists\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {
                Object[][] data = new Object[result.size()][1];
                for (int i = 0; i < result.size(); i++) {
                    data[i][0] = result.get(i);
                }

                MemoryProxy usersProxy = new MemoryProxy(data);
                simulationsStore.setDataProxy(usersProxy);
                simulationsStore.load();
                simulationsStore.commitChanges();
            }
        };
        service.getApplicationsName(applicationClass, callback);
    }
}
