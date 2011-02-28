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
import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.vip.portal.client.bean.Workflow;
import fr.insalyon.creatis.vip.portal.client.bean.WorkflowDescriptor;
import fr.insalyon.creatis.vip.portal.client.bean.WorkflowInput;
import fr.insalyon.creatis.vip.portal.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.portal.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.portal.server.business.simulation.WorkflowMoteurConfig;
import fr.insalyon.creatis.vip.portal.server.dao.DAOFactory;
import fr.insalyon.creatis.vip.portal.server.business.simulation.parser.GwendiaParser;
import fr.insalyon.creatis.vip.portal.server.business.simulation.parser.InputParser;
import fr.insalyon.creatis.vip.portal.server.business.simulation.parser.ScuflParser;
import fr.insalyon.creatis.vip.portal.server.dao.derby.connection.JobsConnection;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;
import org.xml.sax.SAXException;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowServiceImpl extends RemoteServiceServlet implements WorkflowService {

    @Override
    public List<Workflow> getWorkflows(String user, String application, String status, Date startDate, Date endDate) {
        if (endDate != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(endDate);
            c1.add(Calendar.DATE, 1);
            endDate = c1.getTime();
        }

        return DAOFactory.getDAOFactory().getWorkflowDAO().getList(user, application, status, startDate, endDate);
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
        List<String>[] list = new List[2];

        list[0] = DAOFactory.getDAOFactory().getWorkflowDAO().getUsers();
        list[1] = DAOFactory.getDAOFactory().getWorkflowDAO().getApplicationsWithClass(applicationClass);

        return list;
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

    public List<String> getWorkflowSources(String proxyFileName, String workflowName) {

        try {
            WorkflowDescriptor wd = DAOFactory.getDAOFactory().getApplicationDAO().getApplication(workflowName);
            URI uri = new URI(wd.getLfn());

            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            String workflowPath = client.getRemoteFile(uri.getPath(), new File("").getAbsolutePath() + "/workflows");

            if (workflowPath.endsWith(".gwendia")) {
                return new GwendiaParser().parse(workflowPath).getSources();
            } else {
                return new ScuflParser().parse(workflowPath).getSources();
            }

        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (VletAgentClientException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getWorkflowInputs(String fileName) {
        return new InputParser().parse(fileName);
    }

    public String launchWorkflow(Map<String, String> parametersMap, String workflowName, String proxyFileName) {

        try {
            String settings = "GRID=DIRAC\n"
                    + "SE=ccsrm02.in2p3.fr\n"
                    + "TIMEOUT=100000\n"
                    + "RETRYCOUNT=3\n"
                    + "MULTIJOB=1";

            List<ParameterSweep> parameters = new ArrayList<ParameterSweep>();
            for (String name : parametersMap.keySet()) {
                ParameterSweep ps = new ParameterSweep(name);
                String valuesStr = parametersMap.get(name);

                if (valuesStr.contains("##")) {
                    String[] values = valuesStr.split("##");
                    if (values.length != 3) {
                        throw (new ServiceException("Error in range"));
                    }
                    Double start = Double.parseDouble(values[0]);
                    Double stop = Double.parseDouble(values[1]);
                    Double step = Double.parseDouble(values[2]);

                    for (double d = start; d <= stop; d += step) {
                        ps.addValue(d + "");
                    }
//                    for (String v : values) {
//                        ps.addValue(v);
//                    }
                } else if (valuesStr.contains("@@")) {
                    String[] values = valuesStr.split("@@");
                    for (String v : values) {
                        ps.addValue(v);
                    }
                } else {
                    ps.addValue(valuesStr);
                }
                parameters.add(ps);
            }

            WorkflowDescriptor wd = DAOFactory.getDAOFactory().getApplicationDAO().getApplication(workflowName);
            String lfnPath = wd.getLfn().substring(wd.getLfn().lastIndexOf("/") + 1);
            String workflowPath = new File("").getAbsolutePath() + "/workflows/" + new File(lfnPath).getName();

            WorkflowMoteurConfig moteur = new WorkflowMoteurConfig(ServerConfiguration.getInstance().getMoteurServer(), workflowPath, parameters);
            moteur.setSettings(settings);
            String ws = moteur.callWS(proxyFileName);

            return ws;

        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (ServiceException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String addWorkflowInput(WorkflowInput workflowInput) {
        HttpSession session = this.getThreadLocalRequest().getSession();
        String user = (String) session.getAttribute("userDN");
        return DAOFactory.getDAOFactory().getWorkflowInputDAO().addWorkflowInput(user, workflowInput);
    }

    public List<WorkflowInput> getWorkflowsInputByUserAndAppName(String appName) {
        HttpSession session = this.getThreadLocalRequest().getSession();
        String user = (String) session.getAttribute("userDN");
        return DAOFactory.getDAOFactory().getWorkflowInputDAO().getWorkflowInputByUserAndAppName(user, appName);
    }

    public WorkflowInput getWorkflowInputByUserAndName(String inputName) {
        HttpSession session = this.getThreadLocalRequest().getSession();
        String user = (String) session.getAttribute("userDN");
        return DAOFactory.getDAOFactory().getWorkflowInputDAO().getWorkflowInputByUserAndName(user, inputName);
    }

    public void removeWorkflowInput(String inputName) {
        HttpSession session = this.getThreadLocalRequest().getSession();
        String user = (String) session.getAttribute("userDN");
        DAOFactory.getDAOFactory().getWorkflowInputDAO().removeWorkflowInput(user, inputName);
    }

    public void closeConnection(String workflowID) {
        JobsConnection.getInstance().close(ServerConfiguration.getInstance().getWorkflowsPath() + "/" + workflowID + "/jobs.db");
    }

    public List<String> getStats(List<Workflow> workflowIdList, int type, int binSize) {
        return DAOFactory.getDAOFactory().getWorkflowDAO().getStats(workflowIdList, type, binSize);

    }

    public void killWorkflow(String workflowID) {
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
//            ex.printStackTrace();
//        }
    }
}
