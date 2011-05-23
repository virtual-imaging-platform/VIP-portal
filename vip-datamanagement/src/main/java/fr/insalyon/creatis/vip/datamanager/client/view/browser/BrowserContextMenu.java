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
package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Silva
 */
public class BrowserContextMenu extends Menu {

    public BrowserContextMenu(final ModalWindow modal, final String baseDir, final DataRecord data) {

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem uploadItem = new MenuItem("Upload File");
        uploadItem.setIcon("icon-upload.png");
        uploadItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (baseDir.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot upload a file in the root folder.");
                } else {
                    new FileUploadWindow(modal, baseDir).show();
                }
            }
        });
        
        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setIcon("icon-edit.png");
        renameItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (baseDir.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot rename a folder in the root folder.");
                } else {
                    new RenameWindow(modal, baseDir, data.getName()).show();
                }
            }
        });

        this.setItems(uploadItem, renameItem);
    }
}
