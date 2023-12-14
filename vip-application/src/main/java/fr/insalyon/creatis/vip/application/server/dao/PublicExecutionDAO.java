package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.core.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

import java.util.List;

public interface PublicExecutionDAO {

    void add(PublicExecution publicExecution) throws DAOException;

    PublicExecution get(String publicExecutionId) throws DAOException;

    void update(String executionId, PublicExecution.PublicExecutionStatus newStatus) throws DAOException;

    List<PublicExecution> getExecutions() throws DAOException;

    boolean doesExecutionExist(String executionId) throws DAOException;
}

