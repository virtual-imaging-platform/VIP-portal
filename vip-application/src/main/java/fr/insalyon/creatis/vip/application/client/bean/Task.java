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
package fr.insalyon.creatis.vip.application.client.bean;

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
    private String exitMessage;
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
                int exitCode, String exitMessage, String siteName, String nodeName, int minorStatus,
                String... parameters) {

        this.id = id;
        this.invocationID= invocationID;
        this.creationDate = creationDate;
        this.status = status;
        this.command = command;
        this.fileName = fileName;
        this.exitCode = exitCode;
        this.exitMessage = exitMessage;
        this.siteName = siteName;
        this.nodeName = nodeName;
        this.parameters = parameters;
        this.minorStatus = minorStatus;
    }

    public enum GaswExitCode {

        SUCCESS(0, "Successfully executed"),
        ERROR_READ_GRID(1, "Error during download file from grid using lcg-cp"),
        ERROR_WRITE_GRID(2, "Error during write file to grid using lcg-cr"),
        ERROR_WRITE_LOCAL(7, "Error during create execution directory"),
        ERROR_GET_STD(8, "Error during download stderr/out of the application from grid"),
        ERROR_FILE_NOT_FOUND(3, "Error during match result files"),
        EXECUTION_FAILED(6, "Execution failed"),
        EXECUTION_CANCELED(9, "Execution canceled"),
        EXECUTION_STALLED(10, "Execution stalled (used in DIRAC execution)"),
        UNDEFINED(-1, "Undefined exit code"),
        GIRDER_NOT_FOUND(11, "Girder-client not in PATH, and an error occured while trying to install it."),
        FAILED_CREATE_DIR(12, "Failed to create directory"),
        TOO_MANY_NIFTI(13, "Too many or none nifti file (.nii or .nii.gz) in shanoir zip, supporting only 1"),
        BOUTIQUE_INSTALL_FAILED(14, "Boutique installation failed"),
        CONFIG_NOT_FOUND(15, "Configuration file not found"),
        ERROR_WRITE_LOCAL_(20, "Unable to create directory"),
        ERROR_WRITE_GRID_(21, "Cannot copy file to lfn"),
        ERROR_MV_FILE(22, "Error while moving result local file"),
        ERROR_RESULT_FILE_EXIST(23, "Result file already exists"),
        ERROR_UPLOAD_GIRDER(24, "Error while uploading girder file"),
        ERROR_UPLOAD_FILE(25, "Error while uploading file"),
        ERROR_DL(30, "Download was not successful"),
        EXECUTION_FAILED_(40, "Execution was not successful"),
        EXECUTION_CANCELED_(41, "Execution canceled"),
        EXECUTION_STALLED_(42, "Execution stalled"),
        INVALID_IMAGE_NAME(43, "Invalid image name"),
        IMAGE_NOT_FOUND(44, "Image file not found"),
        INVALID_CONTAINER_RUNTIME(45, "Invalid container runtime"),
        TOKEN_REFRESH_TOO_LONG(50, "Token refreshing is taking too long. Aborting the process."),
        TOKEN_DL_FAILED(51, "3 failures at downloading, stop trying and stop the job"),
        TOKEN_REFRESH_ERROR(52, "Error while refreshing the token"),
        UNDEFINED_(90, "Undefined error");
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

    public String getExitMessage() { return exitMessage; }

    public void setExitMessage(String exitMessage) { this.exitMessage = exitMessage; }
}
