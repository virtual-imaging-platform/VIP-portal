package fr.insalyon.creatis.vip.application.server.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputFileParser;
import fr.insalyon.creatis.vip.application.server.business.util.ReproVipInputsParser;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;

public class ReproVipInputsParserTest {

    @Mock private ExternalPlatformBusiness externalPlatformBusiness;
    @Mock private GroupDAO groupDAO;

    private InputFileParser parser;
    private ReproVipInputsParser reproVipInputsParser;

    @BeforeEach
    public void init() throws VipException, DAOException {
        mocks();

        parser = new InputFileParser();

        reproVipInputsParser = new ReproVipInputsParser(externalPlatformBusiness, "localhost");
    }

    public void mocks() throws VipException, DAOException {
        ExternalPlatform platform = new ExternalPlatform();
        platform.setIdentifier("testGirder");
        platform.setUrl("https://example.girder");

        MockitoAnnotations.openMocks(this);

        when(externalPlatformBusiness.listAll()).thenReturn(Arrays.asList(platform));
        when(groupDAO.get()).thenReturn(Collections.emptyList());
    }

    @Test
    public void girderWorkflowInput() throws VipException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        var values = parser.parse(Path.of("src/test/resources/repro_vip/girder.xml"));
        File example = new File("src/test/resources/repro_vip/girder.json");
        
        String inputsExample = mapper.readTree(example).get("inputs").toString();
        String providerExample = mapper.readTree(example).get("provider").toString();
        
        reproVipInputsParser.parse(values);
        assertEquals(inputsExample, mapper.writeValueAsString(reproVipInputsParser.getSimplifiedInputs()));
        assertEquals(providerExample, mapper.writeValueAsString(reproVipInputsParser.getProviderInformations()));
    }

    @Test
    public void localWorkflowInput() throws VipException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        var values = parser.parse(Path.of("src/test/resources/repro_vip/local.xml"));
        File example = new File("src/test/resources/repro_vip/local.json");

        String inputsExample = mapper.readTree(example).get("inputs").toString();
        String providerExample = mapper.readTree(example).get("provider").toString();

        reproVipInputsParser.parse(values);

        assertEquals(inputsExample, mapper.writeValueAsString(reproVipInputsParser.getSimplifiedInputs()));
        assertEquals(providerExample, mapper.writeValueAsString(reproVipInputsParser.getProviderInformations()));
    }
}
