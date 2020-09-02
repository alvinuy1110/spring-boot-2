package com.myproject.springboot.logging.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.myproject.springboot.logging.security.SsoAuthenticationToken;

/**
 * Created by user on 14/08/20.
 */
public class MDCInsertingSecurityFilter implements Filter {

    private static final String MDC_KEY_USER = "otpp.username";
    private static final String MDC_KEY_TOKEN = "otpp.token";

    public MDCInsertingSecurityFilter() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        this.insertIntoMDC(request);

        try {
            chain.doFilter(request, response);
        }
        finally {
            this.clearMDC();
        }

    }

    void insertIntoMDC(ServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        // This could be the username
        Object principal = authentication.getPrincipal();

        Object credentials = authentication.getCredentials();
        if (credentials instanceof SsoAuthenticationToken) {
//            System.out.println("yes");
        }
        SsoAuthenticationToken ssoAuthenticationToken = (SsoAuthenticationToken) credentials;

        MDC.put(MDC_KEY_USER, ssoAuthenticationToken.getName());
        MDC.put(MDC_KEY_TOKEN, ssoAuthenticationToken.getSsoToken());

    }

    void clearMDC() {
        MDC.remove(MDC_KEY_USER);
        MDC.remove(MDC_KEY_TOKEN);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
