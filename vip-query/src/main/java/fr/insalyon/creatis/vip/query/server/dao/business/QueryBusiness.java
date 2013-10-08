/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.dao.business;

import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Boujelben
 */
public class QueryBusiness {

    public List<String[]> getQueries() throws BusinessException {

        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getQueries();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String[]> getVersion() throws BusinessException {

        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getVersion();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Long add(Query query) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().add(query);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Long addVersion(QueryVersion version) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().addVersion(version);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeVersion(Long versionid) throws BusinessException {
        try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().removeVersion(versionid);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }

    }

    public List<Long> addParameter(Parameter param) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().addParameter(param);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }


    }

    public List<String[]> getQuerie(Long queryversionid,Long queryID) throws BusinessException {

        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getQuerie(queryversionid,queryID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }

    }

    public List<Parameter> getParameter(Long queryVersionID) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getParameter(queryVersionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }

    }

    public Long addValue(Value value) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().addValue(value);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }

    }

    public Long addQueryExecution(QueryExecution queryExecution) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().addQueryExecution(queryExecution);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }

    }

    public List<String[]> getQueryHistory() throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getQueryHistory();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }

    }

    public String getBody(Long queryVersionID, Long queryExecutionID, boolean parameter) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getBody(queryVersionID, queryExecutionID, parameter);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }

    }

    public void updateQueryExecution(String bodyResult, String status, Long executionID) throws BusinessException {
        try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().updateQueryExecution(bodyResult, status, executionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateQueryVersion(Long queryID, String name, String description) throws BusinessException {
        try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().updateQueryVersion(queryID, name, description);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public String getDescription(Long queryVersionID) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getDescription(queryVersionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String[]> getParameterValue(Long queryExecutionID) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getParameterValue(queryExecutionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void removeQueryExecution(Long executionID) throws BusinessException {
        try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().removeQueryExecution(executionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Long maxVersion(Long queryID) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().maxVersion(queryID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public Long getQueryID(Long queryVersionID) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getQueryID(queryVersionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
    
     public  boolean getBodies(Long queryID,String body) throws BusinessException {
          try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getBodies(queryID, body);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    
     
}
}
