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
package fr.insalyon.creatis.vip.application.server.dao.derby;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants.ProcessorStatus;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants.SimulationStatus;
import fr.insalyon.creatis.vip.application.client.bean.InOutData;
import fr.insalyon.creatis.vip.application.client.bean.Processor;
import fr.insalyon.creatis.vip.application.client.bean.Simulation;
import fr.insalyon.creatis.vip.application.server.dao.WorkflowDAO;
import fr.insalyon.creatis.vip.application.server.dao.derby.connection.WorkflowsConnection;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowData implements WorkflowDAO {

    private final static Logger logger = Logger.getLogger(WorkflowData.class);
    private static WorkflowData instance;
    private Connection connection;

    /**
     * Pattern Singleton: Gets an unique instance of WorkflowData.
     *
     * @return Unique instance of WorkflowData
     */
    public static WorkflowData getInstance() throws DAOException {
        if (instance == null) {
            instance = new WorkflowData();
        }
        return instance;
    }

    private WorkflowData() throws DAOException {
        connection = WorkflowsConnection.getInstance().getConnection();
    }

    /**
     * 
     * @param workflow
     * @throws DAOException 
     */
    public void add(Simulation workflow) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO "
                    + "Workflows(id, application, simulation_name, "
                    + "username, launched, status) "
                    + "VALUES(?, ?, ?, ?, ?, ?)");

            ps.setString(1, workflow.getID());
            ps.setString(2, workflow.getApplication());
            ps.setString(3, workflow.getSimulationName());
            ps.setString(4, workflow.getUserName());
            ps.setTimestamp(5, new Timestamp(workflow.getDate().getTime()));
            ps.setString(6, workflow.getMajorStatus());

            ps.execute();

        } catch (SQLException ex) {
            if (!ex.getMessage().contains("duplicate key value")) {
                logger.error(ex);
                throw new DAOException(ex);
            } else {
                update(workflow);
            }

        }
    }

    /**
     * 
     * @param workflow
     * @throws DAOException 
     */
    public void update(Simulation workflow) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "Workflows "
                    + "SET application=?, simulation_name=?, username = ? "
                    + "WHERE id=?");

            ps.setString(1, workflow.getApplication());
            ps.setString(2, workflow.getSimulationName());
            ps.setString(3, workflow.getUserName());
            ps.setString(4, workflow.getID());

            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex.getMessage());
        }
    }

    /**
     * 
     * @param workflowID
     * @return
     * @throws DAOException 
     */
    public Simulation get(String workflowID) throws DAOException {

        try {

            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "id, application, username, launched, status, "
                    + "minor_status, simulation_name "
                    + "FROM Workflows WHERE id = ?");

            stat.setString(1, workflowID);

            ResultSet rs = stat.executeQuery();
            rs.next();

            return new Simulation(
                    rs.getString("application"),
                    rs.getString("id"),
                    rs.getString("username"),
                    new Date(rs.getTimestamp("launched").getTime()),
                    rs.getString("status"),
                    rs.getString("minor_status"),
                    rs.getString("simulation_name"));

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * Gets the list of workflows submitted by a user filtered by application
     * name, status, start date and/or end date.
     *
     * @param user User name
     * @param app Application name
     * @param status Application status
     * @param sDate Start date
     * @param eDate End date
     * @return List of workflows filtered
     */
    public List<Simulation> getList(String user, String app, String status,
            Date sDate, Date eDate) throws DAOException {

        try {
            String query = "";
            query = parseQuery(user, query, "username=?");
            query = parseQuery(app, query, "application LIKE ?");
            query = parseQuery(status, query, "status=?");
            query = parseQuery(sDate, query, "launched>=?");
            query = parseQuery(eDate, query, "launched<=?");
            if (!query.equals("")) {
                query = "WHERE " + query;
            }

            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "id, application, username, launched, status, "
                    + "minor_status, simulation_name "
                    + "FROM Workflows " + query + " "
                    + "ORDER BY launched DESC");

            int index = 1;
            if (user != null && !user.equals("")) {
                stat.setString(index++, user);
            }
            if (app != null && !app.equals("")) {
                stat.setString(index++, "%" + app + "%");
            }
            if (status != null && !status.equals("")) {
                stat.setString(index++, status);
            }
            if (sDate != null) {
                stat.setTimestamp(index++, new Timestamp(sDate.getTime()));
            }
            if (eDate != null) {
                stat.setTimestamp(index++, new Timestamp(eDate.getTime()));
            }

            return processResultSet(stat.executeQuery());

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * Gets the list of workflows submitted by a list of users filtered by 
     * application name, status, start date and/or end date.
     *
     * @param user List of users
     * @param app Application name
     * @param status Application status
     * @param sDate Start date
     * @param eDate End date
     * @return List of workflows filtered
     */
    public List<Simulation> getList(List<String> users, String app, String status,
            Date sDate, Date eDate) throws DAOException {

        try {

            StringBuilder sb = new StringBuilder();
            if (!users.isEmpty()) {
                sb.append("(");
                for (String userName : users) {
                    if (sb.length() > 1) {
                        sb.append(" OR ");
                    }
                    sb.append("username = '" + userName + "'");
                }
                sb.append(")");
            }

            String query = "";
            query = parseQuery(sb.toString(), query, sb.toString());
            query = parseQuery(app, query, "application LIKE ?");
            query = parseQuery(status, query, "status=?");
            query = parseQuery(sDate, query, "launched>=?");
            query = parseQuery(eDate, query, "launched<=?");
            if (!query.equals("")) {
                query = "WHERE " + query;
            }

            PreparedStatement stat = connection.prepareStatement("SELECT "
                    + "id, application, username, launched, status, "
                    + "minor_status, simulation_name "
                    + "FROM Workflows " + query + " "
                    + "ORDER BY launched DESC");

            int index = 1;
            if (app != null && !app.equals("")) {
                stat.setString(index++, "%" + app + "%");
            }
            if (status != null && !status.equals("")) {
                stat.setString(index++, status);
            }
            if (sDate != null) {
                stat.setTimestamp(index++, new Timestamp(sDate.getTime()));
            }
            if (eDate != null) {
                stat.setTimestamp(index++, new Timestamp(eDate.getTime()));
            }

            return processResultSet(stat.executeQuery());

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    private String parseQuery(Object value, String query, String argument) {
        if (value != null) {
            if (!query.equals("")) {
                query += "AND ";
            }
            query += argument + " ";
        }
        return query;
    }

    /**
     * Process the ResultSet of the SQL execution creating a list of workflows.
     *
     * @param rs ResultSet of the SQL execution
     * @return List of workflows
     * @throws SQLException
     */
    private List<Simulation> processResultSet(ResultSet rs) throws SQLException {
        List<Simulation> workflows = new ArrayList<Simulation>();

        while (rs.next()) {
            workflows.add(new Simulation(
                    rs.getString("application"),
                    rs.getString("id"),
                    rs.getString("username"),
                    new Date(rs.getTimestamp("launched").getTime()),
                    rs.getString("status"),
                    rs.getString("minor_status"),
                    rs.getString("simulation_name")));
        }
        return workflows;
    }

    /**
     * Gets the list of applications submitted.
     *
     * @return List of applications
     */
    public List<String> getApplications() throws DAOException {
        try {
            List<String> users = new ArrayList<String>();
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT application FROM Workflows "
                    + "GROUP BY application ORDER BY application");

            while (rs.next()) {
                users.add(rs.getString("application"));
            }

            return users;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public void updateStatus(String workflowID, String status) throws DAOException {
        try {
            PreparedStatement stat = connection.prepareStatement("UPDATE "
                    + "Workflows SET status=? WHERE id=?");

            stat.setString(1, status);
            stat.setString(2, workflowID);
            stat.executeUpdate();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public List<String> getOutputs(String workflowID) throws DAOException {

        try {
            PreparedStatement stat = connection.prepareStatement(
                    "SELECT path FROM Outputs WHERE workflow_id=?");

            stat.setString(1, workflowID);
            ResultSet rs = stat.executeQuery();
            List<String> outputs = new ArrayList<String>();

            while (rs.next()) {
                outputs.add(rs.getString("path"));
            }

            return outputs;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public void cleanWorkflow(String workflowID) throws DAOException {
        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM Outputs WHERE workflow_id=?");

            stat.setString(1, workflowID);
            stat.execute();

            stat = connection.prepareStatement("DELETE "
                    + "FROM Inputs WHERE workflow_id=?");

            stat.setString(1, workflowID);
            stat.execute();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public void delete(String workflowID) throws DAOException {
        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM Workflows WHERE id=?");

            stat.setString(1, workflowID);
            stat.execute();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public List<String> getStats(List<Simulation> wfIdList, int type, int binSize) throws DAOException {

        List<String> list = new ArrayList<String>();
        int nbJobs = 0;
        int execTime = 0;
        int uploadTime = 0;
        int downloadTime = 0;
        int cancelled = 0;
        int completed = 0;
        int error = 0;
        String stringExe;
        List<String> ls;
        try {
            for (int i = 0; i < wfIdList.size(); i++) {
                JobData jd = new JobData(wfIdList.get(i).getID());
                System.out.println("getStats - wf id is " + wfIdList.get(i).getID());
                Statement stat = jd.getConnection().createStatement();
                //PreparedStatement stat = null;
                //binSize of 1000000 so that we get only one set of values
                //type 1=executionStats, type2=downloadStats, type3=downloadStats
                switch (type) {
                    case 1:


                        try {

                            ResultSet rs = stat.executeQuery(
                                    "SELECT "
                                    + "sum(upload - running) as exect, sum(running - download) as downl, sum(end_e - upload) as upl,"
                                    + "count(id) as num FROM jobs "
                                    + "WHERE status='COMPLETED' ");

                            while (rs.next()) {
                                if ((rs.getString(1) != null) && (rs.getString(2) != null)) {
                                    execTime = execTime + Integer.parseInt(rs.getString("exect"));
                                    uploadTime = uploadTime + Integer.parseInt(rs.getString("upl"));
                                    downloadTime = downloadTime + Integer.parseInt(rs.getString("downl"));
                                    nbJobs = nbJobs + Integer.parseInt(rs.getString("num"));

                                }
                            }

                        } catch (SQLException ex) {
                            logger.error(ex);
                        }

                        break;
                    case 2:

                        try {
                            ResultSet rs = stat.executeQuery("select status,count(id) as num from jobs group by status");

                            while (rs.next()) {
                                if (rs.getString("status").equalsIgnoreCase("COMPLETED")) {
                                    completed = completed + Integer.parseInt(rs.getString("num"));
                                    System.out.println("completed is " + completed);
                                } else {
                                    if (rs.getString("status").equalsIgnoreCase("CANCELLED")) {
                                        cancelled = cancelled + Integer.parseInt(rs.getString("num"));
                                        System.out.println("cancelled is " + cancelled);
                                    } else {
                                        if (rs.getString("status").equalsIgnoreCase("ERROR")) {
                                            error = error + Integer.parseInt(rs.getString("num"));
                                            System.out.println("error is " + error);
                                        }
                                    }
                                }

                            }

                        } catch (SQLException ex) {
                            logger.error(ex);
                        }
                        break;

                    default:
                        stringExe = jd.getExecutionPerNumberOfJobs(1000000).get(0);
                        logger.info("No type defined for stats, using default execution stats");
                }


            }

            switch (type) {
                case 1:
                    list.add(Integer.toString(nbJobs) + "##" + Integer.toString(execTime) + "##" + Integer.toString(uploadTime) + "##" + Integer.toString(downloadTime));
                    logger.info("getStats case 1 list contains " + Integer.toString(nbJobs) + "##" + Integer.toString(execTime) + "##" + Integer.toString(uploadTime) + "##" + Integer.toString(downloadTime));
                    break;
                case 2:
                    nbJobs = completed + cancelled + error;
                    list.add(Integer.toString(nbJobs) + "##" + Integer.toString(completed) + "##" + Integer.toString(cancelled) + "##" + Integer.toString(error));
                    logger.info("getStats case 2 list contains " + Integer.toString(nbJobs) + "##" + Integer.toString(completed) + "##" + Integer.toString(cancelled) + "##" + Integer.toString(error));
                    break;
                default:

                    logger.info("No type defined for stats, using default execution stats");
            }

            return list;

        } catch (Exception ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @param type
     * @return
     * @throws DAOException 
     */
    public List<InOutData> getInOutData(String simulationID, String type) throws DAOException {

        try {
            List<InOutData> data = new ArrayList<InOutData>();
            PreparedStatement stat = connection.prepareStatement(
                    "SELECT path, processor, type FROM " + type + " WHERE workflow_id=?");

            stat.setString(1, simulationID);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                try {
                    data.add(new InOutData(
                            DataManagerUtil.parseRealDir(rs.getString("path")),
                            rs.getString("processor"),
                            rs.getString("type")));
                } catch (DataManagerException ex) {
                    logger.warn(ex);
                }
            }
            return data;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    
    /**
     * 
     * @param user
     * @return
     * @throws DAOException 
     */
    public int getRunningWorkflows(String user) throws DAOException {
        
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "COUNT(id) AS run FROM Workflows WHERE username = ? "
                    + "AND status = ?");
            
            ps.setString(1, user);
            ps.setString(2, SimulationStatus.Running.name());
            ResultSet rs = ps.executeQuery();
            
            return rs.next() ? rs.getInt("run") : 0;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param simulationID
     * @return
     * @throws DAOException 
     */
    public List<Processor> getProcessors(String simulationID) throws DAOException {
        
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "processor, completed, queued, failed FROM "
                    + "Processors WHERE workflow_id = ? ORDER BY processor");
            
            ps.setString(1, simulationID);
            ResultSet rs = ps.executeQuery();
            
            List<Processor> processors = new ArrayList<Processor>();
            
            while (rs.next()) {
                
                int completed = rs.getInt("completed");
                int queued = rs.getInt("queued");
                int failed = rs.getInt("failed");
                
                ProcessorStatus status = ProcessorStatus.Unstarted;
                
                if (completed + queued + failed > 0) {
                    if (failed > 0) {
                        status = ProcessorStatus.Failed;
                    } else if (queued > 0) {
                        status = ProcessorStatus.Active;
                    } else {
                        status = ProcessorStatus.Completed;
                    }
                }
                
                processors.add(new Processor(rs.getString("processor"), status, 
                        completed, queued, failed));
            }
            
            return processors;
            
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}
