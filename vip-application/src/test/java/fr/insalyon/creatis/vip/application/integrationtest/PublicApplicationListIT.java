package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

public class PublicApplicationListIT extends BaseApplicationSpringIT {

    @Test
    public void shouldNotIncludePrivateGroupsAndClasses() throws BusinessException, ApplicationException, GRIDAClientException {
        setAdminContext();
        Group publicGroup = new Group("public group", true, GroupType.getDefault());
        Group privateGroup = new Group("private group", false, GroupType.getDefault());

        Application app = new Application("testApp", "", "", Set.of(publicGroup));
        AppVersion appVersion = new AppVersion(app.getName(), "", "{}", true);

        groupBusiness.add(publicGroup);
        groupBusiness.add(privateGroup);
        appBusiness.add(app);
        appVersionBusiness.add(appVersion);

        List<Application> publicApplications = appBusiness.getPublicApplications();
        assertEquals(1, publicApplications.size());

        Application resultApp = publicApplications.get(0);
        assertEquals(app.getName(), resultApp.getName());
        assertEquals(1, resultApp.getGroupsNames().size());
    }
}
