package com.myproject.springboot.logging.security;

import lombok.Data;

@Data
public class SsoAuthenticationToken {

    private String ssoToken;
    private String name;
    
    public SsoAuthenticationToken(String name, String ssoToken) {
        this.name = name;
        this.ssoToken = ssoToken;
    }


    public SsoAuthenticationToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }
}
