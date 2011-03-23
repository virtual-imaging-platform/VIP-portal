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
package fr.insalyon.creatis.vip.portal.server.dao;

import fr.insalyon.creatis.vip.portal.server.dao.derby.ApplicationData;
import fr.insalyon.creatis.vip.portal.server.dao.derby.ClassData;
import fr.insalyon.creatis.vip.portal.server.dao.derby.GatelabData;
import fr.insalyon.creatis.vip.portal.server.dao.derby.GroupData;
import fr.insalyon.creatis.vip.portal.server.dao.derby.JobData;
import fr.insalyon.creatis.vip.portal.server.dao.derby.NodeData;
import fr.insalyon.creatis.vip.portal.server.dao.derby.TissueData;
import fr.insalyon.creatis.vip.portal.server.dao.derby.UserData;
import fr.insalyon.creatis.vip.portal.server.dao.derby.WorkflowData;
import fr.insalyon.creatis.vip.portal.server.dao.derby.WorkflowInputData;

/**
 *
 * @author Rafael Silva
 */
public class DerbyDAOFactory extends DAOFactory {

    private static DAOFactory instance;

    // Singleton
    protected static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DerbyDAOFactory();
        }
        return instance;
    }

    private DerbyDAOFactory() {
    }

    @Override
    public WorkflowDAO getWorkflowDAO() throws DAOException {
        return WorkflowData.getInstance();
    }

    @Override
    public WorkflowInputDAO getWorkflowInputDAO() throws DAOException {
        return new WorkflowInputData();
    }

    @Override
    public ApplicationDAO getApplicationDAO() throws DAOException {
        return new ApplicationData();
    }

    @Override
    public ClassDAO getClassDAO() throws DAOException {
        return new ClassData();
    }

    @Override
    public JobDAO getJobDAO(String workflowID) throws DAOException {
        return new JobData(workflowID);
    }

    @Override
    public NodeDAO getNodeDAO(String workflowID) throws DAOException {
        return new NodeData(workflowID);
    }

    @Override
    public GatelabDAO getGatelabDAO(String workflowID) throws DAOException {
        return new GatelabData(workflowID);
    }

    @Override
    public GroupDAO getGroupDAO() throws DAOException {
        return new GroupData();
    }

    @Override
    public UserDAO getUserDAO() throws DAOException {
        return new UserData();
    }

    @Override
    public TissueDAO getTissueDAO() throws DAOException {
        return new TissueData();
    }
}
