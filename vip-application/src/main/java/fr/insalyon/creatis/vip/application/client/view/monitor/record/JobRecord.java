package fr.insalyon.creatis.vip.application.client.view.monitor.record;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobRecord extends ListGridRecord {

    public JobRecord() {
    }

    public JobRecord(String jobID, String status, String command, String fileName,
            int exitCode, String siteName, String nodeName, String[] parameters,
            int minorStatus) {

        setAttribute("jobID", jobID);
        setAttribute("status", status);
        setAttribute("command", command);
        setAttribute("fileName", fileName);
        setAttribute("exitCode", exitCode);
        setAttribute("siteName", siteName);
        setAttribute("nodeName", nodeName);
        StringBuilder sb = new StringBuilder();
        for (String p : parameters) {
            if (!p.isEmpty()) {
                sb.append("<br />");
            }
            sb.append(p);
        }
        setAttribute("parameters", sb.toString());
        setMinorStatus(status, exitCode, minorStatus);
    }

    public String getCommand() {
        return getAttributeAsString("command");
    }

    public String getExitCode() {
        return getAttributeAsString("exitCode");
    }

    public String getFileName() {
        return getAttributeAsString("fileName");
    }

    public String getID() {
        return getAttributeAsString("jobID");
    }

    public String getNodeName() {
        return getAttributeAsString("nodeName");
    }

    public String getSiteName() {
        return getAttributeAsString("siteName");
    }

    public String getStatus() {
        return getAttributeAsString("status");
    }

    public String getMinorStatus() {
        return getAttributeAsString("minorStatus");
    }

    public String getParameters() {
        return getAttributeAsString("parameters");
    }

    private void setMinorStatus(String status, int exitCode, int minorStatus) {
        if (status.equals("COMPLETED") || status.equals("ERROR")) {
            switch (exitCode) {
                case -1:
                    setAttribute("minorStatus", "Retrieving Status");
                    break;
                case 0:
                    setAttribute("minorStatus", "Execution Completed");
                    break;
                case 1:
                    setAttribute("minorStatus", "Inputs Download Failed");
                    break;
                case 2:
                    setAttribute("minorStatus", "Outputs Upload Failed");
                    break;
                case 6:
                    setAttribute("minorStatus", "Application Execution Failed");
                    break;
                case 7:
                    setAttribute("minorStatus", "Directories Creation Failed");
                    break;
                default:
                    setAttribute("minorStatus", "");
            }

        } else {
            switch (minorStatus) {
                case 1:
                    setAttribute("minorStatus", "Job Set Up");
                    break;
                case 2:
                    setAttribute("minorStatus", "Downloading Background Script");
                    break;
                case 3:
                    setAttribute("minorStatus", "Downloading Inputs");
                    break;
                case 4:
                    setAttribute("minorStatus", "Application Execution");
                    break;
                case 5:
                    setAttribute("minorStatus", "Uploading Results");
                    break;
                case 6:
                    setAttribute("minorStatus", "Finishing");
                    break;
                default:
                    setAttribute("minorStatus", "");
            }
        }
    }
}
