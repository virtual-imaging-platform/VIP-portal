package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.insalyon.creatis.vip.application.models.PublicExecution;

import java.util.List;

public interface ReproVipServiceAsync {

    void addPublicExecution(PublicExecution publicExecution, AsyncCallback<Void> asyncCallback);

    void getPublicExecutions(AsyncCallback<List<PublicExecution>> callback);

    void doesExecutionExist(String experienceName, AsyncCallback<Boolean> asyncCallback);

    void canMakeExecutionPublic(List<String> workflowsIds, AsyncCallback<Boolean> asyncCallback);

    void createReproVipDirectory(String experienceName, AsyncCallback<PublicExecution.PublicExecutionStatus> callback);

    void deleteReproVipDirectory(String experienceName, AsyncCallback<PublicExecution.PublicExecutionStatus> callback);

    void setExecutionPublished(String experienceName, String doi, AsyncCallback<PublicExecution.PublicExecutionStatus> asyncCallback);
}

