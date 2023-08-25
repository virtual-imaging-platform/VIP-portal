package fr.insalyon.creatis.application.client;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesInput;
import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesStringInput;
import fr.insalyon.creatis.vip.application.client.view.boutiquesParsing.InvalidBoutiquesDescriptorException;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.GwendiaParser;
import fr.insalyon.creatis.vip.applicationimporter.server.business.VelocityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

/*
This needs te be here to use the production velocity templates
 */
public class GwendiaTemplateTest {

    @Test
    public void testDotGwendiaTemplate() throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        testStandaloneGwendiaTemplate("test input desc", "vm/gwendia-dot-inputs.vm");
    }

    @Test
    public void testStandaloneGwendiaTemplate() throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        testStandaloneGwendiaTemplate("test input desc", "vm/gwendia-standalone.vm");
    }

    @Test
    public void testDotGwendiaTemplateWithoutInputDesc() throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        testStandaloneGwendiaTemplate(null, "vm/gwendia-dot-inputs.vm");
    }

    @Test
    public void testStandaloneGwendiaTemplateWithoutInputDesc() throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        testStandaloneGwendiaTemplate(null, "vm/gwendia-standalone.vm");
    }

    public void testStandaloneGwendiaTemplate(String inputDescription, String templateFile) throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        BoutiquesApplication boutiquesApp = new BoutiquesApplication("testApp", "test app desc", "42.43");
        boutiquesApp.addInput(new BoutiquesStringInput(
                "testInputId", "test input", inputDescription, BoutiquesInput.InputType.FILE,
                true, null, null, null, null, null, null));;


        VelocityUtils sut = new VelocityUtils();
        String gwendiaString = sut.createDocument(boutiquesApp, "lnf:", templateFile);
        Descriptor gwendiaApp = new GwendiaParser().parseString(gwendiaString);
        Assertions.assertEquals(ApplicationConstants.INPUT_WITHOUT_VALUE, gwendiaApp.getSources().get(1).getDefaultValue());
        Assertions.assertEquals(inputDescription == null ? "" : inputDescription, gwendiaApp.getSources().get(1).getDescription());
    }

}
