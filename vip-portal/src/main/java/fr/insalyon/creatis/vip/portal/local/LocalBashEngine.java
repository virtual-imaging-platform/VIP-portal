package fr.insalyon.creatis.vip.portal.local;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Profile("local")
public class LocalBashEngine {

    @Value("${local.workflow.dir}")
    private String workflowsDir;

    private GridaClientLocal gridaClient;

    private Map<String, LocalBashExecution> executionsInfo;
    private Map<String, Future<?>> executionsFutures;
    private ExecutorService executorService;

    @Autowired
    public LocalBashEngine(
            GridaClientLocal gridaClient,
            @Value("local.engine.threadNb") Integer localThreadNb) {
        this.gridaClient = gridaClient;
        this.executorService = Executors.newFixedThreadPool(localThreadNb);
    }

    public String launch(File workflowFile, List<ParameterSweep> parameters)  {
        try {
            LocalBashExecution newExecution =
                    createExecution(workflowFile, parameters);
            String execId = newExecution.id;
            executionsInfo.put(execId, newExecution);
            newExecution.status = SimulationStatus.Queued;
            Future<?> executionFuture =
                    executorService.submit(new LocalExecutionRun(newExecution));
            executionsFutures.put(execId, executionFuture);
            return execId;
        } catch (IOException e) {
            throw new RuntimeException("Cannot launch local execution", e);
        }
    }

    public SimulationStatus getStatus(String workflowID) {
        Future<?> execFuture = executionsFutures.get(workflowID);
        if (execFuture.isCancelled()) {
            return SimulationStatus.Killed;
        }
        if (execFuture.isDone()) {
            return SimulationStatus.Completed;
        }
        return executionsInfo.get(workflowID).status;
    }

    public void kill(String workflowID) {
        Future<?> execFuture = executionsFutures.get(workflowID);
        execFuture.cancel(true);
    }

    private LocalBashExecution createExecution(File workflowFile, List<ParameterSweep> parameters) throws IOException {
        LocalBashExecution exec = new LocalBashExecution();
        exec.workflowFile = workflowFile;
        exec.id = createWorkflowId();
        exec.workflowDir = createWorkflowDir(exec.id);
        exec.gwendiaInputs = getGwendiaInputs(workflowFile);
        exec.execInputs = getExecInputs(parameters);
        return exec;
    }

    private static class LocalBashExecution {
        String id;
        File workflowFile;
        Path workflowDir;
        Map<String,String> execInputs;     // name -> value
        Map<String,String> gwendiaInputs;  // name -> type (string/URI)
        SimulationStatus status = SimulationStatus.Unknown;
    }

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HHmmss.SSS");
    private String createWorkflowId() throws IOException {
        String dateString = DATE_FORMAT.format(new Date());
        return "workflow-local-" + dateString;
    }

    private Path createWorkflowDir(String execId) throws IOException {
        Path dir = Paths.get(workflowsDir).resolve(execId);
        Files.createDirectory(dir);
        return dir;
    }

    private Map<String,String> getGwendiaInputs(File workflowFile) throws IOException {
        // <in name="results-directory" type="string/URI"
        Pattern pattern = Pattern.compile("\\s*<in\\s+name=\"([\\w-]+)\"\\s+type=\"([\\w-]+)\"\\s*");
        return Files.lines(workflowFile.toPath())
                .map(line -> pattern.matcher(line))
                .filter(matcher -> matcher.find())
                .collect(Collectors.toMap(matcher -> matcher.group(1), matcher -> matcher.group(2)));
    }

    private  Map<String,String> getExecInputs(List<ParameterSweep> parameters) {
        if (parameters.stream()
                .anyMatch(param -> param.getValues().size() != 1)) {
            throw new RuntimeException("There must be exactly 1 value for all paramenters");
        }
        return parameters.stream().collect(Collectors.toMap(
                param -> param.getParameterName(),
                param -> param.getValues().get(0)
        ));
    }
    
    /*
    TODO :
    - handle append-date
    - create jobs h2 table
    - support boutiques jobs
    */
    private class LocalExecutionRun implements Runnable {

        LocalBashExecution exec;

        public LocalExecutionRun(LocalBashExecution exec) {
            this.exec = exec;
        }

        @Override
        public void run() {
            try {
                exec.status = SimulationStatus.Running;
                transferInputFiles();

            } catch (IOException | GRIDAClientException e) {
                throw new RuntimeException("Error running local execution", e);
            }
        }

        private void transferInputFiles() throws IOException, GRIDAClientException {
            // find URI inputs
            Path to = exec.workflowDir.resolve("inputs");
            Files.createDirectory(to);
            for (String name : getFileInputName()) {
                Path from = Paths.get(exec.execInputs.get(name));
                gridaClient.getRemoteFile(from.toString(), to.toString());
            }
        }

        private Set<String> getFileInputName() {
            // find URI inputs
            return exec.gwendiaInputs.entrySet().stream()
                    .filter(x -> x.getValue().equals("URI"))
                    .map(x -> x.getKey())
                    .collect(Collectors.toSet());
        }
    }
}
