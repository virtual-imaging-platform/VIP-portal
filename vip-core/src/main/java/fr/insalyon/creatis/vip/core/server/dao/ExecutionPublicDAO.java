package fr.insalyon.creatis.vip.core.server.dao;

import fr.insalyon.creatis.vip.core.client.bean.Execution;

import java.util.List;

public interface ExecutionPublicDAO {
    public void add(Execution execution) throws DAOException;
    void update(String executionId, String newStatus) throws DAOException;
    List<Execution> getExecutions() throws DAOException;
    public boolean doesExecutionExist(String executionId) throws DAOException;
}

