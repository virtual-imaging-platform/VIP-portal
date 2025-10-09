package fr.insalyon.creatis.vip.core.server.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;

@Service
@Transactional
public class StatsBusiness {

    private UserDAO userDAO;

    public StatsBusiness(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public Long getUsersRegisteredNumber(UserSearchCriteria searchCriteria)
            throws VipException {
        try {
            return userDAO.countUsers(searchCriteria);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

    public List<User> getUsersRegistered(UserSearchCriteria searchCriteria)
            throws VipException {
        try {
            return userDAO.searchUsers(searchCriteria);
        } catch (DAOException ex) {
            throw new VipException(ex);
        }
    }

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
}
