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

package fr.insalyon.creatis.vip.application.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.client.bean.Node;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Silva
 */
public interface JobService extends RemoteService {

    public static final String SERVICE_URI = "/jobservice";

    public static class Util {

        public static JobServiceAsync getInstance() {

            JobServiceAsync instance = (JobServiceAsync) GWT.create(JobService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }

    public Map<String, Integer> getStatusMap(String simulationID) throws ApplicationException ;

    public List<Job> getJobsList(String simulationID) throws ApplicationException;

    public String readFile(String simulationID, String dir, String fileName, String ext) throws ApplicationException ;

    public List<String> getExecutionPerNumberOfJobs(String simulationID, int binSize) throws ApplicationException ;

    public List<String> getDownloadPerNumberOfJobs(String simulationID, int binSize) throws ApplicationException ;

    public List<String> getUploadPerNumberOfJobs(String simulationID, int binSize) throws ApplicationException ;

    public List<String> getJobFlow(String simulationID) throws ApplicationException ;
    
    public List<String> getCkptsPerJob(String simulationID) throws ApplicationException ;

    public List<String> getSiteHistogram(String simulationID) throws ApplicationException;
    
    public Node getNode(String simulationID, String siteName, String nodeName) throws ApplicationException ;
    
    public void sendSignal(String simulationID, String jobID, ApplicationConstants.JobStatus status) throws ApplicationException;
    
    public void sendSignal(String simulationID, List<String> jobIDs, ApplicationConstants.JobStatus status) throws ApplicationException;
    
    public Map<String, Integer> getCountriesMap(String simulationID) throws ApplicationException;
}
