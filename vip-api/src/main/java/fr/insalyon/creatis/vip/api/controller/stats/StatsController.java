package fr.insalyon.creatis.vip.api.controller.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.api.business.StatsApiBusiness;
import fr.insalyon.creatis.vip.api.controller.ApiController;
import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.api.model.stats.UsersList;
import fr.insalyon.creatis.vip.api.model.stats.UsersNumber;
import fr.insalyon.creatis.vip.core.client.VipException;

@RestController
@RequestMapping("/statistics")
public class StatsController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final StatsApiBusiness statsApiBusiness;

    @Autowired
    public StatsController(
            StatsApiBusiness statsApiBusiness) {
        this.statsApiBusiness = statsApiBusiness;
    }

    @RequestMapping("/users")
    public UsersNumber getAllUsersRegisteredNumber() throws VipException {
        logMethodInvocation(logger, "getAllUsersRegisteredNumber");
        return statsApiBusiness.getUsersRegisteredNumber(null, null);
    }

    @RequestMapping("/users/{start}")
    public UsersNumber getUsersRegisteredNumberFromDate(
            @PathVariable("start") String startString) throws VipException {
        logMethodInvocation(logger, "getUsersRegisteredNumberFromDate", startString);
        return statsApiBusiness.getUsersRegisteredNumber(startString, null);
    }

    @RequestMapping("/users/{start}/{end}")
    public UsersNumber getUsersRegisteredNumberBetweenDates(
            @PathVariable("start") String startString,
            @PathVariable("end") String endString) throws VipException {
        logMethodInvocation(logger, "getUsersRegisteredNumberBetweenDates", startString, endString);
        return statsApiBusiness.getUsersRegisteredNumber(startString, endString);
    }

    @RequestMapping("/service/{service}")
    public UsersList getAllUsersForStats(
            @PathVariable String service) throws VipException {
        logMethodInvocation(logger, "getAllUsersForStats");
        assertService(service);
        return statsApiBusiness.getAllUsers();
    }

    @RequestMapping("/service/{service}/{start}")
    public UsersList getUsersForStatsFromDate(
            @PathVariable String service,
            @PathVariable("start") String startString) throws VipException {
        logMethodInvocation(logger, "getUsersForStatsFromDate", startString);
        assertService(service);
        return statsApiBusiness.getAllUsersFromDate(startString);
    }

    @RequestMapping("/service/{service}/{start}/{end}")
    public UsersList getUsersForStatsBetweenDates(
            @PathVariable String service,
            @PathVariable("start") String startString,
            @PathVariable("end") String endString) throws VipException {
        logMethodInvocation(logger, "getUsersForStatsBetweenDates", startString, endString);
        assertService(service);
        return statsApiBusiness.getAllUsersBetweenDates(startString, endString);
    }

    @RequestMapping("/country/{country}")
    public UsersList getAllUsersForStatsFromCountry(
            @PathVariable String country) throws VipException {
        logMethodInvocation(logger, "getAllUsersForStatsFromCountry", country);
        return statsApiBusiness.getAllUsersFromCountry(country);
    }

    @RequestMapping("/country/{country}/{service}")
    public UsersList getAllUsersForStatsFromCountryAndService(
            @PathVariable String country, @PathVariable String service)
            throws VipException {
        logMethodInvocation(logger, "getAllUsersForStatsFromCountryAndService", country);
        assertService(service);
        return statsApiBusiness.getAllUsersFromCountry(country);
    }

    @RequestMapping("/country/{country}/{service}/{start}")
    public UsersList getUsersForStatsFromCountryFromDate(
            @PathVariable String country,
            @PathVariable String service,
            @PathVariable("start") String startString) throws VipException {
        logMethodInvocation(logger, "getUsersForStatsFromCountryFromDate", country, startString);
        assertService(service);
        return statsApiBusiness.getAllUsersFromCountryFromDate(country, startString);
    }

    @RequestMapping("/country/{country}/{service}/{start}/{end}")
    public UsersList getUsersForStatsFromCountryBetweenDates(
            @PathVariable String country,
            @PathVariable String service,
            @PathVariable("start") String startString,
            @PathVariable("end") String endString) throws VipException {
        logMethodInvocation(logger, "getUsersForStatsFromCountryBetweenDates", country, startString, endString);
        assertService(service);
        return statsApiBusiness.getAllUsersFromCountryBetweenDates(country, startString, endString);
    }

    @RequestMapping("/institute/{institution}")
    public UsersList getAllUsersForStatsFromInstitution(
            @PathVariable String institution) throws VipException {
        logMethodInvocation(logger, "getAllUsersForStatsFromInstitution", institution);
        return statsApiBusiness.getAllUsersFromInstitution(institution);
    }

    @RequestMapping("/institute/{institution}/{service}")
    public UsersList getAllUsersForStatsFromInstitutionAndService(
            @PathVariable String institution, @PathVariable String service)
            throws VipException {
        logMethodInvocation(logger, "getAllUsersForStatsFromInstitutionAndService", institution);
        assertService(service);
        return statsApiBusiness.getAllUsersFromInstitution(institution);
    }

    @RequestMapping("/institute/{institution}/{service}/{start}")
    public UsersList getUsersForStatsFromInstitutionFromDate(
            @PathVariable String institution,
            @PathVariable String service,
            @PathVariable("start") String startString) throws VipException {
        logMethodInvocation(logger, "getUsersForStatsFromInstitutionFromDate", institution, startString);
        assertService(service);
        return statsApiBusiness.getAllUsersFromInstitutionFromDate(institution, startString);
    }

    @RequestMapping("/institute/{institution}/{service}/{start}/{end}")
    public UsersList getUsersForStatsFromInstitutionBetweenDates(
            @PathVariable String institution,
            @PathVariable String service,
            @PathVariable("start") String startString,
            @PathVariable("end") String endString) throws VipException {
        logMethodInvocation(logger, "getUsersForStatsFromInstitutionBetweenDates", institution, startString, endString);
        assertService(service);
        return statsApiBusiness.getAllUsersFromInstitutionBetweenDates(institution, startString, endString);
    }

    private void assertService(String service) throws VipException {
        if ( ! "vip".equals(service)) {
            logger.error("Looking for stats with wrong service : {}", service);
            throw new VipException(ApiError.WRONG_STAT_SERVICE, service);
        }
    }
}
