package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.api.model.ParameterType;
import fr.insalyon.creatis.vip.api.model.Pipeline;
import fr.insalyon.creatis.vip.api.model.PipelineParameter;
import fr.insalyon.creatis.vip.api.tools.spring.JsonCustomObjectMatcher;
import fr.insalyon.creatis.vip.application.client.bean.AppVersion;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.application.client.bean.Descriptor;
import fr.insalyon.creatis.vip.application.client.bean.Source;
import org.hamcrest.Matcher;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class PipelineTestUtils {

    public static final Map<String,Function> pipelineSuppliers;
    public static final Map<String,Function> pipelineParameterSuppliers;

    // sourceParamx and pipelineParamx must be the same

    public static final Source sourceParam1, sourceParam2;
    public static final PipelineParameter pipelineParam1, pipelineParam2;

    public static final PipelineParameter resultsDir, fileParam, textParam, optionalTextParamNoValueProvided, optionalTextParam, flagParam;

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

        resultsDir = new PipelineParameter("results-directory", ParameterType.File,
                false, false, "/test/vip/Home", "This is the test results directory input");

        fileParam = new PipelineParameter("testFileInput", ParameterType.File,
                false, false, null, "This is a test file input");

        textParam = new PipelineParameter("testTextInput", ParameterType.String,
                false, false, "test text value", "This is a test text input");

        optionalTextParamNoValueProvided = new PipelineParameter("testOptionalTextInput", ParameterType.String,
                true, false, "No_value_provided", "This is a optional test text input");

        optionalTextParam = new PipelineParameter("testOptionalTextInput", ParameterType.String,
                true, false, null, "This is a optional test text input");

        flagParam = new PipelineParameter("testFlagInput", ParameterType.Boolean,
                true, false, "false", "This is a test flag input");

        pipelineSuppliers = getPipelineSuppliers();
        pipelineParameterSuppliers = getPipelineParameterSuppliers();
    }

    public static Pipeline getPipeline(Application app, AppVersion version) {
        return new Pipeline(app.getName() + "/" + version.getVersion(),
                app.getName(), version.getVersion());
    }

    public static Pipeline getPipeline(AppVersion version) {
        return new Pipeline(version.getApplicationName() + "/" + version.getVersion(),
                version.getApplicationName(), version.getVersion());
    }

    public static Descriptor getDescriptor(String desc, Integer... paramIndexes) {
        List<Source> sources = new ArrayList<>();
        for (Integer paramIndex : paramIndexes) {
            sources.add(paramPairs[paramIndex].getKey());
        }
        return new Descriptor(sources, desc);
    }

    public static Pipeline getFullPipeline(AppVersion version, String desc, Integer... paramIndexes) {
        Pipeline pipeline = getPipeline(version);
        pipeline.setDescription(desc);
        for (Integer paramIndex : paramIndexes) {
            pipeline.getParameters().add(paramPairs[paramIndex].getValue());
        }
        return pipeline;
    }

    public static Pipeline getFullPipeline(AppVersion version, String desc, PipelineParameter... params) {
        Pipeline pipeline = getPipeline(version);
        pipeline.setDescription(desc);
        for (PipelineParameter param : params) {
            pipeline.getParameters().add(param);
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
