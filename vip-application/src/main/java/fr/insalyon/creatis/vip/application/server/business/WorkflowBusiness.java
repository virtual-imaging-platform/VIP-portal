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

import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.agent.vlet.client.VletAgentPoolClient;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.SimulationInput;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.server.business.simulation.ParameterSweep;
import fr.insalyon.creatis.vip.application.server.business.simulation.WorkflowMoteurConfig;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.GwendiaParser;
import fr.insalyon.creatis.vip.application.server.business.simulation.parser.ScuflParser;
import fr.insalyon.creatis.vip.application.server.dao.DAOFactory;
import fr.insalyon.creatis.vip.application.server.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.client.bean.Application;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.ServiceException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowBusiness {

    private static final Logger logger = Logger.getLogger(WorkflowBusiness.class);
    
    /**
     * 
     * @param user
     * @param proxyFileName
     * @param workflowName
     * @return
     * @throws BusinessException
     */
    public List<String> getWorkflowSources(String user, String proxyFileName,
            String workflowName) throws BusinessException {

        try {
            Application wd = fr.insalyon.creatis.vip.core.server.dao.DAOFactory.getDAOFactory().getApplicationDAO().getApplication(workflowName);

            ServerConfiguration conf = ServerConfiguration.getInstance();
            URI uri = new URI("lfn://" + conf.getDataManagerLFCHost()
                    + ":" + conf.getDataManagerLFCPort()
                    + DataManagerUtil.parseBaseDir(user, wd.getLfn()));

            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            String workflowPath = client.getRemoteFile(uri.getPath(),
                    System.getenv("HOME") + "/.platform/workflows/"
                    + uri.getPath().substring(0, uri.getPath().lastIndexOf("/")));

            if (workflowPath.endsWith(".gwendia")) {
                return new GwendiaParser().parse(workflowPath).getSources();
            } else {
                return new ScuflParser().parse(workflowPath).getSources();
            }

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (URISyntaxException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (IOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (SAXException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (VletAgentClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param user
     * @param parametersMap
     * @param workflowName
     * @param proxyFileName
     * @param simulationName
     * @return
     * @throws BusinessException
     */
    public String launch(String user, Map<String, String> parametersMap,
            String workflowName, String proxyFileName, String simulationName) 
            throws BusinessException {

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
                } else if (valuesStr.contains("@@")) {
                    String[] values = valuesStr.split("@@");
                    for (String v : values) {
                        ps.addValue(DataManagerUtil.parseBaseDir(user, v.trim()));
                    }
                } else {
                    ps.addValue(DataManagerUtil.parseBaseDir(user, valuesStr.trim()));
                }
                parameters.add(ps);
            }

            Application wd = fr.insalyon.creatis.vip.core.server.dao.DAOFactory.getDAOFactory().getApplicationDAO().getApplication(workflowName);

            String path = DataManagerUtil.parseBaseDir(user, wd.getLfn());
            String workflowPath = System.getenv("HOME") + "/.platform/workflows/" + path;

            WorkflowMoteurConfig moteur = new WorkflowMoteurConfig(ServerConfiguration.getInstance().getMoteurServer(), workflowPath, parameters);
            moteur.setSettings(settings);
            String ws = moteur.launch(proxyFileName);
            
            String workflowID = ws.substring(ws.lastIndexOf("/") + 1, ws.lastIndexOf("."));
            DAOFactory.getDAOFactory().getWorkflowDAO().add(new Simulation(
                    workflowName, workflowID, user, new Date(), simulationName, "Running"));
            
            return workflowID;

        } catch (DataManagerException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (RemoteException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (ServiceException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param workflowID
     * @throws BusinessException
     */
    public void kill(String workflowID) throws BusinessException {

        try {
            WorkflowMoteurConfig moteur = new WorkflowMoteurConfig(
                    ServerConfiguration.getInstance().getMoteurServer());
            moteur.kill(workflowID);

        } catch (RemoteException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (ServiceException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     *
     * @param workflowID
     * @param userDN 
     * @param proxyFileName 
     * @throws BusinessException
     */
    public void clean(String workflowID, String userDN, String proxyFileName) throws BusinessException {

        try {
            WorkflowDAO workflowDAO = DAOFactory.getDAOFactory().getWorkflowDAO();
            workflowDAO.updateStatus(workflowID, "Cleaned");
            String workflowsPath = ServerConfiguration.getInstance().getWorkflowsPath();
            File workflowDir = new File(workflowsPath + "/" + workflowID);

            for (File file : workflowDir.listFiles()) {
                if (!file.getName().equals("jobs.db")
                        && !file.getName().equals("workflow.out")
                        && !file.getName().equals("workflow.err")
                        && !file.getName().equals("gasw.log")) {

                    FileUtils.deleteQuietly(file);
                }
            }
            List<String> outputs = workflowDAO.getOutputs(workflowID);
            VletAgentPoolClient client = new VletAgentPoolClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            for (String output : outputs) {
                client.delete(output, userDN);
            }
            workflowDAO.cleanWorkflow(workflowID);

        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (VletAgentClientException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param workflowID
     * @throws BusinessException
     */
    public void purge(String workflowID) throws BusinessException {

        try {
            WorkflowDAO workflowDAO = DAOFactory.getDAOFactory().getWorkflowDAO();
            workflowDAO.delete(workflowID);

            String workflowsPath = ServerConfiguration.getInstance().getWorkflowsPath();
            File workflowDir = new File(workflowsPath + "/" + workflowID);
            FileUtils.deleteQuietly(workflowDir);

        } catch (DAOException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @throws BusinessException 
     */
    public String getStatus(String simulationID) throws BusinessException {

        try {
            WorkflowMoteurConfig moteur = new WorkflowMoteurConfig(
                    ServerConfiguration.getInstance().getMoteurServer());
            return moteur.getStatus(simulationID);

        } catch (RemoteException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        } catch (ServiceException ex) {
            logger.error(ex);
            throw new BusinessException(ex);
        }
    }
    
    public List<InOutData> getOutputData(String simulationID) throws BusinessException {
        
        try {
            return DAOFactory.getDAOFactory().getWorkflowDAO().getInOutData(simulationID, "Outputs");
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
    
    public List<InOutData> getInputData(String simulationID) throws BusinessException {
        
        try {
            return DAOFactory.getDAOFactory().getWorkflowDAO().getInOutData(simulationID, "Inputs");
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
    
    public void addSimulationInput(String user, SimulationInput workflowInput) 
            throws BusinessException {
        
        try {
            DAOFactory.getDAOFactory().getWorkflowInputDAO()
                    .addSimulationInput(user, workflowInput);
            
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
    
    public void updateSimulationInput(String user, SimulationInput workflowInput) 
            throws BusinessException {
        
        try {
            DAOFactory.getDAOFactory().getWorkflowInputDAO()
                    .updateSimulationInput(user, workflowInput);
            
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
    
    public SimulationInput getInputByUserAndName(String user, String name, 
            String appName) throws BusinessException {
        
        try {
            return DAOFactory.getDAOFactory().getWorkflowInputDAO()
                    .getInputByNameUserApp(user, name, appName);
            
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
