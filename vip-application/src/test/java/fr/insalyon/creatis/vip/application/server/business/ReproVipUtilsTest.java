package fr.insalyon.creatis.vip.application.server.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputM2Parser;
import fr.insalyon.creatis.vip.application.server.business.util.ReproVipInputsParser;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;

public class ReproVipUtilsTest {

    @Mock private ExternalPlatformBusiness externalPlatformBusiness;
    @Mock private GroupDAO groupDAO;

    private InputM2Parser parser;
    private ReproVipInputsParser reproVipUtils;

    @BeforeEach
    public void init() throws BusinessException, DAOException {
        mocks();

        parser = new InputM2Parser();

        reproVipUtils = new ReproVipInputsParser(externalPlatformBusiness, "localhost");
    }

    public void mocks() throws BusinessException, DAOException {
        ExternalPlatform platform = new ExternalPlatform();
        platform.setIdentifier("testGirder");
        platform.setUrl("https://example.girder");

        MockitoAnnotations.openMocks(this);

        when(externalPlatformBusiness.listAll()).thenReturn(Arrays.asList(platform));
        when(groupDAO.getGroups()).thenReturn(Collections.emptyList());
    }

    @Test
    public void girderWorkflowInput() throws BusinessException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        var values = parser.parse("src/test/resources/repro_vip/girder.xml");
        File example = new File("src/test/resources/repro_vip/girder.json");
        
        String inputsExample = mapper.readTree(example).get("inputs").toString();
        String providerExample = mapper.readTree(example).get("provider").toString();
        
        reproVipUtils.parse(values);
        assertEquals(inputsExample, mapper.writeValueAsString(reproVipUtils.getSimplifiedInputs()));
        assertEquals(providerExample, mapper.writeValueAsString(reproVipUtils.getProviderInformations()));
    }

    @Test
    public void localWorkflowInput() throws BusinessException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        var values = parser.parse("src/test/resources/repro_vip/local.xml");
        File example = new File("src/test/resources/repro_vip/local.json");

        String inputsExample = mapper.readTree(example).get("inputs").toString();
        String providerExample = mapper.readTree(example).get("provider").toString();

        reproVipUtils.parse(values);

        assertEquals(inputsExample, mapper.writeValueAsString(reproVipUtils.getSimplifiedInputs()));
        assertEquals(providerExample, mapper.writeValueAsString(reproVipUtils.getProviderInformations()));
    }
}
