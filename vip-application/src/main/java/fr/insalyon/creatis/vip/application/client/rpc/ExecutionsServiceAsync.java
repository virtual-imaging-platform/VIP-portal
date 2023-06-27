package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.core.client.bean.Execution;

import java.util.List;

public interface ExecutionsServiceAsync {
    void getExecutions(AsyncCallback<List<Execution>> callback);
}
