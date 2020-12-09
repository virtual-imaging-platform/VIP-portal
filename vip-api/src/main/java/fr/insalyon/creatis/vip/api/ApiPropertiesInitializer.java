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

import fr.insalyon.creatis.vip.api.model.Module;
import fr.insalyon.creatis.vip.api.model.SupportedTransferProtocol;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

import static fr.insalyon.creatis.vip.api.CarminProperties.*;
import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

/**
 * Add an additional "vip-api.conf" property file to spring environment
 * This file must be in the vip configuration folder (alongside vip.conf)
 *
 * Created by abonnet on 5/7/18.
 */
@Component
public class ApiPropertiesInitializer {

    private ConfigurableEnvironment env;

    @Autowired
    public ApiPropertiesInitializer(
            Resource vipConfigFolder,
            ConfigurableEnvironment environment) throws IOException {
        this.env = environment;
        Resource configFileResource = new FileSystemResource(
                vipConfigFolder.getFile().toPath().resolve("vip-api.conf"));
        env.getPropertySources().addLast(
                new ResourcePropertySource(configFileResource)
        );
    }

    @PostConstruct
    public void init() throws IOException {
        verifyProperties();
    }

    private void verifyProperties() {

        verifyPropertyNotNull(CORS_AUTHORIZED_DOMAINS, String[].class);
        verifyPropertyNotNull(PLATFORM_NAME, String.class);
        verifyPropertyNotNull(PLATFORM_DESCRIPTION, String.class);
        verifyPropertyNotNull(PLATFORM_EMAIL, String.class);
        verifyPropertyNotNull(DEFAULT_LIMIT_LIST_EXECUTION, Long.class);
        verifyPropertyNotNull(SUPPORTED_API_VERSION, String.class);
        verifyPropertyNotNull(APIKEY_HEADER_NAME, String.class);
        verifyPropertyNotNull(APIKEY_GENERATE_NEW_EACH_TIME, Boolean.class);
        verifyPropertyNotNull(API_DIRECTORY_MIME_TYPE, String.class);
        verifyPropertyNotNull(API_DEFAULT_MIME_TYPE, String.class);
        verifyPropertyNotNull(API_DOWNLOAD_RETRY_IN_SECONDS, Integer.class);
        verifyPropertyNotNull(API_DOWNLOAD_TIMEOUT_IN_SECONDS, Integer.class);
        verifyPropertyNotNull(API_DATA_TRANSFERT_MAX_SIZE, Long.class);

        // due to arrays and generics, this verification aren't easy to factorize
        Assert.notEmpty(env.getProperty(SUPPORTED_TRANSFER_PROTOCOLS, SupportedTransferProtocol[].class),
                SUPPORTED_TRANSFER_PROTOCOLS + " required in api conf file");
        Assert.notEmpty(env.getProperty(SUPPORTED_MODULES, Module[].class),
                SUPPORTED_MODULES + " required in api conf file");
        Assert.isInstanceOf(String[].class, env.getProperty(UNSUPPORTED_METHODS, String[].class),
                UNSUPPORTED_METHODS + " required in api conf file");
        Assert.isInstanceOf(String[].class, env.getProperty(API_PIPELINE_WHITE_LIST, String[].class),
                API_PIPELINE_WHITE_LIST + " required in api conf file");

    }

    private void verifyPropertyNotNull(String propertyKey, Class<?> targetType) {
        Assert.notNull(env.getProperty(propertyKey, targetType),
                propertyKey + " required in api conf file");
    }
}
