package fr.insalyon.creatis.vip.application.server.controller;

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

import fr.insalyon.creatis.vip.application.models.AppVersion;
import fr.insalyon.creatis.vip.application.server.business.AppVersionBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Permissions:
 * - same as with Application. If someone can do something on an Application, he can do the same thing on its AppVersion
 * - Devs can only associate resources on creation, not on edition
 * - TODO : Tags permissions to be improved
 */
@RestController
@RequestMapping("/applications")
public class AppVersionController {

    private final AppVersionBusiness appVersionBusiness;

    @Autowired
    public AppVersionController(AppVersionBusiness appVersionBusiness) {
        this.appVersionBusiness = appVersionBusiness;
    }

    @GetMapping(value = "{appId}/versions")
    public PrecisePage<AppVersion> list(@RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity, @PathVariable String appId)
            throws VipException {
        return appVersionBusiness.get(offset, quantity, appId);
    }

    @GetMapping(value = "{appId}/versions/{id}")
    public AppVersion get(@PathVariable String appId, @PathVariable String id) throws VipException {
        AppVersion version = appVersionBusiness.get(appId, id);

        if (version == null) {
            throw new VipException(DefaultError.NOT_FOUND, AppVersion.class.getSimpleName(), id);
        } else {
            return version;
        }
    }

    @DeleteMapping(value = "{appId}/versions/{id}")
    public void deleteVersion(@PathVariable String appId, @PathVariable String id) throws VipException {
        AppVersion version = appVersionBusiness.get(appId, id);

        if (version == null) {
            throw new VipException(DefaultError.NOT_FOUND, AppVersion.class.getSimpleName(), id);
        } else {
            appVersionBusiness.remove(appId, id);
        }
    }

    @PutMapping(value = "{appId}/versions/{id}")
    public AppVersion createOrUpdate(@PathVariable String appId, @PathVariable String id,
            @RequestBody @Valid AppVersion version)
            throws VipException {
        if (!appId.equals(version.getApplicationName())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, appId, "AppVersion application do not match!");
        }
        if (!id.equals(version.getVersion())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, id, "AppVersion version do not match!");
        } else {
            AppVersion existingVersion = appVersionBusiness.get(appId, id);

            if (existingVersion == null) {
                appVersionBusiness.add(version);
            } else {
                appVersionBusiness.update(version);
            }
            return appVersionBusiness.get(appId, id);
        }
    }

    @PostMapping(value = "{appId}/versions")
    public AppVersion create(@PathVariable String appId, @RequestBody @Valid AppVersion version) throws VipException {
        return createOrUpdate(appId, version.getVersion(), version);
    }

    @GetMapping("versions")
    public PrecisePage<AppVersion> list(@RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity) throws VipException {
        return appVersionBusiness.get(offset, quantity, null);
    }

}
