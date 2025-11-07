package fr.insalyon.creatis.vip.application.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.insalyon.creatis.vip.application.client.bean.Application;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApplicationController {
    @GetMapping("/applications")
    public List<Application> getSession() {
        List<Application> apps = new ArrayList<>();
        apps.add(new Application("test", ""));
        return apps;
    }
}
