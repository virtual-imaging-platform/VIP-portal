package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.core.client.bean.Execution;

import java.util.List;

public interface ExecutionsService extends RemoteService {

    public static final String SERVICE_URI = "/executionsservice";

    public static class Util {
        public static ExecutionsServiceAsync getInstance() {
            ExecutionsServiceAsync instance = (ExecutionsServiceAsync) GWT.create(ExecutionsService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }
    public List<Execution> getExecutions() throws ApplicationException;
}

