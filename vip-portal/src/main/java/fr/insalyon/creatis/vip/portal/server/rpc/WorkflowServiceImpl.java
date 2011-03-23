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
package fr.insalyon.creatis.vip.portal.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.vip.portal.client.bean.Workflow;
import fr.insalyon.creatis.vip.portal.client.bean.WorkflowInput;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.server.business.BusinessException;
import fr.insalyon.creatis.vip.portal.server.dao.DAOException;
import fr.insalyon.creatis.vip.portal.server.dao.DAOFactory;
import fr.insalyon.creatis.vip.portal.server.business.simulation.parser.InputParser;
import fr.insalyon.creatis.vip.portal.server.dao.derby.connection.JobsConnection;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.portal.server.business.WorkflowBusiness;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowServiceImpl extends RemoteServiceServlet implements WorkflowService {

    @Override
    public List<Workflow> getWorkflows(String user, String application, String status, Date startDate, Date endDate) {
        try {
            if (endDate != null) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(endDate);
                c1.add(Calendar.DATE, 1);
                endDate = c1.getTime();
            }
            return DAOFactory.getDAOFactory().getWorkflowDAO().getList(user, application, status, startDate, endDate);

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
            ex.printStackTrace();
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
            List<String>[] list = new List[2];
            list[0] = DAOFactory.getDAOFactory().getWorkflowDAO().getUsers();
            list[1] = DAOFactory.getDAOFactory().getWorkflowDAO().getApplicationsWithClass(applicationClass);
            return list;
            
        } catch (DAOException ex) {
            return null;
        }
    }

    @Override
    public List<String> getLogDir(String baseDir) {
        List<String> dirsList = new ArrayList<String>();

        File workflowDir = new File(ServerConfiguration.getInstance().getWorkflowsPath()
                + "/" + baseDir);

        for (File d : workflowDir.listFiles()) {
            if (d.isDirectory()) {
                dirsList.add(d.getName());
            }
        }

        return dirsList;
    }

    public List<String> getLogFiles(String baseDir) {
        List<String> filesList = new ArrayList<String>();

        File workflowDir = new File(ServerConfiguration.getInstance().getWorkflowsPath()
                + "/" + baseDir);

        for (File f : workflowDir.listFiles()) {
            if (!f.isDirectory()) {
                String fileSize = f.length() + "";
                if (f.length() >= 1024) {
                    if (f.length() / 1024 >= 1024) {
                        fileSize = f.length() / 1024 / 1024 + " MB";
                    } else {
                        fileSize = f.length() / 1024 + " KB";
                    }
                }
                String info = f.getName() + "##" + fileSize
                        + "##" + new Date(f.lastModified()) + "##"
                        + baseDir;
                filesList.add(info);
            }
        }

        return filesList;
    }

    public List<String> getWorkflowSources(String user, String proxyFileName, String workflowName) {

        try {       
            WorkflowBusiness business = new WorkflowBusiness();
            return business.getWorkflowSources(user, proxyFileName, workflowName);

        } catch (BusinessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Map<String, String> getWorkflowInputs(String fileName) {
        return new InputParser().parse(fileName);
    }

    public String launchWorkflow(String user, Map<String, String> parametersMap,
            String workflowName, String proxyFileName) {
        
        try {
            WorkflowBusiness business = new WorkflowBusiness();
            return business.launch(user, parametersMap, workflowName, proxyFileName);
        
        } catch (BusinessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String addWorkflowInput(WorkflowInput workflowInput) {
        try {
            HttpSession session = this.getThreadLocalRequest().getSession();
            String user = (String) session.getAttribute("userDN");
            return DAOFactory.getDAOFactory().getWorkflowInputDAO().addWorkflowInput(user, workflowInput);
        } catch (DAOException ex) {
            return null;
        }
    }

    public List<WorkflowInput> getWorkflowsInputByUserAndAppName(String appName) {
        try {
            HttpSession session = this.getThreadLocalRequest().getSession();
            String user = (String) session.getAttribute("userDN");
            return DAOFactory.getDAOFactory().getWorkflowInputDAO().getWorkflowInputByUserAndAppName(user, appName);
        } catch (DAOException ex) {
            return null;
        }
    }

    public WorkflowInput getWorkflowInputByUserAndName(String inputName) {
        try {
            HttpSession session = this.getThreadLocalRequest().getSession();
            String user = (String) session.getAttribute("userDN");
            return DAOFactory.getDAOFactory().getWorkflowInputDAO().getWorkflowInputByUserAndName(user, inputName);
        } catch (DAOException ex) {
            return null;
        }
    }

    public void removeWorkflowInput(String inputName) {
        try {
            HttpSession session = this.getThreadLocalRequest().getSession();
            String user = (String) session.getAttribute("userDN");
            DAOFactory.getDAOFactory().getWorkflowInputDAO().removeWorkflowInput(user, inputName);
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection(String workflowID) {
        try {
            JobsConnection.getInstance().close(ServerConfiguration.getInstance().getWorkflowsPath() + "/" + workflowID + "/jobs.db");
        } catch (DAOException ex) {
            ex.printStackTrace();
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
            DAOFactory.getDAOFactory().getWorkflowDAO().updateStatus(workflowID, "Killed");
            //        try {
            //            int[] moteurData = DAOFactory.getDAOFactory().getWorkflowDAO().getMoteurIDAndKey(workflowID);
            //            ClientPreferences prefs = new ClientPreferences(new File("/var/www/cgi-bin/moteurServer/.moteur2"));
            //            System.out.println("----- Moteur ID: " + moteurData[0] + " - KEY: " + moteurData[1]);
            //            Moteur2.setLog(new Log(1));
            //            Moteur2 client = new Moteur2(moteurData[0], moteurData[1], new ExecutionLogger(new Log(1)), null);
            //            client.stopExecution();
            //
            //        } catch (SAXException ex) {
            //            ex.printStackTrace();
            //        } catch (IOException ex) {
            //            ex.printStackTrace();
            //        } catch (MoteurException ex) {
            //        }
            //        }
        } catch (DAOException ex) {
            ex.printStackTrace();
        }
    }
}
