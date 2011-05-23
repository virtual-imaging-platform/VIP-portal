/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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

import fr.insalyon.creatis.vip.application.client.bean.Workflow;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public interface WorkflowDAO {

    /**
     * Gets the list of workflows submitted by a user filtered by application
     * name, start date and/or end date.
     * 
     * @param user User name
     * @param app Application name
     * @param status Application status
     * @param sDate Start date
     * @param eDate End date
     * @return List of workflows filtered
     */
    public List<Workflow> getList(String user, String app, String status, Date sDate, Date eDate);

    /**
     * Gets the list of applications submitted.
     *
     * @return List of applications
     */
    public List<String> getApplications();

    /**
     * Gets the moteur identification and key for a specific workflow.
     *
     * @param workflowID Workflow identification
     * @return Array with moteur identification at position 0 and key at position 1
     */
    public int[] getMoteurIDAndKey(String workflowID);

    /**
     * 
     * @param workflowID
     * @param status
     */
    public void updateStatus(String workflowID, String status);

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

    public List<String> getStats(List<Workflow> workflowIdList, int type, int binSize);
}
