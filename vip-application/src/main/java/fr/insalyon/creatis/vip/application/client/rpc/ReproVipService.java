package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;

import java.util.List;

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

    void addPublicExecution(PublicExecution PublicExecution) throws ApplicationException;

    public List<PublicExecution> getPublicExecutions() throws ApplicationException;

    boolean doesExecutionExist(String experienceName) throws ApplicationException;

    boolean canMakeExecutionPublic(PublicExecution PublicExecution) throws ApplicationException;

    PublicExecution.PublicExecutionStatus createReproVipDirectory(String experienceName) throws ApplicationException;

    PublicExecution.PublicExecutionStatus deleteReproVipDirectory(String experienceName) throws ApplicationException;

    PublicExecution.PublicExecutionStatus setExecutionPublished(String experienceName, String doi) throws ApplicationException;
}

