package fr.insalyon.creatis.vip.api.tools.spring;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class BearerTokenRequestPostProcessor implements RequestPostProcessor {
    private String token;

    public BearerTokenRequestPostProcessor(String token) {
        this.token = token;
    }

    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        request.addHeader("Authorization", "Bearer " + token);
        return request;
    }
}
