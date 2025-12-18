package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.api.model.Execution;
import fr.insalyon.creatis.vip.api.model.ExecutionStatus;
import fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.application.models.InOutData;
import fr.insalyon.creatis.vip.application.models.Simulation;

import org.hamcrest.Matcher;

import java.util.*;
import java.util.function.Function;

import static fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher.jsonCorrespondsTo;

public class ExecutionTestUtils {

    public static final Map<String,Function> executionSuppliers;

    public static final Execution execution1,   execution2;
    public static final Simulation simulation1, simulation2;
    public static final List<InOutData> simulation1InData, simulation2InData;
    public static final List<InOutData> simulation1OutData, simulation2OutData;

    static {
        // TODO Test with int or float params
        simulation1 = new Simulation("pipelineTest1", "3", null, "execId1",
                UserTestUtils.baseUser1.getFullName(),
                new GregorianCalendar(2016, 9, 2).getTime(),
                "Exec test 1", SimulationStatus.Running.toString(), "engine 1", null);
        execution1 = getExecution(simulation1, ExecutionStatus.RUNNING);
        execution1.setInputValues(new HashMap<String,Object>() {{
                put("param 1", "value 1");
                put("param 2", "42");
            }}
        );
        execution1.clearReturnedFiles();

        simulation1InData = Arrays.asList(
                new InOutData("value 1", "param 1", "String"),
                new InOutData("42", "param 2", "Integer"));
        simulation1OutData = Collections.emptyList();

        simulation2 = new Simulation("pipelineTest2", "4.2", null, "execId2",
                UserTestUtils.baseUser1.getFullName(),
                new GregorianCalendar(2016, 4, 29).getTime(),
                "Exec test 2", SimulationStatus.Completed.toString(), "engine 1", null);
        execution2 = getExecution(simulation2, ExecutionStatus.FINISHED);
        execution2.setInputValues(new HashMap<String,Object>() {{
                  put("param2-1", "5.3");
              }}
        );
        execution2.setReturnedFiles(new HashMap<String,List<Object>>() {{
            put("param2-res", Collections.singletonList("/vip/Home/testFile1.xml"));
        }});
        simulation2InData = Collections.singletonList(
                new InOutData("5.3", "param2-1", "Float"));
        simulation2OutData = Collections.singletonList(
                new InOutData("/vip/Home/testFile1.xml", "param2-res", "URI"));

        executionSuppliers = getExecutionSuppliers();
    }

    private static Execution getExecution(Simulation simulation, ExecutionStatus executionStatus) {
        // TODO : startDate should be in seconds
        String resultsDirectory = null;
        return new Execution(
            simulation.getID(),
            simulation.getSimulationName(),
            simulation.getApplicationName() + "/" + simulation.getApplicationVersion(),
            0, executionStatus, null, null, simulation.getDate().getTime(), null,
            resultsDirectory);
    }

    public static Execution summariseExecution(Execution execution) {
        Execution newExecution = new Execution(
                execution.getIdentifier(),
                execution.getName(),
                execution.getPipelineIdentifier(),
                execution.getTimeout(),
                execution.getStatus(),
                execution.getStudyIdentifier(),
                execution.getErrorCode(),
                execution.getStartDate(),
                execution.getEndDate(),
                execution.getResultsLocation()
        );
        // strip input values (so do not add them);
        return newExecution;
    }

    public static Execution copyExecutionWithNewName(Execution execution, String newName) {
        Execution newExecution = new Execution(
                execution.getIdentifier(),
                newName,
                execution.getPipelineIdentifier(),
                execution.getTimeout(),
                execution.getStatus(),
                execution.getStudyIdentifier(),
                execution.getErrorCode(),
                execution.getStartDate(),
                execution.getEndDate(),
                execution.getResultsLocation()
        );
        // WARNING, do not copy input value and returned files objects
        newExecution.setInputValues(execution.getInputValues());
        newExecution.setReturnedFiles(execution.getReturnedFiles());
        return newExecution;
    }

    public static Simulation copySimulationWithNewName(Simulation simu, String newName) {
        Simulation newSimulation = new Simulation(
                simu.getApplicationName(),
                simu.getApplicationVersion(),
                simu.getApplicationClass(),
                simu.getID(),
                simu.getUserName(),
                simu.getDate(),
                newName,
                simu.getStatus().toString(),
                simu.getEngine(),
                null
        );
        return newSimulation;
    }

    @SuppressWarnings("unchecked")
    public static Map<String,Function> getExecutionSuppliers() {
        return JsonCustomObjectMatcher.formatSuppliers(
                Arrays.asList(
                        "identifier", "name", "pipelineIdentifier", "timeout",
                        "status", "inputValues", "returnedFiles", "studyIdentifier",
                        "errorCode", "startDate", "endDate", "jobs"),
                Execution::getIdentifier,
                Execution::getName,
                Execution::getPipelineIdentifier,
                Execution::getTimeout,
                execution -> execution.getStatus().getRestLabel(),
                Execution::getInputValues,
                Execution::getReturnedFiles,
                Execution::getStudyIdentifier,
                Execution::getErrorCode,
                Execution::getStartDate,
                Execution::getEndDate,
                Execution::getJobs
        );
    }

    public static Matcher<Map<String,?>> jsonCorrespondsToExecution(Execution execution) {
        Map<Class, Map<String, Function>> suppliersRegistry = new HashMap<>();
        return jsonCorrespondsTo(execution, executionSuppliers, suppliersRegistry);
    }
}
