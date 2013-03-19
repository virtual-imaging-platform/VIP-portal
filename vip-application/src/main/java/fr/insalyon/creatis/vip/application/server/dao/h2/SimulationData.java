/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
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
package fr.insalyon.creatis.vip.application.server.dao.h2;

import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Task;
import fr.insalyon.creatis.vip.application.client.view.monitor.job.TaskStatus;
import fr.insalyon.creatis.vip.application.server.dao.SimulationDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
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
 * @author Rafael Ferreira da Silva
 */
public class SimulationData extends AbstractJobData implements SimulationDAO {

    private static final Logger logger = Logger.getLogger(SimulationData.class);

    public SimulationData(String dbPath) throws DAOException {

        super(dbPath);
    }

    /**
     *
     * @return @throws DAOException
     */
    @Override
    public List<Task> getTasks() throws DAOException {

        List<Task> list = new ArrayList<Task>();

        try {
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT "
                    + "invocation_id, status, command "
                    + "FROM Jobs "
                    + "ORDER BY creation");

            while (rs.next()) {
                list.add(new Task(rs.getInt("invocation_id"),
                        rs.getString("status"),
                        rs.getString("command")));
            }
            stat.close();

        } catch (SQLException ex) {
            if (!ex.getMessage().contains("Table \"JOBS\" not found")) {
                logger.error(ex);
                throw new DAOException(ex);
            }
        } finally {
            close(logger);
        }
        return list;
    }

    /**
     *
     * @param jobID
     * @return
     * @throws DAOException
     */
    @Override
    public List<Task> getTasks(int jobID) throws DAOException {

        List<Task> list = new ArrayList<Task>();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT j.id, status, command, file_name, exit_code, node_site, node_name, parameters, "
                    + "ms FROM Jobs AS j LEFT JOIN ("
                    + "  SELECT jm.id, minor_status AS ms FROM JobsMinorStatus AS jm RIGHT JOIN ( "
                    + "    SELECT id, MAX(event_date) AS ed FROM JobsMinorStatus GROUP BY id "
                    + "  ) AS jm1 ON jm1.id = jm.id AND jm1.ed = jm.event_date "
                    + ") AS jm2 ON j.id = jm2.id "
                    + "WHERE j.invocation_id = ? "
                    + "ORDER BY j.id");
            ps.setInt(1, jobID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(parseTask(rs));
            }
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
        return list;
    }

    /**
     *
     * @param taskID
     * @return
     * @throws DAOException
     */
    @Override
    public Task getTask(String taskID) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT j.id, status, command, file_name, exit_code, node_site, node_name, parameters, "
                    + "ms FROM Jobs AS j LEFT JOIN ("
                    + "  SELECT jm.id, minor_status AS ms FROM JobsMinorStatus AS jm RIGHT JOIN ( "
                    + "    SELECT id, MAX(event_date) AS ed FROM JobsMinorStatus GROUP BY id "
                    + "  ) AS jm1 ON jm1.id = jm.id AND jm1.ed = jm.event_date "
                    + ") AS jm2 ON j.id = jm2.id "
                    + "WHERE j.id = ? "
                    + "ORDER BY j.id");
            ps.setString(1, taskID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Task task = parseTask(rs);
            ps.close();
            return task;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private Task parseTask(ResultSet rs) throws SQLException {

        ApplicationConstants.JobStatus status = ApplicationConstants.JobStatus.valueOf(rs.getString("status"));
        int minorStatus = -1;

        if (status == ApplicationConstants.JobStatus.RUNNING) {
            minorStatus = parseMinorStatus(rs.getString("ms"));
        }

        return new Task(rs.getString("id"), status.name(),
                rs.getString("command"), rs.getString("file_name"),
                rs.getInt("exit_code"), rs.getString("node_site"),
                rs.getString("node_name"), rs.getString("parameters"),
                minorStatus);
    }

    @Override
    public void sendTaskSignal(String taskID, TaskStatus status) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE Jobs SET status = ? WHERE id = ?");

            ps.setString(1, status.name());
            ps.setString(2, taskID);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    @Override
    public Map<String, Integer> getStatusMap() throws DAOException {

        Map<String, Integer> statusMap = new HashMap<String, Integer>();

        try {
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT "
                    + "status, COUNT(id) AS total FROM Jobs "
                    + "GROUP BY status");

            while (rs.next()) {
                statusMap.put(rs.getString("status"), rs.getInt("total"));
            }
            stat.close();

        } catch (SQLException ex) {
            if (!ex.getMessage().contains("Table \"JOBS\" not found")) {
                logger.error(ex);
                throw new DAOException(ex);
            }
        } finally {
            close(logger);
        }
        return statusMap;
    }

    @Override
    public List<Task> getJobs() throws DAOException {

        List<Task> list = new ArrayList<Task>();

        try {
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery(
                    "SELECT j.id, status, command, file_name, exit_code, node_site, node_name, parameters, "
                    + "ms FROM Jobs AS j LEFT JOIN ("
                    + "  SELECT jm.id, minor_status AS ms FROM JobsMinorStatus AS jm RIGHT JOIN ( "
                    + "    SELECT id, MAX(event_date) AS ed FROM JobsMinorStatus GROUP BY id "
                    + "  ) AS jm1 ON jm1.id = jm.id AND jm1.ed = jm.event_date "
                    + ") AS jm2 ON j.id = jm2.id ORDER BY j.id");

            while (rs.next()) {
                ApplicationConstants.JobStatus status = ApplicationConstants.JobStatus.valueOf(rs.getString("status"));
                int minorStatus = -1;

                if (status == ApplicationConstants.JobStatus.RUNNING) {
                    minorStatus = parseMinorStatus(rs.getString("ms"));
                }

                list.add(new Task(rs.getString("id"), status.name(),
                        rs.getString("command"), rs.getString("file_name"),
                        rs.getInt("exit_code"), rs.getString("node_site"),
                        rs.getString("node_name"), rs.getString("parameters"),
                        minorStatus));
            }
            stat.close();

        } catch (SQLException ex) {
            if (!ex.getMessage().contains("Table \"JOBS\" not found")
                    && !ex.getMessage().contains("Table \"JOBSMINORSTATUS\" not found")) {
                logger.error(ex);
                throw new DAOException(ex);
            }
        } finally {
            close(logger);
        }
        return list;
    }

    @Override
    public List<String> getExecutionPerNumberOfJobs(int binSize) throws DAOException {

        return getHistogramTimes(binSize, "running", "upload");
    }

    @Override
    public List<String> getDownloadPerNumberOfJobs(int binSize) throws DAOException {

        return getHistogramTimes(binSize, "download", "running");
    }

    @Override
    public List<String> getUploadPerNumberOfJobs(int binSize) throws DAOException {

        return getHistogramTimes(binSize, "upload", "end_e");
    }

    @Override
    public List<String> getJobsPerTime() throws DAOException {

        try {
            List<String> list = new ArrayList<String>();
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT status, creation AS crea, "
                    + "TIMESTAMPDIFF('SECOND', creation, queued) AS creation, "
                    + "TIMESTAMPDIFF('SECOND', queued, download) AS queued, "
                    + "TIMESTAMPDIFF('SECOND', download, running) AS download, "
                    + "TIMESTAMPDIFF('SECOND', running, upload) AS running, "
                    + "TIMESTAMPDIFF('SECOND', upload, end_e) AS upload, "
                    + "checkpoint_init, checkpoint_upload "
                    + "FROM Jobs ORDER BY id");

            long start = -1;

            while (rs.next()) {

                long creation = rs.getTimestamp("crea").getTime();
                if (start == -1) {
                    start = creation;
                }
                creation -= start;
                list.add(rs.getString("status")
                        + "##" + (rs.getInt("creation") + (creation / 1000))
                        + "##" + rs.getInt("queued")
                        + "##" + rs.getInt("download")
                        + "##" + rs.getInt("running")
                        + "##" + rs.getInt("upload")
                        + "##" + rs.getInt("checkpoint_init")
                        + "##" + rs.getInt("checkpoint_upload")
                        + "##0");
            }
            stat.close();
            return list;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    @Override
    public List<String> getCkptsPerJob() throws DAOException {

        try {
            List<String> list = new ArrayList<String>();
            PreparedStatement ps = connection.prepareStatement("SELECT j.id, status, "
                    + "COALESCE(jm.occ, 0) AS ckpt_occ FROM Jobs AS j LEFT JOIN "
                    + "(SELECT id, count(minor_status) AS occ FROM JobsMinorStatus "
                    + "WHERE minor_status = ? GROUP BY id) AS jm "
                    + "ON j.id = jm.id ORDER BY ckpt_occ");
            ps.setString(1, "CheckPoint_End");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("status") + "##" + rs.getString("ckpt_occ"));
            }
            ps.close();
            return list;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    @Override
    public List<String> getSiteHistogram() throws DAOException {

        try {
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT COUNT(id) AS num, "
                    + "node_site AS site FROM jobs GROUP BY node_site "
                    + "ORDER BY num DESC");

            List<String> list = new ArrayList<String>();
            while (rs.next()) {
                list.add(rs.getString("site") + "##" + rs.getString("num"));
            }
            stat.close();
            return list;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    @Override
    public void sendSignal(String jobID, TaskStatus status) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE Jobs SET status = ? WHERE id = ?");

            ps.setString(1, status.name());
            ps.setString(2, jobID);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    @Override
    public int[] getNumberOfActiveTasks() throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "status, count(id) AS num FROM Jobs "
                    + "WHERE status = ? OR status = ? "
                    + "GROUP BY status");
            ps.setString(1, ApplicationConstants.JobStatus.RUNNING.name());
            ps.setString(2, ApplicationConstants.JobStatus.QUEUED.name());

            ResultSet rs = ps.executeQuery();
            int[] tasks = new int[2];

            while (rs.next()) {
                if (ApplicationConstants.JobStatus.valueOf(rs.getString("status")) == ApplicationConstants.JobStatus.RUNNING) {
                    tasks[0] = rs.getInt("num");
                } else if (ApplicationConstants.JobStatus.valueOf(rs.getString("status")) == ApplicationConstants.JobStatus.QUEUED) {
                    tasks[1] = rs.getInt("num");
                }
            }
            ps.close();
            return tasks;

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Table \"JOBS\" not found")) {
                return new int[]{0, 0};
            }
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    @Override
    public Map<String, Integer> getNodeCountriesMap() throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "SUBSTR(node_name, -2) AS country, COUNT(id) AS num "
                    + "FROM Jobs WHERE status = ? "
                    + "GROUP BY country ORDER BY country");
            ps.setString(1, ApplicationConstants.JobStatus.COMPLETED.name());
            ResultSet rs = ps.executeQuery();

            Map<String, Integer> map = new HashMap<String, Integer>();
            while (rs.next()) {
                String name = rs.getString("country");
                if (name != null && !name.isEmpty()) {
                    map.put(name, rs.getInt("num"));
                }
            }
            ps.close();
            return map;

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Table \"JOBS\" not found")) {
                return new HashMap<String, Integer>();
            }
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    /**
     *
     * @param binSize
     * @param startField
     * @param endField
     * @return
     * @throws DAOException
     */
    private List<String> getHistogramTimes(int binSize, String startField, String endField)
            throws DAOException {

        try {
            List<String> list = new ArrayList<String>();
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "TIMESTAMPDIFF('SECOND', " + startField + ", " + endField + ")/" + binSize + "*" + binSize + " AS execut, "
                    + "COUNT(id) AS num, "
                    + "MIN(TIMESTAMPDIFF('SECOND', " + startField + ", " + endField + ")) AS mini, "
                    + "MAX(TIMESTAMPDIFF('SECOND', " + startField + ", " + endField + ")) AS maxi, "
                    + "SUM(TIMESTAMPDIFF('SECOND', " + startField + ", " + endField + ")) AS som "
                    + "FROM JOBS "
                    + "WHERE STATUS = ? AND TIMESTAMPDIFF('SECOND', " + startField + ", " + endField + ") >= 0 "
                    + "GROUP BY TIMESTAMPDIFF('SECOND', " + startField + ", " + endField + ")/" + binSize + "*" + binSize + " "
                    + "ORDER BY EXECUT");
            ps.setString(1, ApplicationConstants.JobStatus.COMPLETED.name());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("execut")
                        + "##" + rs.getString("num")
                        + "##" + rs.getString("mini")
                        + "##" + rs.getString("maxi")
                        + "##" + rs.getString("som"));
            }
            ps.close();
            return list;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        } finally {
            close(logger);
        }
    }

    private int parseMinorStatus(String minorStatus) {

        if (minorStatus == null || minorStatus.isEmpty()) {
            return -1;
        } else if (minorStatus.equals("Started")) {
            return 1;
        } else if (minorStatus.equals("Background")) {
            return 2;
        } else if (minorStatus.equals("Inputs")) {
            return 3;
        } else if (minorStatus.equals("Application")) {
            return 4;
        } else if (minorStatus.equals("Outputs")) {
            return 5;
        } else if (minorStatus.equals("Finished")) {
            return 6;
        }
        return -1;
    }
}
