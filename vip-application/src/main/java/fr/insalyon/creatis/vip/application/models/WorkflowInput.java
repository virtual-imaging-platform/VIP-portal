package fr.insalyon.creatis.vip.application.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.gwt.user.client.rpc.IsSerializable;
import fr.insalyon.creatis.vip.core.server.inter.DataViews;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.ArrayList;

@JsonView(DataViews.User.class)
public class WorkflowInput implements IsSerializable {

    private List<String> values;
    @Size(max=3)
    private List<Double> interval;

    public static WorkflowInput ofList(List<String> values) {
        WorkflowInput workflowInput = new WorkflowInput();
        workflowInput.setValues(values);
        return workflowInput;
    }

    public static WorkflowInput ofValue(String value) {
        return ofList(List.of(value));
    }

    public static WorkflowInput ofinterval(List<Double> values) {
        WorkflowInput workflowInput = new WorkflowInput();
        workflowInput.setInterval(values);
        return workflowInput;
    }

    public boolean isInterval() {
        return interval != null;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = new ArrayList<>(values);
    }

    public List<Double> getInterval() {
        return interval;
    }

    public void setInterval(List<Double> interval) {
        this.interval = new ArrayList<>(interval);
    }
}
