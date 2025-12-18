package fr.insalyon.creatis.vip.api.tools.spring;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class ApikeyRequestPostProcessor implements RequestPostProcessor {
    private String apikeyHeader, apikeyValue;

    public ApikeyRequestPostProcessor(String apikeyHeader, String apikeyValue) {
        this.apikeyHeader = apikeyHeader;
        this.apikeyValue = apikeyValue;
    }

    public static RequestPostProcessor apikey(String apikeyHeader, String apikeyValue) {
        return new ApikeyRequestPostProcessor(apikeyHeader, apikeyValue);
    }

    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        request.addHeader(apikeyHeader, apikeyValue);
        return request;
    }
}
