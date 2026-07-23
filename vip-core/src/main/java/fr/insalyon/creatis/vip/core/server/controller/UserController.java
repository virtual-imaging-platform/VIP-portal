package fr.insalyon.creatis.vip.core.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.models.ActivationCode;
import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.models.UserAndPassword;
import fr.insalyon.creatis.vip.core.server.business.AuthenticationBusiness;
import fr.insalyon.creatis.vip.core.server.business.SessionBusiness;
import fr.insalyon.creatis.vip.core.server.business.UserBusiness;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import fr.insalyon.creatis.vip.core.server.model.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Permissions:
 * - An user can only see/edit/delete himself
 * - Admins can do everything (on the updatable field)
 *
 * User can see:
 * - id, first name, last name, email, level, institution, country code, registration time, last login time, ...
 * - max running simulation number, terms of use validation date, last publication update date, confirmed, groups, apikey
 *
 * Admin can see in addition:
 * - locked, folder
 *
 * On creation:
 * - first name, last name, email, institution, country code, groups (only public ones)
 * - password
 * - MUST BE ABSENT : id, level, dates, max running simulation number, confirmed
 *
 * Users can only edit:
 * - institution, country code
 * - groups : can join public group, and leave any group
 * - terms of use validation date, last publication update date set to NOW() through dedicated endpoints
 *
 * Admin can also edit :
 * - folder, accountLocked, first name, last name, email, confirmed, level, max running simulation
 *
 * About password and api key management:
 * - done through specific endpoints
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserBusiness userBusiness;
    private final AuthenticationBusiness authenticationBusiness;
    private final SessionBusiness sessionBusiness;

    @Autowired
    public UserController(UserBusiness userBusiness, AuthenticationBusiness authenticationBusiness,
            SessionBusiness sessionBusiness) {
        this.userBusiness = userBusiness;
        this.authenticationBusiness = authenticationBusiness;
        this.sessionBusiness = sessionBusiness;
    }

    @GetMapping
    public PrecisePage<User> list(@RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity) throws VipException {
    return userBusiness.getAll(offset, quantity);
    }

    @GetMapping(params = "q")
    public List<User> search(@RequestParam String q,
            @RequestParam(defaultValue = "50") @Positive @Max(value = 50) int limit) throws VipException {
        return userBusiness.searchUsers(q, limit);
    }

    @GetMapping(value = "me")
    public User getCurrentUser() throws VipException {
        return userBusiness.getCurrentUser();
    }


    @GetMapping(value = "{id}")
    public User get(@PathVariable String id) throws VipException {
        User user = userBusiness.get(id);

        if (user == null) {
            throw new VipException(DefaultError.NOT_FOUND, User.class.getSimpleName(), id);
        } else {
            return user;
        }
    }


    @DeleteMapping(value = "{id}")
    public void remove(@PathVariable String id) throws VipException {
        // existance of user is checked inside remove function
        userBusiness.remove(id, true);
    }

    @PutMapping(value = "{id}")
    public User update(@PathVariable String id, @RequestBody @Valid User user) throws VipException {
        if ( ! id.equals(user.getId())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, id, "User id do not match!");
        } else {
            userBusiness.update(user);
            return userBusiness.get(id);
        }
    }

    @PutMapping(value = "{id}/password")
    public void updatePassword(@PathVariable String id, @RequestBody @Valid UserAndPassword form) throws VipException {
        if (!id.equals(form.user.getId())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, "id", "User id do not match!");
        }
        userBusiness.updateUserPassword(id, form.password);
    }

    @PostMapping
    public User create(@RequestBody @Valid UserAndPassword form) throws VipException {

        logger.info("Signup request received: email='{}', country='{}', hasPassword={}, groupsCount={}",
                form.user.getEmail(), form.user.getCountryCode(), !form.password.isBlank(), form.user.getGroups() != null ? form.user.getGroups().size() :"no groups");

        if (form.user.getId() != null) {
            logger.warn("Signup rejected: id must be null for email='{}'", form.user.getEmail());
            throw new VipException(DefaultError.BAD_INPUT_FIELD, "id", "ID must be empty!");
        }
        // Set password on user object from the separate password field
        form.user.setPassword(form.password);
        // the returned data may be partial, but enough for the frontend to do it own stuff!
        User createdUser = authenticationBusiness.signup(form.user, form.comment);
        logger.info("Signup completed: email='{}', generatedId='{}'", createdUser.getEmail(), createdUser.getId());
        return createdUser;
    }

    @PutMapping(value = "{id}/activate")
    public Session activate(@PathVariable String id, @RequestBody @Valid ActivationCode activationCode,
            HttpServletRequest request, HttpServletResponse response) throws VipException {
        try {
            User user = authenticationBusiness.activate(id, activationCode.getCode());
            Session session = sessionBusiness.getSession(user);
            sessionBusiness.createLoginCookies(request, response, session);
            userBusiness.updateUserLastLogin(user.getEmail());
            return session;
        } catch (UnsupportedEncodingException e) {
            throw new VipException("Failed to create session after activation", e);
        }
    }
}
