package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.PublicExecutionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ReproVipBusiness;
import fr.insalyon.creatis.vip.application.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.application.client.rpc.ReproVipService;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;

import jakarta.servlet.ServletException;
import java.util.List;

public class ReproVipServiceImpl extends AbstractRemoteServiceServlet implements ReproVipService {

    private ReproVipBusiness reproVipBusiness;
    private PublicExecutionBusiness publicExecutionBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        reproVipBusiness = getBean(ReproVipBusiness.class);
        publicExecutionBusiness = getBean(PublicExecutionBusiness.class);
    }

    @Override
    public void addPublicExecution(PublicExecution publicExecution) throws ApplicationException {
        try {
            if (publicExecutionBusiness.exist(publicExecution.getExperienceName())) {
                throw new ApplicationException("This experience name already exist!");
            } else {
                publicExecutionBusiness.create(publicExecution);
            }
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public List<PublicExecution> getPublicExecutions() throws ApplicationException {
        try {
            return publicExecutionBusiness.getAll();
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public boolean doesExecutionExist(String experienceName) throws ApplicationException {
        try {
            return publicExecutionBusiness.exist(experienceName);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public boolean canMakeExecutionPublic(List<String> workflowsIds) throws ApplicationException {
        try {
            return reproVipBusiness.canMakeExecutionPublic(workflowsIds);
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public PublicExecution.PublicExecutionStatus createReproVipDirectory(String experienceName) {
        try {
            reproVipBusiness.createReproVipDirectory(experienceName);
            publicExecutionBusiness.updateStatus(experienceName, PublicExecution.PublicExecutionStatus.DIRECTORY_CREATED);
            return PublicExecution.PublicExecutionStatus.DIRECTORY_CREATED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PublicExecution.PublicExecutionStatus deleteReproVipDirectory(String experienceName) throws ApplicationException {
        try {
            reproVipBusiness.deleteReproVipDirectory(experienceName);
            publicExecutionBusiness.updateStatus(experienceName, PublicExecution.PublicExecutionStatus.REQUESTED);
            return PublicExecution.PublicExecutionStatus.REQUESTED;
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public PublicExecution.PublicExecutionStatus setExecutionPublished(String experienceName, String doi) throws ApplicationException {
        try {
            PublicExecution exec = publicExecutionBusiness.get(experienceName);

            exec.setDoi(doi);
            publicExecutionBusiness.update(experienceName, exec);
            publicExecutionBusiness.updateStatus(experienceName, PublicExecution.PublicExecutionStatus.PUBLISHED);
            return PublicExecution.PublicExecutionStatus.PUBLISHED;
        } catch (BusinessException e) {
            throw new ApplicationException(e);
        }
    }
}
