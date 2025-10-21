package fr.insalyon.creatis.vip.application.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

import fr.insalyon.creatis.vip.core.models.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PublicExecution implements IsSerializable {

    public final static String SEPARATOR = ", ";

    private String experienceName;
    private List<WorkflowData> workflowsData; // struct -> workflowId, appName, appVersion
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
    public PublicExecution(List<WorkflowData> workflowsData) {
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

    public PublicExecution(String experienceName, List<WorkflowData> workflowsData,
            PublicExecutionStatus status, String author, String comments, List<String> outputIds, String doi) {
        this.experienceName = experienceName;
        this.workflowsData = workflowsData;
        this.status = status;
        this.author = author;
        this.comments = comments;
        this.outputIds = outputIds;
        this.doi = doi;

        // to ensure that workflowIds are alphabetical ordered
        this.workflowsData.sort(Comparator.comparing(WorkflowData::getWorkflowId));
    }

    public void setStatus(PublicExecutionStatus status) { this.status = status; }
    public void setDoi(String doi) { this.doi = doi; }

    public String getExperienceName() { return experienceName; }
    public String getAuthor() { return author; }
    public String getComments() { return comments; }
    public String getDoi() { return doi; }
    public PublicExecutionStatus getStatus() { return status; }
    public List<String> getOutputIds() { return outputIds; }
    public List<WorkflowData> getWorkflowsData() { return workflowsData; }

    public List<String> getWorkflowsIds() {
        return workflowsData.stream().map((w) -> w.getWorkflowId()).collect(Collectors.toList());
    }

    public List<String> getApplicationsNames() {
        return workflowsData.stream().map((w) -> w.getAppName()).collect(Collectors.toList());
    }

    public List<String> getApplicationsVersions() {
        return workflowsData.stream().map((w) -> w.getAppVersion()).collect(Collectors.toList());
    }

    /**
     * Map(workflow-id, List(outputs))
     */
    public Map<String, List<String>> getMappedOutputIds() {
        Map<String, List<String>> result = new HashMap<>();
        String workflowId, outputId;
        int startIndex;

        for (String output : outputIds) {
            // splitting workflow-xxxxx-outname
            if ( ! output.isEmpty()) {
                startIndex = output.indexOf('-', 0) + 1;
                workflowId = output.substring(0, output.indexOf('-', startIndex));
                outputId = output.substring(output.indexOf('-', startIndex) + 1);
    
                if (result.get(workflowId) != null) {
                    result.get(workflowId).add(outputId);
                } else {
                    result.put(workflowId, new ArrayList<>(Arrays.asList(outputId)));
                }
            }
        }
        return result;
    }

    public List<Pair<String, String>> getAppsAndVersions() {
        return IntStream.range(0, getWorkflowsData().size())
            .boxed()
            .map(i -> new Pair<>(
                getApplicationsNames().get(i),
                getApplicationsVersions().get(i)
            ))
            .collect(Collectors.toList());
    } 

    private static List<WorkflowData> getWorkflowsDataFromStrings(String workflowsIds, String applicationsNames, String applicationsVersions) {
        List<WorkflowData> results = new ArrayList<>();
        String[] ids = workflowsIds.split(SEPARATOR, -1);
        String[] names = applicationsNames.split(SEPARATOR, -1);
        String[] versions = applicationsVersions.split(SEPARATOR, -1);

        if (ids.length == names.length && ids.length == versions.length) {
            for (int i = 0; i < ids.length; i++) {
                results.add(new WorkflowData(ids[i], names[i], versions[i]));
            }
        } else {
            throw new IllegalArgumentException("Each string must have the same number of separators !");
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

