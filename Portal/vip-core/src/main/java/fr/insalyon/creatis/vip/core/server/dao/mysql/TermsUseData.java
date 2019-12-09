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

    public TermsUseData(Connection connection) throws DAOException {
        this.connection = connection;
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
