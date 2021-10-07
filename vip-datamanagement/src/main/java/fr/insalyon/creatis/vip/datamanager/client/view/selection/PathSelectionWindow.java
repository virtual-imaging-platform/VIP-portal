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
package fr.insalyon.creatis.vip.datamanager.client.view.selection;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.common.BasicBrowserToolStrip;
import fr.insalyon.creatis.vip.datamanager.client.view.common.BrowserUtil;

/**
 *
 * @author Rafael Silva
 */
public class PathSelectionWindow extends Window {

    private static String oldPath = DataManagerConstants.ROOT;
    private BasicBrowserToolStrip toolStrip;
    private ListGrid grid;
    private HLayout bottomLayout;
    private ModalWindow modal;
    private TextItem textItem;
    private Menu contextMenu;
    private String name;
   
    public PathSelectionWindow(TextItem textItem) {

        this.textItem = textItem;

        this.setTitle("Path Selection");
        this.setWidth(600);
        this.setHeight(350);
        this.setShowMinimizeButton(false);
        this.setIsModal(true);
        this.setShowModalMask(true);
        this.centerInPage();

        grid = BrowserUtil.getListGrid();
        modal = new ModalWindow(grid);
        toolStrip = new BasicBrowserToolStrip(modal, grid);

        configureGrid();
        configureContextMenu();
        configureBottom();

        this.addItem(toolStrip);
        this.addItem(grid);
        this.addItem(bottomLayout);

        String path = textItem.getValueAsString();
        if (path != null) {
            path = path.trim();
            if (!path.isEmpty() && path.startsWith(DataManagerConstants.ROOT)) {
                oldPath = path.substring(0, path.lastIndexOf("/"));
            }
        }
        BrowserUtil.loadData(modal, grid, toolStrip, oldPath, false);
    }

    private void configureGrid() {
        
        grid.setSelectionType(SelectionStyle.SINGLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        
        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            public void onCellDoubleClick(CellDoubleClickEvent event) {
                String type = event.getRecord().getAttributeAsString("icon");
                String name = event.getRecord().getAttributeAsString("name");

                if (type.contains("folder")) {
                    BrowserUtil.loadData(modal, grid, toolStrip,
                            toolStrip.getPath() + "/" + name, false);
                    oldPath = toolStrip.getPath() + "/" + name;
                } else {
                    selectValueAndDestroy(toolStrip.getPath() + "/" + name);
                }
            }
        });
        grid.addCellContextClickHandler(new CellContextClickHandler() {

            public void onCellContextClick(CellContextClickEvent event) {
                event.cancel();
                name = event.getRecord().getAttributeAsString("name");
                contextMenu.showContextMenu();
            }
        });        
    }
    
    private void configureBottom() {
        
        bottomLayout = new HLayout(5);
        bottomLayout.setWidth100();
        bottomLayout.setHeight(24);
        bottomLayout.setAlign(Alignment.RIGHT);
        bottomLayout.setPadding(2);
        
        IButton selectButton = new IButton("Select");
        selectButton.setIcon(CoreConstants.ICON_ACTIVE);
        selectButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(ClickEvent event) {
                name = grid.getSelectedRecord().getAttributeAsString("name");
                selectValueAndDestroy(toolStrip.getPath() + "/" + name);
            }
        });
        
        IButton cancelButton = new IButton("Cancel");
        cancelButton.setIcon(CoreConstants.ICON_DELETE);
        cancelButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(ClickEvent event) {
                destroy();
            }
        });
        
        bottomLayout.addMember(selectButton);
        bottomLayout.addMember(cancelButton);
    }

    private void configureContextMenu() {

        contextMenu = new Menu();
        contextMenu.setShowShadow(true);
        contextMenu.setShadowDepth(10);
        contextMenu.setWidth(90);

        MenuItem selectItem = new MenuItem("Select this path");
        selectItem.setIcon(CoreConstants.ICON_ACTIVE);
        selectItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                selectValueAndDestroy(toolStrip.getPath() + "/" + name);
            }
        });
        contextMenu.setItems(selectItem);
    }

    private void selectValueAndDestroy(String value) {
        textItem.setValue(toolStrip.getPath() + "/" + name);
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), textItem);
        destroy();
    }
}
