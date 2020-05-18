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
package fr.insalyon.creatis.vip.api.rest.itest;

import fr.insalyon.creatis.vip.api.*;
import fr.insalyon.creatis.vip.api.bean.Module;
import fr.insalyon.creatis.vip.api.business.ApiException;
import fr.insalyon.creatis.vip.api.rest.controller.PlatformController;
import fr.insalyon.creatis.vip.api.rest.model.SupportedTransferProtocol;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import org.junit.*;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static fr.insalyon.creatis.vip.api.CarminProperties.*;

/**
 * Created by abonnet on 7/21/16.
 *
 * Test the the global spring configuration
 *
 * The less mock should be used to be as close as possible to the production
 * environment.
 *
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(initializers = ApiPropertiesInitializer.class)
public class DefaultSpringConfigurationIT {

    // Need to override vipConfigurer that operate on the database
    @Configuration
    @Import(SpringWebConfig.class)
    static class TestConfig {
        @Bean
        public VipConfigurer vipConfigurer() {
            return Mockito.mock(VipConfigurer.class);
        }

        @Bean
        public WorkflowBusiness workflowBusiness() {
            return Mockito.mock(WorkflowBusiness.class);
        }
    }

    @Autowired
    private Environment env;

    @Autowired
    private PlatformController platformController;

    @ClassRule
    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @BeforeClass
    public static void setup() throws URISyntaxException {
        String fakeHomePath = Paths.get(ClassLoader.getSystemResource("TestHome").toURI())
                .toAbsolutePath().toString();
        environmentVariables.set("HOME", fakeHomePath);
    }

    @Test
    public void propertiesShouldBePresent() throws ApiException {
        // test that the platform properties generation does not throw any exception
        Assert.notNull(platformController.getPlatformProperties());
    }
}
