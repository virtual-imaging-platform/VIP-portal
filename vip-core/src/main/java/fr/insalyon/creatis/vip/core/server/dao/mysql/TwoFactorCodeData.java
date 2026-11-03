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
public class TwoFactorCodeData extends JdbcDaoSupport implements TwoFactorCodeDAO {

    @Autowired
    public TwoFactorCodeData(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void save(String email, String codeHash, Timestamp expireAT) throws DAOException {
        try {
            getJdbcTemplate().update(
                    "UPDATE VIPUsers SET two_factor_code_hash = ?, two_factor_expires_at = ?, two_factor_attempts = 0 WHERE email = ?",
                    codeHash, expireAT, email);
        } catch (DataAccessException ex) {
            logger.error("Error saving two factor code for email " + email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void delete(String email) throws DAOException {
        try {
            getJdbcTemplate().update(
                    "UPDATE VIPUsers SET two_factor_code_hash = NULL, two_factor_expires_at = NULL, two_factor_attempts = 0 WHERE email = ?",
                    email);
        } catch (DataAccessException ex) {
            logger.error("Error deleting two factor code for email " + email, ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public TwoFactorCode get(String email) throws DAOException {
        try {
            List<TwoFactorCode> codes = getJdbcTemplate().query(
                    "SELECT email, two_factor_code_hash, two_factor_expires_at, two_factor_attempts FROM VIPUsers "
                            + "WHERE email = ? AND two_factor_code_hash IS NOT NULL",
                    (rs, rowNum) -> new TwoFactorCode(
                            rs.getString("email"),
                            rs.getString("two_factor_code_hash"),
                            rs.getTimestamp("two_factor_expires_at"),
                            rs.getInt("two_factor_attempts")),
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
            getJdbcTemplate().update(
                    "UPDATE VIPUsers SET two_factor_attempts = two_factor_attempts + 1 WHERE email = ?",
                    email);
        } catch (DataAccessException ex) {
            logger.error("Error incrementing attempts for email " + email, ex);
            throw new DAOException(ex);
        }
    }
}