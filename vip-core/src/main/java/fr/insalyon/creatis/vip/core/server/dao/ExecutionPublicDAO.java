package fr.insalyon.creatis.vip.core.server.dao;

import fr.insalyon.creatis.vip.core.client.bean.Execution;

import java.util.List;

public interface ExecutionPublicDAO {
    public void add(Execution execution) throws DAOException;
    public void update(Execution execution) throws DAOException;
    List<Execution> getExecutions() throws DAOException;
}

