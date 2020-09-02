package com.myproject.springboot.logging.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
//    @Autowired
//    private SsoService ssoService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
            ServletException, IOException {
        String ssoToken = request.getHeader("sso-token");
        
        // TODO: DUMMY
        String userName = "TEST_USER";
        ssoToken="ST-12345";
        log.debug("auth request");
        this.authenticateRequest(userName, ssoToken);
        chain.doFilter(request, response);
    }

    private void authenticateRequest(String username, String ssoToken) {
        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(ssoToken)) {
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(username, 
                    new SsoAuthenticationToken(username,ssoToken));
            authentication.setAuthenticated(true);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

    }

    public TokenAuthenticationFilter() {
    }
}
