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
import fr.insalyon.creatis.vip.application.client.view.system.applications.app.ManageApplicationsTab;
import fr.insalyon.creatis.vip.application.client.view.system.engines.ManageEnginesTab;
import fr.insalyon.creatis.vip.application.client.view.system.resources.ManageResourcesTab;
import fr.insalyon.creatis.vip.application.client.view.system.tags.ManageTagsTab;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ApplicationSystemParser extends ApplicationParser {

    @Override
    public void loadApplications() {
        if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin() || CoreModule.user.isDeveloper()) {
            addApplication(ApplicationConstants.APP_APPLICATION, ApplicationConstants.APP_IMG_APPLICATION);
        }
        if (CoreModule.user.isSystemAdministrator()) {
            addApplication(ApplicationConstants.APP_ENGINE, ApplicationConstants.APP_IMG_ENGINE);
            addApplication(ApplicationConstants.APP_RESOURCE, ApplicationConstants.APP_IMG_RESOURCE);
            addApplication(ApplicationConstants.APP_TAG, ApplicationConstants.APP_IMG_TAG);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {
        switch (applicationName) {
            case ApplicationConstants.APP_APPLICATION:
                Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_APPLICATION,
                    () -> new ManageApplicationsTab(false));
                return true;

            case ApplicationConstants.APP_ENGINE:
                Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_ENGINE, ManageEnginesTab::new);
                return true;

            case ApplicationConstants.APP_RESOURCE:
                Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_RESOURCE, ManageResourcesTab::new);
                return true;

            case ApplicationConstants.APP_TAG:
                Layout.getInstance().addTab(
                    ApplicationConstants.TAB_MANAGE_TAG, ManageTagsTab::new);
                return true;

            default:
                return false;
        }
    }
}
