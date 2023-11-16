package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;

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
    public void executionAdminEmail(Execution execution);
    void addExecution(Execution execution) throws CoreException;
    void updateExecution(String executionID, String newStatus) throws CoreException;
    boolean doesExecutionExist(String executionID) throws CoreException;
    String createReproVipDirectory(String executionName, String executionID, String version, String comments) throws CoreException;
    void deleteReproVipDirectory(String executionID) throws CoreException;
}

