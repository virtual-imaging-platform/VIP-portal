package fr.insalyon.creatis.vip.application.server.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.application.server.dao.PublicExecutionDAO;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

@Service
@Transactional
public class PublicExecutionBusiness {
    final private PublicExecutionDAO publicExecutionDAO;
    final private ConfigurationBusiness configurationBusiness;
    final private EmailBusiness emailBusiness;

    @Autowired
    public PublicExecutionBusiness(PublicExecutionDAO publicExecutionDAO, ConfigurationBusiness configurationBusiness, EmailBusiness emailBusiness) {
        this.publicExecutionDAO = publicExecutionDAO;
        this.configurationBusiness = configurationBusiness;
        this.emailBusiness = emailBusiness;
    }

    public void create(PublicExecution publicExecution) throws BusinessException {
        try {
            publicExecutionDAO.add(publicExecution);

            String adminsEmailContents = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Dear Administrator,</p>"
                    + "<p>A new user requested to make an execution public</p>"
                    + "<p>Details:</p>"
                    + "<ul>"
                    + "<li>Experience Name: " + publicExecution.getExperienceName() + "</li>"
                    + "<li>Workflows Ids: " + publicExecution.getWorkflowsIds() + "</li>"
                    + "<li>Applications Names: " + publicExecution.getApplicationsNames() + "</li>"
                    + "<li>Applications Versions: " + publicExecution.getApplicationsVersions() + "</li>"
                    + "<li>Status: " + publicExecution.getStatus() + "</li>"
                    + "<li>Author: " + publicExecution.getAuthor() + "</li>"
                    + "<li>Comments: " + publicExecution.getComments() + "</li>"
                    + "</ul>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            for (String supportEmail : configurationBusiness.getSupportEmails()) {
                emailBusiness.sendEmail("[VIP Admin] Execution Public Request", adminsEmailContents,
                        new String[]{supportEmail}, true, publicExecution.getAuthor());
            }
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public PublicExecution get(String experienceName)  throws BusinessException {
        try {
            return publicExecutionDAO.get(experienceName);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public List<PublicExecution> getAll() throws BusinessException {
        try {
            return publicExecutionDAO.getAll();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public boolean exist(String experienceName) throws BusinessException {
        try {
            return publicExecutionDAO.exist(experienceName);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void update(String experienceName, PublicExecution publicExecution) throws BusinessException {
        try {
            publicExecutionDAO.update(experienceName, publicExecution);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void updateStatus(String experienceName, PublicExecution.PublicExecutionStatus newStatus) throws BusinessException {
        try {
            PublicExecution execution = publicExecutionDAO.get(experienceName);

            execution.setStatus(newStatus);
            publicExecutionDAO.update(experienceName, execution);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }
}
