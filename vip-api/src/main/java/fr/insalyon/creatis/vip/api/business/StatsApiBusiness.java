package fr.insalyon.creatis.vip.api.business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.api.model.stats.StatUser;
import fr.insalyon.creatis.vip.api.model.stats.UsersList;
import fr.insalyon.creatis.vip.api.model.stats.UsersNumber;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.StatsBusiness;
import fr.insalyon.creatis.vip.core.server.business.StatsBusiness.UserSearchCriteria;

@Service
public class StatsApiBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final StatsBusiness statsBusiness;
    private final String dateTimeFormat = "dd-MM-yyyy";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);

    @Autowired
    public StatsApiBusiness(StatsBusiness statsBusiness) {
        this.statsBusiness = statsBusiness;
    }

    public UsersNumber getUsersRegisteredNumber(
            String startDateString, String endDateString) throws VipException {
        // build search criteria
        UserSearchCriteria searchCriteria =
                new UserSearchCriteria()
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString));

        // do search
        Long usersRegisteredNumber;
        usersRegisteredNumber = statsBusiness.getUsersRegisteredNumber(searchCriteria);
        // build response object
        LocalDate startDate = searchCriteria.getRegistrationStart();
        if (startDate == null) {
            startDate = LocalDate.of(2010, Month.JANUARY, 1);
        }
        LocalDateTime endDateTime = LocalDateTime.now();
        if (endDateString != null) {
            endDateTime = searchCriteria.getRegistrationEnd().atStartOfDay();
        }
        return new UsersNumber(startDate.atStartOfDay(), endDateTime, usersRegisteredNumber);
    }


    public UsersList getAllUsers() throws VipException {
        return getUsersList(new UserSearchCriteria());
    }

    public UsersList getAllUsersFromDate(String startDateString) throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withRegistrationStart(parseDate(startDateString)));
    }

    public UsersList getAllUsersBetweenDates(
            String startDateString, String endDateString) throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString)));
    }

    public UsersList getAllUsersFromCountry(String country) throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country)));
    }

    public UsersList getAllUsersFromCountryFromDate(
            String country, String startDateString) throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withRegistrationStart(parseDate(startDateString)));
    }
    
    public UsersList getAllUsersFromCountryBetweenDates(
            String country, String startDateString, String endDateString)
            throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString)));
    }

    public UsersList getAllUsersFromInstitution(
            String institution) throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withInstitution(institution));
    }

    public UsersList getAllUsersFromInstitutionFromDate(
            String institution, String startDateString) throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withInstitution(institution)
                .withRegistrationStart(parseDate(startDateString)));
    }

    public UsersList getAllUsersFromInstitutionBetweenDates(
            String institution, String startDateString, String endDateString)
            throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withInstitution(institution)
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString)));
    }

    public UsersList getAllUsersFromCountryAndInstitution(
            String country, String institution)
            throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withInstitution(institution));
    }

    public UsersList getAllUsersFromCountryAndInstitutionFromDate(
            String country, String institution, String startDateString)
            throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withInstitution(institution)
                .withRegistrationStart(parseDate(startDateString)));
    }

    public UsersList getAllUsersFromCountryAndInstitutionBetweenDates(
            String country, String institution, String startDateString,
            String endDateString) throws VipException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withInstitution(institution)
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString)));
    }

    private UsersList getUsersList(UserSearchCriteria searchCriteria)
            throws VipException {

        List<User> users;
        users = statsBusiness.getUsersRegistered(searchCriteria);
        List<StatUser> statUsers = users
                .stream()
                .map(this::mapVipUserToStatUser)
                .collect(Collectors.toList());
        return new UsersList(statUsers);
    }

    private StatUser mapVipUserToStatUser(User vipUser) {
        return new StatUser(
                null,
                vipUser.getCountryCode().getCountryName(),
                vipUser.getLastLogin().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                vipUser.getRegistration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                null,
                vipUser.getInstitution(),
                null
        );
    }

    private CountryCode getCountry(String countryString) throws VipException {
        CountryCode country = CountryCode.searchIgnoreCase(countryString);
        if (country == null) {
            logger.error("Wrong country {}", countryString);
            throw new VipException(ApiError.COUNTRY_UNKNOWN, countryString);
        }
        return country;
    }

    private LocalDate parseDate(String dateString) throws VipException {
        if (dateString == null) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            logger.error("Wrong date format for {} (required : {})", dateString, dateTimeFormat);
            throw new VipException(ApiError.WRONG_DATE_FORMAT, e, dateString, dateTimeFormat);
        }
    }
}
