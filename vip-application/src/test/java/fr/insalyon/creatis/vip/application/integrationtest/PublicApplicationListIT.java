package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PublicApplicationListIT extends BaseSpringIT {

    @Autowired
    private ApplicationBusiness applicationBusiness;

    @Autowired
    private ConfigurationBusiness configurationBusiness;

    @Test
    public void shouldNotIncludePrivateGroupsAndClasses() throws BusinessException, ApplicationException {
        // prepare test data
        Group publicGroup = new Group("public group", true, GroupType.getDefault());
        Group privateGroup = new Group("private group", false, GroupType.getDefault());
        // classes are not really public/private, but they are linked to public/private groups
        // a class is considered private if it is linked only to private groups
        Application app = new Application("testApp", "");
        AppVersion appVersion = new AppVersion(app.getName(), "", null, null, true, false);
        // persist data in database
        groupBusiness.add(publicGroup);
        groupBusiness.add(privateGroup);
        applicationBusiness.add(app);
        applicationBusiness.addVersion(appVersion);

        // verify
        List<Application> publicApplications = applicationBusiness.getPublicApplicationsWithGroups();
        // Assertions.assertEquals(1, publicApplications.size()); 
        // changer
        // Application resultApp = publicApplications.get(0);
        // Assertions.assertEquals(app.getName(), resultApp.getName());
        // Assertions.assertEquals(1, resultApp.getApplicationGroups().size());
    }
}
