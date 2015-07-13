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
package fr.insalyon.creatis.vip.gatelab.client.view.monitor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.property.PropertyRecord;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerSection;
import fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;

/**
 *
 * @author Sorina Camarasu
 */
public class GeneralInformationContextMenu extends Menu {

    private ModalWindow modal;

    public GeneralInformationContextMenu(String simulationID, 
            final PropertyRecord record, ModalWindow modal) {

        this.modal = modal;

        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem downloadFileItem = new MenuItem("Download File");
        downloadFileItem.setIcon("icon-download.png");
        downloadFileItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                downloadFile(record.getValue());
            }
        });

        MenuItem jumpToItem = new MenuItem("Go to Folder");
        jumpToItem.setIcon("icon-jumpto.png");
        jumpToItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                String folder = record.getValue().substring(0, record.getValue().lastIndexOf("/"));
                BrowserLayout.getInstance().loadData(folder, false);
                ((DataManagerSection)Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
              }
        });

        if (record.getProperty().equals("Input") 
                || record.getProperty().equals("Output") 
                || record.getProperty().equals("Gate Release")) {

            this.setItems(downloadFileItem, jumpToItem);
        }
    }

    private void downloadFile(String path) {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to download file:<br />" + caught.getMessage());
            }

            public void onSuccess(String result) {
                modal.hide();
                OperationLayout.getInstance().addOperation(result);
                ((DataManagerSection)Layout.getInstance().getMainSection(DataManagerConstants.SECTION_FILE_TRANSFER)).expand();
            }
        };
        modal.show("Adding file to transfer queue...", true);
        service.downloadFile(path, callback);
    }
}
