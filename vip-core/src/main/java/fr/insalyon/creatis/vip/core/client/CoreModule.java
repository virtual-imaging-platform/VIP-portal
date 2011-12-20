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
package fr.insalyon.creatis.vip.core.client;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationExecutor;
import fr.insalyon.creatis.vip.core.client.view.contact.ContactTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.layout.toolstrip.MainToolStrip;
import fr.insalyon.creatis.vip.core.client.view.system.SystemParser;
import fr.insalyon.creatis.vip.core.client.view.system.SystemTab;
import fr.insalyon.creatis.vip.core.client.view.user.UserMenuButton;
import fr.insalyon.creatis.vip.core.client.view.main.HomeParser;
import fr.insalyon.creatis.vip.core.client.view.main.HomeTab;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class CoreModule extends Module {

    public static User user;
    public static ApplicationExecutor systemExecutor;
    public static ApplicationExecutor homeExecutor;
    public static List<String> accountTypes;

    public CoreModule() {

        systemExecutor = new ApplicationExecutor();
        systemExecutor.addParser(new SystemParser());

        homeExecutor = new ApplicationExecutor();
        homeExecutor.addParser(new HomeParser());
    }

    @Override
    public void load() {

        // Configure User's toolstrip        
        MainToolStrip.getInstance().addMenuButton(new UserMenuButton(user));

        // Tabs
        if (user.isGroupAdmin()) {
            Layout.getInstance().addTab(new SystemTab());
        }
        Layout.getInstance().addTab(new HomeTab());
    }

    @Override
    public void postLoading() {
        
        ToolStripButton helpButton = new ToolStripButton("Experiencing problems?");
        helpButton.setIcon(CoreConstants.ICON_HELP);
        helpButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Layout.getInstance().addTab(new ContactTab());
            }
        });
        
        MainToolStrip.getInstance().addFill();
        MainToolStrip.getInstance().addMember(helpButton);
    }
    
    @Override
    public boolean parseAccountType(String accountType) {
        
        if (accountType.equals(CoreConstants.ACCOUNT_OTHER)) {
            return true;
        }
        return false;
    }
    
    @Override
    public void terminate() {
    }   
}
