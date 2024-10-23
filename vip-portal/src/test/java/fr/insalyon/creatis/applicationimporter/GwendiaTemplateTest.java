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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/*
This needs te be here to use the production velocity templates
 */
public class GwendiaTemplateTest {

    final protected String STANDALONE_TEMPLATE = "vm/gwendia-standalone.vm";

    @ParameterizedTest
    @ValueSource(strings = {STANDALONE_TEMPLATE})
    public void testTemplateWithNonNullDescription(String template) throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        String inputDescription = "test input description";
        Descriptor gwendiaDesc = testGwendiaTemplate(
                template,
                getBasicFileInput(1, inputDescription, null));
        Assertions.assertEquals(ApplicationConstants.INPUT_WITHOUT_VALUE, gwendiaDesc.getSources().get(1).getDefaultValue());
        Assertions.assertEquals(inputDescription, gwendiaDesc.getSources().get(1).getDescription());
    }

    @ParameterizedTest
    @ValueSource(strings = {STANDALONE_TEMPLATE})
    public void testTemplateWithNullDescription(String template) throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        // when the description is not in boutiques, it must be an empty string in gwendia
        Descriptor gwendiaDesc = testGwendiaTemplate(
                template,
                getBasicFileInput(1, null, null));
        Assertions.assertEquals(ApplicationConstants.INPUT_WITHOUT_VALUE, gwendiaDesc.getSources().get(1).getDefaultValue());
        Assertions.assertEquals("", gwendiaDesc.getSources().get(1).getDescription());
    }

    @ParameterizedTest
    @ValueSource(strings = {STANDALONE_TEMPLATE})
    public void testTemplateWithAIntegerInput(String template) throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        Descriptor gwendiaDesc = testGwendiaTemplate(
                template,
                getIntegerInput(1, 42.));
        Assertions.assertEquals("42", gwendiaDesc.getSources().get(1).getDefaultValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {STANDALONE_TEMPLATE})
    public void testTemplateWithANumberInput(String template) throws IOException, InvalidBoutiquesDescriptorException, SAXException {
        Descriptor gwendiaDesc = testGwendiaTemplate(
                template,
                getNumberInput(1, false, 42.));
        Assertions.assertEquals("42.0", gwendiaDesc.getSources().get(1).getDefaultValue());
    }

    protected BoutiquesInput getIntegerInput(Object id, Double defaultValue) {
        return getNumberInput(1, true, 42.);
    }

    protected BoutiquesInput getNumberInput(Object id, boolean isInteger, Double defaultValue) {
        return new BoutiquesNumberInput("testNumberInput" + id, "test number input " + id, "test number description",
                        true, null, null, null, null, null, defaultValue, isInteger, false, false, null, null);
    }

    protected BoutiquesInput getBasicFileInput(Object id, String description, String defaultValue) {
        return new BoutiquesStringInput(
                "testFileInput" + id, "test file input " +id, description, BoutiquesInput.InputType.FILE,
                true, null, null, null, null, null, defaultValue);
    }

    protected Descriptor testGwendiaTemplate(String templateFile, BoutiquesInput... inputs) throws IOException, SAXException {
        BoutiquesApplication boutiquesApp = new BoutiquesApplication("testApp", "test app desc", "42.43");
        for (BoutiquesInput input : inputs) {
            boutiquesApp.addInput(input);
        }
        VelocityUtils sut = new VelocityUtils();
        String gwendiaString = sut.createDocument(boutiquesApp, "lnf:", templateFile);
        return new GwendiaParser().parseString(gwendiaString);
    }

}
