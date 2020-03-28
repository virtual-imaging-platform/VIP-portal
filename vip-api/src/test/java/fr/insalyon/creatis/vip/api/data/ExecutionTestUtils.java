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
package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.api.bean.*;
import fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import org.hamcrest.Matcher;

import java.lang.Object;
import java.util.*;
import java.util.function.Function;

import static fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher.jsonCorrespondsTo;

/**
 * Created by abonnet on 8/3/16.
 */
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
                "Exec test 1", SimulationStatus.Running.toString(), "engine 1");
        execution1 = getExecution(simulation1, ExecutionStatus.RUNNING);
        execution1.setRestInputValues(new HashMap<String,Object>() {{
                put("param 1", "value 1");
                put("param 2", "42");
            }}
        );

        simulation1InData = Arrays.asList(
                new InOutData("value 1", "param 1", "String"),
                new InOutData("42", "param 2", "Integer"));
        simulation1OutData = Collections.emptyList();

        simulation2 = new Simulation("pipelineTest2", "4.2", null, "execId2",
                UserTestUtils.baseUser1.getFullName(),
                new GregorianCalendar(2016, 4, 29).getTime(),
                "Exec test 2", SimulationStatus.Completed.toString(), "engine 1");
        execution2 = getExecution(simulation2, ExecutionStatus.FINISHED);
        execution2.setRestInputValues(new HashMap<String,Object>() {{
                  put("param2-1", "5.3");
              }}
        );
        execution2.setRestReturnedFiles(new HashMap<String,List<Object>>() {{
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
        newExecution.setRestInputValues(execution.getRestInputValues());
        newExecution.setRestReturnedFiles(execution.getRestReturnedFiles());
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
                simu.getEngine()
        );
        return newSimulation;
    }

    public static Map<String,Function> getExecutionSuppliers() {
        return JsonCustomObjectMatcher.formatSuppliers(
                Arrays.asList(
                        "identifier", "name", "pipelineIdentifier", "timeout",
                        "status", "inputValues", "returnedFiles", "studyIdentifier",
                        "errorCode", "startDate", "endDate"),
                Execution::getIdentifier,
                Execution::getName,
                Execution::getPipelineIdentifier,
                Execution::getTimeout,
                execution -> execution.getStatus().getRestLabel(),
                Execution::getRestInputValues,
                Execution::getRestReturnedFiles,
                Execution::getStudyIdentifier,
                Execution::getErrorCode,
                Execution::getStartDate,
                Execution::getEndDate
        );
    }

    public static Matcher<Map<String,?>> jsonCorrespondsToExecution(Execution execution) {
        Map<Class, Map<String, Function>> suppliersRegistry = new HashMap<>();
        return jsonCorrespondsTo(execution, executionSuppliers, suppliersRegistry);
    }
}
