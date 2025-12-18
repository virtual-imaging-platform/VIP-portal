package fr.insalyon.creatis.vip.gatelab.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;

public interface GateLabDAO {

    public long getNumberParticles() throws DAOException;

    public void StopWorkflowSimulation() throws DAOException;
}
