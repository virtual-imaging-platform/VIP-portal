/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.server.dao.mysql;

import fr.insalyon.creatis.vip.core.client.bean.TermsOfUse;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.TermsUseDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Nouha Boujelben
 */
public class TermsUseData implements TermsUseDAO {

    private final static Logger logger = Logger.getLogger(TermsUseData.class);
    private Connection connection;

    public TermsUseData() throws DAOException {
        connection = PlatformConnection.getInstance().getConnection();
    }

    @Override
    public void add(TermsOfUse termsOfUse) throws DAOException {

        try {

            PreparedStatement ps = connection.prepareStatement("INSERT INTO VIPTermsOfuse(date,text) "
                    + "VALUES (?, ?)");
            ps.setTimestamp(1, termsOfUse.getDate());
            ps.setString(2, termsOfUse.getText());
            ps.execute();
            ps.close();


        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public Timestamp getLastUpdateTermsOfUse() throws DAOException {
        try {
            Timestamp date=null;
            PreparedStatement ps = connection.prepareStatement("Select date From VIPTermsOfuse ORDER BY idTermsOfuse DESC " +
              "LIMIT 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            date=rs.getTimestamp("date");
            }
            ps.close();
            
            return date;
            
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
        
    }
    
    
    
}
