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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.client.rpc.JobService;
import fr.insalyon.creatis.vip.application.server.dao.DAOFactory;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
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
public class JobServiceImpl extends RemoteServiceServlet implements JobService {

    private static Logger logger = Logger.getLogger(JobServiceImpl.class);
    
    public Map<String, Integer> getStatusMap(String workflowID) {
        try {
            return DAOFactory.getDAOFactory().getJobDAO(workflowID).getStatusMap();
        } catch (DAOException ex) {
            return null;
        }
    }

    public List<Job> getJobsList(String workflowID) {
        try {
            return DAOFactory.getDAOFactory().getJobDAO(workflowID).getJobs();
        } catch (DAOException ex) {
            return null;
        }
    }

    public String getFile(String workflowID, String dir, String fileName, String ext) {
        try {
            fileName += ext;
            FileReader fr = new FileReader(ServerConfiguration.getInstance().getWorkflowsPath() + "/" + workflowID + "/" + dir + "/" + fileName);
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
            return null;
        }
    }

    public List<String> getExecutionPerNumberOfJobs(String workflowID, int binSize) {
        try {
            return DAOFactory.getDAOFactory().getJobDAO(workflowID).getExecutionPerNumberOfJobs(binSize);
        } catch (DAOException ex) {
            return null;
        }
    }

    public List<String> getDownloadPerNumberOfJobs(String workflowID, int binSize) {
        try {
            return DAOFactory.getDAOFactory().getJobDAO(workflowID).getDownloadPerNumberOfJobs(binSize);
        } catch (DAOException ex) {
            return null;
        }
    }

    public List<String> getUploadPerNumberOfJobs(String workflowID, int binSize) {
        try {
            return DAOFactory.getDAOFactory().getJobDAO(workflowID).getUploadPerNumberOfJobs(binSize);
        } catch (DAOException ex) {
            return null;
        }
    }

    public List<String> getJobsPertTime(String workflowID) {
        try {
            return DAOFactory.getDAOFactory().getJobDAO(workflowID).getJobsPerTime();
        } catch (DAOException ex) {
            return null;
        }
    }

    public Node getNode(String workflowID, String siteName, String nodeName) {
        try {
            return DAOFactory.getDAOFactory().getNodeDAO(workflowID).getNode(siteName, nodeName);
        } catch (DAOException ex) {
            return null;
        }
    }
    
    public void sendSignal(String workflowID, String jobID, ApplicationConstants.JobStatus status) {
        try {
            DAOFactory.getDAOFactory().getJobDAO(workflowID).sendSignal(jobID, status);
        } catch (DAOException ex) {
        }
    }
}
