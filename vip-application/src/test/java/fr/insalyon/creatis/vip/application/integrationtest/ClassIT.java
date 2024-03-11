package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.server.business.ClassBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// testing framework must recreate a MessageBusiness after each test method
public class ClassIT extends BaseSpringIT {
    @Autowired
    public ClassBusiness classBusiness;
    @Autowired
    public EngineBusiness engineBusiness;

    private List<String> applicationGroups = new ArrayList<>();
    private List<String> engines = new ArrayList<>();

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        Group group1 = new Group("group1", true, true, true);
        applicationGroups.add("group1");
        configurationBusiness.addGroup(group1);

        Engine engine = new Engine("test engine", "test endpoint", "enabled");
        engines.add("test engine");
        engineBusiness.add(engine);

        AppClass appClass = new AppClass("class1", engines, applicationGroups);
        classBusiness.addClass(appClass);
    }

    @Test
    public void testInitialization() throws BusinessException {
        Assertions.assertEquals(classBusiness.getClasses().size(), 1, "Incorrect number of classes");
        Assertions.assertEquals(classBusiness.getClassesName().get(0), "class1", "Incorrect name of class");
    }


    /* ********************************************************************************************************************************************** */
    /* ******************************************************************* create and add class **************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testAddClass() throws BusinessException {
        // With all parameters
        AppClass appClass1 = new AppClass("class2", engines, applicationGroups);
        classBusiness.addClass(appClass1);

        Assertions.assertEquals(classBusiness.getClasses().size(), 2, "Incorrect number of classes");
        Assertions.assertEquals(classBusiness.getClassesName().get(1), "class2", "Incorrect name of class");
    }

    @Test
    public void testAddExistingClass() throws BusinessException {

        AppClass appClass = new AppClass("class1", engines, applicationGroups);

        Exception exception = assertThrows(
                BusinessException.class, () ->
                        classBusiness.addClass(appClass)
        );

        Assertions.assertTrue(StringUtils.contains(exception.getMessage(), "A class named \"class1\" already exists"));
    }

    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** get class ***************************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetClass() throws BusinessException {
        classBusiness.getClass("class1");
    }

    @Test
    public void testCatchGetNonExistentClass() throws BusinessException {
        // SELECT + nonExistent primary key className => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        assertNull(classBusiness.getClass("nonExistent class"));
    }


    /* ********************************************************************************************************************************************** */
    /* **************************************************************** remove class **************************************************************** */
    /* ********************************************************************************************************************************************** */
    @Test
    public void testRemoveClass() throws BusinessException {
        classBusiness.removeClass("class1");

        // SELECT + nonExistent primary key className => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be selected
        assertNull(classBusiness.getClass("class1"));
        Assertions.assertEquals(classBusiness.getClasses().size(), 0, "Incorrect number of classes");
    }

    @Test
    public void testCatchNonExistentClassRemoveClass() throws BusinessException {
        // DELETE + nonExistent primary key className => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        classBusiness.removeClass("nonExistent class");
        Assertions.assertEquals(classBusiness.getClasses().size(), 1, "Incorrect number of classes");
    }

    /* ********************************************************************************************************************************************** */
    /* ****************************************************************** update class ************************************************************** */
    /* ********************************************************************************************************************************************** */


    @Test
    public void testUpdateClass() throws BusinessException {
        List<String> applicationGroups = new ArrayList<>();
        applicationGroups.add("group1");

        Engine engine = new Engine("test engine 2", "test endpoint", "enabled");
        List<String> engines = new ArrayList<>();
        engines.add("test engine 2");
        engineBusiness.add(engine);

        AppClass appClass = new AppClass("class1", engines, applicationGroups);
        classBusiness.updateClass(appClass);

        Assertions.assertEquals(classBusiness.getClasses().size(), 1, "Incorrect number of classes");
        Assertions.assertEquals(classBusiness.getClasses().get(0).getEngines().get(0), "test engine 2", "Incorrect number of classes");
    }

    @Test
    public void testCatchUpdateClass() throws BusinessException {
        List<String> applicationGroups = new ArrayList<>();
        applicationGroups.add("group1");

        Engine engine = new Engine("test engine 2", "test endpoint", "enabled");
        List<String> engines = new ArrayList<>();
        engines.add("test engine 2");
        engineBusiness.add(engine);

        AppClass appClass = new AppClass("class1", engines, applicationGroups);
        classBusiness.updateClass(appClass);

        Assertions.assertEquals(classBusiness.getClasses().size(), 1, "Incorrect number of classes");
        Assertions.assertEquals(classBusiness.getClasses().get(0).getEngines().get(0), "test engine 2", "Incorrect number of classes");
    }
}