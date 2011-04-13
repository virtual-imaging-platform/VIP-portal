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
package fr.insalyon.creatis.vip.portal.server.dao.derby;

import fr.insalyon.creatis.vip.portal.server.dao.DAOException;
import fr.insalyon.creatis.vip.portal.server.dao.derby.connection.WorkflowsConnection;
import fr.insalyon.creatis.vip.portal.client.bean.Workflow;
import fr.insalyon.creatis.vip.portal.server.dao.WorkflowDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class WorkflowData implements WorkflowDAO {

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
     * Gets the list of workflows submitted by a user filtered by application
     * name, start date and/or end date.
     *
     * @param user User name
     * @param app Application name
     * @param status Application status
     * @param sDate Start date
     * @param eDate End date
     * @return List of workflows filtered
     */
    public List<Workflow> getList(String user, String app, String status, Date sDate, Date eDate) {
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
                    + "id, application, username, launched, status, minor_status "
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
            ex.printStackTrace();
        }
        return null;
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
    private List<Workflow> processResultSet(ResultSet rs) throws SQLException {
        List<Workflow> workflows = new ArrayList<Workflow>();
        SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        while (rs.next()) {
            workflows.add(new Workflow(
                    rs.getString("application"),
                    rs.getString("id"),
                    rs.getString("username"),
                    f.format(rs.getTimestamp("launched")),
                    rs.getString("status"),
                    rs.getString("minor_status")));
        }
        return workflows;
    }

    /**
     * Gets the list of users whom submitted workflows.
     *
     * @return List of users
     */
    public List<String> getUsers() {
        try {
            List<String> users = new ArrayList<String>();
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT username FROM Workflows "
                    + "GROUP BY username ORDER BY username");

            while (rs.next()) {
                users.add(rs.getString("username"));
            }

            return users;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the list of applications submitted.
     *
     * @return List of applications
     */
    public List<String> getApplications() {
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
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the moteur identification and key for a specific workflow.
     *
     * @param workflowID Workflow identification
     * @return Array with moteur identification at position 0 and key at position 1
     */
    public int[] getMoteurIDAndKey(String workflowID) {
        try {
            int[] moteurData = new int[2];
            PreparedStatement stat = connection.prepareStatement(
                    "SELECT moteur_id, moteur_key "
                    + "FROM Workflows WHERE id=?");

            stat.setString(1, workflowID);

            ResultSet rs = stat.executeQuery();
            rs.next();

            moteurData[0] = rs.getInt("moteur_id");
            moteurData[1] = rs.getInt("moteur_key");

            return moteurData;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<String> getApplicationsWithClass(String appClass) {
        if (appClass == null) {
            return getApplications();
        }
        List<String> result = new ArrayList<String>();
        PreparedStatement stat = null;
        try {
            stat = connection.prepareStatement(
                    "SELECT workflow FROM WorkflowClasses WHERE class=? "
                    + "GROUP BY workflow ORDER BY workflow");

            stat.setString(1, appClass);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("workflow"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void updateStatus(String workflowID, String status) {
        try {
            PreparedStatement stat = connection.prepareStatement("UPDATE "
                    + "Workflows SET status=? WHERE id=?");

            stat.setString(1, status);
            stat.setString(2, workflowID);
            stat.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getOutputs(String workflowID) throws DAOException {
        List<String> outputs = new ArrayList<String>();

        try {
            PreparedStatement stat = connection.prepareStatement(
                    "SELECT path FROM Outputs WHERE workflow_id=?");

            stat.setString(1, workflowID);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                outputs.add(rs.getString("path"));
            }

        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
        return outputs;
    }

    public void cleanWorkflow(String workflowID) throws DAOException {
        try {
            PreparedStatement stat = connection.prepareStatement("DELETE "
                    + "FROM Outputs WHERE workflow_id=?");

            stat.setString(1, workflowID);
            stat.execute();

        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }

    public List<String> getStats(List<Workflow> wfIdList, int type, int binSize) {

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
                JobData jd = new JobData(wfIdList.get(i).getWorkflowID());
                System.out.println("getStats - wf id is " + wfIdList.get(i).getWorkflowID());
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
                            ex.printStackTrace();
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
                            ex.printStackTrace();
                        }
                        break;

                    default:
                        stringExe = jd.getExecutionPerNumberOfJobs(1000000).get(0);
                        System.out.println("No type defined for stats, using default execution stats");
                }


            }

            switch (type) {
                case 1:
                    list.add(Integer.toString(nbJobs) + "##" + Integer.toString(execTime) + "##" + Integer.toString(uploadTime) + "##" + Integer.toString(downloadTime));
                    System.out.println("getStats case 1 list contains " + Integer.toString(nbJobs) + "##" + Integer.toString(execTime) + "##" + Integer.toString(uploadTime) + "##" + Integer.toString(downloadTime));
                    break;
                case 2:
                    nbJobs = completed + cancelled + error;
                    list.add(Integer.toString(nbJobs) + "##" + Integer.toString(completed) + "##" + Integer.toString(cancelled) + "##" + Integer.toString(error));
                    System.out.println("getStats case 2 list contains " + Integer.toString(nbJobs) + "##" + Integer.toString(completed) + "##" + Integer.toString(cancelled) + "##" + Integer.toString(error));
                    break;
                default:

                    System.out.println("No type defined for stats, using default execution stats");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
