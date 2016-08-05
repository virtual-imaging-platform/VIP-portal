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
package fr.insalyon.creatis.vip.api.rest.itest.config;

import fr.insalyon.creatis.vip.api.SpringWebConfig;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.mockito.Mockito;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by abonnet on 7/26/16.
 *
 * Spring test config that mock bean that interacts with vip outside vip-api
 * Should not be annotated with @Configuration or other to avoid it
 * being package scanned and automatically taken in account.
 */
@Import(SpringWebConfig.class)
public class SpringTestConfig {

    @Bean
    public ApplicationListener<ContextRefreshedEvent> vipInitializer() {
        return (ApplicationListener<ContextRefreshedEvent>)
                Mockito.mock(ApplicationListener.class);
    }

    @Bean
    public UserDAO userDAO() {
        return Mockito.mock(UserDAO.class);
    }

    @Bean
    public ConfigurationBusiness configurationBusiness() {
        return Mockito.mock(ConfigurationBusiness.class);
    }

    @Bean
    public WorkflowBusiness workflowBusiness() {
        return Mockito.mock(WorkflowBusiness.class);
    }

    @Bean
    public ApplicationBusiness applicationBusiness() {
        return Mockito.mock(ApplicationBusiness.class);
    }

    @Bean
    public ClassBusiness classBusiness() {
        return Mockito.mock(ClassBusiness.class);
    }
}
