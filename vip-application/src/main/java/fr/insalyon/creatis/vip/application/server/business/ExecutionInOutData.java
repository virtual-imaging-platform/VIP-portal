package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class ExecutionInOutData {
    private List<InOutData> inputData;
    private List<InOutData> outputData;
    public ExecutionInOutData(List<InOutData> inputData, List<InOutData> outputData) {
        this.inputData = inputData;
        this.outputData = outputData;
    }
    public List<InOutData> getInputData() {
        return inputData;
    }
    public void setInputData(List<InOutData> inputData) {
        this.inputData = inputData;
    }
    public List<InOutData> getOutputData() {
        return outputData;
    }
    public void setOutputData(List<InOutData> outputData) {
        this.outputData = outputData;
    }
}
