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
package fr.insalyon.creatis.platform.main.client.view.common.toolbar;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.SyntaxHighlightPanel;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import fr.insalyon.creatis.platform.main.client.view.common.window.fileviewer.SyntaxHighlightWindow;
import fr.insalyon.creatis.platform.main.client.view.common.window.fileviewer.FileViewerWindow;

/**
 *
 * @author ibrahim Kallel
 */
public class LogsToolbarButton extends ToolbarButton {

    private String clickedFileName;
    private String clickedDir;

    public LogsToolbarButton(String workflowID) {

        this.clickedDir = "/" + workflowID;
        this.setText("Logs");
        Menu logMenu = new Menu();
        logMenu.setShadow(true);
        logMenu.setMinWidth(15);


        Item outputlog = new Item("Workflow.out", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                clickedFileName = "workflow.out";
                loadFileWindow();
            }
        });

        Item errorlog = new Item("Workflow.err", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                clickedFileName = "workflow.err";
                loadFileWindow();
            }
        });

        Item xmllog = new Item("Workflow.xml", new BaseItemListenerAdapter() {

            @Override
            public void onClick(BaseItem item, EventObject e) {
                clickedFileName = "workflow.xml";
                loadFileWindow();
            }
        });

        logMenu.addItem(outputlog);
        logMenu.addItem(errorlog);
        logMenu.addItem(xmllog);

        this.setMenu(logMenu);

    }

    private void loadFileWindow() {
        if (clickedFileName.toLowerCase().endsWith(".xml")) {
            new SyntaxHighlightWindow(clickedDir, clickedFileName, SyntaxHighlightPanel.SYNTAX_XML, "Logs");
        } else {
            new FileViewerWindow(clickedDir, clickedFileName, "Logs");
        }
    }
}
