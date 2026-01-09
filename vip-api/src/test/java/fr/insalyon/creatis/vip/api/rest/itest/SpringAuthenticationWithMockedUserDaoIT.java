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
package fr.insalyon.creatis.vip.api.rest.itest;

import fr.insalyon.creatis.vip.core.server.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT;
import fr.insalyon.creatis.vip.api.tools.spring.ApikeyRequestPostProcessor;
import fr.insalyon.creatis.vip.core.server.dao.UserDAO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/*
 Needs to merge the mock of UserDAO into the root application context
 */
@ContextHierarchy(
    @ContextConfiguration(name="root", classes = SpringAuthenticationWithMockedUserDaoIT.TestContextConfiguration.class)
)
public class SpringAuthenticationWithMockedUserDaoIT extends BaseRestApiSpringIT {

    static class TestContextConfiguration {
        @Bean
        @Primary
        public UserDAO mockUser() {
            return Mockito.mock(UserDAO.class);
        }
    }

    /**
     * Test that an unexpected runtime exception ends up with a clean AuthenticationError
     */
    @Test
    public void authenticationWithCoreKo() throws Exception {
        Mockito.when(getUserDAO().getUserByApikey("apikeyvalue"))
                .thenThrow(new RuntimeException("hey hey"));
        mockMvc.perform(get("/rest/wrongUrl")
                .with(ApikeyRequestPostProcessor.apikey("testapikey", "apikeyvalue")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(ApiError.AUTHENTICATION_ERROR.getCode()));
    }
}
