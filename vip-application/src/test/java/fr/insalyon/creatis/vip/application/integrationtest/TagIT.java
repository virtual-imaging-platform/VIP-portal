package fr.insalyon.creatis.vip.application.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.business.TagBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;

public class TagIT extends BaseSpringIT {

    @Autowired private TagBusiness tagBusiness;
    @Autowired private ApplicationBusiness appBusiness;
    @Autowired private AppVersionBusiness appVersionBusiness;

    private Tag tag;
    private Application app;
    private AppVersion appVersion;

    @BeforeEach
    public void setUp() throws Exception {
        setAdminContext();
        super.setUp();
        app = new Application("applicationA", "super citation");
        appVersion = new AppVersion("applicationA", "0.1","{}", false);
        tag = new Tag("bla", "blou", Tag.ValueType.STRING, "applicationA", "0.1", true, true);

        appBusiness.add(app);
        appVersionBusiness.add(appVersion);
        tagBusiness.add(tag);
    }

    @Test
    public void add() throws VipException {
        List<Tag> result = tagBusiness.getAll();

        assertEquals(1, result.size());
    }

    @Test
    public void addExistingTag() throws VipException {
        assertThrows(VipException.class, () -> tagBusiness.add(tag));
    }

    @Test
    public void update() throws VipException {
        Tag copy = new Tag(tag);

        copy.setKey("wow");
        copy.setValue("bli");
        copy.setVisible(false);

        tagBusiness.update(tag, copy);

        tag = tagBusiness.getAll().get(0);
        assertEquals(copy, tag);
    }

    @Test
    public void remove() throws VipException {
        tagBusiness.remove(tag);
        assertEquals(0, tagBusiness.getAll().size());
    }

    @Test
    public void getAll() throws VipException {
        Tag bis = new Tag("bli", "ilb", Tag.ValueType.STRING, appVersion.getApplicationName(), appVersion.getVersion(), false, false);
        Tag bis2 = new Tag("bleu", "uelb", Tag.ValueType.STRING, appVersion.getApplicationName(), appVersion.getVersion(), false, false);
        Tag bis3 = new Tag("blui", "iulb", Tag.ValueType.STRING, appVersion.getApplicationName(), appVersion.getVersion(), false, false);

        tagBusiness.add(bis);
        tagBusiness.add(bis2);
        tagBusiness.add(bis3);
        assertEquals(4, tagBusiness.getAll().size());
    }

    @Test
    public void getTags() throws VipException {
        Application app1 = new Application("test", "super citation");
        AppVersion appVersion1 = new AppVersion("test", "0.1", "{}", false);
        Application app2 = new Application("applicationC", "super citation");
        AppVersion appVersion2 = new AppVersion("applicationC", "0.3", "{}", false);
        List<Tag> result = new ArrayList<>();
        
        Tag bis = new Tag("bli", "ilb", Tag.ValueType.STRING, appVersion1, false, false);
        Tag bis2 = new Tag("bleu", "uelb", Tag.ValueType.STRING, appVersion1, true, true);
        Tag bis3 = new Tag("blui", "iulb", Tag.ValueType.STRING, appVersion2, false, true);

        appVersion1.setTags(Arrays.asList(bis, bis2));

        appBusiness.add(app1);
        appBusiness.add(app2);
        appVersionBusiness.add(appVersion1);
        appVersionBusiness.add(appVersion2);
        tagBusiness.add(bis3);

        result = tagBusiness.getTags(appVersion1);
        assertEquals(2, result.size());
        assertEquals(bis2, result.get(0));        
        assertEquals(bis, result.get(1));        
    }
}
