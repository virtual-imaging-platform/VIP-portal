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
package fr.insalyon.creatis.vip.datamanager.client.view.common;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;

/**
 *
 * @author Rafael Silva
 */
public class BasicBrowserToolStrip extends ToolStrip {

    protected ModalWindow modal;
    protected SelectItem pathItem;

    public BasicBrowserToolStrip(ModalWindow modal) {

        this.modal = modal;
        this.setWidth100();

        pathItem = new SelectItem("path");
        pathItem.setShowTitle(false);
        pathItem.setWidth(400);
        pathItem.setValue(DataManagerConstants.ROOT);
        this.addFormItem(pathItem);

        ToolStripButton folderUpButton = new ToolStripButton();
        folderUpButton.setIcon("icon-folderup.png");
        folderUpButton.setPrompt("Folder up");
        folderUpButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (!pathItem.getValueAsString().equals(DataManagerConstants.ROOT)) {
                    String newPath = pathItem.getValueAsString();
                    BrowserLayout.getInstance().loadData(
                            newPath.substring(0, newPath.lastIndexOf("/")), false);
                }
            }
        });
        this.addButton(folderUpButton);
        ToolStripButton refreshButton = new ToolStripButton();
        refreshButton.setIcon("icon-refresh.png");
        refreshButton.setPrompt("Refresh");
        refreshButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                BrowserLayout.getInstance().loadData(pathItem.getValueAsString(), true);
            }
        });
        this.addButton(refreshButton);

        ToolStripButton homeButton = new ToolStripButton();
        homeButton.setIcon("icon-home.png");
        homeButton.setPrompt("Home");
        homeButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                BrowserLayout.getInstance().loadData(DataManagerConstants.ROOT, false);
            }
        });
        this.addButton(homeButton);
    }

    public String getPath() {
        return pathItem.getValueAsString();
    }

    public void setPath(String path) {
        pathItem.setValue(path);
    }
}
