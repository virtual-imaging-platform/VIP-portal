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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.modal.ModalWindow;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.FileCatalogService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.FileCatalogServiceAsync;

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

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setIcon("icon-delete.png");
        deleteItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                if (baseDir.equals(DataManagerConstants.ROOT)) {
                    SC.warn("You cannot delete a folder in the root folder.");
                } else {
                    delete(modal, baseDir, data.getName());
                }
            }
        });

        this.setItems(uploadItem, renameItem, deleteItem);
    }

    private void delete(final ModalWindow modal, final String baseDir, final String name) {
        SC.confirm("Do you really want to delete \"" + name + "\"?", new BooleanCallback() {

            public void execute(Boolean value) {
                if (value != null && value) {
                    FileCatalogServiceAsync service = FileCatalogService.Util.getInstance();
                    AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Error executing delete files/folders: " + caught.getMessage());
                        }

                        public void onSuccess(Void result) {
                            modal.hide();
                            BrowserLayout.getInstance().loadData(baseDir, true);
                        }
                    };
                    modal.show("Deleting " + name + "...", true);
                    Context context = Context.getInstance();
                    String oldPath = baseDir + "/" + name;
                    String newPath = DataManagerConstants.ROOT + "/" 
                            + DataManagerConstants.TRASH_HOME + "/" + name;
                    service.rename(context.getUser(), context.getProxyFileName(),
                            oldPath, newPath, callback);
                }
            }
        });
    }
}
