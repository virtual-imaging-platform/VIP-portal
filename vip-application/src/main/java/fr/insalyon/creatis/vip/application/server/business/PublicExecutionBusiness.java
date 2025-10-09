package fr.insalyon.creatis.vip.application.server.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.application.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.application.server.dao.PublicExecutionDAO;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

@Service
@Transactional
public class PublicExecutionBusiness {
    final private PublicExecutionDAO publicExecutionDAO;
    final private EmailBusiness emailBusiness;

    @Autowired
    public PublicExecutionBusiness(PublicExecutionDAO publicExecutionDAO, EmailBusiness emailBusiness) {
        this.publicExecutionDAO = publicExecutionDAO;
        this.emailBusiness = emailBusiness;
    }

    public void create(PublicExecution publicExecution) throws VipException {
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

            for (String supportEmail : emailBusiness.getAdministratorsEmails()) {
                emailBusiness.sendEmail("[VIP Admin] Execution Public Request", adminsEmailContents,
                        new String[]{supportEmail}, true, publicExecution.getAuthor());
            }
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public PublicExecution get(String experienceName)  throws VipException {
        try {
            return publicExecutionDAO.get(experienceName);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public List<PublicExecution> getAll() throws VipException {
        try {
            return publicExecutionDAO.getAll();
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public boolean exist(String experienceName) throws VipException {
        try {
            return publicExecutionDAO.exist(experienceName);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void update(String experienceName, PublicExecution publicExecution) throws VipException {
        try {
            publicExecutionDAO.update(experienceName, publicExecution);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }

    public void updateStatus(String experienceName, PublicExecution.PublicExecutionStatus newStatus) throws VipException {
        try {
            PublicExecution execution = publicExecutionDAO.get(experienceName);

            execution.setStatus(newStatus);
            publicExecutionDAO.update(experienceName, execution);
        } catch (DAOException e) {
            throw new VipException(e);
        }
    }
}
