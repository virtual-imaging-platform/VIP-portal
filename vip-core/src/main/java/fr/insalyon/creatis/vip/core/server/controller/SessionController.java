package fr.insalyon.creatis.vip.core.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.SessionBusiness;
import fr.insalyon.creatis.vip.core.server.model.AuthenticationCredentials;
import fr.insalyon.creatis.vip.core.server.model.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {

    private final SessionBusiness sessionBusiness;

    @Autowired
    public SessionController(SessionBusiness sessionBusiness) {
        this.sessionBusiness = sessionBusiness;
    }

    @GetMapping()
    public Session getSession() {
        return sessionBusiness.getSession();
    }

    @PostMapping()
    public Session createSession(@RequestBody @Valid AuthenticationCredentials credentials, HttpServletRequest request, HttpServletResponse response)
            throws BusinessException {
        return sessionBusiness.signin(credentials, request, response);
    }

    @DeleteMapping
    public void deleteSession(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        sessionBusiness.signout(request, response);
    }
}
