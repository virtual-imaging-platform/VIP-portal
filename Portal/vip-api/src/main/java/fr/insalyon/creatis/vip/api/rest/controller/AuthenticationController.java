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
import fr.insalyon.creatis.vip.api.exception.SQLRuntimeException;
import fr.insalyon.creatis.vip.api.rest.RestApiBusiness;
import fr.insalyon.creatis.vip.api.rest.model.*;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by abonnet on 8/21/17.
 */
@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    // although the controller is a singleton, these are proxies that always point on the current request
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    private RestApiBusiness restApiBusiness;

    @Autowired
    private Supplier<Connection> connectionSupplier;

    @RequestMapping(method = RequestMethod.POST)
    public AuthenticationInfo authenticate(
            @RequestBody AuthenticationCredentials authenticationCredentials) throws ApiException {
        // common stuff
        ApiUtils.methodInvocationLog("authenticate", authenticationCredentials.getUsername());
        // TODO : improve this apiContext stuff. Verify that it is initialized somewhere.
        // TODO : Do not call it "get" if it does not return anything
        restApiBusiness.getApiContext(httpServletRequest, false);
        // TODO verify the presence of credentials
        try(Connection connection = connectionSupplier.get()) {
            // business call
            return restApiBusiness
                .authenticate(authenticationCredentials, connection);
        } catch (SQLException | SQLRuntimeException ex) {
            throw new ApiException(ex);
        }
    }
}
