/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.creatis.vip.core.server.dao;

import fr.insalyon.creatis.vip.core.client.bean.TermsOfUse;
import java.sql.Timestamp;

/**
 *
 * @author Nouha Boujelben
 */
public interface TermsUseDAO {

    public void add(TermsOfUse termsOfUse) throws DAOException;

    public Timestamp getLastUpdateTermsOfUse() throws DAOException;
}
