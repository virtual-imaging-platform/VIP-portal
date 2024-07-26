package fr.insalyon.creatis.applicationimporter;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesInput;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesNumberInput;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesStringInput;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.GwendiaParser;
import fr.insalyon.creatis.vip.applicationimporter.server.business.VelocityUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

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
        String wrapperString = sut.createDocument(null, boutiquesApp, false, WRAPPER_TEMPLATE);
        MatcherAssert.assertThat(wrapperString, containsString("exec launch testApp.json input_param_file.json"));
        // if present
        boutiquesApp.setVipContainer("testContainer");
        wrapperString = sut.createDocument(null, boutiquesApp, false, WRAPPER_TEMPLATE);
        MatcherAssert.assertThat(wrapperString, containsString("exec launch --imagepath testContainer testApp.json input_param_file.json"));
    }

}
