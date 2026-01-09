package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for vip-application integration test that add common properties for easy access
 * The vip-application mocks are configured in {@link SpringApplicationTestConfig}
 */
public class BaseApplicationSpringIT extends BaseSpringIT {

    @Autowired protected WorkflowDAO workflowDAO;
    @Autowired protected OutputDAO outputDAO;
    @Autowired protected InputDAO inputDAO;
    @Autowired protected EngineBusiness engineBusiness;
    @Autowired protected AppVersionBusiness appVersionBusiness;
    @Autowired protected WorkflowEngineInstantiator webServiceEngine;
    @Autowired protected TagBusiness tagBusiness;
    @Autowired protected ApplicationBusiness appBusiness;
    @Autowired protected ResourceBusiness resourceBusiness;
    @Autowired protected WorkflowBusiness workflowBusiness;
    @Autowired protected BoutiquesBusiness boutiquesBusiness;

}
