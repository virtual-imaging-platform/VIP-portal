package fr.insalyon.creatis.vip.api;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.web.filter.UrlHandlerFilter;

import java.io.IOException;

/**
 * Accepts REST URLs with a trailing slash (e.g. /rest/pipelines/ used by Shanoir)
 * by stripping the slash before Spring Security and the DispatcherServlet see
 * the request.
 * Replaces PathMatchConfigurer#setUseTrailingSlashMatch, removed in Spring 7.
 * Must be mapped before springSecurityFilterChain in web.xml.
 */
public class RestTrailingSlashFilter implements Filter {

    private final Filter delegate = UrlHandlerFilter
            .trailingSlashHandler("/rest/**")
            .wrapRequest()
            .build();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        delegate.doFilter(request, response, chain);
    }
}