package fr.insalyon.creatis.vip.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.application.server.rpc.ApplicationServiceImpl;
import fr.insalyon.creatis.vip.core.client.VipException;


public class JsoupIntegrationTest {

    @Test
    public void testJsoup() throws VipException {
        String citationOk = "&nbsp; S. Camarasu-Pop, T. Glatard, R. Ferreira da Silva, P. Gueth, D. Sarrut, and H. Benoit-Cattin, <a href=\"http://www.sciencedirect.com/science/article/pii/S0167739X12001768\">\"Monte-Carlo Simulation on Heterogeneous Distributed Systems: a Computing Framework with Parallel Merging and Checkpointing Strategies\"</a>, Future Generation Computer Systems, vol. 29, no. 3, pp. 728--738, 03/2013<br>";
        String emptyCitation =  "  <br />  ";

        ApplicationServiceImpl applicationService = new ApplicationServiceImpl();
        ApplicationBusiness applicationBusiness = Mockito.mock(ApplicationBusiness.class);
        Mockito.when(applicationBusiness.getCitation(ArgumentMatchers.anyString())).thenReturn(citationOk, emptyCitation);
        applicationService.setBeans(applicationBusiness, null, null, null, null, null, null, null, null, null);
        Assertions.assertEquals(
                citationOk,
                applicationService.getCitation("firstcall"));
        Assertions.assertNull(applicationService.getCitation("secondcall"));
    }

}
