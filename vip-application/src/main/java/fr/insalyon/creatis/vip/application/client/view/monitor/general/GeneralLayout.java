/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.monitor.general;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Label;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.view.monitor.progress.ProgressLayout;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GeneralLayout extends AbstractFormLayout {

    private String simulationID;
    private Label generalLabel;
    private ProgressLayout progressLayout;

    public GeneralLayout(String simulationID, SimulationStatus status) {

        super("100%", "140px");
        this.simulationID = simulationID;

        generalLabel = new Label();
        generalLabel.setHeight(25);
        generalLabel.setIcon(ApplicationConstants.ICON_GENERAL);
        generalLabel.setCanSelectText(true);
        this.addMember(generalLabel);

        addTitle("<font color=\"#333333\">Execution Progress</font>", null);
        progressLayout = new ProgressLayout(simulationID, status);
        this.addMember(progressLayout);
        
        loadData();
    }

    private void loadData() {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Simulation> callback = new AsyncCallback<Simulation>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load general information:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Simulation result) {
                
                StringBuilder sb = new StringBuilder();
                sb.append("<font color=\"#333333\"><b>");
                sb.append(result.getApplicationName());
                sb.append(" ");
                sb.append(result.getApplicationVersion());
                sb.append("</b> launched on <b>");
                sb.append(result.getDate().toString());
                sb.append("</b>");
                if (CoreModule.user.isSystemAdministrator()) {
                    sb.append(" by <b>");
                    sb.append(result.getUserName());
                    sb.append("</b> (");
                    sb.append(result.getID());
                    sb.append(")</font>");
                }              
                generalLabel.setContents(sb.toString());
            }
        };
        service.getSimulation(simulationID, callback);
    }

    public void update() {
        progressLayout.update();
    }
}
