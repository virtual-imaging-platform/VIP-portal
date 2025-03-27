package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.sma.client.SMAClient;
import fr.insalyon.creatis.sma.client.SMAClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Server server;
    private SMAClient smaClient;

    @Autowired
    public EmailBusiness(Server server, SMAClient smaClient) {
        this.server = server;
        this.smaClient = smaClient;
    }

    public void sendEmail(String subject, String content, String[] recipients,
                                 boolean direct, String username) throws BusinessException {
        if (server.useSMA()) {
            sendWithSMA(subject, content, recipients, direct, username);
        } else {
            logger.info("SMA disabled, not sending email and logging it");
            logger.info("subject : {}", subject);
            logger.info("recipients : {}", (Object[])recipients);
            logger.info("content : {}", content);
        }
    }

    private void sendWithSMA(String subject, String content, String[] recipients, boolean direct, String username) throws BusinessException {
        try {
            smaClient.sendEmail(subject, content, recipients, direct, username);
        } catch (SMAClientException ex) {
            logger.error("Error sending {} email to {}", subject, username, ex);
            throw new BusinessException(ex);
        }
    }
}
