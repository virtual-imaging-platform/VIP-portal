/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.client.view;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.view.system.application.ManageApplicationsTab;
import fr.insalyon.creatis.vip.application.client.view.system.classes.ManageClassesTab;
import fr.insalyon.creatis.vip.application.client.view.system.engine.ManageEnginesTab;
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

        if (CoreModule.user.isSystemAdministrator() || CoreModule.user.isGroupAdmin()) {
            addApplication(ApplicationConstants.APP_APPLICATION, ApplicationConstants.APP_IMG_APPLICATION);
        }
        if (CoreModule.user.isSystemAdministrator()) {
            addApplication(ApplicationConstants.APP_CLASSES, ApplicationConstants.APP_IMG_CLASSES);
            addApplication(ApplicationConstants.APP_ENGINE, ApplicationConstants.APP_IMG_ENGINE);
        }
    }

    @Override
    public boolean parse(String applicationName, String applicationVersion) {

        if (applicationName.equals(ApplicationConstants.APP_APPLICATION)) {
            Layout.getInstance().addTab(new ManageApplicationsTab());
            return true;

        } else if (applicationName.equals(ApplicationConstants.APP_CLASSES)) {
            Layout.getInstance().addTab(new ManageClassesTab());
            return true;

        } else if (applicationName.equals(ApplicationConstants.APP_ENGINE)) {
            Layout.getInstance().addTab(new ManageEnginesTab());
            return true;

        }
        return false;
    }
}
