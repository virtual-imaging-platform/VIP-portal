package fr.insalyon.creatis.vip.application.server.controller;

import javax.validation.constraints.Max;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.application.models.Tag;
import fr.insalyon.creatis.vip.application.server.business.TagBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.server.model.PrecisePage;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController()
@RequestMapping("/tags")
public class TagController {

    private final TagBusiness tagBusiness;

    @Autowired
    public TagController(TagBusiness tagBusiness) {
        this.tagBusiness = tagBusiness;
    }

    @GetMapping
    public PrecisePage<Tag> list(@RequestParam(defaultValue = "0") @PositiveOrZero int offset,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 50) int quantity) throws VipException {
        return tagBusiness.get(offset, quantity);
    }
}
