package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.models.Group;
import fr.insalyon.creatis.vip.core.models.GroupType;

public class ApplicationIT extends BaseSpringIT {

    @Autowired private ApplicationBusiness applicationBusiness;
    @Autowired private EngineBusiness engineBusiness;
    @Autowired private AppVersionBusiness appVersionBusiness;

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

        Application application = new Application("Application1", "test1@test.fr", "test1", "citation1");
        applicationBusiness.add(application);

        AppVersion appVersion = new AppVersion("Application1", "version 0.0", "{}", true);
        appVersionBusiness.add(appVersion);

    }

    @Test
    public void testInitialization() throws VipException {
        // verify number of applications
        Assertions.assertEquals(1, applicationBusiness.getApplications().size(), "Incorrect number of applications");

        // verify properties of one application
        Application application = applicationBusiness.getApplication("Application1");
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
    public void testUpdateApplication() throws VipException {
        Application updatedApplication = new Application("Application1", "test2@test.fr", "test1", "citation1");
        applicationBusiness.update(updatedApplication);

        Assertions.assertEquals("test2@test.fr", applicationBusiness.getApplication("Application1").getOwner(), "Incorrect owner of updated application");
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** remove application ************************************************************ */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveApplication() throws VipException {
        applicationBusiness.remove("Application1");

        Assertions.assertEquals(0, applicationBusiness.getApplications().size(), "Incorrect number of applications");
    }

    @Test
    public void testCatchRemoveNonExistentApplication() throws VipException {
        // DELETE + nonExistent primary key publicationId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        applicationBusiness.remove("NonExistent application");

        Assertions.assertEquals(1, applicationBusiness.getApplications().size(), "Incorrect number of applications");
    }

    /* ********************************************************************************************************************************************** */
    /* ***************************************************************** get citation *************************************************************** */
    /* ********************************************************************************************************************************************** */


    @Test
    public void testGetCitationApplication() throws VipException {
        Assertions.assertEquals("citation1", applicationBusiness.getCitation("Application1"), "Incorrect citation");
    }

    @Test
    public void testCatchGetCitationNonExistentApplication() throws VipException {
        assertNull(applicationBusiness.getCitation("NonExistent application"));
    }

    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** add version *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testAddVersionApplication() throws VipException {
        AppVersion appVersion = new AppVersion("Application1", "version 1.0", "{}", true);
        appVersionBusiness.add(appVersion);
        Assertions.assertEquals(2, appVersionBusiness.getVersions("Application1").size(), "Incorrect versions number");
    }

    /* ********************************************************************************************************************************************** */
    /* **************************************************************** update version ************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdateVersionApplication() throws VipException {
        String descriptor = "{\"some\":\"change\"}";
        AppVersion appVersion = new AppVersion("Application1", "version 0.0", descriptor, true);
        appVersionBusiness.update(appVersion);
        Assertions.assertEquals(descriptor, appVersionBusiness.getVersions("Application1").get(0).getDescriptor(), "Incorrect descriptor update");
    }

    @Test
    public void testUpdateAppVersionWithTags() throws VipException {
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
    public void getApplication() throws VipException {
        Application app = applicationBusiness.getApplication("Application1");

        assertEquals(app.getOwner(), "test1@test.fr");
    }

    @Test
    public void getApplications() throws VipException {
        Application appbis = new Application("test", "testeu");

        applicationBusiness.add(appbis);
        List<Application> apps = applicationBusiness.getApplications();
        assertEquals(2, apps.size());
    }

    @Test
    public void getApplicationsByGroup() throws VipException {
        Application app = applicationBusiness.getApplication("Application1");
        Group group = new Group("test", false, GroupType.APPLICATION);

        groupBusiness.add(group);
        app.setGroups(Arrays.asList(group));
        applicationBusiness.update(app);

        assertEquals(1, applicationBusiness.getApplications(group).size());
    }

    @Test
    public void getApplicationByGroupNotIn() throws VipException {
        Application app = applicationBusiness.getApplication("Application1");
        Group group = new Group("test", false, GroupType.APPLICATION);

        groupBusiness.add(group);
        app.setGroups(Arrays.asList(group));
        applicationBusiness.update(app);

        assertEquals(1, applicationBusiness.getApplications(group).size());  
        app.setGroups(new ArrayList<>());
        applicationBusiness.update(app);

        assertEquals(0, applicationBusiness.getApplications(group).size());  
    }
}
