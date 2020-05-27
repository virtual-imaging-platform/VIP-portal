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

import fr.insalyon.creatis.vip.api.model.*;
import fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher;
import fr.insalyon.creatis.vip.application.client.bean.*;
import org.hamcrest.Matcher;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.function.*;

/**
 * Created by abonnet on 8/3/16.
 */
public class PipelineTestUtils {

    public static final Map<String,Function> pipelineSuppliers;
    public static final Map<String,Function> pipelineParameterSuppliers;

    // sourceParamx and pipelineParamx must be the same

    public static final Source sourceParam1, sourceParam2;
    public static final PipelineParameter pipelineParam1, pipelineParam2;

    public static final Entry<Source, PipelineParameter>[] paramPairs;

    static {
        sourceParam1 = new Source("param 1", "type param 1", "user level 1", "desc param 1",
                "true", "default value 1", "", "");

        pipelineParam1 = new PipelineParameter(sourceParam1.getName(), ParameterType.String,
                true, false, sourceParam1.getDefaultValue(), sourceParam1.getDescription());

        sourceParam2 = new Source("param 2", "URI", "user level 2", "desc param 2");

        pipelineParam2 = new PipelineParameter(sourceParam2.getName(), ParameterType.File,
                false, false, sourceParam2.getDefaultValue(), sourceParam2.getDescription());

        paramPairs = new Entry[] {new SimpleEntry(sourceParam1, pipelineParam1),
            new SimpleEntry(sourceParam2, pipelineParam2)};


        pipelineSuppliers = getPipelineSuppliers();
        pipelineParameterSuppliers = getPipelineParameterSuppliers();
    }

    public static Pipeline getPipeline(Application app, AppVersion version) {
        return new Pipeline(app.getName() + "/" + version.getVersion(),
                app.getName(), version.getVersion(), true);
    }

    public static Descriptor getDescriptor(String desc, Integer... paramIndexes) {
        List<Source> sources = new ArrayList<>();
        for (Integer paramIndex : paramIndexes) {
            sources.add(paramPairs[paramIndex].getKey());
        }
        return new Descriptor(sources, desc);
    }

    public static Pipeline getFullPipeline(Application app, AppVersion version, String desc, Integer... paramIndexes) {
        Pipeline pipeline = getPipeline(app, version);
        pipeline.setDescription(desc);
        for (Integer paramIndex : paramIndexes) {
            pipeline.getParameters().add(paramPairs[paramIndex].getValue());
        }
        return pipeline;
    }

    public static Map<String,Function> getPipelineSuppliers() {
        return JsonCustomObjectMatcher.formatSuppliers(
                Arrays.asList("identifier", "name", "version", "description", "canExecute", "parameters"),
                Pipeline::getIdentifier,
                Pipeline::getName,
                Pipeline::getVersion,
                Pipeline::getDescription,
                Pipeline::canExecute,
                Pipeline::getParameters);
    }

    public static Map<String,Function> getPipelineParameterSuppliers() {
        return JsonCustomObjectMatcher.formatSuppliers(
                Arrays.asList("name", "type", "isOptional", "isReturnedValue", "defaultValue", "description"),
                PipelineParameter::getName,
                PipelineParameter::getType,
                PipelineParameter::isOptional,
                PipelineParameter::isReturnedValue,
                PipelineParameter::getDefaultValue,
                PipelineParameter::getDescription);
    }

    public static Matcher<Map<String,?>> jsonCorrespondsToPipeline(Pipeline pipeline) {
        Map<Class, Map<String, Function>> suppliersRegistry = new HashMap<>();
        suppliersRegistry.put(PipelineParameter.class, pipelineParameterSuppliers);
        return JsonCustomObjectMatcher.jsonCorrespondsTo(pipeline, pipelineSuppliers, suppliersRegistry);
    }
}
