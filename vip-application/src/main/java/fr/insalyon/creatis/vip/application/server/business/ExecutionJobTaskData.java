package fr.insalyon.creatis.vip.application.server.business;


import fr.insalyon.creatis.vip.application.client.bean.Task;

import java.util.List;
import java.util.Map;

public class ExecutionJobTaskData {
    private final List<Task> jobs;
    public ExecutionJobTaskData(List<Task> jobs) {
        this.jobs = jobs;
    }
    public List<Task> getJobs() {
        return jobs;
    }
}


