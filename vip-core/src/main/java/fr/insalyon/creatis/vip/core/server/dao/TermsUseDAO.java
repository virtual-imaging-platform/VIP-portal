package fr.insalyon.creatis.vip.core.server.dao;

import java.sql.Timestamp;

import fr.insalyon.creatis.vip.core.models.TermsOfUse;

public interface TermsUseDAO {

    public void add(TermsOfUse termsOfUse) throws DAOException;

    public Timestamp getLastUpdateTermsOfUse() throws DAOException;
}
