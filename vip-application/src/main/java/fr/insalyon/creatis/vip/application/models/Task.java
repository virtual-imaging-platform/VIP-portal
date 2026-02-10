package fr.insalyon.creatis.vip.application.models;

import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;

import java.util.Date;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class Task implements IsSerializable {

    private String id;
    private int invocationID;
    private Date creationDate;
    private TaskStatus status;
    private int exitCode;
    private String siteName;
    private String nodeName;
    private String command;
    private String fileName;
    private String[] parameters;
    private int minorStatus;
    private int jobID;

    public Task() {
    }

    public Task(int jobID, TaskStatus status, String command) {

        this.jobID = jobID;
        this.status = status;
        this.command = command;
    }

    public Task(String id, int invocationID, Date creationDate, TaskStatus status, String command, String fileName,
                int exitCode, String siteName, String nodeName, int minorStatus,
                String... parameters) {

        this.id = id;
        this.invocationID= invocationID;
        this.creationDate = creationDate;
        this.status = status;
        this.command = command;
        this.fileName = fileName;
        this.exitCode = exitCode;
        this.siteName = siteName;
        this.nodeName = nodeName;
        this.parameters = parameters;
        this.minorStatus = minorStatus;
    }

    public enum GaswExitCode {

        SUCCESS(0, "Successfully executed"),
        ERROR_READ_GRID(1, "Error downloading a file"),
        ERROR_WRITE_GRID(2, "Error uploading a result file"),
        ERROR_WRITE_LOCAL(7, "Error writing on local disk"),
        ERROR_GET_STD(8, "Error downloading the execution logs"),
        ERROR_FILE_NOT_FOUND(3, "Error: File not found"),
        EXECUTION_FAILED_LEGACY(6, "Execution failed"),
        EXECUTION_CANCELED(9, "Execution canceled"),
        EXECUTION_STALLED(10, "Execution stalled"),
        UNDEFINED(-1, "Exit code not known"),
        GIRDER_CLIENT_INSTALL_FAILED(20, "Error installing girder client"),
        BOUTIQUE_INSTALL_FAILED(21, "Error installing boutiques"),
        ERROR_CREATE_EXEC_DIR(22, "Error: Unable to create the execution directory"),
        ERROR_CD_EXEC_DIR(23, "Error: Unable to use the execution directory"),
        CONFIG_NOT_FOUND(24, "Error: Execution configuration file not found"),
        BOUTIQUE_IMPORT_FAILED(25, "Error: Import boutiques fails"),
        ERROR_WRITE_LFN(30, "Error uploading a result file to lfn"),
        ERROR_MV_FILE(31, "Error while moving a local result file"),
        ERROR_RESULT_FILE_EXIST(32, "Error : A local result file already exists"),
        ERROR_UPLOAD_GIRDER(33, "Error uploading a result file to girder"),
        ERROR_UPLOAD_SHANOIR(34, "Error uploading a result file to shanoir"),
        FAILED_CREATE_LOCAL_UPLOAD_DIR(35, "Error creating a local result directory"),
        ERROR_GIRDER_MKDIR(36, "Error while checking or creating a girder directory for results"),
        ERROR_SHANOIR_UPLOAD_SUBDIR(37, "Error: Unsupported subdirectory for shanoir results"),
        ERROR_DL(40, "Download error"),
        INVALID_SHANOIR_NIFTI(41, "Too many or none nifti file (.nii or .nii.gz) in shanoir zip, supporting only 1"),
        SHANOIR_DL_FAILED(42, "Error downloading a shanoir file"),
        EXECUTION_FAILED(50, "Execution was not successful"),
        SING_INVALID_IMAGE_NAME(51, "Invalid singularity/apptainer image name"),
        SING_IMAGE_NOT_FOUND(52, "Error: Singularity/apptainer image not found"),
        INVALID_CONTAINER_RUNTIME(53, "Invalid container runtime (only docker/singularity)"),
        TOKEN_REFRESH_TOO_LONG(60, "Error: Timeout while refreshing an OIDC token."),
        TOKEN_REFRESH_ERROR(61, "Error while refreshing an OIDC token");

        private int exitCode;
        private String message;

        private GaswExitCode(int exitCode, String message) {
            this.exitCode = exitCode;
            this.message = message;
        }

        public String getMessage() { return message; }

        public int getExitCode() {
            return this.exitCode;
        }

        public static GaswExitCode fromCode(int code) {
            for (GaswExitCode value : values()) {
                if (value.exitCode == code) {
                    return value;
                }
            }
            return UNDEFINED; // fallback if unknown code
        }
    }


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public int getMinorStatus() {
        return minorStatus;
    }

    public void setMinorStatus(int minorStatus) {
        this.minorStatus = minorStatus;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public int getInvocationID() {
        return invocationID;
    }

    public void setInvocationID(int jobID) {
        this.invocationID = invocationID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(int jobID) {
        this.creationDate = creationDate;
    }

    public String getExitMessage() { return GaswExitCode.fromCode(this.exitCode).getMessage(); }
}
