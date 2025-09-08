package fr.insalyon.creatis.vip.datamanager.client.view.selection;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.TextItem;
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
    private Runnable toRunOnSelection;
    private Menu contextMenu;
    private String name;

    public PathSelectionWindow(TextItem textItem) {
        this(textItem, null);
    }
   
    public PathSelectionWindow(TextItem textItem, Runnable toRunOnSelection) {

        this.textItem = textItem;
        this.toRunOnSelection = toRunOnSelection;

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
        textItem.setValue(value);
        if (toRunOnSelection != null) {
            toRunOnSelection.run();
        }
        destroy();
    }
}
