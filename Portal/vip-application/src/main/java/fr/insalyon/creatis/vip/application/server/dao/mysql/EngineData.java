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
package fr.insalyon.creatis.vip.application.server.dao.mysql;

import fr.insalyon.creatis.vip.application.client.bean.Engine;
import fr.insalyon.creatis.vip.application.server.dao.EngineDAO;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EngineData implements EngineDAO {

    private final static Logger logger = Logger.getLogger(EngineData.class);
    private Connection connection;

    public EngineData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    @Override
    public void add(Engine engine) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO VIPEngines(name, endpoint) "
                    + "VALUES (?, ?)");

            ps.setString(1, engine.getName());
            ps.setString(2, engine.getEndpoint());
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                throw new DAOException("An engine named \"" + engine.getName() + "\" already exists.");
            } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void update(Engine engine) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE VIPEngines SET endpoint = ? "
                    + "WHERE name = ?");

            ps.setString(1, engine.getEndpoint());
            ps.setString(2, engine.getName());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void remove(String name) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE "
                    + "FROM VIPEngines WHERE name=?");

            ps.setString(1, name);
            ps.execute();
            ps.close();

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Engine> get() throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "name, endpoint FROM VIPEngines "
                    + "ORDER BY name");

            ResultSet rs = ps.executeQuery();
            List<Engine> list = new ArrayList<Engine>();

            while (rs.next()) {
                list.add(new Engine(rs.getString("name"), rs.getString("endpoint")));
            }
            return list;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Engine> getByClass(String className) throws DAOException {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "e.name AS engineName, endpoint "
                    + "FROM VIPEngines e, VIPClassesEngines c "
                    + "WHERE e.name = c.engine AND "
                    + "c.class = ?");
            ps.setString(1, className);

            ResultSet rs = ps.executeQuery();
            
            List<Engine> list = new ArrayList<Engine>();
            while (rs.next()) {
                list.add(new Engine(rs.getString("engineName"), rs.getString("endpoint")));
            }
            ps.close();
            return list;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
}
