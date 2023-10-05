package fr.insalyon.creatis.vip.application.server.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;

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
    @Override
    public void executionOutputData(String executionID) throws CoreException {
        try {
            User currentUser = getSessionUser();
            reproVipBusiness.executionOutputData(executionID, currentUser);
        } catch (BusinessException | ApplicationException e) {
            throw new RuntimeException(e);
        }
    }
    public String downloadJsonOutputData(String executionID) throws CoreException {
        try {
            User currentUser = getSessionUser();
            String json = reproVipBusiness.createJsonOutputData(executionID, currentUser);
            return json;
        } catch (BusinessException | ApplicationException e) {
            throw new RuntimeException(e);
        }
    }
    public void createReproVipDirectory() {
        try {
            reproVipBusiness.createReproVipDirectory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
