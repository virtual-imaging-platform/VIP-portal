package fr.insalyon.creatis.vip.application.server.business;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.dao.h2.SimulationData;
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
import java.util.*;
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
    public String generateReprovipJson(Path reproVipDir, String executionName, String executionID, String version,
                                       String comments, User currentUser, List<Path> provenanceFiles)
            throws BusinessException, DAOException {
        List<String> filesToDownload = getFilesToCopyPaths(executionName, executionID, version);

        List<String> filesToUpload = provenanceFiles.stream()
                .map(Path::toString)
                .collect(Collectors.toList());

        Map<String, Object> structuredJson = new LinkedHashMap<>();
        structuredJson.put("files_to_download", filesToDownload);
        structuredJson.put("files_to_upload", filesToUpload);

        Map<String, List<String>> invocationOutputsMap = new LinkedHashMap<>();

        for (Path provenanceFile : provenanceFiles) {
            String fileName = provenanceFile.getFileName().toString();
            String invocationID = fileName.substring(0, fileName.indexOf(".sh.provenance.json"));
            List<String> outputDataPaths = simulationBusiness.getSimulationDAO(executionID).getOutputData(invocationID);
            invocationOutputsMap.put(invocationID, outputDataPaths);
        }

        structuredJson.put("invocation_outputs", invocationOutputsMap);

        Map<String, Object> metadataOuter = new LinkedHashMap<>();
        Map<String, Object> metadataInner = new LinkedHashMap<>();

        metadataInner.put("title", "your title");
        metadataInner.put("upload_type", "workflow");
        metadataInner.put("description", comments);

        List<Map<String, String>> creators = new ArrayList<>();
        Map<String, String> creator = new LinkedHashMap<>();
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
    }
    public String createReproVipDirectory(String executionName, String executionID, String version, String comments,  User currentUser) throws BusinessException, DAOException {
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
        List<Path> provenanceFiles = copyProvenanceFiles(reproVipDir, executionID);
        return generateReprovipJson(reproVipDir, executionName, executionID, version, comments, currentUser, provenanceFiles);
    }

    public List<Path> copyProvenanceFiles(Path reproVipDir, String executionID) {
        try {
            logger.debug("Workflows path: " + server.getWorkflowsPath());
            List<Path> copiedProvenanceFiles = new ArrayList<>();

            // directory where provenance files are stored
            Path provenanceDirPath = Paths.get(server.getWorkflowsPath() + "/" + executionID + "/provenance");
            if (!Files.exists(provenanceDirPath)) {
                logger.error("Provenance directory does not exist: " + provenanceDirPath);
                return copiedProvenanceFiles;
            }

            try (Stream<Path> stream = Files.list(provenanceDirPath)) {
                List<Path> provenanceFiles = stream
                        .filter(path -> path.toString().endsWith(".sh.provenance.json"))
                        .collect(Collectors.toList());

                for (Path provenanceFile : provenanceFiles) {
                    // Extract the invocationID from the provenance file name
                    String fileName = provenanceFile.getFileName().toString();
                    // The invocationID is between the first dash and ".sh.provenance.json"
                    String invocationID = fileName.substring(0, fileName.indexOf(".sh.provenance.json"));

                    // Create subfolder named with the invocationID
                    Path invocationDir = reproVipDir.resolve(invocationID);
                    if (!Files.exists(invocationDir)) {
                        Files.createDirectories(invocationDir);
                    }

                    // Copy the source file to the new subfolder
                    Path newLocation = invocationDir.resolve(provenanceFile.getFileName());
                    Files.copy(provenanceFile, newLocation, StandardCopyOption.REPLACE_EXISTING);
                    logger.info("Copied provenance file to directory: {}", newLocation);

                    copiedProvenanceFiles.add(newLocation);
                }
            }
            return copiedProvenanceFiles;
        } catch (IOException e) {
            logger.error("Error while copying provenance files", e);
            throw new RuntimeException(e);
        }
    }

    public List<String> getFilesToCopyPaths(String executionName, String executionID, String version) throws BusinessException {
        List<String> paths = new ArrayList<>();

        List<InOutData> outputData = workflowBusiness.getRawOutputData(executionID);
        if (outputData != null && !outputData.isEmpty()) {
            for (InOutData data : outputData) {
                String outputPath = data.getPath();
                if (outputPath != null) {
                    paths.add(outputPath);
                }
            }
        }

        AppVersion appVersion = applicationBusiness.getVersion(executionName, version);
        if (appVersion != null && appVersion.getJsonLfn() != null && !paths.contains(appVersion.getJsonLfn())) {
            paths.add(appVersion.getJsonLfn());
        }

        return paths;
    }
}
