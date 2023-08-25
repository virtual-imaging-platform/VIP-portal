package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.moteur.plugins.workflowsdb.bean.*;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.InputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.OutputDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.ProcessorDAO;
import fr.insalyon.creatis.moteur.plugins.workflowsdb.dao.WorkflowsDBDAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class LocalWorkflowDataManager {

    private final InputDAO inputDAO;
    private final OutputDAO outputDAO;
    private final ProcessorDAO processorDAO;

    @Autowired
    public LocalWorkflowDataManager(InputDAO inputDAO, OutputDAO outputDAO, ProcessorDAO processorDAO) {
        this.inputDAO = inputDAO;
        this.outputDAO = outputDAO;
        this.processorDAO = processorDAO;
    }

    public void addInputs(LocalBashEngine.LocalBashExecution localBashExecution) throws WorkflowsDBDAOException {
        for (String inputName : localBashExecution.execInputs.keySet()) {
            String inputValue = localBashExecution.execInputs.get(inputName);
            addInput(localBashExecution, inputName, inputValue);
        }
    }

    private void addInput(LocalBashEngine.LocalBashExecution localBashExecution,
                          String inputName, String inputValue) throws WorkflowsDBDAOException {
        // the processor is the input name
        Processor processor = new Processor(
                new ProcessorID(localBashExecution.id, inputName), 0, 0, 0);
        processorDAO.add(processor);
        String type = localBashExecution.gwendiaInputs.get(inputName);
        Input input = new Input(
                new InputID(localBashExecution.id, inputValue, inputName),
                "string".equalsIgnoreCase(type) ? DataType.String : DataType.URI);
        inputDAO.add(input);
    }

    public void addOutputs(LocalBashEngine.LocalBashExecution localBashExecution) throws WorkflowsDBDAOException {
        for (String outputName : localBashExecution.execOutputs.keySet()) {
            String outputValue = localBashExecution.execOutputs.get(outputName);
            addOutput(localBashExecution, outputName, outputValue);
        }
    }

    private void addOutput(LocalBashEngine.LocalBashExecution localBashExecution,
                          String outputName, String outputValue) throws WorkflowsDBDAOException {
        // the processor is the output name
        Processor processor = new Processor(
                new ProcessorID(localBashExecution.id, outputName), 0, 0, 0);
        processorDAO.add(processor);
        Output output = new Output(
                new OutputID(localBashExecution.id, outputValue, outputName),
                DataType.URI,
                outputName);
        outputDAO.add(output);
    }

}
