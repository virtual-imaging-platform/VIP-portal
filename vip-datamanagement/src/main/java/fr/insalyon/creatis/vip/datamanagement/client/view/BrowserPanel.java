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
package fr.insalyon.creatis.vip.datamanagement.client.view;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;

/**
 *
 * @author Rafael Silva
 */
public class BrowserPanel extends Panel {

    private ToolbarTextItem pathItem;

    public BrowserPanel() {
        this.setLayout(new FitLayout());
        this.setMargins(0, 0, 0, 0);
        this.setBorder(false);
        this.add(getRemoteGrid());
        this.setTopToolbar(getToolbar());
        this.setWidth(420);
    }

    private GridPanel getRemoteGrid() {

        GridPanel remoteGrid = new GridPanel();
        remoteGrid.setFrame(true);
        remoteGrid.setStripeRows(true);
        remoteGrid.setMargins(0, 0, 0, 0);

        CheckboxSelectionModel cbSelectionModel = new CheckboxSelectionModel();

        Store remoteStore = new SimpleStore(
                new String[]{"typeico", "fileName"}, getRemoteData());
        remoteStore.load();
        remoteGrid.setStore(remoteStore);

        remoteGrid.addGridRowListener(new GridRowListenerAdapter() {

            @Override
            public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
                super.onRowDblClick(grid, rowIndex, e);
            }
        });

        BaseColumnConfig[] columns = {
            new CheckboxColumnConfig(cbSelectionModel),
            getIcoTypeColumnConfig(),
            new ColumnConfig("File Name", "fileName", 450),};
        ColumnModel columnModel = new ColumnModel(columns);
        remoteGrid.setColumnModel(columnModel);
        remoteGrid.setSelectionModel(cbSelectionModel);

        return remoteGrid;
    }

    private Object[][] getRemoteData() {
        return new Object[][]{
                    new Object[]{"Folder", "abc"},
                    new Object[]{"File", "qwwe.txt"},
                    new Object[]{"File", "file231.doc"}
                };
    }

    private ColumnConfig getIcoTypeColumnConfig() {
        ColumnConfig icoColumn = new ColumnConfig("", "typeico", 25);
        icoColumn.setRenderer(new Renderer() {

            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex,
                    int colNum, Store store) {
                String status = ((String) value);
                String image = "icon-file.png";
                if (status.equals("Folder")) {
                    image = "icon-folder.gif";
                }
                return "<img src=\"./images/" + image + "\" style=\"border: 0\"/>";
            }
        });
        return icoColumn;
    }

    private Toolbar getToolbar() {

        Toolbar topToolbar = new Toolbar();

        ComboBox pathComboBox = new ComboBox();
        pathComboBox.setWidth(300);

        // Folder up Button
        ToolbarButton folderupButton = new ToolbarButton("", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                // TODO
            }
        });
        folderupButton.setIcon("images/icon-folderup.gif");
        folderupButton.setCls("x-btn-icon");

        // Refresh Button
        ToolbarButton refreshButton = new ToolbarButton("", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                // TODO
            }
        });
        refreshButton.setIcon("images/icon-refresh.gif");
        refreshButton.setCls("x-btn-icon");

        // Actions Menu
        ToolbarButton actionsButton = new ToolbarButton("Actions");
        Menu menu = new Menu();

        Item uploadItem = new Item("Upload a File");
        Item downloadSelectedItem = new Item("Download Selected Files");
        Item deleteSelectedItem = new Item("Delete Selected Files");

        menu.addItem(uploadItem);
        menu.addSeparator();
        menu.addItem(downloadSelectedItem);
        menu.addItem(deleteSelectedItem);
        actionsButton.setMenu(menu);

        topToolbar.addField(pathComboBox);
        topToolbar.addButton(folderupButton);
        topToolbar.addButton(refreshButton);
        topToolbar.addButton(actionsButton);

        return topToolbar;
    }
}
