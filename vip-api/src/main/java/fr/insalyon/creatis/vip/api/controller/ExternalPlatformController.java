package fr.insalyon.creatis.vip.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;

@RestController
@RequestMapping("/externalPlatforms")
public class ExternalPlatformController extends ApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ExternalPlatformBusiness externalPlatformBusiness;

    @Autowired
    public ExternalPlatformController(
            ExternalPlatformBusiness externalPlatformBusiness) {
        this.externalPlatformBusiness = externalPlatformBusiness;
    }

    @GetMapping
    public List<ExternalPlatform> listExternalPlatforms() throws VipException {
        logMethodInvocation(logger,"listExternalPlatforms");
        return externalPlatformBusiness.listAll();
    }
}
