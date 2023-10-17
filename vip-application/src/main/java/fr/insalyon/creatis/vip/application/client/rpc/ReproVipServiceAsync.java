package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.client.bean.User;

public interface ReproVipServiceAsync {
    void executionAdminEmail(Execution execution, AsyncCallback<Void> callback);
    void addExecution(Execution execution, AsyncCallback<Void> asyncCallback);
    void updateExecution(String executionID, String newStatus, AsyncCallback<Void> asyncCallback);
    void executionOutputData(String executionID, AsyncCallback<Void> callback);
    void downloadJsonOutputData(String executionID, AsyncCallback<String> callback);
    void createReproVipDirectory(String executionName, String execution, String version, AsyncCallback<Void> callback);
}

