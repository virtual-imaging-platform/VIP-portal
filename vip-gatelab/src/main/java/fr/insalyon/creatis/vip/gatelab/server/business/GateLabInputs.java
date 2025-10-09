package fr.insalyon.creatis.vip.gatelab.server.business;

import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputFileParser;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import jakarta.annotation.PostConstruct;

/**
 * This stores data in fields and this is not threadsafe. So it cannot be used
 * as a spring singleton and this needs prototype scope.
 */
@Component
@Scope("prototype")
public class GateLabInputs {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, String> inputsMap;
    private String inputfile;

    private Server server;
    private String workflowID;
    private InputFileParser inputFileParser;

    @Autowired
    public final void setServer(Server server) {
        this.server = server;
    }

    @Autowired
    public void setInputFileParser(InputFileParser inputFileParser) {
        this.inputFileParser = inputFileParser;
    }

    public GateLabInputs(String workflowID) {
        this.workflowID = workflowID;
    }

    @PostConstruct
    public final void init() throws VipException {
        inputfile = server.getWorkflowsPath() + "/" + workflowID + "/inputs.json";
        inputsMap = inputFileParser.parse(Path.of(inputfile));
    }

    public Map<String, String> getWorkflowInputs() {
        return Map.of(
            "gateInput", inputsMap.get("gateInput"),
            "numberOfJobs", inputsMap.get("numberOfJobs"),
            "macfileName", inputsMap.get("macfileName")
        );
    }
}
