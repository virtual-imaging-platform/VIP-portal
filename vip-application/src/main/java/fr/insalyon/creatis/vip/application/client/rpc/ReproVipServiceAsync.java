package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.bean.Execution;

public interface ReproVipServiceAsync {
    void executionAdminEmail(Execution execution, AsyncCallback<Void> callback);
    void addExecution(Execution execution, AsyncCallback<Void> asyncCallback);
    void updateExecution(String executionID, String newStatus, AsyncCallback<Void> asyncCallback);
    void doesExecutionExist(String executionID, AsyncCallback<Boolean> asyncCallback);
    void createReproVipDirectory(String executionName, String execution, String version, String comments, AsyncCallback<String> callback);
    void deleteReproVipDirectory(String executionID, AsyncCallback<String> callback);
}

