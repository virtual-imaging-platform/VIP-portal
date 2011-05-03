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
package fr.insalyon.creatis.vip.portal.client.view.application.monitor.menu;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.portal.client.view.application.monitor.NodeInfoWindow;
import fr.insalyon.creatis.vip.portal.client.view.common.window.FileViewerWindow;

/**
 *
 * @author Rafael Silva
 */
public class JobsContextMenu extends Menu {

    public JobsContextMenu(final String simulationID, final String jobID, 
            String status, final String fileName) {
        
        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);
        
        MenuItem appOutputItem = new MenuItem("View Application Output");
        appOutputItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new FileViewerWindow("Viewing Application Output for Job ID " + jobID, 
                        simulationID, "out", fileName, ".sh.app.out").show();
            }
        });
        
        MenuItem appErrorItem = new MenuItem("View Application Error");
        appErrorItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new FileViewerWindow("Viewing Application Error for Job ID " + jobID, 
                        simulationID, "err", fileName, ".sh.app.err").show();
            }
        });
        
        MenuItem outputItem = new MenuItem("View Output File");
        outputItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new FileViewerWindow("Viewing Output File for Job ID " + jobID, 
                        simulationID, "out", fileName, ".sh.out").show();
            }
        });
        
        MenuItem errorItem = new MenuItem("View Error File");
        errorItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new FileViewerWindow("Viewing Error File for Job ID " + jobID, 
                        simulationID, "err", fileName, ".sh.err").show();
            }
        });
        
        MenuItem scriptItem = new MenuItem("View Script File");
        scriptItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new FileViewerWindow("Viewing Script File for Job ID " + jobID, 
                        simulationID, "sh", fileName, ".sh").show();
            }
        });
        
        MenuItem nodeItem = new MenuItem("Node Information");
        nodeItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new NodeInfoWindow(simulationID, jobID).show();
            }
        });
        
        MenuItemSeparator separator = new MenuItemSeparator();
        
        if (status.equals("ERROR") || status.equals("COMPLETED")) {
            this.setItems(appOutputItem, appErrorItem, separator, 
                    outputItem, errorItem, separator, scriptItem,
                    separator, nodeItem);
        } else {
            this.setItems(scriptItem);
        }
    }
}
