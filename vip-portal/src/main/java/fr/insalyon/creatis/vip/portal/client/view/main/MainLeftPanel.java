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
package fr.insalyon.creatis.vip.portal.client.view.main;

import com.gwtext.client.widgets.Panel;
import fr.insalyon.creatis.vip.common.client.bean.Authentication;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.portal.client.view.layout.AbstractLeftPanel;
import fr.insalyon.creatis.vip.portal.client.view.layout.Layout;

/**
 *
 * @author Rafael Silva
 */
public class MainLeftPanel extends AbstractLeftPanel {

    public MainLeftPanel() {
        super(null);
        title = "VIP Platform";
        collapsed = false;
    }

    @Override
    public Panel getPanel() {
        Panel panel = new Panel();
        panel.setPaddings(5, 5, 5, 5);

        Authentication auth = Context.getInstance().getAuthentication();
        Layout layout = Layout.getInstance();
        layout.setCenterPanel(MainCenterPanel.getInstance());

        String text = "";
        if (auth.isProxyValid()) {
            text = "You have valid grid credentials!";
        } else {
            if (auth.getUserName().equals("Anonymous")) {
                layout.addCenterPanel(QuickstartCenterPanel.getInstance());
                layout.setActiveCenterPanel("main-center-panel");
                text = "Welcome to the Virtual Imaging Platform (VIP)";
                
            } else {

                text = "Warning! You do not have valid credentials uploaded!<br />"
                        + "Please, go to <a href=\"http://kingkong.grid.creatis.insa-lyon.fr:9008/portal/web/vip/proxy-initialisation\">"
                        + "our portal</a> to upload your proxy.";
            }
        }

        panel.setHtml("<p style=\"font-size: 11px\">" + text + "</p>");

        return panel;
    }
}
