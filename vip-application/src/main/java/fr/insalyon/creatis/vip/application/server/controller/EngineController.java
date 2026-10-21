package fr.insalyon.creatis.vip.application.server.controller;

import java.util.Optional;
import java.util.List;

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

import fr.insalyon.creatis.vip.application.models.Engine;
import fr.insalyon.creatis.vip.application.server.business.EngineBusiness;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/engines")
public class EngineController {

    private final EngineBusiness engineBusiness;

    @Autowired
    public EngineController(EngineBusiness engineBusiness) {
        this.engineBusiness = engineBusiness;
    }

    @GetMapping(value = "{name}")
    public Engine get(@PathVariable String name) throws VipException {
        Engine engine = engineBusiness.get(name);
        if (engine == null) {
            throw new VipException(DefaultError.NOT_FOUND, Engine.class.getSimpleName(), name);
        } else {
            return engine;
        }
    }

    @GetMapping
    public PrecisePage<Engine> list(
            @RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity) throws VipException {
        return engineBusiness.getAll(offset, quantity);
    }

    @PutMapping(value = "{name}")
    public Engine upsert(@PathVariable String name, @RequestBody @Valid Engine engine) throws VipException {
        if (!name.equals(engine.getName())) {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, name, "Engine name do not match!");
        } else {
            Engine existing = engineBusiness.get(name);
            if (existing == null) {
                engineBusiness.add(engine);
            } else {
                engineBusiness.update(engine);
            }
            return engineBusiness.get(name);
        }
    }

    @PostMapping
    public Engine create(@RequestBody @Valid Engine engine) throws VipException {
        Engine existing = engineBusiness.get(engine.getName());
        if (existing == null) {
            engineBusiness.add(engine);
            return engineBusiness.get(engine.getName());
        } else {
            throw new VipException(DefaultError.BAD_INPUT_FIELD, engine.getName(), "Engine already exists!");
        }
    }

    @DeleteMapping(value = "{name}")
    public void delete(@PathVariable String name) throws VipException {
        Engine engine = get(name);
        if (engine != null) {
            engineBusiness.remove(name);
        }
    }

}
