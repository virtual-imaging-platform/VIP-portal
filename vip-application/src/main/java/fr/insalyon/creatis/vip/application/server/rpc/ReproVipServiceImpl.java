package fr.insalyon.creatis.vip.application.server.rpc;

import java.util.List;

import fr.insalyon.creatis.vip.application.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.application.client.rpc.ReproVipService;
import fr.insalyon.creatis.vip.application.server.business.PublicExecutionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ReproVipBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import jakarta.servlet.ServletException;

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
    public void addPublicExecution(PublicExecution publicExecution) throws VipException {
        if (publicExecutionBusiness.exist(publicExecution.getExperienceName())) {
            throw new VipException("This experience name already exist!");
        } else {
            publicExecutionBusiness.create(publicExecution);
        }
    }

    @Override
    public List<PublicExecution> getPublicExecutions() throws VipException {
        return publicExecutionBusiness.getAll();
    }

    @Override
    public boolean doesExecutionExist(String experienceName) throws VipException {
        return publicExecutionBusiness.exist(experienceName);
    }

    @Override
    public boolean canMakeExecutionPublic(List<String> workflowsIds) throws VipException {
        return reproVipBusiness.canMakeExecutionPublic(workflowsIds);
    }

    @Override
    public PublicExecution.PublicExecutionStatus createReproVipDirectory(String experienceName) {
        try {
            reproVipBusiness.createReproVipDirectory(experienceName);
            publicExecutionBusiness.updateStatus(experienceName,
                    PublicExecution.PublicExecutionStatus.DIRECTORY_CREATED);

            return PublicExecution.PublicExecutionStatus.DIRECTORY_CREATED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PublicExecution.PublicExecutionStatus deleteReproVipDirectory(String experienceName) throws VipException {
        reproVipBusiness.deleteReproVipDirectory(experienceName);
        publicExecutionBusiness.updateStatus(experienceName, PublicExecution.PublicExecutionStatus.REQUESTED);

        return PublicExecution.PublicExecutionStatus.REQUESTED;
    }

    @Override
    public PublicExecution.PublicExecutionStatus setExecutionPublished(String experienceName, String doi) throws VipException {
        PublicExecution exec = publicExecutionBusiness.get(experienceName);

        exec.setDoi(doi);
        publicExecutionBusiness.update(experienceName, exec);
        publicExecutionBusiness.updateStatus(experienceName, PublicExecution.PublicExecutionStatus.PUBLISHED);
        return PublicExecution.PublicExecutionStatus.PUBLISHED;
    }
}
