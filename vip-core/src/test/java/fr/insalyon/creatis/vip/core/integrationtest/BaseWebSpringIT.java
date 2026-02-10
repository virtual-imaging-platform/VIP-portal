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
    protected UserDAO userDAO;
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
        return userDAO;
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
