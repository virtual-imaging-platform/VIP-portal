/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.api.business.PipelineBusiness;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.datamanager.server.business.TransferPoolBusiness;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by abonnet on 7/13/16.
 */
@EnableWebMvc
@ComponentScan
@PropertySource("classpath:carmin.properties")
public class SpringWebConfig {

    public static final Logger logger = Logger.getLogger(SpringWebConfig.class);



    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = BeanDefinition.SCOPE_PROTOTYPE)
    public UserDAO userDAO() {
        try {
            return CoreDAOFactory.getDAOFactory().getUserDAO();
        } catch (DAOException e) {
            logger.error("error creating user dao bean", e);
            throw new RuntimeException("Cannot create user dao", e);
        }
    }

    @Bean
    public WorkflowBusiness workflowBusiness() {
        return new WorkflowBusiness();
    }

    @Bean
    public ApplicationBusiness applicationBusiness() {
        return new ApplicationBusiness();
    }

    @Bean
    public ClassBusiness classBusiness() {
        return  new ClassBusiness();
    }

    @Bean
    public SimulationBusiness simulationBusiness() {
        return new SimulationBusiness();
    }

    @Bean
    public ConfigurationBusiness configurationBusiness() {
        return new ConfigurationBusiness();
    }

    @Bean
    public TransferPoolBusiness transferPoolBusiness() {
        return new TransferPoolBusiness();
    }
}
