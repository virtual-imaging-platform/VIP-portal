package fr.insalyon.creatis.vip.core.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;

@RestController
public class SessionController {
    class Session {
        public String username;
        public String password;
        public UserLevel level;
    }
    @GetMapping("/session")
    public Session getSession() {
        Session session = new Session();
        session.username = "test";
        session.password = null;
        session.level = UserLevel.Beginner;
        return session;
    }
}
