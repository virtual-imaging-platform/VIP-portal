package fr.insalyon.creatis.vip.application.models;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Rafael Silva
 */
public class SimulationInput implements IsSerializable {

    private String application;
    private String name;
    private String inputs;

    public SimulationInput() {
    }

    public SimulationInput(String application, String name, String inputs) {
        
        this.application = application;
        this.name = name;
        this.inputs = inputs;
    }

    public String getApplication() {
        return application;
    }

    public String getInputs() {
        return inputs;
    }

    public String getName() {
        return name;
    }
}
