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

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.application.server.dao.JobDAO;
import fr.insalyon.creatis.vip.application.server.dao.derby.connection.JobsConnection;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.common.server.dao.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class JobData implements JobDAO {

    private static Logger logger = Logger.getLogger(JobData.class);
    private Connection connection;

    public JobData(String workflowID) throws DAOException {
        connection = JobsConnection.getInstance().connect(
                ServerConfiguration.getInstance().getWorkflowsPath() + "/" + workflowID + "/jobs.db");
    }

    /**
     * Gets a map with the status of the jobs.
     * 
     * @return Jobs status map
     */
    public Map<String, Integer> getStatusMap() throws DAOException {
        try {
            Map<String, Integer> statusMap = new HashMap<String, Integer>();
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT "
                    + "status, COUNT(id) AS total FROM jobs "
                    + "GROUP BY status");

            while (rs.next()) {
                statusMap.put(rs.getString("status"), rs.getInt("total"));
            }

            return statusMap;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * Gets a list of all jobs.
     * 
     * @return List of jobs
     */
    public List<Job> getJobs() throws DAOException {
        try {
            List<Job> jobsList = new ArrayList<Job>();

            ResultSet rs = connection.getMetaData().getTables(null, null, "%", null);
            boolean hasMinorStatus = false;
            while (rs.next()) {
                if (rs.getString(3).toLowerCase().equals("jobsminorstatus")) {
                    hasMinorStatus = true;
                }
            }

            Statement stat = connection.createStatement();
            if (hasMinorStatus) {
                rs = stat.executeQuery("SELECT "
                        + "j.id, status, command, file_name, exit_code, node_site, "
                        + "node_name, parameters, COALESCE(ms, -1) AS ms "
                        + "FROM Jobs AS j LEFT JOIN "
                        + "(SELECT id, max(minor_status) AS ms "
                        + "FROM JobsMinorStatus GROUP BY id) AS jm "
                        + "ON j.id = jm.id ORDER BY j.id");

                while (rs.next()) {
                    String status = rs.getString("status");
                    int minorStatus = rs.getInt("ms");
                    
                    if (!status.equals("RUNNING")) {
                        minorStatus = -1;
                    }
                    
                    jobsList.add(new Job(rs.getString("id"), status,
                            rs.getString("command"), rs.getString("file_name"),
                            rs.getInt("exit_code"), rs.getString("node_site"),
                            rs.getString("node_name"), rs.getString("parameters"),
                            minorStatus));
                }

            } else {
                rs = stat.executeQuery("SELECT "
                        + "id, status, command, file_name, exit_code, node_site, "
                        + "node_name, parameters FROM jobs "
                        + "ORDER BY command, id");

                while (rs.next()) {
                    jobsList.add(new Job(rs.getString("id"), rs.getString("status"),
                            rs.getString("command"), rs.getString("file_name"),
                            rs.getInt("exit_code"), rs.getString("node_site"),
                            rs.getString("node_name"), rs.getString("parameters"), -1));
                }
            }
            return jobsList;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    /**
     * 
     * @param binSize
     * @return
     */
    public List<String> getExecutionPerNumberOfJobs(int binSize) {
        try {
            List<String> list = new ArrayList<String>();
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT "
                    + "(upload - running)/" + binSize + "*" + binSize + " as execut, "
                    + "count(id) as num, min(upload - running) as mini, "
                    + "max(upload - running) as maxi, sum(upload - running) "
                    + "as som FROM jobs "
                    + "WHERE status='COMPLETED' "
                    + "GROUP BY (upload - running)/" + binSize + "*" + binSize);

            while (rs.next()) {
                list.add(rs.getString("execut")
                        + "##" + rs.getString("num")
                        + "##" + rs.getString("mini")
                        + "##" + rs.getString("maxi")
                        + "##" + rs.getString("som"));
            }

            return list;

        } catch (SQLException ex) {
            logger.error(ex);
        }
        return null;
    }

    /**
     * 
     * @param binSize
     * @return
     */
    public List<String> getDownloadPerNumberOfJobs(int binSize) {
        try {
            List<String> list = new ArrayList<String>();
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT "
                    + "(running - download)/" + binSize + "*" + binSize + " as execut, "
                    + "count(id) as num, min(running - download) as mini, "
                    + "max(running - download) as maxi, sum(running - download) "
                    + "as som FROM jobs "
                    + "WHERE status='COMPLETED' "
                    + "GROUP BY (running - download)/" + binSize + "*" + binSize);

            while (rs.next()) {
                list.add(rs.getString("execut")
                        + "##" + rs.getString("num")
                        + "##" + rs.getString("mini")
                        + "##" + rs.getString("maxi")
                        + "##" + rs.getString("som"));
            }

            return list;

        } catch (SQLException ex) {
            logger.error(ex);
        }
        return null;
    }

    /**
     * 
     * @param binSize
     * @return
     */
    public List<String> getUploadPerNumberOfJobs(int binSize) {
        try {
            List<String> list = new ArrayList<String>();
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT "
                    + "(end_e - upload)/" + binSize + "*" + binSize + " as execut, "
                    + "count(id) as num, min(end_e - upload) as mini, "
                    + "max(end_e - upload) as maxi, sum(end_e - upload) as som FROM jobs "
                    + "WHERE status='COMPLETED' "
                    + "GROUP BY (end_e - upload)/" + binSize + "*" + binSize);

            while (rs.next()) {
                list.add(rs.getString("execut")
                        + "##" + rs.getString("num")
                        + "##" + rs.getString("mini")
                        + "##" + rs.getString("maxi")
                        + "##" + rs.getString("som"));
            }

            return list;

        } catch (SQLException ex) {
            logger.error(ex);
        }
        return null;
    }

    public List<String> getJobsPerTime() {
        try {
            List<String> list = new ArrayList<String>();
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT "
                    + "status, queued as cre, "
                    + "(download - queued) as que, "
                    + "(running - download) as inp, "
                    + "(upload - running) as exe, "
                    + "(end_e - upload) as outp "
                    + "FROM jobs "
                    + "WHERE status='COMPLETED' OR status='ERROR' "
                    + "ORDER BY id");

            while (rs.next()) {
                list.add(rs.getString("status")
                        + "##" + rs.getString("cre")
                        + "##" + rs.getString("que")
                        + "##" + rs.getString("inp")
                        + "##" + rs.getString("exe")
                        + "##" + rs.getString("outp"));
            }

            return list;

        } catch (SQLException ex) {
            logger.error(ex);
        }
        return null;
    }

    public void sendSignal(String jobID, ApplicationConstants.JobStatus status) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE Jobs SET status = ? WHERE id = ?");

            ps.setString(1, status.name());
            ps.setString(2, jobID);
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
