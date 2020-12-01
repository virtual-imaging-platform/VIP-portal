package fr.insalyon.creatis.vip.local;

import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.client.GRIDAPoolClient;
import fr.insalyon.creatis.grida.common.bean.Operation;
import fr.insalyon.creatis.grida.common.bean.Operation.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile("local")
@Primary
public class GRIDAPoolClientLocal extends GRIDAPoolClient {

    private GridaClientLocal gridaClientLocal;

    private Map<String, Operation> operations = new HashMap<>();
    private Integer operationId = 1;

    @Autowired
    public GRIDAPoolClientLocal(GridaClientLocal gridaClientLocal) {
        super(null, 0, null);
        this.gridaClientLocal = gridaClientLocal;
    }

    private String addOperation(String source, String dest, Operation.Type type, String user) {
        String operationIdString = (operationId++).toString();
        Operation operation = new Operation(operationIdString, source, dest, type, user, "", 0);
        operations.put(operationIdString, operation);
        return operationIdString;
    }

    @Override
    public String uploadFile(String localFile, String remoteDir, String user) throws GRIDAClientException {
       gridaClientLocal.uploadFile(localFile, remoteDir);
       return addOperation(localFile, remoteDir, Type.Upload, user);
    }

    @Override
    public String downloadFile(String remoteFile, String localDir, String user) throws GRIDAClientException {
        gridaClientLocal.getRemoteFile(remoteFile, localDir);
        return addOperation(remoteFile, localDir, Type.Download, user);
    }

    @Override
    public String downloadFiles(String[] remoteFiles, String packName, String user) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public String downloadFolder(String remoteFolder, String localDir, String user) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public String replicateToPreferredSEs(String remoteFile, String user) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public void delete(String remotePath, String user) throws GRIDAClientException {
        gridaClientLocal.delete(remotePath);
        addOperation(remotePath, "", Type.Delete, user);
    }

    @Override
    public List<Operation> getOperationsListByUser(String user) throws GRIDAClientException {
        return operations.values().stream()
                .filter(operation -> user.equals(operation.getUser()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> getOperationsLimitedListByUserAndDate(String user, int limit, Date startDate) throws GRIDAClientException {
        throw new GRIDAClientException("not implemented in local version");
    }

    @Override
    public Operation getOperationById(String id) throws GRIDAClientException {
        return operations.get(id);
    }

    @Override
    public void removeOperationById(String id) throws GRIDAClientException {
        operations.remove(id);
    }

    @Override
    public void removeOperationsByUser(String user) throws GRIDAClientException {
        List<Operation> userOperations = getOperationsListByUser(user);
        userOperations.forEach(operation -> operations.remove(operation.getId()));
    }

    @Override
    public List<Operation> getAllOperations() throws GRIDAClientException {
        return new ArrayList<>(operations.values());
    }
}
