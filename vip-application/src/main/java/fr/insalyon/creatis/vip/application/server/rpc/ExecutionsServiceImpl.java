package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.vip.application.client.rpc.ExecutionsService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import java.util.List;

public class ExecutionsServiceImpl extends AbstractRemoteServiceServlet implements ExecutionsService {
    @Autowired
    private ExecutionsBusiness executionsBusiness;
    @Override
    public void init() throws ServletException {
        super.init();
        executionsBusiness = getBean(ExecutionsBusiness.class);
    }
    @Override
    public List<Execution> getExecutions() throws ApplicationException {
        if(executionsBusiness != null) {
            try {
                return executionsBusiness.getExecutions();
            } catch (BusinessException e) {
                throw new ApplicationException(e);
            }
        } else {
            throw new ApplicationException("ExecutionsBusiness est null");
        }
    }

}
