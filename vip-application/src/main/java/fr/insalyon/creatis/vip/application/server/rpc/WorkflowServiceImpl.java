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
import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Workflow;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException;
import fr.insalyon.creatis.vip.application.server.business.InputsBusiness;
import fr.insalyon.creatis.vip.application.server.business.WorkflowBusiness;
import fr.insalyon.creatis.vip.application.server.dao.DAOFactory;
import fr.insalyon.creatis.vip.application.server.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.application.server.dao.derby.connection.JobsConnection;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.client.bean.Application;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowServiceImpl extends RemoteServiceServlet implements WorkflowService {

    private static final Logger logger = Logger.getLogger(WorkflowServiceImpl.class);

    @Override
    public List<Workflow> getWorkflows(String user, String application, String status, Date startDate, Date endDate) {
        try {
            if (endDate != null) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(endDate);
                c1.add(Calendar.DATE, 1);
                endDate = c1.getTime();
            }
            WorkflowDAO workflowDAO = DAOFactory.getDAOFactory().getWorkflowDAO();
            List<Workflow> workflows = workflowDAO.getList(user, application, status, startDate, endDate);
            WorkflowBusiness business = new WorkflowBusiness();

            for (Workflow workflow : workflows) {
                if (workflow.getMajorStatus().equals(ApplicationConstants.WorkflowStatus.Running.name())) {
                    try {
                        String workflowStatus = business.getStatus(workflow.getWorkflowID());

                        if (workflowStatus.equals(ApplicationConstants.MoteurStatus.COMPLETE.name())) {
                            workflowDAO.updateStatus(workflow.getWorkflowID(),
                                    ApplicationConstants.WorkflowStatus.Completed.name());
                            workflow.setMajorStatus(ApplicationConstants.WorkflowStatus.Completed.name());

                        } else if (workflowStatus.equals(ApplicationConstants.MoteurStatus.TERMINATED.name())
                                || workflowStatus.contains(ApplicationConstants.MoteurStatus.UNKNOWN.name())) {
                            workflowDAO.updateStatus(workflow.getWorkflowID(),
                                    ApplicationConstants.WorkflowStatus.Killed.name());
                            workflow.setMajorStatus(ApplicationConstants.WorkflowStatus.Killed.name());
                        }
                    } catch (BusinessException ex) {
                    }
                }
            }
            return workflows;

        } catch (DAOException ex) {
            return null;
        }
    }

    @Override
    public String getFile(String baseDir, String fileName) {
        try {
            FileReader fr = new FileReader(
                    ServerConfiguration.getInstance().getWorkflowsPath() + "/"
                    + baseDir + "/" + fileName);

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
        }
        return null;
    }

    @Override
    public String getFileURL(String baseDir, String fileName) {
        ServerConfiguration configuration = ServerConfiguration.getInstance();
        return "https://" + configuration.getApacheHost() + ":"
                + configuration.getApacheSSLPort()
                + "/workflows"
                + baseDir + "/" + fileName;
    }

    @Override
    public List<String>[] getApplicationsAndUsersList(String applicationClass) {
        try {
            fr.insalyon.creatis.vip.core.server.dao.DAOFactory daoFactory = fr.insalyon.creatis.vip.core.server.dao.DAOFactory.getDAOFactory();

            List<String> users = new ArrayList<String>();
            for (User user : daoFactory.getUserDAO().getUsers()) {
                users.add(user.getCanonicalName());
            }

            List<String> apps = new ArrayList<String>();
            for (Application app : daoFactory.getApplicationDAO().getApplications(applicationClass)) {
                apps.add(app.getName());
            }

            return new List[]{users, apps};

        } catch (DAOException ex) {
            return null;
        }
    }

    @Override
    public List<String> getLogs(String baseDir) {
        List<String> list = new ArrayList<String>();

        File folder = new File(ServerConfiguration.getInstance().getWorkflowsPath()
                + "/" + baseDir);

        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                list.add(f.getName() + "-#-Folder");
            } else {
                String fileSize = FileUtils.parseFileSize(f.length());
                String info = f.getName() + "##" + fileSize
                        + "##" + new Date(f.lastModified());
                list.add(info + "-#-File");
            }
        }
        return list;
    }

    public List<String> getWorkflowSources(String user, String proxyFileName, 
            String workflowName) throws ApplicationException {

        try {
            WorkflowBusiness business = new WorkflowBusiness();
            return business.getWorkflowSources(user, proxyFileName, workflowName);

        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    public String getWorkflowInputs(String fileName) {

        try {
            InputsBusiness business = new InputsBusiness();
            return business.getWorkflowInputs(fileName);

        } catch (BusinessException ex) {
            logger.error(ex);
            return null;
        }
    }

    public String launchWorkflow(String user, Map<String, String> parametersMap,
            String workflowName, String proxyFileName) {

        try {
            WorkflowBusiness business = new WorkflowBusiness();
            return business.launch(user, parametersMap, workflowName, proxyFileName);

        } catch (BusinessException ex) {
            return null;
        }
    }

    public void addSimulationInput(String user, SimulationInput workflowInput) throws ApplicationException {
        
        try {
            WorkflowBusiness business = new WorkflowBusiness();
            business.addSimulationInput(user, workflowInput);
            
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }
    
    public void updateSimulationInput(String user, SimulationInput workflowInput) throws ApplicationException {
        
        try {
            WorkflowBusiness business = new WorkflowBusiness();
            business.updateSimulationInput(user, workflowInput);
            
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    public List<SimulationInput> getWorkflowsInputByUser(String user) {
        try {
            return DAOFactory.getDAOFactory().getWorkflowInputDAO().getWorkflowInputByUser(user);
        } catch (DAOException ex) {
            return null;
        }
    }

    public List<SimulationInput> getWorkflowsInputByUserAndAppName(String user, String appName) {
        try {
            return DAOFactory.getDAOFactory().getWorkflowInputDAO().getWorkflowInputByUserAndAppName(user, appName);
        } catch (DAOException ex) {
            return null;
        }
    }

    public SimulationInput getInputByNameUserApp(String user, String name, String appName) throws ApplicationException {
        try {
            WorkflowBusiness business = new WorkflowBusiness();
            return business.getInputByUserAndName(user, name, appName);
            
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }

    public void removeWorkflowInput(String user, String inputName, String application) {
        try {
            DAOFactory.getDAOFactory().getWorkflowInputDAO().removeWorkflowInput(user, inputName, application);
        } catch (DAOException ex) {
        }
    }

    public void closeConnection(String workflowID) {
        try {
            JobsConnection.getInstance().close(ServerConfiguration.getInstance().getWorkflowsPath() + "/" + workflowID + "/jobs.db");
        } catch (DAOException ex) {
        }
    }

    public List<String> getStats(List<Workflow> workflowIdList, int type, int binSize) {
        try {
            return DAOFactory.getDAOFactory().getWorkflowDAO().getStats(workflowIdList, type, binSize);
        } catch (DAOException ex) {
            return null;
        }

    }

    public void killWorkflow(String workflowID) {
        try {
            WorkflowBusiness business = new WorkflowBusiness();
            business.kill(workflowID);
        } catch (BusinessException ex) {
        }
    }

    public void cleanWorkflow(String workflowID, String userDN, String proxyFileName) {
        try {
            WorkflowBusiness business = new WorkflowBusiness();
            business.clean(workflowID, userDN, proxyFileName);
        } catch (BusinessException ex) {
        }
    }

    public void purgeWorkflow(String workflowID) {
        try {
            WorkflowBusiness business = new WorkflowBusiness();
            business.purge(workflowID);
        } catch (BusinessException ex) {
        }
    }

    public List<InOutData> getOutputData(String simulationID) throws ApplicationException {
        try {
            WorkflowBusiness business = new WorkflowBusiness();
            return business.getOutputData(simulationID);
            
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }
    
    public List<InOutData> getInputData(String simulationID) throws ApplicationException {
        try {
            WorkflowBusiness business = new WorkflowBusiness();
            return business.getInputData(simulationID);
            
        } catch (BusinessException ex) {
            throw new ApplicationException(ex);
        }
    }
}
