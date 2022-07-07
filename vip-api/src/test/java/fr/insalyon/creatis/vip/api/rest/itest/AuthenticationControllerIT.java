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

import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.data.UserTestUtils;
import fr.insalyon.creatis.vip.api.rest.config.BaseVIPSpringIT;
import fr.insalyon.creatis.vip.core.server.business.ConfigurationBusiness;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static fr.insalyon.creatis.vip.api.data.AuthenticationInfoTestUtils.jsonCorrespondsToAuthenticationInfo;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_APIKEY_HEADER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 8/21/17.
 */
@Disabled
public class AuthenticationControllerIT extends BaseVIPSpringIT {

    @Test
    public void okAuthentication() throws Exception {
        ConfigurationBusiness configBness = getConfigurationBusiness();
        String email = UserTestUtils.baseUser1.getEmail();
        String apikey="plopplop";
        when(configBness.signin(anyString(), anyString()))
            .thenThrow(new RuntimeException());
        doReturn(UserTestUtils.baseUser1)
            .when(configBness)
            .signin(eq(email),eq("coucou"));
        when(configBness.getUserApikey(eq(email))).thenReturn(apikey);
        mockMvc.perform(
                post("/rest/authenticate")
                        .contentType("application/json")
                        .content(getResourceAsString("jsonObjects/user-credentials.json")))
                .andDo(print())
                .andExpect(jsonPath(
                        "$",
                        jsonCorrespondsToAuthenticationInfo(TEST_APIKEY_HEADER, apikey)
                ))
                .andExpect(status().isOk());
    }


    @Test
    public void missingInfoAuthentication() throws Exception {
        mockMvc.perform(
                post("/rest/authenticate")
                        .contentType("application/json")
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode")
                        .value(ApiError.INPUT_FIELD_NOT_VALID.getCode()));;
    }

}
