/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.datamanager.server.dao.mysql;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.datamanager.client.bean.SSH;
import fr.insalyon.creatis.vip.datamanager.client.bean.TransfertType;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.dao.SSHDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author glatard, Nouha Boujelben
 */
public class SSHData implements SSHDAO {

    private final static Logger logger = Logger.getLogger(SSHData.class);
    private Connection connection;

    public SSHData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    @Override
    public List<SSH> getSSHConnections() throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "* FROM "
                    + "VIPSSHAccounts");

            ResultSet rs = ps.executeQuery();
            List<SSH> ssh = new ArrayList<SSH>();

            while (rs.next()) {

                String email = rs.getString("email");
                String name = DataManagerBusiness.extractName(rs.getString("LFCDir"));
                String sshUser = rs.getString("sshUser");
                String sshHost = rs.getString("sshHost");
                TransfertType sshTransfertType = TransfertType.valueOf(rs.getString("transfertType"));
                String sshDir = rs.getString("sshDir");
                int sshPort = rs.getInt("sshPort");
                boolean validated = rs.getBoolean("validated");
                boolean auth_failed = rs.getBoolean("auth_failed");
                long numberSynchronizationFailed = rs.getLong("numberSynchronizationFailed");
                boolean deleteFilesFromSource = rs.getBoolean("deleteFilesFromSource");

                String status = "ok";
                if (auth_failed) {
                    status = "authentication failed";
                }
                if (!validated) {
                    status = "waiting for validation";
                }

                ssh.add(new SSH(email, name, sshUser, sshHost, sshPort, sshTransfertType, sshDir, status, numberSynchronizationFailed, deleteFilesFromSource));
            }
            ps.close();
            return ssh;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void addSSH(SSH ssh) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPSSHAccounts(email,LFCDir,sshUser,sshHost,transfertType,sshDir,sshPort,validated,auth_failed,numberSynchronizationFailed,deleteFilesFromSource) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?)");

            ps.setString(1, ssh.getEmail());
            try {
                ps.setString(2, DataManagerBusiness.generateLFCDir(ssh.getName(), ssh.getEmail()));
            } catch (DataManagerException ex) {
                throw new DAOException(ex);
            } catch (BusinessException ex) {
                throw new DAOException(ex);
            }
            ps.setString(3, ssh.getUser());
            ps.setString(4, ssh.getHost());
            ps.setString(5, ssh.getTransfertType().name());
            ps.setString(6, ssh.getDirectory());
            ps.setInt(7, ssh.getPort());
            ps.setString(8, "1");
            ps.setString(9, "0");
            ps.setLong(10, ssh.getNumberSynchronizationFailes());
            ps.setBoolean(11, ssh.isDeleteFilesFromSource());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                logger.error("An ssh connection named \"" + ssh.getName() + "\" already exists for user " + ssh.getEmail() + ".");
                throw new DAOException("An ssh connection named \"" + ssh.getName() + "\" already exists for user " + ssh.getEmail() + ".");
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void updateSSH(SSH ssh) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "
                    + "VIPSSHAccounts "
                    + "SET sshUser=?, sshHost=?, transfertType=?, sshDir=?, sshPort=?, auth_failed='0', numberSynchronizationFailed=?, deleteFilesFromSource=? "
                    + "WHERE email=? AND LFCDir=?");
            ps.setString(1, ssh.getUser());
            ps.setString(2, ssh.getHost());
            ps.setString(3, ssh.getTransfertType().toString());
            ps.setString(4, ssh.getDirectory());
            ps.setInt(5, ssh.getPort());
            ps.setLong(6, ssh.getNumberSynchronizationFailes());
            ps.setBoolean(7, ssh.isDeleteFilesFromSource());
            ps.setString(8, ssh.getEmail());

            try {
                ps.setString(9, DataManagerBusiness.generateLFCDir(ssh.getName(), ssh.getEmail()));
            } catch (DataManagerException ex) {
                logger.error(ex);
                throw new DAOException(ex);
            } catch (BusinessException ex) {
                throw new DAOException(ex);
            }

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void removeSSH(String email, String name) throws DAOException {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM VIPSSHAccounts WHERE email=? AND LFCDir=?");

            ps.setString(1, email);
            try {
                ps.setString(2, DataManagerBusiness.generateLFCDir(name, email));
                logger.info("Removing connection " + email + " " + DataManagerBusiness.generateLFCDir(name, email));
            } catch (DataManagerException ex) {
                logger.error(ex);
                throw new DAOException(ex);
            } catch (BusinessException ex) {
                throw new DAOException(ex);
            }

            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}
