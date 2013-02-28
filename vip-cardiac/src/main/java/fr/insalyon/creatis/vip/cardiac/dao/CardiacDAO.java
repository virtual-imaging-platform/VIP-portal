package fr.insalyon.creatis.vip.cardiac.dao;

import fr.insalyon.creatis.vip.cardiac.client.bean.Simulation;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author glatard
 */



public interface CardiacDAO {
    
    public void addSimulation(Simulation s) throws DAOException;
    
    public void deleteSimulation(Simulation s) throws DAOException;
    
//    public void addFileToSimulation(File f, Simulation s) throws DAOException;
//    
//    public void deleteFileFromSimulation(File f, Simulation s) throws DAOException;
    
    public List<Simulation> getSimulations() throws DAOException;

    public void updateSimulation(Simulation s)throws DAOException;
}
