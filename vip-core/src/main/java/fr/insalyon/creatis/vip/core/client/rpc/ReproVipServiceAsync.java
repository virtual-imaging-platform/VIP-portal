package fr.insalyon.creatis.vip.core.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.bean.Execution;

public interface ReproVipServiceAsync {
    void executionAdminEmail(Execution execution, AsyncCallback<Void> callback);
    void addExecution(Execution execution, AsyncCallback<Void> asyncCallback);
    void updateExecution(Execution execution, AsyncCallback<Void> asyncCallback);
}

