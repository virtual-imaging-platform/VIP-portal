/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.query.server.dao.persistance;

import fr.insalyon.creatis.vip.query.client.bean.Query;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import fr.insalyon.creatis.vip.core.server.dao.mysql.UserData;
import fr.insalyon.creatis.vip.query.client.bean.QueryVersion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Boujelben
 */
public class QueryData implements QueryDAO {
     private final static Logger logger = Logger.getLogger(QueryData.class);
     private Connection connection;

    public QueryData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    
    
    
    
    @Override
    public List<Query> getQueries() throws DAOException {

        
       

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT queryID, queryName, dateCreation FROM Query");
            ResultSet rs = ps.executeQuery();
            List<Query> queries = new ArrayList<Query>();
            while (rs.next()) {
                
                int id=rs.getInt("queryID");
                
                 PreparedStatement ps2 = connection.prepareStatement("SELECT queryVersion FROM QueryVersion WHERE queryID=?");
                 
                
                 ps2.setInt(1,id);
                 ResultSet rs2 = ps2.executeQuery();
                
                while (rs2.next()) {
               
                    queries.add(new Query(rs.getString("queryName"),rs.getDate("dateCreation"),rs2.getString("queryVersion")));
                }
                ps2.close();

            }
            ps.close();
            return queries;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
        
        
        
        
        
        
        

    public List<String[]> getVersion() throws DAOException {
       try {
            PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "queryName,dateCreation,queryVersion FROM"
                    + " Query query,QueryVersion queryversion "
                    +"query.queryID=queryVersion.queryID"
                    +"ORDER BY query.queryName");

            ResultSet rs = ps.executeQuery();
            List<String[]> queries = new ArrayList<String[]>();

            while (rs.next()) {
                queries.add(new String[]{rs.getString("queryName"), rs.getString("dateCreation"),rs.getString("queryVersion")});
            }
            ps.close();
            return queries;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    
    

           /* while (rs.next()) {
                queries.add(new Query(rs.getLong("queryID"),
            rs.getString("queryName"), rs.getString("description"),rs.getString("queryMaker"),rs.getDate("dateCreation")));
            }
            ps.close();
            return queries;

        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
        * */

    public void add(Query query) throws DAOException {
         try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO query(description, queryName, queryMaker) "
                    + "VALUES (?, ?, ?)");

            ps.setString(1, query.getDescription());
            ps.setString(2, query.getName());
            ps.setString(3, query.getQueryMaker());
            
            ps.execute();
            ps.close();
            

        } catch (SQLException ex) {
            //if (ex.getMessage().contains("Duplicate entry")) {
               // logger.error("An application named \"" + application.getName() + "\" already exists.");
                //throw new DAOException("An application named \"" + application.getName() + "\" already exists.");
           // } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
         
        }
    
      public void addVersion(QueryVersion version, Query query) throws DAOException {
         try {
           
            PreparedStatement ps2 = connection.prepareStatement(
                    "INSERT INTO queryversion(queryVersion, queryID, body) "
                    + "VALUES (?, ?, ?)");
            ps2.setString(1,version.getQueryVersion());
            ps2.setLong(2,query.getQueryID());
            ps2.setString(3,version.getBody());
            ps2.execute();
            ps2.close();

        } catch (SQLException ex) {
            //if (ex.getMessage().contains("Duplicate entry")) {
               // logger.error("An application named \"" + application.getName() + "\" already exists.");
                //throw new DAOException("An application named \"" + application.getName() + "\" already exists.");
           // } else {
                logger.error(ex);
                throw new DAOException(ex);
            }
         
        }
    }
        
    
    

    
    
    
    

