package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.vip.application.server.business.ReproVipBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.application.client.rpc.ReproVipService;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.ExecutionPublicDAO;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;

import javax.servlet.ServletException;

public class ReproVipServiceImpl extends AbstractRemoteServiceServlet implements ReproVipService {
    private ReproVipBusiness reproVipBusiness;
    private ExecutionPublicDAO executionPublicDAO;
    @Override
    public void init() throws ServletException {
        super.init();
        reproVipBusiness = getBean(ReproVipBusiness.class);
        executionPublicDAO = getBean(ExecutionPublicDAO.class);
    }

    public void executionAdminEmail(Execution execution) {
        try {
            reproVipBusiness.executionAdminEmail(execution);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
    }

    // execution management
    @Override
    public void addExecution(Execution execution) throws CoreException {
        try {
            executionPublicDAO.add(execution);
        } catch (DAOException e) {
            throw new CoreException("Failed to add execution", e);
        }
    }

    @Override
    public void updateExecution(String executionID, String newStatus) throws CoreException {
        try {
            executionPublicDAO.update(executionID, newStatus);
        } catch (DAOException e) {
            throw new CoreException("Failed to update execution", e);
        }
    }
    public String createReproVipDirectory(String executionName, String executionID, String version, String comments) {
        try {
            User currentUser = getSessionUser();
            return reproVipBusiness.createReproVipDirectory(executionName, executionID, version, comments, currentUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
