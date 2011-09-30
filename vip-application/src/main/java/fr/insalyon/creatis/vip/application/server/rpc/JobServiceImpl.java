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
package fr.insalyon.creatis.vip.application.server.rpc;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants.JobStatus;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.JobBusiness;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class JobServiceImpl extends AbstractRemoteServiceServlet implements JobService {

    private static Logger logger = Logger.getLogger(JobServiceImpl.class);
    private JobBusiness jobBusiness;

    public JobServiceImpl() {

        jobBusiness = new JobBusiness();
    }

    /**
     * 
     * @param simulationID
     * @return
     * @throws ApplicationException 
     */
    public Map<String, Integer> getStatusMap(String simulationID) throws ApplicationException {

        try {
            return jobBusiness.getStatusMap(simulationID);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @return
     * @throws ApplicationException 
     */
    public List<Job> getJobsList(String simulationID) throws ApplicationException {

        try {
            return jobBusiness.getJobsList(simulationID);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @param folder
     * @param fileName
     * @param extension
     * @return
     * @throws ApplicationException 
     */
    public String readFile(String simulationID, String folder, String fileName,
            String extension) throws ApplicationException {

        try {
            return jobBusiness.readFile(simulationID, folder, fileName, extension);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @param binSize
     * @return
     * @throws ApplicationException 
     */
    public List<String> getExecutionPerNumberOfJobs(String simulationID,
            int binSize) throws ApplicationException {

        try {
            return jobBusiness.getExecutionPerNumberOfJobs(simulationID, binSize);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @param binSize
     * @return
     * @throws ApplicationException 
     */
    public List<String> getDownloadPerNumberOfJobs(String simulationID,
            int binSize) throws ApplicationException {

        try {
            return jobBusiness.getDownloadPerNumberOfJobs(simulationID, binSize);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @param binSize
     * @return
     * @throws ApplicationException 
     */
    public List<String> getUploadPerNumberOfJobs(String simulationID,
            int binSize) throws ApplicationException {

        try {
            return jobBusiness.getUploadPerNumberOfJobs(simulationID, binSize);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @return
     * @throws ApplicationException 
     */
    public List<String> getJobsPertTime(String simulationID) throws ApplicationException {

        try {
            return jobBusiness.getJobsPertTime(simulationID);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @param siteName
     * @param nodeName
     * @return
     * @throws ApplicationException 
     */
    public Node getNode(String simulationID, String siteName, String nodeName)
            throws ApplicationException {

        try {
            return jobBusiness.getNode(simulationID, siteName, nodeName);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @param jobID
     * @param status
     * @throws ApplicationException 
     */
    public void sendSignal(String simulationID, String jobID, JobStatus status)
            throws ApplicationException {

        try {
            trace(logger, "Sending '" + status.name() + "' signal to '" + jobID
                    + "' (" + simulationID + ").");
            jobBusiness.sendSignal(simulationID, jobID, status);

        } catch (CoreException ex) {
            throw new ApplicationException(ex);
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }
}
