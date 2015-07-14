/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
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
package fr.insalyon.creatis.vip.application.client.view.system.application;

import com.smartgwt.client.widgets.layout.HLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractManageTab;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class ManageApplicationsTab extends AbstractManageTab {

    private ApplicationsLayout appsLayout;
    private EditApplicationLayout editLayout;
    private VersionsLayout versionsLayout;
    private EditVersionLayout editVersionLayout;

    public ManageApplicationsTab() {

        super(ApplicationConstants.ICON_APPLICATION, ApplicationConstants.APP_APPLICATION, ApplicationConstants.TAB_MANAGE_APPLICATION);

        appsLayout = new ApplicationsLayout();
        editLayout = new EditApplicationLayout();

        HLayout appLayout = new HLayout(5);
        appLayout.setHeight("50%");
        appLayout.addMember(appsLayout);
        appLayout.addMember(editLayout);
        vLayout.addMember(appLayout);

        versionsLayout = new VersionsLayout();
        editVersionLayout = new EditVersionLayout();

        HLayout versionLayout = new HLayout(5);
        versionLayout.setHeight("50%");
        versionLayout.addMember(versionsLayout);
        versionLayout.addMember(editVersionLayout);
        vLayout.addMember(versionLayout);
    }

    public void loadApplications() {
        appsLayout.loadData();
    }

    public void loadVersions(String applicationName) {
        versionsLayout.setApplication(applicationName);
        editVersionLayout.setApplication(applicationName);
    }

    public void setApplication(String name, String owner, String classes, String citation) {
        editLayout.setApplication(name, owner, classes, citation);
    }

    public void setVersion(String version, String lfn, boolean isVisible) {
        editVersionLayout.setVersion(version, lfn, isVisible);
    }
}
