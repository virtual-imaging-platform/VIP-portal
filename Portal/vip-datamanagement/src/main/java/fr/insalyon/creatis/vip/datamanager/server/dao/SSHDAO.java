/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.server.dao;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.bean.SSH;
import java.util.List;

/**
 *
 * @author glatard
 */
public interface SSHDAO {
    public List<SSH> getSSHConnections() throws DAOException;
    
    public void addSSH(SSH ssh) throws DAOException;
    
    public void updateSSH(SSH ssh) throws DAOException;
    
    public void removeSSH(String email, String name) throws DAOException;
    
}
