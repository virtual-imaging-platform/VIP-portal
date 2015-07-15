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
package fr.insalyon.creatis.vip.query.server.rpc;

import java.util.List;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import fr.insalyon.creatis.vip.query.client.bean.Parameter;
import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.query.client.bean.QueryExecution;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import fr.insalyon.creatis.vip.query.client.bean.Value;
import fr.insalyon.creatis.vip.query.client.rpc.QueryService;
import fr.insalyon.creatis.vip.query.client.view.QueryException;
import fr.insalyon.creatis.vip.query.server.dao.business.QueryBusiness;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Nouha Boujelben
 */
public class QueryServiceImpl extends AbstractRemoteServiceServlet implements QueryService {

    private static Logger logger = Logger.getLogger(QueryServiceImpl.class);
    private QueryBusiness queryBusiness;

    public QueryServiceImpl() {


        queryBusiness = new QueryBusiness();
    }

    @Override
    public List<String[]> getQueries() throws QueryException {

        try {
            String creator = null;
            try {
                creator = getSessionUser().getEmail();
            } catch (CoreException ex) {
                java.util.logging.Logger.getLogger(QueryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return queryBusiness.getQueries(creator);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }

    @Override
    public List<String[]> getVersion() throws QueryException {
        try {
            return queryBusiness.getVersion();

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }

    @Override
    public Long add(Query query) throws QueryException {

        try {
            query.setQueryMaker(getSessionUser().getEmail());
        } catch (CoreException ex) {
            java.util.logging.Logger.getLogger(QueryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            return queryBusiness.add(query);


        } catch (BusinessException ex) {
            throw new QueryException(ex);

        }
    }

    @Override
    public Long addVersion(QueryVersion version, boolean bodyTypeHtml) throws QueryException {

        try {
            return queryBusiness.addVersion(version, bodyTypeHtml);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }

    @Override
    public void removeVersion(Long versionid) throws QueryException {


        try {
            queryBusiness.removeVersion(versionid);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }

    @Override
    public List<Long> addParameter(Parameter param) throws QueryException {
        try {
            return queryBusiness.addParameter(param);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }

    @Override
    public List<String[]> getQuerie(Long queryversionid, Long queryID) throws QueryException {
        try {
            return queryBusiness.getQuerie(queryversionid, queryID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }

    @Override
    public List<Parameter> getParameter(Long queryVersionID) throws QueryException {
        try {
            return queryBusiness.getParameter(queryVersionID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }
    }

    @Override
    public Long addValue(Value value) throws QueryException {
        try {
            return queryBusiness.addValue(value);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public Long addQueryExecution(QueryExecution queryExecution) throws QueryException {

        try {
            queryExecution.setExecuter(getSessionUser().getEmail());

        } catch (CoreException ex) {
            java.util.logging.Logger.getLogger(QueryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            return queryBusiness.addQueryExecution(queryExecution);
        } catch (BusinessException ex) {
            throw new QueryException(ex);

        }

    }

    @Override
    public List<String[]> getQueryHistory(String state) throws QueryException {
        String executer = null;
        try {

            try {
                executer = getSessionUser().getEmail();
            } catch (CoreException ex) {
                java.util.logging.Logger.getLogger(QueryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return queryBusiness.getQueryHistory(executer, state);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public String getBody(Long queryVersionID, Long queryExecutionID, boolean parameter) throws QueryException {
        try {
            return queryBusiness.getBody(queryVersionID, queryExecutionID, parameter);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public void updateQueryExecution(String bodyResult, String status, Long executionID) throws QueryException {
        try {
            queryBusiness.updateQueryExecution(bodyResult, status, executionID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public void updateQueryExecutionStatusWaiting(String status, Long executionID) throws QueryException {
        try {
            queryBusiness.updateQueryExecutionStatusWaiting(status, executionID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public void updateQueryExecutionStatusFailed(String status, Long executionID) throws QueryException {
        try {
            queryBusiness.updateQueryExecutionStatusFailed(status, executionID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public void updateQueryVersion(Long queryID, String name, String description, boolean isPublic) throws QueryException {
        try {
            queryBusiness.updateQueryVersion(queryID, name, description, isPublic);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public List<String> getDescriptionQueryMaker(Long queryVersionID) throws QueryException {
        try {
            return queryBusiness.getDescriptionQueryMaker(queryVersionID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public List<String[]> getParameterValue(Long queryExecutionID) throws QueryException {
        try {
            return queryBusiness.getParameterValue(queryExecutionID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public void removeQueryExecution(Long executionID) throws QueryException {
        try {
            queryBusiness.removeQueryExecution(executionID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public Long maxVersion(Long queryID) throws QueryException {

        try {
            return queryBusiness.maxVersion(queryID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);
        }

    }

    @Override
    public Long getQueryID(Long queryVersionID) throws QueryException {

        try {
            return queryBusiness.getQueryID(queryVersionID);

        } catch (BusinessException ex) {
            throw new QueryException(ex);

        }

    }

    @Override
    public boolean getBodies(Long queryID, String body) throws QueryException {

        try {
            return queryBusiness.getBodies(queryID, body);

        } catch (BusinessException ex) {
            throw new QueryException(ex);

        }

    }
}
