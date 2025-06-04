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
package fr.insalyon.creatis.vip.application.client.view;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.ApplicationModule;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.client.inter.CustomApplicationModule;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchTab;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationsTileGrid;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationTileGrid extends ApplicationsTileGrid {

    private List<String> applicationNames;

    public ApplicationTileGrid(Group group) {
        super(group.getName());
        applicationNames = new ArrayList<String>();
        loadApplications(group);
    }

    private void loadApplications(Group group) {
        final AsyncCallback<Map<Application, List<AppVersion>>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load applications:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Map<Application, List<AppVersion>> result) {
                for (var set : result.entrySet()) {
                    Application app = set.getKey();

                    if (app.getApplicationGroups().contains(group.getName())) {
                        for (var version : set.getValue()) {
                            addApplication(app.getName(), version.getVersion(), ApplicationConstants.APP_IMG_APPLICATION);
                            applicationNames.add(app.getName() + " " + version.getVersion());
                        }
                    }
                }
            }
        };
        ApplicationService.Util.getInstance().getApplications(callback);
    }

    @Override
    public void parse(final String applicationName, final String applicationVersion) {
        final AsyncCallback<Boolean> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to check application avaibility:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    load(applicationName, applicationVersion);
                } else {
                    Layout.getInstance().setWarningMessage("Sorry, but this application application is temporarily unavailable !");
                }
            }
        };
        ApplicationService.Util.getInstance().isAppUsableWithCurrentUser(applicationName, applicationVersion, callback);
    }

    private void load(String appName, String appVersion) {
        final AsyncCallback<List<Tag>> callback = new AsyncCallback<>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to load the application:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Tag> result) {
                display(appName, appVersion, result);
            }
        };
        ApplicationService.Util.getInstance().getTags(appName, appVersion, callback);
    }


    private void display(final String applicationName, final String applicationVersion, final List<Tag> tags) {
        String appName = applicationVersion == null ? applicationName : applicationName + " " + applicationVersion;
        CustomApplicationModule custom = retrieveView(tags);

        if (applicationNames.contains(appName)) {
            if (custom != null) {
                custom.run(applicationName, applicationVersion, tileName);
            } else {
                Layout.getInstance().addTab(
                    ApplicationConstants.getLaunchTabID(applicationName),
                    () -> new LaunchTab(
                        applicationName, applicationVersion, tileName));
            }
        }
    }

    private CustomApplicationModule retrieveView(List<Tag> tags) {
        for (CustomApplicationModule custom : ApplicationModule.customModules) {
            if (custom.doOverride(tags)) {
                return custom;
            }
        }
        return null;
    }
}
