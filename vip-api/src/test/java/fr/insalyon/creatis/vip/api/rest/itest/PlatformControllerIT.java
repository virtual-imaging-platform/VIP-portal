package fr.insalyon.creatis.vip.api.rest.itest;

import fr.insalyon.creatis.vip.core.server.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.core.server.model.Module;
import fr.insalyon.creatis.vip.core.server.model.SupportedTransferProtocol;
import fr.insalyon.creatis.vip.api.rest.config.BaseWebSpringIT;
import fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.*;
import static fr.insalyon.creatis.vip.api.data.ErrorCodeAndMessageTestUtils.jsonCorrespondsToErrorCodeAndMessage;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test method on platform path
 */
public class PlatformControllerIT extends BaseWebSpringIT {

    @Test
    public void platformShouldNotBeSecured() throws Exception {
        mockMvc.perform(get("/rest/platform"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPlatformProperties() throws Exception {
        // the test properties are set in BaseVIPSpringIT (with @TestPropertySource)
        mockMvc.perform(get("/rest/platform"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.platformName")
                        .value(TEST_PLATFORM_NAME))
                .andExpect(jsonPath("$.platformDescription")
                        .value(TEST_PLATFORM_DESCRIPTION))
                .andExpect(jsonPath("$.email")
                        .value(TEST_PLATFORM_EMAIL))
                .andExpect(jsonPath("$.supportedTransferProtocols[*]",
                        isArray(TEST_SUPPORTED_PROTOCOLS, SupportedTransferProtocol::toValue)))
                .andExpect(jsonPath("$.supportedModules[*]",
                        isArray(TEST_SUPPORTED_MODULES, Module::getLabel)))
                .andExpect(jsonPath("$.defaultLimitListExecutions")
                        .value(Integer.valueOf(TEST_DEFAULT_LIST_LIMIT)))
                .andExpect(jsonPath("$.unsupportedMethods[*]",
                        isArray(TEST_UNSUPPORTED_METHOD, String::toString)))
                .andExpect(jsonPath("$.supportedAPIVersion")
                        .value(TEST_SUPPORTED_API_VERSION))
                .andExpect(jsonPath("$.maxSizeDirectTransfer")
                        .value(Integer.valueOf(TEST_DATA_MAX_SIZE)))
                // it should be a long, be the test value being small it's actually an int
                .andExpect(jsonPath("$.APIErrorCodesAndMessages[*]",
                        hasSize(ApiError.values().length + ApplicationError.values().length)))
                .andExpect(jsonPath("$.APIErrorCodesAndMessages[*]",
                    hasItems(
                        jsonCorrespondsToErrorCodeAndMessage(ApiError.GENERIC_API_ERROR),
                        jsonCorrespondsToErrorCodeAndMessage(ApiError.NOT_ALLOWED_TO_USE_PIPELINE),
                        jsonCorrespondsToErrorCodeAndMessage(ApplicationError.USER_MAX_EXECS),
                        jsonCorrespondsToErrorCodeAndMessage(ApiError.BAD_CREDENTIALS),
                        jsonCorrespondsToErrorCodeAndMessage(ApiError.INSUFFICIENT_AUTH.getCode(), "The error message for 'insufficient auth' cannot be known in advance"))));

    }

    private <T> Matcher<Collection<String>> isArray(T[] values, Function<T,String> mapper) {
        // magic java 8 to convert enum array to string array
        String[] enumAsStringArray = Arrays.stream(values).map(mapper).toArray(String[]::new);
        return allOf(
                Matchers.<String>hasSize(values.length),
                Matchers.containsInAnyOrder(enumAsStringArray) );
    }
}
