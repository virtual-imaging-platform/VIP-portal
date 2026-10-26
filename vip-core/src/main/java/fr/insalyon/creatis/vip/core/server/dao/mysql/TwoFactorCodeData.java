package fr.insalyon.creatis.vip.core.server.dao.mysql;

import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.TwoFactorCodeDAO;
import fr.insalyon.creatis.vip.core.server.model.TwoFactorCode;

@Component
@Transactional
public class TwoFactorCodeData   extends JdbcDaoSupport implements TwoFactorCodeDAO
{

    
    @Autowired
    public TwoFactorCodeData(DataSource dataSource) {
        setDataSource(dataSource);

    }

    @Override
    public void save(String email, String codeHash, Timestamp expireAT) throws DAOException {
      try{
        getJdbcTemplate().update("DELETE FROM VIPTwoFactorCodes WHERE email = ?", email);
        getJdbcTemplate().update("INSERT INTO VIPTwoFactorCodes(email,code_hash,expires_at,attempts) VALUES(?, ?, ?, ?)", email, codeHash, expireAT, 0);
      } catch (DataAccessException ex) {
        logger.error("Error saving two factor code for email " + email, ex);
        throw new DAOException(ex);
      }
    }

    @Override
    public void delete(String email) throws DAOException {
        try {
            getJdbcTemplate().update("DELETE FROM VIPTwoFactorCodes WHERE email = ?", email);
        } catch (DataAccessException ex) {
            logger.error("Error deleting two factor code for email " + email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public TwoFactorCode get(String email) throws DAOException {
        try {
            List<TwoFactorCode> codes = getJdbcTemplate().query(
                    "SELECT email, code_hash, expires_at, attempts FROM VIPTwoFactorCodes WHERE email = ?",
                    (rs, rowNum) -> new TwoFactorCode(
                            rs.getString("email"),
                            rs.getString("code_hash"),
                            rs.getTimestamp("expires_at"),
                            rs.getInt("attempts")),
                    email);
            return codes.isEmpty() ? null : codes.get(0);
        } catch (DataAccessException ex) {
            logger.error("Error reading 2FA code for " + email, ex);
            throw new DAOException(ex);
        }
    }


    @Override
    public void incrementAttempts(String email) throws DAOException {
        try {
            getJdbcTemplate().update("UPDATE VIPTwoFactorCodes SET attempts = attempts + 1 WHERE email = ?", email);
        } catch (DataAccessException ex) {
            logger.error("Error incrementing attempts for email " + email, ex);
            throw new DAOException(ex);
        }
    }

}