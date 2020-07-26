package fr.insalyon.creatis.vip.portal.local;

import fr.insalyon.creatis.sma.client.SMAClient;
import fr.insalyon.creatis.sma.client.SMAClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("local")
public class SmaClientLocal extends SMAClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public SmaClientLocal() {
        super(null, 0);
    }

    @Override
    public String sendEmail(String subject, String contents, String[] recipients, boolean direct, String username) throws SMAClientException {
        logger.info("sending {} mail from {} to {} ", subject, username, recipients);
        logger.info("Mail content : {} ", contents);
        return "localSMAOperation";
    }
}
