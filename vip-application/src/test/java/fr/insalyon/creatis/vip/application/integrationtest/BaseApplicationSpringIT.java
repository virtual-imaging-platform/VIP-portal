package fr.insalyon.creatis.vip.application.integrationtest;

import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.application.server.business.TagBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowEngineInstantiator;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;

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
