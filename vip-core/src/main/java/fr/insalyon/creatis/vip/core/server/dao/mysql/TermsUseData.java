package fr.insalyon.creatis.vip.core.server.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.models.TermsOfUse;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.TermsUseDAO;

@Repository
@Transactional
public class TermsUseData extends JdbcDaoSupport implements TermsUseDAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void useDataSource(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void add(TermsOfUse termsOfUse) throws DAOException {

        try {

            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO VIPTermsOfUse(date) "
                    + "VALUES (?)");
            ps.setTimestamp(1, termsOfUse.getDate());
            ps.execute();
            ps.close();


        } catch (SQLException ex) {
            logger.error("Error adding terms of use {}", termsOfUse.getDate(), ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public Timestamp getLastUpdateTermsOfUse() throws DAOException {
        try {
            Timestamp date=null;
            PreparedStatement ps = getConnection().prepareStatement("SELECT date FROM VIPTermsOfUse ORDER BY id DESC " +
              "LIMIT 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            date=rs.getTimestamp("date");
            }
            ps.close();

            return date;

        } catch (SQLException ex) {
            logger.error("Error getting last terms of use", ex);
            throw new DAOException(ex);
        }

    }



}
