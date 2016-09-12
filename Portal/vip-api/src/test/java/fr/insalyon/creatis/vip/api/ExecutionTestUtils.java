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
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.api.MapHasSamePropertyAs;
import fr.insalyon.creatis.vip.api.bean.*;
import fr.insalyon.creatis.vip.api.business.ApiUtils;
import fr.insalyon.creatis.vip.application.client.bean.*;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationStatus;
import fr.insalyon.creatis.vip.core.client.bean.User;
import org.hamcrest.Matcher;

import java.lang.Object;
import java.util.*;
import java.util.function.Function;

import static fr.insalyon.creatis.vip.api.PipelineTestUtils.*;
import static fr.insalyon.creatis.vip.core.client.view.util.CountryCode.re;

/**
 * Created by abonnet on 8/3/16.
 */
public class ExecutionTestUtils {

    public static final Map<String,Function<Execution,?>> executionSuppliers;

    public static final Execution execution1;

    static {
        // TODO : startDate should be in seconds
        execution1 = new Execution("execTest1", "exec test 1", "pipelineTest1/3", 0, ExecutionStatus.RUNNING,
                null, null, new GregorianCalendar(2016, 9, 2).getTimeInMillis(), null);
        execution1.setRestInputValues(new HashMap<String,Object>() {{
                put("param 1", "value 1");
                put("param 2", 42);
            }}
        );

        executionSuppliers = getExecutionSuppliers();
    }

    public static Simulation getSimulation(Execution execution) {
        return new Simulation("pipelineTest1", "3", null, execution.getIdentifier(),
                null, new Date(execution.getStartDate()), execution.getName(),
                SimulationStatus.Running.toString(),
                null);
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
                execution.getEndDate()
        );
        // strip input values;
        return newExecution;
    }

    public static Map<String,Function<Execution,?>> getExecutionSuppliers() {
        // TODO : add returnedFiles
        return MapHasSamePropertyAs.formatSuppliers(
                Arrays.asList(
                        "identifier", "name", "pipelineIdentifier", "timeout", "status", "inputValues",
                        "studyIdentifier", "errorCode", "startDate", "endDate"),
                Execution::getIdentifier,
                Execution::getName,
                Execution::getPipelineIdentifier,
                Execution::getTimeout,
                execution -> execution.getStatus().getRestLabel(),
                Execution::getRestInputValues,
                Execution::getStudyIdentifier,
                Execution::getErrorCode,
                Execution::getStartDate,
                Execution::getEndDate
        );
    }

    public static Matcher<Map<String,?>> mapCorrespondsToExecution(Execution execution) {
        Map<Class<?>, Map<String, Function<Object, ?>>> suppliersRegistry = new HashMap<>();
        return MapHasSamePropertyAs.mapHasSamePropertyAs(execution, executionSuppliers, suppliersRegistry);
    }
}
