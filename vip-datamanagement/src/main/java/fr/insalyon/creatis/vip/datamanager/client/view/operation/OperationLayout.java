package fr.insalyon.creatis.vip.datamanager.client.view.operation;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import fr.insalyon.creatis.vip.datamanager.models.PoolOperation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;


/**
 *
 * @author Rafael Ferreira da Silva
 */
public class OperationLayout extends VLayout {

    private static OperationLayout instance;
    private OperationToolStrip toolStrip;
    private VLayout operationsLayout;
    private Date lastDate;
    private MoreOperationsBoxLayout loadMoreDataBoxLayout;

    public static OperationLayout getInstance() {
        if (instance == null) {
            instance = new OperationLayout();
        }
        return instance;
    }

    public static void terminate() {
        instance.destroy();
        instance = null;
    }

    private OperationLayout() {

        this.setWidth("35%");
        this.setMaxWidth(450);
        this.setHeight100();
        this.setOverflow(Overflow.AUTO);

        operationsLayout = new VLayout(2);
        operationsLayout.setWidth100();
        operationsLayout.setHeight100();
        operationsLayout.setPadding(5);
        operationsLayout.setOverflow(Overflow.AUTO);
        operationsLayout.setAlign(VerticalAlignment.TOP);
        operationsLayout.setBackgroundColor("#F2F2F2");

        toolStrip = new OperationToolStrip();
        this.addMember(toolStrip);
        this.addMember(operationsLayout);

        loadMoreDataBoxLayout = new MoreOperationsBoxLayout();
        loadMoreDataBoxLayout.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                loadMoreData();
            }
        });

        loadData();
    }

    private void loadData() {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<List<PoolOperation>> callback = new AsyncCallback<List<PoolOperation>>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of operations:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<PoolOperation> result) {

                operationsLayout.removeMembers(operationsLayout.getMembers());

                if (!result.isEmpty()) {
                    for (PoolOperation operation : result) {
                        operationsLayout.addMember(new OperationBoxLayout(operation));
                        lastDate = operation.getRegistration();
                    }
                    if (result.size() == DataManagerConstants.MAX_OPERATIONS_LIMIT) {
                        operationsLayout.addMember(loadMoreDataBoxLayout);
                    }
                }
            }
        };
        service.getPoolOperationsByUser(callback);
    }

    private void loadMoreData() {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<List<PoolOperation>> callback = new AsyncCallback<List<PoolOperation>>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage("Unable to get list of operations:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<PoolOperation> result) {

                operationsLayout.removeMember(loadMoreDataBoxLayout);

                if (!result.isEmpty()) {

                    for (PoolOperation operation : result) {
                        operationsLayout.addMember(new OperationBoxLayout(operation));
                        lastDate = operation.getRegistration();
                    }
                    if (result.size() == DataManagerConstants.MAX_OPERATIONS_LIMIT) {
                        operationsLayout.addMember(loadMoreDataBoxLayout);
                    }
                }
            }
        };
        service.getPoolOperationsByUserAndDate(lastDate, callback);
    }

    /**
     * Adds an operation to the list of operations.
     *
     * @param operationID
     */
    public void addOperation(final String operationID) {

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        AsyncCallback<PoolOperation> asyncCallback = new AsyncCallback<PoolOperation>() {

            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage(operationID + "<br />Unable to get operation data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(PoolOperation result) {
                operationsLayout.addMember(new OperationBoxLayout(result), 0);
            }
        };
        service.getPoolOperationById(operationID, asyncCallback);
    }

    public void addOperationsWithCallback(String[] operationsIds, AsyncCallback<Void> callback) {
        if (operationsIds.length == 1) {
            addOperationWithCallback(operationsIds[0], callback);
            return;
        }
        AsyncCallback<Void> counterCallback = new AsyncCallback<Void>() {
            Integer counter = operationsIds.length;
            @Override
            public void onFailure(Throwable caught) {} // cannot be called

            @Override
            public void onSuccess(Void v) {
                counter--;
                if (counter == 0) {
                    callback.onSuccess(null);
                }
            }
        };

        DataManagerServiceAsync service = DataManagerService.Util.getInstance();
        for (String operationID : operationsIds) {
            service.getPoolOperationById(operationID, new AsyncCallback<PoolOperation>() {
                @Override
                public void onFailure(Throwable caught) {
                    Layout.getInstance().setWarningMessage(operationID + "<br />Unable to get operation data:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(PoolOperation result) {
                    operationsLayout.addMember(new OperationBoxLayout(result, counterCallback), 0);
                }
            });
        }
    }

    public void addOperationWithCallback(String operationID, AsyncCallback<Void> callback) {
        DataManagerService.Util.getInstance().getPoolOperationById(operationID, new AsyncCallback<PoolOperation>() {
            @Override
            public void onFailure(Throwable caught) {
                Layout.getInstance().setWarningMessage(operationID + "<br />Unable to get operation data:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(PoolOperation result) {
                operationsLayout.addMember(new OperationBoxLayout(result, callback), 0);
            }
        });
    }

    /**
     * Removes all operations from the list.
     */
    public void clearOperations() {

        operationsLayout.removeMembers(operationsLayout.getMembers());
    }
}
