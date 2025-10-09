package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.api.business.VipConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import fr.insalyon.creatis.vip.core.server.business.Server;

/**
 * Configure the spring mvc DispatcherServlet. Few things to do, as the
 * controllers and dependencies are automatically configured through
 * scanning.
 */
@EnableWebMvc
@Configuration
// Scan all controllers in vip-api (other vip-api beans are already scanned by SpringCoreConfig)
@ComponentScan(
        basePackages = "fr.insalyon.creatis.vip.api",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class)}
)
public class SpringRestApiConfig implements WebMvcConfigurer {

    private final Server server;
    private final VipConfigurer vipConfigurer;

    @Autowired
    public SpringRestApiConfig(Server server, VipConfigurer vipConfigurer) {
        this.server = server;
        this.vipConfigurer = vipConfigurer;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // this is deprecated, but temporary necessary for shanoir
        // that uses requests like /rest/pipelines/
        // Shanoir should get rid of the trailing slash and we should
        // be able to remove this method when spring removes its support
        configurer.setUseTrailingSlashMatch(true);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // necessary in the content negotiation stuff of carmin data
        // this should be the default in Spring 5.3 and may be removed then
        configurer.useRegisteredExtensionsOnly(true);
        configurer.replaceMediaTypes(Collections.emptyMap());
    }

    /*
     to verify that the proxy is still valid each day
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(vipConfigurer);
    }
}
