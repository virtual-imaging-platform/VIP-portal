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
package fr.insalyon.creatis.vip.core.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This is the base class to extends the BaseSpringIT test class (with only the root application context) with
 * the necessary spring-web tests.
 * Spring web uses a MockMvc that mocks a single Spring MVC Dispatcher servlet, so one MockMvc can only be used on
 * the Rest API or the internal api API, not both.
 * There are 2 separate Base Test class for each API, extending this with a custom @ContextHierarchy and with the
 * appropriate servlet path in getServletPath()
 * see {@link BaseInternalApiSpringIT}
 * and {@link fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT} (in vip-api)
 */
abstract public class BaseWebSpringIT extends BaseSpringIT {

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;
    @Autowired
    protected ResourceLoader resourceLoader;
    @Autowired
    protected UserDAO testUserDAO;
    @Autowired
    protected ConfigurationBusiness configurationBusiness;
    @Autowired
    protected GRIDAClient gridaClient;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockMvc = buildMockMvc(getServletPath());
    }

    protected abstract String getServletPath();

    protected MockMvc buildMockMvc(String servletPath) {
        return MockMvcBuilders
                .webAppContextSetup(wac)
                .defaultRequest(MockMvcRequestBuilders.get("/").servletPath("/" + servletPath))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    protected String getResourceAsString(String pathFromClasspath) throws IOException {
        Resource resource = getResourceFromClasspath(pathFromClasspath);
        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    protected Resource getResourceFromClasspath(String pathFromClasspath) {
        return resourceLoader.getResource("classpath:" + pathFromClasspath);
    }

    public WebApplicationContext getWac() {
        return wac;
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public UserDAO getUserDAO() {
        return testUserDAO;
    }

    public ConfigurationBusiness getConfigurationBusiness() {
        return configurationBusiness;
    }

    protected static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
