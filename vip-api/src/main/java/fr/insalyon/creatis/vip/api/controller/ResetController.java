package fr.insalyon.creatis.vip.api.controller;

import fr.insalyon.creatis.vip.api.business.ApiUserBusiness;
import fr.insalyon.creatis.vip.api.exception.ApiException;

import fr.insalyon.creatis.vip.api.model.EmailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/reset-code")
public class ResetController extends ApiController {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        private final ApiUserBusiness apiUserBusiness;

        @Autowired
        public ResetController(ApiUserBusiness apiUserBusiness) { this.apiUserBusiness = apiUserBusiness; }

        @RequestMapping(method = RequestMethod.POST)
        public void resetPassword(@RequestBody @Valid EmailDTO email) throws ApiException {
            logMethodInvocation(logger, "resetCode", email.getEmail());
            apiUserBusiness.resetCode(email.getEmail());
            logger.info("reset code of  " + email.getEmail());
        }
}
