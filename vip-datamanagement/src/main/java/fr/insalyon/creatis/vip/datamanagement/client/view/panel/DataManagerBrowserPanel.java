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
package fr.insalyon.creatis.vip.datamanagement.client.view.panel;

import com.google.gwt.user.client.DOM;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.menu.Menu;
import fr.insalyon.creatis.vip.datamanagement.client.view.menu.BrowserActionsMenu;
import fr.insalyon.creatis.vip.datamanagement.client.view.menu.BrowserMenu;

/**
 *
 * @author Rafael Silva
 */
public class DataManagerBrowserPanel extends AbstractBrowserPanel {

    private static DataManagerBrowserPanel instance;
    private Menu menu;
    private String name;
    private String type;

    public static DataManagerBrowserPanel getInstance() {
        if (instance == null) {
            instance = new DataManagerBrowserPanel();
        }
        return instance;
    }

    private DataManagerBrowserPanel() {
        super("dm-browser");
        this.setWidth(420);
        this.configureGrid();
        this.configureToolbar();
    }

    private void configureGrid() {
        grid.setEnableDragDrop(true);
        grid.setDdGroup("dm-browser-dd");
        grid.addGridRowListener(new GridRowListenerAdapter() {

            @Override
            public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
                Record record = grid.getStore().getRecordAt(rowIndex);
                if (record.getAsString("typeico").equals("Folder")) {
                    String clickedFolderName = record.getAsString("fileName");
                    String parentDir = pathCB.getValue();
                    loadData(parentDir + "/" + clickedFolderName, true);
                }
            }

            @Override
            public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
                DOM.eventPreventDefault(e.getBrowserEvent());
                Record record = grid.getStore().getRecordAt(rowIndex);
                name = record.getAsString("fileName");
                type = record.getAsString("typeico");
                showMenu(e);
            }
        });
    }

    private void configureToolbar() {
        // Actions Menu
        ToolbarButton actionsButton = new ToolbarButton("Actions");
        actionsButton.setMenu(new BrowserActionsMenu());
        topToolbar.addButton(actionsButton);
    }

    private void showMenu(EventObject e) {
        if (menu == null) {
            menu = new BrowserMenu();
        }
        menu.showAt(e.getXY());
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public CheckboxSelectionModel getCbSelectionModel() {
        return cbSelectionModel;
    }

    public RowSelectionModel getSelectionModel() {
        return grid.getSelectionModel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
