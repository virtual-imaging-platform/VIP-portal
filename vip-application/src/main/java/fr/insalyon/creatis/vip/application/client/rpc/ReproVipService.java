package fr.insalyon.creatis.vip.application.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import fr.insalyon.creatis.vip.application.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.core.client.VipException;

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

    void addPublicExecution(PublicExecution publicExecution) throws VipException;

    public List<PublicExecution> getPublicExecutions() throws VipException;

    boolean doesExecutionExist(String experienceName) throws VipException;

    boolean canMakeExecutionPublic(List<String> workflowsIds) throws VipException;

    PublicExecution.PublicExecutionStatus createReproVipDirectory(String experienceName) throws VipException;

    PublicExecution.PublicExecutionStatus deleteReproVipDirectory(String experienceName) throws VipException;

    PublicExecution.PublicExecutionStatus setExecutionPublished(String experienceName, String doi) throws VipException;
}

