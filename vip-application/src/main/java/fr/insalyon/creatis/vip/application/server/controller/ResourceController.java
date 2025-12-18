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

import fr.insalyon.creatis.vip.application.models.Resource;
import fr.insalyon.creatis.vip.application.server.business.ResourceBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceBusiness resourceBusiness;

    @Autowired
    public ResourceController(ResourceBusiness resourceBusiness) {
        this.resourceBusiness = resourceBusiness;
    }

    @GetMapping
    public PrecisePage<Resource> list(@RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity, @RequestParam Optional<String> group) throws VipException {
        return resourceBusiness.get(offset, quantity, group.orElse(null));
    }

    @GetMapping(value = "{id}")
    public Resource get(@PathVariable String id) throws VipException {
        Resource resource = resourceBusiness.get(id);

        if (resource == null) {
            throw new VipException(DefaultError.NOT_FOUND, id);
        } else {
            return resource;
        }
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable String id) throws VipException {
        Resource resource = get(id);

        if (resource != null) {
            resourceBusiness.remove(resource);
        }
    }

    @PutMapping(value = "{id}")
    public Resource createOrUpdate(@PathVariable String id, @RequestBody @Valid Resource resource) throws VipException {
        if ( ! id.equals(resource.getName())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, id, "Resource name do not match!");
        } else {
            Resource existing = resourceBusiness.get(id);

            if (existing == null) {
                resourceBusiness.add(resource);
            } else {
                resourceBusiness.update(resource);
            }
            return resourceBusiness.get(id);
        }
    }

    @PostMapping
    public Resource create(@RequestBody @Valid Resource resource) throws VipException {
        return createOrUpdate(resource.getName(), resource);
    }
}
