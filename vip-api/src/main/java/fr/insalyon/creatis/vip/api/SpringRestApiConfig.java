package fr.insalyon.creatis.vip.api;

import java.util.Collections;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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



    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        // necessary in the content negotiation stuff of carmin data
        // this should be the default in Spring 5.3 and may be removed then
        configurer.useRegisteredExtensionsOnly(true);
        configurer.replaceMediaTypes(Collections.emptyMap());
    }
}
