package fr.insalyon.creatis.vip.core.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(
        // Scan all controller beans, except those in vip-api
        basePackages = "fr.insalyon.creatis.vip",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "fr\\.insalyon\\.creatis\\.vip\\.api\\..*") },
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class) }
)
public class SpringInternalApiConfig implements WebMvcConfigurer {

    private ObjectMapper objectMapper;

    @Autowired
    public SpringInternalApiConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     *
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
                .filter(MappingJackson2HttpMessageConverter.class::isInstance)
                .map(MappingJackson2HttpMessageConverter.class::cast)
                .findFirst()
                .ifPresent(converter -> converter.setObjectMapper(objectMapper));
    }
}
