package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.sma.client.SMAClient;
import fr.insalyon.creatis.sma.client.SMAClientException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SMAClient smaClient;
    private UserDAO userDAO;

    @Autowired
    public EmailBusiness(SMAClient smaClient, UserDAO userDAO) {
        this.smaClient = smaClient;
        this.userDAO = userDAO;
    }

    public void sendEmail(String subject, String content, String[] recipients, boolean direct, String username) throws BusinessException {
        try {
            smaClient.sendEmail(subject, content, recipients, direct, username);
        } catch (SMAClientException ex) {
            logger.error("Error sending {} email to {}", subject, username, ex);
            throw new BusinessException(ex);
        }
    }

    public void sendEmailToAdmins(String subject, String content, boolean direct, String userEmail) throws BusinessException {
        try {
            for (String adminEmail : getAdministratorsEmails()) {
                sendEmail(subject, content, new String[] {adminEmail}, direct, userEmail);
            }
        } catch (DAOException e) {
            logger.error("Error sending {} to admins !", subject, e);
            throw new BusinessException(e);
        }
    }

    /**
     * Gets an array of administrator's e-mails
     */
    public String[] getAdministratorsEmails() throws DAOException {
        List<String> emails = new ArrayList<>();
        for (User admin : userDAO.getAdministrators()) {
            emails.add(admin.getEmail());
        }
        return emails.toArray(new String[0]);
    }
}
