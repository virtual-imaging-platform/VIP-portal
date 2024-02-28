package fr.insalyon.creatis.vip.application.server.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.server.dao.PublicExecutionDAO;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    private final ConfigurationBusiness configurationBusiness;
    private final ApplicationBusiness applicationBusiness;
    private final EmailBusiness emailBusiness;
    private final Server server;
    private final SimulationBusiness simulationBusiness;
    private final PublicExecutionDAO publicExecutionDAO;
    private final WorkflowBusiness workflowBusiness;

    @Autowired
    public ReproVipBusiness(
            ConfigurationBusiness configurationBusiness, ApplicationBusiness applicationBusiness,
            EmailBusiness emailBusiness, Server server, SimulationBusiness simulationBusiness,
            PublicExecutionDAO publicExecutionDAO, WorkflowBusiness workflowBusiness) {
        this.configurationBusiness = configurationBusiness;
        this.applicationBusiness = applicationBusiness;
        this.emailBusiness = emailBusiness;
        this.server = server;
        this.simulationBusiness = simulationBusiness;
        this.publicExecutionDAO = publicExecutionDAO;
        this.workflowBusiness = workflowBusiness;
    }

    public void createPublicExecution(PublicExecution publicExecution) throws BusinessException {
        try {
            publicExecutionDAO.add(publicExecution);

            String adminsEmailContents = "<html>"
                    + "<head></head>"
                    + "<body>"
                    + "<p>Dear Administrator,</p>"
                    + "<p>A new user requested to make an execution public</p>"
                    + "<p>Details:</p>"
                    + "<ul>"
                    + "<li>ID: " + publicExecution.getId() + "</li>"
                    + "<li>Name: " + publicExecution.getSimulationName() + "</li>"
                    + "<li>Name: " + publicExecution.getApplicationName() + "</li>"
                    + "<li>Version: " + publicExecution.getApplicationVersion() + "</li>"
                    + "<li>Status: " + publicExecution.getStatus() + "</li>"
                    + "<li>Author: " + publicExecution.getAuthor() + "</li>"
                    + "<li>Comments: " + publicExecution.getComments() + "</li>"
                    + "</ul>"
                    + "<p>Best Regards,</p>"
                    + "<p>VIP Team</p>"
                    + "</body>"
                    + "</html>";

            for (String supportEmail : configurationBusiness.getSupportEmails()) {
                emailBusiness.sendEmail("[VIP Admin] Execution Public Request", adminsEmailContents,
                        new String[]{supportEmail}, true, publicExecution.getAuthor());
            }
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public PublicExecution getPublicExecution(String publicExecutionId)  throws BusinessException {
        try {
            return publicExecutionDAO.get(publicExecutionId);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public void updatePublicExecutionStatus(String publicExecutionId, PublicExecution.PublicExecutionStatus newStatus)
            throws BusinessException {
        try {
            publicExecutionDAO.update(publicExecutionId, newStatus);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public List<PublicExecution> getPublicExecutions() throws BusinessException {
        try {
            return publicExecutionDAO.getExecutions();
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public boolean doesExecutionExist(String executionID) throws BusinessException {
        try {
            return publicExecutionDAO.doesExecutionExist(executionID);
        } catch (DAOException e) {
            throw new BusinessException(e);
        }
    }

    public boolean canMakeExecutionPublic(String executionID) throws BusinessException {
        // looking for provenance directory
        Path provenanceDirPath = Paths.get(server.getWorkflowsPath() + "/" + executionID + "/provenance");
        if ( ! Files.exists(provenanceDirPath)) {
             return false;
        }
        // checking if it is empty
        if (provenanceDirPath.toFile().listFiles().length == 0) {
            return false;
        }
        // verifying the application has a boutiques file
        Simulation simulation = workflowBusiness.getSimulation(executionID);
        String boutiquesPath = getBoutiquesDescriptorJsonPath(simulation.getApplicationName(), simulation.getApplicationVersion());
        return boutiquesPath != null;
    }

    public String createReproVipDirectory(String executionID) throws BusinessException {
        PublicExecution publicExecution = getPublicExecution(executionID);
        Path reproVipDir = Paths.get(server.getReproVIPRootDir()).resolve(executionID);
        logger.info("Creating reprovip dir : {}", reproVipDir);
        try {
            if ( ! Files.exists(reproVipDir)) {
                Files.createDirectories(reproVipDir);
            }
        } catch (IOException e) {
            logger.error("Exception creating the a reprovip directory {}", reproVipDir, e);
            throw new BusinessException("Error creating a reprovip directory", e);
        }
        List<Path> provenanceFiles = copyProvenanceFiles(reproVipDir, publicExecution.getId());
        return generateReprovipJson(reproVipDir, publicExecution, provenanceFiles);
    }

    public List<Path> copyProvenanceFiles(Path reproVipDir, String executionID) throws BusinessException {
        try {
            List<Path> copiedProvenanceFiles = new ArrayList<>();

            // directory where provenance files are stored
            Path provenanceDirPath = Paths.get(server.getWorkflowsPath() + "/" + executionID + "/provenance");
            if ( ! Files.exists(provenanceDirPath)) {
                logger.error("Error creating a reprovip directory : no provenance dir for {}", provenanceDirPath);
                throw new BusinessException("Error creating a reprovip directory : no provenance dir");
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
                    if ( ! Files.exists(invocationDir)) {
                        Files.createDirectories(invocationDir);
                    }

                    // Copy the source file to the new subfolder
                    Path newLocation = invocationDir.resolve(provenanceFile.getFileName());
                    Files.copy(provenanceFile, newLocation, StandardCopyOption.REPLACE_EXISTING);
                    logger.debug("Copied provenance file to directory: {}", newLocation);

                    copiedProvenanceFiles.add(newLocation);
                }
            }
            return copiedProvenanceFiles;
        } catch (IOException e) {
            logger.error("Error while copying provenance files for {}", executionID, e);
            throw new BusinessException("Error while copying provenance files", e);
        }
    }

    public String generateReprovipJson(Path reproVipDir, PublicExecution publicExecution, List<Path> provenanceFiles)
            throws BusinessException {

        String executionID = publicExecution.getId();
        String filesToDownload = getBoutiquesDescriptorJsonPath(
                publicExecution.getApplicationName(), publicExecution.getApplicationVersion());

        List<String> filesToUpload = provenanceFiles.stream()
                .map(Path::toString)
                .collect(Collectors.toList());

        Map<String, Object> structuredJson = new LinkedHashMap<>();
        structuredJson.put("path_workflow_directory", reproVipDir);
        structuredJson.put("descriptor_boutique", filesToDownload);
        structuredJson.put("files_to_upload", filesToUpload);

        Map<String, List<String>> invocationOutputsMap = new LinkedHashMap<>();
        try {
            for (Path provenanceFile : provenanceFiles) {
                String fileName = provenanceFile.getFileName().toString();
                String invocationID = fileName.substring(0, fileName.indexOf(".sh.provenance.json"));
                List<String> outputDataPaths = simulationBusiness.getSimulationDAO(executionID).getOutputData(invocationID);
                Map<String, String> provenanceOutputFilenames = getOutputFilenamesFromProvenanceFile(provenanceFile);
                List<String> outputDataPathToKeep = getOutputPathToDownload(
                        outputDataPaths,
                        provenanceOutputFilenames,
                        publicExecution.getOutputNames());
                invocationOutputsMap.put(invocationID, outputDataPathToKeep);
            }
        } catch (DAOException e) {
            throw new BusinessException(e);
        }

        structuredJson.put("invocation_outputs", invocationOutputsMap);

        Map<String, Object> metadataOuter = new LinkedHashMap<>();
        Map<String, Object> metadataInner = new LinkedHashMap<>();

        metadataInner.put("title", "your title");
        metadataInner.put("upload_type", "workflow");
        metadataInner.put("description", publicExecution.getComments());

        List<Map<String, String>> creators = new ArrayList<>();
        Map<String, String> creator = new LinkedHashMap<>();
        creator.put("name", publicExecution.getAuthor());
        creators.add(creator);

        metadataInner.put("creators", creators);
        metadataOuter.put("metadata", metadataInner);

        structuredJson.put("metadata", metadataOuter);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonContent = objectMapper.writeValueAsString(structuredJson);
            Path reprovipJsonPath = reproVipDir.resolve("workflowMetadata.json");
            Files.writeString(reprovipJsonPath, jsonContent);

            return jsonContent;
        } catch (IOException e) {
            logger.error("Error saving reprovip metadata file for {}", publicExecution.getId(), e);
            throw new BusinessException("Failed to save JSON to file", e);
        }
    }

    public String getBoutiquesDescriptorJsonPath(String applicationName, String applicationVersion) throws BusinessException {
        AppVersion appVersion = applicationBusiness.getVersion(applicationName, applicationVersion);
        if (appVersion != null && appVersion.getJsonLfn() != null) {
            return appVersion.getJsonLfn();
        }
        return null;
    }

    /*
        outputDataPaths is the list of all the results path/URIs produced for a job
        provenanceOutputFilenames is the mapping between the output id and their filename
     */
    private List<String> getOutputPathToDownload(
            List<String> outputDataPaths,
            Map<String, String> provenanceOutputFilenames,
            List<String> outputIdsToKeep) throws BusinessException {
        Map<String, String> outputDataPathsByFilenames = mapOutputDataPathsByFilenames(outputDataPaths);
        List<String> results = new ArrayList<>(); // all the path/URIs to keep
        for (String outputIdToKeep : outputIdsToKeep) {
            if ( ! provenanceOutputFilenames.containsKey(outputIdToKeep)) {
                logger.error("Error getting a output path from provenance file : {} output id not found", outputIdToKeep);
                throw new BusinessException("Error getting output path from provenance");
            }
            String filename = provenanceOutputFilenames.get(outputIdToKeep);
            if ( ! outputDataPathsByFilenames.containsKey(filename)) {
                logger.error("Error getting a output with filename {}", filename);
                throw new BusinessException("Error getting a output with the expected filename");
            }
            results.add(outputDataPathsByFilenames.get(filename));
        }
        return results;
    }

    private Map<String, String> mapOutputDataPathsByFilenames(List<String> outputDataPaths) throws BusinessException {
        Map<String, String> outputDataMap = new HashMap<>();
        for (String outputDataPath : outputDataPaths) {
            try {
                    URI uri = new URI(outputDataPath);
                    String filename = Paths.get(uri.getPath()).getFileName().toString();
                    if (outputDataMap.containsKey(filename)) {
                        logger.error("A job contains two results with the same filename [{}]", filename);
                        throw new BusinessException("A job contains two results with the same filename");
                    }
                    outputDataMap.put(filename, outputDataPath);
            } catch (URISyntaxException e) {
                logger.error("Cannot convert a job result to URI [{}]", outputDataPath, e);
                throw new BusinessException("Error converting a job result to URI", e);
            }
        }
        return outputDataMap;
    }

    private Map<String, String> getOutputFilenamesFromProvenanceFile(Path provenanceFilePath) throws BusinessException {
        try {
            Map<?, ?> map = new ObjectMapper().readValue(provenanceFilePath.toFile(), Map.class);
            Map<String, ?> publicOutputSection = (Map<String, ?>) map.get("public-output");
            Map<String, Map<String,String>> outputFiles = (Map<String, Map<String, String>>) publicOutputSection.get("output-files");
            Map<String, String> outputFilenames = new HashMap<>();
            for (String outputId : outputFiles.keySet()) {
                outputFilenames.put(outputId, outputFiles.get(outputId).get("file-name"));
            }
            return outputFilenames;
        } catch (IOException e) {
            logger.error("Error reading a provenance file : {}", provenanceFilePath);
            throw new BusinessException("Error reading a provenance file", e);
        }
    }

    public void deleteReproVipDirectory(String executionID) throws BusinessException {
        Path reproVipDir = Paths.get(server.getReproVIPRootDir()).resolve(executionID);
        logger.info("Deleting ReproVip directory: {}", reproVipDir);

        if (Files.exists(reproVipDir)) {
            try {
                boolean isDeleteOk = Files.walk(reproVipDir)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .allMatch(f -> {
                            if ( ! f.delete()) {
                                logger.error("Error deleting file {} for reprovip dir {}", f, reproVipDir);
                                return false;
                            }
                            return true;
                        }); // this will stop if any delete returns false (and is in error)
                if ( ! isDeleteOk) {
                    throw new BusinessException("Error deleting a reprovip dir");
                }
            } catch (IOException e) {
                logger.error("Error deleting directory {}", reproVipDir, e);
                throw new BusinessException("Error deleting a reprovip dir", e);
            }
        } else {
            logger.info("ReproVip directory does not exist: {}", reproVipDir);
        }
    }
}
