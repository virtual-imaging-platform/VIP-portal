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
