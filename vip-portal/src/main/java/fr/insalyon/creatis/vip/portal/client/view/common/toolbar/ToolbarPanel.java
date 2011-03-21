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
package fr.insalyon.creatis.vip.portal.client.view.common.toolbar;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import fr.insalyon.creatis.vip.portal.client.bean.AppClass;
import fr.insalyon.creatis.vip.portal.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.portal.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.portal.client.view.common.panel.AbstractPanel;
import java.util.List;

/**
 *
 * @author Rafael Silva, Tristan Glatard
 */
public class ToolbarPanel extends AbstractPanel {

    private Toolbar toolbar;

    public ToolbarPanel() {
        super(null);
    }

    protected void buildPanel() {

        this.setWidth("100%");
        this.setHeight(25);

        toolbar = new Toolbar();
        toolbar.setHeight(25);

        // Home Menu
        toolbar.addButton(new HomeToolbarButton("VIP Platform"));
        toolbar.addSeparator();

        // Data Management Menu
        toolbar.addButton(new DataManagerToolbarButton("Data Manager"));
        toolbar.addSeparator();

        // System Menu
        if (isSystemAdmin()) {
            toolbar.addButton(new SystemToolbarButton("System"));
        }

        // Gatelab Menu
        if (hasGroupAccess(new String[]{"Administrator", "GateLab"})) {
            toolbar.addButton(new GatelabToolbarButton("GateLab"));
        }

        // Tissues Menu
        if (hasGroupAccess(new String[]{"Administrator", "Physical Properties"})) {
            toolbar.addButton(new PhysicalPropertiesButton("Physical Properties"));
        }

        // Application Menus
        loadApplicationMenus();

        this.setTopToolbar(toolbar);
    }

    private void loadApplicationMenus() {
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<AppClass>> callback = new AsyncCallback<List<AppClass>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error", "Error executing get classes list\n" + caught.getMessage());
            }

            public void onSuccess(List<AppClass> result) {
                for (AppClass appClass : result) {
                    String[] appGroups = appClass.getGroups().toArray(new String[0]);
                    if (hasGroupAccess(appGroups)) {
                        toolbar.addButton(new ApplicationToolbarButton(appClass.getName(), 
                                isGroupAdmin(appGroups)));
                    }
                }
            }
        };
        service.getClasses(callback);
    }

    public void addClassButton(String className) {
        toolbar.addButton(new ApplicationToolbarButton(className, false));
    }
}
