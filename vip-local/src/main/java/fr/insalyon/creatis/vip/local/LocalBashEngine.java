package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * Local engine that simulates workflow execution.
 *
 * At the moment it does the basic minimum :
 * - looks for a bash script in the gwendia file
 * - creates a local directory for each execution
 * - copies the input file and the bash script in the file
 * - launches the bash script with the inputs in the command line
 * - transfers the results in a results-directory given path
 * - do all this asynchronously and handle a status
 * - handles all path as lfn and use GridaClientLocal for transfers
 *
 * A lot of improvements are possible
 */
@Component
@Profile("local")
@Lazy
public class LocalBashEngine {
    private final Logger logger = LoggerFactory.getLogger(getClass());

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
        } catch (Exception e) {
            logger.error("launch error", e);
            throw e;
        }
    }

    public SimulationStatus getStatus(String workflowID) {
        if ( ! executionsFutures.containsKey(workflowID)) {
            return SimulationStatus.Killed;
        }
        Future<?> execFuture = executionsFutures.get(workflowID);
        if (execFuture.isCancelled()) {
            // todo : verify that this works, that an exception in the job leads to this
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
        exec.scriptFileLFN = getGwendiaScriptFile(workflowFile);
        return exec;
    }

    private static class LocalBashExecution {
        String id;
        File workflowFile;
        Path workflowDir;
        String scriptFileLFN;
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

    private String getGwendiaScriptFile(File workflowFile) throws IOException {
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
        return bashScripts.get(0);
    }


    private  Map<String,String> getExecInputs(List<ParameterSweep> parameters) {
        if (parameters.stream()
                .anyMatch(param -> param.getValues().size() != 1)) {
            throw new RuntimeException("There must be exactly 1 value for all parameters");
        }
        return parameters.stream()
                .map(p -> {logger.info("exec input {} -- {}", p.getParameterName(), p.getValues()); return p;})
                .collect(Collectors.toMap(
                param -> param.getParameterName(),
                param -> param.getValues().get(0)
        ));
    }
    
    /*
    TODO :
    - verify inputs are present
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
                List<String> commandLine = generateCommandLine(scriptPath, inputFiles);
                Path execDir = scriptPath.getParent();
                execute(execDir, commandLine);
                transferOutputFiles(execDir);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // set interrupt flag
            } catch (Exception e) {
                logger.error("test run error", e);
                throw new RuntimeException("Error running local execution", e);
            }
        }

        private Map<String,Path> transferInputFiles() throws IOException, GRIDAClientException {
            // find URI inputs
            Path toDir = exec.workflowDir.resolve("inputs");
            Files.createDirectory(toDir);
            Map<String,Path> inputsFiles = new HashMap<>();
            for (String name : getFileInputNames()) {
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
            String from = exec.scriptFileLFN;
            String to = gridaClient.getRemoteFile(from, execDir.toString());
            return Paths.get(to);
        }

        private Set<String> getFileInputNames() {
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

        private List<String> generateCommandLine(Path scriptPath, Map<String, Path> inputFiles) {
            // sh $WORKFLOW_DIR/exec_dir/$script_name --$file-input-name $WORKFLOW_DIR/inputs/$file_name --$non-file-input-name $value
            List<String> commandLine = new ArrayList<>();
            commandLine.add("bash");
            commandLine.add(scriptPath.getFileName().toString());
            inputFiles.entrySet().stream().forEach(mapEntry -> {
                commandLine.add("--" + mapEntry.getKey());
                commandLine.add(mapEntry.getValue().toString());
            });
            for (String nonFileInput : getNonFileInputName()) {
                commandLine.add("--" + nonFileInput);
                commandLine.add(exec.execInputs.get(nonFileInput));
            }
            return commandLine;
        }

        private void execute(Path execDir, List<String> commandLine) throws IOException, InterruptedException {
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
            String toDir = exec.execInputs.get("results-directory");
            if (toDir == null) {
                throw new RuntimeException("there should be a results-directory parameter");
            }
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
