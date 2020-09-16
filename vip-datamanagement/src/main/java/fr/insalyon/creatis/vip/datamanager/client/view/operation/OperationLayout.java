/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.client.view.operation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.PoolOperation;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerService;
import fr.insalyon.creatis.vip.datamanager.client.rpc.DataManagerServiceAsync;
import java.util.Date;
import java.util.List;

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

    /**
     * Removes all operations from the list.
     */
    public void clearOperations() {

        operationsLayout.removeMembers(operationsLayout.getMembers());
    }
}
