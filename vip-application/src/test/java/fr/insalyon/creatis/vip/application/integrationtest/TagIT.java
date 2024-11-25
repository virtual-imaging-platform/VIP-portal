package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.TagBusiness;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;

public class TagIT extends BaseSpringIT {

    @Autowired
    private TagBusiness tagBusiness;

    @Autowired 
    private ApplicationBusiness appBusiness;

    private Tag tag;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        tag = new Tag("bla");
        tagBusiness.add(tag);
    }

    @Test
    public void add() throws BusinessException {
        assertEquals(1, tagBusiness.getAll().size());
    }

    @Test
    public void addExistingTag() throws BusinessException {
        assertThrows(BusinessException.class, () -> tagBusiness.add(tag));
    }

    @Test
    public void update() throws BusinessException {
        tagBusiness.update(tag, "blou");
        tag = tagBusiness.getAll().get(0);
        assertEquals("blou", tag.getName());
    }

    @Test
    public void remove() throws BusinessException {
        tagBusiness.remove(tag);
        assertEquals(0, tagBusiness.getAll().size());
    }

    @Test
    public void getAll() throws BusinessException {
        Tag bis = new Tag("bli");
        Tag bis2 = new Tag("bleu");
        Tag bis3 = new Tag("blui");

        tagBusiness.add(bis);
        tagBusiness.add(bis2);
        tagBusiness.add(bis3);
        assertEquals(4, tagBusiness.getAll().size());
    }
    

    @Test
    public void associate() throws BusinessException {
        Tag bis = new Tag("blou");
        Application app = new Application("test", "super citation");
        AppVersion appVersion = new AppVersion("test", "0.1", "blink", "blank", false, false);

        appBusiness.add(app);
        appBusiness.addVersion(appVersion);
        tagBusiness.associate(tag, appVersion);

        assertEquals(appVersion.getApplicationName(), tagBusiness.getAssociated(tag).get(0).getApplicationName());
        assertEquals(appVersion.getVersion(), tagBusiness.getAssociated(tag).get(0).getVersion());
        assertEquals(appVersion.getJsonLfn(), tagBusiness.getAssociated(tag).get(0).getJsonLfn());
        assertEquals(tag.getName(), tagBusiness.getTags(appVersion).get(0).getName());
        assertTrue(tagBusiness.getAssociated(bis).isEmpty());
    }

    @Test
    public void dissociate() throws BusinessException {
        Application app = new Application("test", "super citation");
        AppVersion appVersion = new AppVersion("test", "0.1", "blink", "blank", false, false);

        appBusiness.add(app);
        appBusiness.addVersion(appVersion);
        tagBusiness.associate(tag, appVersion);

        assertEquals(appVersion.getApplicationName(), tagBusiness.getAssociated(tag).get(0).getApplicationName());
        assertEquals(appVersion.getVersion(), tagBusiness.getAssociated(tag).get(0).getVersion());
        assertEquals(appVersion.getLfn(), tagBusiness.getAssociated(tag).get(0).getLfn());
        assertEquals(tag.getName(), tagBusiness.getTags(appVersion).get(0).getName());

        tagBusiness.dissociate(tag, appVersion);

        assertDoesNotThrow(() -> tagBusiness.dissociate(tag, appVersion));
        assertTrue(tagBusiness.getTags(appVersion).isEmpty());
    }
}
