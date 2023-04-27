package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface ReproVipService extends RemoteService {

    public static final String SERVICE_URI = "/reprovipservice";

    public static class Util {

        public static ReproVipServiceAsync getInstance() {

            ReproVipServiceAsync instance = (ReproVipServiceAsync) GWT.create(ReproVipService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public void executionAdminEmail(String user);
}
