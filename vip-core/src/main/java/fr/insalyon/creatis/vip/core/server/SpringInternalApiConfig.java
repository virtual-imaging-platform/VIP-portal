package fr.insalyon.creatis.vip.core.server;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(
        // Scan all controller beans, except those in vip-api
        basePackages = { "fr.insalyon.creatis.vip", "org.springdoc" },
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "fr\\.insalyon\\.creatis\\.vip\\.api\\..*") },
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class) }
)
public class SpringInternalApiConfig {

    @Bean
    public SpringDocConfigProperties springDocConfigProperties() {
        SpringDocConfigProperties properties = new SpringDocConfigProperties();
        ApiDocs apiDocs = new ApiDocs();
        // the `/internal/` need to be specified
        // for swagger-ui config generation
        apiDocs.setPath("/internal/v3/api-docs");

        properties.setApiDocs(apiDocs);
        return properties;
    }
}
