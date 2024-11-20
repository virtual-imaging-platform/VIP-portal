package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationIT extends BaseSpringIT {
    @Autowired
    private ApplicationBusiness applicationBusiness;
    @Autowired
    private ClassBusiness classBusiness;
    @Autowired
    private EngineBusiness engineBusiness;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // group test and user test creation
        group1 = new Group("group1", true, GroupType.getDefault());
        configurationBusiness.addGroup(group1);
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

        AppClass appClass = new AppClass("class1", engines, groups);
        classBusiness.addClass(appClass);
        applicationClasses = new ArrayList<>();
        applicationClasses.add("class1");

        Application application = new Application("Application1", applicationClasses, "test1@test.fr", "test1", "citation1");
        applicationBusiness.add(application);

        AppVersion appVersion = new AppVersion("Application1", "version 0.0", "lfn", "jsonLfn", true, true);
        applicationBusiness.addVersion(appVersion);

    }

    @Test
    public void testInitialization() throws BusinessException {
        // verify number of applications
        Assertions.assertEquals(1, applicationBusiness.getApplications().size(), "Incorrect number of applications");

        // verify properties of one application
        Application application = applicationBusiness.getApplication("Application1");
        Assertions.assertEquals("citation1", application.getCitation(), "Incorrect citation of application");
        Assertions.assertEquals("Application1", application.getName(), "Incorrect name of application");
        Assertions.assertEquals("test1@test.fr", application.getOwner(), "Incorrect owner of application");
        Assertions.assertNull(application.getFullName(), "getApplication should not fill fullname");
        Assertions.assertEquals("class1", application.getApplicationClasses().get(0), "Incorrect class of application");
        Assertions.assertNull(application.getApplicationGroups(), "getApplication should not fill applicationGroups");
        Assertions.assertEquals(1, applicationBusiness.getVersions("Application1").size(), "Incorrect versions number");

        // verify that there is no application in the first class
        Assertions.assertEquals(1, applicationBusiness.getApplications("class1").size(), "Incorrect number of application");

    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** update application ************************************************************ */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdateApplication() throws BusinessException {
        Application updatedApplication = new Application("Application1", applicationClasses, "test2@test.fr", "test1", "citation1");
        applicationBusiness.update(updatedApplication);

        Assertions.assertEquals("test2@test.fr", applicationBusiness.getApplication("Application1").getOwner(), "Incorrect owner of updated application");
    }

    // TODO : corriger
    @Test
    public void testCatchUpdateNonExistentApplication() {
        Application updatedApplication = new Application("NonExistent Application", applicationClasses, "test2@test.fr", "test1", "citation1");

        Exception exception = assertThrows(
                BusinessException.class, () ->
                        applicationBusiness.update(updatedApplication)
        );

        // UPDATE + nonExistent foreign key name for VIPAPPLICATIONCLASSES => violation
        assertTrue(StringUtils.contains(exception.getMessage(), "JdbcSQLException: Referential integrity constraint violation"));


    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************** remove application ************************************************************ */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemoveApplication() throws BusinessException {
        applicationBusiness.remove("Application1");

        Assertions.assertEquals(0, applicationBusiness.getApplications().size(), "Incorrect number of applications");
    }

    @Test
    public void testCatchRemoveNonExistentApplication() throws BusinessException {
        // DELETE + nonExistent primary key publicationId => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        applicationBusiness.remove("NonExistent application");

        Assertions.assertEquals(1, applicationBusiness.getApplications().size(), "Incorrect number of applications");
    }

    /* ********************************************************************************************************************************************** */
    /* ***************************************************************** get citation *************************************************************** */
    /* ********************************************************************************************************************************************** */


    @Test
    public void testGetCitationApplication() throws BusinessException {
        Assertions.assertEquals("citation1", applicationBusiness.getCitation("Application1"), "Incorrect citation");
    }

    @Test
    public void testCatchGetCitationNonExistentApplication() {
        Exception exception = assertThrows(
                BusinessException.class, () ->
                        applicationBusiness.getCitation("NonExistent application")
        );

        assertTrue(StringUtils.contains(exception.getMessage(), "jdbc.JdbcSQLException: No data is available"));
    }

    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** add version *************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testAddVersionApplication() throws BusinessException {
        AppVersion appVersion = new AppVersion("Application1", "version 1.0", "lfn", "jsonLfn", true, true);
        applicationBusiness.addVersion(appVersion);
        Assertions.assertEquals(2, applicationBusiness.getVersions("Application1").size(), "Incorrect versions number");
    }

    /* ********************************************************************************************************************************************** */
    /* **************************************************************** update version ************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdateVersionApplication() throws BusinessException {

        AppVersion appVersion = new AppVersion("Application1", "version 0.0", "lfn updated", "jsonLfn", true, true);
        applicationBusiness.updateVersion(appVersion);
        Assertions.assertEquals("lfn updated", applicationBusiness.getVersions("Application1").get(0).getLfn(), "Incorrect lfn updated");
    }


    /* ********************************************************************************************************************************************** */
    /* *************************************************************** get applications ************************************************************* */
    /* ********************************************************************************************************************************************** */


    @Test
    public void testGetApplications() throws BusinessException {
        List<String[]> applications = applicationBusiness.getApplications("class1");
        Assertions.assertEquals(1, applications.size(), "Incorrect applications number");
    }


    @Test
    public void testCatchGetApplicationsNonExistentClass() throws BusinessException {
        List<String[]> applications = applicationBusiness.getApplications("nonExistent class");
        Assertions.assertEquals(0, applications.size(), "Incorrect applications number");
    }

}
