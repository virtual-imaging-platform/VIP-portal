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
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.api.business.VipConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.servlet.config.annotation.*;

import java.io.IOException;
import java.util.Collections;

import static fr.insalyon.creatis.vip.api.CarminProperties.CORS_AUTHORIZED_DOMAINS;

/**
 * Configure the spring mvc DispatcherServlet. Few things to do, as the
 * controllers and dependencies are automatically configured through
 * scanning.
 *
 * Created by abonnet on 7/13/16.
 */
@EnableWebMvc
@Configuration
public class SpringWebConfig implements WebMvcConfigurer {

    private Environment env;
    private VipConfigurer vipConfigurer;

    @Autowired
    public SpringWebConfig(Environment env, VipConfigurer vipConfigurer) {
        this.env = env;
        this.vipConfigurer = vipConfigurer;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Otherwise all that follow a dot in an URL is considered an extension and removed
        // It's a problem for URL like "/pipelines/gate/3.2
        // The below will become the default values in Spring 5.3
        // Safe to use in 5.2 as long as disabling pattern match
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // necessary in the content negotiation stuff of carmin data
        // this should be the default in Spring 5.3 and may be removed then
        configurer.useRegisteredExtensionsOnly(true);
        configurer.replaceMediaTypes(Collections.emptyMap());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
            .allowedOrigins(env.getRequiredProperty(CORS_AUTHORIZED_DOMAINS, String[].class));
    }

    /*
     to verify that the proxy ist still valid each day
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(vipConfigurer);
    }

}
