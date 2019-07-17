/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api.rest.controller;

import fr.insalyon.creatis.vip.api.business.*;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by abonnet on 7/4/19.
 */
@RestController
@RequestMapping("/externalPlatforms")
public class ExternalPlatformController {

    private static final Logger logger = Logger.getLogger(ExternalPlatformController.class);

    private ExternalPlatformBusiness externalPlatformBusiness;

    @Autowired
    public ExternalPlatformController(ExternalPlatformBusiness externalPlatformBusiness) {
        this.externalPlatformBusiness = externalPlatformBusiness;
    }

    @GetMapping
    public List<ExternalPlatform> listExternalPlatforms() throws ApiException {
        ApiUtils.methodInvocationLog("listExternalPlatforms");
        try {
            return externalPlatformBusiness.listAll();
        } catch (BusinessException e) {
            logger.error("Error listing all external platforms");
            throw new ApiException(e);
        }
    }
}
