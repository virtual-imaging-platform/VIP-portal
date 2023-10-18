package fr.insalyon.creatis.vip.application.server.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReproVipBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ConfigurationBusiness configurationBusiness;
    @Autowired
    private WorkflowBusiness workflowBusiness;
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

        List<InOutData> outputData = workflowBusiness.getOutputData(executionID, currentUser.getFolder(), false);
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

    public String createJsonOutputData(String executionID, User currentUser)
            throws ApplicationException, BusinessException {
        ExecutionInOutData inOutData = executionOutputData(executionID, currentUser);
        //ExecutionJobTaskData jobTaskData = getExecutionJobTaskData(executionID);

        //Map<String, Object> combinedData = new HashMap<>();
        //combinedData.put("inOutData", inOutData);
        //combinedData.put("jobs", jobTaskData.getJobs());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(inOutData);
            //saveJsonToFile(json, executionID);
            logger.info(json);
            return json;
        } catch (JsonProcessingException e) {
            throw new ApplicationException(ApplicationException.ApplicationError.valueOf("Failed to convert Output to JSON"), e);
        }
    }

    public void saveJsonToFile(String jsonContent, String executionID) throws IOException {
        String filePath = server.getWorkflowsPath() + "/" + executionID + "/inOutPut.json";
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonContent);
        }
    }
    public void createReproVipDirectory(String executionName, String executionID, String version, User currentUser) {
        try {
            logger.info("Attempting to create ReproVip directory...");
            logger.info("Version de l'app : " + version);

            String reproVipDirPath = "/vip/ReproVip";
            File reproVipDir = new File(reproVipDirPath);

            if (!reproVipDir.exists()) {
                if (reproVipDir.mkdirs()) {
                    logger.info("ReproVip directory successfully created");
                } else {
                    logger.error("Error creating ReproVip directory");
                }
            }

            List<InOutData> outputData = workflowBusiness.getOutputData(executionID, currentUser.getFolder(), true);
            logger.info(outputData.get(0).getPath());

            if (outputData != null && !outputData.isEmpty()) {
                String outputPath = outputData.get(0).getPath();
                if (outputPath != null) {
                    File outputFile = new File(outputPath);
                    if (outputFile.exists()) {
                        Files.copy(outputFile.toPath(), new File(reproVipDir, outputFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        logger.info("Output file {} successfully copied to ReproVip directory", outputPath);
                    } else {
                        logger.warn("Output file {} not found", outputPath);
                    }
                } else {
                    logger.warn("No output path found for executionID: {}", executionID);
                }
            } else {
                logger.warn("No output data found for executionID: {}", executionID);
            }

            String workflowPath = String.valueOf(workflowBusiness.getRawApplicationDescriptorPath(currentUser, executionName, version));
            logger.info(workflowPath);
            File workflowFile = new File(workflowPath);
            if (workflowFile.exists()) {
                Files.copy(workflowFile.toPath(), new File(reproVipDir, workflowFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.info("Workflow file successfully copied to ReproVip directory");
            } else {
                logger.warn("Workflow file not found");
            }

            logger.info("Workflows path: " + server.getWorkflowsPath());

            String provenanceDirPath = server.getWorkflowsPath() + "/" + executionID + "/provenance";
            File provenanceDir = new File(provenanceDirPath);
            if (!provenanceDir.exists()) {
                logger.warn("Provenance directory does not exist: " + provenanceDirPath);
                return;
            }

            File[] allFiles = provenanceDir.listFiles();
            if (allFiles != null) {
                for (File file : allFiles) {
                    logger.info("Found file in provenance directory: " + file.getName());
                }
            }
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".sh.provenance.json");
                }
            };

            File[] matchingFiles = provenanceDir.listFiles(filter);

            if (matchingFiles != null && matchingFiles.length > 0) {
                File provenanceFile = matchingFiles[0];
                if (provenanceFile.exists()) {
                    Files.copy(provenanceFile.toPath(), new File(reproVipDir, provenanceFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    logger.info(provenanceFile.getName() + " file successfully copied to ReproVip directory");
                } else {
                    logger.warn(provenanceFile.getName() + " file not found");
                }
            } else {
                logger.warn("No matching provenance file found");
            }

        } catch (IOException e) {
            logger.error("Exception while copying files to ReproVip directory", e);
            throw new RuntimeException(e);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
    }
}
