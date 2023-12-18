package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.ReproVipBusiness;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.application.client.rpc.ReproVipService;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.application.server.dao.PublicExecutionDAO;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ReproVipServiceImpl extends AbstractRemoteServiceServlet implements ReproVipService {

    private ReproVipBusiness reproVipBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        reproVipBusiness = getBean(ReproVipBusiness.class);
    }

    @Override
    public void addPublicExecution(PublicExecution publicExecution) throws ApplicationException {
        try {
            reproVipBusiness.createPublicExecution(publicExecution);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public List<PublicExecution> getPublicExecutions() throws ApplicationException {
        try {
            return reproVipBusiness.getPublicExecutions();
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public boolean doesExecutionExist(String executionID) throws ApplicationException {
        try {
            return reproVipBusiness.doesExecutionExist(executionID);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public PublicExecution.PublicExecutionStatus createReproVipDirectory(String executionID) {
        try {
            reproVipBusiness.createReproVipDirectory(executionID);
            reproVipBusiness. updatePublicExecutionStatus(executionID, PublicExecution.PublicExecutionStatus.DIRECTORY_CREATED);
            return PublicExecution.PublicExecutionStatus.DIRECTORY_CREATED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PublicExecution.PublicExecutionStatus deleteReproVipDirectory(String executionID) throws ApplicationException {
        try {
            reproVipBusiness.deleteReproVipDirectory(executionID);
            reproVipBusiness. updatePublicExecutionStatus(executionID, PublicExecution.PublicExecutionStatus.REQUESTED);
            return PublicExecution.PublicExecutionStatus.REQUESTED;
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }
}
