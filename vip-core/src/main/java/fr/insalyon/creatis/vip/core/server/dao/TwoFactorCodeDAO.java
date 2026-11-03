package fr.insalyon.creatis.vip.core.server.dao;

import java.sql.Timestamp;
import fr.insalyon.creatis.vip.core.server.model.TwoFactorCode;

public interface TwoFactorCodeDAO {

    public void save(String email, String codeHash, Timestamp expireAT) throws DAOException;
    public void delete(String email) throws DAOException;
    public TwoFactorCode get(String email) throws DAOException;
    public void incrementAttempts(String email) throws DAOException;
    // public TwoFactorCode ResendCode(String codeHash) throws DAOException;

   
}