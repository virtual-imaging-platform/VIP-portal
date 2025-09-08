package fr.insalyon.creatis.vip.datamanager.client.view.system.cache;

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
 * @author Rafael Ferreira da Silva
 */
public class ManageCachedFilesToolStrip extends ToolStrip {

    public ManageCachedFilesToolStrip(final ModalWindow modal) {

        this.setWidth100();

        ToolStripButton refreshButton = new ToolStripButton("Refresh");
        refreshButton.setIcon(CoreConstants.ICON_REFRESH);
        refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ManageCachedFilesTab tab = (ManageCachedFilesTab) Layout.getInstance().getTab(DataManagerConstants.TAB_MANAGE_CACHED_FILES);
                tab.loadData();
            }
        });
        this.addButton(refreshButton);

        ToolStripButton deleteSelectedFiles = new ToolStripButton("Delete Selected Files");
        deleteSelectedFiles.setIcon(CoreConstants.ICON_DELETE);
        deleteSelectedFiles.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to delete all selected files?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {

                        if (value) {
                            final ManageCachedFilesTab tab = (ManageCachedFilesTab) Layout.getInstance().getTab(DataManagerConstants.TAB_MANAGE_CACHED_FILES);
                            List<String> paths = new ArrayList<String>();

                            for (ListGridRecord record : tab.getGridSelection()) {
                                CachedFileRecord cf = (CachedFileRecord) record;
                                paths.add(cf.getPath());
                            }

                            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                            AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    modal.hide();
                                    Layout.getInstance().setWarningMessage("Unable to delete cached files:<br />" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    modal.hide();
                                    tab.loadData();
                                }
                            };
                            modal.show("Deleting cached files...", true);
                            service.deleteCachedFiles(paths, callback);
                        }
                    }
                });
            }
        });
        this.addButton(deleteSelectedFiles);
    }
}
