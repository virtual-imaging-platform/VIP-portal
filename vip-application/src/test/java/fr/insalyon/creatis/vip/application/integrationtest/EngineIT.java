package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EngineIT extends BaseSpringIT {

    @Autowired
    private EngineBusiness engineBusiness;

    @Test
    public void shouldAddEngine() throws BusinessException {
        Assertions.assertTrue(engineBusiness.get().isEmpty());
        String engineName = "test engine";
        String engineEndpoint = "test endpoint";
        String engineStatus = "enabled";
        Engine engine = new Engine(engineName, engineEndpoint, engineStatus);

        // add engine
        engineBusiness.add(engine);

        // verify
        Assertions.assertEquals(1, engineBusiness.get().size());
        engine = engineBusiness.get().get(0);
        Assertions.assertEquals(engineName, engine.getName());
        Assertions.assertEquals(engineEndpoint, engine.getEndpoint());
        Assertions.assertEquals(engineStatus, engine.getStatus());
    }
}
