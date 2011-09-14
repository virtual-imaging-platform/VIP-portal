/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.common.AbstractLaunchTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class LaunchToolStrip extends ToolStrip {

    private String applicationClass;
    private SelectItem simulatorItem;

    public LaunchToolStrip(final String applicationClass, final String tabID) {

        this.applicationClass = applicationClass;
        this.setWidth100();

        simulatorItem = new SelectItem(applicationClass);
        simulatorItem.setWidth(300);
        simulatorItem.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                String simulationName = simulatorItem.getValueAsString();
                if (simulationName != null && !simulationName.isEmpty()) {
                    AbstractLaunchTab launchTab = 
                            (AbstractLaunchTab) Layout.getInstance().getTab(tabID);
                    launchTab.createSimulation(simulationName);
                    launchTab.addInputsSection();
                }
            }
        });
        this.addFormItem(simulatorItem);
        
        loadData();
    }

    private void loadData() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                SC.warn("Error executing get workflow descriptors lists\n" + caught.getMessage());
            }

            public void onSuccess(List<String> result) {

                LinkedHashMap<String, String> appsMap = new LinkedHashMap<String, String>();

                for (String s : result) {
                    appsMap.put(s, s);
                }
                simulatorItem.setValueMap(appsMap);
            }
        };
        service.getApplicationsName(applicationClass, callback);
    }
}
