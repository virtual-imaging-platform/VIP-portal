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
package fr.insalyon.creatis.vip.api.rest.config;

import fr.insalyon.creatis.vip.api.*;
import fr.insalyon.creatis.vip.api.business.VipConfigurer;
import fr.insalyon.creatis.vip.application.server.business.*;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import fr.insalyon.creatis.vip.datamanager.server.business.*;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;

import java.sql.Connection;
import java.util.function.*;

import static org.mockito.ArgumentMatchers.any;

/**
 * Created by abonnet on 7/26/16.
 *
 * Spring test config that mock bean that interacts with vip outside vip-api
 * Should not be annotated with @Configuration or other to avoid it
 * being package scanned and automatically taken in account.
 */
@Import(SpringWebConfig.class)
@Configuration
public class SpringTestConfig {

    @Bean
    public VipConfigurer vipConfigurer() {
        VipConfigurer mock = Mockito.mock(VipConfigurer.class);
        Mockito.when(mock.preHandle(any(), any(), any())).thenReturn(true);
        return mock;
    }

    @Bean
    public Supplier<Connection> connectionSupplier() {
        return Mockito.mock(Supplier.class);
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

    @Bean
    public TransferPoolBusiness transferPoolBusiness() {
        return Mockito.mock(TransferPoolBusiness.class);
    }

    @Bean
    public SimulationBusiness simulationBusiness() {
        return Mockito.mock(SimulationBusiness.class);
    }

    @Bean
    public LFCBusiness lfcBusiness() {
        return Mockito.mock(LFCBusiness.class);
    }

    @Bean
    public LFCPermissionBusiness lfcPermissionBusiness() {
        return Mockito.mock(LFCPermissionBusiness.class);
    }
}
