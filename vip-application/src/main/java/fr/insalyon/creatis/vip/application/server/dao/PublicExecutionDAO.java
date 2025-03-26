package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.client.bean.PublicExecution;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;

import java.util.List;

public interface PublicExecutionDAO {

    void add(PublicExecution publicExecution) throws DAOException;

    PublicExecution get(String experienceName) throws DAOException;

    void update(String experienceName, PublicExecution publicExecution) throws DAOException;

    List<PublicExecution> getAll() throws DAOException;

    boolean exist(String experienceName) throws DAOException;
}

