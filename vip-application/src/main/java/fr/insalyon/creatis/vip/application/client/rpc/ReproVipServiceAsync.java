package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReproVipServiceAsync {
    public void executionAdminEmail(String user, AsyncCallback<Void> callback);
}

