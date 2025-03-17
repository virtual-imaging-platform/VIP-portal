package fr.insalyon.creatis.vip.application.server.business;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.InputM2Parser;
import fr.insalyon.creatis.vip.application.server.business.util.ReproVipUtils;
import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.core.client.bean.Triplet;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;
import fr.insalyon.creatis.vip.datamanager.server.business.LfcPathsBusiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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

    private final PublicExecutionBusiness publicExecutionBusiness;
    private final ApplicationBusiness applicationBusiness;
    private final Server server;
    private final SimulationBusiness simulationBusiness;
    private final WorkflowBusiness workflowBusiness;
    private final LfcPathsBusiness lfcPathsBusiness;
    private final ExternalPlatformBusiness externalPlatformBusiness;

    @Autowired
    public ReproVipBusiness(ApplicationBusiness applicationBusiness, Server server, SimulationBusiness simulationBusiness,
            WorkflowBusiness workflowBusiness, LfcPathsBusiness lfcPathsBusiness, ExternalPlatformBusiness externalPlatformBusiness,
            PublicExecutionBusiness publicExecutionBusiness) {
        this.applicationBusiness = applicationBusiness;
        this.server = server;
        this.simulationBusiness = simulationBusiness;
        this.workflowBusiness = workflowBusiness;
        this.lfcPathsBusiness = lfcPathsBusiness;
        this.externalPlatformBusiness = externalPlatformBusiness;
        this.publicExecutionBusiness = publicExecutionBusiness;
    }

    public boolean canMakeExecutionPublic(List<String> workflowIds) throws BusinessException {
        for (String workflow : workflowIds) {
            // looking for provenance directory
            Path provenanceDirPath = Paths.get(server.getWorkflowsPath() + "/" + workflow + "/provenance");

            if ( ! Files.exists(provenanceDirPath)) {
                 return false;
            }
            // checking if it is empty
            if (provenanceDirPath.toFile().listFiles().length == 0) {
                return false;
            }
            // verifying the application has a boutiques file
            Simulation simulation = workflowBusiness.getSimulation(workflow);

            if (getBoutiquesDescriptorJsonPath(simulation.getApplicationName(), simulation.getApplicationVersion()) == null) {
                return false;
            }
        }
        return true;
    }

    public String createReproVipDirectory(String experienceName) throws BusinessException {
        PublicExecution publicExecution = publicExecutionBusiness.get(experienceName);
        Path reproVipDir = Paths.get(server.getReproVIPRootDir()).resolve(experienceName);

        logger.info("Creating reprovip dir : {}", reproVipDir);
        try {
            if ( ! Files.exists(reproVipDir)) {
                Files.createDirectories(reproVipDir);
            }
        } catch (IOException e) {
            logger.error("Exception creating the a reprovip directory {}", reproVipDir, e);
            throw new BusinessException("Error creating a reprovip directory", e);
        }

        copyReadme(reproVipDir);
        return generateReprovipJson(reproVipDir, publicExecution);
    }

    public List<Path> copyProvenanceFiles(Path reproVipDir, String executionID) throws BusinessException {
        try {
            List<Path> copiedProvenanceFiles = new ArrayList<>();
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

    private Map<String, List<String>> getInvocationsOutputs(String executionID, List<Path> provenanceFiles, List<String> outputIds) throws BusinessException, DAOException {
        Map<String, List<String>> invocationsOutputs = new HashMap<>();

        for (Path provenanceFile : provenanceFiles) {
            logger.debug("handling provenance file {}", provenanceFile);

            String fileName = provenanceFile.getFileName().toString();
            String invocationFilename = fileName.substring(0, fileName.indexOf(".sh.provenance.json"));
            List<String> outputDataPaths = simulationBusiness.getSimulationDAO(executionID).getOutputData(invocationFilename);
            Map<String, String> provenanceOutputFilenames = getOutputFilenamesFromProvenanceFile(provenanceFile);
            List<String> outputDataPathToKeep = getOutputPathToDownload(outputDataPaths, provenanceOutputFilenames, outputIds);

            invocationsOutputs.put(invocationFilename, outputDataPathToKeep);
        }
        return invocationsOutputs;
    }

    private Map<String, Object> formatWorkflowData(Path reproVipDir, Triplet<String, String, String> workflowData, PublicExecution execution)
            throws BusinessException, DAOException {
        Map<String, Object> data = new HashMap<>();
        List<Path> provenancesFiles = copyProvenanceFiles(reproVipDir, workflowData.getFirst());
        List<String> outputIds = execution.getMappedOutputIds().getOrDefault(workflowData.getFirst(), Collections.emptyList());

        // we convert path to string because if there are some caracters like " " by default it encodes it
        data.put("workflowId", workflowData.getFirst());
        data.put("directory", reproVipDir.resolve(workflowData.getFirst()).toString());
        data.put("boutique_descriptor", getBoutiquesDescriptorJsonPath(workflowData.getSecond(), workflowData.getThird()));
        data.put("provenances_files", provenancesFiles.stream().map(Path::toString).collect(Collectors.toList()));
        data.put("invocation_outputs", getInvocationsOutputs(workflowData.getFirst(), provenancesFiles, outputIds));

        return data;
    }

    public String generateReprovipJson(Path reproVipDir, PublicExecution publicExecution)
            throws BusinessException {
        Map<String, Object> structuredJson = new LinkedHashMap<>();
        Map<String, Object> metadata = new LinkedHashMap<>();
        List<Map<String, Object>> workflowsData = new ArrayList<>();

        metadata.put("title", "your title");
        metadata.put("upload_type", "workflow");
        metadata.put("description", publicExecution.getComments());
        metadata.put("creators", List.of(Map.of("name", publicExecution.getAuthor())));

        try {
            for (var data : publicExecution.getWorkflowsData()) {
                workflowsData.add(formatWorkflowData(reproVipDir, data, publicExecution));
                generateWorkflowInputJson(data.getFirst(), publicExecution.getAuthor(), reproVipDir);
            }
        } catch (DAOException e) {
            throw new BusinessException(e);
        }

        structuredJson.put("metadata", metadata);
        structuredJson.put("workflows", workflowsData);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonContent = objectMapper.writeValueAsString(structuredJson);
            Path reprovipJsonPath = reproVipDir.resolve("summary.json");
            Files.writeString(reprovipJsonPath, jsonContent);

            return jsonContent;
        } catch (IOException e) {
            logger.error("Error saving reprovip metadata file for {}", publicExecution.getExperienceName(), e);
            throw new BusinessException("Failed to save JSON to file", e);
        }
    }

    public void generateWorkflowInputJson(String workflowId, String author, Path reproVipDir) throws BusinessException {
        ObjectMapper mapper = new ObjectMapper();
        ReproVipUtils utils = new ReproVipUtils(externalPlatformBusiness, server.getHostURL());
        InputM2Parser parser = new InputM2Parser(author);
        parser.setLfcPathsBusiness(lfcPathsBusiness);

        Map<String, String> inputs = parser.parse(server.getWorkflowsPath() + "/" + workflowId + "/inputs.xml");
        Map<String, Object> json = new HashMap<>();

        utils.parse(inputs);
        json.put("provider", utils.getProviderInformations());
        json.put("inputs", utils.getSimplifiedInputs());

        try {
            Files.writeString(reproVipDir.resolve(workflowId + ".json"), mapper.writeValueAsString(json));
        } catch (IOException e) {
            logger.error("Error saving reprovip inputs file for {}", workflowId, e);
            throw new BusinessException("Failed to save inputs JSON to file", e);
        }
    }

    public void copyReadme(Path reproVipDir) throws BusinessException {
        try {
            Resource resource = new ClassPathResource("repro_vip.md");

            Files.copy(resource.getInputStream(), reproVipDir.resolve("README.md"));
        } catch (IOException e) {
            throw new BusinessException("Cannot copy the README.md", e);
        }
    }

    public String getBoutiquesDescriptorJsonPath(String applicationName, String applicationVersion) throws BusinessException {
        AppVersion appVersion = applicationBusiness.getVersion(applicationName, applicationVersion);

        if (appVersion != null && appVersion.getJsonLfn() != null) {
            return appVersion.getJsonLfn();
        } else {
            return null;
        }
    }

    /*
     * outputDataPaths is the list of all the results path/URIs produced for a job
     * provenanceOutputFilenames is the mapping between the output id and their filename
     */
    private List<String> getOutputPathToDownload(List<String> outputDataPaths, Map<String, String> provenanceOutputFilenames,
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
            logger.debug("[ReproVIP] Handling outputDataPath {}", outputDataPath);
            try {
                    URI uri = new URI(outputDataPath);
                    String filename = Paths.get(uri.getPath()).getFileName().toString();
                    if (outputDataMap.containsKey(filename)) {
                        logger.error("A job contains two results with the same filename [{}]", filename);
                        throw new BusinessException("A job contains two results with the same filename");
                    }
                    logger.debug("[ReproVIP] got filename for {} : {}", outputDataPath, filename);
                    outputDataMap.put(filename, outputDataPath);
            } catch (URISyntaxException e) {
                logger.error("Cannot convert a job result to URI [{}]", outputDataPath, e);
                throw new BusinessException("Error converting a job result to URI", e);
            }
        }
        return outputDataMap;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getOutputFilenamesFromProvenanceFile(Path provenanceFilePath) throws BusinessException {
        try {
            Map<?, ?> map = new ObjectMapper().readValue(provenanceFilePath.toFile(), Map.class);
            Map<String, ?> publicOutputSection = (Map<String, ?>) map.get("public-output");
            Map<String, Map<String,String>> outputFiles = (Map<String, Map<String, String>>) publicOutputSection.get("output-files");
            Map<String, String> outputFilenames = new HashMap<>();

            for (String outputId : outputFiles.keySet()) {
                outputFilenames.put(outputId, outputFiles.get(outputId).get("file-name"));
                logger.debug("got {} as filename for {} in {}", outputFilenames.get(outputId), outputId, provenanceFilePath);
            }
            return outputFilenames;

        } catch (IOException e) {
            logger.error("Error reading a provenance file : {}", provenanceFilePath);
            throw new BusinessException("Error reading a provenance file", e);
        }
    }

    public void deleteReproVipDirectory(String experienceName) throws BusinessException {
        Path reproVipDir = Paths.get(server.getReproVIPRootDir()).resolve(experienceName);
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
