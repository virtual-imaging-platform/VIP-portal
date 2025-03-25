package fr.insalyon.creatis.vip.core.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PublicExecution implements IsSerializable {

    public final static String SEPARATOR = ", ";

    private String experienceName;
    private List<Triplet<String, String, String>> workflowsData; // struct -> workflowId, appName, appVersion
    private PublicExecutionStatus status;
    private String author;
    private String comments;
    private String doi;
    private List<String> outputIds;

    public enum PublicExecutionStatus {
        REQUESTED,
        DIRECTORY_CREATED,
        PUBLISHED;
    }
    public PublicExecution() {}

    /**
     * Only use this constructor for parsing workflowsData and having access to 
     * getWorkflowsIds, getApplicationsNames, getApplicationsVersions
     */
    public PublicExecution(List<Triplet<String, String, String>> workflowsData) {
        this.workflowsData = workflowsData;
    }

    /**
     * This constructor supposed that workflowsIds, applicationsNames and applicationsVersions are strings
     * that represent the list with SEPARATOR as delimiter.
     * Each string must be aligned to others ones (in the same order).
     * Outputnames must also be a list represented as a String with SEPARATOR as delimiter.
     */
    public PublicExecution(String experienceName, String workflowsIds, String applicationsNames, String applicationsVersions,
            PublicExecutionStatus status, String author, String comments, String outputIds, String doi) {
        this(experienceName, getWorkflowsDataFromStrings(workflowsIds, applicationsNames, applicationsVersions),
            status, author, comments, Arrays.asList(outputIds.split(SEPARATOR, -1)), doi);
    }

    public PublicExecution(String experienceName, List<Triplet<String, String, String>> workflowsData,
            PublicExecutionStatus status, String author, String comments, List<String> outputIds, String doi) {
        this.experienceName = experienceName;
        this.workflowsData = workflowsData;
        this.status = status;
        this.author = author;
        this.comments = comments;
        this.outputIds = outputIds;
        this.doi = doi;

        // to ensure that workflowIds are alphabetical ordered
        this.workflowsData.sort(Comparator.comparing(Triplet::getFirst));
    }

    public void setStatus(PublicExecutionStatus status) { this.status = status; }
    public void setDoi(String doi) { this.doi = doi; }

    public String getExperienceName() { return experienceName; }
    public String getAuthor() { return author; }
    public String getComments() { return comments; }
    public String getDoi() { return doi; }
    public PublicExecutionStatus getStatus() { return status; }
    public List<String> getOutputIds() { return outputIds; }
    public List<Triplet<String, String, String>> getWorkflowsData() { return workflowsData; }

    public List<String> getWorkflowsIds() {
        return workflowsData.stream().map((w) -> w.getFirst()).collect(Collectors.toList());
    }

    public List<String> getApplicationsNames() {
        return workflowsData.stream().map((w) -> w.getSecond()).collect(Collectors.toList());
    }

    public List<String> getApplicationsVersions() {
        return workflowsData.stream().map((w) -> w.getThird()).collect(Collectors.toList());
    }

    /**
     * Map(workflow-id, List(outputs))
     */
    public Map<String, List<String>> getMappedOutputIds() {
        Map<String, List<String>> result = new HashMap<>();
        String workflowId, outputId;

        for (String output : outputIds) {
            // splitting workflow-xxxxx-outname
            workflowId = output.substring(0, output.indexOf('-', 10));
            outputId = output.substring(output.indexOf('-', 10) + 1);

            if (result.get(workflowId) != null) {
                result.get(workflowId).add(outputId);
            } else {
                result.put(workflowId, new ArrayList<>(Arrays.asList(outputId)));
            }
        }
        return result;
    }

    private static List<Triplet<String, String, String>> getWorkflowsDataFromStrings(String workflowsIds, String applicationsNames, String applicationsVersions) {
        List<Triplet<String, String, String>> results = new ArrayList<>();
        String[] ids = workflowsIds.split(SEPARATOR, -1);
        String[] names = applicationsNames.split(SEPARATOR, -1);
        String[] versions = applicationsVersions.split(SEPARATOR, -1);

        if (ids.length == names.length && ids.length == versions.length) {
            for (int i = 0; i < ids.length; i++) {
                results.add(new Triplet<>(ids[i], names[i], versions[i]));
            }
        }
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublicExecution that = (PublicExecution) o;

        return Objects.equals(experienceName, that.experienceName) &&
               Objects.equals(workflowsData, that.workflowsData) &&
               status == that.status &&
               Objects.equals(author, that.author) &&
               Objects.equals(comments, that.comments) &&
               Objects.equals(doi, that.doi) &&
               Objects.equals(outputIds, that.outputIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(experienceName, workflowsData, status, author, comments, doi, outputIds);
    }
}

