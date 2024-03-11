package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;

import java.util.List;

public interface ReproVipServiceAsync {

    void addPublicExecution(PublicExecution publicExecution, AsyncCallback<Void> asyncCallback);

    void getPublicExecutions(AsyncCallback<List<PublicExecution>> callback);

    void doesExecutionExist(String executionID, AsyncCallback<Boolean> asyncCallback);

    void canMakeExecutionPublic(String executionID, AsyncCallback<Boolean> asyncCallback);

    void createReproVipDirectory(String executionId, AsyncCallback<PublicExecution.PublicExecutionStatus> callback);

    void deleteReproVipDirectory(String executionId, AsyncCallback<PublicExecution.PublicExecutionStatus> callback);
}

