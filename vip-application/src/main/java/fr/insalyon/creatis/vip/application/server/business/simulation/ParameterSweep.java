package fr.insalyon.creatis.vip.application.server.business.simulation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class ParameterSweep {

    private String parameterName;
    private List<String> values;

    public ParameterSweep(String parameterName) {
        this.parameterName = parameterName;
        this.values = new ArrayList<String>();
    }

    public ParameterSweep(String parameterName, String value) {
        this(parameterName);
        addValue(value);
    }

    public String getParameterName() {
        return parameterName;
    }

    public List<String> getValues() {
        return values;
    }

    public void addValue(String value) {
        values.add(value);
    }
}
