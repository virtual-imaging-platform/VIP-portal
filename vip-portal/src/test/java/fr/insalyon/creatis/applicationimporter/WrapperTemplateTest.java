package fr.insalyon.creatis.applicationimporter;

import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
import fr.insalyon.creatis.vip.applicationimporter.server.business.VelocityUtils;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;

/*
This needs te be here to use the production velocity templates
 */
public class WrapperTemplateTest {

    final protected String WRAPPER_TEMPLATE = "vm/wrapper.vm";

    @Test
    public void testImagePathInWrapperTemplate() throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        BoutiquesApplication boutiquesApp = new BoutiquesApplication("testApp", "test app desc", "42.43");
        VelocityUtils sut = new VelocityUtils();
        // if null
        String wrapperString = sut.createDocument(null, boutiquesApp, WRAPPER_TEMPLATE);
        MatcherAssert.assertThat(wrapperString, containsString("exec launch --stream testApp.json input_param_file.json"));
        // if present
        boutiquesApp.setVipContainer("testContainer");
        wrapperString = sut.createDocument(null, boutiquesApp, WRAPPER_TEMPLATE);
        MatcherAssert.assertThat(wrapperString, containsString("exec launch --stream --imagepath testContainer testApp.json input_param_file.json"));
    }

}
