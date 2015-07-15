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
 * @author Nouha Boujelben
 */
public interface QueryDAO {

    public List<String[]> getQueries(String creator) throws DAOException;

    public List<String[]> getVersion() throws DAOException;

    public Long add(Query query) throws DAOException;

    public Long addVersion(QueryVersion version,boolean bodyTypeHtml) throws DAOException;

    public void removeVersion(Long versionid) throws DAOException;

    public List<Long> addParameter(Parameter param) throws DAOException;

    public List<String[]> getQuerie(Long queryversionid,Long queryID) throws DAOException;

    public List<Parameter> getParameter(Long queryVersionID) throws DAOException;

    public Long addValue(Value value) throws DAOException;

    public Long addQueryExecution(QueryExecution queryExecution) throws DAOException;

    public List<String[]> getQueryHistory(String executer,String state) throws DAOException;

    public String getBody(Long queryVersionID, Long queryExecutionID, boolean parameter) throws DAOException;

    public void updateQueryExecution(String bodyResult, String status, Long executionID) throws DAOException;

    public void updateQueryVersion(Long queryID, String name, String description,boolean isPublic) throws DAOException;

    public List<String>getDescriptionQueryMaker(Long queryVersionID) throws DAOException;

    public List<String[]> getParameterValue(Long queryExecutionID) throws DAOException;

    public void removeQueryExecution(Long executionID) throws DAOException;

    public Long maxVersion(Long queryID) throws DAOException;

    public Long getQueryID(Long queryVersionID) throws DAOException;
    public  boolean getBodies(Long queryID,String body) throws DAOException ;
    public void updateQueryExecutionStatusWaiting(String status, Long executionID) throws DAOException ;
     public void updateQueryExecutionStatusFailed(String status, Long executionID) throws DAOException ;
}
