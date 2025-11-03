package fr.insalyon.creatis.vip.datamanager.client.view.browser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.NamedFrame;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.view.common.BrowserUtil;
import fr.insalyon.creatis.vip.datamanager.client.view.operation.OperationLayout;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class BrowserLayout extends VLayout {

    private static BrowserLayout instance;
    private ModalWindow modal;
    private BrowserToolStrip toolStrip;
    private ListGrid grid;
    private DataUploadWindow dataUploadWindow;

    public static BrowserLayout getInstance() {
        if (instance == null) {
            instance = new BrowserLayout();
        }
        return instance;
    }

    public static void terminate() {

        instance = null;
    }

    private BrowserLayout() {

        initComplete(this);
        this.setWidth("*");
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);
        this.setShowResizeBar(true);

        this.grid = BrowserUtil.getListGrid();
        configureGrid();

        modal = new ModalWindow(grid);
        toolStrip = new BrowserToolStrip(modal, grid);
        this.addMember(toolStrip);
        this.addMember(grid);

        loadData(DataManagerConstants.ROOT, false);

        NamedFrame frame = new NamedFrame("dataManagerUploadComplete");
        frame.setVisible(false);
        frame.setHeight("1px");
        frame.setWidth("1px");
        this.addMember(frame);
    }

    private void configureGrid() {

        grid.setSelectionType(SelectionStyle.SIMPLE);
        grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

        grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

            @Override
            public void onCellDoubleClick(CellDoubleClickEvent event) {
                String type = event.getRecord().getAttributeAsString("icon");
                if (type.contains("folder") || type.contains("trash")) {
                    String name = event.getRecord().getAttributeAsString("name");
                    String path = toolStrip.getPath() + "/" + name;
                    loadData(path, false);
                }
            }
        });
        grid.addCellContextClickHandler(new CellContextClickHandler() {

            @Override
            public void onCellContextClick(CellContextClickEvent event) {
                event.cancel();
                if (event.getColNum() != 0) {
                    DataRecord data = (DataRecord) event.getRecord();
                    new BrowserContextMenu(modal,
                            toolStrip.getPath(), data).showContextMenu();
                }
            }
        });
    }

    public void loadData(final String path, boolean refresh) {
        BrowserUtil.loadData(modal, grid, toolStrip, path, refresh);
    }

    public ListGridRecord[] getGridSelection() {
        return grid.getSelectedRecords();
    }

    public void mask(String maskText) {
        modal.show(maskText, true);
    }

    public void unmask() {
        modal.hide();
    }

    public void setDataUploadWindow(DataUploadWindow dataUploadWindow) {
        this.dataUploadWindow = dataUploadWindow;
    }

    public BrowserToolStrip getToolStrip() {
        return toolStrip;
    }

    public void uploadComplete(String result) {
        
        if (dataUploadWindow != null) {
            dataUploadWindow.destroy();
            dataUploadWindow = null;
        }
        modal.hide();

        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {} // cannot be called

            @Override
            public void onSuccess(Void v) {
                loadData(toolStrip.getPath(), true);
            }
        };

        String[] operationsIds = result.split("##");

        OperationLayout.getInstance().addOperationsWithCallback(operationsIds, callback);

    }

    private native void initComplete(BrowserLayout upload) /*-{
    $wnd.dataManagerUploadComplete = function (fileName) {
    upload.@fr.insalyon.creatis.vip.datamanager.client.view.browser.BrowserLayout::uploadComplete(Ljava/lang/String;)(fileName);
    };
    }-*/;
}
