package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.api.business.ApiRegisterUser;
import fr.insalyon.creatis.vip.api.controller.DTO.SignUpUserDTO;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.function.Supplier;

/**
 * @author KhalilKes
 */
@RestController
@RequestMapping("/register")
public class RegisterUserController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApiRegisterUser apiRegisterUser;

    /**
     *
     * @param currentUserSupplier
     * @param apiRegisterUser
     */
    @Autowired
    public RegisterUserController(Supplier<User> currentUserSupplier, ApiRegisterUser apiRegisterUser) {
        super(currentUserSupplier);
        this.apiRegisterUser = apiRegisterUser;
    }

    /**
     *
     * @param signUpUser
     * @return
     * @throws ApiException
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> signup(
            @RequestBody @Valid SignUpUserDTO signUpUser)
            throws ApiException {
        logMethodInvocation(logger,"signup", signUpUser.getEmail());
        User user = new User(signUpUser.getFirstName(),
                signUpUser.getLastName(),
                signUpUser.getEmail(),
                signUpUser.getInstitution(),
                signUpUser.getPhone(),
                signUpUser.getLevel(),
                signUpUser.getCountryCode()
                );
        user.setRegistration(new Date());
        user.setLastLogin(new Date());
        this.apiRegisterUser.signup(user, signUpUser.getComments(), signUpUser.getAccountTypes());
        return new ResponseEntity("registered", HttpStatus.OK);
    }


}
