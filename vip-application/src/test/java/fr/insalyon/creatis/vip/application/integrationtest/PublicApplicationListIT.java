package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;

public class PublicApplicationListIT extends BaseSpringIT {

    @Autowired private ApplicationBusiness applicationBusiness;
    @Autowired private AppVersionBusiness appVersionBusiness;

    @Test
    public void shouldNotIncludePrivateGroupsAndClasses() throws VipException, ApplicationException, GRIDAClientException {
        setAdminContext();
        Group publicGroup = new Group("public group", true, GroupType.getDefault());
        Group privateGroup = new Group("private group", false, GroupType.getDefault());

        Application app = new Application("testApp", "", Arrays.asList(publicGroup));
        AppVersion appVersion = new AppVersion(app.getName(), "", "{}", true);

        groupBusiness.add(publicGroup);
        groupBusiness.add(privateGroup);
        applicationBusiness.add(app);
        appVersionBusiness.add(appVersion);

        List<Application> publicApplications = applicationBusiness.getPublicApplications();
        assertEquals(1, publicApplications.size());

        Application resultApp = publicApplications.get(0);
        assertEquals(app.getName(), resultApp.getName());
        assertEquals(1, resultApp.getGroupsNames().size());
    }
}
