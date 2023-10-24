package fr.insalyon.creatis.vip.application.server.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.rpc.JobServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.rpc.JobServiceImpl;
import fr.insalyon.creatis.vip.core.client.bean.Execution;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import fr.insalyon.creatis.vip.core.server.business.EmailBusiness;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ReproVipBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ConfigurationBusiness configurationBusiness;
    @Autowired
    private WorkflowBusiness workflowBusiness;
    @Autowired
    private ApplicationBusiness applicationBusiness;
    @Autowired
    private EmailBusiness emailBusiness;
    @Autowired
    private ExecutionInOutData executionInOutData;
    @Autowired
    private Server server;
    @Autowired
    private SimulationBusiness simulationBusiness;

    public void executionAdminEmail(Execution execution) throws DAOException, BusinessException {
        String adminsEmailContents = "<html>"
                + "<head></head>"
                + "<body>"
                + "<p>Dear Administrator,</p>"
                + "<p>A new user requested to make an execution public</p>"
                + "<p>Details:</p>"
                + "<ul>"
                + "<li>ID: " + execution.getId() + "</li>"
                + "<li>Name: " + execution.getSimulationName() + "</li>"
                + "<li>Name: " + execution.getApplicationName() + "</li>"
                + "<li>Version: " + execution.getVersion() + "</li>"
                + "<li>Status: " + execution.getStatus() + "</li>"
                + "<li>Author: " + execution.getAuthor() + "</li>"
                + "<li>Comments: " + execution.getComments() + "</li>"
                + "</ul>"
                + "<p>Best Regards,</p>"
                + "<p>VIP Team</p>"
                + "</body>"
                + "</html>";

        logger.info("Sending confirmation email from '" + execution.getAuthor() + "'.");
        for (String adminEmail : configurationBusiness.getAdministratorsEmails()) {
            emailBusiness.sendEmail("[VIP Admin] Execution Public Request", adminsEmailContents,
                    new String[]{adminEmail}, true, execution.getAuthor());
        }
        logger.info("Email send");
    }

    public ExecutionInOutData executionOutputData(String executionID, User currentUser) throws ApplicationException, BusinessException {
        logger.info("Fetching data for executionID: {}", executionID);

        List<InOutData> outputData = workflowBusiness.getOutputData(executionID, currentUser.getFolder());
        logger.info(String.valueOf(outputData));
        List<InOutData> inputData = workflowBusiness.getInputData(executionID, currentUser.getFolder());

        if (outputData != null) {
            logger.info("Fetched {} output data items", outputData.size());
            logger.info(outputData.toString());
        } else {
            logger.info("Output data is null for executionID: {}", executionID);
        }

        if (inputData != null) {
            logger.info("Fetched {} input data items", inputData.size());
            logger.info(inputData.toString());
        } else {
            logger.info("Input data is null for executionID: {}", executionID);
        }

        return new ExecutionInOutData(inputData, outputData);
    }

    public ExecutionJobTaskData getExecutionJobTaskData(String executionID) throws BusinessException {
        logger.info("Fetching job and task data for executionID: {}", executionID);
        List<Task> jobList = simulationBusiness.getJobsList(executionID);
        if (jobList == null || jobList.isEmpty()) {
            logger.info("No jobs found for executionID: {}", executionID);
        }
        return new ExecutionJobTaskData(jobList);
    }
    public String generateReprovipJson(Path reproVipDir, String executionName, String executionID, String version, User currentUser)
            throws BusinessException {
        List<String> filesToDownload = getFilesToCopyPaths(executionName, executionID, version);

        Map<String, Object> structuredJson = new HashMap<>();

        structuredJson.put("files_to_download", filesToDownload);

        Map<String, Object> metadataOuter = new HashMap<>();
        Map<String, Object> metadataInner = new HashMap<>();

        metadataInner.put("title", "your title");
        metadataInner.put("upload_type", "workflow");
        metadataInner.put("description", "your description");

        List<Map<String, String>> creators = new ArrayList<>();
        Map<String, String> creator = new HashMap<>();
        creator.put("name", currentUser.getFullName());
        creator.put("affiliation", "your affiliation");
        creators.add(creator);

        metadataInner.put("creators", creators);
        metadataOuter.put("metadata", metadataInner);

        structuredJson.put("metadata", metadataOuter);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(structuredJson);
            logger.info(json);

            Path reprovipJsonPath = reproVipDir.resolve("structuredOutput.json");
            saveJsonToFile(json, reprovipJsonPath);

            return json;
        } catch (IOException e) {
            throw new BusinessException("Failed to save JSON to file", e);
        }
    }
    public void saveJsonToFile(String jsonContent, Path filePath) throws IOException {
        Files.writeString(filePath, jsonContent);
        /*try (FileWriter fileWriter = new FileWriter(filePath.toFile());
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(jsonContent);
        }*/
    }
    public String createReproVipDirectory(String executionName, String executionID, String version, User currentUser) throws BusinessException {
        Path reproVipDir = Paths.get("/vip/ReproVip/" + executionID);
        logger.info("Creating reprovip dir : {}", reproVipDir);
        try {

            if ( ! Files.exists(reproVipDir)) {
                Files.createDirectories(reproVipDir);
            }
        } catch (IOException e) {
            logger.error("Exception creating the a reprovip directory {}", reproVipDir, e);
            throw new RuntimeException(e);
        }
        copyProvenanceFiles(reproVipDir, executionID);
        return generateReprovipJson(reproVipDir, executionName, executionID, version, currentUser);
    }

    public void copyProvenanceFiles(Path reproVipDir, String executionID) {
        try {
            logger.debug("Workflows path: " + server.getWorkflowsPath());

            Path provenanceDirPath = Paths.get(server.getWorkflowsPath() + "/" + executionID + "/provenance");
            if ( ! Files.exists(provenanceDirPath)) {
                logger.error("Provenance directory does not exist: " + provenanceDirPath);
                return;
            }

            try (Stream<Path> stream = Files.list(provenanceDirPath)) {
                List<Path> provenanceFiles = stream
                        .filter(path -> path.toString().endsWith(".sh.provenance.json"))
                        .collect(Collectors.toList());
                for (Path provenanceFile : provenanceFiles) {
                    Files.copy(provenanceFile, reproVipDir.resolve(provenanceFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    logger.info("{} file successfully copied to ReproVip directory", provenanceFile);
                }
            }

        } catch (IOException e) {
            logger.error("Exception creating the a reprovip directory {}", reproVipDir, e);
            throw new RuntimeException(e);
        }
    }

    public List<String> getFilesToCopyPaths(String executionName, String executionID, String version) throws BusinessException {
        List<String> paths = new ArrayList<>();

        List<InOutData> outputData = workflowBusiness.getRawOutputData(executionID);
        if (outputData != null && !outputData.isEmpty()) {
            String outputPath = outputData.get(0).getPath();
            if (outputPath != null) {
                paths.add(outputPath);
            }
        }

        AppVersion appVersion = applicationBusiness.getVersion(executionName, version);
        if (appVersion != null && appVersion.getJsonLfn() != null) {
            paths.add(appVersion.getJsonLfn());
        }

        return paths;
    }
}
