package fr.insalyon.creatis.vip.application.server.business.simulation;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.application.client.view.ApplicationError;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.util.ProxyUtil;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.business.Server;

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
     */
    @Override
    public String launch(String addressWS, String workflow, String inputs, String settings, String executorConfig, String proxyFileName)
            throws VipException {
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
            String jsonBody;

            try {
                jsonBody = mapper.writeValueAsString(restWorkflow);
            } catch (JsonProcessingException e) {
                logger.error("Error serializing RestWorkflow to JSON", e);
                throw new VipException("Error serializing RestWorkflow to JSON", e);
            }

            RestClient restClient = buildRestClient(addressWS);
            return restClient.post()
                    .uri("/submit")
                    .contentType(APPLICATION_JSON)
                    .body(jsonBody)
                    .retrieve()
                    .body(String.class);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            switch (e.getStatusCode().value()) {
                case 503:
                    logger.warn("Engine satured!: {}", e.getMessage(), e);
                    throw new VipException(ApplicationError.ENGINE_SATURATED, e);
                case 400:
                    logger.warn("Application likely misconfigured: {}", e.getMessage(), e);
                    throw new VipException(DefaultError.GENERIC_ERROR, e);
                default:
                    logger.error("Server error while fetching workflow status: {}", e.getResponseBodyAsString(), e);
                    throw new VipException("Internal server error while fetching workflow status", e);
            }
        } catch (RestClientException e) {
            logger.error("REST client error while fetching workflow status", e);
            throw new VipException("REST client error while fetching workflow status", e);
        }
    }


    @Override
    public void kill(String addressWS, String workflowID) throws VipException {

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
            throw new VipException("Internal server error while fetching workflow status", e);
        } catch (RestClientException e) {
            logger.error("REST client error while fetching workflow status", e);
            throw new VipException("REST client error while fetching workflow status", e);
        }
    }

    public SimulationStatus getStatus(String addressWS, String workflowID) throws VipException {
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
            throw new VipException("Internal server error while fetching workflow status", e);
        } catch (RestClientException e) {
            logger.error("REST client error while fetching workflow status", e);
            throw new VipException("REST client error while fetching workflow status", e);
        }
    }

    private RestClient buildRestClient(String addressWS) {
        return RestClient.builder()
                .baseUrl(addressWS)
                .defaultHeaders(headers -> headers.setBasicAuth("user", server.getMoteurServerPassword()))
                .build();
    }
}