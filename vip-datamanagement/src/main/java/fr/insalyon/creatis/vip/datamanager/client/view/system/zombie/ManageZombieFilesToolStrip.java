/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.datamanager.client.view.system.zombie;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ManageZombieFilesToolStrip extends ToolStrip {

    public ManageZombieFilesToolStrip(final ModalWindow modal) {

        this.setWidth100();

        ToolStripButton refreshButton = new ToolStripButton("Refresh");
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                ManageZombieFilesTab tab = (ManageZombieFilesTab) Layout.getInstance().getTab(DataManagerConstants.TAB_MANAGE_ZOMBIE_FILES);
                tab.loadData();
            }
        });
        this.addButton(refreshButton);

        ToolStripButton deleteSelectedFiles = new ToolStripButton("Delete Selected SURLs");
        deleteSelectedFiles.setIcon(CoreConstants.ICON_DELETE);
        deleteSelectedFiles.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to delete all selected SURLs?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        
                        if (value) {    
                            final ManageZombieFilesTab tab = (ManageZombieFilesTab) Layout.getInstance().getTab(DataManagerConstants.TAB_MANAGE_ZOMBIE_FILES);
                            List<String> surls = new ArrayList<String>();

                            for (ListGridRecord record : tab.getGridSelection()) {
                                ZombieFileRecord zf = (ZombieFileRecord) record;
                                surls.add(zf.getSURL());
                            }

                            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                            AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                                public void onFailure(Throwable caught) {
                                    modal.hide();
                                    SC.warn("Unable to delete SURLs:<br />" + caught.getMessage());
                                }

                                public void onSuccess(Void result) {
                                    modal.hide();
                                    tab.loadData();
                                }
                            };
                            modal.show("Deleting SURLs...", true);
                            service.deleteZombieFiles(surls, callback);
                        }
                    }
                });
            }
        });
        this.addButton(deleteSelectedFiles);
    }
}
