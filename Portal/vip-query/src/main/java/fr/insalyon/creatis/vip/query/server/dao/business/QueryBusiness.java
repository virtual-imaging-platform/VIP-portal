/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
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

    public List<String[]> getQueries(String creator) throws BusinessException {

        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getQueries( creator);

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

    public Long addVersion(QueryVersion version,boolean bodyTypeHtml) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().addVersion(version,bodyTypeHtml);

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

    public List<String[]> getQueryHistory(String executer,String state) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getQueryHistory(executer,state);

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
    
    
    
     public void updateQueryExecutionStatusWaiting(String status, Long executionID) throws BusinessException {
             try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().updateQueryExecutionStatusWaiting(status, executionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
     public void updateQueryExecutionStatusFailed(String status, Long executionID) throws BusinessException {
             try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().updateQueryExecutionStatusFailed(status, executionID);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public void updateQueryVersion(Long queryID, String name, String description,boolean isPublic) throws BusinessException {
        try {
            QueryDAOFactory.getDAOFactory().getQueryDAO().updateQueryVersion(queryID, name, description,isPublic);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getDescriptionQueryMaker(Long queryVersionID) throws BusinessException {
        try {
            return QueryDAOFactory.getDAOFactory().getQueryDAO().getDescriptionQueryMaker(queryVersionID);

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
