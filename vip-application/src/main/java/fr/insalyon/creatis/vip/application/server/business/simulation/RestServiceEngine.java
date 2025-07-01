/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.server.business.simulation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.util.ProxyUtil;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import javax.xml.rpc.ServiceException;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author Rafael Ferreira da Silva, Ibrahim kallel
 */
@Service
public class RestServiceEngine extends WorkflowEngineInstantiator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Server server;

    @Autowired
    public RestServiceEngine(Server server) {
        this.server = server;
    }


    private static class RestWorkflow {
        @JsonProperty("workflow")
        String workflow;

        @JsonProperty("inputs")
        String inputs;

        @JsonProperty("proxy")
        String proxy;

        @JsonProperty("settings")
        String settings;

        @JsonProperty("executorConfig")
        String executorConfig;

        public RestWorkflow(String workflow, String inputs, String proxy, String settings, String executorConfig) {
            this.workflow = workflow;
            this.inputs = inputs;
            this.proxy = proxy;
            this.settings = settings;
            this.executorConfig = executorConfig;
        }
    }

    /**
     * Call the WS that is going to run the workflow and return the HTTP link
     * that can be used to monitor the workflow status.
     *
     * @return the HTTP link that shows the workflow current status
     */
    @Override
    public String launch(String addressWS, String workflow, String inputs, String settings, String executorConfig, String proxyFileName)
            throws RemoteException, ServiceException, BusinessException {
        loadTrustStore(server);

        String strProxy = null;
        if (server.getMyProxyEnabled()) {
            strProxy = ProxyUtil.readAsString(proxyFileName);
        }

        String base64Workflow = Base64.getEncoder().encodeToString(workflow.getBytes(StandardCharsets.UTF_8));
        String base64Input = Base64.getEncoder().encodeToString(inputs.getBytes(StandardCharsets.UTF_8));
        String base64Proxy = strProxy != null ?
                (Base64.getEncoder().encodeToString(strProxy.getBytes(StandardCharsets.UTF_8)))
                : null;
        String base64Settings = Base64.getEncoder().encodeToString(settings.getBytes(StandardCharsets.UTF_8));
        String base64ExecutorConfig = Base64.getEncoder().encodeToString(executorConfig.getBytes(StandardCharsets.UTF_8));

        RestWorkflow restWorkflow = new RestWorkflow(base64Workflow, base64Input, base64Proxy, base64Settings, base64ExecutorConfig);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(restWorkflow);

            RestClient restClient = buildRestClient(addressWS);
            return restClient.post()
                    .uri("/submit")
                    .contentType(APPLICATION_JSON)
                    .body(jsonBody)
                    .retrieve()
                    .body(String.class);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            logger.error("Server error while fetching workflow status: {}", e.getResponseBodyAsString(), e);
            throw new BusinessException("Internal server error while fetching workflow status", e);
        } catch (RestClientException e) {
            logger.error("REST client error while fetching workflow status", e);
            throw new BusinessException("REST client error while fetching workflow status", e);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing RestWorkflow to JSON", e);
            throw new BusinessException("Error serializing RestWorkflow to JSON", e);
        }
    }


    @Override
    public void kill(String addressWS, String workflowID) throws BusinessException {

        loadTrustStore(server);

        try {
            RestClient restClient = buildRestClient(addressWS);

            restClient
                    .put()
                    .uri("/kill")
                    .body(Map.of("workflowID", workflowID))
                    .contentType(APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);


            logger.info("Successfully sent kill request for workflow ID: {}", workflowID);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            logger.error("Server error while fetching workflow status: {}", e.getResponseBodyAsString(), e);
            throw new BusinessException("Internal server error while fetching workflow status", e);
        } catch (RestClientException e) {
            logger.error("REST client error while fetching workflow status", e);
            throw new BusinessException("REST client error while fetching workflow status", e);
        }
    }

    public SimulationStatus getStatus(String addressWS, String workflowID) throws BusinessException {
        loadTrustStore(server);

        try {
            RestClient restClient = buildRestClient(addressWS);

            String workflowStatus = restClient
                    .get()
                    .uri("/status/{workflowID}", workflowID)
                    .retrieve()
                    .body(String.class);

            MoteurStatus moteurStatus = MoteurStatus.valueOf(workflowStatus != null ? workflowStatus.toUpperCase() : null);
            return switch (moteurStatus) {
                case RUNNING -> SimulationStatus.Running;
                case COMPLETE -> SimulationStatus.Completed;
                case FAILED -> SimulationStatus.Failed;
                case TERMINATED -> SimulationStatus.Killed;
                default -> SimulationStatus.Unknown;
            };
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            logger.error("Server error while fetching workflow status: {}", e.getResponseBodyAsString(), e);
            throw new BusinessException("Internal server error while fetching workflow status", e);
        } catch (RestClientException e) {
            logger.error("REST client error while fetching workflow status", e);
            throw new BusinessException("REST client error while fetching workflow status", e);
        }
    }

    private RestClient buildRestClient(String addressWS) {
        return RestClient.builder()
                .baseUrl(addressWS)
                .defaultHeaders(headers -> headers.setBasicAuth("user", server.getMoteurServerPassword()))
                .build();
    }
}