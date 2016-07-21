package fr.insalyon.creatis.vip.api.rest.model;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by abonnet on 7/19/16.
 */
public class Pipeline {

    @NotNull
    private String identifier;
    @NotNull
    private String name;
    @NotNull
    private String version;
    private String description;
    private Boolean canExecute;
    @NotNull
    private List<PipelineParameter> parameters;
}
