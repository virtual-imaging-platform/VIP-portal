/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.dao.persistance;


import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.List;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
/**
 *
 * @author Boujelben
 */
public interface QueryDAO {
    
     public List<Query> getQueries() throws DAOException;
     public List<String[]> getVersion() throws DAOException;
     public void add(Query query) throws DAOException;
     public void addVersion(QueryVersion version,Query query) throws DAOException;
     
}
