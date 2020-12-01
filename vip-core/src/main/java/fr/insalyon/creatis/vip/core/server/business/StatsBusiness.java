package fr.insalyon.creatis.vip.core.server.business;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class StatsBusiness {

    private UserDAO userDAO;

    public StatsBusiness(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public Long getUsersRegisteredNumber(UserSearchCriteria searchCriteria)
            throws BusinessException {
        try {
            return userDAO.countUsers(searchCriteria);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<User> getUsersRegistered(UserSearchCriteria searchCriteria)
            throws BusinessException {
        try {
            return userDAO.searchUsers(searchCriteria);
        } catch (DAOException ex) {
            throw new BusinessException(ex);
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
