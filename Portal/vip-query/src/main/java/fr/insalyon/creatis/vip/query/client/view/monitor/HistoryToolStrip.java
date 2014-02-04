/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.client.view.monitor;

//import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.QueryConstants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nouha Boujelben
 */
public class HistoryToolStrip extends ToolStrip {

    private ModalWindow modal;

    public HistoryToolStrip(ModalWindow modal) {

        this.modal = modal;
        this.setWidth100();

        // Refresh Button
        this.addButton(WidgetUtil.getToolStripButton("Refresh",
                CoreConstants.ICON_REFRESH, null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getQueryHistoryTab().loadData();
            }
        }));

        // Search Button
        this.addButton(WidgetUtil.getToolStripButton("Search Query",
                QueryConstants.ICON_SEARCH, null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getQueryHistoryTab().setFilter();
            }
        }));


        //purge query
        this.addButton(WidgetUtil.getToolStripButton("Purge Query",
                CoreConstants.ICON_CLEAR, null, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {


                SC.ask("Do you really want to purge the selected  Query Execution?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            purgeQuery();
                        }
                    }
                });

            }
        }));

    }

    private void purgeQuery() {
        ListGridRecord[] records = getQueryHistoryTab().getGridSelection();

        for (ListGridRecord record : records) {

            Long id = record.getAttributeAsLong("queryExecutionID");

            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {

                    Layout.getInstance().setWarningMessage("Unable to purge simulations:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    getQueryHistoryTab().loadData();
                }
            };
            QueryService.Util.getInstance().removeQueryExecution(id, callback);
        }
    }

    private QueryHistoryTab getQueryHistoryTab() {
        return (QueryHistoryTab) Layout.getInstance().getTab(QueryConstants.TAB_QUERYHISTORY);
    }
}
