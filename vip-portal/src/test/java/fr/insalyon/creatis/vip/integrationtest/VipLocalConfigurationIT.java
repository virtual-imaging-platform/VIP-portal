package fr.insalyon.creatis.vip.integrationtest;

import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.server.SpringCoreConfig;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitWebConfig(value = SpringCoreConfig.class)
// launch all spring environment for testing, also take test bean though automatic package scan
@ActiveProfiles({"local", "spring-config-server", "local-db"}) // to take random h2 database and not the test h2 jndi one
@Transactional
@TestPropertySource(properties = "vipConfigFolder:/home/abonnet/Workspace/Testing/VIP/LocalInstance1")
public class VipLocalConfigurationIT {

    @Autowired
    private ApplicationBusiness applicationBusiness;

    @Test
    public void testConfig() throws BusinessException {
        Assertions.assertEquals(0, applicationBusiness.getApplications().size());
    }
}
