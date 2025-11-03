package fr.insalyon.creatis.vip.datamanager.client.view.operation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class OperationToolStrip extends ToolStrip {

    private ToolStripButton clearButton;

    public OperationToolStrip() {

        this.setWidth100();
        this.setPadding(2);

        Label titleLabel = new Label("Pool of Transfers");
        titleLabel.setWidth(150);
        this.addMember(titleLabel);

        // Clear Button
        this.addFill();
        clearButton = WidgetUtil.getToolStripButton("Clear List",
                DataManagerConstants.OP_ICON_CLEAR,
                "Remove all operations from this list", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you want to clear all operations?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {

                        if (value) {
                            DataManagerServiceAsync service = DataManagerService.Util.getInstance();
                            AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    WidgetUtil.resetToolStripButton(clearButton, "Clear List", DataManagerConstants.OP_ICON_CLEAR);
                                    Layout.getInstance().setWarningMessage("Unable to remove operations:<br />" + caught.getMessage());
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    WidgetUtil.resetToolStripButton(clearButton, "Clear List", DataManagerConstants.OP_ICON_CLEAR);
                                    OperationLayout.getInstance().clearOperations();
                                }
                            };
                            WidgetUtil.setLoadingToolStripButton(clearButton, "Clearing...");
                            service.removeUserOperations(callback);
                        }
                    }
                });
            }
        });
        this.addButton(clearButton);
    }
}
