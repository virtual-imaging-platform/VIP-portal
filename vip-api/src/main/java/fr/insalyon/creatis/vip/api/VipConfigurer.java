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

import fr.insalyon.creatis.vip.core.server.business.*;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * Used to configure vip on startup and daily
 *
 * On startup, test database
 * Daily, renew the grida proxy
 *
 * Created by abonnet on 7/26/16.
 */
@Component
public class VipConfigurer implements ApplicationListener<ContextRefreshedEvent> {

    public static final Logger logger = Logger.getLogger(VipConfigurer.class);

    @Autowired
    private ConfigurationBusiness configurationBusiness;

    private Calendar lastConfiguration = null;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Init VIP : initialize database");
        PlatformConnection.getInstance();
        configureIfNecessary();
    }

    public synchronized void configureIfNecessary() {
        if (!shouldConfigure()) {
            logger.debug("no need to reconfigure");
            return;
        }
        try {
            logger.info("New VIP configuration necessary");
            configurationBusiness.configure();
            lastConfiguration = Calendar.getInstance();
        } catch (BusinessException e) {
            throw new RuntimeException("Cannot init VIP", e);
        }
    }

    private boolean shouldConfigure() {
        if (lastConfiguration == null) {
            logger.debug("first check : configure VIP");
            return true;
        }
        Calendar now = Calendar.getInstance();
        logger.debug("comparing {" + lastConfiguration +"} to now {" + now + "} to check configuration");
        return now.get(Calendar.DAY_OF_YEAR) != lastConfiguration.get(Calendar.DAY_OF_YEAR);
    }
}
