package fr.insalyon.creatis.vip.application.client.view.monitor.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.LogsTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.ViewerWindow;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class LogsContextMenu extends Menu {

    private LogsTab section;
    private String baseDir;

    public LogsContextMenu(LogsTab section, final String simulationID,
            final String dataName, final String folder, boolean isFile) {

        this.section = section;
        this.baseDir = "/" + simulationID + "/" + folder;
        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem viewItem = new MenuItem("View File");
        viewItem.setIcon(ApplicationConstants.ICON_PREVIEW);
        viewItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                new ViewerWindow("Viewing File: " + dataName,
                        simulationID, folder, dataName, "").show();
            }
        });

        MenuItem downloadItem = new MenuItem("Download");
        downloadItem.setIcon(DataManagerConstants.ICON_DOWNLOAD);
        downloadItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                Window.open(
                        GWT.getModuleBaseURL()
                        + "/getfileservice?filepath=" + baseDir + "/" + dataName,
                        "", "");
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setIcon(CoreConstants.ICON_DELETE);
        deleteItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to delete '" + baseDir + "/" + dataName + "'?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value != null && value) {
                            delete(baseDir + "/" + dataName);
                        }
                    }
                });
            }
        });

        if (isFile) {
            this.setItems(viewItem, downloadItem, deleteItem);

        } else {
            this.setItems(downloadItem, deleteItem);
        }
    }

    private void delete(String path) {

        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                section.getModal().hide();
                Layout.getInstance().setWarningMessage("Unable to delete:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                section.getModal().hide();
                section.loadData(baseDir);
            }
        };
        section.getModal().show("Deleting data...", true);
        service.deleteLogData(path, callback);
    }
}
