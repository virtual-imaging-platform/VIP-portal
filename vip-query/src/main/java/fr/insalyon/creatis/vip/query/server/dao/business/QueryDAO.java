/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.dao.business;


import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import java.util.List;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import java.sql.Timestamp;
/**
 *
 * @author Boujelben
 */
public interface QueryDAO {
    
     public List<String[]> getQueries() throws DAOException;
     public List<String[]> getVersion() throws DAOException;
     public Long add(Query query) throws DAOException;
     public Long addVersion(QueryVersion version) throws DAOException;
     public void removeVersion(Long versionid) throws DAOException;
     public List<Long> addParameter(Parameter param) throws DAOException;
     public List<String[]> getQuerie(Long queryversionid)throws DAOException;
     public List<Parameter> getParameter(Long queryVersionID) throws DAOException;
     public Long addValue(Value value) throws DAOException ;
     public Long addQueryExecution(QueryExecution queryExecution)throws DAOException ;
     public List<String[]> getQueryHistory() throws DAOException ;
     public String getBody(Long queryVersionID,Long queryExecutionID) throws DAOException;
     public void updateQueryExecution(String urlResult, String status,Long executionID ) throws DAOException;
    
}
