/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.server.dao.mysql;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.datamanager.client.bean.SSH;
import fr.insalyon.creatis.vip.datamanager.client.bean.TransferType;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.business.DataManagerBusiness;
import fr.insalyon.creatis.vip.datamanager.server.dao.SSHDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
                TransferType sshTransferType = TransferType.valueOf(rs.getString("transferType"));
                String sshDir = rs.getString("sshDir");
                int sshPort = rs.getInt("sshPort");
                boolean validated = rs.getBoolean("validated");
                boolean auth_failed = rs.getBoolean("auth_failed");
                Timestamp theEarliestNextSynchronistation = rs.getTimestamp("theEarliestNextSynchronistation");
                long numberSynchronizationFailed = rs.getLong("numberSynchronizationFailed");
                boolean deleteFilesFromSource = rs.getBoolean("deleteFilesFromSource");
                boolean activate = rs.getBoolean("activate");

                String status = "ok";
                if (auth_failed) {
                    status = "authentication failed";
                }
                if (!validated) {
                    status = "waiting for validation";
                }

                ssh.add(new SSH(email, name, sshUser, sshHost, sshPort, sshTransferType, sshDir, status, theEarliestNextSynchronistation, numberSynchronizationFailed, deleteFilesFromSource, activate));
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
                    "INSERT INTO VIPSSHAccounts(email,LFCDir,sshUser,sshHost,transferType,sshDir,sshPort,validated,auth_failed,numberSynchronizationFailed,deleteFilesFromSource) "
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
            ps.setString(5, ssh.getTransferType().name());
            ps.setString(6, ssh.getDirectory());
            ps.setInt(7, ssh.getPort());
            ps.setString(8, "1");
            ps.setString(9, "0");
            ps.setLong(10, 0);
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
                    + "SET sshUser=?, sshHost=?, transferType=?, sshDir=?, sshPort=?, deleteFilesFromSource=?, activate=? "
                    + "WHERE email=? AND LFCDir=?");
            ps.setString(1, ssh.getUser());
            ps.setString(2, ssh.getHost());
            ps.setString(3, ssh.getTransferType().toString());
            ps.setString(4, ssh.getDirectory());
            ps.setInt(5, ssh.getPort());
            ps.setBoolean(6, ssh.isDeleteFilesFromSource());
            ps.setBoolean(7, ssh.isActivate());
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

    @Override
    public void resetSSHs(List<List<String>> sshConnections) throws DAOException {

        for (List<String> sshC : sshConnections) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE "
                        + "VIPSSHAccounts "
                        + "SET auth_failed='1', numberSynchronizationFailed='0', theEarliestNextSynchronistation=? "
                        + "WHERE email=? AND LFCDir=?");
                ps.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
                ps.setString(2, sshC.get(0));
                try {
                    ps.setString(3, DataManagerBusiness.generateLFCDir(sshC.get(1), sshC.get(0)));
                    logger.info("Reset connection " + sshC.get(0) + " " + DataManagerBusiness.generateLFCDir(sshC.get(1), sshC.get(0)));
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
}
