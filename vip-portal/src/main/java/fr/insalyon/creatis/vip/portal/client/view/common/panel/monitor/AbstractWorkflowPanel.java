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
package fr.insalyon.creatis.vip.portal.client.view.common.panel.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.GenericConfig;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.DefaultsHandler;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowServiceAsync;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractWorkflowPanel extends Panel {

    protected TabPanel tabPanel;
    protected String workflowID;
    protected boolean completed;

    public AbstractWorkflowPanel(String workflowID, String workflowStatus) {

        this.workflowID = workflowID;
        this.setId(workflowID);
        this.setTitle(workflowID);
        this.setClosable(true);
        this.setLayout(new FitLayout());

        tabPanel = new TabPanel();
        tabPanel.setTabPosition(Position.BOTTOM);
        tabPanel.setResizeTabs(true);
        tabPanel.setMinTabWidth(80);
        tabPanel.setActiveTab(0);

        completed = !workflowStatus.equals("Running") ? true : false;

        tabPanel.setDefaults(new DefaultsHandler() {

            public void apply(Component component) {
                if (!Ext.isIE()) {
                    component.setHideMode("visibility");
                    GenericConfig config = new GenericConfig();
                    config.setProperty("position", "absolute");
                    component.setStyle(config);
                }
            }
        });

        this.add(tabPanel);
    }

    @Override
    protected void onDestroy() {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error closing the connection: " + caught.getMessage());
            }

            public void onSuccess(Void v) {
            }
        };
        service.closeConnection(workflowID, callback);
    }
}
