package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ApplicationIT extends BaseApplicationSpringIT {

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        setAdminContext();

        // group test and user test creation
        group1 = new Group("group1", true, GroupType.getDefault());
        groupBusiness.add(group1);
        List<String> groups = new ArrayList<>();
        groups.add("group1");
        createUserInGroup("test1@test.fr", "suffix1", "group1");
        createUserInGroup("test2@test.fr", "suffix2", "group1");

        // engine test creation
        String engineName = "test engine";
        String engineEndpoint = "test endpoint";
        String engineStatus = "enabled";
        Engine engine = new Engine(engineName, engineEndpoint, engineStatus);
        List<String> engines = new ArrayList<>();
        engines.add("test engine");
        engineBusiness.add(engine);

        Application application = new Application("Application1", "test1@test.fr", "citation1", "test1");
        appBusiness.add(application);

        AppVersion appVersion = new AppVersion("Application1", "version 0.0", "{}", true);
        appVersionBusiness.add(appVersion);

    }

    @Test
    public void testInitialization() throws BusinessException {
        // verify number of applications
        Assertions.assertEquals(1, appBusiness.getApplications().size(), "Incorrect number of applications");

        // verify properties of one application
        Application application = appBusiness.getApplication("Application1");
        Assertions.assertEquals("citation1", application.getCitation(), "Incorrect citation of application");
        Assertions.assertEquals("Application1", application.getName(), "Incorrect name of application");
        Assertions.assertEquals("test1@test.fr", application.getOwner(), "Incorrect owner of application");
        Assertions.assertNull(application.getFullName(), "getApplication should not fill fullname");
        Assertions.assertTrue(application.getGroupsNames().isEmpty(), "getApplication should not fill applicationGroups");
        Assertions.assertEquals(1, appVersionBusiness.getVersions("Application1").size(), "Incorrect versions number");

    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** update application ************************************************************ */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdateApplication() throws BusinessException {
        Application updatedApplication = new Application("Application1", "test2@test.fr", "test1", "citation1");
        appBusiness.update(updatedApplication);

        Assertions.assertEquals("test2@test.fr", appBusiness.getApplication("Application1").getOwner(), "Incorrect owner of updated application");
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** remove application ************************************************************ */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveApplication() throws BusinessException {
        appBusiness.remove("Application1");

        Assertions.assertEquals(0, appBusiness.getApplications().size(), "Incorrect number of applications");
    }

    @Test
    public void testCatchRemoveNonExistentApplication() throws BusinessException {
        // DELETE + nonExistent primary key publicationId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        appBusiness.remove("NonExistent application");

        Assertions.assertEquals(1, appBusiness.getApplications().size(), "Incorrect number of applications");
    }

    /* ********************************************************************************************************************************************** */
    /* ***************************************************************** get citation *************************************************************** */
    /* ********************************************************************************************************************************************** */


    @Test
    public void testGetCitationApplication() throws BusinessException {
        Assertions.assertEquals("citation1", appBusiness.getCitation("Application1"), "Incorrect citation");
    }

    @Test
    public void testCatchGetCitationNonExistentApplication() throws BusinessException {
        assertNull(appBusiness.getCitation("NonExistent application"));
    }

    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** add version *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testAddVersionApplication() throws BusinessException {
        AppVersion appVersion = new AppVersion("Application1", "version 1.0", "{}", true);
        appVersionBusiness.add(appVersion);
        Assertions.assertEquals(2, appVersionBusiness.getVersions("Application1").size(), "Incorrect versions number");
    }

    /* ********************************************************************************************************************************************** */
    /* **************************************************************** update version ************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdateVersionApplication() throws BusinessException {
        String descriptor = "{\"some\":\"change\"}";
        AppVersion appVersion = new AppVersion("Application1", "version 0.0", descriptor, true);
        appVersionBusiness.update(appVersion);
        Assertions.assertEquals(descriptor, appVersionBusiness.getVersions("Application1").get(0).getDescriptor(), "Incorrect descriptor update");
    }

    @Test
    public void testUpdateAppVersionWithTags() throws BusinessException {
        AppVersion appVersion = new AppVersion("Application1", "version 0.0", "{}", true);
        Tag tagA = new Tag("a", "x", Tag.ValueType.STRING, appVersion, false, false);
        Tag tagB = new Tag("b", "x", Tag.ValueType.STRING, appVersion, false, false);
        appVersion.setTags(List.of(tagA, tagB));
        appVersionBusiness.update(appVersion);

        assertEquals(List.of(tagA, tagB), appVersionBusiness.getVersions("Application1").get(0).getTags());
        // now try to remove a tag, update one and add a two new ones
        tagB.setValue("y");
        tagB.setBoutiques(true);
        tagB.setVisible(true);
        Tag tagC = new Tag("c", "x", Tag.ValueType.STRING, appVersion, false, false);
        Tag tagD = new Tag("d", "x", Tag.ValueType.STRING, appVersion, false, false);
        appVersion.setTags(List.of(tagB, tagC, tagD));
        appVersionBusiness.update(appVersion);

        assertEquals(List.of(tagB, tagC, tagD), appVersionBusiness.getVersions("Application1").get(0).getTags());
    }

    @Test 
    public void getApplication() throws BusinessException {
        Application app = appBusiness.getApplication("Application1");

        assertEquals(app.getOwner(), "test1@test.fr");
    }

    @Test
    public void getApplications() throws BusinessException {
        Application appbis = new Application("test", "testeu");

        appBusiness.add(appbis);
        List<Application> apps = appBusiness.getApplications();
        assertEquals(2, apps.size());
    }

    @Test
    public void getApplicationsByGroup() throws BusinessException {
        Application app = appBusiness.getApplication("Application1");
        Group group = new Group("test", false, GroupType.APPLICATION);

        groupBusiness.add(group);
        app.setGroups(Set.of(group));
        appBusiness.update(app);

        assertEquals(1, appBusiness.getApplications(group).size());
    }

    @Test
    public void getApplicationByGroupNotIn() throws BusinessException {
        Application app = appBusiness.getApplication("Application1");
        Group group = new Group("test", false, GroupType.APPLICATION);

        groupBusiness.add(group);
        app.setGroups(Set.of(group));
        appBusiness.update(app);

        assertEquals(1, appBusiness.getApplications(group).size());
        app.setGroups(new HashSet<>());
        appBusiness.update(app);

        assertEquals(0, appBusiness.getApplications(group).size());
    }
}
