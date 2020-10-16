package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class StatsBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static class UserSearchCriteria {
        private LocalDate registrationStart;
        private LocalDate registrationEnd;
        private CountryCode country;
        private String institution;

        public UserSearchCriteria withRegistrationStart(LocalDate startDate) {
            this.registrationStart = startDate;
            return this;
        }

        public UserSearchCriteria withRegistrationEnd(LocalDate endDate) {
            this.registrationEnd = endDate;
            return this;
        }

        public UserSearchCriteria withCountry(CountryCode country) {
            this.country = country;
            return this;
        }

        public UserSearchCriteria withInstitution(String institution) {
            this.institution = institution;
            return this;
        }

        public LocalDate getRegistrationStart() {
            return registrationStart;
        }

        public LocalDate getRegistrationEnd() {
            return registrationEnd;
        }

        public CountryCode getCountry() {
            return country;
        }

        public String getInstitution() {
            return institution;
        }
    }

    public Long getUsersRegisteredNumber(
            UserSearchCriteria searchCriteria, Connection connection)
            throws BusinessException {
        try {
            UserDAO userDAO = CoreDAOFactory.getDAOFactory()
                    .getUserDAO(connection);
            return userDAO.countUsers(searchCriteria);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<User> getUsersRegistered(
            UserSearchCriteria searchCriteria,Connection connection)
            throws BusinessException {
        try {
            UserDAO userDAO = CoreDAOFactory.getDAOFactory()
                    .getUserDAO(connection);
            return userDAO.searchUsers(searchCriteria);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }
}
