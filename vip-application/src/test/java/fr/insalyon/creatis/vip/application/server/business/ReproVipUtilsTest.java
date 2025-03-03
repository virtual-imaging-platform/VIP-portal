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
import fr.insalyon.creatis.vip.application.server.business.util.ReproVipUtils;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.GroupDAO;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;

public class ReproVipUtilsTest {

    @Mock private ExternalPlatformBusiness externalPlatformBusiness;
    @Mock private Server server;
    @Mock private GroupDAO groupDAO;
    
    private LfcPathsBusiness lfcPathsBusiness;
    private InputM2Parser parser;
    private ReproVipUtils reproVipUtils;

    @BeforeEach
    public void init() throws BusinessException, DAOException {
        mocks();

        lfcPathsBusiness = new LfcPathsBusiness(server, groupDAO);
        parser = new InputM2Parser("admin_test");
        parser.setLfcPathsBusiness(lfcPathsBusiness);

        reproVipUtils = new ReproVipUtils(externalPlatformBusiness, "localhost");
    }

    public void mocks() throws BusinessException, DAOException {
        ExternalPlatform platform = new ExternalPlatform();
        platform.setIdentifier("testGirder");
        platform.setUrl("https://example.girder");

        MockitoAnnotations.openMocks(this);

        when(externalPlatformBusiness.listAll()).thenReturn(Arrays.asList(platform));
        when(groupDAO.getGroups()).thenReturn(Collections.emptyList());
        when(server.getDataManagerUsersHome()).thenReturn("/var/www/html/workflows/SharedData/users");
        when(server.getVoRoot()).thenReturn("/var/www/html/workflows/SharedData");
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
