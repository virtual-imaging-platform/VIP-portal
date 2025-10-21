package fr.insalyon.creatis.vip.application.server.controller;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Max;

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

import fr.insalyon.creatis.vip.application.models.Application;
import fr.insalyon.creatis.vip.application.server.business.ApplicationBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController()
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationBusiness applicationBusiness;

    @Autowired
    public ApplicationController(ApplicationBusiness applicationBusiness) {
        this.applicationBusiness = applicationBusiness;
    }

    @GetMapping
    public PrecisePage<Application> listApplications(@RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity, @RequestParam Optional<String> group) throws VipException {
        return applicationBusiness.get(offset, quantity, group.orElse(null));
    }

    @GetMapping(value = "{id}")
    public Application getApplication(@PathVariable String id) throws VipException {
        Application app = applicationBusiness.get(id);

        if (app == null) {
            throw new VipException(DefaultError.NOT_FOUND, id);
        } else {
            return app;
        }
    }

    @DeleteMapping(value = "{id}")
    public void deleteApplication(@PathVariable String id) throws VipException {
        applicationBusiness.remove(id);
    }

    @PutMapping(value = "{id}")
    public Application createOrUpdateApplication(@PathVariable String id, @RequestBody @Valid Application app)
            throws VipException {
        if ( ! id.equals(app.getName())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, id, "Application name do not match!");
        } else {
            Application existingApp = applicationBusiness.getApplication(id);
            if (existingApp == null) {
                applicationBusiness.add(app);
            } else {
                applicationBusiness.update(app);
            }
            return applicationBusiness.getApplication(id);
        }
    }

    @PostMapping()
    public Application createApplication(@RequestBody @Valid Application app) throws VipException {
        return createOrUpdateApplication(app.getName(), app);
    }
}
