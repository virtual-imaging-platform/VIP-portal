package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

public class PublicApplicationListIT extends BaseSpringIT {

    @Autowired private ApplicationBusiness applicationBusiness;
    @Autowired private AppVersionBusiness appVersionBusiness;

    @Test
    public void shouldNotIncludePrivateGroupsAndClasses() throws BusinessException, ApplicationException {
        Group publicGroup = new Group("public group", true, GroupType.getDefault());
        Group privateGroup = new Group("private group", false, GroupType.getDefault());

        Application app = new Application("testApp", "", Arrays.asList(publicGroup.getName()), true);
        AppVersion appVersion = new AppVersion(app.getName(), "", "{}", true);

        groupBusiness.add(publicGroup);
        groupBusiness.add(privateGroup);
        applicationBusiness.add(app);
        appVersionBusiness.add(appVersion);

        List<Application> publicApplications = applicationBusiness.getPublicApplicationsWithGroups();
        assertEquals(1, publicApplications.size());

        Application resultApp = publicApplications.get(0);
        assertEquals(app.getName(), resultApp.getName());
        assertEquals(1, resultApp.getApplicationGroups().size());
    }
}
