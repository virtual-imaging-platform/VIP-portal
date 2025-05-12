package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Tag;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.TagBusiness;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;

public class TagIT extends BaseSpringIT {

    @Autowired private TagBusiness tagBusiness;
    @Autowired private ApplicationBusiness appBusiness;
    @Autowired private AppVersionBusiness appVersionBusiness;

    private Tag tag;
    private Application app;
    private AppVersion appVersion;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        app = new Application("applicationA", "super citation");
        appVersion = new AppVersion("applicationA", "0.1", "blink", "blank", false, false);
        tag = new Tag("bla", "applicationA", "0.1", true, true);

        appBusiness.add(app);
        appVersionBusiness.add(appVersion);
        tagBusiness.add(tag);
    }

    @Test
    public void add() throws BusinessException {
        List<Tag> result = tagBusiness.getAll();

        assertEquals(1, result.size());
        assertEquals(tag.getName(), result.getFirst().getName());
        assertEquals(tag.getApplication(), result.getFirst().getApplication());
        assertEquals(tag.getVersion(), result.getFirst().getVersion());
        assertEquals(tag.isVisible(), result.getFirst().isVisible());
        assertEquals(tag.isBoutiques(), result.getFirst().isBoutiques());
    }

    @Test
    public void addExistingTag() throws BusinessException {
        assertThrows(BusinessException.class, () -> tagBusiness.add(tag));
    }

    @Test
    public void update() throws BusinessException {
        Tag copy = new Tag(tag);

        copy.setName("bli");
        copy.setVisible(false);

        tagBusiness.update(tag, copy);

        tag = tagBusiness.getAll().get(0);
        assertEquals(copy.getName(), tag.getName());
        assertEquals(copy.isVisible(), tag.isVisible());
    }

    @Test
    public void remove() throws BusinessException {
        tagBusiness.remove(tag);
        assertEquals(0, tagBusiness.getAll().size());
    }

    @Test
    public void getAll() throws BusinessException {
        Tag bis = new Tag("bli", appVersion.getApplicationName(), appVersion.getVersion());
        Tag bis2 = new Tag("bleu", appVersion.getApplicationName(), appVersion.getVersion());
        Tag bis3 = new Tag("blui", appVersion.getApplicationName(), appVersion.getVersion());

        tagBusiness.add(bis);
        tagBusiness.add(bis2);
        tagBusiness.add(bis3);
        assertEquals(4, tagBusiness.getAll().size());
    }

    @Test
    public void getTags() throws BusinessException {
        List<Tag> result = new ArrayList<>();
        Tag bis = new Tag("bli", false, false);
        Tag bis2 = new Tag("bleu", true, true);
        Tag bis3 = new Tag("blui", "applicationC", "0.3", false, true);
    
        Application app1 = new Application("test", "super citation");
        AppVersion appVersion1 = new AppVersion("test", "0.1", "blink", "blank", false, false);
        Application app2 = new Application("applicationC", "super citation");
        AppVersion appVersion2 = new AppVersion("applicationC", "0.3", "blink", "blank", false, false);
        appVersion1.setTags(Arrays.asList(bis, bis2));

        appBusiness.add(app1);
        appBusiness.add(app2);
        appVersionBusiness.add(appVersion1);
        appVersionBusiness.add(appVersion2);
        tagBusiness.add(bis3);

        result = tagBusiness.getTags(appVersion1);
        assertEquals(2, result.size());
        assertEquals(bis2.getName(), result.get(0).getName());        
        assertEquals(bis.getName(), result.get(1).getName());        
    }
}
