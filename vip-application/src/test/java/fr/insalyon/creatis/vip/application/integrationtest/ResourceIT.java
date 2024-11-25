package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.application.client.bean.Resource;
import fr.insalyon.creatis.vip.application.client.bean.ResourceType;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.GroupType;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;

public class ResourceIT extends BaseSpringIT {
    
    @Autowired
    private ResourceBusiness resourceBusiness;

    private Resource resource;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        resource = new Resource(
            "resourceA", 
            false, 
            false, 
            ResourceType.BATCH, 
            "conf.file");
    
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
        resource.setVisible(true);
        resourceBusiness.add(resource);

        assertEquals(1, resourceBusiness.getAll(true).size());
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
        Group group = configurationBusiness.getGroup("resourcetest");

        resourceBusiness.putInGroup(resource, group);
        assertEquals(1, resourceBusiness.getByGroup(group).size());
        resourceBusiness.removeFromGroup(resource, group);
        assertEquals(0, resourceBusiness.getByGroup(group).size());
    }

    @Test
    public void getAvailableForUser() throws BusinessException, GRIDAClientException {
        createGroup("resourcetest");
        createUserInGroup("super@test.insa", "resourcetest");
        Group group = configurationBusiness.getGroup("resourcetest");
        User user = configurationBusiness.getUser("super@test.insa");

        resourceBusiness.putInGroup(resource, group);
        assertEquals(0, resourceBusiness.getAvailableForUser(user).size());

        resource.setStatus(true);
        resourceBusiness.update(resource);

        assertEquals(1, resourceBusiness.getAvailableForUser(user).size());
    }
}
