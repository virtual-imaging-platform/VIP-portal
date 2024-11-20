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
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.util.ProxyUtil;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import localhost.moteur_service_wsdl.Moteur_ServiceLocator;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * Communicates with a moteur server through a web service.
 *
 * Each call is relative to a unique endpoint and must create a new instance,
 * so this needs the spring prototype scope.
 *
 * @author Rafael Ferreira da Silva, Ibrahim kallel
 */
@Service
@Scope("prototype")
public class WebServiceEngine extends WorkflowEngineInstantiator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // URI address of the Moteur Web service
    private String addressWS;
    // settings to send to the web service.
    private String settings;

    public String getAddressWS() {
        return addressWS;
    }

    public void setAddressWS(String addressWS) {
        this.addressWS = addressWS;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public WebServiceEngine() {
        this(null, null);
    }

    private Server server;

    private WebServiceEngine(File workflow, List<ParameterSweep> parameters) {
        super(workflow, parameters);
    }

    @Autowired
    public final void setServer(Server server) {
        this.server = server;
    }

    /**
     * Call the WS that is going to run the workflow and return the HTTP link
     * that can be used to monitor the workflow status.
     *
     * @return the HTTP link that shows the workflow current status
     */
    @Override
    public String launch(String proxyFileName, String userDN)
            throws java.rmi.RemoteException, javax.xml.rpc.ServiceException, BusinessException {

        System.setProperty("javax.net.ssl.trustStore", server.getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", server.getTruststorePass());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        String strProxy = ProxyUtil.readAsString(proxyFileName);

        String base64Workflow = Base64.getEncoder().encodeToString(workflow.getBytes(StandardCharsets.UTF_8));
        String base64Input = Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
        String base64Proxy = Base64.getEncoder().encodeToString(strProxy.getBytes(StandardCharsets.UTF_8));
        String base64Settings = Base64.getEncoder().encodeToString(settings.getBytes(StandardCharsets.UTF_8));

        String jsonInputString = String.format(
                "{\"workflow\":\"%s\",\"input\":\"%s\",\"proxy\":\"%s\",\"settings\":\"%s\"}",
                base64Workflow, base64Input, base64Proxy, base64Settings
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonInputString, headers);

        String url = addressWS + "/submit";

        logger.info("sending rest request to launch workflow");
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
        } catch (Error e) {
            logger.error("Error in the rest request on moteur server", e);
            throw new BusinessException("Error in the rest request on moteur server", e);
        }
    }

    @Override
    public void kill(String workflowID) {
        System.setProperty("javax.net.ssl.trustStore", server.getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", server.getTruststorePass());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        String url = addressWS + "/kill";

        // Create JSON body
        String jsonInputString = String.format("{\"workflowID\":\"%s\"}", workflowID);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HTTP entity
        HttpEntity<String> entity = new HttpEntity<>(jsonInputString, headers);

        try {
            // Send POST request
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            // Check response status
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to kill workflow, HTTP response code: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to kill the workflow", e);
        }
    }


    @Override
    public SimulationStatus getStatus(String workflowID) {

        System.setProperty("javax.net.ssl.trustStore", server.getTruststoreFile());
        System.setProperty("javax.net.ssl.trustStorePassword", server.getTruststorePass());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        String url = addressWS + "/status/" + workflowID;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        // Create HTTP entity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Send GET request
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Check response status
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to get status, HTTP response code: " + response.getStatusCodeValue());
            }

            // Process response
            String workflowStatus = response.getBody();
            WebServiceEngine.MoteurStatus moteurStatus = WebServiceEngine.MoteurStatus.valueOf(workflowStatus);
            switch (moteurStatus) {
                case RUNNING:
                    return SimulationStatus.Running;
                case COMPLETE:
                    return SimulationStatus.Completed;
                case TERMINATED:
                    return SimulationStatus.Killed;
                default:
                    return SimulationStatus.Unknown;
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while getting the workflow status", e);
        }
    }

    static enum MoteurStatus {
        RUNNING, COMPLETE, TERMINATED, UNKNOWN
    };
}