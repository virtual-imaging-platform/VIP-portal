package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.ResourceType;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;

public class ResourceIT extends BaseSpringIT {
    
    @Autowired private ResourceBusiness resourceBusiness;
    @Autowired private ApplicationBusiness appBusiness;
    @Autowired private EngineBusiness engineBusiness;
    @Autowired private AppVersionBusiness appVersionBusiness;

    private Resource resource;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        resource = new Resource(
            "resourceA", 
            true, 
            false, 
            ResourceType.BATCH, 
            "conf.file",
            new ArrayList<>(),
            new ArrayList<>());
    
        createGroup("test_resource", GroupType.RESOURCE);
        resourceBusiness.add(resource);
    }

    @Test
    public void add() throws BusinessException {
        assertEquals(1, resourceBusiness.getAll().size());
    }

    @Test
    public void remove() throws BusinessException {
        resourceBusiness.remove(resource);
        assertEquals(0, resourceBusiness.getAll().size());
    }

    @Test
    public void addExistingResource() throws BusinessException {
        assertThrows(BusinessException.class, () -> resourceBusiness.add(resource));
    }

    @Test
    public void update() throws BusinessException {
        resource.setType(ResourceType.KUBERNETES);
        resourceBusiness.update(resource);
        resource = resourceBusiness.getAll().get(0);

        assertEquals(ResourceType.KUBERNETES, resource.getType());
    }

    @Test
    public void getAll() throws BusinessException {
        resource.setName("resourceB");
        resourceBusiness.add(resource);
        resource.setName("resourceC");
        resourceBusiness.add(resource);

        assertEquals(3, resourceBusiness.getAll().size());
    }

    @Test
    public void getAllVisible() throws BusinessException {
        resource.setName("resourceB");
        resource.setPublic(true);
        resourceBusiness.add(resource);

        assertEquals(2, resourceBusiness.getAll(true).size());
    }

    @Test
    public void getActiveResources() throws BusinessException {
        resource.setName("resourceB");
        resource.setStatus(true);
        resourceBusiness.add(resource);

        assertEquals(1, resourceBusiness.getActiveResources().size());
    }

    @Test
    public void getDeleteAddWithGroup() throws BusinessException {
        createGroup("resourcetest");
        Group group = groupBusiness.get("resourcetest");

        resourceBusiness.associate(resource, group);
        assertEquals(1, resourceBusiness.getByGroup(group).size());
        resourceBusiness.dissociate(resource, group);
        assertEquals(0, resourceBusiness.getByGroup(group).size());
    }

    @Test
    public void getAvailableForUser() throws BusinessException, GRIDAClientException {
        createGroup("resourcetest");
        createUserInGroup("super@test.insa", "resourcetest");
        Group group = groupBusiness.get("resourcetest");
        User user = configurationBusiness.getUser("super@test.insa");

        resource.setGroups(Arrays.asList(group.getName()));
        resourceBusiness.update(resource);
        assertEquals(0, resourceBusiness.getAvailableForUser(user).size());

        resource.setStatus(true);
        resourceBusiness.update(resource);

        assertEquals(1, resourceBusiness.getAvailableForUser(user).size());
    }

    @Test
    public void associateToAppVersion() throws BusinessException {
        Application app = new Application("test", "super citation");
        AppVersion appVersion = new AppVersion("test", "0.1", "{}", false);
        Resource bis = new Resource("bis");

        appBusiness.add(app);
        appVersionBusiness.add(appVersion);

        resourceBusiness.add(bis);
        resourceBusiness.associate(resource, appVersion);

        assertEquals(resource.getName(), resourceBusiness.getByAppVersion(appVersion).get(0).getName());
        assertEquals(resource.getType(), resourceBusiness.getByAppVersion(appVersion).get(0).getType());
        assertEquals(resource.getConfiguration(), resourceBusiness.getByAppVersion(appVersion).get(0).getConfiguration());
        assertEquals(1, resourceBusiness.getByAppVersion(appVersion).size());
    }

    @Test
    public void dissociateFromAppVersion() throws BusinessException {
        Application app = new Application("test", "super citation");
        AppVersion appVersion = new AppVersion("test", "0.1", "{}", false);

        appBusiness.add(app);
        appVersionBusiness.add(appVersion);

        resourceBusiness.associate(resource, appVersion);

        assertEquals(resource.getName(), resourceBusiness.getByAppVersion(appVersion).get(0).getName());
        assertEquals(resource.getType(), resourceBusiness.getByAppVersion(appVersion).get(0).getType());
        assertEquals(resource.getConfiguration(), resourceBusiness.getByAppVersion(appVersion).get(0).getConfiguration());
        assertEquals(1, resourceBusiness.getByAppVersion(appVersion).size());

        resourceBusiness.dissociate(resource, appVersion);

        assertTrue(resourceBusiness.getByAppVersion(appVersion).isEmpty());
    }

    @Test
    public void associateToEngine() throws BusinessException {
        Engine engine = new Engine("bla", "blou", "bli");
        Resource bis = new Resource("bis");

        engineBusiness.add(engine);

        resourceBusiness.add(bis);
        resourceBusiness.associate(resource, engine);

        assertEquals(resource.getName(), resourceBusiness.getByEngine(engine).get(0).getName());
        assertEquals(resource.getType(), resourceBusiness.getByEngine(engine).get(0).getType());
        assertEquals(resource.getConfiguration(), resourceBusiness.getByEngine(engine).get(0).getConfiguration());
        assertEquals(1, resourceBusiness.getByEngine(engine).size());
    }

    @Test
    public void dissociateFromEngine() throws BusinessException {
        Engine engine = new Engine("bla", "blou", "bli");

        engineBusiness.add(engine);

        resourceBusiness.associate(resource, engine);

        assertEquals(resource.getName(), resourceBusiness.getByEngine(engine).get(0).getName());
        assertEquals(resource.getType(), resourceBusiness.getByEngine(engine).get(0).getType());
        assertEquals(resource.getConfiguration(), resourceBusiness.getByEngine(engine).get(0).getConfiguration());
        assertEquals(1, resourceBusiness.getByEngine(engine).size());

        resourceBusiness.dissociate(resource, engine);

        assertTrue(resourceBusiness.getByEngine(engine).isEmpty());
    }
}
