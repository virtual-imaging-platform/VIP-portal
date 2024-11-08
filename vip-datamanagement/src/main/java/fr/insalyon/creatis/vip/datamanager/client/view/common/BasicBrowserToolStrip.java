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
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.ValidatorUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.AddFolderWindow;

public class BasicBrowserToolStrip extends ToolStrip {

    private BasicBrowserToolStrip toolStrip;
    protected ModalWindow modal;
    protected SelectItem pathItem;
    private SelectItem fileTypeSelect;
    private ListGrid grid;
    private TextItem searchItem;

    public BasicBrowserToolStrip(final ModalWindow modal, final ListGrid grid1) {
        grid = grid1;
        this.modal = modal;
        this.toolStrip = this;
        //this.setWidth100();

        Label titleLabel = new Label("&nbsp;&nbsp;Platform Files");
        titleLabel.setWidth(85);
        this.addMember(titleLabel);
        //this.addSeparator();

        pathItem = new SelectItem("path");
        pathItem.setShowTitle(false);
        pathItem.setWidth("100%");
        pathItem.setValue(DataManagerConstants.ROOT);
        this.addFormItem(pathItem);
          
        fileTypeSelect = new SelectItem();
        fileTypeSelect.setTitle("File Type");
        fileTypeSelect.setShowTitle(false);
        fileTypeSelect.setWidth(120);
        fileTypeSelect.setValueMap("All Files (*.*)", "JSON File (*.json)");
        fileTypeSelect.setDefaultToFirstOption(true);
        BrowserUtil.fileType = "all";
        fileTypeSelect.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                String fileType = event.getValue().toString();
                if ("JSON File (*.json)".equals(fileType)) {
                    // Filter JSON files
                    BrowserUtil.fileTypeChanged("json");
                } else {
                    // Show all files
                    BrowserUtil.fileTypeChanged("all");
                }
                reloadGridData();
            }
        });
        
        this.addFormItem(fileTypeSelect);

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
                BrowserUtil.fileType = "all";
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

        searchItem = new TextItem();
        searchItem.setTitle("Search");
        searchItem.setShowTitle(false);
        searchItem.setWidth(120);
        searchItem.setHint("<span style='color:gray'>Search</span>");
        searchItem.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(com.smartgwt.client.widgets.form.fields.events.KeyPressEvent event) {
                // Reload grid data when the user presses Enter in the search box
                if ("Enter".equals(event.getKeyName())) {
                    filterData();
                }
            }
        });
        this.addFormItem(searchItem);
    }

    public String getPath() {
        return pathItem.getValueAsString();
    }

    public void setPath(String path) {
        pathItem.setValue(path);
    }

    public void reloadGridData() {
        String path = toolStrip.getPath(); // Get the current path
        boolean refresh = true; // Force refresh of data
    
        // Reload data based on the current path and fileType value
        BrowserUtil.loadData(modal, grid, toolStrip, path, refresh);
    }

    public void filterData() {
        String searchValue = searchItem.getValueAsString(); // Get the value entered in the search box
        BrowserUtil.setSearchValue(searchValue); // Set the search value in BrowserUtil
        reloadGridData(); // Reload grid data with the new search value
    }
}
