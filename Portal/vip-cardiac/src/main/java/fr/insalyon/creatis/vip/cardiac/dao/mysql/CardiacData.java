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
package fr.insalyon.creatis.vip.cardiac.dao.mysql;

import fr.insalyon.creatis.vip.cardiac.client.bean.Simulation;
import fr.insalyon.creatis.vip.cardiac.dao.CardiacDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author glatard
 */
public class CardiacData implements CardiacDAO {

    private final static Logger logger = Logger.getLogger(CardiacData.class);
    private Connection connection;

    public void CardiacData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
        
    }

    @Override
    public void addSimulation(Simulation s) throws DAOException {
        try {
            connection = PlatformConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO CardiacSimulations(name, description, files, modalities,simulation) "
                    + "VALUES (?, ?, ?, ?,?)");

            ps.setString(1, s.getName());
            ps.setString(2, s.getDescription());
            ps.setString(3, s.getFilesAsString());
            ps.setString(4, s.getModalities());
            ps.setString(5, s.getSimulationID());
            ps.execute();

            ps.close();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void deleteSimulation(Simulation s) throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM CardiacSimulations WHERE name=?");

            ps.setString(1, s.getName());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Simulation> getSimulations() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
        List<Simulation> simulations = new ArrayList<Simulation>();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "name, description, files, modalities, simulation FROM "
                    + "CardiacSimulations ORDER BY name");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                String files = rs.getString("files");
                String modalities = rs.getString("modalities");
                String simulationID = rs.getString("simulation");
                Simulation s = new Simulation(name, description,files,modalities,simulationID);
                simulations.add(s);
            }
            ps.close();
            return simulations;
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }



//    private void deleteFile(File f) throws DAOException {
//        connection = PlatformConnection.getInstance().getConnection();
//        try {
//            PreparedStatement ps = connection.prepareStatement("DELETE "
//                      + "FROM CardiacFiles WHERE  uri=?");
//              ps.setString(1, f.getUri().toString());
//              ps.execute();
//              ps.close();
//        } catch (SQLException ex) {
//          logger.error(ex);
//            throw new DAOException(ex);
//        }
//    }


//
//    public void addFileToSimulation(File f, Simulation s) throws DAOException {
//        connection = PlatformConnection.getInstance().getConnection();
//        addFile(f);
//        addAssociation(s,f);
//    }
//
//    @Override
//    public void deleteFileFromSimulation(File f, Simulation s) throws DAOException {
//        connection = PlatformConnection.getInstance().getConnection();
//        deleteFile(f);
//        deleteAssociation(s,f);
//    }

//    private List<File> getFiles(Simulation s) throws DAOException {
//        connection = PlatformConnection.getInstance().getConnection();
//        List<File> files = new ArrayList<File>();
//
//        try {
//            PreparedStatement ps = connection.prepareStatement("SELECT "
//                    + "uri FROM "
//                    + "CardiacAssociations where name=? ORDER BY name");
//
//             ps.setString(1, s.getName());
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                String uri = rs.getString("uri");
//                
//                PreparedStatement ps2 = connection.prepareStatement("SELECT name, description from CardiacFiles where uri=?");
//                ps2.setString(1,uri);
//                ResultSet rs1 = ps2.executeQuery();
//                while(rs1.next()){
//                    String name = rs1.getString("name");
//                    String description = rs1.getString("description");
//                    File f = new File(uri,name,description);
//                    files.add(f);
//                }
//                ps2.close();
//            }
//            ps.close();
//            return files;
//         } catch (SQLException ex) {
//            logger.error(ex);
//            throw new DAOException(ex);
//        }
//    }
//
//    private void addFile(File f) throws DAOException {
//        connection = PlatformConnection.getInstance().getConnection();
//        try {
//            PreparedStatement ps = connection.prepareStatement(
//                     "INSERT INTO CardiacFiles(uri,name,description) "
//                     + "VALUES (?, ?, ?)");
//
//             ps.setString(1, f.getUri().toString());
//             ps.setString(2, f.getName());
//             ps.setString(3, f.getDescription());
//             ps.execute();
//             ps.close();
//        } catch (SQLException ex) {
//                logger.error(ex);
//            throw new DAOException(ex);
//        }
//    }
//
//    private void addAssociation(Simulation s, File f) throws DAOException {
//        connection = PlatformConnection.getInstance().getConnection();
//         try {
//            PreparedStatement ps = connection.prepareStatement(
//                     "INSERT INTO CardiacAssociations(name,uri) "
//                     + "VALUES (?, ?)");
//
//             ps.setString(1, s.getName());
//             ps.setString(2, f.getUri().toString());
//             ps.execute();
//             ps.close();
//        } catch (SQLException ex) {
//                logger.error(ex);
//            throw new DAOException(ex);
//        }
//    }
//
//    private void deleteAssociation(Simulation s, File f) throws DAOException {
//        connection = PlatformConnection.getInstance().getConnection();
//        try {
//            PreparedStatement ps = connection.prepareStatement("DELETE "
//                      + "FROM CardiacAssociations WHERE name=? and uri=?");
//
//              ps.setString(1, s.getName());
//              ps.setString(2, f.getUri().toString());
//              ps.execute();
//              ps.close();
//        } catch (SQLException ex) {
//          logger.error(ex);
//            throw new DAOException(ex);
//        }
//
//    }

    @Override
    public void updateSimulation(Simulation s) throws DAOException{
        

            connection = PlatformConnection.getInstance().getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE "
                        + "CardiacSimulations "
                        + "SET description=?, files=?, modalities=?, simulation=? "
                        + "WHERE name=?");

                ps.setString(1, s.getDescription());
                ps.setString(2, s.getFilesAsString());
                ps.setString(3, s.getModalities());
                ps.setString(5, s.getName());
                ps.setString(4, s.getSimulationID());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                logger.error(ex);
                throw new DAOException(ex);
            }

       

    }
}
