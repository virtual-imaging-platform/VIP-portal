package fr.insalyon.creatis.vip.api.business;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Configure vip (proxy verification)
 *
 * Do it on startup, and then every day (test at each api request)
 */
@Component
public class VipConfigurer implements ApplicationListener<ContextRefreshedEvent>, HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigurationBusiness configurationBusiness;

    private Calendar lastConfiguration = null;

    @Autowired
    public VipConfigurer(ConfigurationBusiness configurationBusiness) {
        this.configurationBusiness = configurationBusiness;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        configureIfNecessary();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        configureIfNecessary();
        return true;
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
        } catch (VipException e) {
            throw new IllegalStateException("Cannot init VIP", e);
        }
    }

    private boolean shouldConfigure() {
        if (lastConfiguration == null) {
            logger.debug("first check : configure VIP");
            return true;
        }
        Calendar now = Calendar.getInstance();
        logger.debug("comparing {} to now {} to check configuration", lastConfiguration, now);
        return now.get(Calendar.DAY_OF_YEAR) != lastConfiguration.get(Calendar.DAY_OF_YEAR);
    }
}
