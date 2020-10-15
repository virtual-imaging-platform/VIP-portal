package fr.insalyon.creatis.vip.api.business;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.exception.SQLRuntimeException;
import fr.insalyon.creatis.vip.api.model.stats.StatUser;
import fr.insalyon.creatis.vip.api.model.stats.UsersList;
import fr.insalyon.creatis.vip.api.model.stats.UsersNumber;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.StatsBusiness;
import fr.insalyon.creatis.vip.core.server.business.StatsBusiness.UserSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class StatsApiBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final StatsBusiness statsBusiness;
    private final Supplier<Connection> connectionSupplier;

    @Autowired
    public StatsApiBusiness(
            StatsBusiness statsBusiness,
            Supplier<Connection> connectionSupplier) {
        this.statsBusiness = statsBusiness;
        this.connectionSupplier = connectionSupplier;
    }

    public UsersNumber getUsersRegisteredNumber(
            @Nullable String startDateString,
            @Nullable String endDateString) throws ApiException {
        // build search criteria
        UserSearchCriteria searchCriteria =
                new UserSearchCriteria()
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString));

        // do search
        Long usersRegisteredNumber;
        try(Connection connection = connectionSupplier.get()) {
            usersRegisteredNumber =
                    statsBusiness.getUsersRegisteredNumber(
                            searchCriteria, connection);
        } catch (SQLException ex) {
            logger.error("Error handling a connection", ex);
            throw new ApiException(ex);
        } catch (SQLRuntimeException | BusinessException ex) {
            throw new ApiException(ex);
        }
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


    public UsersList getAllUsers() throws ApiException {
        return getUsersList(new UserSearchCriteria());
    }

    public UsersList getAllUsersBetweenDate(
            @NotNull String startDateString) throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withRegistrationStart(parseDate(startDateString)));
    }

    public UsersList getAllUsersBetweenDate(
            @NotNull String startDateString, @NotNull String endDateString)
            throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString)));
    }

    public UsersList getAllUsersFromCountry(
            @NotNull String country) throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country)));
    }

    public UsersList getAllUsersFromCountryFromDate(
            @NotNull String country, @NotNull String startDateString)
            throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withRegistrationStart(parseDate(startDateString)));
    }
    
    public UsersList getAllUsersFromCountryBetweenDate(
            @NotNull String country, @NotNull String startDateString,
            @NotNull String endDateString) throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString)));
    }

    public UsersList getAllUsersFromInstitution(
            @NotNull String institution) throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withInstitution(institution));
    }

    public UsersList getAllUsersFromInstitutionFromDate(
            @NotNull String institution, @NotNull String startDateString)
            throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withInstitution(institution)
                .withRegistrationStart(parseDate(startDateString)));
    }

    public UsersList getAllUsersFromInstitutionBetweenDate(
            @NotNull String institution, @NotNull String startDateString,
            @NotNull String endDateString) throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withInstitution(institution)
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString)));
    }

    public UsersList getAllUsersFromCountryAndInstitution(
            @NotNull String country, @NotNull String institution)
            throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withInstitution(institution));
    }

    public UsersList getAllUsersFromCountryAndInstitutionFromDate(
            @NotNull String country, @NotNull String institution,
            @NotNull String startDateString) throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withInstitution(institution)
                .withRegistrationStart(parseDate(startDateString)));
    }

    public UsersList getAllUsersFromCountryAndInstitutionBetweenDate(
            @NotNull String country, @NotNull String institution,
            @NotNull String startDateString, @NotNull String endDateString)
            throws ApiException {
        return getUsersList(new UserSearchCriteria()
                .withCountry(getCountry(country))
                .withInstitution(institution)
                .withRegistrationStart(parseDate(startDateString))
                .withRegistrationEnd(parseDate(endDateString)));
    }

    private UsersList getUsersList(
            @NotNull UserSearchCriteria searchCriteria) throws ApiException {

        List<User> users;
        try(Connection connection = connectionSupplier.get()) {
            users = statsBusiness.getUsersRegistered(
                    searchCriteria, connection);
        } catch (SQLException ex) {
            logger.error("Error handling a connection", ex);
            throw new ApiException(ex);
        } catch (SQLRuntimeException | BusinessException ex) {
            throw new ApiException(ex);
        }
        List<StatUser> statUsers = users
                .stream()
                .map(this::mapVipUserToStatUser)
                .collect(Collectors.toList());
        return new UsersList(statUsers);
    }

    @NotNull private StatUser mapVipUserToStatUser(@NotNull User vipUser) {
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

    @NotNull private CountryCode getCountry(String countryString) throws ApiException {
        CountryCode country = CountryCode.searchIgnoreCase(countryString);
        if (country == null) {
            logger.error("Wrong country {}", countryString);
            throw new ApiException(ApiError.COUNTRY_UNKNOWN, countryString);
        }
        return country;
    }

    private final String DATE_TIME_FORMAT = "dd-MM-yyyy";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    @Nullable private LocalDate parseDate(@Nullable String dateString) throws ApiException {
        if (dateString == null) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            logger.error("Wrong date format for {} (required : {})", dateString, DATE_TIME_FORMAT);
            throw new ApiException(ApiError.WRONG_DATE_FORMAT, e, dateString, DATE_TIME_FORMAT);
        }
    }
}
