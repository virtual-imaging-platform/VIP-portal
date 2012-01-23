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
package fr.insalyon.creatis.vip.application.server.business;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.JobStatus;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.server.dao.JobDAO;
import fr.insalyon.creatis.vip.application.server.dao.WorkflowDAOFactory;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class JobBusiness {

    private static final Logger logger = Logger.getLogger(JobBusiness.class);

    /**
     *
     * @param simulationID
     * @return
     * @throws BusinessException
     */
    public Map<String, Integer> getStatusMap(String simulationID) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID).getStatusMap();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws BusinessException
     */
    public List<Job> getJobsList(String simulationID) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID).getJobs();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param folder
     * @param fileName
     * @param extension
     * @return
     * @throws BusinessException
     */
    public String readFile(String simulationID, String folder, String fileName,
            String extension) throws BusinessException {

        try {
            fileName += extension;
            FileReader fr = new FileReader(Server.getInstance().getWorkflowsPath()
                    + "/" + simulationID + "/" + folder + "/" + fileName);
            BufferedReader br = new BufferedReader(fr);

            String strLine;
            StringBuilder sb = new StringBuilder();

            while ((strLine = br.readLine()) != null) {
                sb.append(strLine);
                sb.append("\n");
            }

            br.close();
            fr.close();
            return sb.toString();

        } catch (IOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param binSize
     * @return
     * @throws BusinessException
     */
    public List<String> getExecutionPerNumberOfJobs(String simulationID,
            int binSize) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID).getExecutionPerNumberOfJobs(binSize);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param binSize
     * @return
     * @throws BusinessException
     */
    public List<String> getDownloadPerNumberOfJobs(String simulationID,
            int binSize) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID).getDownloadPerNumberOfJobs(binSize);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param binSize
     * @return
     * @throws BusinessException
     */
    public List<String> getUploadPerNumberOfJobs(String simulationID,
            int binSize) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID).getUploadPerNumberOfJobs(binSize);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws BusinessException
     */
    public List<String> getJobsPertTime(String simulationID) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID).getJobsPerTime();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @return
     * @throws BusinessException
     */
    public List<String> getCkptsPerJob(String simulationID) throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID).getCkptsPerJob();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param siteName
     * @param nodeName
     * @return
     * @throws BusinessException
     */
    public Node getNode(String simulationID, String siteName, String nodeName)
            throws BusinessException {

        try {
            return WorkflowDAOFactory.getDAOFactory().getNodeDAO(simulationID).getNode(siteName, nodeName);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulationID
     * @param jobID
     * @param status
     * @throws BusinessException
     */
    public void sendSignal(String simulationID, String jobID, JobStatus status)
            throws BusinessException {

        try {
            WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID).sendSignal(jobID, status);

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
    
    /**
     * 
     * @param simulationID
     * @param jobIDs
     * @param status
     * @throws BusinessException 
     */
    public void sendSignal(String simulationID, List<String> jobIDs, 
            JobStatus status) throws BusinessException {
        
        try {
            JobDAO jobDAO = WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID);
            for (String jobID : jobIDs) {
                jobDAO.sendSignal(jobID, status);
            }
            
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<String> getSiteHistogram(String simulationID) throws BusinessException {
        try {
            return WorkflowDAOFactory.getDAOFactory().getJobDAO(simulationID).getSiteHistogram();

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param simulations
     * @param status
     * @return
     * @throws BusinessException
     */
    public int getNumberOfTasks(List<Simulation> simulations, ApplicationConstants.JobStatus status) throws BusinessException {

        try {
            int tasks = 0;
            for (Simulation simulation : simulations) {
                tasks += WorkflowDAOFactory.getDAOFactory().getJobDAO(simulation.getID()).getNumberOfTasks(status);
            }
            return tasks;

        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
