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
package fr.insalyon.creatis.vip.datamanager.client.view.common;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.ValidatorUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.AddFolderWindow;

/**
 *
 * @author Rafael Silva
 */
public class BasicBrowserToolStrip extends ToolStrip {

    private BasicBrowserToolStrip toolStrip;
    protected ModalWindow modal;
    protected SelectItem pathItem;

    public BasicBrowserToolStrip(final ModalWindow modal, final ListGrid grid) {

        this.modal = modal;
        this.toolStrip = this;
        this.setWidth100();

        Label titleLabel = new Label("&nbsp;&nbsp;Platform Files");
        titleLabel.setWidth(75);
        this.addMember(titleLabel);
        this.addSeparator();

        pathItem = new SelectItem("path");
        pathItem.setShowTitle(false);
        pathItem.setWidth(400);
        pathItem.setValue(DataManagerConstants.ROOT);
        this.addFormItem(pathItem);

        // Folder Up Button
        this.addButton(WidgetUtil.getToolStripButton(
                DataManagerConstants.ICON_FOLDER_UP, "Folder up", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (!pathItem.getValueAsString().equals(DataManagerConstants.ROOT)) {
                    String newPath = pathItem.getValueAsString();
                    BrowserUtil.loadData(modal, grid, toolStrip,
                            newPath.substring(0, newPath.lastIndexOf("/")), false);
                }
            }
        }));

        // Refresh Button
        this.addButton(WidgetUtil.getToolStripButton(
                CoreConstants.ICON_REFRESH, "Refresh", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                BrowserUtil.loadData(modal, grid, toolStrip, pathItem.getValueAsString(), true);
            }
        }));

        // Home Button
        this.addButton(WidgetUtil.getToolStripButton(
                CoreConstants.ICON_HOME, "Home", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                BrowserUtil.loadData(modal, grid, toolStrip, DataManagerConstants.ROOT, false);
            }
        }));

        // Add Folder Button
        this.addButton(WidgetUtil.getToolStripButton(
                DataManagerConstants.ICON_FOLDER_ADD, "Create Folder", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String path = toolStrip.getPath();
                if (ValidatorUtil.validateRootPath(path, "create a folder in")
                        && ValidatorUtil.validateUserLevel(path, "create a folder in")) {
                    new AddFolderWindow(modal, path).show();
                }
            }
        }));
    }

    public String getPath() {
        return pathItem.getValueAsString();
    }

    public void setPath(String path) {
        pathItem.setValue(path);
    }
}
