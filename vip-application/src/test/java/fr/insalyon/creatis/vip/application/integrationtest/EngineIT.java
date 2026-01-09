package fr.insalyon.creatis.vip.application.integrationtest;

import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EngineIT extends BaseApplicationSpringIT {

    private Engine engine;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        engine = new Engine("test engine", "test endpoint", "enabled");
        engineBusiness.add(engine);
    }

    @Test
    public void testInitialization() throws BusinessException {
        // verify engines number
        Assertions.assertEquals(1, engineBusiness.get().size());

        // verify engine1 properties
        Assertions.assertEquals("test engine", engine.getName());
        Assertions.assertEquals("test endpoint", engine.getEndpoint());
        Assertions.assertEquals("enabled", engine.getStatus());
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* create and add engine *********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testAdd() throws BusinessException {
        Engine engine2 = new Engine("test engine 2", "test endpoint 2", "enabled");
        engineBusiness.add(engine2);

        // verify engines number
        Assertions.assertEquals(2, engineBusiness.get().size());

        // verify engine2 properties
        Assertions.assertEquals("test engine 2", engineBusiness.get().get(1).getName());
        Assertions.assertEquals("test endpoint 2", engineBusiness.get().get(1).getEndpoint());
        Assertions.assertEquals("enabled", engineBusiness.get().get(1).getStatus());
    }

    @Test
    public void testCatchAddExistingEngine() {
        Engine engine = new Engine("test engine", "test endpoint 2", "enabled");

        Exception exception = assertThrows(
                BusinessException.class, () ->
                        engineBusiness.add(engine)
        );

        // the exception was created before the beginning of the internship
        Assertions.assertTrue(StringUtils.contains(exception.getMessage(), "n engine named \"test engine\" already exists"));
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* update engine *********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testUpdate() throws BusinessException {
        Engine engine = new Engine("test engine", "test endpoint 2", "enabled");
        engineBusiness.update(engine);

        // verify engines number
        Assertions.assertEquals(1, engineBusiness.get().size());

        // verify engine1 properties
        Assertions.assertEquals("test engine", engine.getName());
        Assertions.assertEquals("test endpoint 2", engine.getEndpoint());
        Assertions.assertEquals("enabled", engine.getStatus());
    }

    @Test
    public void testUpdateNonExistentEngine() throws BusinessException {
        Engine engine = new Engine("nonExistent engine", "test endpoint 2", "enabled");

        // UPDATE + nonExistent primary key engine name => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be updated
        engineBusiness.update(engine);
    }

    @Test
    public void testSet() throws BusinessException {
        engine.setStatus("status updated");
        engineBusiness.update(engine);
        Assertions.assertTrue(StringUtils.contains(engine.getStatus(), "status updated"));
    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* remove engine *********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testRemove() throws BusinessException, DAOException {
        engineBusiness.remove("test engine");
        Assertions.assertEquals(0, engineBusiness.get().size());
    }

    @Test
    public void testCatchRemoveNonExistentEngine() throws BusinessException {

        // DELETE + nonExistent primary key engine name => no exception
        // We decided not to add an exception because if this occurs, it will not create problem, just no row will be deleted
        engineBusiness.remove("nonExistent engine");
        Assertions.assertEquals("test engine", engineBusiness.get().get(0).getName());

        // verify engines number
        Assertions.assertEquals(1, engineBusiness.get().size());

    }

    /* ********************************************************************************************************************************************** */
    /* ************************************************************* get engine *********************************************************** */
    /* ********************************************************************************************************************************************** */

    @Test
    public void testGetEngine() throws BusinessException {
        Assertions.assertEquals("test engine", engineBusiness.get().get(0).getName());
    }
}