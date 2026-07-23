package fr.insalyon.creatis.vip.application.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import fr.insalyon.creatis.vip.application.server.business.BoutiquesBusiness;
import fr.insalyon.creatis.vip.core.client.VipException;

/**
 * Permission : only developers can check a boutiques file
 */

@RestController
@RequestMapping("/boutiques")
public class BoutiquesController {

    private final BoutiquesBusiness boutiquesBusiness;

    @Autowired
    public BoutiquesController(BoutiquesBusiness boutiquesBusiness) {
        this.boutiquesBusiness = boutiquesBusiness;
    }

    
    @PostMapping(value = "check")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void check(@RequestBody String descriptorJson) throws VipException {
        boutiquesBusiness.validateBoutiquesString(descriptorJson);
    }

}
