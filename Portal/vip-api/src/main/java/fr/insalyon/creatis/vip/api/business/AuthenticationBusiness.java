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
package fr.insalyon.creatis.vip.api.business;

import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.auth.AbstractAuthenticationService;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tristan Glatard
 */
public class AuthenticationBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApiContext apiContext;

    private final ConfigurationBusiness configurationBusiness;

    public AuthenticationBusiness(ApiContext apiContext) {
        this.apiContext = apiContext;
        this.configurationBusiness = new ConfigurationBusiness();
    }

    public void authenticateSession(
        String userName, String password, Connection connection)
        throws ApiException {

         try {
             //verify userName and password
             configurationBusiness.configure();
             User user = configurationBusiness
                 .signin(userName, password, connection);

             AbstractAuthenticationService.setVIPSession(
                 apiContext.getRequest(),
                 apiContext.getResponse(),
                 user,
                 connection);
         } catch (BusinessException ex) {
             throw new ApiException(ex);
         }
    }

    public void authenticateHTTP(String userName) throws ApiException {
        throw new ApiException("Not supported."); // will *not* be supported soon. This doesn't violate the API specification.
    }

    public void logout(Connection connection) throws ApiException {
         try {
             // authentication has been done, so user is present in apiContext
             configurationBusiness
                 .signout(apiContext.getUser().getEmail(), connection);
         } catch (BusinessException ex) {
             throw new ApiException(ex);
         }
    }

}
