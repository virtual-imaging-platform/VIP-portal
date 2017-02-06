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
import fr.insalyon.creatis.vip.api.rest.controller.PlatformController;
import fr.insalyon.creatis.vip.api.rest.model.SupportedTransferProtocol;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

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
@ContextConfiguration
public class SpringConfigurationIT {

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

    @Test
    public void propertiesShouldBePresent() {
        Assert.notNull(env.getProperty(PLATFORM_NAME));
        Assert.notNull(env.getProperty(PLATFORM_DESCRIPTION));
        Assert.notNull(env.getProperty(PLATFORM_EMAIL));
        Assert.notEmpty(env.getProperty(SUPPORTED_TRANSFER_PROTOCOLS, SupportedTransferProtocol[].class));
        Assert.notEmpty(env.getProperty(SUPPORTED_MODULES, Module[].class));
        Assert.notNull(env.getProperty(DEFAULT_LIMIT_LIST_EXECUTION, Long.class));
        Assert.isInstanceOf(String[].class, env.getProperty(UNSUPPORTED_METHODS, String[].class));
        Assert.notNull(env.getProperty(SUPPORTED_API_VERSION));
        Assert.notNull(env.getProperty(IS_KILL_EXECUTION_SUPPORTED, Boolean.class));
        Assert.notEmpty(env.getProperty(PLATFORM_ERROR_CODES_AND_MESSAGES, String[].class));
        // test platform properties generation
        Assert.notNull(platformController.getPlatformProperties());
        Assert.notNull(env.getProperty(API_URI_PREFIX));
        Assert.notNull(env.getProperty(API_DEFAULT_MIME_TYPE));
        Assert.notNull(env.getProperty(API_DIRECTORY_MIME_TYPE));
        Assert.notNull(env.getProperty(API_DOWNLOAD_TIMEOUT_IN_SECONDS, Integer.class));
        Assert.notNull(env.getProperty(API_DOWNLOAD_RETRY_IN_SECONDS, Integer.class));
    }
}
