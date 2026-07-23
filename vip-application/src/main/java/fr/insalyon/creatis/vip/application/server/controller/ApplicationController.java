package fr.insalyon.creatis.vip.application.server.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
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
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Permissions:
 * - Users and Devs can see the applications of the groups they belong to
 * - Users can NOT create/edit/delete
 * - Devs can create/edit/delete applications of the private groups they belong to but...
 * - Devs can only associate groups at creation (not when editing)
 * - Admin can do everything
 */
@RestController()
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationBusiness applicationBusiness;
    private final AppVersionBusiness appVersionBusiness;

    @Autowired
    public ApplicationController(ApplicationBusiness applicationBusiness, AppVersionBusiness appVersionBusiness) {
        this.applicationBusiness = applicationBusiness;
        this.appVersionBusiness = appVersionBusiness;
    }

    @GetMapping
    public PrecisePage<Application> list(@RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity, @RequestParam Optional<String> group) throws VipException {
        return applicationBusiness.get(offset, quantity, group.orElse(null));
    }

    @GetMapping("/public")
    public List<Application> getPublic() throws VipException {
        return appVersionBusiness.getPublicApplications();
    }

    @GetMapping(value = "{id}")
    public Application get(@PathVariable String id) throws VipException {
        Application app = applicationBusiness.get(id);

        if (app == null) {
            throw new VipException(DefaultError.NOT_FOUND, Application.class.getSimpleName(), id);
        } else {
            return app;
        }
    }

    @DeleteMapping(value = "{id}")
    public void deleteApplication(@PathVariable String id) throws VipException {
        Application app = applicationBusiness.get(id);

        if (app == null) {
            throw new VipException(DefaultError.NOT_FOUND, Application.class.getSimpleName(), id);
        } else {
            applicationBusiness.remove(id);
        }
    }

    @PutMapping(value = "{id}")
    public Application createOrUpdate(@PathVariable String id, @RequestBody @Valid Application app)
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
    public Application create(@RequestBody @Valid Application app) throws VipException {
        return createOrUpdate(app.getName(), app);
    }
}
