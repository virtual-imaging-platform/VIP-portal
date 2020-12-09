package fr.insalyon.creatis.vip.social.integrationtest;


import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.core.integrationtest.ServerMockConfig;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.social.server.business.MessageBusiness;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SocialSpringConfigIT extends BaseSpringIT {

    @Autowired
    public MessageBusiness messageBusiness;

    @Test
    public void testMessageIsSend() throws GRIDAClientException, BusinessException {
        assertRowsNbInTable("VIPSocialMessage", 0);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 0);
        assertRowsNbInTable("VIPSocialGroupMessage", 0);
        createUser("test1@test.fr", "1");
        createUser("test2@test.fr", "2");
        createUser("test3@test.fr", "3");
        messageBusiness.sendMessage(
                configurationBusiness.getUser(ServerMockConfig.TEST_ADMIN_EMAIL),
                new String[]{"test1@test.fr", "test3@test.fr"},
                "test subject", "test message");
        assertRowsNbInTable("VIPSocialMessage", 1);
        assertRowsNbInTable("VIPSocialMessageSenderReceiver", 2);
        assertRowsNbInTable("VIPSocialGroupMessage", 0);
    }

    
}
