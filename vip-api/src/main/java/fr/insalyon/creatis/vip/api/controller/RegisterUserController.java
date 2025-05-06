package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.api.business.ApiUserBusiness;
import fr.insalyon.creatis.vip.api.model.SignUpUserDTO;
import fr.insalyon.creatis.vip.api.exception.ApiException;
import fr.insalyon.creatis.vip.core.client.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;

/**
 * @author KhalilKes
 */
@RestController
@RequestMapping("/register")
public class RegisterUserController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApiUserBusiness apiUserBusiness;

    /**
     *
     * @param apiUserBusiness
     */
    @Autowired
    public RegisterUserController(ApiUserBusiness apiUserBusiness) {
        this.apiUserBusiness = apiUserBusiness;
    }

    /**
     *
     * @param signUpUser
     * @return ResponseEntity<String>
     * @throws ApiException
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpUserDTO signUpUser) throws ApiException {
        logMethodInvocation(logger,"signup", signUpUser.getEmail());
        User user = new User(signUpUser.getFirstName(),
                signUpUser.getLastName(),
                signUpUser.getEmail(),
                signUpUser.getInstitution(),
                signUpUser.getPassword(),
                signUpUser.getCountryCode(),
                null
                );
        user.setRegistration(new Date());
        user.setLastLogin(new Date());
        this.apiUserBusiness.signup(user, signUpUser.getComments());
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
