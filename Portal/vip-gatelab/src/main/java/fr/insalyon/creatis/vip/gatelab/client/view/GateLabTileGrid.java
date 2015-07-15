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
package fr.insalyon.creatis.vip.gatelab.client.view;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationsTileGrid;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.gatelab.client.GateLabConstants;
import fr.insalyon.creatis.vip.gatelab.client.view.launch.GateLabLaunchTab;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class GateLabTileGrid extends ApplicationsTileGrid {

    private List<String> applicationNames;

    public GateLabTileGrid(String tileName) {

        super(tileName);
        applicationNames = new ArrayList<String>();
        loadApplications(tileName);
    }

    private void loadApplications(String applicationClass) {

        final AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load gatelab:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String[]> result) {

                for (String[] app : result) {
                    addApplication(app[0], app[1], GateLabConstants.APP_IMG_APPLICATION);
                    applicationNames.add(app[0] + " " + app[1]);
                }
            }
        };
        ApplicationService.Util.getInstance().getApplications(applicationClass, callback);
    }

    @Override
    public void parse(String applicationName, String applicationVersion) {

        String appName = applicationVersion == null ? applicationName : applicationName + " " + applicationVersion;
        if (applicationNames.contains(appName)) {
            Layout.getInstance().addTab(new GateLabLaunchTab(applicationName, 
                    applicationVersion, tileName));
        }
    }
}
