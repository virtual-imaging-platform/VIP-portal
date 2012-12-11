/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.server.dao;

import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Processor;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public interface WorkflowDAO {

    /**
     * 
     * @param workflow
     * @throws DAOException 
     */
    public void add(Simulation workflow) throws DAOException;

    /**
     * 
     * @param workflow
     * @throws DAOException 
     */
    public void update(Simulation workflow) throws DAOException;

    /**
     * 
     * @param workflowID
     * @return
     * @throws DAOException 
     */
    public Simulation get(String workflowID) throws DAOException;
    
    /**
     * Gets the list of simulations.
     * 
     * @param user
     * @return
     * @throws DAOException 
     */
    public List<Simulation> getList(String user) throws DAOException;
    
    /**
     * Gets the list of simulations from a date.
     * 
     * @param user
     * @param lastDate
     * @return
     * @throws DAOException 
     */
    public List<Simulation> getList(String user, Date lastDate) throws DAOException;
    
    /**
     * Gets the list of workflows submitted by a user filtered by application
     * name, status, start date and/or end date.
     * 
     * @param user User name
     * @param app Application name
     * @param status Application status
     * @param sDate Start date
     * @param eDate End date
     * @return List of workflows filtered
     */
    public List<Simulation> getList(String user, String app, String status, Date sDate, Date eDate) throws DAOException;
    
    /**
     * Gets the list of workflows submitted by a list of users filtered by 
     * application name, status, start date and/or end date.
     * 
     * @param user List of users
     * @param app Application name
     * @param status Application status
     * @param sDate Start date
     * @param eDate End date
     * @return List of workflows filtered
     */
    public List<Simulation> getList(List<String> users, String app, String status, Date sDate, Date eDate) throws DAOException;

    /**
     * Gets the list of applications submitted.
     *
     * @return List of applications
     */
    public List<String> getApplications() throws DAOException;

    /**
     * 
     * @param workflowID
     * @param status
     */
    public void updateStatus(String workflowID, String status) throws DAOException;

    /**
     *
     * @param workflowID
     * @return
     * @throws DAOException
     */
    public List<String> getOutputs(String workflowID) throws DAOException;

    /**
     *
     * @param workflowID
     * @throws DAOException
     */
    public void cleanWorkflow(String workflowID) throws DAOException;

    /**
     * 
     * @param workflowID
     * @throws DAOException
     */
    public void delete(String workflowID) throws DAOException;

    /**
     * 
     * @param simulationID
     * @param type
     * @return
     * @throws DAOException 
     */
    public List<InOutData> getInOutData(String simulationID, String type) throws DAOException;
    
    /**
     * 
     * @param user
     * @return
     * @throws DAOException 
     */
    public int getRunningWorkflows(String user) throws DAOException;
    
    /**
     * 
     * @return
     * @throws DAOException 
     */
    public List<Simulation> getRunningWorkflows() throws DAOException;
    
    /**
     * 
     * @param simulationID
     * @return
     * @throws DAOException 
     */
    public List<Processor> getProcessors(String simulationID) throws DAOException;
    
    /**
     * 
     * @param simulationList
     * @return
     * @throws DAOException 
     */
    public String getTimeAnalysis(List<Simulation> simulationList) throws DAOException;
    
    /**
     * 
     * @param simulationList
     * @return
     * @throws DAOException 
     */
    public String getJobStatuses(List<Simulation> simulationList) throws DAOException;
    
    /**
     * 
     * @param currentUser
     * @param newUser
     * @throws DAOException 
     */
    public void updateUser(String currentUser, String newUser) throws DAOException;
}
