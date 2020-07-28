package fr.insalyon.creatis.vip.portal.local;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Profile("local")
@Lazy
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
            @Value("${local.engine.threadNb}") Integer localThreadNb) {
        this.gridaClient = gridaClient;
        this.executorService = Executors.newFixedThreadPool(localThreadNb);
        executionsInfo = new HashMap<>();
        executionsFutures = new HashMap<>();
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
        exec.gwendiaOutputs = getGwendiaOutputs(workflowFile);
        exec.execInputs = getExecInputs(parameters);
        exec.scriptFile = getGwendiaScriptFile(workflowFile);
        return exec;
    }

    private static class LocalBashExecution {
        String id;
        File workflowFile;
        Path workflowDir;
        File scriptFile;
        Map<String,String> execInputs;     // name -> value
        Map<String,String> gwendiaInputs;  // name -> type (string/URI)
        Map<String,String> gwendiaOutputs;  // name -> type (string/URI)
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
        // <in name="results-directory" type="string/URI" />
        Pattern pattern = Pattern.compile("\\s*<in\\s+"
                + "name=\"([\\w-]+)\"\\s+"
                + "type=\"([\\w-]+)\".*/>\\s*");
        return Files.lines(workflowFile.toPath())
                .map(line -> pattern.matcher(line))
                .filter(matcher -> matcher.find())
                .collect(Collectors.toMap(matcher -> matcher.group(1), matcher -> matcher.group(2)));
    }

    private Map<String,String> getGwendiaOutputs(File workflowFile) throws IOException {
        // <out name="output" type="URI" depth="0"/>
        Pattern pattern = Pattern.compile("\\s*<out\\s+"
                + "name=\"([\\w-]+)\"\\s+"
                + "type=\"([\\w-]+)\".*/>\\s*");
        return Files.lines(workflowFile.toPath())
                .map(line -> pattern.matcher(line))
                .filter(matcher -> matcher.find())
                .collect(Collectors.toMap(matcher -> matcher.group(1), matcher -> matcher.group(2)));
    }

    private File getGwendiaScriptFile(File workflowFile) throws IOException {
        //  <bash script="/path/to/script.sh"/>
        Pattern pattern = Pattern.compile("\\s*<bash\\s+" +
                "script=\"([\\w/_.-]+)\"\\s*/>\\s*");
        List<String> bashScripts = Files.lines(workflowFile.toPath())
                .map(line -> pattern.matcher(line))
                .filter(matcher -> matcher.find())
                .map(matcher -> matcher.group(1))
                .collect(Collectors.toList());
        if (bashScripts.size() != 1) {
            throw new RuntimeException("There should be 1 and only 1 bash script");
        }
        File scriptFile = Paths.get(bashScripts.get(0)).toFile();
        if ( ! scriptFile.exists()) {
            throw new RuntimeException("script file does not exists");
        }
        return scriptFile;
    }


    private  Map<String,String> getExecInputs(List<ParameterSweep> parameters) {
        if (parameters.stream()
                .anyMatch(param -> param.getValues().size() != 1)) {
            throw new RuntimeException("There must be exactly 1 value for all parameters");
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
                Map<String, Path> inputFiles = transferInputFiles();
                Path scriptPath = copyScript();
                String commandLine = generateCommandLine(scriptPath, inputFiles);
                Path execDir = scriptPath.getParent();
                execute(execDir, commandLine);
                transferOutputFiles(execDir);
            } catch (IOException | GRIDAClientException e) {
                throw new RuntimeException("Error running local execution", e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // set interrupt flag
            }
        }

        private Map<String,Path> transferInputFiles() throws IOException, GRIDAClientException {
            // find URI inputs
            Path toDir = exec.workflowDir.resolve("inputs");
            Files.createDirectory(toDir);
            Map<String,Path> inputsFiles = new HashMap<>();
            for (String name : getFileInputName()) {
                Path from = Paths.get(exec.execInputs.get(name));
                String to = gridaClient.getRemoteFile(from.toString(), toDir.toString());
                inputsFiles.put(name, Paths.get(to));
            }
            return inputsFiles;
        }

        private Path copyScript() throws IOException, GRIDAClientException {
            // find URI inputs
            Path execDir = exec.workflowDir.resolve("exec_dir");
            Files.createDirectory(execDir);
            String from = exec.scriptFile.toString();
            String to = gridaClient.getRemoteFile(from, execDir.toString());
            return Paths.get(to);
        }

        private Set<String> getFileInputName() {
            // find URI inputs
            return exec.gwendiaInputs.entrySet().stream()
                    .filter(x -> x.getValue().equals("URI"))
                    .map(x -> x.getKey())
                    .collect(Collectors.toSet());
        }

        private Set<String> getNonFileInputName() {
            // find URI inputs
            return exec.gwendiaInputs.entrySet().stream()
                    .filter(x -> x.getValue().equals("string"))
                    .map(x -> x.getKey())
                    .collect(Collectors.toSet());
        }

        private String generateCommandLine(Path scriptPath, Map<String, Path> inputFiles) {
            // sh $WORKFLOW_DIR/exec_dir/$script_name --$file-input-name $WORKFLOW_DIR/inputs/$file_name --$non-file-input-name $value
            Path execDir = scriptPath.getParent();
            StringBuilder sb = new StringBuilder()
                    .append("bash ").append(scriptPath.getFileName()).append(" ");
            inputFiles.entrySet().stream().forEach(mapEntry -> {
                sb
                        .append("--").append(mapEntry.getKey()).append(" ")
                        .append(mapEntry.getValue()).append(" ");
            });
            for (String nonFileInput : getNonFileInputName()) {
                sb
                        .append("--").append(nonFileInput).append(" ")
                        .append(exec.execInputs.get(nonFileInput)).append(" ");
            }
            return sb.toString();
        }

        private void execute(Path execDir, String commandLine) throws IOException, InterruptedException {
            ProcessBuilder builder = new ProcessBuilder()
                    .command(commandLine)
                    .directory(execDir.toFile())
                    .redirectErrorStream(true)
                    .redirectOutput(execDir.resolve("output.log").toFile());
            Process process = builder.start();
            if ( process.waitFor() != 0) {
                throw new RuntimeException("process finished with error");
            };
        }

        private void transferOutputFiles(Path execDir) throws GRIDAClientException {
            String toDir = exec.execInputs.get("result-directory");
            if (exec.gwendiaOutputs.values().stream()
                    .anyMatch(type -> ! "URI".equals(type) )) {
                throw new RuntimeException("Only URI outputs are supported");
            }
            for (String output : exec.gwendiaOutputs.keySet()) {
                Path from = execDir.resolve(output);
                gridaClient.uploadFile(from.toString(), toDir);
            };
        }
    }
}
