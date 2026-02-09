package fr.insalyon.creatis.vip.api.rest.itest;

import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_DATA_MAX_SIZE;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_DEFAULT_LIST_LIMIT;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_PLATFORM_DESCRIPTION;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_PLATFORM_EMAIL;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_PLATFORM_NAME;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_SUPPORTED_API_VERSION;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_SUPPORTED_MODULES;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_SUPPORTED_PROTOCOLS;
import static fr.insalyon.creatis.vip.api.data.CarminAPITestConstants.TEST_UNSUPPORTED_METHOD;
import static fr.insalyon.creatis.vip.api.data.ErrorCodeAndMessageTestUtils.jsonCorrespondsToErrorCodeAndMessage;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import fr.insalyon.creatis.vip.api.exception.ApiError;
import fr.insalyon.creatis.vip.api.rest.config.BaseRestApiSpringIT;
import fr.insalyon.creatis.vip.core.client.DefaultError;
import fr.insalyon.creatis.vip.core.server.model.Module;
import fr.insalyon.creatis.vip.core.server.model.SupportedTransferProtocol;

/**
 * Test method on platform path
 */
public class PlatformControllerIT extends BaseRestApiSpringIT {

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
                        hasSize(ApiError.values().length + DefaultError.values().length)))
                .andExpect(jsonPath("$.APIErrorCodesAndMessages[*]",
                    hasItems(
                        jsonCorrespondsToErrorCodeAndMessage(DefaultError.GENERIC_ERROR),
                        jsonCorrespondsToErrorCodeAndMessage(ApiError.NOT_ALLOWED_TO_USE_PIPELINE),
                        jsonCorrespondsToErrorCodeAndMessage(DefaultError.BAD_CREDENTIALS),
                        jsonCorrespondsToErrorCodeAndMessage(DefaultError.INSUFFICIENT_AUTH))));

    }

    private <T> Matcher<Collection<String>> isArray(T[] values, Function<T,String> mapper) {
        // magic java 8 to convert enum array to string array
        String[] enumAsStringArray = Arrays.stream(values).map(mapper).toArray(String[]::new);
        return allOf(
                Matchers.<String>hasSize(values.length),
                Matchers.containsInAnyOrder(enumAsStringArray) );
    }
}
